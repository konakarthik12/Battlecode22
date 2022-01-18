package monkey4;

import battlecode.common.*;

import java.nio.file.Path;

class Soldier {

    static MapLocation destination = null;
    static MapLocation spawn = null;
    static MapLocation nearestEnemy = null;

    static boolean toLeadFarm = false;
    static boolean isBackup = false;

    static int sinceLastAttack = 0;
    static int visibleEnemies = 0;
    static int visibleAttackers = 0;
    static int visibleAllies = 0;

    static int attackersInRange = 0;

    static double allyDPS = 0;
    static double enemyDPS = 0;

    // TODO if higher health make the soldier more aggressive?
    // TODO better chasing after enemies
    // TODO produce a better calculation of enemy priority

    static void setup(RobotController rc) throws GameActionException {
        spawn = rc.getLocation();
        readQuadrant(rc);
    }

    static void attack(RobotController rc) throws GameActionException{
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
            int k = (robotInfo.health + 2)/3 * (10 * robotInfo.getType().damage / (10 + rc.senseRubble(robotInfo.location)));

            int score = multiplier * 1000000 + 1000 * rc.senseRubble(robotInfo.location) + robotInfo.getHealth();
            score = multiplier * 1000 + k;
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
        MapLocation cur = rc.getLocation();
        if (sinceLastAttack > 3) readQuadrant(rc);

        if (destination == null || destination.equals(cur)) {
            destination = new MapLocation(Utils.randomInt(3, Utils.width - 3), Utils.randomInt(3, Utils.height-3));
        }

        if (toLeadFarm && cur.distanceSquaredTo(spawn) < 7) {
            if (rc.senseLead(cur) == 0) {
                rc.disintegrate();
            } else {
                rc.disintegrate();
            }
            return;
        }

        if (!toLeadFarm && rc.getHealth() < 8) {
//            destination = spawn;
            toLeadFarm = true;
        }
    }

    static void readQuadrant(RobotController rc) throws GameActionException {
        int bytecode = Clock.getBytecodeNum();
        MapLocation cur = rc.getLocation();
        boolean moving = false;
        int dist = Integer.MAX_VALUE;
        int best = 1;
        for (int quadrant = 0; quadrant < 36; ++quadrant) {
            int temp = rc.readSharedArray(quadrant);
            int visAllies = temp & 127;
            int visAttackers = (temp >> 7) & 127;

            int x = quadrant % 6 * Utils.b_width + Utils.width/12;
            int y = quadrant / 6 * Utils.b_height + Utils.height/12;
            assert x < Utils.width;
            assert y < Utils.height;

            MapLocation dest = new MapLocation(x,y);

            if (visAttackers > 1) {
                best = visAttackers;
                if (cur.distanceSquaredTo(dest) < dist) {
                    dist = cur.distanceSquaredTo(dest);
                    isBackup = true;
                    moving = true;
                    destination = dest;
                }
            }
        }
        int dx = Utils.randomInt(-Utils.width/12, Utils.width/12);
        int dy = Utils.randomInt(-Utils.height/12, Utils.height/12);
        if (destination != null) destination = new MapLocation(destination.x + dx, destination.y + dy);
    }

    static void macro(RobotController rc) throws GameActionException {

    }

    static void micro(RobotController rc) throws GameActionException {
        int actionCooldown = rc.getActionCooldownTurns();
        int moveCooldown = rc.getMovementCooldownTurns();
    }

    static void move(RobotController rc) throws GameActionException {
        MapLocation cur = rc.getLocation();
//        int rubble = Integer.MAX_VALUE;
         int rubble = rc.senseRubble(cur);

         if (visibleAttackers > visibleAllies) {
             destination = spawn;
             Pathfinder.move(rc, spawn);
         } else if (visibleAttackers > 0) {
            Direction go = Direction.CENTER;
            for (Direction dir : Constants.directions) {
                if (rc.canSenseLocation(rc.adjacentLocation(dir)) && rc.senseRubble(rc.adjacentLocation(dir)) < rubble) {
                    rubble = rc.senseRubble(rc.adjacentLocation(dir));
                    go = dir;
                }
            }
            if (rc.canMove(go)) rc.move(go);
            else Pathfinder.move(rc, destination);
        } else {
            Pathfinder.move(rc, destination);
        }
//        if (visibleAttackers+1 > visibleAllies) {
//            Pathfinder.move(rc, spawn);
//        } else if (visibleAttackers == visibleAllies) {
//            Direction go = Direction.CENTER;
//            for (Direction dir : Constants.directions) {
//                if (rc.canSenseLocation(rc.adjacentLocation(dir)) && rc.senseRubble(rc.adjacentLocation(dir)) < rubble) {
//                    rubble = rc.senseRubble(rc.adjacentLocation(dir));
//                    go = dir;
//                }
//            }
//            if (rc.canMove(go)) rc.move(go);
//            else Pathfinder.move(rc, destination);
//        } else {
//            Pathfinder.move(rc, destination);
//        }
    }

    static void writeQuadrantInformation(RobotController rc) throws GameActionException {
        MapLocation cur = rc.getLocation();
        MapLocation center = new MapLocation(cur.x - cur.x % (Utils.width/6) + Utils.width/12,
                                             cur.y - cur.y % (Utils.height/6) + Utils.height/12);

        int colIndex =  6 * cur.x / Utils.width;
        int rowIndex = 6 * cur.y / Utils.height;
        int quadrant = 6 * rowIndex + colIndex;

        int dist = cur.distanceSquaredTo(center);

        // indices 0 - 35 are quadrant information for enemies and allies
        int newValue = (visibleAttackers << 7) + visibleAllies;
        int prevValue = rc.readSharedArray(quadrant) & 32767;
        int writeValue = (dist * prevValue) / 60 + ((60 - dist) * newValue) / 60;
        if (prevValue == 0) writeValue = newValue;
        if (writeValue >= 1 << 15) {
            System.out.println(dist);
            System.out.println(visibleAllies);
            System.out.println(visibleAttackers);
            System.out.println(prevValue);
            System.out.println(newValue);
            rc.resign();
        }
        assert writeValue < 1 << 15;
        rc.writeSharedArray(quadrant, writeValue + (1 << 15));

//        int lead = (rc.senseNearbyLocationsWithLead().length << 8) + visibleAttackers;
//        int oldVal = rc.readSharedArray(18 + quadrant);
//        writeValue = (oldVal * dist + (50-dist) * lead) / 50;
//        if (oldVal == 0) writeValue = lead;
//        rc.writeSharedArray(18 + quadrant, writeValue);

        assert quadrant < 36;
        assert dist <= 60;
    }

    static MapLocation enemy = null;

    static void senseEnemies(RobotController rc) throws GameActionException {
        visibleEnemies = 0;
        visibleAttackers = 0;
        visibleAllies = 1;

        for (RobotInfo info : rc.senseNearbyRobots(-1)) {
            if (info.team.equals(Utils.opponent)) {
                ++visibleEnemies;
                switch (info.type) {
                    case SOLDIER: case WATCHTOWER: case SAGE:
                        ++visibleAttackers;
                }
                if (info.type == RobotType.SOLDIER) enemy = info.location;
            } else {
                if (info.type == RobotType.SOLDIER) ++visibleAllies;
            }
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
