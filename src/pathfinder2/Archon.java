package pathfinder2;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

class Archon {
    static boolean builtMiner = false;

    static void run(RobotController rc) throws GameActionException {
        if (builtMiner) return;
        for (Direction dir : Constants.directions) {
            if (rc.canBuildRobot(RobotType.MINER, dir)) {
                rc.buildRobot(RobotType.MINER, dir);
                builtMiner = true;
                break;
            }
        }

    }
}
