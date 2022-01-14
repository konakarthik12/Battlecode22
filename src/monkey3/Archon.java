package monkey3;

import battlecode.common.*;

public class Archon {

    static int archonID = 0;
    static int minersBuilt = 0;
    static int soldiersBuilt = 0;
    static int enemiesInVision = 0;

    static void summonUnitAnywhere(RobotController rc, RobotType type) throws GameActionException {
        for (Direction dir : Constants.directions) {
            if (rc.canBuildRobot(type, dir)) {
                rc.buildRobot(type, dir);
                if (type == RobotType.MINER) ++minersBuilt;
                if (type == RobotType.SOLDIER) ++soldiersBuilt;
            }
        }
    }

    static void heal(RobotController rc) throws GameActionException {
        RobotInfo[] info = rc.senseNearbyRobots(-1, rc.getTeam());
        for (RobotInfo r : info) {
            if (rc.canAttack(r.location)) {
                rc.attack(r.location);
            }
        }
    }

    static void setup(RobotController rc) throws GameActionException {
        int ind = 63;
        while (rc.readSharedArray(ind) != 0) --ind;
        archonID = 64 - ind;
        rc.writeSharedArray(ind, (1 << 15) + (rc.getLocation().x << 6) + rc.getLocation().y);
        if (archonID == 1) {
            int k = 228;
            if (rc.getArchonCount() == 1) k = 0;
            if (rc.getArchonCount() == 2) k = 4;
            if (rc.getArchonCount() == 3) k = 36;
            rc.writeSharedArray(58, k);
            // 228 in binary is 11 10 01 00
        }
    }

    static void reset(RobotController rc) throws GameActionException {
        enemiesInVision = 0;
        if (archonID == rc.getArchonCount()) {
            for (int i = 2; i < 58; ++i) {
                if (i == 1) continue;
                rc.writeSharedArray(i, 0);
            }
        }
    }

    static void summonUnits(RobotController rc) throws GameActionException {
//        if (rc.getRoundNum() <= 10) {
//            int buildTurn = rc.readSharedArray(58);
//            int a1 = buildTurn & 3;
//            int a2 = buildTurn & (3 << 2);
//            int a3 = buildTurn & (3 << 4);
//            int a4 = buildTurn & (3 << 6);
//            if ((buildTurn & 3) != archonID-1) return;
//            buildTurn = (buildTurn >> 2) | ((buildTurn & 3) << (2 * (rc.getArchonCount() - 1)));
//            rc.writeSharedArray(58, buildTurn);
//        }

        if (rc.getRoundNum() <= 4) {
            if (enemiesInVision > 0) {
                summonUnitAnywhere(rc, RobotType.SOLDIER);
                ++soldiersBuilt;
            }
            MapLocation[] leadLoc = rc.senseNearbyLocationsWithLead();
            if (leadLoc.length > 0) {
                Direction go = rc.getLocation().directionTo(leadLoc[0]);
                if (rc.canBuildRobot(RobotType.MINER, go)) rc.buildRobot(RobotType.MINER, go);
                else {
                    go = go.rotateLeft();
                    if (rc.canBuildRobot(RobotType.MINER, go)) rc.buildRobot(RobotType.MINER, go);
                    go = go.rotateRight().rotateRight();
                    if (rc.canBuildRobot(RobotType.MINER, go)) rc.buildRobot(RobotType.MINER, go);
                }
                ++minersBuilt;
            } else {
                summonUnitAnywhere(rc, RobotType.MINER);
                ++minersBuilt;
            }
        }
        else {
            // TODO don't spawn on rubble :skull:
            if (rc.readSharedArray(1) < rc.readSharedArray(0) - 10) {
                summonUnitAnywhere(rc, RobotType.SOLDIER);
                ++soldiersBuilt;
                rc.writeSharedArray(1, rc.readSharedArray(1) + 1);
            } else if (Utils.randomInt(1, rc.getArchonCount() * 2) <= 1) {
                summonUnitAnywhere(rc, RobotType.MINER);
                ++minersBuilt;
            }
        }
    }

    static void senseLocalEnemies(RobotController rc) throws GameActionException {
        enemiesInVision += rc.senseNearbyRobots(-1, rc.getTeam().opponent()).length;
//        for (RobotInfo info : rc.senseNearbyRobots(-1, rc.getTeam().opponent())) {}
    }

    static void run(RobotController rc) throws GameActionException {
        senseLocalEnemies(rc);

        heal(rc);
//        summonUnitAnywhere(rc, RobotType.MINER);
        summonUnits(rc);

        reset(rc);
    }

}
