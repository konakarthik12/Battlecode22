package monkey1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

import static monkey1.Constants.*;

class Archon {

    static int minersBuilt = 0;
    static int soldiersBuilt = 0;

    static void run(RobotController rc) throws GameActionException {
        if (minersBuilt * rc.getArchonCount() < earlyMinerCap) {
            for (Direction dir : directions) {
                if (rc.canBuildRobot(RobotType.MINER, dir)) {
                    rc.buildRobot(RobotType.MINER, dir);
                    ++minersBuilt;
                }
            }
        } else {
            if (soldiersBuilt < 50) {
                if (soldiersBuilt < minerSoldierRatio * minersBuilt) {
                    for (Direction dir : directions) {
                        if (rc.canBuildRobot(RobotType.SOLDIER, dir)) {
                            rc.buildRobot(RobotType.SOLDIER, dir);
                            ++soldiersBuilt;
                            break;
                        }
                    }
                } else {
                    for (Direction dir : directions) {
                        if (rc.canBuildRobot(RobotType.MINER, dir)) {
                            rc.buildRobot(RobotType.MINER, dir);
                            ++minersBuilt;
                            break;
                        }
                    }
                }
            } else {
                for (Direction dir : directions) {
                    if (rc.canBuildRobot(RobotType.BUILDER, dir)) {
                        rc.buildRobot(RobotType.BUILDER, dir);
                        break;
                    }
                }
            }
        }
    }

}
