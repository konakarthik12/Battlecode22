package Aj2;

import battlecode.common.*;

import static Aj2.Utils.randomInt;


class Archon {

    static int archonIndex = -1;

    static int minersBuilt = 0;
    static int soldiersBuilt = 0;

    static int minerProbability = 10;
    static int soldierProbability = 30;
    static int builderProbability = 5;

    static int minerBudget = 50;
    static int soldierBudget = 50;
    static int builderBudget = 40;

    static void adjustProbability(RobotController rc, int lead) throws GameActionException {
        int excessLead = rc.readSharedArray(5);
        if (rc.getRoundNum() <= 3) minerProbability = 100;
        else {
            minerProbability = 30;

            soldierProbability = 50;
        }

    }
    static void adjustBudgets(RobotController rc, int lead) throws GameActionException {
        int excessLead = rc.readSharedArray(5);
        if (rc.getRoundNum() <= 3) minerBudget = lead;
        else {
            minerBudget = lead * RobotType.MINER.buildCostLead / RobotType.SOLDIER.buildCostLead;
            soldierBudget = lead;
        }
    }

    static void summonUnitAnywhere(RobotController rc, RobotType type) throws GameActionException {
        for (Direction dir : Constants.directions) {
            if (rc.canBuildRobot(type, dir)) {
                rc.buildRobot(type, dir);
                if (type == RobotType.MINER) ++minersBuilt;
                if (type == RobotType.SOLDIER) ++soldiersBuilt;
            }
        }
    }

    public static void setup(RobotController rc) throws GameActionException {
        if(rc.getRoundNum() == 1) {
            rc.writeSharedArray(5, 0);
            rc.writeSharedArray(6, 65535);
            rc.writeSharedArray(7, 0);
            rc.writeSharedArray(8, (1 << 16) - 1);
        }

        if (archonIndex < 0) {
            for (int i = 63; i > 59; --i) {
                if (rc.readSharedArray(i) == 0) {
                    archonIndex = 63 - i;
                    rc.writeSharedArray(i, (1 << 15) + (rc.getLocation().x << 6) + rc.getLocation().y);
                    break;
                }
            }
        }
    }

    public static void trySummonUnits(RobotController rc, RobotType type, int budget) throws GameActionException {
        for (Direction dir : Constants.directions) {
            if (rc.canBuildRobot(type, dir)) {
                if (type.buildCostLead <= budget) {
                    rc.buildRobot(type, dir);
                    budget -= type.buildCostLead;
                } else {
                    break;
                }
            }
        }
    }

    public static void summonUnits(RobotController rc) throws  GameActionException {
//        int lead = rc.getTeamLeadAmount(rc.getTeam()) / (rc.getArchonCount() - archonIndex);
        int lead = rc.readSharedArray(59) / (rc.getArchonCount());
        adjustProbability(rc, lead);
        adjustBudgets(rc, lead);
        if (randomInt(0, 100) <= soldierProbability) {
            trySummonUnits(rc, RobotType.SOLDIER, soldierBudget);
        }

        if (randomInt(0, 100) <= minerProbability) {
            trySummonUnits(rc, RobotType.MINER, minerBudget);
        }

    }

    static void run(RobotController rc) throws GameActionException {
        if (archonIndex == 0) {
            rc.writeSharedArray(59, rc.getTeamLeadAmount(rc.getTeam()));
        }
        rc.setIndicatorString(String.valueOf(archonIndex));
        summonUnits(rc);
        if(rc.getRoundNum() % 2 == 0) rc.writeSharedArray(5, 0);

    }
}
