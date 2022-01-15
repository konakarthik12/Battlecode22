package pathfinder;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

class Pathfinder {
    static int temp;
    static Direction nextDirection;
    private static RobotController rc;
    static MapLocation dest;


    static int regularLevel(MapLocation cur) throws GameActionException {
        // destination -1000, obstacle 10000
        int rubble = smartRubble(cur);
        // use heuristic to estimate once u reach the perimeter of 5 by 5 square around robot
        if (Math.abs(cur.x - rc.getLocation().x) == 2 || Math.abs(cur.y - rc.getLocation().y) == 2) {
//            System.out.println(pathSoFar);
            return rubble + cur.distanceSquaredTo(dest);
        }

        int score = Integer.MAX_VALUE;
        Direction toDest = rc.getLocation().directionTo(dest);
        Direction beforeDest = toDest.rotateLeft();
        Direction afterDest = toDest.rotateRight();
        for (Direction dir : new Direction[]{toDest, beforeDest, afterDest}) {
            MapLocation newLoc = cur.add(dir);
            int temp = regularLevel(newLoc);
            if (temp < score) {
                score = temp;
                if (rc.getLocation().equals(cur)) nextDirection = dir;
            }
        }

        return rubble + score;
    }


    public static Direction pathfind(RobotController robotController, MapLocation destination) throws GameActionException {
        dest = destination;
        rc = robotController;
        regularLevel(rc.getLocation());
        return nextDirection;
    }

    static boolean inBounds(MapLocation loc) {
        return loc.x >= 0 && loc.x < rc.getMapWidth() && loc.y >= 0 && loc.y < rc.getMapHeight();
    }

    static int smartRubble(MapLocation cur) throws GameActionException {
        if (rc.getLocation().equals(cur)) return 0;
        else if (cur.equals(dest)) return -1000;
        else if (rc.canSenseLocation(cur) && !rc.isLocationOccupied(cur)) return rc.senseRubble(cur) + 10;
        //out of bounds or is occupied
        return 10000;

    }
}