package monkey2newmienr;

import battlecode.common.*;
import monkey2newmienr.Constants;

import static monkey2newmienr.Utils.randomInt;


class Archon {

    static int minersBuilt = 0;
    static int soldiersBuilt = 0;
    static MapLocation congregate = null;
    static int mySpot = 0;
    static void summonUnitAnywhere(RobotController rc, RobotType type) throws GameActionException {
        for (Direction dir : Constants.directions) {
            if (rc.canBuildRobot(type, dir)) {
                rc.buildRobot(type, dir);
                if (type == RobotType.MINER) ++minersBuilt;
                if (type == RobotType.SOLDIER) ++soldiersBuilt;
            }
        }
    }

    static void run(RobotController rc) throws GameActionException {
        MapLocation loc = rc.getLocation();
        if (mySpot == 0) {
            mySpot = 1;
            congregate = rc.getLocation();
            if ((rc.getMapHeight() >> 1) > loc.y) congregate = new MapLocation(congregate.x, congregate.y + 4);
            else congregate = new MapLocation(congregate.x, congregate.y - 4);
            if ((rc.getMapWidth() >> 1) > loc.x) congregate = new MapLocation(congregate.x + 4, congregate.y);
            else congregate = new MapLocation(congregate.x - 4, congregate.y);

            rc.writeSharedArray(63, (loc.x << 6) + loc.y);
            while (rc.readSharedArray(mySpot) != 0) mySpot++;
            rc.writeSharedArray(mySpot, (congregate.x << 6) + congregate.y);
        }
        if (minersBuilt * rc.getArchonCount() < Constants.earlyMinerCap && randomInt(1,rc.getArchonCount()) == 1) {
            summonUnitAnywhere(rc, RobotType.MINER);
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
                if (randomInt(0, 100) <= 5) {
                    for (Direction dir : Constants.directions) {
                        if (rc.canBuildRobot(RobotType.BUILDER, dir)) {
                            rc.buildRobot(RobotType.BUILDER, dir);
                            break;
                        }
                    }
                } else if (randomInt(0, 100) <= 10) {
                    for (Direction dir : Constants.directions) {
                        if (rc.canBuildRobot(RobotType.MINER, dir)) {
                            rc.buildRobot(RobotType.MINER, dir);
                            ++minersBuilt;
                            break;
                        }
                    }
                } else if (randomInt(0, 100) <= 40) {
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
