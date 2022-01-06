package monkey1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

import static monkey1.Utils.*;

public class Archon {

    static int minersBuilt = 0;
    static final int earlyMinerCap = 15;

    public static void run(RobotController rc) throws GameActionException {
        if (minersBuilt * rc.getArchonCount() < earlyMinerCap) {
            for (Direction dir: directions){
                if (rc.canBuildRobot(RobotType.MINER, dir)) {
                    rc.buildRobot(RobotType.MINER, dir);
                    ++minersBuilt;
                }
            }
        }
    }

}
