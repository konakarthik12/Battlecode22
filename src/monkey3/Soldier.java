package monkey3;

import battlecode.common.*;

class Soldier {
    static MapLocation destination = null;
    static MapLocation spawn = null;

    static boolean toLeadFarm = false;
    static boolean isBackup = false;

    static int sinceLastAttack = 0;
    static int visibleEnemies = 0;
    static int visibleAttackers = 0;
    static int visibleAllies = 0;

    static double allyDPS = 0;
    static double enemyDPS = 0;

    // TODO if higher health make the soldier more aggressive?

    static void setup(RobotController rc) throws GameActionException {
        spawn = rc.getLocation();
        readQuadrant(rc);
    }

    static void attack(RobotController rc) throws GameActionException{
        // TODO: produce a better calculation of enemy priority
        int priority = Integer.MAX_VALUE;
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

            int score = multiplier * 1000000 + 1000 * rc.senseRubble(robotInfo.location) + robotInfo.getHealth();
            if (score < priority) {
                priority = score;
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
        if (sinceLastAttack > 6) {
            readQuadrant(rc);
        }

        if (destination == null || destination.equals(rc.getLocation())) {
            destination = new MapLocation((Utils.rng.nextInt(rc.getMapWidth()) - 3) + 3, (Utils.rng.nextInt(rc.getMapHeight() - 3) + 3));
        }

        if (toLeadFarm && rc.getLocation().distanceSquaredTo(spawn) < 10) {
            if (rc.senseLead(rc.getLocation()) == 0) {
                rc.disintegrate();
                return;
            }

            int bestDist = 10000;
            for (MapLocation loc : rc.getAllLocationsWithinRadiusSquared(rc.getLocation(), RobotType.SOLDIER.visionRadiusSquared)) {
                if (rc.canSenseLocation(loc) && rc.senseLead(loc) == 0 && !rc.isLocationOccupied(loc)) {
                    if (loc.distanceSquaredTo(rc.getLocation()) < bestDist) {
                        bestDist = loc.distanceSquaredTo(rc.getLocation());
                        destination = loc;
                    }
                }
            }
            return;
        }

        if (!toLeadFarm && rc.getHealth() < 8) {
            int numSoldiers = rc.readSharedArray(1);
            rc.writeSharedArray(1, Math.max(numSoldiers - 1, 0));
            destination = spawn;
            toLeadFarm = true;
        }
    }

    static void readQuadrant(RobotController rc) throws GameActionException {
        boolean moving = false;
        int dist = Integer.MAX_VALUE;
        for (int quadrant = 2; quadrant <= 17; ++quadrant) {
            int temp = rc.readSharedArray(quadrant);
            int visAttackers = rc.readSharedArray(quadrant + 18) & 255;
            int visAllies = temp & 255;
            int visEnemies = (temp >> 8) & 255;

            int x = (quadrant - 2) / 4 * rc.getMapWidth()  / 4 + Utils.randomInt(0, rc.getMapWidth()/4);
            int y = (quadrant - 2) % 4 * rc.getMapHeight() / 4 + Utils.randomInt(0, rc.getMapHeight()/4);

//            if (visAttackers > visAllies + 1 && visAttackers > 2) {
            if (visAttackers > 1) {
//                if (rc.getLocation().distanceSquaredTo(new MapLocation(x, y)) < dist) {
                    dist = rc.getLocation().distanceSquaredTo(new MapLocation(x, y));
                    isBackup = true;
                    moving = true;
                    destination = new MapLocation(x,y);
//                }
            }
        }
        if (!moving) {
            destination = new MapLocation(Utils.randomInt(3,rc.getMapWidth()-3), Utils.randomInt(3, rc.getMapHeight()-3));
        }
    }

    static void macro(RobotController rc) throws GameActionException {

    }

    static void move(RobotController rc) throws GameActionException {
        MapLocation cur = rc.getLocation();
        int rubble = rc.senseRubble(cur);

        if (visibleAttackers > visibleAllies) {
            Pathfinder.move(rc, spawn);
        } else if (visibleAttackers >= 1) {
            // TODO better chasing after enemies
//            Direction go = Direction.CENTER;
            Direction go = rc.getLocation().directionTo(enemy);
            for (Direction dir : Constants.directions) {
                if (dir == go.rotateLeft() || dir == go.rotateRight() || dir == go)
                if (rc.canSenseLocation(rc.adjacentLocation(dir)) &&  rc.senseRubble(rc.adjacentLocation(dir)) < rubble) {
                    rubble = rc.senseRubble(rc.adjacentLocation(dir));
                    if (rc.canMove(dir)) go = dir;
                }
            }
            if (rc.canMove(go) && rc.senseRubble(rc.adjacentLocation(go)) <= 20) rc.move(go);
        } else {
            Pathfinder.move(rc, destination);
        }
    }

    static void writeQuadrantInformation(RobotController rc) throws GameActionException {
        // TODO test no random as well
        MapLocation cur = rc.getLocation();
        MapLocation center = new MapLocation(cur.x - cur.x % (rc.getMapWidth()/4) + rc.getMapWidth()/8,
                                             cur.y - cur.y % (rc.getMapHeight()/4) + rc.getMapHeight()/8);
        int x = 4 * cur.x / rc.getMapWidth();
        int y = 4 * cur.y / rc.getMapHeight();
        int quadrant = 4*x + y;

        // weights for gamma averaging, max is a 15 x 15 so distance^2 to center is at most 10
        int dist = cur.distanceSquaredTo(center);

        // indices 2 - 17 are quadrant information for enemies and allies
        int newValue = (visibleEnemies << 8) + visibleAllies;
        int prevValue = rc.readSharedArray(2 + quadrant);
        int writeValue = (dist * prevValue + (100 - dist) * newValue) / 100;
        if (prevValue == 0) writeValue = newValue;
        rc.writeSharedArray(2 + quadrant, writeValue);

        int lead = (rc.senseNearbyLocationsWithLead().length << 8) + visibleAttackers;
        int oldVal = rc.readSharedArray(18 + quadrant);
        writeValue = (oldVal * dist + (100-dist) * lead) / 100;
        if (oldVal == 0) writeValue = lead;
        rc.writeSharedArray(18 + quadrant, writeValue);

        assert dist <= 100;
        assert(quadrant < 16);
    }

    static MapLocation enemy = null;

    static void senseEnemies(RobotController rc) throws GameActionException {
        visibleEnemies = 0;
        visibleAttackers = 0;
        visibleAllies = 1;

        for (RobotInfo info : rc.senseNearbyRobots(-1)) {
            if (info.team.equals(rc.getTeam().opponent())) {
                ++visibleEnemies;
                switch (info.type) {
                    case SOLDIER: case WATCHTOWER: case SAGE:
                        ++visibleAttackers;
                }
                if (info.type.equals(RobotType.SOLDIER)) enemy = info.location;
            } else {
                if (info.type.equals(RobotType.SOLDIER)) ++visibleAllies;
            }
        }

        if (Utils.randomInt(1, visibleAllies) == 1) {
            rc.writeSharedArray(0, rc.readSharedArray(0) + visibleEnemies);
        }
    }

    static void run(RobotController rc) throws GameActionException {
        senseEnemies(rc);
        setDestination(rc);
        move(rc);
        attack(rc);

        writeQuadrantInformation(rc);

        rc.setIndicatorString(destination.toString());
        rc.setIndicatorLine(rc.getLocation(), destination, 255, 255, 255);
    }
}
