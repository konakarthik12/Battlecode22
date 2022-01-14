package pathfinder;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

import java.util.HashMap;

class Pathfinder {
    static int temp;
    static Direction nextDirection;

    // higher depth logic (don't save best direction)
    static int regularLevel(RobotController rc, MapLocation cur, int depth) throws GameActionException {
//        if (cur.equals(destination)) return 0;

        int rubble = 10000;
        if (depth == 0) rubble = 0;
        else if (cur.equals(destination)) rubble = -1000;
        else if (rc.canSenseLocation(cur) && !rc.isLocationOccupied(cur)) rubble = rc.senseRubble(cur) + 10;

        if (depth == 3) return rubble + cur.distanceSquaredTo(destination);

        Direction toDest = rc.getLocation().directionTo(destination);
        Direction beforeDest = toDest.rotateLeft();
        Direction afterDest = toDest.rotateRight();

        int score = regularLevel(rc, cur.add(beforeDest), depth + 1);
        Direction bestDir = beforeDest;

        temp = regularLevel(rc, cur.add(toDest), depth + 1);
        if (temp < score) {
            score = temp;
            bestDir = toDest;
        }

        temp = regularLevel(rc, cur.add(afterDest), depth + 1);
        if (temp < score) {
            score = temp;
            bestDir = afterDest;

        }
        if (depth == 0) nextDirection = bestDir;

        return rubble + score;
    }


    static MapLocation destination;

    public static Direction pathfind(RobotController rc, MapLocation dest) throws GameActionException {
        destination = dest;

        regularLevel(rc, rc.getLocation(), 0);

        return nextDirection;
    }

}