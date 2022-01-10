package turtle2;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

class Archon {

    static int minersBuilt = 0;
    static int soldiersBuilt = 0;

    static int soldierProbability = 30;
    static int minerProbability = 15;
    static int builderProbability = 5;

    static void firstTurnMiners(RobotController rc) throws GameActionException {
        if (minersBuilt * rc.getArchonCount() < Constants.earlyMinerCap && Utils.rng.nextInt(rc.getArchonCount()) == 0) {
            for (Direction dir : Utils.directions) {
                if (rc.canBuildRobot(RobotType.MINER, dir)) {
                    rc.buildRobot(RobotType.MINER, dir);
                    minersBuilt++;
                }
            }
        }
    }

    static void adjustProbability(RobotController rc) throws GameActionException {
        int lead = rc.getTeamLeadAmount(rc.getTeam());
        if (lead >= 2000) {
            soldierProbability = 50;
            minerProbability = 40;
            builderProbability = 10;
        } else {
            soldierProbability = 30;
            minerProbability = 15;
            builderProbability = 5;
        }
    }

    static void buildUnits(RobotController rc) throws GameActionException {
        // probably not a very good function at the moment
        for (Direction dir : Constants.directions) {
            int roll = Utils.rng.nextInt(100);
            if (roll < soldierProbability) {
                if (rc.canBuildRobot(RobotType.SOLDIER, dir)) {
                    rc.buildRobot(RobotType.SOLDIER, dir);
                }
            } else if (roll < minerProbability) {
                if (rc.canBuildRobot(RobotType.MINER, dir)) {
                    rc.buildRobot(RobotType.MINER, dir);
                }
            } else if (roll < builderProbability) {
                if (rc.canBuildRobot(RobotType.BUILDER, dir)) {
                    rc.buildRobot(RobotType.BUILDER, dir);
                }
            }
        }
    }

    static void run(RobotController rc) throws GameActionException {
        adjustProbability(rc);

        firstTurnMiners(rc);
        buildUnits(rc);
    }
}
