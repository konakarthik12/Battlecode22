package monkey2clone;

import battlecode.common.*;

class Miner {

    static int numReached = 0;
    static Direction previousStep = Direction.CENTER;
    static MapLocation destination = null;
    static MapLocation spawn = null;
    static int near = 0;
    static int visibleEnemies = 0;
    static int visibleAllies = 0;
    static int visibleAttackers = 0;
    static int lead = 0;

    static int turnsSearching = 0;
    static boolean isSearching = true;
    static boolean isMinID = true;

    static void setup(RobotController rc) throws GameActionException {
        spawn = rc.getLocation();
        int dist = 63;
        if (rc.getRoundNum() <= 10) {
            for (MapLocation loc : rc.senseNearbyLocationsWithLead()) {
                if (loc.distanceSquaredTo(rc.getLocation()) < dist) {
                    destination = loc;
                    dist = loc.distanceSquaredTo(rc.getLocation());
                }
            }
        } else {
            readQuadrant(rc);
        }
    }

    static void move(RobotController rc) throws GameActionException {
        // experiment with returning to spawn and with running directly opposite to soldiers
        for (RobotInfo info : rc.senseNearbyRobots(-1, rc.getTeam().opponent())) {
            if (info.type.equals(RobotType.SOLDIER)) {
                int dist = rc.getLocation().distanceSquaredTo(info.location);
                int curr = rc.senseRubble(rc.getLocation());
                Direction best = null;
                for (Direction dir: Constants.directions) {
                    MapLocation adj = rc.adjacentLocation(dir);
                    if (rc.canSenseLocation(adj) && rc.adjacentLocation(dir).distanceSquaredTo(info.location) > dist && rc.senseRubble(adj) <= curr) {
                        curr = rc.senseRubble(adj);
                        best = dir;
                    }
                }
                if (best == null || !rc.canMove(best)) {
                    Pathfinder.move(rc, spawn);
                } else rc.move(best);
                return;
            }
        }
        /*for (RobotInfo info : rc.senseNearbyRobots(-1, rc.getTeam().opponent())) {
            if (info.type.equals(RobotType.SOLDIER)) {
                Pathfinder.move(rc, spawn);
                return;
            }
        }*/
        Pathfinder.move(rc, destination);
    }

    static void setDestination(RobotController rc) throws GameActionException {
        if (destination == null || (rc.canSenseLocation(destination)) && rc.senseLead(destination) <= 5) {
            ++numReached;
//            destination = new MapLocation((Utils.rng.nextInt(rc.getMapWidth()) - 6) + 3, (Utils.rng.nextInt(rc.getMapHeight() - 6) + 3));
            destination = new MapLocation(Utils.randomInt(0, rc.getMapWidth()-1), Utils.randomInt(0, rc.getMapHeight()-1));
        }

        int x = rc.readSharedArray(34);
        if (rc.getHealth() < 4) rc.writeSharedArray(34, Math.max(x-1, 0));
        rc.setIndicatorString(destination.toString());
        rc.setIndicatorLine(rc.getLocation(), destination, 255, 255, 255);
    }

    static void readQuadrant(RobotController rc) throws GameActionException {
        for (int quadrant = 2; quadrant <= 17; ++quadrant) {
            int temp = rc.readSharedArray(quadrant);
            int lead = (rc.readSharedArray(quadrant + 16) >> 8) & 255;
            int visAllies = temp & 255;
            int visEnemies = (temp >> 8) & 255;

            int x = (quadrant - 2) / 4 * rc.getMapWidth()  / 4 + Utils.randomInt(0, rc.getMapWidth()/4);
            int y = (quadrant - 2) % 4 * rc.getMapHeight() / 4 + Utils.randomInt(0, rc.getMapHeight()/4);

            if (lead > 5 && visAllies >= visEnemies) {
//            if (Math.abs(visAttackers-visAllies) < 3) {
                destination = new MapLocation(x,y);
            }
        }
    }
    static void writeQuadrantInformation(RobotController rc) throws GameActionException {
        // TODO test no random as well
        MapLocation cur = rc.getLocation();
        int quadrant = 4 * (4 * cur.x / rc.getMapWidth()) + (4 * cur.y / rc.getMapHeight());
        // indices 2 - 17 are quadrant information for enemies and allies
        int writeValue = (visibleEnemies << 8) + visibleAllies;
        if (Utils.randomInt(1, visibleAllies) <= 1)
            rc.writeSharedArray(2 + quadrant, writeValue);

        writeValue = (lead << 8) + visibleAttackers;
        if (Utils.randomInt(1, visibleAllies) <= 1)
            rc.writeSharedArray(18 + quadrant, writeValue);

        assert(quadrant < 16);
    }

