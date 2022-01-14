package monkey1;

import battlecode.common.*;

public class Archon {

    static int archonID = 0;
    static int minersBuilt = 0;
    static int soldiersBuilt = 0;
    static int enemiesInVision = 0;
    static int miner = 0;
    static int allies = 0;

    static int mCost = 50;
    static int sCost = 75;
    static int bCost = 40;

    static int leadIdx = 34;
    static int usedLeadIdx = 35;
    static int flag = 36;

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
//                rc.buildRobot(type, dir);
//                if (type == RobotType.MINER) ++minersBuilt;
//                if (type == RobotType.SOLDIER) ++soldiersBuilt;
            }
        }
        if (rc.canBuildRobot(type, build)) {
            rc.buildRobot(type, build);
            if (type == RobotType.MINER) ++minersBuilt;
            if (type == RobotType.SOLDIER) ++soldiersBuilt;
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
        if (archonID == 1) rc.writeSharedArray(flag, 1);
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
static void minerSpawn(RobotController rc, int num) throws GameActionException {
        int dist = 63;
        MapLocation go = rc.getLocation();
        for (MapLocation loc : rc.senseNearbyLocationsWithLead()) {
            if (loc.distanceSquaredTo(rc.getLocation()) < dist) {
                dist = loc.distanceSquaredTo(rc.getLocation());
                go = loc;
            }
        }
        for (int i = 0; i < num; i++) {
            Direction best = null;
            dist = 63;
            for (Direction dir : Constants.directions) {
                if (!rc.canBuildRobot(RobotType.MINER, dir)) continue;
                int newdist = go.distanceSquaredTo(rc.getLocation().add(dir));
                if (newdist < dist) {
                    dist = newdist;
                    best = dir;
                }
            }
            if (best == null) break;
            rc.buildRobot(RobotType.MINER, best);
            ++minersBuilt;
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

        int usedLead = rc.readSharedArray(usedLeadIdx);
        int currFlag = rc.readSharedArray(flag) % rc.getArchonCount() + 1;
        int leadHere = rc.readSharedArray(leadIdx) / rc.getArchonCount();
        if (rc.getRoundNum() <= 4) {
            if (enemiesInVision > 0) {
                summonUnitAnywhere(rc, RobotType.SOLDIER);
                ++soldiersBuilt;
                usedLead += sCost;
            }
            int num = leadHere / mCost;
            if (rc.readSharedArray(leadIdx) - num * rc.getArchonCount() * mCost - usedLead >= mCost && currFlag == archonID) {
                rc.writeSharedArray(flag, currFlag + 1);
                num++;
            }
            usedLead += num * mCost;
            minerSpawn(rc, num);
        }
        else {
            // TODO don't spawn on rubble :skull:
            if (rc.readSharedArray(1) < rc.readSharedArray(0) - 10) {
                summonUnitAnywhere(rc, RobotType.SOLDIER);
                ++soldiersBuilt;
                rc.writeSharedArray(1, rc.readSharedArray(1) + 1);
//            } else if (Utils.randomInt(1, rc.getArchonCount() * 2) <= 1) {
            } else if (Utils.randomInt(1, minersBuilt) <= 1) {
                summonUnitAnywhere(rc, RobotType.MINER);
                ++minersBuilt;
            } else {
                summonUnitAnywhere(rc, RobotType.SOLDIER);
                ++soldiersBuilt;
                rc.writeSharedArray(1, rc.readSharedArray(1) + 1);
            }
            if (rc.getRoundNum() % 5 == 0) minersBuilt--;
        }
        rc.writeSharedArray(usedLeadIdx, usedLead);
    }

    static void senseLocalEnemies(RobotController rc) throws GameActionException {
        enemiesInVision += rc.senseNearbyRobots(-1, rc.getTeam().opponent()).length;
//        for (RobotInfo info : rc.senseNearbyRobots(-1, rc.getTeam().opponent())) {}
    }

    static void senseAllies(RobotController rc) throws GameActionException {
        RobotInfo[] rAllies = rc.senseNearbyRobots(-1, rc.getTeam());
        miner = 0;
        for (RobotInfo x : rAllies) {
            if (x.type == RobotType.MINER) miner++;
            allies++;
        }

    }

    static void run(RobotController rc) throws GameActionException {
        if (archonID == 1) {
            int currentLead = rc.getTeamLeadAmount(rc.getTeam());
            rc.writeSharedArray(leadIdx, currentLead);
            rc.writeSharedArray(usedLeadIdx, 0);
        }
        senseLocalEnemies(rc);
        senseAllies(rc);
        heal(rc);
        summonUnits(rc);
        reset(rc);
    }

}
