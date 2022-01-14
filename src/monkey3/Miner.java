package monkey3;

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
    }

    static void move(RobotController rc) throws GameActionException {
        // experiment with returning to spawn and with running directly opposite to soldiers
        for (RobotInfo info : rc.senseNearbyRobots(-1, rc.getTeam().opponent())) {
            if (info.type.equals(RobotType.SOLDIER)) {
                Pathfinder.move(rc, spawn);
                return;
            }
        }
        Pathfinder.move(rc, destination);
    }

    static void setDestination(RobotController rc) throws GameActionException {
        if (destination == null || (rc.getLocation().equals(destination)) && rc.senseLead(destination) <= 5) {
            ++numReached;
//            destination = new MapLocation((Utils.rng.nextInt(rc.getMapWidth()) - 6) + 3, (Utils.rng.nextInt(rc.getMapHeight() - 6) + 3));
            destination = new MapLocation( Utils.randomInt(2, rc.getMapWidth()-3), Utils.randomInt(2, rc.getMapHeight()-3));
        }

        int x = rc.readSharedArray(34);
        if (rc.getHealth() < 4) rc.writeSharedArray(34, Math.max(x-1, 0));
        rc.setIndicatorString(destination.toString());
        rc.setIndicatorLine(rc.getLocation(), destination, 255, 255, 255);
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
        for (MapLocation loc : leadLocations) {
            while (rc.canMineLead(loc) && rc.senseLead(loc) > 1) rc.mineLead(loc);
            if (rc.canSenseLocation(loc) && rc.senseLead(loc) > 5 && isMinID && Utils.randomInt(1, near) <= 1) destination = loc;
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
