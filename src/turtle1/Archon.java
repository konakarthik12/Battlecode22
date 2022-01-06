package turtle1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

import java.util.ArrayList;
import java.util.Arrays;

import static turtle1.Utils.directions;

class Archon {

    static final int earlyMinerCap = 15;
    static int minersBuilt = 0;
    static ArrayList<Direction> buildDirs = new ArrayList<>();

    static {
        buildDirs.addAll(Arrays.asList(Direction.cardinalDirections()));
    }

    static void run(RobotController rc) throws GameActionException {
        if (minersBuilt * rc.getArchonCount() < earlyMinerCap) {
            for (Direction dir : directions) {
                if (rc.canBuildRobot(RobotType.MINER, dir)) {
                    rc.buildRobot(RobotType.MINER, dir);
                    minersBuilt++;
                }
            }
        } else {
            for (Direction dir : buildDirs) {
                if (rc.canBuildRobot(RobotType.BUILDER, dir)) {
                    rc.buildRobot(RobotType.BUILDER, dir);
                    buildDirs.remove(dir);
                    break;
                }
            }
        }
    }

}
