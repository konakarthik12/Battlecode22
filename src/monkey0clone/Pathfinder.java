package monkey0clone;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class Pathfinder {

    static MapLocation destination;

    static int nextMove(RobotController rc, MapLocation cur, int depth) throws GameActionException {
        if (!rc.canSenseLocation(cur)) return 100000;
        if (cur.equals(destination)) return 0;

        int curDist = cur.distanceSquaredTo(destination);
        int score = Integer.MAX_VALUE;
//        int rubble = (rc.isLocationOccupied(cur) && !cur.equals(rc.getLocation())) ? 10000 : rc.senseRubble(cur);
        int rubble = (rc.getType() == RobotType.SAGE ? 2 : 1) * rc.senseRubble(cur);

        if (depth == 3) return (10 + rubble) + curDist;

        Direction toDest = cur.directionTo(destination);
        Direction leftToDest = toDest.rotateLeft();
        Direction rightToDest = toDest.rotateRight();
        Direction nextDirection = Direction.CENTER;

        int curScore = 10 + rubble + nextMove(rc, cur.add(toDest), depth + 1);
        if (curScore < score) {
            score = curScore;
            nextDirection = toDest;
        }

        curScore = 10 + rubble + nextMove(rc, cur.add(leftToDest), depth + 1);
        if (curScore < score) {
            score = curScore;
            nextDirection = leftToDest;
        }

        curScore = 10 + rubble + nextMove(rc, cur.add(rightToDest), depth + 1);
        if (curScore < score) {
            score = curScore;
            nextDirection = rightToDest;
        }

        if (depth == 0) {
            if (rc.canMove(nextDirection)) {
                rc.move(nextDirection);
            } else {
                int roll = Utils.randomInt(1, 3);
                if (roll == 1 && rc.canMove(toDest)) rc.move(toDest);
                if (roll == 2 && rc.canMove(leftToDest)) rc.move(leftToDest);
                if (roll == 3 && rc.canMove(rightToDest)) rc.move(rightToDest);
            }
        }

        return score;
    }

    static void move(RobotController rc, MapLocation _destination) throws GameActionException {
        MapLocation cur = rc.getLocation();
        destination = _destination;

        if (destination.isAdjacentTo(cur)) {
            Direction go = cur.directionTo(destination);
            if (rc.canMove(go)) rc.move(go);
        } else {
            nextMove(rc, cur, 0);
        }
    }
}
