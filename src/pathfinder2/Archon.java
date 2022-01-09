package pathfinder2;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

class Archon {
    static int builtMiner = 0;

    static void run(RobotController rc) throws GameActionException {
        if (builtMiner > 3) return;
        for (Direction dir : Constants.directions) {
            if (rc.canBuildRobot(RobotType.MINER, dir)) {
                rc.buildRobot(RobotType.MINER, dir);
                builtMiner++;
                break;
            }
        }

    }
}
