package monkey6;

import battlecode.common.*;

class Miner {

    static MapLocation destination = null;
    static MapLocation spawn = null;
    static int near = 0;
    static int visibleEnemies = 0;
    static int visibleAllies = 0;
    static int visibleAttackers = 0;
    static boolean enemyArchon = false;
    static int lead = 0;

    static boolean pretendingDead = false;
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
        if (rc.getLocation().isAdjacentTo(destination)) {
            int rubble = rc.senseRubble(rc.getLocation());
            Direction best = Direction.CENTER;
            for (Direction dir : Constants.directions) {
                if (rc.canMove(dir) && rc.adjacentLocation(dir).isAdjacentTo(destination) 
                    && rc.senseRubble(rc.adjacentLocation(dir)) < rubble) {
                    rubble = rc.senseRubble(rc.adjacentLocation(dir));
                    best = dir;
                }
            }
            if (rc.canMove(best)) rc.move(best);
        } else Pathfinder.move(rc, destination);
    }

    static void setDestination(RobotController rc) throws GameActionException {
        if (destination == null || (rc.canSenseLocation(destination)) && rc.senseLead(destination) <= 5) {
            destination = new MapLocation(Utils.randomInt(0, rc.getMapWidth()-1), Utils.randomInt(0, rc.getMapHeight()-1));
            while (destination.x != 0 && destination.x != rc.getMapWidth() - 1 && destination.y != 0 && destination.y != rc.getMapHeight() - 1) {
                destination = destination.add(rc.getLocation().directionTo(destination));
            }
        }

        rc.setIndicatorString(destination.toString());
        rc.setIndicatorLine(rc.getLocation(), destination, 255, 255, 255);
    }

    static void readQuadrant(RobotController rc) throws GameActionException {
        int dist = Integer.MAX_VALUE;
        for (int quadrant = 2; quadrant <= 26; ++quadrant) {
            int temp = rc.readSharedArray(quadrant);
            int lead = (rc.readSharedArray(quadrant + 16) >> 8) & 127;
            int visAllies = temp & 127;
            int visEnemies = (temp >> 8) & 127;

            int x = (quadrant - 2) / 5 * rc.getMapWidth()  / 5 + Utils.randomInt(0, rc.getMapWidth()/5);
            int y = (quadrant - 2) % 5 * rc.getMapHeight() / 5 + Utils.randomInt(0, rc.getMapHeight()/5);
            MapLocation target = new MapLocation(x, y);
            if (lead > 5 && visAllies >= visEnemies && rc.getLocation().distanceSquaredTo(target) < dist) {
                dist = rc.getLocation().distanceSquaredTo(target);
                destination = target;
            }
        }
    }
    static void writeQuadrantInformation(RobotController rc) throws GameActionException {
        // TODO test no random as well
        MapLocation cur = rc.getLocation();
        int quadrant = 5 * (5 * cur.x / rc.getMapWidth()) + (5 * cur.y / rc.getMapHeight());
        // indices 2 - 17 are quadrant information for enemies and allies
        int writeValue = (visibleEnemies << 8) + visibleAllies + (1<<15);
        rc.writeSharedArray(2 + quadrant, writeValue);

        writeValue = (lead << 8) + visibleAttackers + (1<<15);
        rc.writeSharedArray(27 + quadrant, writeValue);

        assert(quadrant < 27);
    }

    static void readSharedArray(RobotController rc) throws GameActionException {

    }

    static void senseEnemies(RobotController rc) throws GameActionException {
        visibleEnemies = 0;
        visibleAttackers = 0;
        visibleAllies = 0;
        enemyArchon = false;
        for (RobotInfo info : rc.senseNearbyRobots(-1)) {
            if (info.team.equals(rc.getTeam())) {
                if (info.type.equals(RobotType.SOLDIER)) ++visibleAllies;
            } else {
                ++visibleEnemies;
                switch (info.type) {
                    case SOLDIER:
                    case WATCHTOWER:
                    case SAGE:
                        ++visibleAttackers;
                        break;
                    case ARCHON:
                        enemyArchon = true;
                }
            }
        }
        rc.writeSharedArray(0, rc.readSharedArray(0) + visibleEnemies);
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
        lead = 0;
        int hiLead = 5;
        for (MapLocation loc : leadLocations) {
            while (rc.canMineLead(loc) && rc.senseLead(loc) > 1) rc.mineLead(loc);
            if (enemyArchon && rc.canMineLead(loc)) rc.mineLead(loc);
            if (rc.canSenseLocation(loc) && rc.senseLead(loc) > hiLead && isMinID && Utils.randomInt(1, near) <= 1) {
                destination = loc;
                hiLead = rc.senseLead(loc);
            }
            lead += rc.senseLead(loc);
//            if (rc.canSenseLocation(loc) && rc.senseLead(loc) > 5 && isMinID) destination = loc;
            // maybe don't need to sense
        }
        lead = Math.min(lead / 10, 127);
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
