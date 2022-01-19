package monkey4;

import battlecode.common.*;

class Soldier {

    static MapLocation destination = null;
    static MapLocation spawn = null;
    static MapLocation enemy = null;
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
        int bestScore = Integer.MAX_VALUE;
//        int bestScore = Integer.MIN_VALUE;
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
            score = (rc.senseRubble(robotInfo.location) + 10) * (robotInfo.health + visibleAllies);
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
        if (sinceLastAttack > 3) readQuadrant(rc);

        if (destination == null || destination.equals(cur)) {
            destination = new MapLocation(Utils.randomInt(3, Utils.width - 3), Utils.randomInt(3, Utils.height-3));
        }

        if (toLeadFarm && cur.distanceSquaredTo(spawn) < 7) {
            if (rc.senseLead(cur) == 0) {
                rc.disintegrate();
            }

            if (toLeadFarm && rc.getLocation().distanceSquaredTo(spawn) < 10) {
                if (rc.senseLead(rc.getLocation()) == 0) {
                    rc.disintegrate();
                    return;
                }

                int bestDist = 10000;
                for (MapLocation loc : rc.getAllLocationsWithinRadiusSquared(rc.getLocation(), RobotType.BUILDER.visionRadiusSquared)) {
                    if (rc.canSenseLocation(loc) && rc.senseLead(loc) == 0 && !rc.isLocationOccupied(loc)) {
                        if (loc.distanceSquaredTo(rc.getLocation()) < bestDist) {
                            bestDist = loc.distanceSquaredTo(rc.getLocation());
                            destination = loc;
                        }
                    }
                }
                return;
            }
        }

        if (!toLeadFarm && rc.getHealth() < 4) {
            destination = spawn;
            toLeadFarm = true;
        }
    }

    static void readQuadrant(RobotController rc) throws GameActionException {
        if (toLeadFarm) return;
        MapLocation cur = rc.getLocation();
        int dist = Integer.MAX_VALUE;
        int mostAttackers = 1;
        for (int quadrant = 0; quadrant < 36; ++quadrant) {
            int temp = rc.readSharedArray(quadrant);
            int visAllies = temp & 127;
            int visAttackers = (temp >> 7) & 127;

            int x = quadrant % 6 * Utils.b_width + Utils.width/12;
            int y = quadrant / 6 * Utils.b_height + Utils.height/12;

            assert x < Utils.width && 0 <= x;
            assert y < Utils.height && 0 <= y;

            MapLocation dest = new MapLocation(x,y);

            if ((visAttackers-visAllies) > mostAttackers) {
                mostAttackers = visAttackers-visAllies;
                isBackup = true;
                destination = dest;
            }

//            if (visAttackers > 1) {
//                if (cur.distanceSquaredTo(dest) < dist) {
//                    dist = cur.distanceSquaredTo(dest);
//                    isBackup = true;
//                    destination = dest;
//                }
//            }
        }
        int dx = Utils.randomInt(-Utils.width/12 + 2, Utils.width/12 - 2);
        int dy = Utils.randomInt(-Utils.height/12 + 2, Utils.height/12 - 2);
        if (destination != null && isBackup) destination = new MapLocation(destination.x + dx, destination.y + dy);
    }

    static void micro(RobotController rc) throws GameActionException {
        if (visibleEnemies > 0) {
            MicroInfo[] info = new MicroInfo[9];
            for (int i = 0; i < 9; ++i) info[i] = new MicroInfo(rc, rc.adjacentLocation(Direction.allDirections()[i]), visibleAttackers);

            for (RobotInfo robot : rc.senseNearbyRobots(-1)) {
                info[0].update(rc, robot);
                info[1].update(rc, robot);
                info[2].update(rc, robot);
                info[3].update(rc, robot);
                info[4].update(rc, robot);
                info[5].update(rc, robot);
                info[6].update(rc, robot);
                info[7].update(rc, robot);
                info[8].update(rc, robot);
            }

            MicroInfo bestMicro = info[0];
            for (int i = 1; i < 9; ++i) {
                if (info[i].isBetter(rc, bestMicro)) bestMicro = info[i];
            }

            Direction go = rc.getLocation().directionTo(bestMicro.loc);

            if (rc.canMove(go)) rc.move(go);
        } else {
            Pathfinder.move(rc, destination);
        }
        attack(rc);
    }

//    static void move(RobotController rc) throws GameActionException {
//        MapLocation cur = rc.getLocation();
//         int rubble = rc.senseRubble(cur);
//
//         if (visibleAttackers > visibleAllies) {
//             destination = spawn;
//             Pathfinder.move(rc, spawn);
//         } else if (visibleAttackers > 0) {
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
//    }

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
        rc.writeSharedArray(quadrant, writeValue + (1 << 15));

        assert quadrant < 36;
        assert dist <= 60;
    }


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
        micro(rc);

        writeQuadrantInformation(rc);

        rc.setIndicatorString(destination.toString());
        rc.setIndicatorLine(rc.getLocation(), destination, 255, 255, 255);
    }
}
