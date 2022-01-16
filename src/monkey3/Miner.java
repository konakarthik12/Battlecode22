package monkey3;

import battlecode.common.*;

class Miner {

    static int numReached = 0;
    static MapLocation destination = null;
    static MapLocation spawn = null;
    static int near = 0;
    static int visibleEnemies = 0;
    static int visibleAllies = 0;
    static int visibleAttackers = 0;
    static int lead = 0;

    static int turnsSearching = 0;
    static boolean isMinID = true;

    static void setup(RobotController rc) throws GameActionException {
        spawn = rc.getLocation();
    }

    static void move(RobotController rc) throws GameActionException {
        // experiment with returning to spawn and with running directly opposite to soldiers
        if (visibleAttackers > visibleAllies) {
            RobotInfo[] enemies = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
            for (RobotInfo info : enemies) {
                if (info.type.equals(RobotType.SOLDIER)) {
//                    Pathfinder.move(rc, spawn);
                    Pathfinder.move(rc, Utils.nearestArchon(rc));
                    return;
                }
            }
            if (enemies.length == 0) Pathfinder.move(rc, destination);
        } else {
            Pathfinder.move(rc, destination);
        }
    }

    static boolean pretendingDead = false;

    static void setDestination(RobotController rc) throws GameActionException {
        // TODO really overhaul miner running away
        if (destination == null || (rc.getLocation().equals(destination)) && rc.senseLead(destination) <= 5) {
            ++numReached;
//            destination = new MapLocation((Utils.rng.nextInt(rc.getMapWidth()) - 6) + 3, (Utils.rng.nextInt(rc.getMapHeight() - 6) + 3));
            destination = new MapLocation( Utils.randomInt(2, rc.getMapWidth()-3), Utils.randomInt(2, rc.getMapHeight()-3));
        }

        int x = rc.readSharedArray(34);
        if (!pretendingDead && rc.getHealth() < 10) {
            rc.writeSharedArray(34, Math.max(x-1, 0));
            pretendingDead = true;
        }
        // TODO make the miners add their count to 34 instead of pretending dead
        rc.setIndicatorString(destination.toString());
        rc.setIndicatorLine(rc.getLocation(), destination, 255, 255, 255);
    }

    static void writeQuadrantInformation(RobotController rc) throws GameActionException {
        // TODO test no random as well
        MapLocation cur = rc.getLocation();
        MapLocation center = new MapLocation(cur.x - cur.x % (rc.getMapWidth()/4) + rc.getMapWidth()/8,
                cur.y - cur.y % (rc.getMapHeight()/4) + rc.getMapHeight()/8);
        int x = 4 * cur.x / rc.getMapWidth();
        int y = 4 * cur.y / rc.getMapHeight();
        int quadrant = 4*x + y;

        // weights for gamma averaging, max is a 15 x 15 so distance^2 to center is at most 10
        int dist = cur.distanceSquaredTo(center);

        // indices 2 - 17 are quadrant information for enemies and allies
        int newValue = (visibleEnemies << 8) + visibleAllies;
        int prevValue = rc.readSharedArray(2 + quadrant);
        int writeValue = (dist * prevValue + (100 - dist) * newValue) / 100;
        if (prevValue == 0) writeValue = newValue;
        rc.writeSharedArray(2 + quadrant, writeValue);

        int lead = (rc.senseNearbyLocationsWithLead().length << 8) + visibleAttackers;
        int oldVal = rc.readSharedArray(18 + quadrant);
        writeValue = (oldVal * dist + (100-dist) * lead) / 100;
        if (oldVal == 0) writeValue = lead;
        rc.writeSharedArray(18 + quadrant, writeValue);

        assert dist <= 100;
        assert(quadrant < 16);
    }

    static void senseEnemies(RobotController rc) throws GameActionException {
        visibleEnemies = 0;
        visibleAttackers = 0;
        visibleAllies = 0;

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

        if (Utils.randomInt(1, visibleAllies+1) <= 2) {
            rc.writeSharedArray(0, rc.readSharedArray(0) + visibleEnemies);
        }
    }

    static void senseAllies(RobotController rc) throws GameActionException {
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
        ++turnsSearching;
        //TODO make miners not clump up
        MapLocation[] leadLocations = rc.senseNearbyLocationsWithLead();
        lead = leadLocations.length;
        if (lead > 0) turnsSearching = 0;
        for (MapLocation loc : leadLocations) {
            while (rc.canMineLead(loc) && rc.senseLead(loc) > 1) rc.mineLead(loc);
            if (rc.canSenseLocation(loc) && rc.senseLead(loc) > 5 && isMinID && Utils.randomInt(1, near) <= 1) destination = loc;
//            if (rc.canSenseLocation(loc) && rc.senseLead(loc) > 5 && isMinID) destination = loc;
            // maybe don't need to sense
        }
    }


    static void run(RobotController rc) throws GameActionException {
        senseAllies(rc);
        senseEnemies(rc);
        setDestination(rc);

        move(rc);
        mine(rc);

        writeQuadrantInformation(rc);
    }
}
