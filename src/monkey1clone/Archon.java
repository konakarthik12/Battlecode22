package monkey1clone;

import battlecode.common.*;

public class Archon {

    static int archonID = 0;
    static MapLocation old = null;
    static int minersBuilt = 0;
    static int soldiersBuilt = 0;
    static int buildersBuilt = 0;
    static int sagesBuilt = 0;
    static int visibleEnemies = 0;
    static int visibleAllies = 0;
    static int visibleAttackers = 0;
    static int visibleMiners = 0;
    static int toHeal = 0;
    static int sinceMove = 50;

    static boolean myTurn = true;
    // TODO: test adding ceil(enemies/(visible allies)) or other averaging schemes
    static int[] enemyEstimates = new int[64];
    static MapLocation destination = null;
    static MapLocation lowRubble = null;
    static boolean turret = true;

    static void summonUnitAnywhere(RobotController rc, RobotType type) throws GameActionException {
        Direction build = Direction.EAST;
        int rubble = 1000;
        MapLocation center = new MapLocation(rc.getMapWidth() / 2, rc.getMapHeight() / 2);
        for (Direction dir : Constants.directions) {
            if (rc.canBuildRobot(type, dir)) {
                int _rubble = rc.senseRubble(rc.adjacentLocation(dir))/10 + rc.adjacentLocation(dir).distanceSquaredTo(center);
                if (_rubble < rubble) {
                    build = dir;
                    rubble = _rubble;
                }
            }
        }
        if (type == RobotType.BUILDER) {
            for (Direction dir : Constants.directions) {
                if (rc.canBuildRobot(type, dir)) {
                    int _rubble = rc.senseRubble(rc.adjacentLocation(dir))/10 + Utils.cornerDist(rc, rc.adjacentLocation(dir));
                    if (_rubble < rubble) {
                        build = dir;
                        rubble = _rubble;
                    }
                }
            }
        }
        if (type == RobotType.SOLDIER) {
            if (rc.getTeamGoldAmount(rc.getTeam()) >= 20 || (sagesBuilt == 0 && soldiersBuilt > 0 && rc.getTeamLeadAmount(rc.getTeam()) < 200)) {
                type = RobotType.SAGE;
            }
        }
        int temp = rc.readSharedArray(56);
        if (rc.canBuildRobot(type, build)) {
            rc.buildRobot(type, build);
            if (type == RobotType.MINER) {
                rc.writeSharedArray(56, 0);
                ++minersBuilt;
            }
            if (type == RobotType.SOLDIER) {
                rc.writeSharedArray(56, temp + 2);
                ++soldiersBuilt;
            }
            if (type == RobotType.BUILDER) {
                rc.writeSharedArray(56, temp + 2);
                ++buildersBuilt;
            }
            if (type == RobotType.SAGE) {
                rc.writeSharedArray(56, temp + 2);
                ++sagesBuilt;
            }
        }
        temp = rc.readSharedArray(56);
        if (temp / 2 > rc.readSharedArray(34) + 1) {
            rc.writeSharedArray(56, temp | 1);
        }
    }

    static void heal(RobotController rc) throws GameActionException {
        RobotInfo[] info = rc.senseNearbyRobots(20, rc.getTeam());
        int lowest = 1000;
        int score = Integer.MAX_VALUE;
        RobotInfo best = null;
        for (RobotInfo r : info) {
            int multiplier = 1;
            if (r.type == RobotType.SOLDIER || r.type == RobotType.SAGE) multiplier = 0;
            int _score = multiplier * 10000 + r.health;
            if (toHeal == 0) {
                if (_score < score) {
                    score = _score;
                    best = r;
                }
//                if ((r.type == RobotType.SOLDIER || r.type == RobotType.SAGE) && r.health < lowest) {
//                    best = r;
//                    lowest = r.health;
//                }
            } else if (r.ID == toHeal) {
                best = r;
            }
        }
        if (best != null && rc.canRepair(best.location)) rc.repair(best.location);
//        if (best != null) {
//            if (rc.canRepair(best.location)) {
//                rc.repair(best.location);
//                if (best.type == RobotType.SOLDIER && rc.getHealth() >= 44) toHeal = 0;
//                if (best.type == RobotType.SAGE && rc.getHealth() >= 94) toHeal = 0;
//
//                else toHeal = best.ID;
//            }
//        } else toHeal = 0;
    }

    static void setup(RobotController rc) throws GameActionException {
        old = rc.getLocation();
        int ind = 63;
        while (rc.readSharedArray(ind) != 0) --ind;
        archonID = 64 - ind;
        rc.writeSharedArray(ind, (1 << 15) + (rc.getLocation().x << 6) + rc.getLocation().y);
    }

