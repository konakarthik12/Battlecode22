package pathfinder;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class Pathfinder {
    static Direction nextDirection;
    static MapLocation destination;

    public static Direction pathfind(RobotController rc, MapLocation dest) throws GameActionException {
        destination = dest;
        nextMove(rc, rc.getLocation(), 0);
        return nextDirection;
    }

    static int nextMove(RobotController rc, MapLocation cur, int depth) throws GameActionException {
        int rubble = 10000;
        if (rc.canSenseLocation(cur) && !rc.isLocationOccupied(cur)) rubble = rc.senseRubble(cur) + 10;

        if (depth == 3) {
            int curDist = cur.distanceSquaredTo(destination);

            return (rubble) + curDist;
        }

        int toDest = cur.directionTo(destination).ordinal();
        int score = Integer.MAX_VALUE;

        for (int i = (7 + toDest); i <= (9 + toDest); ++i) {
            Direction dir = Constants.directions[i % 8];
            MapLocation next = cur.add(dir);
            int nextScore = rubble + nextMove(rc, next, depth + 1);

            if (score > nextScore) {
                score = nextScore;
                nextDirection = dir;
            }
        }


   /*     if (depth == 0) {
            nextDirection = dir;
        }
*/
        return score;
    }

}
