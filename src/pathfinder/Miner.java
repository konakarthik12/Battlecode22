package pathfinder;

import battlecode.common.*;

import java.util.Arrays;


class Miner {

    static int numReached = 0;
    static int numSteps = 0;
    static Direction previousStep = Direction.CENTER;

    static final int minerLowDist = 10;
    static final int minerHighDist = 18;
    static MapLocation destination = null;
    static MapLocation spawn = null;
    static int highX = 0;
    static int lowX = 0;
    static int highY = 0;
    static int lowY = 0;
    static int near = 0;



    static Direction directMove(RobotController rc) throws GameActionException {
        return rc.getLocation().directionTo(destination);
    }

    static void setup(RobotController rc) throws GameActionException {
        spawn = rc.getLocation();
        lowX = Math.max(spawn.x - minerLowDist, 0);
        highX = Math.min(spawn.x + minerLowDist, rc.getMapWidth());
        lowY = Math.max(spawn.y - minerLowDist, 0);
        highY = Math.min(spawn.y + minerLowDist, rc.getMapHeight());
    }

    static int called = 0;

    static void run(RobotController rc) throws GameActionException {
        if (destination == null || (rc.getLocation().equals(destination))) {
            ++numReached;
            destination = new MapLocation((Utils.rng.nextInt(rc.getMapWidth()) - 3) + 3, (Utils.rng.nextInt(rc.getMapHeight() - 3) + 3));
        }
        rc.setIndicatorString(destination.toString());
        rc.setIndicatorLine(rc.getLocation(), destination, 255, 255, 255);

        if (rc.getLocation().distanceSquaredTo(destination) <= 2) {
            Direction go = directMove(rc);
            if (rc.canMove(go)) rc.move(go);
        } else {
            Direction attempt = UnrolledPathfinder.pathfind(rc,destination);
            if(rc.canMove(attempt)) rc.move(attempt);
        }

        near = 0;

        if (rc.getRoundNum() >= 1998) {
            System.out.println(rc.getID() + " Reached " + numReached + " Locations " + called);
            rc.disintegrate();
        }


    }
}
