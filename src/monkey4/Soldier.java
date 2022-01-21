package monkey4;

import battlecode.common.*;

import java.nio.file.Path;

class Soldier {

    static MapLocation destination = null;
    static MapLocation spawn = null;
    static MapLocation enemy = null;
    static MapLocation nearestAlly = null;
    static MapLocation nearestEnemy = null;

    static boolean toLeadFarm = false;
    static boolean isBackup = false;

    static int sinceLastAttack = 0;
    static int visibleEnemies = 0;
    static int visibleAttackers = 0;
    static int visibleAllies = 0;
    static int attackersInRange = 0;

    enum TYPE {
        WATCH,
        ATTACK,
        RETREAT,
        FARM
    }

    // TODO if higher health make the soldier more aggressive?
    // TODO better chasing after enemies
    // TODO produce a better calculation of enemy priority

    static void setup(RobotController rc) throws GameActionException {
        spawn = rc.getLocation();
        readQuadrant(rc);
    }

    static void attack(RobotController rc) throws GameActionException{
        int bestScore = Integer.MAX_VALUE;
        MapLocation target = rc.getLocation();
        for (RobotInfo robotInfo : rc.senseNearbyRobots(13, rc.getTeam().opponent())) {
            int multiplier;
            switch (robotInfo.getType()) {
                case SAGE: multiplier = 0; break;
                case SOLDIER: multiplier = 1; break;
                case BUILDER: multiplier = 2; break;
                case WATCHTOWER: multiplier = 3; break;
                case MINER: multiplier = 4; break;
                default: multiplier = 5;
            }
            int score = multiplier * 1000000 + rc.senseRubble(robotInfo.location) + 1000 * robotInfo.getHealth();
            score = multiplier * 1000000 + (rc.senseRubble(robotInfo.location) + 10) * (robotInfo.health + visibleAllies);
            if (score < bestScore) {
                bestScore = score;
                target = robotInfo.location;
            }
        }

        if (rc.canAttack(target))  {
            rc.attack(target);
            sinceLastAttack = 0;
        } else {
            ++sinceLastAttack;
        }
    }

    static void setDestination(RobotController rc) throws GameActionException {
        MapLocation cur = rc.getLocation();
        if (sinceLastAttack > 5) readQuadrant(rc);

        if (destination == null || rc.canSenseLocation(destination) ) {
            isBackup = false;
            destination = new MapLocation(Utils.randomInt(3, Utils.width - 3), Utils.randomInt(3, Utils.height-3));
        }

        if (toLeadFarm && cur.distanceSquaredTo(spawn) <= 7) {
            if (rc.senseLead(rc.getLocation()) == 0 && visibleAllies >= 4) {
                rc.disintegrate();
            } else {
                int best = 100000;
                Direction go = Direction.CENTER;
                for (Direction dir : Direction.allDirections()) {
                    if (rc.senseRubble(rc.adjacentLocation(dir)) < best) {
                        best = rc.senseRubble(rc.adjacentLocation(dir));
                        go = dir;
                    }
                }
            }
            return;
        }

        if (!toLeadFarm && rc.getHealth() <= 10) {
            destination = spawn;
            toLeadFarm = true;
        }
    }

    static Direction lowestOpenRubble(RobotController rc) throws GameActionException {
        int bestRubble = 100000;
        Direction go = Direction.CENTER;
        for (Direction dir : Direction.allDirections()) {
            MapLocation candidate = rc.adjacentLocation(dir);
            if (rc.canSenseLocation(candidate) && rc.senseRubble(candidate) < bestRubble && rc.canMove(dir)) {
                bestRubble = rc.senseRubble(candidate);
                go = dir;
            }
        }
        return go;
    }


    static void micro(RobotController rc) throws GameActionException {
        if (!rc.isMovementReady()) {
            attack(rc);
        } else if (visibleEnemies == 0) {
            // TODO make it not always run at destination
            Pathfinder.move(rc, destination);
        }

        if (attackersInRange == 1) {
            Direction go = Direction.CENTER;
            int best = 1000000;
            for (Direction dir : Constants.directions) {
                if (rc.adjacentLocation(dir).distanceSquaredTo(nearestEnemy) <= best) {
                    best = rc.adjacentLocation(dir).distanceSquaredTo(nearestEnemy);
                    go = dir;
                }
            }
        }

        if (toLeadFarm && visibleAllies >= 6) {
            if (rc.senseLead(rc.getLocation()) == 0) rc.disintegrate();
            else rc.move(lowestOpenRubble(rc));
        }


        attack(rc);
    }

