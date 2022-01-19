package monkey4;

import battlecode.common.*;

class Miner {

    static int numReached = 0;
    static MapLocation destination = null;
    static MapLocation tempDestination = null;
    static MapLocation spawn = null;
    static MapLocation previous = null;

    static int lastSide = -1;
    static int near = 0;
    static int visibleEnemies = 0;
    static int visibleAllies = 0;
    static int visibleAttackers = 0;
    static int lead = 0;
    static boolean enemyArchon = false;

    static int turnsSearching = 0;
    static boolean isMinID = true;

    // TODO experiment with returning to spawn and with running directly opposite to soldiers or nearest Archon
    // TODO make the miners add their count to shared array instead of pretending dead
    // TODO really overhaul miner running away
    // TODO make miners not clump up
    // TODO fix where the miner changes destination to lead
    // TODO change highlead

    static void setup(RobotController rc) {
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
            int roll = Utils.randomInt(1,4);
            while (roll == lastSide) roll = Utils.randomInt(1,4);
            lastSide = roll;
            destination = Utils.getWall(roll);
        }

        int numMiners = rc.readSharedArray(58);
        if (!pretendingDead && rc.getHealth() < 9) {
            rc.writeSharedArray(58, Math.max(numMiners-1, 0));
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
        int dist = cur.distanceSquaredTo(center);

        int newValue = (visibleAttackers << 7) + visibleAllies;
        int prevValue = rc.readSharedArray(quadrant) & 32767;
        int writeValue = (dist * prevValue + (60 - dist) * newValue) / 60;
        if (prevValue == 0) writeValue = newValue;
        rc.writeSharedArray(quadrant, writeValue + (1 << 15));
    }

    static void readQuadrantInformation(RobotController rc) throws GameActionException {
        for (int quadrant = 0; quadrant < 36; ++quadrant) {
            int temp = rc.readSharedArray(quadrant);
        }
    }

    static void senseNearby(RobotController rc) {
        enemyArchon = false;
        isMinID = true;
        near = 0;
        visibleEnemies = 0;
        visibleAttackers = 0;
        visibleAllies = 0;

        for (RobotInfo info : rc.senseNearbyRobots(-1)) {
            if (info.team.equals(rc.getTeam())) {
                if (info.type.equals(RobotType.SOLDIER)) ++visibleAllies;
                if (info.type.equals(RobotType.MINER)) {
                    ++near;
                    if (info.getID() < rc.getID()) isMinID = false;
                }
            } else {
                ++visibleEnemies;
                switch (info.type) {
                    case ARCHON: enemyArchon = true; break;
                    case SOLDIER:
                    case WATCHTOWER:
                    case SAGE:
                        ++visibleAttackers;
                }
            }
        }
    }

    static void mine(RobotController rc) throws GameActionException {
        ++turnsSearching;
        MapLocation[] leadLocations = rc.senseNearbyLocationsWithLead();
        lead = leadLocations.length;
        if (lead > 0) turnsSearching = 0;
        int highLead = 5;
//        isMinID = isMinID || Utils.randomInt(1, 10) == 1;
        for (MapLocation loc : leadLocations) {
            while (rc.canMineLead(loc) && (rc.senseLead(loc) > 1 || enemyArchon)) rc.mineLead(loc);
            if (rc.canSenseLocation(loc) && (rc.senseLead(loc) > highLead || enemyArchon) && isMinID && Utils.randomInt(1, near) <= 1) {
                destination = loc;
                highLead = rc.senseLead(loc);
            }
        }
    }

    static void run(RobotController rc) throws GameActionException {
        senseNearby(rc);
        setDestination(rc);

        mine(rc);
        move(rc);
        mine(rc);

        writeQuadrantInformation(rc);
    }
}
