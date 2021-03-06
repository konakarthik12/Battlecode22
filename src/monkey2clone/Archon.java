package monkey2clone;

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
    static int sinceMove = 50;
    // TODO: test adding ceil(enemies/(visible allies)) or other averaging schemes
    static int[] enemyEstimates = new int[64];
    static MapLocation destination = null;
    static MapLocation lowRubble = null;
    static boolean turret = true;

    static void summonUnitAnywhere(RobotController rc, RobotType type) throws GameActionException {
        Direction build = Direction.EAST;
        int rubble = 1000000;
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
        if (!turret && (rc.getLocation().equals(lowRubble) || (lowRubble == null && rc.getLocation().equals(destination)))) {
            rc.writeSharedArray(58, rc.readSharedArray(58) - 1);
            destination = null;
            lowRubble = null;
            turret = true;
        }
        if (turret && rc.getMode() != RobotMode.TURRET) {
            if (rc.isTransformReady()) rc.transform();
        }
        if (archonID == rc.getArchonCount()) {
            for (int i = 2; i < 58; ++i) {
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
        if (rc.getRoundNum() <= 4) {
            if (visibleEnemies > 0) summonUnitAnywhere(rc, RobotType.SOLDIER);
            if (leadLoc.length > 0) {
                System.out.println(leadLoc.length + " ?? " + rc.getRoundNum());
                int lowRubble = Integer.MAX_VALUE;
                Direction go = rc.getLocation().directionTo(leadLoc[0]);
                Direction build = go;
                if (rc.canSenseLocation(rc.adjacentLocation(go))) {
                    if (rc.senseRubble(rc.adjacentLocation(go)) < lowRubble) {
                        lowRubble = rc.senseRubble(rc.adjacentLocation(go));
                    }
                }
                if (rc.canSenseLocation(rc.adjacentLocation(go.rotateLeft()))) {
                    if (rc.senseRubble(rc.adjacentLocation(go.rotateLeft())) < lowRubble) {
                        lowRubble = rc.senseRubble(rc.adjacentLocation(go.rotateLeft()));
                        build = go.rotateLeft();
                    }
                }
                if (rc.canSenseLocation(rc.adjacentLocation(go.rotateRight()))) {
                    if (rc.senseRubble(rc.adjacentLocation(go.rotateRight())) < lowRubble) {
                        lowRubble = rc.senseRubble(rc.adjacentLocation(go.rotateRight()));
                        build = go.rotateRight();
                    }
                }
                if (rc.canBuildRobot(RobotType.MINER, build)) {
                    rc.buildRobot(RobotType.MINER, build);
                    ++minersBuilt;
                }
            } else {
                System.out.println("HI???");
                summonUnitAnywhere(rc, RobotType.MINER);
            }
        }
        else {
            // TODO don't spawn on rubble :skull:
            int lead = 0;
            for (MapLocation loc : leadLoc) {
                lead += rc.senseLead(loc);
            }
            if (lead > 100 && visibleMiners == 0 && visibleAllies > visibleAttackers) {
                int lowRubble = Integer.MAX_VALUE;
                Direction go = rc.getLocation().directionTo(leadLoc[Utils.randomInt(0, leadLoc.length-1)]);
                Direction build = go;
                if (rc.canSenseLocation(rc.adjacentLocation(go))) {
                    if (rc.senseRubble(rc.adjacentLocation(go)) < lowRubble) {
                        lowRubble = rc.senseRubble(rc.adjacentLocation(go));
                    }
                }
                if (rc.canSenseLocation(rc.adjacentLocation(go.rotateLeft()))) {
                    if (rc.senseRubble(rc.adjacentLocation(go.rotateLeft())) < lowRubble) {
                        lowRubble = rc.senseRubble(rc.adjacentLocation(go.rotateLeft()));
                        build = go.rotateLeft();
                    }
                }
                if (rc.canSenseLocation(rc.adjacentLocation(go.rotateRight()))) {
                    if (rc.senseRubble(rc.adjacentLocation(go.rotateRight())) < lowRubble) {
                        lowRubble = rc.senseRubble(rc.adjacentLocation(go.rotateRight()));
                        build = go.rotateRight();
                    }
                }
                if (rc.canBuildRobot(RobotType.MINER, build)) {
                    rc.buildRobot(RobotType.MINER, build);
                    ++minersBuilt;
                }
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
            if (visAttackers > 1 && visAllies > 2 && rc.getLocation().distanceSquaredTo(target) < dist) {
                dist = rc.getLocation().distanceSquaredTo(target);
                destination = target;
            }
        }
        if (dist > 50 && rc.readSharedArray(58) < rc.getArchonCount() - 1
            && turret && destination != null && sinceMove >= 100 && visibleAllies < 2) {
            turret = false;
            rc.writeSharedArray(58, rc.readSharedArray(58) + 1);
        } else if (rc.getRoundNum() >= 50 && rc.senseRubble(rc.getLocation()) >= 30 && turret && rc.readSharedArray(58) < rc.getArchonCount() - 1) {
            MapLocation cur = rc.getLocation();
            int rubble = rc.senseRubble(cur);
            dist = Integer.MAX_VALUE;
            for (MapLocation poss : rc.getAllLocationsWithinRadiusSquared(cur, 8)) {
                if (((cur.distanceSquaredTo(poss) < dist && rc.senseRubble(poss) <= rubble) || rc.senseRubble(poss) < rubble) 
                && !rc.canSenseRobotAtLocation(poss)) {
                    lowRubble = poss;
                    dist = cur.distanceSquaredTo(poss);
                    rubble = rc.senseRubble(poss);
                }
            }
            if (rc.senseRubble(cur) - rubble >= 30 && visibleAllies > 2) {
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
            for (MapLocation poss : rc.getAllLocationsWithinRadiusSquared(cur, 8)) {
                if (rc.senseRubble(poss) < rubble) {
                    lowRubble = poss;
                    rubble = rc.senseRubble(poss);
                }
            }
        }
        if (lowRubble != null) {
            if (rc.canSenseRobotAtLocation(lowRubble) && cur.isAdjacentTo(lowRubble)) {
                Direction go = Direction.CENTER;
                int rubble = rc.senseRubble(cur);
                for (Direction dir : Constants.directions) {
                    if (rc.canMove(dir) && rc.senseRubble(rc.adjacentLocation(dir)) <= rubble) {
                        rubble = rc.senseRubble(rc.adjacentLocation(dir));
                        go = dir;
                    }
                }
                if (rc.canMove(go)) {
                    lowRubble = rc.adjacentLocation(go);
                    rc.move(go);
                }
            } else {
                Pathfinder.move(rc, lowRubble);
            }
        } else {
            Pathfinder.move(rc, destination);
        }
    }

    static void run(RobotController rc) throws GameActionException {
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
