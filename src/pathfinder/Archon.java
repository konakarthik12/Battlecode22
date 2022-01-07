package pathfinder;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import com.sun.javafx.geom.RectBounds;

class Archon {
    static void run(RobotController rc) throws GameActionException {
        if (rc.canBuildRobot(RobotType.MINER, Direction.EAST)) {
            rc.buildRobot(RobotType.MINER, Direction.EAST);
        }
    }
}
