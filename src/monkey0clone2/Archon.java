package monkey0clone2;

import battlecode.common.*;

public class Archon {
    static int archonCount = 0;
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
        if (rc.canBuildRobot(type, build)) {
            if (type == RobotType.MINER) {
                rc.writeSharedArray(34, rc.readSharedArray(34) + 1);
                rc.writeSharedArray(56, 0);
                ++minersBuilt;
            }
            if (type == RobotType.SOLDIER) {
                if ((rc.readSharedArray(55) & 1) > 0) return;
                rc.writeSharedArray(1, rc.readSharedArray(1) + 1);
                ++soldiersBuilt;
            }
            if (type == RobotType.BUILDER) {
                ++buildersBuilt;
            }
            if (type == RobotType.SAGE) {
                rc.writeSharedArray(57, rc.readSharedArray(57) + 1);
                ++sagesBuilt;
            }
            rc.buildRobot(type, build);
        }
    }

    static void heal(RobotController rc) throws GameActionException {
        RobotInfo[] info = rc.senseNearbyRobots(20, rc.getTeam());
        int lowest = 1000;
        RobotInfo best = null;
        for (RobotInfo r : info) {
            if (toHeal == 0) {
                if (r.type == RobotType.SOLDIER && r.health < lowest) {
                    best = r;
                    lowest = r.health;
                } else if (r.type == RobotType.SAGE && (r.health+1)/2 < lowest) {
                    best = r;
                    lowest = (r.health+1)/2;
                }
            } else if (r.ID == toHeal) {
                best = r;
            }
        }
        if (best != null) {
            if (rc.canRepair(best.location)) {
                toHeal = best.ID;
                rc.repair(best.location);
                if (best.type == RobotType.SOLDIER && best.health >= 44) toHeal = 0;
                if (best.type == RobotType.SAGE && best.health >= 94) toHeal = 0;
            }
        } else toHeal = 0;
    }

    static void setup(RobotController rc) throws GameActionException {
        archonCount = rc.getArchonCount();
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
        if (rc.getArchonCount() < archonCount) {
            if (rc.readSharedArray(64 - archonCount) != 0) {
                for (int i = 63; i > 59; i--) {
                    rc.writeSharedArray(i, 0);
                }
            }
            archonCount = rc.getArchonCount();
            int ind = 63;
            while (rc.readSharedArray(ind) != 0) --ind;
            archonID = 64 - ind;
            rc.writeSharedArray(ind, (1 << 15) + (rc.getLocation().x << 6) + rc.getLocation().y);
        }
        if (archonID == rc.getArchonCount()) {
            for (int i = 2; i < 34; ++i) {
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
        if (rc.readSharedArray(34) < (rc.readSharedArray(57))) { 
            rc.writeSharedArray(56, 1);
        }
        if (rc.readSharedArray(34) < 4) {
            if (leadLoc.length > 0) {
                Direction go = rc.getLocation().directionTo(leadLoc[0]);

                if (rc.canBuildRobot(RobotType.MINER, go)) {
                    rc.buildRobot(RobotType.MINER, go);
                    rc.writeSharedArray(34, rc.readSharedArray(34) + 1);
                    ++minersBuilt;
                }
                else {
                    go = go.rotateLeft();
                    if (rc.canBuildRobot(RobotType.MINER, go)) {
                        rc.buildRobot(RobotType.MINER, go);
                        ++minersBuilt;
                        rc.writeSharedArray(34, rc.readSharedArray(34) + 1);
                    }
                    go = go.rotateRight().rotateRight();
                    if (rc.canBuildRobot(RobotType.MINER, go)) {
                        rc.buildRobot(RobotType.MINER, go);
                        ++minersBuilt;
                        rc.writeSharedArray(34, rc.readSharedArray(34) + 1);
                    }
                }
                if (go == Direction.CENTER) summonUnitAnywhere(rc, RobotType.MINER);
            } else {
                summonUnitAnywhere(rc, RobotType.MINER);
            }
        }
        else {
            if (buildersBuilt == 0 && myTurn) {
                int nid = 0;
                int myDist = Utils.cornerDist(rc, rc.getLocation());

                for (int i = 1; i <= rc.getArchonCount(); i++) {
                    MapLocation loc = new MapLocation((rc.readSharedArray(64 - i) >> 6) & 63, rc.readSharedArray(64 - i) & 63);
                    int oDist = Utils.cornerDist(rc, loc);
                    if (myDist > oDist || (myDist == oDist && archonID >= i))nid++;
                }
                if (nid <= 1) summonUnitAnywhere(rc, RobotType.BUILDER);
            }
            // TODO don't spawn on rubble :skull:
            int lead = 0;
            for (MapLocation loc : leadLoc) {
                lead += rc.senseLead(loc);
            }
            if (lead > 100 && visibleMiners == 0 && visibleAllies > visibleAttackers) {
                Direction go = rc.getLocation().directionTo(leadLoc[0]);

                if (rc.canBuildRobot(RobotType.MINER, go)) {
                    rc.buildRobot(RobotType.MINER, go);
                    ++minersBuilt;
                }
                else {
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
                if (go == Direction.CENTER) summonUnitAnywhere(rc, RobotType.MINER);
            } else if (myTurn && rc.getTeamGoldAmount(rc.getTeam()) >= 20) {
                summonUnitAnywhere(rc, RobotType.SAGE);
            } else if (rc.readSharedArray(56) > 0 || (myTurn && ((rc.readSharedArray(55) & 1) == 0 || rc.getTeamLeadAmount(rc.getTeam()) >= 230)
            && rc.readSharedArray(34) - 4 <= rc.readSharedArray(1) + rc.readSharedArray(57))) {
                summonUnitAnywhere(rc, RobotType.MINER);
            } else if (myTurn) {
                summonUnitAnywhere(rc, RobotType.SOLDIER);
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
        } else if ((rc.senseRubble(rc.getLocation()) >= 30 && turret && rc.readSharedArray(58) < rc.getArchonCount() - 1)) {
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
            UnrolledPathfinder.move(rc, lowRubble);
        } else {
            UnrolledPathfinder.move(rc, destination);
        }
    }

    static void run(RobotController rc) throws GameActionException {
        myTurn = (rc.getRoundNum() % (rc.getArchonCount()) + 1) == archonID;
        if (rc.getTeamGoldAmount(rc.getTeam()) >= 40 
        || rc.getTeamLeadAmount(rc.getTeam()) >= 200) myTurn = true;
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