    static void writeQuadrantInformation(RobotController rc) throws GameActionException {
        MapLocation cur = rc.getLocation();
        MapLocation center = new MapLocation(cur.x - cur.x % (Utils.blockWidth) + Utils.blockWidth/2,
                cur.y - cur.y % (Utils.blockHeight) + Utils.blockHeight/2);

        int colIndex =  cur.x / Utils.blockWidth;
        int rowIndex = cur.y / Utils.blockHeight;
        int quadrant = Utils.numBlocks * rowIndex + colIndex;

        int dist = cur.distanceSquaredTo(center);

        // indices 0 - 35 are quadrant information for enemies and allies
        int newValue = (visibleAttackers << 7) + visibleAllies;
        int prevValue = rc.readSharedArray(quadrant) & 32767;
        int writeValue = (dist * prevValue + (101 - dist) * newValue) / 101;
        if (prevValue == 0) writeValue = newValue;
        rc.writeSharedArray(quadrant, writeValue + (1 << 15));
        assert dist < 101;

        int lead = rc.senseNearbyLocationsWithLead(-1).length;
        newValue = (visibleEnemies << 7) + lead;
        prevValue = rc.readSharedArray(quadrant + Utils.gridSize) & 32767;
        writeValue = (dist * prevValue + (101 - dist) * newValue) / 101;
        if (prevValue == 0) writeValue = newValue;
        rc.writeSharedArray(quadrant + Utils.gridSize, writeValue + (1 << 15));
    }

    static void readQuadrant(RobotController rc) throws GameActionException {
        if (toLeadFarm) return;
        MapLocation cur = rc.getLocation();
        int dist = Integer.MAX_VALUE;
        int mostAttackers = 1;
        for (int quadrant = 0; quadrant < Utils.gridSize; ++quadrant) {
            int temp = rc.readSharedArray(quadrant);
            int visAllies = temp & 127;
            int visAttackers = (temp >> 7) & 127;

            int x = quadrant % Utils.numBlocks * Utils.blockWidth + Utils.blockWidth/2;
            int y = quadrant / Utils.numBlocks * Utils.blockHeight + Utils.blockHeight/2;

            MapLocation dest = new MapLocation(x,y);

            if (visAttackers > 1) {
                if (cur.distanceSquaredTo(dest) < dist) {
                    dist = cur.distanceSquaredTo(dest);
                    isBackup = true;
                    destination = dest;
                }
            }
        }

        int dx = Utils.randomInt(-Utils.blockWidth/2 + 2, Utils.blockWidth/2 - 2);
        int dy = Utils.randomInt(-Utils.blockHeight/2 + 2, Utils.blockHeight/2 - 2);
        if (destination != null && isBackup) {
            int x = Math.min(Math.max(destination.x + dx, 0), Utils.width-1);
            int y = Math.min(Math.max(destination.y + dy, 0), Utils.height-1);
            destination = new MapLocation(x, y);
        }
    }


    static void senseEnemies(RobotController rc) throws GameActionException {
        visibleEnemies = 0;
        visibleAttackers = 0;
        visibleAllies = 1;

        int far = 100000;
        nearestAlly = null;
        MapLocation cur = rc.getLocation();

        for (RobotInfo info : rc.senseNearbyRobots(-1)) {
            if (info.team.equals(Utils.opponent)) {
                ++visibleEnemies;
                switch (info.type) {
                    case SOLDIER: case WATCHTOWER: case SAGE:
                        ++visibleAttackers;
                }
                if (info.type == RobotType.SOLDIER) enemy = info.location;
            } else {
                if (info.type == RobotType.SOLDIER) {
                    ++visibleAllies;
                    if (info.location.distanceSquaredTo(cur) < far) {
                        nearestAlly = info.location;
                        far = info.location.distanceSquaredTo(cur);
                    }
                }
            }
        }
    }

    static void run(RobotController rc) throws GameActionException {
        senseEnemies(rc);
        setDestination(rc);
        micro(rc);

        writeQuadrantInformation(rc);

        rc.setIndicatorString(destination.toString());
        rc.setIndicatorLine(rc.getLocation(), destination, 255, 255, 255);
    }
}
