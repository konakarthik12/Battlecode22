package monkey2;

import battlecode.common.*;

public class Archon {

    static int archonID = 0;
    static int minersBuilt = 0;
    static int soldiersBuilt = 0;
    static int enemiesInVision = 0;
    static int visibleEnemies = 0;
    static int visibleAllies = 0;
    static int visibleAttackers = 0;
    static int visibleMiners = 0;

    // TODO: test adding ceil(enemies/(visible allies)) or other averaging schemes
    static int[] enemyEstimates = new int[64];

    static void summonUnitAnywhere(RobotController rc, RobotType type) throws GameActionException {
        Direction build = Direction.EAST;
        int rubble = 1000;
        for (Direction dir : Constants.directions) {
            if (rc.canBuildRobot(type, dir)) {
                int _rubble = rc.senseRubble(rc.adjacentLocation(dir));
                if (_rubble < rubble) {
                    build = dir;
                    rubble = _rubble;
                }
            }
        }
        if (rc.canBuildRobot(type, build)) {
            rc.buildRobot(type, build);
            if (type.equals(RobotType.MINER)) {
                rc.writeSharedArray(42, rc.readSharedArray(42) + 1);
                ++minersBuilt;
            } else if (type.equals(RobotType.SOLDIER)) {
                rc.writeSharedArray(1, rc.readSharedArray(1) + 1);
                ++soldiersBuilt;
            }
        }
    }

    static void heal(RobotController rc) throws GameActionException {
        RobotInfo[] info = rc.senseNearbyRobots(-1, rc.getTeam());
        for (RobotInfo r : info) {
            if (rc.canRepair(r.location) && (r.type == RobotType.SOLDIER || visibleAllies == 0)) {
                rc.repair(r.location);
            }
        }
    }

    static void setup(RobotController rc) throws GameActionException {
        int ind = 63;
        while (rc.readSharedArray(ind) != 0) --ind;
        archonID = 64 - ind;
        rc.writeSharedArray(ind, (1 << 15) + (rc.getLocation().x << 6) + rc.getLocation().y);
    }

    static void reset(RobotController rc) throws GameActionException {
        enemiesInVision = 0;
        for (int i = 2; i <= 17; ++i) {
            enemyEstimates[i] = (enemyEstimates[i]*4 + rc.readSharedArray(i))/5;
        }
        if (archonID == rc.getArchonCount()) {
            for (int i = 2; i < 34; ++i) {
                rc.writeSharedArray(i, 0);
            }
        }
    }

    static void summonUnits(RobotController rc) throws GameActionException {
        MapLocation[] leadLoc = rc.senseNearbyLocationsWithLead();
        if (rc.getRoundNum() <= 4) {
            if (enemiesInVision > 0) summonUnitAnywhere(rc, RobotType.SOLDIER);
            if (leadLoc.length > 0) {
                Direction go = rc.getLocation().directionTo(leadLoc[0]);
                if (rc.canBuildRobot(RobotType.MINER, go)) rc.buildRobot(RobotType.MINER, go);
                else {
                    go = go.rotateLeft();
                    if (rc.canBuildRobot(RobotType.MINER, go)) rc.buildRobot(RobotType.MINER, go);
                    go = go.rotateRight().rotateRight();
                    if (rc.canBuildRobot(RobotType.MINER, go)) rc.buildRobot(RobotType.MINER, go);
                }
            } else {
                summonUnitAnywhere(rc, RobotType.MINER);
            }
        } else {
            // prioritize soldiers where needed
            int numEnemies = 0;
            int numSoldiers = rc.readSharedArray(1);
            for (int i = 2; i < 18; ++i) {
                int val = rc.readSharedArray(i);
                numEnemies += (val >> 8) & 255;
            }

//            rc.setIndicatorString(numEnemies + " " + numSoldiers);
            rc.setIndicatorString(numEnemies + " " + numSoldiers + " " + rc.readSharedArray(42));
//            System.out.println(rc.readSharedArray(34));
            int carryingCapacity = 10 + rc.getRoundNum() / 150; // 2000 / 150 = 200 / 15 = 13.5
            int roll = Utils.randomInt(1, carryingCapacity);
            if (numSoldiers-3 < numEnemies || enemiesInVision > 0 || roll < rc.readSharedArray(42) ) {
                summonUnitAnywhere(rc, RobotType.SOLDIER);
            } else {
                if (Utils.randomInt(1, rc.getArchonCount()) == 1)
                    summonUnitAnywhere(rc, RobotType.MINER);
            }

        }
    }
    static void senseEnemies(RobotController rc) throws GameActionException {
//        int enemies = 0;
//        int attackers = 0;
        visibleEnemies = 0;
        visibleAttackers = 0;
        visibleAllies = 0;
        visibleMiners = 0;
//        visibleAllies = 1 + rc.senseNearbyRobots(RobotType.MINER.visionRadiusSquared, rc.getTeam()).length;
//        for (RobotInfo info : rc.senseNearbyRobots(RobotType.MINER.visionRadiusSquared, rc.getTeam().opponent())) {
        for (RobotInfo info : rc.senseNearbyRobots(-1)) {
            if (info.team.equals(rc.getTeam())) {
                if (info.type.equals(RobotType.SOLDIER)) ++visibleAllies;
                if (info.type.equals(RobotType.MINER)) ++visibleMiners;
            } else {
                ++visibleEnemies;
                switch (info.type) {
                    case SOLDIER:
                    case WATCHTOWER:
                    case SAGE:
                        ++visibleAttackers;
                }
            }
        }

//        if (Utils.randomInt(1, allies) <= 3) {
        rc.writeSharedArray(0, rc.readSharedArray(0) + visibleEnemies);
//        }
    }

    static void run(RobotController rc) throws GameActionException {
        senseEnemies(rc);
        summonUnits(rc);
        heal(rc);
        reset(rc);
        rc.writeSharedArray(37 + archonID, 65535);
    }
}
