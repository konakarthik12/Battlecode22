package monkey3;

import battlecode.common.*;

class Miner {

    static int numReached = 0;
    static Direction previousStep = Direction.CENTER;
    static MapLocation destination = null;
    static MapLocation spawn = null;
    static int near = 0;
    static int enemies = 0;
    static int allies = 0;
    static int attackers = 0;

    static int turnsSearching = 0;
    static boolean isSearching = true;

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
            destination = new MapLocation((Utils.rng.nextInt(rc.getMapWidth()) - 6) + 3, (Utils.rng.nextInt(rc.getMapHeight() - 6) + 3));
        }
        rc.setIndicatorString(destination.toString());
        rc.setIndicatorLine(rc.getLocation(), destination, 255, 255, 255);
    }

    static void writeQuadrantInformation(RobotController rc) throws GameActionException {
        MapLocation cur = rc.getLocation();
        int quadrant = 4 * (4 * cur.x / rc.getMapWidth()) + (4 * cur.y / rc.getMapHeight());
        // indices 2 - 17 are quadrant information
        int writeValue = (enemies << 7) + allies;
        rc.writeSharedArray(2 + quadrant, writeValue);
        assert(quadrant < 16);
    }

    static void senseEnemies(RobotController rc) throws GameActionException {
//        int enemies = 0;
//        int attackers = 0;
        enemies = 0;
        attackers = 0;
        allies = 1 + rc.senseNearbyRobots(RobotType.MINER.visionRadiusSquared, rc.getTeam()).length;
        for (RobotInfo info : rc.senseNearbyRobots(RobotType.MINER.visionRadiusSquared, rc.getTeam().opponent())) {
            ++enemies;
            switch (info.type) {
                case SOLDIER: case WATCHTOWER: case SAGE:
                    ++attackers;
            }
        }
//        System.out.println(attackers);
//        System.out.println(rc.readSharedArray(0));

//        if (Utils.randomInt(1, allies) <= 3) {
            rc.writeSharedArray(0, rc.readSharedArray(0) + enemies);
//        }
    }

    static void senseAllies(RobotController rc) throws  GameActionException {
        near = 0;
        for (RobotInfo info : rc.senseNearbyRobots(-1, rc.getTeam())) {
            if (info.getType().equals(RobotType.MINER)) ++near;
        }
    }

    static void mine(RobotController rc) throws GameActionException {
        MapLocation[] leadLocations = rc.senseNearbyLocationsWithLead();
        for (MapLocation loc : leadLocations) {
            while (rc.canMineLead(loc) && rc.senseLead(loc) > 1) rc.mineLead(loc);
            if (rc.senseLead(loc) > 5 && Utils.randomInt(1, near) <= 1) destination = loc;
        }
    }

    static void writeShared(RobotController rc) throws GameActionException {

    }

    static void run(RobotController rc) throws GameActionException {
        senseAllies(rc);
        senseEnemies(rc);
        writeQuadrantInformation(rc);
        setDestination(rc);
        move(rc);
        mine(rc);

//        for (RobotInfo robotInfo : rc.senseNearbyRobots(-1, rc.getTeam().opponent())) {
//            rc.writeSharedArray(Utils.randomInt(0, 4), (1 << 12) + (robotInfo.location.x << 6) + robotInfo.location.y);
//            break;
//        }
    }
}
