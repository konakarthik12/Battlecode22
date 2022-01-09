package pathfinder;

import battlecode.common.*;



class Archon {

    static int minersBuilt = 0;
    static int soldiersBuilt = 0;

    static void run(RobotController rc) throws GameActionException {
        MapLocation loc = rc.getLocation();
        rc.writeSharedArray(63, (loc.x << 6) + loc.y);
//        if (minersBuilt < 4) {
//            for (Direction dir : Constants.directions) {
//                if (rc.canBuildRobot(RobotType.MINER, dir))  {
//                    rc.buildRobot(RobotType.MINER, dir);
//                    minersBuilt++;
//                    break;
//                }
//            }
//        } else {
//            return;
//        }
        if (minersBuilt * rc.getArchonCount() < Constants.earlyMinerCap) {
            for (Direction dir : Constants.directions) {
                if (rc.canBuildRobot(RobotType.MINER, dir)) {
                    rc.buildRobot(RobotType.MINER, dir);
                    ++minersBuilt;
                }
            }
        } else {
            if (soldiersBuilt < 30) {
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
                if (Utils.randomInt(0, 100) <= 5) {
                    for (Direction dir : Constants.directions) {
                        if (rc.canBuildRobot(RobotType.BUILDER, dir)) {
                            rc.buildRobot(RobotType.BUILDER, dir);
                            break;
                        }
                    }
                } else if (Utils.randomInt(0, 100) <= 10) {
                    for (Direction dir : Constants.directions) {
                        if (rc.canBuildRobot(RobotType.MINER, dir)) {
                            rc.buildRobot(RobotType.MINER, dir);
                            ++minersBuilt;
                            break;
                        }
                    }
                } else if (Utils.randomInt(0, 100) <= 40) {
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
