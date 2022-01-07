package pathfinder;

import battlecode.common.*;
import org.omg.PortableInterceptor.INACTIVE;

import static pathfinder.Utils.*;

class Miner {

    static MapLocation destination = new MapLocation(28,5);
    static MapLocation nearestLead = null;

    static Direction nextMove(RobotController rc) throws GameActionException {
        MapLocation cur = rc.getLocation();
        int visionSq = RobotType.MINER.visionRadiusSquared;

        MapLocation[] nearbyLead = rc.senseNearbyLocationsWithLead(10000);

        if (nearbyLead.length > 0 && nearestLead == null) {
//            nearestLead = nearbyLead[0];
        }

        Direction go = (nearestLead != null) ? rc.getLocation().directionTo(nearestLead) :  rc.getLocation().directionTo(destination);
        go = cur.directionTo(destination);

//        if (rc.senseRubble(rc.adjacentLocation(go)) > 100) {
        Direction newGo = null;

        int score = Integer.MAX_VALUE;
        final int sensitivity = 5;
        final int EPS = 3;

//        int curDistSq = rc.adjacentLocation(go).distanceSquaredTo(destination);
        int curDistSq = cur.distanceSquaredTo(destination);
        for (Direction dir : directions) {
            int newDistSq = rc.adjacentLocation(dir).distanceSquaredTo(destination);
            int distSq = -(curDistSq-newDistSq);
            int newScore = rc.senseRubble(rc.adjacentLocation(dir)) + sensitivity * distSq;

            if (newScore < score) {
                newGo = dir;
                score = newScore;
            }
        }

        if (newGo != null) go = newGo;

//        }
//        System.out.println(go.toString());

        return go;
    }

    static void run(RobotController rc) throws GameActionException {
        Direction go = nextMove(rc);
        while (rc.canMineLead(rc.getLocation())) {
            rc.mineLead(rc.getLocation());
        }

        rc.setIndicatorString(destination.toString());
        if (rc.canMove(go)) rc.move(go);
    }
}
