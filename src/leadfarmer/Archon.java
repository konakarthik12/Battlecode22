package leadfarmer;

import battlecode.common.*;

class Archon {

    static int minersBuilt = 0;
    static int soldiersBuilt = 0;

    static void summonUnitAnywhere(RobotController rc, RobotType type) throws GameActionException {
        for (Direction dir : Constants.directions) {
            if (rc.canBuildRobot(type, dir)) {
                rc.buildRobot(type, dir);
                if (type == RobotType.MINER) ++minersBuilt;
                if (type == RobotType.SOLDIER) ++soldiersBuilt;
            }
        }
    }

    static void heal(RobotController rc) throws GameActionException {
        RobotInfo[] info = rc.senseNearbyRobots(-1, rc.getTeam());
        for (RobotInfo r : info) {
            if (rc.canAttack(r.location)) {
                rc.attack(r.location);
            }
        }
    }

    static void run(RobotController rc) throws GameActionException {
        MapLocation loc = rc.getLocation();
        rc.writeSharedArray(63, (loc.x << 6) + loc.y);

//        if (rc.getRoundNum() <= 4) {
//            for (Direction dir : Constants.directions) {
//                if (rc.canBuildRobot(RobotType.MINER, dir)) {
//                    rc.buildRobot(RobotType.MINER, dir);
//                }
//            }
//        }

//        if (Utils.randInt(0, 1) == 0) {
//            for (Direction dir : Constants.directions) {
//                if (rc.canBuildRobot(RobotType.MINER, dir)) {
//                    rc.buildRobot(RobotType.MINER, dir);
//                }
//            }
//        } else {
//            for (Direction dir : Constants.directions) {
//                if (rc.canBuildRobot(RobotType.BUILDER, dir)) {
//                    rc.buildRobot(RobotType.BUILDER, dir);
//                }
//            }
//        }

//        if (rc.getRoundNum() > 400) rc.resign();

        if (minersBuilt * rc.getArchonCount() < Constants.earlyMinerCap && Utils.randInt(1,rc.getArchonCount()) == 1) {
            summonUnitAnywhere(rc, RobotType.MINER);
        } else {
            if (soldiersBuilt < 60) {
                if (soldiersBuilt < Constants.minerSoldierRatio * minersBuilt) {
                    for (Direction dir : Constants.directions) {
                        if (rc.canBuildRobot(RobotType.SOLDIER, dir)) {
                            rc.buildRobot(RobotType.SOLDIER, dir);
                            ++soldiersBuilt;
                            break;
                        }
                    }
                } else {
                    for (Direction dir : Constants.directions) {
                        if (rc.canBuildRobot(RobotType.MINER, dir)) {
                            rc.buildRobot(RobotType.MINER, dir);
                            ++minersBuilt;
                            break;
                        }
                    }
                }
            } else {
                if (Utils.randInt(0, 100) <= 5) {
                    for (Direction dir : Constants.directions) {
                        if (rc.canBuildRobot(RobotType.BUILDER, dir)) {
                            rc.buildRobot(RobotType.BUILDER, dir);
                            break;
                        }
                    }
                } else if (Utils.randInt(0, 100) <= 10) {
                    for (Direction dir : Constants.directions) {
                        if (rc.canBuildRobot(RobotType.MINER, dir)){
                            rc.buildRobot(RobotType.MINER, dir);
                            ++minersBuilt;
                            break;
                        }
                    }
                } else if (Utils.randInt(0, 100) <= 40) {
                    for (Direction dir : Constants.directions) {
                        if (rc.canBuildRobot(RobotType.SOLDIER, dir)) {
                            rc.buildRobot(RobotType.SOLDIER, dir);
                            ++soldiersBuilt;
                            break;
                        }
                    }
                }
            }
        }
    }

}