    static void reset(RobotController rc) throws GameActionException {
        if (!turret && (rc.getLocation().equals(lowRubble) || (lowRubble == null && rc.getLocation().equals(destination)))) {
            rc.writeSharedArray(58, rc.readSharedArray(58) - 1);
            destination = null;
            lowRubble = null;
            turret = true;
            old = rc.getLocation();
        }
        if (turret && rc.getMode() != RobotMode.TURRET) {
            if (rc.isTransformReady()) rc.transform();
        }
        for (int i = 2; i <= 17; ++i) {
            enemyEstimates[i] = (enemyEstimates[i]*4 + rc.readSharedArray(i))/5;
        }
        if (archonID == rc.getArchonCount()) {
            for (int i = 2; i < 35; ++i) {
                int fromShared = rc.readSharedArray(i);
                if ((fromShared >> 15) == 1) {
                    rc.writeSharedArray(i, fromShared - (1 << 15));
                } else rc.writeSharedArray(i, 0);
            }
        }
        rc.writeSharedArray(64 - archonID, (1 << 15) + (rc.getLocation().x << 6) + rc.getLocation().y);
    }

    static void summonUnits(RobotController rc) throws GameActionException {
        MapLocation[] leadLoc = rc.senseNearbyLocationsWithLead();
        if (minersBuilt <= 1) {
            if (visibleEnemies > 0) {
                summonUnitAnywhere(rc, RobotType.SOLDIER);
            }
            if (leadLoc.length > 0) {
                Direction go = rc.getLocation().directionTo(leadLoc[0]);
                if (go == Direction.CENTER) summonUnitAnywhere(rc, RobotType.MINER);
                else {
                    if (rc.canBuildRobot(RobotType.MINER, go)) {
                        rc.buildRobot(RobotType.MINER, go);
                        ++minersBuilt;
                    } else {
                        go = go.rotateLeft();
                        if (rc.canBuildRobot(RobotType.MINER, go)) {
                            rc.buildRobot(RobotType.MINER, go);
                            ++minersBuilt;
                        }
                        go = go.rotateRight().rotateRight();
                        if (rc.canBuildRobot(RobotType.MINER, go)) {
                            rc.buildRobot(RobotType.MINER, go);
                            ++minersBuilt;
                        }
                    }
                }
            } else {
                summonUnitAnywhere(rc, RobotType.MINER);
            }
        }
        else {
            if (buildersBuilt == 0 && myTurn && archonID <= (rc.getArchonCount() + 1) / 2) {
                summonUnitAnywhere(rc, RobotType.BUILDER);
            }
            // TODO don't spawn on rubble :skull:
            int lead = 0;
            for (MapLocation loc : leadLoc) {
                lead += rc.senseLead(loc);
            }
            if (lead > 100 && visibleMiners == 0 && visibleAllies > visibleAttackers) {
                Direction go = rc.getLocation().directionTo(leadLoc[0]);
                Direction left = go.rotateLeft();
                Direction right = go.rotateRight();
                if (rc.canBuildRobot(RobotType.MINER, go)) rc.buildRobot(RobotType.MINER, go);
                else {
                    go = go.rotateLeft();
                    if (rc.canBuildRobot(RobotType.MINER, go)) rc.buildRobot(RobotType.MINER, go);
                    go = go.rotateRight().rotateRight();
                    if (rc.canBuildRobot(RobotType.MINER, go)) rc.buildRobot(RobotType.MINER, go);
                }
                ++minersBuilt;
            } else if (visibleEnemies > 0 || rc.getTeamLeadAmount(rc.getTeam()) >= 150) {
                summonUnitAnywhere(rc, RobotType.SOLDIER);
                rc.writeSharedArray(1, rc.readSharedArray(1) + 1);
            } else if (myTurn && (rc.readSharedArray(56) & 1) == 1) {
                summonUnitAnywhere(rc, RobotType.MINER);
            } else if (myTurn) {
                summonUnitAnywhere(rc, RobotType.SOLDIER);
                rc.writeSharedArray(1, rc.readSharedArray(1) + 1);
            }
        }
    }

