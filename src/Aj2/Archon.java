package Aj2;

import battlecode.common.*;

import static Aj2.Utils.randomInt;


class Archon {

    static int minersBuilt = 0;
    static int soldiersBuilt = 0;
    static int leadIdx = 5;
    static int dLeadIdx = 6;
    static int usedLeadIdx = 7;
    static int flag = 8;
    static int archonIndex = 0;

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
        if (archonIndex == 0) {
            archonIndex = 1;
            while(rc.readSharedArray(archonIndex) != 0) archonIndex++;
            rc.writeSharedArray(archonIndex, (1 << 15) + (rc.getLocation().x << 6) + rc.getLocation().y);
        }
        if (archonIndex == 1) rc.writeSharedArray(flag, 1);
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
        }
    }
    static void run(RobotController rc) throws GameActionException {

        if (rc.getRoundNum() == 0) setup(rc);
        if (archonIndex == 1) {
            int currentLead = rc.getTeamLeadAmount(rc.getTeam());
            rc.writeSharedArray(dLeadIdx, currentLead - Math.min(rc.readSharedArray(leadIdx), currentLead));
            rc.writeSharedArray(leadIdx, currentLead);
            rc.writeSharedArray(usedLeadIdx, 0);
        }
        int usedLead = rc.readSharedArray(usedLeadIdx);
        int currFlag = rc.readSharedArray(flag);
        rc.setIndicatorString(String.valueOf(archonIndex));
        if (rc.getRoundNum() < 50) {
            int num = rc.readSharedArray(leadIdx) / rc.getArchonCount() / 50;
            if (rc.readSharedArray(leadIdx) - num * rc.getArchonCount() * 50 - usedLead >= 50 && rc.readSharedArray(flag) == archonIndex) {
                rc.writeSharedArray(flag, currFlag + 1);
                rc.writeSharedArray(usedLeadIdx, usedLead + 50);
                num++;
            }
            minerSpawn(rc, num);
        }
        if (minersBuilt * rc.getArchonCount() < Constants.earlyMinerCap && randomInt(1,rc.getArchonCount()) == 1) {
            summonUnitAnywhere(rc, RobotType.MINER);
        } else {
            if (soldiersBuilt < 30) {
                if (soldiersBuilt < Constants.minerSoldierRatio * minersBuilt) {
                    for (Direction dir : Constants.directions) {
                        if (rc.canBuildRobot(RobotType.SOLDIER, dir)) {
                            rc.buildRobot(RobotType.SOLDIER, dir);
                            ++soldiersBuilt;
                            break;
                        }
                    }
                } else {
                    for (Direction dir : Constants.directions) {
                        if (rc.canBuildRobot(RobotType.MINER, dir)) {
                            rc.buildRobot(RobotType.MINER, dir);
                            ++minersBuilt;
                            break;
                        }
                    }
                }
            } else {
                if (randomInt(0, 100) <= 5) {
                    for (Direction dir : Constants.directions) {
                        if (rc.canBuildRobot(RobotType.BUILDER, dir)) {
                            rc.buildRobot(RobotType.BUILDER, dir);
                            break;
                        }
                    }
                } else if (randomInt(0, 100) <= 10 + Math.min(35, rc.readSharedArray(5) * 3)) {
                    for (Direction dir : Constants.directions) {
                        if (rc.canBuildRobot(RobotType.MINER, dir)){
                            rc.buildRobot(RobotType.MINER, dir);
                            ++minersBuilt;
                            break;
                        }
                    }
                } else if (randomInt(0, 100) <= 60) {
                    for (Direction dir : Constants.directions) {
                        if (rc.canBuildRobot(RobotType.SOLDIER, dir)) {
                            rc.buildRobot(RobotType.SOLDIER, dir);
                            ++soldiersBuilt;
                            break;
                        }
                    }
                }
            }
        }
        if(rc.getRoundNum() % 2 == 0) rc.writeSharedArray(5, 0);
    }

}

