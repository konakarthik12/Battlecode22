package monkey2newmienr;

import battlecode.common.*;

public class Archon {

    static int archonID = 0;
    static int minersBuilt = 0;
    static int soldiersBuilt = 0;
    static int visibleEnemies = 0;
    static int visibleAllies = 0;
    static int visibleAttackers = 0;
    static int visibleMiners = 0;
    static int toHeal = 0;
    // TODO: test adding ceil(enemies/(visible allies)) or other averaging schemes
    static int[] enemyEstimates = new int[64];
    static MapLocation destination = null;

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
            if (type == RobotType.MINER) ++minersBuilt;
            if (type == RobotType.SOLDIER) ++soldiersBuilt;
        }
    }

    static void heal(RobotController rc) throws GameActionException {
        RobotInfo[] info = rc.senseNearbyRobots(20, rc.getTeam());
        int lowest = 1000;
        RobotInfo best = null;
        for (RobotInfo r : info) {
            if (toHeal == 0) {
                if (r.health < lowest && r.type == RobotType.SOLDIER) {
                    lowest = r.health;
                    best = r;
                }
            } else if (r.ID == toHeal) {
                best = r;
            }
        }
        if (best != null) {
            if (rc.canRepair(best.location)) {
                rc.repair(best.location);
                if (best.health >= 44) toHeal = 0;
                else toHeal = best.ID;
            }
        } else toHeal = 0;
    }

    static void setup(RobotController rc) throws GameActionException {
        int ind = 63;
        while (rc.readSharedArray(ind) != 0) --ind;
        archonID = 64 - ind;
        rc.writeSharedArray(ind, (1 << 15) + (rc.getLocation().x << 6) + rc.getLocation().y);
    }

    static void reset(RobotController rc) throws GameActionException {
        for (int i = 2; i <= 17; ++i) {
            enemyEstimates[i] = (enemyEstimates[i]*4 + rc.readSharedArray(i))/5;
        }
        if (archonID == rc.getArchonCount()) {
            for (int i = 2; i < 58; ++i) {
                int fromShared = rc.readSharedArray(i);
                if ((fromShared >> 15) == 1) {
                    rc.writeSharedArray(i, fromShared - (1 << 15));
                } else rc.writeSharedArray(i, 0);
            }
        }
    }

    static void summonUnits(RobotController rc) throws GameActionException {
        MapLocation[] leadLoc = rc.senseNearbyLocationsWithLead();
        if (rc.getRoundNum() <= 4) {
            if (visibleEnemies > 0) {
                summonUnitAnywhere(rc, RobotType.SOLDIER);
            }
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
            }
        }
        else {
            // TODO don't spawn on rubble :skull:
            int lead = 0;
            for (MapLocation loc : leadLoc) {
                lead += rc.senseLead(loc);
            }
            if (lead > 100 && visibleMiners == 0 && visibleAttackers == 0) {
                Direction go = rc.getLocation().directionTo(leadLoc[0]);
                if (rc.canBuildRobot(RobotType.MINER, go)) rc.buildRobot(RobotType.MINER, go);
                else {
                    go = go.rotateLeft();
                    if (rc.canBuildRobot(RobotType.MINER, go)) rc.buildRobot(RobotType.MINER, go);
                    go = go.rotateRight().rotateRight();
                    if (rc.canBuildRobot(RobotType.MINER, go)) rc.buildRobot(RobotType.MINER, go);
                }
                ++minersBuilt;
            } else if (rc.readSharedArray(1) < rc.readSharedArray(0) - 10 || visibleEnemies > 0) {
                summonUnitAnywhere(rc, RobotType.SOLDIER);
                rc.writeSharedArray(1, rc.readSharedArray(1) + 1);
//            } else if (Utils.randomInt(1, rc.getArchonCount() * 2) <= 1) {
            } else if (Utils.randomInt(1, minersBuilt + rc.getArchonCount() - 1) <= 1) {
                summonUnitAnywhere(rc, RobotType.MINER);
                //rc.writeSharedArray(34, rc.readSharedArray(34) + 1);
            } else {
                summonUnitAnywhere(rc, RobotType.SOLDIER);
                rc.writeSharedArray(1, rc.readSharedArray(1) + 1);
            }
            //if (rc.getRoundNum() >= 300 && rc.getRoundNum() % 20 == 0) minersBuilt--;
//            int x = rc.readSharedArray(1);
//            if (rc.getRoundNum() % 10 == 0) rc.writeSharedArray(1, Math.max(x-1, 0));
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

    static void readQuadrant(RobotController rc) throws GameActionException {
        int dist = Integer.MAX_VALUE;
        int ddist = Integer.MAX_VALUE;
        for (int quadrant = 2; quadrant <= 26; ++quadrant) {
            int visAttackers = rc.readSharedArray(quadrant + 25) & 127;
            int visEnemies = (rc.readSharedArray(quadrant) >> 8) & 127;
            int visAllies = (rc.readSharedArray(quadrant)) & 127;
            int x = (quadrant - 2) / 5 * rc.getMapWidth() / 5 + rc.getMapWidth()/10;
            int y = (quadrant - 2) % 5 * rc.getMapHeight() / 5 + rc.getMapHeight()/10;
            MapLocation target = new MapLocation(x, y);
            if (visAllies > 1 && visEnemies > 1 && rc.getLocation().distanceSquaredTo(target) < dist) {
                dist = rc.getLocation().distanceSquaredTo(target);
            } else if (visAllies > 5) {
                destination = target;
            }
        }
        if (dist > Math.min(500, rc.getMapWidth() * rc.getMapHeight() / 4) && rc.readSharedArray(58) < rc.getArchonCount() - 1
            && rc.getMode() == RobotMode.TURRET && destination != null) {
            if (rc.canTransform()) rc.transform();
        }
    }

    static void move(RobotController rc) throws GameActionException {

    }

    static void run(RobotController rc) throws GameActionException {
        senseEnemies(rc);
        summonUnits(rc);
        heal(rc);
        readQuadrant(rc);
        move(rc);
        reset(rc);
    }
}