    static void senseEnemies(RobotController rc) throws GameActionException {
        visibleEnemies = 0;
        visibleAttackers = 0;
        visibleAllies = 0;
        visibleMiners = 0;

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
            int visAttackers = rc.readSharedArray(quadrant) & 31;
            int visAllies = (rc.readSharedArray(quadrant) >> 5) & 31;
            int x = (quadrant - 2) / 5 * rc.getMapWidth() / 5 + rc.getMapWidth()/10;
            int y = (quadrant - 2) % 5 * rc.getMapHeight() / 5 + rc.getMapHeight()/10;
            MapLocation target = new MapLocation(x, y);
            if (visAttackers > 1 && visAllies > 1 && rc.getLocation().distanceSquaredTo(target) < dist) {
                dist = rc.getLocation().distanceSquaredTo(target);
                destination = target;
            }
        }
        //System.out.println(dist + " " + visibleAllies + " " + rc.readSharedArray(58));
        if (dist > 50 && rc.readSharedArray(58) < rc.getArchonCount() - 1
            && turret && destination != null && sinceMove >= 50 && visibleAllies < 2) {
            //System.out.println("HI?");
            turret = false;
            rc.writeSharedArray(58, rc.readSharedArray(58) + 1);
        } else if (rc.senseRubble(rc.getLocation()) >= 30 && turret && rc.readSharedArray(58) < rc.getArchonCount() - 1) {
            MapLocation cur = rc.getLocation();
            int rubble = rc.senseRubble(cur);
            dist = Integer.MAX_VALUE;
            for (MapLocation poss : rc.getAllLocationsWithinRadiusSquared(cur, 20)) {
                if (((cur.distanceSquaredTo(poss) < dist && rc.senseRubble(poss) <= rubble) || rc.senseRubble(poss) < rubble) 
                && !rc.canSenseRobotAtLocation(poss)) {
                    lowRubble = poss;
                    dist = cur.distanceSquaredTo(poss);
                    rubble = rc.senseRubble(poss);
                }
            }
            if (rc.senseRubble(cur) - rubble >= 30) {
                turret = false;
                rc.writeSharedArray(58, rc.readSharedArray(58) + 1);
            } else {
                lowRubble = null;
            }   
        }
    }

    static void move(RobotController rc) throws GameActionException {
        if (!turret && rc.getMode() == RobotMode.TURRET) {
            if (rc.canTransform()) rc.transform();
        } else if (turret || rc.getMode() == RobotMode.TURRET) return;
        MapLocation cur = rc.getLocation();
        if ((visibleAllies > 3 || visibleAttackers > 0) && lowRubble == null) {
            int rubble = rc.senseRubble(cur);
            int dist = Integer.MAX_VALUE;
            for (MapLocation poss : rc.getAllLocationsWithinRadiusSquared(cur, 8)) {
                if (((rc.senseRubble(poss) == rubble && cur.distanceSquaredTo(old) < dist) || 
                rc.senseRubble(poss) < rubble) && !rc.canSenseRobotAtLocation(poss)) {
                    dist = cur.distanceSquaredTo(old);
                    lowRubble = poss;
                    rubble = rc.senseRubble(poss);
                }
            }
        }
        if (lowRubble != null) {
            if (rc.canSenseRobotAtLocation(lowRubble) && cur.isAdjacentTo(lowRubble)) {
                int rubble = rc.senseRubble(cur);
                int dist = Integer.MAX_VALUE;
                lowRubble = rc.getLocation();
                for (MapLocation poss : rc.getAllLocationsWithinRadiusSquared(cur, 8)) {
                    if (((rc.senseRubble(poss) == rubble && cur.distanceSquaredTo(old) < dist) || 
                    rc.senseRubble(poss) < rubble) && !rc.canSenseRobotAtLocation(poss)) {
                        dist = cur.distanceSquaredTo(old);
                        lowRubble = poss;
                        rubble = rc.senseRubble(poss);
                    }
                }
            }
            Pathfinder.move(rc, lowRubble);
        } else {
            Pathfinder.move(rc, destination);
        }
    }

    static void run(RobotController rc) throws GameActionException {
        if (rc.getTeamLeadAmount(rc.getTeam()) > 300 && rc.canMutate(rc.getLocation())) rc.mutate(rc.getLocation());

        myTurn = (rc.getRoundNum() % (rc.getArchonCount()) + 1) == archonID;
        senseEnemies(rc);
        summonUnits(rc);
        move(rc);
        heal(rc);
        readQuadrant(rc);
        reset(rc);
        if (lowRubble != null) rc.setIndicatorLine(rc.getLocation(), lowRubble, 0, 0, 255);
        else if (destination != null) rc.setIndicatorLine(rc.getLocation(), destination, 255, 255, 255);
        if (turret) {
            sinceMove++;
        } else {
            sinceMove = 0;
        }
    }
}
