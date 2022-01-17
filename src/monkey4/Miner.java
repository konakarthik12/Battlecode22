package monkey4;

import battlecode.common.*;

class Miner {

    static int numReached = 0;
    static MapLocation destination = null;
    static MapLocation spawn = null;
    static MapLocation previous = null;

    static int near = 0;
    static int visibleEnemies = 0;
    static int visibleAllies = 0;
    static int visibleAttackers = 0;
    static int lead = 0;

    static int turnsSearching = 0;
    static boolean isMinID = true;

    // TODO experiment with returning to spawn and with running directly opposite to soldiers or nearest archon
    // TODO make the miners add their count to shared array instead of pretending dead
    // TODO really overhaul miner running away
    // TODO make miners not clump up

    static void setup(RobotController rc) throws GameActionException {
        spawn = rc.getLocation();
    }

    static void move(RobotController rc) throws GameActionException {
        if (visibleAttackers > visibleAllies) {
            RobotInfo[] enemies = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
            for (RobotInfo info : enemies) {
                if (info.type.equals(RobotType.SOLDIER)) {
                    Pathfinder.move(rc, spawn);
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
        if (destination == null || (rc.getLocation().equals(destination)) && rc.senseLead(destination) <= 5) {
            ++numReached;
//            destination = new MapLocation((Utils.rng.nextInt(rc.getMapWidth()) - 6) + 3, (Utils.rng.nextInt(rc.getMapHeight() - 6) + 3));
            if (destination != null) {
                previous = destination;
                int vx = previous.x;
            }
            destination = new MapLocation( Utils.randomInt(2, rc.getMapWidth()-3), Utils.randomInt(2, rc.getMapHeight()-3));
        }

        int x = rc.readSharedArray(58);
        if (!pretendingDead && rc.getHealth() < 9) {
            rc.writeSharedArray(58, Math.max(x-1, 0));
            pretendingDead = true;
        }
        rc.setIndicatorString(destination.toString());
        rc.setIndicatorLine(rc.getLocation(), destination, 255, 255, 255);
    }

    static void writeQuadrantInformation(RobotController rc) throws GameActionException {
        MapLocation cur = rc.getLocation();
        MapLocation center = new MapLocation(cur.x - cur.x % (rc.getMapWidth()/6) + rc.getMapWidth()/12,
                cur.y - cur.y % (rc.getMapHeight()/6) + rc.getMapHeight()/12);

        int colIndex = 6 * cur.x / rc.getMapWidth();
        int rowIndex = 6 * cur.y / rc.getMapHeight();
        int quadrant = 6 * rowIndex + colIndex;

        // weights for gamma averaging, max is a 10 x 10, max square-distance from center is 50
        int dist = cur.distanceSquaredTo(center);


        // indices 0 - 35 are quadrant information for enemies and allies
        int newValue = (visibleAttackers << 7) + visibleAllies;
        int prevValue = rc.readSharedArray(quadrant) & 32767;
        int writeValue = (dist * prevValue + (60 - dist) * newValue) / 60;
        if (prevValue == 0) writeValue = newValue;
        rc.writeSharedArray(quadrant, writeValue + (1 << 15));

//        int lead = (rc.senseNearbyLocationsWithLead().length << 8) + visibleAttackers;
//        int oldVal = rc.readSharedArray(18 + quadrant);
//        writeValue = (oldVal * dist + (50-dist) * lead) / 50;
//        if (oldVal == 0) writeValue = lead;
//        rc.writeSharedArray(18 + quadrant, writeValue);

        assert quadrant < 36;
        assert dist <= 60;
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
        MapLocation[] leadLocations = rc.senseNearbyLocationsWithLead();
        lead = leadLocations.length;
        if (lead > 0) turnsSearching = 0;
        int highLead = 4;
        for (MapLocation loc : leadLocations) {
            while (rc.canMineLead(loc) && rc.senseLead(loc) > 1) rc.mineLead(loc);
            if (rc.canSenseLocation(loc) && rc.senseLead(loc) > highLead && isMinID && Utils.randomInt(1, near) <= 1) {
                destination = loc;
                highLead = rc.senseLead(loc);
            }
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
