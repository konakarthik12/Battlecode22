package pathfinder;

import battlecode.common.*;

import static pathfinder.Utils.directions;
import static pathfinder.Utils.rng;

class Miner {

    static MapLocation destination = null;
    static MapLocation nearestLead = null;


    static Direction nextMove(RobotController rc) throws GameActionException {
        MapLocation cur = rc.getLocation();

        int score = Integer.MAX_VALUE;


        MapLocation[] locations = rc.getAllLocationsWithinRadiusSquared(cur, 18);

        int[] moveScores = new int[8]; // score for east, north, ... northeast northwest...

        int c1 = 25;
        int c2 = 3;
        int c3 = 100;
        for (int i = 0; i < directions.length; i++) {
            Direction dir = directions[i];
            for (MapLocation location : locations) {
                int distToDest = location.distanceSquaredTo(destination) - rc.adjacentLocation(dir).distanceSquaredTo(destination);
                int distToMove = rc.adjacentLocation(dir).distanceSquaredTo(location);
                int rubble = rc.senseRubble(location);
                int scoreDelta = (c3 * rubble - c1 * distToDest) / (1 + distToMove / c2);
                moveScores[i] += scoreDelta;
            }
        }


        int minIndex = 0;
        for (int j = 0; j < 8; ++j) {
            if (moveScores[j] < score) {
                score = moveScores[j];
                minIndex = j;
            }
        }

        return directions[minIndex];
    }

    static void run(RobotController rc) throws GameActionException {
        int start = rc.getRoundNum();
        if (destination == null || destination.equals(rc.getLocation())) {
            destination = new MapLocation(rng.nextInt(rc.getMapWidth()), rng.nextInt(rc.getMapHeight()));
        }

        rc.setIndicatorDot(destination, 255, 0, 0);

        MapLocation cur = rc.getLocation();
        Direction go = nextMove(rc);

        for (MapLocation location : rc.getAllLocationsWithinRadiusSquared(cur, 2)) {
            if (rc.canMineLead(location)) {
                rc.mineLead(location);
            }
        }
        if (rc.canMove(go)) rc.move(go);
        int rounds = rc.getRoundNum() - start;
        rc.setIndicatorString(String.valueOf(rounds * rc.getType().bytecodeLimit + Clock.getBytecodeNum()));

    }
}