    static void readSharedArray(RobotController rc) throws GameActionException {

    }

    static void senseEnemies(RobotController rc) throws GameActionException {
//        int enemies = 0;
//        int attackers = 0;
        visibleEnemies = 0;
        visibleAttackers = 0;
        visibleAllies = 0;
//        visibleAllies = 1 + rc.senseNearbyRobots(RobotType.MINER.visionRadiusSquared, rc.getTeam()).length;
//        for (RobotInfo info : rc.senseNearbyRobots(RobotType.MINER.visionRadiusSquared, rc.getTeam().opponent())) {
        for (RobotInfo info : rc.senseNearbyRobots(-1)) {
            if (info.team.equals(rc.getTeam())) {
                if (info.type.equals(RobotType.SOLDIER)) ++visibleAllies;
            } else {
                if (info.type == RobotType.SOLDIER) {
                    for (int i = 1; i <= rc.getArchonCount(); i++) {
                        int fromShared = rc.readSharedArray(64 - i);
                        MapLocation arLoc = new MapLocation((fromShared >> 6) & 63, fromShared & 63);
                        fromShared = rc.readSharedArray(37 + i);
                        if (info.location.distanceSquaredTo(arLoc) < fromShared) {
                            rc.writeSharedArray(33 + i, (info.location.x << 6) + info.location.y);
                            rc.writeSharedArray(37 + i, info.location.distanceSquaredTo(arLoc));
                        }
                    }
                }
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

    static void senseAllies(RobotController rc) throws  GameActionException {
        isMinID = true;
        near = 0;
        for (RobotInfo info : rc.senseNearbyRobots(-1, rc.getTeam())) {
            if (info.getType().equals(RobotType.MINER)) {
                ++near;
                if (info.getID() < rc.getID()) isMinID = false;
            }
        }
    }

    static void mine(RobotController rc) throws GameActionException {
        //TODO make miners not clump up
        MapLocation[] leadLocations = rc.senseNearbyLocationsWithLead();
        lead = leadLocations.length;
        int hiLead = 5;
        for (MapLocation loc : leadLocations) {
            while (rc.canMineLead(loc) && rc.senseLead(loc) > 1) rc.mineLead(loc);
            if (rc.canSenseLocation(loc) && rc.senseLead(loc) > hiLead && isMinID && Utils.randomInt(1, near) <= 1) {
                destination = loc;
                hiLead = rc.senseLead(loc);
            }
//            if (rc.canSenseLocation(loc) && rc.senseLead(loc) > 5 && isMinID) destination = loc;
            // maybe don't need to sense
        }
    }

    static void writeShared(RobotController rc) throws GameActionException {

    }

    static void run(RobotController rc) throws GameActionException {
        senseAllies(rc);
        senseEnemies(rc);
        setDestination(rc);
        move(rc);
        mine(rc);

        writeQuadrantInformation(rc);

//        for (RobotInfo robotInfo : rc.senseNearbyRobots(-1, rc.getTeam().opponent())) {
//            rc.writeSharedArray(Utils.randomInt(0, 4), (1 << 12) + (robotInfo.location.x << 6) + robotInfo.location.y);
//            break;
//        }
    }
}
