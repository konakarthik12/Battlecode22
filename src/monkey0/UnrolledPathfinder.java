package monkey0;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class UnrolledPathfinder {
    static RobotController rc;
    static MapLocation rcCur;
    static MapLocation dest;
    private static final int RUBBLE_ESTIMATE = 35;

    static long heuristicCooldown(MapLocation a) {
        int dx = Math.abs(a.x - dest.x);
        int dy = Math.abs(a.y - dest.y);
        int heuristicDistance = Math.max(dx, dy) + Math.min(dx, dy) / 2;
        return heuristicDistance * (2 * RUBBLE_ESTIMATE + 20);
    }


    static long smartCooldown(MapLocation cur) throws GameActionException {
        if (rc.getLocation().equals(cur)) return 0;
        else if (cur.equals(dest)) return -10000;
        else if (rc.canSenseLocation(cur) && !rc.isLocationOccupied(cur)) return 2L * rc.senseRubble(cur) + 20;
        //out of bounds or is occupied
        return 100000;
    }


    static void move(RobotController robotController, MapLocation destination) throws GameActionException {
        rc = robotController;
        rcCur = rc.getLocation();
        dest = destination;
        if(rcCur.equals(destination)) return;
        Direction nextDirection = pathfind();
        if (rc.canMove(nextDirection)) {
            rc.move(nextDirection);
        } else if (!rc.getLocation().isAdjacentTo(dest)) {
            for (Direction dir : Constants.directions) {
                MapLocation next = rc.getLocation().add(dir);
                int curDist = rcCur.distanceSquaredTo(dest);

                if (rc.canSenseLocation(next) && next.distanceSquaredTo(dest) <= curDist) {
                    if (rc.canMove(dir)) rc.move(dir);
                }
            }
        }
    }
    
static Direction pathfind() throws GameActionException {
switch(rcCur.directionTo(dest)) {
case NORTH:
return pathfind_NORTH();
case NORTHEAST:
return pathfind_NORTHEAST();
case EAST:
return pathfind_EAST();
case SOUTHEAST:
return pathfind_SOUTHEAST();
case SOUTH:
return pathfind_SOUTH();
case SOUTHWEST:
return pathfind_SOUTHWEST();
case WEST:
return pathfind_WEST();
case NORTHWEST:
return pathfind_NORTHWEST();
}
return null;
}
static Direction pathfind_NORTH() throws GameActionException {
MapLocation loc_0_0 = rcCur.translate(0, 0);
long cost_0_0 = Long.MAX_VALUE;
Direction best_dir = null;
MapLocation loc_0_1 = rcCur.translate(0, 1);
long cost_0_1 = Long.MAX_VALUE;
MapLocation loc_0_2 = rcCur.translate(0, 2);
long cost_0_2 = Long.MAX_VALUE;
MapLocation loc_0_3 = rcCur.translate(0, 3);
long cost_0_3 = Long.MAX_VALUE;
MapLocation loc_0_4 = rcCur.translate(0, 4);
long cost_0_4 = Long.MAX_VALUE;
MapLocation loc_0_5 = rcCur.translate(0, 5);
long cost_0_5 = heuristicCooldown(loc_0_5);
if(cost_0_5 < cost_0_4) cost_0_4 = cost_0_5;
MapLocation loc_n1_5 = rcCur.translate(-1, 5);
long cost_n1_5 = heuristicCooldown(loc_n1_5);
if(cost_n1_5 < cost_0_4) cost_0_4 = cost_n1_5;
MapLocation loc_1_5 = rcCur.translate(1, 5);
long cost_1_5 = heuristicCooldown(loc_1_5);
if(cost_1_5 < cost_0_4) cost_0_4 = cost_1_5;
MapLocation loc_n1_4 = rcCur.translate(-1, 4);
long cost_n1_4 = Long.MAX_VALUE;
if(cost_n1_5 < cost_n1_4) cost_n1_4 = cost_n1_5;
MapLocation loc_n2_5 = rcCur.translate(-2, 5);
long cost_n2_5 = heuristicCooldown(loc_n2_5);
if(cost_n2_5 < cost_n1_4) cost_n1_4 = cost_n2_5;
if(cost_0_5 < cost_n1_4) cost_n1_4 = cost_0_5;
MapLocation loc_n2_4 = rcCur.translate(-2, 4);
long cost_n2_4 = Long.MAX_VALUE;
if(cost_n2_5 < cost_n2_4) cost_n2_4 = cost_n2_5;
MapLocation loc_n3_5 = rcCur.translate(-3, 5);
long cost_n3_5 = heuristicCooldown(loc_n3_5);
if(cost_n3_5 < cost_n2_4) cost_n2_4 = cost_n3_5;
if(cost_n1_5 < cost_n2_4) cost_n2_4 = cost_n1_5;
MapLocation loc_n3_4 = rcCur.translate(-3, 4);
long cost_n3_4 = heuristicCooldown(loc_n3_4);
if(cost_n3_4 < cost_n2_4) cost_n2_4 = cost_n3_4;
cost_n2_4 += smartCooldown(loc_n2_4);
//best for [-2, 4]
if(cost_n2_4 < cost_n1_4) cost_n1_4 = cost_n2_4;
cost_n1_4 += smartCooldown(loc_n1_4);
//best for [-1, 4]
if(cost_n1_4 < cost_0_4) cost_0_4 = cost_n1_4;
MapLocation loc_1_4 = rcCur.translate(1, 4);
long cost_1_4 = Long.MAX_VALUE;
if(cost_1_5 < cost_1_4) cost_1_4 = cost_1_5;
if(cost_0_5 < cost_1_4) cost_1_4 = cost_0_5;
MapLocation loc_2_5 = rcCur.translate(2, 5);
long cost_2_5 = heuristicCooldown(loc_2_5);
if(cost_2_5 < cost_1_4) cost_1_4 = cost_2_5;
MapLocation loc_2_4 = rcCur.translate(2, 4);
long cost_2_4 = Long.MAX_VALUE;
if(cost_2_5 < cost_2_4) cost_2_4 = cost_2_5;
if(cost_1_5 < cost_2_4) cost_2_4 = cost_1_5;
MapLocation loc_3_5 = rcCur.translate(3, 5);
long cost_3_5 = heuristicCooldown(loc_3_5);
if(cost_3_5 < cost_2_4) cost_2_4 = cost_3_5;
MapLocation loc_3_4 = rcCur.translate(3, 4);
long cost_3_4 = heuristicCooldown(loc_3_4);
if(cost_3_4 < cost_2_4) cost_2_4 = cost_3_4;
cost_2_4 += smartCooldown(loc_2_4);
//best for [2, 4]
if(cost_2_4 < cost_1_4) cost_1_4 = cost_2_4;
cost_1_4 += smartCooldown(loc_1_4);
//best for [1, 4]
if(cost_1_4 < cost_0_4) cost_0_4 = cost_1_4;
cost_0_4 += smartCooldown(loc_0_4);
//best for [0, 4]
if(cost_0_4 < cost_0_3) cost_0_3 = cost_0_4;
if(cost_n1_4 < cost_0_3) cost_0_3 = cost_n1_4;
if(cost_1_4 < cost_0_3) cost_0_3 = cost_1_4;
MapLocation loc_n1_3 = rcCur.translate(-1, 3);
long cost_n1_3 = Long.MAX_VALUE;
if(cost_n1_4 < cost_n1_3) cost_n1_3 = cost_n1_4;
if(cost_n2_4 < cost_n1_3) cost_n1_3 = cost_n2_4;
if(cost_0_4 < cost_n1_3) cost_n1_3 = cost_0_4;
MapLocation loc_n2_3 = rcCur.translate(-2, 3);
long cost_n2_3 = Long.MAX_VALUE;
if(cost_n2_4 < cost_n2_3) cost_n2_3 = cost_n2_4;
if(cost_n3_4 < cost_n2_3) cost_n2_3 = cost_n3_4;
if(cost_n1_4 < cost_n2_3) cost_n2_3 = cost_n1_4;
MapLocation loc_n3_3 = rcCur.translate(-3, 3);
long cost_n3_3 = Long.MAX_VALUE;
if(cost_n3_4 < cost_n3_3) cost_n3_3 = cost_n3_4;
MapLocation loc_n4_4 = rcCur.translate(-4, 4);
long cost_n4_4 = heuristicCooldown(loc_n4_4);
if(cost_n4_4 < cost_n3_3) cost_n3_3 = cost_n4_4;
if(cost_n2_4 < cost_n3_3) cost_n3_3 = cost_n2_4;
MapLocation loc_n4_3 = rcCur.translate(-4, 3);
long cost_n4_3 = heuristicCooldown(loc_n4_3);
if(cost_n4_3 < cost_n3_3) cost_n3_3 = cost_n4_3;
cost_n3_3 += smartCooldown(loc_n3_3);
//best for [-3, 3]
if(cost_n3_3 < cost_n2_3) cost_n2_3 = cost_n3_3;
cost_n2_3 += smartCooldown(loc_n2_3);
//best for [-2, 3]
if(cost_n2_3 < cost_n1_3) cost_n1_3 = cost_n2_3;
cost_n1_3 += smartCooldown(loc_n1_3);
//best for [-1, 3]
if(cost_n1_3 < cost_0_3) cost_0_3 = cost_n1_3;
MapLocation loc_1_3 = rcCur.translate(1, 3);
long cost_1_3 = Long.MAX_VALUE;
if(cost_1_4 < cost_1_3) cost_1_3 = cost_1_4;
if(cost_0_4 < cost_1_3) cost_1_3 = cost_0_4;
if(cost_2_4 < cost_1_3) cost_1_3 = cost_2_4;
MapLocation loc_2_3 = rcCur.translate(2, 3);
long cost_2_3 = Long.MAX_VALUE;
if(cost_2_4 < cost_2_3) cost_2_3 = cost_2_4;
if(cost_1_4 < cost_2_3) cost_2_3 = cost_1_4;
if(cost_3_4 < cost_2_3) cost_2_3 = cost_3_4;
MapLocation loc_3_3 = rcCur.translate(3, 3);
long cost_3_3 = Long.MAX_VALUE;
if(cost_3_4 < cost_3_3) cost_3_3 = cost_3_4;
if(cost_2_4 < cost_3_3) cost_3_3 = cost_2_4;
MapLocation loc_4_4 = rcCur.translate(4, 4);
long cost_4_4 = heuristicCooldown(loc_4_4);
if(cost_4_4 < cost_3_3) cost_3_3 = cost_4_4;
MapLocation loc_4_3 = rcCur.translate(4, 3);
long cost_4_3 = heuristicCooldown(loc_4_3);
if(cost_4_3 < cost_3_3) cost_3_3 = cost_4_3;
cost_3_3 += smartCooldown(loc_3_3);
//best for [3, 3]
if(cost_3_3 < cost_2_3) cost_2_3 = cost_3_3;
cost_2_3 += smartCooldown(loc_2_3);
//best for [2, 3]
if(cost_2_3 < cost_1_3) cost_1_3 = cost_2_3;
cost_1_3 += smartCooldown(loc_1_3);
//best for [1, 3]
if(cost_1_3 < cost_0_3) cost_0_3 = cost_1_3;
cost_0_3 += smartCooldown(loc_0_3);
//best for [0, 3]
if(cost_0_3 < cost_0_2) cost_0_2 = cost_0_3;
if(cost_n1_3 < cost_0_2) cost_0_2 = cost_n1_3;
if(cost_1_3 < cost_0_2) cost_0_2 = cost_1_3;
MapLocation loc_n1_2 = rcCur.translate(-1, 2);
long cost_n1_2 = Long.MAX_VALUE;
if(cost_n1_3 < cost_n1_2) cost_n1_2 = cost_n1_3;
if(cost_n2_3 < cost_n1_2) cost_n1_2 = cost_n2_3;
if(cost_0_3 < cost_n1_2) cost_n1_2 = cost_0_3;
MapLocation loc_n2_2 = rcCur.translate(-2, 2);
long cost_n2_2 = Long.MAX_VALUE;
if(cost_n2_3 < cost_n2_2) cost_n2_2 = cost_n2_3;
if(cost_n3_3 < cost_n2_2) cost_n2_2 = cost_n3_3;
if(cost_n1_3 < cost_n2_2) cost_n2_2 = cost_n1_3;
MapLocation loc_n3_2 = rcCur.translate(-3, 2);
long cost_n3_2 = Long.MAX_VALUE;
if(cost_n3_3 < cost_n3_2) cost_n3_2 = cost_n3_3;
if(cost_n4_3 < cost_n3_2) cost_n3_2 = cost_n4_3;
if(cost_n2_3 < cost_n3_2) cost_n3_2 = cost_n2_3;
MapLocation loc_n4_2 = rcCur.translate(-4, 2);
long cost_n4_2 = Long.MAX_VALUE;
if(cost_n4_3 < cost_n4_2) cost_n4_2 = cost_n4_3;
MapLocation loc_n5_3 = rcCur.translate(-5, 3);
long cost_n5_3 = heuristicCooldown(loc_n5_3);
if(cost_n5_3 < cost_n4_2) cost_n4_2 = cost_n5_3;
if(cost_n3_3 < cost_n4_2) cost_n4_2 = cost_n3_3;
MapLocation loc_n5_2 = rcCur.translate(-5, 2);
long cost_n5_2 = heuristicCooldown(loc_n5_2);
if(cost_n5_2 < cost_n4_2) cost_n4_2 = cost_n5_2;
cost_n4_2 += smartCooldown(loc_n4_2);
//best for [-4, 2]
if(cost_n4_2 < cost_n3_2) cost_n3_2 = cost_n4_2;
cost_n3_2 += smartCooldown(loc_n3_2);
//best for [-3, 2]
if(cost_n3_2 < cost_n2_2) cost_n2_2 = cost_n3_2;
cost_n2_2 += smartCooldown(loc_n2_2);
//best for [-2, 2]
if(cost_n2_2 < cost_n1_2) cost_n1_2 = cost_n2_2;
cost_n1_2 += smartCooldown(loc_n1_2);
//best for [-1, 2]
if(cost_n1_2 < cost_0_2) cost_0_2 = cost_n1_2;
MapLocation loc_1_2 = rcCur.translate(1, 2);
long cost_1_2 = Long.MAX_VALUE;
if(cost_1_3 < cost_1_2) cost_1_2 = cost_1_3;
if(cost_0_3 < cost_1_2) cost_1_2 = cost_0_3;
if(cost_2_3 < cost_1_2) cost_1_2 = cost_2_3;
MapLocation loc_2_2 = rcCur.translate(2, 2);
long cost_2_2 = Long.MAX_VALUE;
if(cost_2_3 < cost_2_2) cost_2_2 = cost_2_3;
if(cost_1_3 < cost_2_2) cost_2_2 = cost_1_3;
if(cost_3_3 < cost_2_2) cost_2_2 = cost_3_3;
MapLocation loc_3_2 = rcCur.translate(3, 2);
long cost_3_2 = Long.MAX_VALUE;
if(cost_3_3 < cost_3_2) cost_3_2 = cost_3_3;
if(cost_2_3 < cost_3_2) cost_3_2 = cost_2_3;
if(cost_4_3 < cost_3_2) cost_3_2 = cost_4_3;
MapLocation loc_4_2 = rcCur.translate(4, 2);
long cost_4_2 = Long.MAX_VALUE;
if(cost_4_3 < cost_4_2) cost_4_2 = cost_4_3;
if(cost_3_3 < cost_4_2) cost_4_2 = cost_3_3;
MapLocation loc_5_3 = rcCur.translate(5, 3);
long cost_5_3 = heuristicCooldown(loc_5_3);
if(cost_5_3 < cost_4_2) cost_4_2 = cost_5_3;
MapLocation loc_5_2 = rcCur.translate(5, 2);
long cost_5_2 = heuristicCooldown(loc_5_2);
if(cost_5_2 < cost_4_2) cost_4_2 = cost_5_2;
cost_4_2 += smartCooldown(loc_4_2);
//best for [4, 2]
if(cost_4_2 < cost_3_2) cost_3_2 = cost_4_2;
cost_3_2 += smartCooldown(loc_3_2);
//best for [3, 2]
if(cost_3_2 < cost_2_2) cost_2_2 = cost_3_2;
cost_2_2 += smartCooldown(loc_2_2);
//best for [2, 2]
if(cost_2_2 < cost_1_2) cost_1_2 = cost_2_2;
cost_1_2 += smartCooldown(loc_1_2);
//best for [1, 2]
if(cost_1_2 < cost_0_2) cost_0_2 = cost_1_2;
cost_0_2 += smartCooldown(loc_0_2);
//best for [0, 2]
if(cost_0_2 < cost_0_1) cost_0_1 = cost_0_2;
if(cost_n1_2 < cost_0_1) cost_0_1 = cost_n1_2;
if(cost_1_2 < cost_0_1) cost_0_1 = cost_1_2;
MapLocation loc_n1_1 = rcCur.translate(-1, 1);
long cost_n1_1 = Long.MAX_VALUE;
if(cost_n1_2 < cost_n1_1) cost_n1_1 = cost_n1_2;
if(cost_n2_2 < cost_n1_1) cost_n1_1 = cost_n2_2;
if(cost_0_2 < cost_n1_1) cost_n1_1 = cost_0_2;
MapLocation loc_n2_1 = rcCur.translate(-2, 1);
long cost_n2_1 = Long.MAX_VALUE;
if(cost_n2_2 < cost_n2_1) cost_n2_1 = cost_n2_2;
if(cost_n3_2 < cost_n2_1) cost_n2_1 = cost_n3_2;
if(cost_n1_2 < cost_n2_1) cost_n2_1 = cost_n1_2;
MapLocation loc_n3_1 = rcCur.translate(-3, 1);
long cost_n3_1 = Long.MAX_VALUE;
if(cost_n3_2 < cost_n3_1) cost_n3_1 = cost_n3_2;
if(cost_n4_2 < cost_n3_1) cost_n3_1 = cost_n4_2;
if(cost_n2_2 < cost_n3_1) cost_n3_1 = cost_n2_2;
MapLocation loc_n4_1 = rcCur.translate(-4, 1);
long cost_n4_1 = Long.MAX_VALUE;
if(cost_n4_2 < cost_n4_1) cost_n4_1 = cost_n4_2;
if(cost_n5_2 < cost_n4_1) cost_n4_1 = cost_n5_2;
if(cost_n3_2 < cost_n4_1) cost_n4_1 = cost_n3_2;
MapLocation loc_n5_1 = rcCur.translate(-5, 1);
long cost_n5_1 = heuristicCooldown(loc_n5_1);
if(cost_n5_1 < cost_n4_1) cost_n4_1 = cost_n5_1;
cost_n4_1 += smartCooldown(loc_n4_1);
//best for [-4, 1]
if(cost_n4_1 < cost_n3_1) cost_n3_1 = cost_n4_1;
cost_n3_1 += smartCooldown(loc_n3_1);
//best for [-3, 1]
if(cost_n3_1 < cost_n2_1) cost_n2_1 = cost_n3_1;
cost_n2_1 += smartCooldown(loc_n2_1);
//best for [-2, 1]
if(cost_n2_1 < cost_n1_1) cost_n1_1 = cost_n2_1;
cost_n1_1 += smartCooldown(loc_n1_1);
//best for [-1, 1]
if(cost_n1_1 < cost_0_1) cost_0_1 = cost_n1_1;
MapLocation loc_1_1 = rcCur.translate(1, 1);
long cost_1_1 = Long.MAX_VALUE;
if(cost_1_2 < cost_1_1) cost_1_1 = cost_1_2;
if(cost_0_2 < cost_1_1) cost_1_1 = cost_0_2;
if(cost_2_2 < cost_1_1) cost_1_1 = cost_2_2;
MapLocation loc_2_1 = rcCur.translate(2, 1);
long cost_2_1 = Long.MAX_VALUE;
if(cost_2_2 < cost_2_1) cost_2_1 = cost_2_2;
if(cost_1_2 < cost_2_1) cost_2_1 = cost_1_2;
if(cost_3_2 < cost_2_1) cost_2_1 = cost_3_2;
MapLocation loc_3_1 = rcCur.translate(3, 1);
long cost_3_1 = Long.MAX_VALUE;
if(cost_3_2 < cost_3_1) cost_3_1 = cost_3_2;
if(cost_2_2 < cost_3_1) cost_3_1 = cost_2_2;
if(cost_4_2 < cost_3_1) cost_3_1 = cost_4_2;
MapLocation loc_4_1 = rcCur.translate(4, 1);
long cost_4_1 = Long.MAX_VALUE;
if(cost_4_2 < cost_4_1) cost_4_1 = cost_4_2;
if(cost_3_2 < cost_4_1) cost_4_1 = cost_3_2;
if(cost_5_2 < cost_4_1) cost_4_1 = cost_5_2;
MapLocation loc_5_1 = rcCur.translate(5, 1);
long cost_5_1 = heuristicCooldown(loc_5_1);
if(cost_5_1 < cost_4_1) cost_4_1 = cost_5_1;
cost_4_1 += smartCooldown(loc_4_1);
//best for [4, 1]
if(cost_4_1 < cost_3_1) cost_3_1 = cost_4_1;
cost_3_1 += smartCooldown(loc_3_1);
//best for [3, 1]
if(cost_3_1 < cost_2_1) cost_2_1 = cost_3_1;
cost_2_1 += smartCooldown(loc_2_1);
//best for [2, 1]
if(cost_2_1 < cost_1_1) cost_1_1 = cost_2_1;
cost_1_1 += smartCooldown(loc_1_1);
//best for [1, 1]
if(cost_1_1 < cost_0_1) cost_0_1 = cost_1_1;
cost_0_1 += smartCooldown(loc_0_1);
//best for [0, 1]
if(cost_0_1 < cost_0_0) { cost_0_0 = cost_0_1;best_dir = Direction.NORTH;}
if(cost_n1_1 < cost_0_0) { cost_0_0 = cost_n1_1;best_dir = Direction.NORTHWEST;}
if(cost_1_1 < cost_0_0) { cost_0_0 = cost_1_1;best_dir = Direction.NORTHEAST;}
MapLocation loc_n1_0 = rcCur.translate(-1, 0);
long cost_n1_0 = Long.MAX_VALUE;
if(cost_n1_1 < cost_n1_0) cost_n1_0 = cost_n1_1;
if(cost_n2_1 < cost_n1_0) cost_n1_0 = cost_n2_1;
if(cost_0_1 < cost_n1_0) cost_n1_0 = cost_0_1;
MapLocation loc_n2_0 = rcCur.translate(-2, 0);
long cost_n2_0 = Long.MAX_VALUE;
if(cost_n2_1 < cost_n2_0) cost_n2_0 = cost_n2_1;
if(cost_n3_1 < cost_n2_0) cost_n2_0 = cost_n3_1;
if(cost_n1_1 < cost_n2_0) cost_n2_0 = cost_n1_1;
MapLocation loc_n3_0 = rcCur.translate(-3, 0);
long cost_n3_0 = Long.MAX_VALUE;
if(cost_n3_1 < cost_n3_0) cost_n3_0 = cost_n3_1;
if(cost_n4_1 < cost_n3_0) cost_n3_0 = cost_n4_1;
if(cost_n2_1 < cost_n3_0) cost_n3_0 = cost_n2_1;
MapLocation loc_n4_0 = rcCur.translate(-4, 0);
long cost_n4_0 = Long.MAX_VALUE;
if(cost_n4_1 < cost_n4_0) cost_n4_0 = cost_n4_1;
if(cost_n5_1 < cost_n4_0) cost_n4_0 = cost_n5_1;
if(cost_n3_1 < cost_n4_0) cost_n4_0 = cost_n3_1;
MapLocation loc_n5_0 = rcCur.translate(-5, 0);
long cost_n5_0 = heuristicCooldown(loc_n5_0);
if(cost_n5_0 < cost_n4_0) cost_n4_0 = cost_n5_0;
cost_n4_0 += smartCooldown(loc_n4_0);
//best for [-4, 0]
if(cost_n4_0 < cost_n3_0) cost_n3_0 = cost_n4_0;
cost_n3_0 += smartCooldown(loc_n3_0);
//best for [-3, 0]
if(cost_n3_0 < cost_n2_0) cost_n2_0 = cost_n3_0;
cost_n2_0 += smartCooldown(loc_n2_0);
//best for [-2, 0]
if(cost_n2_0 < cost_n1_0) cost_n1_0 = cost_n2_0;
cost_n1_0 += smartCooldown(loc_n1_0);
//best for [-1, 0]
if(cost_n1_0 < cost_0_0) { cost_0_0 = cost_n1_0;best_dir = Direction.WEST;}
MapLocation loc_1_0 = rcCur.translate(1, 0);
long cost_1_0 = Long.MAX_VALUE;
if(cost_1_1 < cost_1_0) cost_1_0 = cost_1_1;
if(cost_0_1 < cost_1_0) cost_1_0 = cost_0_1;
if(cost_2_1 < cost_1_0) cost_1_0 = cost_2_1;
MapLocation loc_2_0 = rcCur.translate(2, 0);
long cost_2_0 = Long.MAX_VALUE;
if(cost_2_1 < cost_2_0) cost_2_0 = cost_2_1;
if(cost_1_1 < cost_2_0) cost_2_0 = cost_1_1;
if(cost_3_1 < cost_2_0) cost_2_0 = cost_3_1;
MapLocation loc_3_0 = rcCur.translate(3, 0);
long cost_3_0 = Long.MAX_VALUE;
if(cost_3_1 < cost_3_0) cost_3_0 = cost_3_1;
if(cost_2_1 < cost_3_0) cost_3_0 = cost_2_1;
if(cost_4_1 < cost_3_0) cost_3_0 = cost_4_1;
MapLocation loc_4_0 = rcCur.translate(4, 0);
long cost_4_0 = Long.MAX_VALUE;
if(cost_4_1 < cost_4_0) cost_4_0 = cost_4_1;
if(cost_3_1 < cost_4_0) cost_4_0 = cost_3_1;
if(cost_5_1 < cost_4_0) cost_4_0 = cost_5_1;
MapLocation loc_5_0 = rcCur.translate(5, 0);
long cost_5_0 = heuristicCooldown(loc_5_0);
if(cost_5_0 < cost_4_0) cost_4_0 = cost_5_0;
cost_4_0 += smartCooldown(loc_4_0);
//best for [4, 0]
if(cost_4_0 < cost_3_0) cost_3_0 = cost_4_0;
cost_3_0 += smartCooldown(loc_3_0);
//best for [3, 0]
if(cost_3_0 < cost_2_0) cost_2_0 = cost_3_0;
cost_2_0 += smartCooldown(loc_2_0);
//best for [2, 0]
if(cost_2_0 < cost_1_0) cost_1_0 = cost_2_0;
cost_1_0 += smartCooldown(loc_1_0);
//best for [1, 0]
if(cost_1_0 < cost_0_0) { cost_0_0 = cost_1_0;best_dir = Direction.EAST;}
cost_0_0 += smartCooldown(loc_0_0);
//best for [0, 0]
return best_dir;
}
static Direction pathfind_NORTHEAST() throws GameActionException {
MapLocation loc_0_0 = rcCur.translate(0, 0);
long cost_0_0 = Long.MAX_VALUE;
Direction best_dir = null;
MapLocation loc_1_1 = rcCur.translate(1, 1);
long cost_1_1 = Long.MAX_VALUE;
MapLocation loc_2_2 = rcCur.translate(2, 2);
long cost_2_2 = Long.MAX_VALUE;
MapLocation loc_3_3 = rcCur.translate(3, 3);
long cost_3_3 = Long.MAX_VALUE;
MapLocation loc_4_4 = rcCur.translate(4, 4);
long cost_4_4 = heuristicCooldown(loc_4_4);
if(cost_4_4 < cost_3_3) cost_3_3 = cost_4_4;
MapLocation loc_3_4 = rcCur.translate(3, 4);
long cost_3_4 = heuristicCooldown(loc_3_4);
if(cost_3_4 < cost_3_3) cost_3_3 = cost_3_4;
MapLocation loc_4_3 = rcCur.translate(4, 3);
long cost_4_3 = heuristicCooldown(loc_4_3);
if(cost_4_3 < cost_3_3) cost_3_3 = cost_4_3;
MapLocation loc_2_4 = rcCur.translate(2, 4);
long cost_2_4 = Long.MAX_VALUE;
MapLocation loc_3_5 = rcCur.translate(3, 5);
long cost_3_5 = heuristicCooldown(loc_3_5);
if(cost_3_5 < cost_2_4) cost_2_4 = cost_3_5;
MapLocation loc_2_5 = rcCur.translate(2, 5);
long cost_2_5 = heuristicCooldown(loc_2_5);
if(cost_2_5 < cost_2_4) cost_2_4 = cost_2_5;
if(cost_3_4 < cost_2_4) cost_2_4 = cost_3_4;
MapLocation loc_1_5 = rcCur.translate(1, 5);
long cost_1_5 = heuristicCooldown(loc_1_5);
if(cost_1_5 < cost_2_4) cost_2_4 = cost_1_5;
cost_2_4 += smartCooldown(loc_2_4);
//best for [2, 4]
if(cost_2_4 < cost_3_3) cost_3_3 = cost_2_4;
MapLocation loc_4_2 = rcCur.translate(4, 2);
long cost_4_2 = Long.MAX_VALUE;
MapLocation loc_5_3 = rcCur.translate(5, 3);
long cost_5_3 = heuristicCooldown(loc_5_3);
if(cost_5_3 < cost_4_2) cost_4_2 = cost_5_3;
if(cost_4_3 < cost_4_2) cost_4_2 = cost_4_3;
MapLocation loc_5_2 = rcCur.translate(5, 2);
long cost_5_2 = heuristicCooldown(loc_5_2);
if(cost_5_2 < cost_4_2) cost_4_2 = cost_5_2;
MapLocation loc_5_1 = rcCur.translate(5, 1);
long cost_5_1 = heuristicCooldown(loc_5_1);
if(cost_5_1 < cost_4_2) cost_4_2 = cost_5_1;
cost_4_2 += smartCooldown(loc_4_2);
//best for [4, 2]
if(cost_4_2 < cost_3_3) cost_3_3 = cost_4_2;
cost_3_3 += smartCooldown(loc_3_3);
//best for [3, 3]
if(cost_3_3 < cost_2_2) cost_2_2 = cost_3_3;
MapLocation loc_2_3 = rcCur.translate(2, 3);
long cost_2_3 = Long.MAX_VALUE;
if(cost_3_4 < cost_2_3) cost_2_3 = cost_3_4;
if(cost_2_4 < cost_2_3) cost_2_3 = cost_2_4;
if(cost_3_3 < cost_2_3) cost_2_3 = cost_3_3;
MapLocation loc_1_4 = rcCur.translate(1, 4);
long cost_1_4 = Long.MAX_VALUE;
if(cost_2_5 < cost_1_4) cost_1_4 = cost_2_5;
if(cost_1_5 < cost_1_4) cost_1_4 = cost_1_5;
if(cost_2_4 < cost_1_4) cost_1_4 = cost_2_4;
MapLocation loc_0_5 = rcCur.translate(0, 5);
long cost_0_5 = heuristicCooldown(loc_0_5);
if(cost_0_5 < cost_1_4) cost_1_4 = cost_0_5;
cost_1_4 += smartCooldown(loc_1_4);
//best for [1, 4]
if(cost_1_4 < cost_2_3) cost_2_3 = cost_1_4;
MapLocation loc_3_2 = rcCur.translate(3, 2);
long cost_3_2 = Long.MAX_VALUE;
if(cost_4_3 < cost_3_2) cost_3_2 = cost_4_3;
if(cost_3_3 < cost_3_2) cost_3_2 = cost_3_3;
if(cost_4_2 < cost_3_2) cost_3_2 = cost_4_2;
MapLocation loc_4_1 = rcCur.translate(4, 1);
long cost_4_1 = Long.MAX_VALUE;
if(cost_5_2 < cost_4_1) cost_4_1 = cost_5_2;
if(cost_4_2 < cost_4_1) cost_4_1 = cost_4_2;
if(cost_5_1 < cost_4_1) cost_4_1 = cost_5_1;
MapLocation loc_5_0 = rcCur.translate(5, 0);
long cost_5_0 = heuristicCooldown(loc_5_0);
if(cost_5_0 < cost_4_1) cost_4_1 = cost_5_0;
cost_4_1 += smartCooldown(loc_4_1);
//best for [4, 1]
if(cost_4_1 < cost_3_2) cost_3_2 = cost_4_1;
cost_3_2 += smartCooldown(loc_3_2);
//best for [3, 2]
if(cost_3_2 < cost_2_3) cost_2_3 = cost_3_2;
cost_2_3 += smartCooldown(loc_2_3);
//best for [2, 3]
if(cost_2_3 < cost_2_2) cost_2_2 = cost_2_3;
if(cost_3_2 < cost_2_2) cost_2_2 = cost_3_2;
MapLocation loc_1_3 = rcCur.translate(1, 3);
long cost_1_3 = Long.MAX_VALUE;
if(cost_2_4 < cost_1_3) cost_1_3 = cost_2_4;
if(cost_1_4 < cost_1_3) cost_1_3 = cost_1_4;
if(cost_2_3 < cost_1_3) cost_1_3 = cost_2_3;
MapLocation loc_0_4 = rcCur.translate(0, 4);
long cost_0_4 = Long.MAX_VALUE;
if(cost_1_5 < cost_0_4) cost_0_4 = cost_1_5;
if(cost_0_5 < cost_0_4) cost_0_4 = cost_0_5;
if(cost_1_4 < cost_0_4) cost_0_4 = cost_1_4;
MapLocation loc_n1_5 = rcCur.translate(-1, 5);
long cost_n1_5 = heuristicCooldown(loc_n1_5);
if(cost_n1_5 < cost_0_4) cost_0_4 = cost_n1_5;
cost_0_4 += smartCooldown(loc_0_4);
//best for [0, 4]
if(cost_0_4 < cost_1_3) cost_1_3 = cost_0_4;
cost_1_3 += smartCooldown(loc_1_3);
//best for [1, 3]
if(cost_1_3 < cost_2_2) cost_2_2 = cost_1_3;
MapLocation loc_3_1 = rcCur.translate(3, 1);
long cost_3_1 = Long.MAX_VALUE;
if(cost_4_2 < cost_3_1) cost_3_1 = cost_4_2;
if(cost_3_2 < cost_3_1) cost_3_1 = cost_3_2;
if(cost_4_1 < cost_3_1) cost_3_1 = cost_4_1;
MapLocation loc_4_0 = rcCur.translate(4, 0);
long cost_4_0 = Long.MAX_VALUE;
if(cost_5_1 < cost_4_0) cost_4_0 = cost_5_1;
if(cost_4_1 < cost_4_0) cost_4_0 = cost_4_1;
if(cost_5_0 < cost_4_0) cost_4_0 = cost_5_0;
MapLocation loc_5_n1 = rcCur.translate(5, -1);
long cost_5_n1 = heuristicCooldown(loc_5_n1);
if(cost_5_n1 < cost_4_0) cost_4_0 = cost_5_n1;
cost_4_0 += smartCooldown(loc_4_0);
//best for [4, 0]
if(cost_4_0 < cost_3_1) cost_3_1 = cost_4_0;
cost_3_1 += smartCooldown(loc_3_1);
//best for [3, 1]
if(cost_3_1 < cost_2_2) cost_2_2 = cost_3_1;
cost_2_2 += smartCooldown(loc_2_2);
//best for [2, 2]
if(cost_2_2 < cost_1_1) cost_1_1 = cost_2_2;
MapLocation loc_1_2 = rcCur.translate(1, 2);
long cost_1_2 = Long.MAX_VALUE;
if(cost_2_3 < cost_1_2) cost_1_2 = cost_2_3;
if(cost_1_3 < cost_1_2) cost_1_2 = cost_1_3;
if(cost_2_2 < cost_1_2) cost_1_2 = cost_2_2;
MapLocation loc_0_3 = rcCur.translate(0, 3);
long cost_0_3 = Long.MAX_VALUE;
if(cost_1_4 < cost_0_3) cost_0_3 = cost_1_4;
if(cost_0_4 < cost_0_3) cost_0_3 = cost_0_4;
if(cost_1_3 < cost_0_3) cost_0_3 = cost_1_3;
MapLocation loc_n1_4 = rcCur.translate(-1, 4);
long cost_n1_4 = Long.MAX_VALUE;
if(cost_0_5 < cost_n1_4) cost_n1_4 = cost_0_5;
if(cost_n1_5 < cost_n1_4) cost_n1_4 = cost_n1_5;
if(cost_0_4 < cost_n1_4) cost_n1_4 = cost_0_4;
MapLocation loc_n2_5 = rcCur.translate(-2, 5);
long cost_n2_5 = heuristicCooldown(loc_n2_5);
if(cost_n2_5 < cost_n1_4) cost_n1_4 = cost_n2_5;
cost_n1_4 += smartCooldown(loc_n1_4);
//best for [-1, 4]
if(cost_n1_4 < cost_0_3) cost_0_3 = cost_n1_4;
cost_0_3 += smartCooldown(loc_0_3);
//best for [0, 3]
if(cost_0_3 < cost_1_2) cost_1_2 = cost_0_3;
MapLocation loc_2_1 = rcCur.translate(2, 1);
long cost_2_1 = Long.MAX_VALUE;
if(cost_3_2 < cost_2_1) cost_2_1 = cost_3_2;
if(cost_2_2 < cost_2_1) cost_2_1 = cost_2_2;
if(cost_3_1 < cost_2_1) cost_2_1 = cost_3_1;
MapLocation loc_3_0 = rcCur.translate(3, 0);
long cost_3_0 = Long.MAX_VALUE;
if(cost_4_1 < cost_3_0) cost_3_0 = cost_4_1;
if(cost_3_1 < cost_3_0) cost_3_0 = cost_3_1;
if(cost_4_0 < cost_3_0) cost_3_0 = cost_4_0;
MapLocation loc_4_n1 = rcCur.translate(4, -1);
long cost_4_n1 = Long.MAX_VALUE;
if(cost_5_0 < cost_4_n1) cost_4_n1 = cost_5_0;
if(cost_4_0 < cost_4_n1) cost_4_n1 = cost_4_0;
if(cost_5_n1 < cost_4_n1) cost_4_n1 = cost_5_n1;
MapLocation loc_5_n2 = rcCur.translate(5, -2);
long cost_5_n2 = heuristicCooldown(loc_5_n2);
if(cost_5_n2 < cost_4_n1) cost_4_n1 = cost_5_n2;
cost_4_n1 += smartCooldown(loc_4_n1);
//best for [4, -1]
if(cost_4_n1 < cost_3_0) cost_3_0 = cost_4_n1;
cost_3_0 += smartCooldown(loc_3_0);
//best for [3, 0]
if(cost_3_0 < cost_2_1) cost_2_1 = cost_3_0;
cost_2_1 += smartCooldown(loc_2_1);
//best for [2, 1]
if(cost_2_1 < cost_1_2) cost_1_2 = cost_2_1;
cost_1_2 += smartCooldown(loc_1_2);
//best for [1, 2]
if(cost_1_2 < cost_1_1) cost_1_1 = cost_1_2;
if(cost_2_1 < cost_1_1) cost_1_1 = cost_2_1;
MapLocation loc_0_2 = rcCur.translate(0, 2);
long cost_0_2 = Long.MAX_VALUE;
if(cost_1_3 < cost_0_2) cost_0_2 = cost_1_3;
if(cost_0_3 < cost_0_2) cost_0_2 = cost_0_3;
if(cost_1_2 < cost_0_2) cost_0_2 = cost_1_2;
MapLocation loc_n1_3 = rcCur.translate(-1, 3);
long cost_n1_3 = Long.MAX_VALUE;
if(cost_0_4 < cost_n1_3) cost_n1_3 = cost_0_4;
if(cost_n1_4 < cost_n1_3) cost_n1_3 = cost_n1_4;
if(cost_0_3 < cost_n1_3) cost_n1_3 = cost_0_3;
MapLocation loc_n2_4 = rcCur.translate(-2, 4);
long cost_n2_4 = Long.MAX_VALUE;
if(cost_n1_5 < cost_n2_4) cost_n2_4 = cost_n1_5;
if(cost_n2_5 < cost_n2_4) cost_n2_4 = cost_n2_5;
if(cost_n1_4 < cost_n2_4) cost_n2_4 = cost_n1_4;
MapLocation loc_n3_5 = rcCur.translate(-3, 5);
long cost_n3_5 = heuristicCooldown(loc_n3_5);
if(cost_n3_5 < cost_n2_4) cost_n2_4 = cost_n3_5;
cost_n2_4 += smartCooldown(loc_n2_4);
//best for [-2, 4]
if(cost_n2_4 < cost_n1_3) cost_n1_3 = cost_n2_4;
cost_n1_3 += smartCooldown(loc_n1_3);
//best for [-1, 3]
if(cost_n1_3 < cost_0_2) cost_0_2 = cost_n1_3;
cost_0_2 += smartCooldown(loc_0_2);
//best for [0, 2]
if(cost_0_2 < cost_1_1) cost_1_1 = cost_0_2;
MapLocation loc_2_0 = rcCur.translate(2, 0);
long cost_2_0 = Long.MAX_VALUE;
if(cost_3_1 < cost_2_0) cost_2_0 = cost_3_1;
if(cost_2_1 < cost_2_0) cost_2_0 = cost_2_1;
if(cost_3_0 < cost_2_0) cost_2_0 = cost_3_0;
MapLocation loc_3_n1 = rcCur.translate(3, -1);
long cost_3_n1 = Long.MAX_VALUE;
if(cost_4_0 < cost_3_n1) cost_3_n1 = cost_4_0;
if(cost_3_0 < cost_3_n1) cost_3_n1 = cost_3_0;
if(cost_4_n1 < cost_3_n1) cost_3_n1 = cost_4_n1;
MapLocation loc_4_n2 = rcCur.translate(4, -2);
long cost_4_n2 = Long.MAX_VALUE;
if(cost_5_n1 < cost_4_n2) cost_4_n2 = cost_5_n1;
if(cost_4_n1 < cost_4_n2) cost_4_n2 = cost_4_n1;
if(cost_5_n2 < cost_4_n2) cost_4_n2 = cost_5_n2;
MapLocation loc_5_n3 = rcCur.translate(5, -3);
long cost_5_n3 = heuristicCooldown(loc_5_n3);
if(cost_5_n3 < cost_4_n2) cost_4_n2 = cost_5_n3;
cost_4_n2 += smartCooldown(loc_4_n2);
//best for [4, -2]
if(cost_4_n2 < cost_3_n1) cost_3_n1 = cost_4_n2;
cost_3_n1 += smartCooldown(loc_3_n1);
//best for [3, -1]
if(cost_3_n1 < cost_2_0) cost_2_0 = cost_3_n1;
cost_2_0 += smartCooldown(loc_2_0);
//best for [2, 0]
if(cost_2_0 < cost_1_1) cost_1_1 = cost_2_0;
cost_1_1 += smartCooldown(loc_1_1);
//best for [1, 1]
if(cost_1_1 < cost_0_0) { cost_0_0 = cost_1_1;best_dir = Direction.NORTHEAST;}
MapLocation loc_0_1 = rcCur.translate(0, 1);
long cost_0_1 = Long.MAX_VALUE;
if(cost_1_2 < cost_0_1) cost_0_1 = cost_1_2;
if(cost_0_2 < cost_0_1) cost_0_1 = cost_0_2;
if(cost_1_1 < cost_0_1) cost_0_1 = cost_1_1;
MapLocation loc_n1_2 = rcCur.translate(-1, 2);
long cost_n1_2 = Long.MAX_VALUE;
if(cost_0_3 < cost_n1_2) cost_n1_2 = cost_0_3;
if(cost_n1_3 < cost_n1_2) cost_n1_2 = cost_n1_3;
if(cost_0_2 < cost_n1_2) cost_n1_2 = cost_0_2;
MapLocation loc_n2_3 = rcCur.translate(-2, 3);
long cost_n2_3 = Long.MAX_VALUE;
if(cost_n1_4 < cost_n2_3) cost_n2_3 = cost_n1_4;
if(cost_n2_4 < cost_n2_3) cost_n2_3 = cost_n2_4;
if(cost_n1_3 < cost_n2_3) cost_n2_3 = cost_n1_3;
MapLocation loc_n3_4 = rcCur.translate(-3, 4);
long cost_n3_4 = heuristicCooldown(loc_n3_4);
if(cost_n3_4 < cost_n2_3) cost_n2_3 = cost_n3_4;
cost_n2_3 += smartCooldown(loc_n2_3);
//best for [-2, 3]
if(cost_n2_3 < cost_n1_2) cost_n1_2 = cost_n2_3;
cost_n1_2 += smartCooldown(loc_n1_2);
//best for [-1, 2]
if(cost_n1_2 < cost_0_1) cost_0_1 = cost_n1_2;
MapLocation loc_1_0 = rcCur.translate(1, 0);
long cost_1_0 = Long.MAX_VALUE;
if(cost_2_1 < cost_1_0) cost_1_0 = cost_2_1;
if(cost_1_1 < cost_1_0) cost_1_0 = cost_1_1;
if(cost_2_0 < cost_1_0) cost_1_0 = cost_2_0;
MapLocation loc_2_n1 = rcCur.translate(2, -1);
long cost_2_n1 = Long.MAX_VALUE;
if(cost_3_0 < cost_2_n1) cost_2_n1 = cost_3_0;
if(cost_2_0 < cost_2_n1) cost_2_n1 = cost_2_0;
if(cost_3_n1 < cost_2_n1) cost_2_n1 = cost_3_n1;
MapLocation loc_3_n2 = rcCur.translate(3, -2);
long cost_3_n2 = Long.MAX_VALUE;
if(cost_4_n1 < cost_3_n2) cost_3_n2 = cost_4_n1;
if(cost_3_n1 < cost_3_n2) cost_3_n2 = cost_3_n1;
if(cost_4_n2 < cost_3_n2) cost_3_n2 = cost_4_n2;
MapLocation loc_4_n3 = rcCur.translate(4, -3);
long cost_4_n3 = heuristicCooldown(loc_4_n3);
if(cost_4_n3 < cost_3_n2) cost_3_n2 = cost_4_n3;
cost_3_n2 += smartCooldown(loc_3_n2);
//best for [3, -2]
if(cost_3_n2 < cost_2_n1) cost_2_n1 = cost_3_n2;
cost_2_n1 += smartCooldown(loc_2_n1);
//best for [2, -1]
if(cost_2_n1 < cost_1_0) cost_1_0 = cost_2_n1;
cost_1_0 += smartCooldown(loc_1_0);
//best for [1, 0]
if(cost_1_0 < cost_0_1) cost_0_1 = cost_1_0;
cost_0_1 += smartCooldown(loc_0_1);
//best for [0, 1]
if(cost_0_1 < cost_0_0) { cost_0_0 = cost_0_1;best_dir = Direction.NORTH;}
if(cost_1_0 < cost_0_0) { cost_0_0 = cost_1_0;best_dir = Direction.EAST;}
MapLocation loc_n1_1 = rcCur.translate(-1, 1);
long cost_n1_1 = Long.MAX_VALUE;
if(cost_0_2 < cost_n1_1) cost_n1_1 = cost_0_2;
if(cost_n1_2 < cost_n1_1) cost_n1_1 = cost_n1_2;
if(cost_0_1 < cost_n1_1) cost_n1_1 = cost_0_1;
MapLocation loc_n2_2 = rcCur.translate(-2, 2);
long cost_n2_2 = Long.MAX_VALUE;
if(cost_n1_3 < cost_n2_2) cost_n2_2 = cost_n1_3;
if(cost_n2_3 < cost_n2_2) cost_n2_2 = cost_n2_3;
if(cost_n1_2 < cost_n2_2) cost_n2_2 = cost_n1_2;
MapLocation loc_n3_3 = rcCur.translate(-3, 3);
long cost_n3_3 = Long.MAX_VALUE;
if(cost_n2_4 < cost_n3_3) cost_n3_3 = cost_n2_4;
if(cost_n3_4 < cost_n3_3) cost_n3_3 = cost_n3_4;
if(cost_n2_3 < cost_n3_3) cost_n3_3 = cost_n2_3;
MapLocation loc_n4_4 = rcCur.translate(-4, 4);
long cost_n4_4 = heuristicCooldown(loc_n4_4);
if(cost_n4_4 < cost_n3_3) cost_n3_3 = cost_n4_4;
cost_n3_3 += smartCooldown(loc_n3_3);
//best for [-3, 3]
if(cost_n3_3 < cost_n2_2) cost_n2_2 = cost_n3_3;
cost_n2_2 += smartCooldown(loc_n2_2);
//best for [-2, 2]
if(cost_n2_2 < cost_n1_1) cost_n1_1 = cost_n2_2;
cost_n1_1 += smartCooldown(loc_n1_1);
//best for [-1, 1]
if(cost_n1_1 < cost_0_0) { cost_0_0 = cost_n1_1;best_dir = Direction.NORTHWEST;}
MapLocation loc_1_n1 = rcCur.translate(1, -1);
long cost_1_n1 = Long.MAX_VALUE;
if(cost_2_0 < cost_1_n1) cost_1_n1 = cost_2_0;
if(cost_1_0 < cost_1_n1) cost_1_n1 = cost_1_0;
if(cost_2_n1 < cost_1_n1) cost_1_n1 = cost_2_n1;
MapLocation loc_2_n2 = rcCur.translate(2, -2);
long cost_2_n2 = Long.MAX_VALUE;
if(cost_3_n1 < cost_2_n2) cost_2_n2 = cost_3_n1;
if(cost_2_n1 < cost_2_n2) cost_2_n2 = cost_2_n1;
if(cost_3_n2 < cost_2_n2) cost_2_n2 = cost_3_n2;
MapLocation loc_3_n3 = rcCur.translate(3, -3);
long cost_3_n3 = Long.MAX_VALUE;
if(cost_4_n2 < cost_3_n3) cost_3_n3 = cost_4_n2;
if(cost_3_n2 < cost_3_n3) cost_3_n3 = cost_3_n2;
if(cost_4_n3 < cost_3_n3) cost_3_n3 = cost_4_n3;
MapLocation loc_4_n4 = rcCur.translate(4, -4);
long cost_4_n4 = heuristicCooldown(loc_4_n4);
if(cost_4_n4 < cost_3_n3) cost_3_n3 = cost_4_n4;
cost_3_n3 += smartCooldown(loc_3_n3);
//best for [3, -3]
if(cost_3_n3 < cost_2_n2) cost_2_n2 = cost_3_n3;
cost_2_n2 += smartCooldown(loc_2_n2);
//best for [2, -2]
if(cost_2_n2 < cost_1_n1) cost_1_n1 = cost_2_n2;
cost_1_n1 += smartCooldown(loc_1_n1);
//best for [1, -1]
if(cost_1_n1 < cost_0_0) { cost_0_0 = cost_1_n1;best_dir = Direction.SOUTHEAST;}
cost_0_0 += smartCooldown(loc_0_0);
//best for [0, 0]
return best_dir;
}
static Direction pathfind_EAST() throws GameActionException {
MapLocation loc_0_0 = rcCur.translate(0, 0);
long cost_0_0 = Long.MAX_VALUE;
Direction best_dir = null;
MapLocation loc_1_0 = rcCur.translate(1, 0);
long cost_1_0 = Long.MAX_VALUE;
MapLocation loc_2_0 = rcCur.translate(2, 0);
long cost_2_0 = Long.MAX_VALUE;
MapLocation loc_3_0 = rcCur.translate(3, 0);
long cost_3_0 = Long.MAX_VALUE;
MapLocation loc_4_0 = rcCur.translate(4, 0);
long cost_4_0 = Long.MAX_VALUE;
MapLocation loc_5_0 = rcCur.translate(5, 0);
long cost_5_0 = heuristicCooldown(loc_5_0);
if(cost_5_0 < cost_4_0) cost_4_0 = cost_5_0;
MapLocation loc_5_1 = rcCur.translate(5, 1);
long cost_5_1 = heuristicCooldown(loc_5_1);
if(cost_5_1 < cost_4_0) cost_4_0 = cost_5_1;
MapLocation loc_5_n1 = rcCur.translate(5, -1);
long cost_5_n1 = heuristicCooldown(loc_5_n1);
if(cost_5_n1 < cost_4_0) cost_4_0 = cost_5_n1;
MapLocation loc_4_1 = rcCur.translate(4, 1);
long cost_4_1 = Long.MAX_VALUE;
if(cost_5_1 < cost_4_1) cost_4_1 = cost_5_1;
MapLocation loc_5_2 = rcCur.translate(5, 2);
long cost_5_2 = heuristicCooldown(loc_5_2);
if(cost_5_2 < cost_4_1) cost_4_1 = cost_5_2;
if(cost_5_0 < cost_4_1) cost_4_1 = cost_5_0;
MapLocation loc_4_2 = rcCur.translate(4, 2);
long cost_4_2 = Long.MAX_VALUE;
if(cost_5_2 < cost_4_2) cost_4_2 = cost_5_2;
MapLocation loc_5_3 = rcCur.translate(5, 3);
long cost_5_3 = heuristicCooldown(loc_5_3);
if(cost_5_3 < cost_4_2) cost_4_2 = cost_5_3;
if(cost_5_1 < cost_4_2) cost_4_2 = cost_5_1;
MapLocation loc_4_3 = rcCur.translate(4, 3);
long cost_4_3 = heuristicCooldown(loc_4_3);
if(cost_4_3 < cost_4_2) cost_4_2 = cost_4_3;
cost_4_2 += smartCooldown(loc_4_2);
//best for [4, 2]
if(cost_4_2 < cost_4_1) cost_4_1 = cost_4_2;
cost_4_1 += smartCooldown(loc_4_1);
//best for [4, 1]
if(cost_4_1 < cost_4_0) cost_4_0 = cost_4_1;
MapLocation loc_4_n1 = rcCur.translate(4, -1);
long cost_4_n1 = Long.MAX_VALUE;
if(cost_5_n1 < cost_4_n1) cost_4_n1 = cost_5_n1;
if(cost_5_0 < cost_4_n1) cost_4_n1 = cost_5_0;
MapLocation loc_5_n2 = rcCur.translate(5, -2);
long cost_5_n2 = heuristicCooldown(loc_5_n2);
if(cost_5_n2 < cost_4_n1) cost_4_n1 = cost_5_n2;
MapLocation loc_4_n2 = rcCur.translate(4, -2);
long cost_4_n2 = Long.MAX_VALUE;
if(cost_5_n2 < cost_4_n2) cost_4_n2 = cost_5_n2;
if(cost_5_n1 < cost_4_n2) cost_4_n2 = cost_5_n1;
MapLocation loc_5_n3 = rcCur.translate(5, -3);
long cost_5_n3 = heuristicCooldown(loc_5_n3);
if(cost_5_n3 < cost_4_n2) cost_4_n2 = cost_5_n3;
MapLocation loc_4_n3 = rcCur.translate(4, -3);
long cost_4_n3 = heuristicCooldown(loc_4_n3);
if(cost_4_n3 < cost_4_n2) cost_4_n2 = cost_4_n3;
cost_4_n2 += smartCooldown(loc_4_n2);
//best for [4, -2]
if(cost_4_n2 < cost_4_n1) cost_4_n1 = cost_4_n2;
cost_4_n1 += smartCooldown(loc_4_n1);
//best for [4, -1]
if(cost_4_n1 < cost_4_0) cost_4_0 = cost_4_n1;
cost_4_0 += smartCooldown(loc_4_0);
//best for [4, 0]
if(cost_4_0 < cost_3_0) cost_3_0 = cost_4_0;
if(cost_4_1 < cost_3_0) cost_3_0 = cost_4_1;
if(cost_4_n1 < cost_3_0) cost_3_0 = cost_4_n1;
MapLocation loc_3_1 = rcCur.translate(3, 1);
long cost_3_1 = Long.MAX_VALUE;
if(cost_4_1 < cost_3_1) cost_3_1 = cost_4_1;
if(cost_4_2 < cost_3_1) cost_3_1 = cost_4_2;
if(cost_4_0 < cost_3_1) cost_3_1 = cost_4_0;
MapLocation loc_3_2 = rcCur.translate(3, 2);
long cost_3_2 = Long.MAX_VALUE;
if(cost_4_2 < cost_3_2) cost_3_2 = cost_4_2;
if(cost_4_3 < cost_3_2) cost_3_2 = cost_4_3;
if(cost_4_1 < cost_3_2) cost_3_2 = cost_4_1;
MapLocation loc_3_3 = rcCur.translate(3, 3);
long cost_3_3 = Long.MAX_VALUE;
if(cost_4_3 < cost_3_3) cost_3_3 = cost_4_3;
MapLocation loc_4_4 = rcCur.translate(4, 4);
long cost_4_4 = heuristicCooldown(loc_4_4);
if(cost_4_4 < cost_3_3) cost_3_3 = cost_4_4;
if(cost_4_2 < cost_3_3) cost_3_3 = cost_4_2;
MapLocation loc_3_4 = rcCur.translate(3, 4);
long cost_3_4 = heuristicCooldown(loc_3_4);
if(cost_3_4 < cost_3_3) cost_3_3 = cost_3_4;
cost_3_3 += smartCooldown(loc_3_3);
//best for [3, 3]
if(cost_3_3 < cost_3_2) cost_3_2 = cost_3_3;
cost_3_2 += smartCooldown(loc_3_2);
//best for [3, 2]
if(cost_3_2 < cost_3_1) cost_3_1 = cost_3_2;
cost_3_1 += smartCooldown(loc_3_1);
//best for [3, 1]
if(cost_3_1 < cost_3_0) cost_3_0 = cost_3_1;
MapLocation loc_3_n1 = rcCur.translate(3, -1);
long cost_3_n1 = Long.MAX_VALUE;
if(cost_4_n1 < cost_3_n1) cost_3_n1 = cost_4_n1;
if(cost_4_0 < cost_3_n1) cost_3_n1 = cost_4_0;
if(cost_4_n2 < cost_3_n1) cost_3_n1 = cost_4_n2;
MapLocation loc_3_n2 = rcCur.translate(3, -2);
long cost_3_n2 = Long.MAX_VALUE;
if(cost_4_n2 < cost_3_n2) cost_3_n2 = cost_4_n2;
if(cost_4_n1 < cost_3_n2) cost_3_n2 = cost_4_n1;
if(cost_4_n3 < cost_3_n2) cost_3_n2 = cost_4_n3;
MapLocation loc_3_n3 = rcCur.translate(3, -3);
long cost_3_n3 = Long.MAX_VALUE;
if(cost_4_n3 < cost_3_n3) cost_3_n3 = cost_4_n3;
if(cost_4_n2 < cost_3_n3) cost_3_n3 = cost_4_n2;
MapLocation loc_4_n4 = rcCur.translate(4, -4);
long cost_4_n4 = heuristicCooldown(loc_4_n4);
if(cost_4_n4 < cost_3_n3) cost_3_n3 = cost_4_n4;
MapLocation loc_3_n4 = rcCur.translate(3, -4);
long cost_3_n4 = heuristicCooldown(loc_3_n4);
if(cost_3_n4 < cost_3_n3) cost_3_n3 = cost_3_n4;
cost_3_n3 += smartCooldown(loc_3_n3);
//best for [3, -3]
if(cost_3_n3 < cost_3_n2) cost_3_n2 = cost_3_n3;
cost_3_n2 += smartCooldown(loc_3_n2);
//best for [3, -2]
if(cost_3_n2 < cost_3_n1) cost_3_n1 = cost_3_n2;
cost_3_n1 += smartCooldown(loc_3_n1);
//best for [3, -1]
if(cost_3_n1 < cost_3_0) cost_3_0 = cost_3_n1;
cost_3_0 += smartCooldown(loc_3_0);
//best for [3, 0]
if(cost_3_0 < cost_2_0) cost_2_0 = cost_3_0;
if(cost_3_1 < cost_2_0) cost_2_0 = cost_3_1;
if(cost_3_n1 < cost_2_0) cost_2_0 = cost_3_n1;
MapLocation loc_2_1 = rcCur.translate(2, 1);
long cost_2_1 = Long.MAX_VALUE;
if(cost_3_1 < cost_2_1) cost_2_1 = cost_3_1;
if(cost_3_2 < cost_2_1) cost_2_1 = cost_3_2;
if(cost_3_0 < cost_2_1) cost_2_1 = cost_3_0;
MapLocation loc_2_2 = rcCur.translate(2, 2);
long cost_2_2 = Long.MAX_VALUE;
if(cost_3_2 < cost_2_2) cost_2_2 = cost_3_2;
if(cost_3_3 < cost_2_2) cost_2_2 = cost_3_3;
if(cost_3_1 < cost_2_2) cost_2_2 = cost_3_1;
MapLocation loc_2_3 = rcCur.translate(2, 3);
long cost_2_3 = Long.MAX_VALUE;
if(cost_3_3 < cost_2_3) cost_2_3 = cost_3_3;
if(cost_3_4 < cost_2_3) cost_2_3 = cost_3_4;
if(cost_3_2 < cost_2_3) cost_2_3 = cost_3_2;
MapLocation loc_2_4 = rcCur.translate(2, 4);
long cost_2_4 = Long.MAX_VALUE;
if(cost_3_4 < cost_2_4) cost_2_4 = cost_3_4;
MapLocation loc_3_5 = rcCur.translate(3, 5);
long cost_3_5 = heuristicCooldown(loc_3_5);
if(cost_3_5 < cost_2_4) cost_2_4 = cost_3_5;
if(cost_3_3 < cost_2_4) cost_2_4 = cost_3_3;
MapLocation loc_2_5 = rcCur.translate(2, 5);
long cost_2_5 = heuristicCooldown(loc_2_5);
if(cost_2_5 < cost_2_4) cost_2_4 = cost_2_5;
cost_2_4 += smartCooldown(loc_2_4);
//best for [2, 4]
if(cost_2_4 < cost_2_3) cost_2_3 = cost_2_4;
cost_2_3 += smartCooldown(loc_2_3);
//best for [2, 3]
if(cost_2_3 < cost_2_2) cost_2_2 = cost_2_3;
cost_2_2 += smartCooldown(loc_2_2);
//best for [2, 2]
if(cost_2_2 < cost_2_1) cost_2_1 = cost_2_2;
cost_2_1 += smartCooldown(loc_2_1);
//best for [2, 1]
if(cost_2_1 < cost_2_0) cost_2_0 = cost_2_1;
MapLocation loc_2_n1 = rcCur.translate(2, -1);
long cost_2_n1 = Long.MAX_VALUE;
if(cost_3_n1 < cost_2_n1) cost_2_n1 = cost_3_n1;
if(cost_3_0 < cost_2_n1) cost_2_n1 = cost_3_0;
if(cost_3_n2 < cost_2_n1) cost_2_n1 = cost_3_n2;
MapLocation loc_2_n2 = rcCur.translate(2, -2);
long cost_2_n2 = Long.MAX_VALUE;
if(cost_3_n2 < cost_2_n2) cost_2_n2 = cost_3_n2;
if(cost_3_n1 < cost_2_n2) cost_2_n2 = cost_3_n1;
if(cost_3_n3 < cost_2_n2) cost_2_n2 = cost_3_n3;
MapLocation loc_2_n3 = rcCur.translate(2, -3);
long cost_2_n3 = Long.MAX_VALUE;
if(cost_3_n3 < cost_2_n3) cost_2_n3 = cost_3_n3;
if(cost_3_n2 < cost_2_n3) cost_2_n3 = cost_3_n2;
if(cost_3_n4 < cost_2_n3) cost_2_n3 = cost_3_n4;
MapLocation loc_2_n4 = rcCur.translate(2, -4);
long cost_2_n4 = Long.MAX_VALUE;
if(cost_3_n4 < cost_2_n4) cost_2_n4 = cost_3_n4;
if(cost_3_n3 < cost_2_n4) cost_2_n4 = cost_3_n3;
MapLocation loc_3_n5 = rcCur.translate(3, -5);
long cost_3_n5 = heuristicCooldown(loc_3_n5);
if(cost_3_n5 < cost_2_n4) cost_2_n4 = cost_3_n5;
MapLocation loc_2_n5 = rcCur.translate(2, -5);
long cost_2_n5 = heuristicCooldown(loc_2_n5);
if(cost_2_n5 < cost_2_n4) cost_2_n4 = cost_2_n5;
cost_2_n4 += smartCooldown(loc_2_n4);
//best for [2, -4]
if(cost_2_n4 < cost_2_n3) cost_2_n3 = cost_2_n4;
cost_2_n3 += smartCooldown(loc_2_n3);
//best for [2, -3]
if(cost_2_n3 < cost_2_n2) cost_2_n2 = cost_2_n3;
cost_2_n2 += smartCooldown(loc_2_n2);
//best for [2, -2]
if(cost_2_n2 < cost_2_n1) cost_2_n1 = cost_2_n2;
cost_2_n1 += smartCooldown(loc_2_n1);
//best for [2, -1]
if(cost_2_n1 < cost_2_0) cost_2_0 = cost_2_n1;
cost_2_0 += smartCooldown(loc_2_0);
//best for [2, 0]
if(cost_2_0 < cost_1_0) cost_1_0 = cost_2_0;
if(cost_2_1 < cost_1_0) cost_1_0 = cost_2_1;
if(cost_2_n1 < cost_1_0) cost_1_0 = cost_2_n1;
MapLocation loc_1_1 = rcCur.translate(1, 1);
long cost_1_1 = Long.MAX_VALUE;
if(cost_2_1 < cost_1_1) cost_1_1 = cost_2_1;
if(cost_2_2 < cost_1_1) cost_1_1 = cost_2_2;
if(cost_2_0 < cost_1_1) cost_1_1 = cost_2_0;
MapLocation loc_1_2 = rcCur.translate(1, 2);
long cost_1_2 = Long.MAX_VALUE;
if(cost_2_2 < cost_1_2) cost_1_2 = cost_2_2;
if(cost_2_3 < cost_1_2) cost_1_2 = cost_2_3;
if(cost_2_1 < cost_1_2) cost_1_2 = cost_2_1;
MapLocation loc_1_3 = rcCur.translate(1, 3);
long cost_1_3 = Long.MAX_VALUE;
if(cost_2_3 < cost_1_3) cost_1_3 = cost_2_3;
if(cost_2_4 < cost_1_3) cost_1_3 = cost_2_4;
if(cost_2_2 < cost_1_3) cost_1_3 = cost_2_2;
MapLocation loc_1_4 = rcCur.translate(1, 4);
long cost_1_4 = Long.MAX_VALUE;
if(cost_2_4 < cost_1_4) cost_1_4 = cost_2_4;
if(cost_2_5 < cost_1_4) cost_1_4 = cost_2_5;
if(cost_2_3 < cost_1_4) cost_1_4 = cost_2_3;
MapLocation loc_1_5 = rcCur.translate(1, 5);
long cost_1_5 = heuristicCooldown(loc_1_5);
if(cost_1_5 < cost_1_4) cost_1_4 = cost_1_5;
cost_1_4 += smartCooldown(loc_1_4);
//best for [1, 4]
if(cost_1_4 < cost_1_3) cost_1_3 = cost_1_4;
cost_1_3 += smartCooldown(loc_1_3);
//best for [1, 3]
if(cost_1_3 < cost_1_2) cost_1_2 = cost_1_3;
cost_1_2 += smartCooldown(loc_1_2);
//best for [1, 2]
if(cost_1_2 < cost_1_1) cost_1_1 = cost_1_2;
cost_1_1 += smartCooldown(loc_1_1);
//best for [1, 1]
if(cost_1_1 < cost_1_0) cost_1_0 = cost_1_1;
MapLocation loc_1_n1 = rcCur.translate(1, -1);
long cost_1_n1 = Long.MAX_VALUE;
if(cost_2_n1 < cost_1_n1) cost_1_n1 = cost_2_n1;
if(cost_2_0 < cost_1_n1) cost_1_n1 = cost_2_0;
if(cost_2_n2 < cost_1_n1) cost_1_n1 = cost_2_n2;
MapLocation loc_1_n2 = rcCur.translate(1, -2);
long cost_1_n2 = Long.MAX_VALUE;
if(cost_2_n2 < cost_1_n2) cost_1_n2 = cost_2_n2;
if(cost_2_n1 < cost_1_n2) cost_1_n2 = cost_2_n1;
if(cost_2_n3 < cost_1_n2) cost_1_n2 = cost_2_n3;
MapLocation loc_1_n3 = rcCur.translate(1, -3);
long cost_1_n3 = Long.MAX_VALUE;
if(cost_2_n3 < cost_1_n3) cost_1_n3 = cost_2_n3;
if(cost_2_n2 < cost_1_n3) cost_1_n3 = cost_2_n2;
if(cost_2_n4 < cost_1_n3) cost_1_n3 = cost_2_n4;
MapLocation loc_1_n4 = rcCur.translate(1, -4);
long cost_1_n4 = Long.MAX_VALUE;
if(cost_2_n4 < cost_1_n4) cost_1_n4 = cost_2_n4;
if(cost_2_n3 < cost_1_n4) cost_1_n4 = cost_2_n3;
if(cost_2_n5 < cost_1_n4) cost_1_n4 = cost_2_n5;
MapLocation loc_1_n5 = rcCur.translate(1, -5);
long cost_1_n5 = heuristicCooldown(loc_1_n5);
if(cost_1_n5 < cost_1_n4) cost_1_n4 = cost_1_n5;
cost_1_n4 += smartCooldown(loc_1_n4);
//best for [1, -4]
if(cost_1_n4 < cost_1_n3) cost_1_n3 = cost_1_n4;
cost_1_n3 += smartCooldown(loc_1_n3);
//best for [1, -3]
if(cost_1_n3 < cost_1_n2) cost_1_n2 = cost_1_n3;
cost_1_n2 += smartCooldown(loc_1_n2);
//best for [1, -2]
if(cost_1_n2 < cost_1_n1) cost_1_n1 = cost_1_n2;
cost_1_n1 += smartCooldown(loc_1_n1);
//best for [1, -1]
if(cost_1_n1 < cost_1_0) cost_1_0 = cost_1_n1;
cost_1_0 += smartCooldown(loc_1_0);
//best for [1, 0]
if(cost_1_0 < cost_0_0) { cost_0_0 = cost_1_0;best_dir = Direction.EAST;}
if(cost_1_1 < cost_0_0) { cost_0_0 = cost_1_1;best_dir = Direction.NORTHEAST;}
if(cost_1_n1 < cost_0_0) { cost_0_0 = cost_1_n1;best_dir = Direction.SOUTHEAST;}
MapLocation loc_0_1 = rcCur.translate(0, 1);
long cost_0_1 = Long.MAX_VALUE;
if(cost_1_1 < cost_0_1) cost_0_1 = cost_1_1;
if(cost_1_2 < cost_0_1) cost_0_1 = cost_1_2;
if(cost_1_0 < cost_0_1) cost_0_1 = cost_1_0;
MapLocation loc_0_2 = rcCur.translate(0, 2);
long cost_0_2 = Long.MAX_VALUE;
if(cost_1_2 < cost_0_2) cost_0_2 = cost_1_2;
if(cost_1_3 < cost_0_2) cost_0_2 = cost_1_3;
if(cost_1_1 < cost_0_2) cost_0_2 = cost_1_1;
MapLocation loc_0_3 = rcCur.translate(0, 3);
long cost_0_3 = Long.MAX_VALUE;
if(cost_1_3 < cost_0_3) cost_0_3 = cost_1_3;
if(cost_1_4 < cost_0_3) cost_0_3 = cost_1_4;
if(cost_1_2 < cost_0_3) cost_0_3 = cost_1_2;
MapLocation loc_0_4 = rcCur.translate(0, 4);
long cost_0_4 = Long.MAX_VALUE;
if(cost_1_4 < cost_0_4) cost_0_4 = cost_1_4;
if(cost_1_5 < cost_0_4) cost_0_4 = cost_1_5;
if(cost_1_3 < cost_0_4) cost_0_4 = cost_1_3;
MapLocation loc_0_5 = rcCur.translate(0, 5);
long cost_0_5 = heuristicCooldown(loc_0_5);
if(cost_0_5 < cost_0_4) cost_0_4 = cost_0_5;
cost_0_4 += smartCooldown(loc_0_4);
//best for [0, 4]
if(cost_0_4 < cost_0_3) cost_0_3 = cost_0_4;
cost_0_3 += smartCooldown(loc_0_3);
//best for [0, 3]
if(cost_0_3 < cost_0_2) cost_0_2 = cost_0_3;
cost_0_2 += smartCooldown(loc_0_2);
//best for [0, 2]
if(cost_0_2 < cost_0_1) cost_0_1 = cost_0_2;
cost_0_1 += smartCooldown(loc_0_1);
//best for [0, 1]
if(cost_0_1 < cost_0_0) { cost_0_0 = cost_0_1;best_dir = Direction.NORTH;}
MapLocation loc_0_n1 = rcCur.translate(0, -1);
long cost_0_n1 = Long.MAX_VALUE;
if(cost_1_n1 < cost_0_n1) cost_0_n1 = cost_1_n1;
if(cost_1_0 < cost_0_n1) cost_0_n1 = cost_1_0;
if(cost_1_n2 < cost_0_n1) cost_0_n1 = cost_1_n2;
MapLocation loc_0_n2 = rcCur.translate(0, -2);
long cost_0_n2 = Long.MAX_VALUE;
if(cost_1_n2 < cost_0_n2) cost_0_n2 = cost_1_n2;
if(cost_1_n1 < cost_0_n2) cost_0_n2 = cost_1_n1;
if(cost_1_n3 < cost_0_n2) cost_0_n2 = cost_1_n3;
MapLocation loc_0_n3 = rcCur.translate(0, -3);
long cost_0_n3 = Long.MAX_VALUE;
if(cost_1_n3 < cost_0_n3) cost_0_n3 = cost_1_n3;
if(cost_1_n2 < cost_0_n3) cost_0_n3 = cost_1_n2;
if(cost_1_n4 < cost_0_n3) cost_0_n3 = cost_1_n4;
MapLocation loc_0_n4 = rcCur.translate(0, -4);
long cost_0_n4 = Long.MAX_VALUE;
if(cost_1_n4 < cost_0_n4) cost_0_n4 = cost_1_n4;
if(cost_1_n3 < cost_0_n4) cost_0_n4 = cost_1_n3;
if(cost_1_n5 < cost_0_n4) cost_0_n4 = cost_1_n5;
MapLocation loc_0_n5 = rcCur.translate(0, -5);
long cost_0_n5 = heuristicCooldown(loc_0_n5);
if(cost_0_n5 < cost_0_n4) cost_0_n4 = cost_0_n5;
cost_0_n4 += smartCooldown(loc_0_n4);
//best for [0, -4]
if(cost_0_n4 < cost_0_n3) cost_0_n3 = cost_0_n4;
cost_0_n3 += smartCooldown(loc_0_n3);
//best for [0, -3]
if(cost_0_n3 < cost_0_n2) cost_0_n2 = cost_0_n3;
cost_0_n2 += smartCooldown(loc_0_n2);
//best for [0, -2]
if(cost_0_n2 < cost_0_n1) cost_0_n1 = cost_0_n2;
cost_0_n1 += smartCooldown(loc_0_n1);
//best for [0, -1]
if(cost_0_n1 < cost_0_0) { cost_0_0 = cost_0_n1;best_dir = Direction.SOUTH;}
cost_0_0 += smartCooldown(loc_0_0);
//best for [0, 0]
return best_dir;
}
static Direction pathfind_SOUTHEAST() throws GameActionException {
MapLocation loc_0_0 = rcCur.translate(0, 0);
long cost_0_0 = Long.MAX_VALUE;
Direction best_dir = null;
MapLocation loc_1_n1 = rcCur.translate(1, -1);
long cost_1_n1 = Long.MAX_VALUE;
MapLocation loc_2_n2 = rcCur.translate(2, -2);
long cost_2_n2 = Long.MAX_VALUE;
MapLocation loc_3_n3 = rcCur.translate(3, -3);
long cost_3_n3 = Long.MAX_VALUE;
MapLocation loc_4_n4 = rcCur.translate(4, -4);
long cost_4_n4 = heuristicCooldown(loc_4_n4);
if(cost_4_n4 < cost_3_n3) cost_3_n3 = cost_4_n4;
MapLocation loc_4_n3 = rcCur.translate(4, -3);
long cost_4_n3 = heuristicCooldown(loc_4_n3);
if(cost_4_n3 < cost_3_n3) cost_3_n3 = cost_4_n3;
MapLocation loc_3_n4 = rcCur.translate(3, -4);
long cost_3_n4 = heuristicCooldown(loc_3_n4);
if(cost_3_n4 < cost_3_n3) cost_3_n3 = cost_3_n4;
MapLocation loc_4_n2 = rcCur.translate(4, -2);
long cost_4_n2 = Long.MAX_VALUE;
MapLocation loc_5_n3 = rcCur.translate(5, -3);
long cost_5_n3 = heuristicCooldown(loc_5_n3);
if(cost_5_n3 < cost_4_n2) cost_4_n2 = cost_5_n3;
MapLocation loc_5_n2 = rcCur.translate(5, -2);
long cost_5_n2 = heuristicCooldown(loc_5_n2);
if(cost_5_n2 < cost_4_n2) cost_4_n2 = cost_5_n2;
if(cost_4_n3 < cost_4_n2) cost_4_n2 = cost_4_n3;
MapLocation loc_5_n1 = rcCur.translate(5, -1);
long cost_5_n1 = heuristicCooldown(loc_5_n1);
if(cost_5_n1 < cost_4_n2) cost_4_n2 = cost_5_n1;
cost_4_n2 += smartCooldown(loc_4_n2);
//best for [4, -2]
if(cost_4_n2 < cost_3_n3) cost_3_n3 = cost_4_n2;
MapLocation loc_2_n4 = rcCur.translate(2, -4);
long cost_2_n4 = Long.MAX_VALUE;
MapLocation loc_3_n5 = rcCur.translate(3, -5);
long cost_3_n5 = heuristicCooldown(loc_3_n5);
if(cost_3_n5 < cost_2_n4) cost_2_n4 = cost_3_n5;
if(cost_3_n4 < cost_2_n4) cost_2_n4 = cost_3_n4;
MapLocation loc_2_n5 = rcCur.translate(2, -5);
long cost_2_n5 = heuristicCooldown(loc_2_n5);
if(cost_2_n5 < cost_2_n4) cost_2_n4 = cost_2_n5;
MapLocation loc_1_n5 = rcCur.translate(1, -5);
long cost_1_n5 = heuristicCooldown(loc_1_n5);
if(cost_1_n5 < cost_2_n4) cost_2_n4 = cost_1_n5;
cost_2_n4 += smartCooldown(loc_2_n4);
//best for [2, -4]
if(cost_2_n4 < cost_3_n3) cost_3_n3 = cost_2_n4;
cost_3_n3 += smartCooldown(loc_3_n3);
//best for [3, -3]
if(cost_3_n3 < cost_2_n2) cost_2_n2 = cost_3_n3;
MapLocation loc_3_n2 = rcCur.translate(3, -2);
long cost_3_n2 = Long.MAX_VALUE;
if(cost_4_n3 < cost_3_n2) cost_3_n2 = cost_4_n3;
if(cost_4_n2 < cost_3_n2) cost_3_n2 = cost_4_n2;
if(cost_3_n3 < cost_3_n2) cost_3_n2 = cost_3_n3;
MapLocation loc_4_n1 = rcCur.translate(4, -1);
long cost_4_n1 = Long.MAX_VALUE;
if(cost_5_n2 < cost_4_n1) cost_4_n1 = cost_5_n2;
if(cost_5_n1 < cost_4_n1) cost_4_n1 = cost_5_n1;
if(cost_4_n2 < cost_4_n1) cost_4_n1 = cost_4_n2;
MapLocation loc_5_0 = rcCur.translate(5, 0);
long cost_5_0 = heuristicCooldown(loc_5_0);
if(cost_5_0 < cost_4_n1) cost_4_n1 = cost_5_0;
cost_4_n1 += smartCooldown(loc_4_n1);
//best for [4, -1]
if(cost_4_n1 < cost_3_n2) cost_3_n2 = cost_4_n1;
MapLocation loc_2_n3 = rcCur.translate(2, -3);
long cost_2_n3 = Long.MAX_VALUE;
if(cost_3_n4 < cost_2_n3) cost_2_n3 = cost_3_n4;
if(cost_3_n3 < cost_2_n3) cost_2_n3 = cost_3_n3;
if(cost_2_n4 < cost_2_n3) cost_2_n3 = cost_2_n4;
MapLocation loc_1_n4 = rcCur.translate(1, -4);
long cost_1_n4 = Long.MAX_VALUE;
if(cost_2_n5 < cost_1_n4) cost_1_n4 = cost_2_n5;
if(cost_2_n4 < cost_1_n4) cost_1_n4 = cost_2_n4;
if(cost_1_n5 < cost_1_n4) cost_1_n4 = cost_1_n5;
MapLocation loc_0_n5 = rcCur.translate(0, -5);
long cost_0_n5 = heuristicCooldown(loc_0_n5);
if(cost_0_n5 < cost_1_n4) cost_1_n4 = cost_0_n5;
cost_1_n4 += smartCooldown(loc_1_n4);
//best for [1, -4]
if(cost_1_n4 < cost_2_n3) cost_2_n3 = cost_1_n4;
cost_2_n3 += smartCooldown(loc_2_n3);
//best for [2, -3]
if(cost_2_n3 < cost_3_n2) cost_3_n2 = cost_2_n3;
cost_3_n2 += smartCooldown(loc_3_n2);
//best for [3, -2]
if(cost_3_n2 < cost_2_n2) cost_2_n2 = cost_3_n2;
if(cost_2_n3 < cost_2_n2) cost_2_n2 = cost_2_n3;
MapLocation loc_3_n1 = rcCur.translate(3, -1);
long cost_3_n1 = Long.MAX_VALUE;
if(cost_4_n2 < cost_3_n1) cost_3_n1 = cost_4_n2;
if(cost_4_n1 < cost_3_n1) cost_3_n1 = cost_4_n1;
if(cost_3_n2 < cost_3_n1) cost_3_n1 = cost_3_n2;
MapLocation loc_4_0 = rcCur.translate(4, 0);
long cost_4_0 = Long.MAX_VALUE;
if(cost_5_n1 < cost_4_0) cost_4_0 = cost_5_n1;
if(cost_5_0 < cost_4_0) cost_4_0 = cost_5_0;
if(cost_4_n1 < cost_4_0) cost_4_0 = cost_4_n1;
MapLocation loc_5_1 = rcCur.translate(5, 1);
long cost_5_1 = heuristicCooldown(loc_5_1);
if(cost_5_1 < cost_4_0) cost_4_0 = cost_5_1;
cost_4_0 += smartCooldown(loc_4_0);
//best for [4, 0]
if(cost_4_0 < cost_3_n1) cost_3_n1 = cost_4_0;
cost_3_n1 += smartCooldown(loc_3_n1);
//best for [3, -1]
if(cost_3_n1 < cost_2_n2) cost_2_n2 = cost_3_n1;
MapLocation loc_1_n3 = rcCur.translate(1, -3);
long cost_1_n3 = Long.MAX_VALUE;
if(cost_2_n4 < cost_1_n3) cost_1_n3 = cost_2_n4;
if(cost_2_n3 < cost_1_n3) cost_1_n3 = cost_2_n3;
if(cost_1_n4 < cost_1_n3) cost_1_n3 = cost_1_n4;
MapLocation loc_0_n4 = rcCur.translate(0, -4);
long cost_0_n4 = Long.MAX_VALUE;
if(cost_1_n5 < cost_0_n4) cost_0_n4 = cost_1_n5;
if(cost_1_n4 < cost_0_n4) cost_0_n4 = cost_1_n4;
if(cost_0_n5 < cost_0_n4) cost_0_n4 = cost_0_n5;
MapLocation loc_n1_n5 = rcCur.translate(-1, -5);
long cost_n1_n5 = heuristicCooldown(loc_n1_n5);
if(cost_n1_n5 < cost_0_n4) cost_0_n4 = cost_n1_n5;
cost_0_n4 += smartCooldown(loc_0_n4);
//best for [0, -4]
if(cost_0_n4 < cost_1_n3) cost_1_n3 = cost_0_n4;
cost_1_n3 += smartCooldown(loc_1_n3);
//best for [1, -3]
if(cost_1_n3 < cost_2_n2) cost_2_n2 = cost_1_n3;
cost_2_n2 += smartCooldown(loc_2_n2);
//best for [2, -2]
if(cost_2_n2 < cost_1_n1) cost_1_n1 = cost_2_n2;
MapLocation loc_2_n1 = rcCur.translate(2, -1);
long cost_2_n1 = Long.MAX_VALUE;
if(cost_3_n2 < cost_2_n1) cost_2_n1 = cost_3_n2;
if(cost_3_n1 < cost_2_n1) cost_2_n1 = cost_3_n1;
if(cost_2_n2 < cost_2_n1) cost_2_n1 = cost_2_n2;
MapLocation loc_3_0 = rcCur.translate(3, 0);
long cost_3_0 = Long.MAX_VALUE;
if(cost_4_n1 < cost_3_0) cost_3_0 = cost_4_n1;
if(cost_4_0 < cost_3_0) cost_3_0 = cost_4_0;
if(cost_3_n1 < cost_3_0) cost_3_0 = cost_3_n1;
MapLocation loc_4_1 = rcCur.translate(4, 1);
long cost_4_1 = Long.MAX_VALUE;
if(cost_5_0 < cost_4_1) cost_4_1 = cost_5_0;
if(cost_5_1 < cost_4_1) cost_4_1 = cost_5_1;
if(cost_4_0 < cost_4_1) cost_4_1 = cost_4_0;
MapLocation loc_5_2 = rcCur.translate(5, 2);
long cost_5_2 = heuristicCooldown(loc_5_2);
if(cost_5_2 < cost_4_1) cost_4_1 = cost_5_2;
cost_4_1 += smartCooldown(loc_4_1);
//best for [4, 1]
if(cost_4_1 < cost_3_0) cost_3_0 = cost_4_1;
cost_3_0 += smartCooldown(loc_3_0);
//best for [3, 0]
if(cost_3_0 < cost_2_n1) cost_2_n1 = cost_3_0;
MapLocation loc_1_n2 = rcCur.translate(1, -2);
long cost_1_n2 = Long.MAX_VALUE;
if(cost_2_n3 < cost_1_n2) cost_1_n2 = cost_2_n3;
if(cost_2_n2 < cost_1_n2) cost_1_n2 = cost_2_n2;
if(cost_1_n3 < cost_1_n2) cost_1_n2 = cost_1_n3;
MapLocation loc_0_n3 = rcCur.translate(0, -3);
long cost_0_n3 = Long.MAX_VALUE;
if(cost_1_n4 < cost_0_n3) cost_0_n3 = cost_1_n4;
if(cost_1_n3 < cost_0_n3) cost_0_n3 = cost_1_n3;
if(cost_0_n4 < cost_0_n3) cost_0_n3 = cost_0_n4;
MapLocation loc_n1_n4 = rcCur.translate(-1, -4);
long cost_n1_n4 = Long.MAX_VALUE;
if(cost_0_n5 < cost_n1_n4) cost_n1_n4 = cost_0_n5;
if(cost_0_n4 < cost_n1_n4) cost_n1_n4 = cost_0_n4;
if(cost_n1_n5 < cost_n1_n4) cost_n1_n4 = cost_n1_n5;
MapLocation loc_n2_n5 = rcCur.translate(-2, -5);
long cost_n2_n5 = heuristicCooldown(loc_n2_n5);
if(cost_n2_n5 < cost_n1_n4) cost_n1_n4 = cost_n2_n5;
cost_n1_n4 += smartCooldown(loc_n1_n4);
//best for [-1, -4]
if(cost_n1_n4 < cost_0_n3) cost_0_n3 = cost_n1_n4;
cost_0_n3 += smartCooldown(loc_0_n3);
//best for [0, -3]
if(cost_0_n3 < cost_1_n2) cost_1_n2 = cost_0_n3;
cost_1_n2 += smartCooldown(loc_1_n2);
//best for [1, -2]
if(cost_1_n2 < cost_2_n1) cost_2_n1 = cost_1_n2;
cost_2_n1 += smartCooldown(loc_2_n1);
//best for [2, -1]
if(cost_2_n1 < cost_1_n1) cost_1_n1 = cost_2_n1;
if(cost_1_n2 < cost_1_n1) cost_1_n1 = cost_1_n2;
MapLocation loc_2_0 = rcCur.translate(2, 0);
long cost_2_0 = Long.MAX_VALUE;
if(cost_3_n1 < cost_2_0) cost_2_0 = cost_3_n1;
if(cost_3_0 < cost_2_0) cost_2_0 = cost_3_0;
if(cost_2_n1 < cost_2_0) cost_2_0 = cost_2_n1;
MapLocation loc_3_1 = rcCur.translate(3, 1);
long cost_3_1 = Long.MAX_VALUE;
if(cost_4_0 < cost_3_1) cost_3_1 = cost_4_0;
if(cost_4_1 < cost_3_1) cost_3_1 = cost_4_1;
if(cost_3_0 < cost_3_1) cost_3_1 = cost_3_0;
MapLocation loc_4_2 = rcCur.translate(4, 2);
long cost_4_2 = Long.MAX_VALUE;
if(cost_5_1 < cost_4_2) cost_4_2 = cost_5_1;
if(cost_5_2 < cost_4_2) cost_4_2 = cost_5_2;
if(cost_4_1 < cost_4_2) cost_4_2 = cost_4_1;
MapLocation loc_5_3 = rcCur.translate(5, 3);
long cost_5_3 = heuristicCooldown(loc_5_3);
if(cost_5_3 < cost_4_2) cost_4_2 = cost_5_3;
cost_4_2 += smartCooldown(loc_4_2);
//best for [4, 2]
if(cost_4_2 < cost_3_1) cost_3_1 = cost_4_2;
cost_3_1 += smartCooldown(loc_3_1);
//best for [3, 1]
if(cost_3_1 < cost_2_0) cost_2_0 = cost_3_1;
cost_2_0 += smartCooldown(loc_2_0);
//best for [2, 0]
if(cost_2_0 < cost_1_n1) cost_1_n1 = cost_2_0;
MapLocation loc_0_n2 = rcCur.translate(0, -2);
long cost_0_n2 = Long.MAX_VALUE;
if(cost_1_n3 < cost_0_n2) cost_0_n2 = cost_1_n3;
if(cost_1_n2 < cost_0_n2) cost_0_n2 = cost_1_n2;
if(cost_0_n3 < cost_0_n2) cost_0_n2 = cost_0_n3;
MapLocation loc_n1_n3 = rcCur.translate(-1, -3);
long cost_n1_n3 = Long.MAX_VALUE;
if(cost_0_n4 < cost_n1_n3) cost_n1_n3 = cost_0_n4;
if(cost_0_n3 < cost_n1_n3) cost_n1_n3 = cost_0_n3;
if(cost_n1_n4 < cost_n1_n3) cost_n1_n3 = cost_n1_n4;
MapLocation loc_n2_n4 = rcCur.translate(-2, -4);
long cost_n2_n4 = Long.MAX_VALUE;
if(cost_n1_n5 < cost_n2_n4) cost_n2_n4 = cost_n1_n5;
if(cost_n1_n4 < cost_n2_n4) cost_n2_n4 = cost_n1_n4;
if(cost_n2_n5 < cost_n2_n4) cost_n2_n4 = cost_n2_n5;
MapLocation loc_n3_n5 = rcCur.translate(-3, -5);
long cost_n3_n5 = heuristicCooldown(loc_n3_n5);
if(cost_n3_n5 < cost_n2_n4) cost_n2_n4 = cost_n3_n5;
cost_n2_n4 += smartCooldown(loc_n2_n4);
//best for [-2, -4]
if(cost_n2_n4 < cost_n1_n3) cost_n1_n3 = cost_n2_n4;
cost_n1_n3 += smartCooldown(loc_n1_n3);
//best for [-1, -3]
if(cost_n1_n3 < cost_0_n2) cost_0_n2 = cost_n1_n3;
cost_0_n2 += smartCooldown(loc_0_n2);
//best for [0, -2]
if(cost_0_n2 < cost_1_n1) cost_1_n1 = cost_0_n2;
cost_1_n1 += smartCooldown(loc_1_n1);
//best for [1, -1]
if(cost_1_n1 < cost_0_0) { cost_0_0 = cost_1_n1;best_dir = Direction.SOUTHEAST;}
MapLocation loc_1_0 = rcCur.translate(1, 0);
long cost_1_0 = Long.MAX_VALUE;
if(cost_2_n1 < cost_1_0) cost_1_0 = cost_2_n1;
if(cost_2_0 < cost_1_0) cost_1_0 = cost_2_0;
if(cost_1_n1 < cost_1_0) cost_1_0 = cost_1_n1;
MapLocation loc_2_1 = rcCur.translate(2, 1);
long cost_2_1 = Long.MAX_VALUE;
if(cost_3_0 < cost_2_1) cost_2_1 = cost_3_0;
if(cost_3_1 < cost_2_1) cost_2_1 = cost_3_1;
if(cost_2_0 < cost_2_1) cost_2_1 = cost_2_0;
MapLocation loc_3_2 = rcCur.translate(3, 2);
long cost_3_2 = Long.MAX_VALUE;
if(cost_4_1 < cost_3_2) cost_3_2 = cost_4_1;
if(cost_4_2 < cost_3_2) cost_3_2 = cost_4_2;
if(cost_3_1 < cost_3_2) cost_3_2 = cost_3_1;
MapLocation loc_4_3 = rcCur.translate(4, 3);
long cost_4_3 = heuristicCooldown(loc_4_3);
if(cost_4_3 < cost_3_2) cost_3_2 = cost_4_3;
cost_3_2 += smartCooldown(loc_3_2);
//best for [3, 2]
if(cost_3_2 < cost_2_1) cost_2_1 = cost_3_2;
cost_2_1 += smartCooldown(loc_2_1);
//best for [2, 1]
if(cost_2_1 < cost_1_0) cost_1_0 = cost_2_1;
MapLocation loc_0_n1 = rcCur.translate(0, -1);
long cost_0_n1 = Long.MAX_VALUE;
if(cost_1_n2 < cost_0_n1) cost_0_n1 = cost_1_n2;
if(cost_1_n1 < cost_0_n1) cost_0_n1 = cost_1_n1;
if(cost_0_n2 < cost_0_n1) cost_0_n1 = cost_0_n2;
MapLocation loc_n1_n2 = rcCur.translate(-1, -2);
long cost_n1_n2 = Long.MAX_VALUE;
if(cost_0_n3 < cost_n1_n2) cost_n1_n2 = cost_0_n3;
if(cost_0_n2 < cost_n1_n2) cost_n1_n2 = cost_0_n2;
if(cost_n1_n3 < cost_n1_n2) cost_n1_n2 = cost_n1_n3;
MapLocation loc_n2_n3 = rcCur.translate(-2, -3);
long cost_n2_n3 = Long.MAX_VALUE;
if(cost_n1_n4 < cost_n2_n3) cost_n2_n3 = cost_n1_n4;
if(cost_n1_n3 < cost_n2_n3) cost_n2_n3 = cost_n1_n3;
if(cost_n2_n4 < cost_n2_n3) cost_n2_n3 = cost_n2_n4;
MapLocation loc_n3_n4 = rcCur.translate(-3, -4);
long cost_n3_n4 = heuristicCooldown(loc_n3_n4);
if(cost_n3_n4 < cost_n2_n3) cost_n2_n3 = cost_n3_n4;
cost_n2_n3 += smartCooldown(loc_n2_n3);
//best for [-2, -3]
if(cost_n2_n3 < cost_n1_n2) cost_n1_n2 = cost_n2_n3;
cost_n1_n2 += smartCooldown(loc_n1_n2);
//best for [-1, -2]
if(cost_n1_n2 < cost_0_n1) cost_0_n1 = cost_n1_n2;
cost_0_n1 += smartCooldown(loc_0_n1);
//best for [0, -1]
if(cost_0_n1 < cost_1_0) cost_1_0 = cost_0_n1;
cost_1_0 += smartCooldown(loc_1_0);
//best for [1, 0]
if(cost_1_0 < cost_0_0) { cost_0_0 = cost_1_0;best_dir = Direction.EAST;}
if(cost_0_n1 < cost_0_0) { cost_0_0 = cost_0_n1;best_dir = Direction.SOUTH;}
MapLocation loc_1_1 = rcCur.translate(1, 1);
long cost_1_1 = Long.MAX_VALUE;
if(cost_2_0 < cost_1_1) cost_1_1 = cost_2_0;
if(cost_2_1 < cost_1_1) cost_1_1 = cost_2_1;
if(cost_1_0 < cost_1_1) cost_1_1 = cost_1_0;
MapLocation loc_2_2 = rcCur.translate(2, 2);
long cost_2_2 = Long.MAX_VALUE;
if(cost_3_1 < cost_2_2) cost_2_2 = cost_3_1;
if(cost_3_2 < cost_2_2) cost_2_2 = cost_3_2;
if(cost_2_1 < cost_2_2) cost_2_2 = cost_2_1;
MapLocation loc_3_3 = rcCur.translate(3, 3);
long cost_3_3 = Long.MAX_VALUE;
if(cost_4_2 < cost_3_3) cost_3_3 = cost_4_2;
if(cost_4_3 < cost_3_3) cost_3_3 = cost_4_3;
if(cost_3_2 < cost_3_3) cost_3_3 = cost_3_2;
MapLocation loc_4_4 = rcCur.translate(4, 4);
long cost_4_4 = heuristicCooldown(loc_4_4);
if(cost_4_4 < cost_3_3) cost_3_3 = cost_4_4;
cost_3_3 += smartCooldown(loc_3_3);
//best for [3, 3]
if(cost_3_3 < cost_2_2) cost_2_2 = cost_3_3;
cost_2_2 += smartCooldown(loc_2_2);
//best for [2, 2]
if(cost_2_2 < cost_1_1) cost_1_1 = cost_2_2;
cost_1_1 += smartCooldown(loc_1_1);
//best for [1, 1]
if(cost_1_1 < cost_0_0) { cost_0_0 = cost_1_1;best_dir = Direction.NORTHEAST;}
MapLocation loc_n1_n1 = rcCur.translate(-1, -1);
long cost_n1_n1 = Long.MAX_VALUE;
if(cost_0_n2 < cost_n1_n1) cost_n1_n1 = cost_0_n2;
if(cost_0_n1 < cost_n1_n1) cost_n1_n1 = cost_0_n1;
if(cost_n1_n2 < cost_n1_n1) cost_n1_n1 = cost_n1_n2;
MapLocation loc_n2_n2 = rcCur.translate(-2, -2);
long cost_n2_n2 = Long.MAX_VALUE;
if(cost_n1_n3 < cost_n2_n2) cost_n2_n2 = cost_n1_n3;
if(cost_n1_n2 < cost_n2_n2) cost_n2_n2 = cost_n1_n2;
if(cost_n2_n3 < cost_n2_n2) cost_n2_n2 = cost_n2_n3;
MapLocation loc_n3_n3 = rcCur.translate(-3, -3);
long cost_n3_n3 = Long.MAX_VALUE;
if(cost_n2_n4 < cost_n3_n3) cost_n3_n3 = cost_n2_n4;
if(cost_n2_n3 < cost_n3_n3) cost_n3_n3 = cost_n2_n3;
if(cost_n3_n4 < cost_n3_n3) cost_n3_n3 = cost_n3_n4;
MapLocation loc_n4_n4 = rcCur.translate(-4, -4);
long cost_n4_n4 = heuristicCooldown(loc_n4_n4);
if(cost_n4_n4 < cost_n3_n3) cost_n3_n3 = cost_n4_n4;
cost_n3_n3 += smartCooldown(loc_n3_n3);
//best for [-3, -3]
if(cost_n3_n3 < cost_n2_n2) cost_n2_n2 = cost_n3_n3;
cost_n2_n2 += smartCooldown(loc_n2_n2);
//best for [-2, -2]
if(cost_n2_n2 < cost_n1_n1) cost_n1_n1 = cost_n2_n2;
cost_n1_n1 += smartCooldown(loc_n1_n1);
//best for [-1, -1]
if(cost_n1_n1 < cost_0_0) { cost_0_0 = cost_n1_n1;best_dir = Direction.SOUTHWEST;}
cost_0_0 += smartCooldown(loc_0_0);
//best for [0, 0]
return best_dir;
}
static Direction pathfind_SOUTH() throws GameActionException {
MapLocation loc_0_0 = rcCur.translate(0, 0);
long cost_0_0 = Long.MAX_VALUE;
Direction best_dir = null;
MapLocation loc_0_n1 = rcCur.translate(0, -1);
long cost_0_n1 = Long.MAX_VALUE;
MapLocation loc_0_n2 = rcCur.translate(0, -2);
long cost_0_n2 = Long.MAX_VALUE;
MapLocation loc_0_n3 = rcCur.translate(0, -3);
long cost_0_n3 = Long.MAX_VALUE;
MapLocation loc_0_n4 = rcCur.translate(0, -4);
long cost_0_n4 = Long.MAX_VALUE;
MapLocation loc_0_n5 = rcCur.translate(0, -5);
long cost_0_n5 = heuristicCooldown(loc_0_n5);
if(cost_0_n5 < cost_0_n4) cost_0_n4 = cost_0_n5;
MapLocation loc_1_n5 = rcCur.translate(1, -5);
long cost_1_n5 = heuristicCooldown(loc_1_n5);
if(cost_1_n5 < cost_0_n4) cost_0_n4 = cost_1_n5;
MapLocation loc_n1_n5 = rcCur.translate(-1, -5);
long cost_n1_n5 = heuristicCooldown(loc_n1_n5);
if(cost_n1_n5 < cost_0_n4) cost_0_n4 = cost_n1_n5;
MapLocation loc_1_n4 = rcCur.translate(1, -4);
long cost_1_n4 = Long.MAX_VALUE;
if(cost_1_n5 < cost_1_n4) cost_1_n4 = cost_1_n5;
MapLocation loc_2_n5 = rcCur.translate(2, -5);
long cost_2_n5 = heuristicCooldown(loc_2_n5);
if(cost_2_n5 < cost_1_n4) cost_1_n4 = cost_2_n5;
if(cost_0_n5 < cost_1_n4) cost_1_n4 = cost_0_n5;
MapLocation loc_2_n4 = rcCur.translate(2, -4);
long cost_2_n4 = Long.MAX_VALUE;
if(cost_2_n5 < cost_2_n4) cost_2_n4 = cost_2_n5;
MapLocation loc_3_n5 = rcCur.translate(3, -5);
long cost_3_n5 = heuristicCooldown(loc_3_n5);
if(cost_3_n5 < cost_2_n4) cost_2_n4 = cost_3_n5;
if(cost_1_n5 < cost_2_n4) cost_2_n4 = cost_1_n5;
MapLocation loc_3_n4 = rcCur.translate(3, -4);
long cost_3_n4 = heuristicCooldown(loc_3_n4);
if(cost_3_n4 < cost_2_n4) cost_2_n4 = cost_3_n4;
cost_2_n4 += smartCooldown(loc_2_n4);
//best for [2, -4]
if(cost_2_n4 < cost_1_n4) cost_1_n4 = cost_2_n4;
cost_1_n4 += smartCooldown(loc_1_n4);
//best for [1, -4]
if(cost_1_n4 < cost_0_n4) cost_0_n4 = cost_1_n4;
MapLocation loc_n1_n4 = rcCur.translate(-1, -4);
long cost_n1_n4 = Long.MAX_VALUE;
if(cost_n1_n5 < cost_n1_n4) cost_n1_n4 = cost_n1_n5;
if(cost_0_n5 < cost_n1_n4) cost_n1_n4 = cost_0_n5;
MapLocation loc_n2_n5 = rcCur.translate(-2, -5);
long cost_n2_n5 = heuristicCooldown(loc_n2_n5);
if(cost_n2_n5 < cost_n1_n4) cost_n1_n4 = cost_n2_n5;
MapLocation loc_n2_n4 = rcCur.translate(-2, -4);
long cost_n2_n4 = Long.MAX_VALUE;
if(cost_n2_n5 < cost_n2_n4) cost_n2_n4 = cost_n2_n5;
if(cost_n1_n5 < cost_n2_n4) cost_n2_n4 = cost_n1_n5;
MapLocation loc_n3_n5 = rcCur.translate(-3, -5);
long cost_n3_n5 = heuristicCooldown(loc_n3_n5);
if(cost_n3_n5 < cost_n2_n4) cost_n2_n4 = cost_n3_n5;
MapLocation loc_n3_n4 = rcCur.translate(-3, -4);
long cost_n3_n4 = heuristicCooldown(loc_n3_n4);
if(cost_n3_n4 < cost_n2_n4) cost_n2_n4 = cost_n3_n4;
cost_n2_n4 += smartCooldown(loc_n2_n4);
//best for [-2, -4]
if(cost_n2_n4 < cost_n1_n4) cost_n1_n4 = cost_n2_n4;
cost_n1_n4 += smartCooldown(loc_n1_n4);
//best for [-1, -4]
if(cost_n1_n4 < cost_0_n4) cost_0_n4 = cost_n1_n4;
cost_0_n4 += smartCooldown(loc_0_n4);
//best for [0, -4]
if(cost_0_n4 < cost_0_n3) cost_0_n3 = cost_0_n4;
if(cost_1_n4 < cost_0_n3) cost_0_n3 = cost_1_n4;
if(cost_n1_n4 < cost_0_n3) cost_0_n3 = cost_n1_n4;
MapLocation loc_1_n3 = rcCur.translate(1, -3);
long cost_1_n3 = Long.MAX_VALUE;
if(cost_1_n4 < cost_1_n3) cost_1_n3 = cost_1_n4;
if(cost_2_n4 < cost_1_n3) cost_1_n3 = cost_2_n4;
if(cost_0_n4 < cost_1_n3) cost_1_n3 = cost_0_n4;
MapLocation loc_2_n3 = rcCur.translate(2, -3);
long cost_2_n3 = Long.MAX_VALUE;
if(cost_2_n4 < cost_2_n3) cost_2_n3 = cost_2_n4;
if(cost_3_n4 < cost_2_n3) cost_2_n3 = cost_3_n4;
if(cost_1_n4 < cost_2_n3) cost_2_n3 = cost_1_n4;
MapLocation loc_3_n3 = rcCur.translate(3, -3);
long cost_3_n3 = Long.MAX_VALUE;
if(cost_3_n4 < cost_3_n3) cost_3_n3 = cost_3_n4;
MapLocation loc_4_n4 = rcCur.translate(4, -4);
long cost_4_n4 = heuristicCooldown(loc_4_n4);
if(cost_4_n4 < cost_3_n3) cost_3_n3 = cost_4_n4;
if(cost_2_n4 < cost_3_n3) cost_3_n3 = cost_2_n4;
MapLocation loc_4_n3 = rcCur.translate(4, -3);
long cost_4_n3 = heuristicCooldown(loc_4_n3);
if(cost_4_n3 < cost_3_n3) cost_3_n3 = cost_4_n3;
cost_3_n3 += smartCooldown(loc_3_n3);
//best for [3, -3]
if(cost_3_n3 < cost_2_n3) cost_2_n3 = cost_3_n3;
cost_2_n3 += smartCooldown(loc_2_n3);
//best for [2, -3]
if(cost_2_n3 < cost_1_n3) cost_1_n3 = cost_2_n3;
cost_1_n3 += smartCooldown(loc_1_n3);
//best for [1, -3]
if(cost_1_n3 < cost_0_n3) cost_0_n3 = cost_1_n3;
MapLocation loc_n1_n3 = rcCur.translate(-1, -3);
long cost_n1_n3 = Long.MAX_VALUE;
if(cost_n1_n4 < cost_n1_n3) cost_n1_n3 = cost_n1_n4;
if(cost_0_n4 < cost_n1_n3) cost_n1_n3 = cost_0_n4;
if(cost_n2_n4 < cost_n1_n3) cost_n1_n3 = cost_n2_n4;
MapLocation loc_n2_n3 = rcCur.translate(-2, -3);
long cost_n2_n3 = Long.MAX_VALUE;
if(cost_n2_n4 < cost_n2_n3) cost_n2_n3 = cost_n2_n4;
if(cost_n1_n4 < cost_n2_n3) cost_n2_n3 = cost_n1_n4;
if(cost_n3_n4 < cost_n2_n3) cost_n2_n3 = cost_n3_n4;
MapLocation loc_n3_n3 = rcCur.translate(-3, -3);
long cost_n3_n3 = Long.MAX_VALUE;
if(cost_n3_n4 < cost_n3_n3) cost_n3_n3 = cost_n3_n4;
if(cost_n2_n4 < cost_n3_n3) cost_n3_n3 = cost_n2_n4;
MapLocation loc_n4_n4 = rcCur.translate(-4, -4);
long cost_n4_n4 = heuristicCooldown(loc_n4_n4);
if(cost_n4_n4 < cost_n3_n3) cost_n3_n3 = cost_n4_n4;
MapLocation loc_n4_n3 = rcCur.translate(-4, -3);
long cost_n4_n3 = heuristicCooldown(loc_n4_n3);
if(cost_n4_n3 < cost_n3_n3) cost_n3_n3 = cost_n4_n3;
cost_n3_n3 += smartCooldown(loc_n3_n3);
//best for [-3, -3]
if(cost_n3_n3 < cost_n2_n3) cost_n2_n3 = cost_n3_n3;
cost_n2_n3 += smartCooldown(loc_n2_n3);
//best for [-2, -3]
if(cost_n2_n3 < cost_n1_n3) cost_n1_n3 = cost_n2_n3;
cost_n1_n3 += smartCooldown(loc_n1_n3);
//best for [-1, -3]
if(cost_n1_n3 < cost_0_n3) cost_0_n3 = cost_n1_n3;
cost_0_n3 += smartCooldown(loc_0_n3);
//best for [0, -3]
if(cost_0_n3 < cost_0_n2) cost_0_n2 = cost_0_n3;
if(cost_1_n3 < cost_0_n2) cost_0_n2 = cost_1_n3;
if(cost_n1_n3 < cost_0_n2) cost_0_n2 = cost_n1_n3;
MapLocation loc_1_n2 = rcCur.translate(1, -2);
long cost_1_n2 = Long.MAX_VALUE;
if(cost_1_n3 < cost_1_n2) cost_1_n2 = cost_1_n3;
if(cost_2_n3 < cost_1_n2) cost_1_n2 = cost_2_n3;
if(cost_0_n3 < cost_1_n2) cost_1_n2 = cost_0_n3;
MapLocation loc_2_n2 = rcCur.translate(2, -2);
long cost_2_n2 = Long.MAX_VALUE;
if(cost_2_n3 < cost_2_n2) cost_2_n2 = cost_2_n3;
if(cost_3_n3 < cost_2_n2) cost_2_n2 = cost_3_n3;
if(cost_1_n3 < cost_2_n2) cost_2_n2 = cost_1_n3;
MapLocation loc_3_n2 = rcCur.translate(3, -2);
long cost_3_n2 = Long.MAX_VALUE;
if(cost_3_n3 < cost_3_n2) cost_3_n2 = cost_3_n3;
if(cost_4_n3 < cost_3_n2) cost_3_n2 = cost_4_n3;
if(cost_2_n3 < cost_3_n2) cost_3_n2 = cost_2_n3;
MapLocation loc_4_n2 = rcCur.translate(4, -2);
long cost_4_n2 = Long.MAX_VALUE;
if(cost_4_n3 < cost_4_n2) cost_4_n2 = cost_4_n3;
MapLocation loc_5_n3 = rcCur.translate(5, -3);
long cost_5_n3 = heuristicCooldown(loc_5_n3);
if(cost_5_n3 < cost_4_n2) cost_4_n2 = cost_5_n3;
if(cost_3_n3 < cost_4_n2) cost_4_n2 = cost_3_n3;
MapLocation loc_5_n2 = rcCur.translate(5, -2);
long cost_5_n2 = heuristicCooldown(loc_5_n2);
if(cost_5_n2 < cost_4_n2) cost_4_n2 = cost_5_n2;
cost_4_n2 += smartCooldown(loc_4_n2);
//best for [4, -2]
if(cost_4_n2 < cost_3_n2) cost_3_n2 = cost_4_n2;
cost_3_n2 += smartCooldown(loc_3_n2);
//best for [3, -2]
if(cost_3_n2 < cost_2_n2) cost_2_n2 = cost_3_n2;
cost_2_n2 += smartCooldown(loc_2_n2);
//best for [2, -2]
if(cost_2_n2 < cost_1_n2) cost_1_n2 = cost_2_n2;
cost_1_n2 += smartCooldown(loc_1_n2);
//best for [1, -2]
if(cost_1_n2 < cost_0_n2) cost_0_n2 = cost_1_n2;
MapLocation loc_n1_n2 = rcCur.translate(-1, -2);
long cost_n1_n2 = Long.MAX_VALUE;
if(cost_n1_n3 < cost_n1_n2) cost_n1_n2 = cost_n1_n3;
if(cost_0_n3 < cost_n1_n2) cost_n1_n2 = cost_0_n3;
if(cost_n2_n3 < cost_n1_n2) cost_n1_n2 = cost_n2_n3;
MapLocation loc_n2_n2 = rcCur.translate(-2, -2);
long cost_n2_n2 = Long.MAX_VALUE;
if(cost_n2_n3 < cost_n2_n2) cost_n2_n2 = cost_n2_n3;
if(cost_n1_n3 < cost_n2_n2) cost_n2_n2 = cost_n1_n3;
if(cost_n3_n3 < cost_n2_n2) cost_n2_n2 = cost_n3_n3;
MapLocation loc_n3_n2 = rcCur.translate(-3, -2);
long cost_n3_n2 = Long.MAX_VALUE;
if(cost_n3_n3 < cost_n3_n2) cost_n3_n2 = cost_n3_n3;
if(cost_n2_n3 < cost_n3_n2) cost_n3_n2 = cost_n2_n3;
if(cost_n4_n3 < cost_n3_n2) cost_n3_n2 = cost_n4_n3;
MapLocation loc_n4_n2 = rcCur.translate(-4, -2);
long cost_n4_n2 = Long.MAX_VALUE;
if(cost_n4_n3 < cost_n4_n2) cost_n4_n2 = cost_n4_n3;
if(cost_n3_n3 < cost_n4_n2) cost_n4_n2 = cost_n3_n3;
MapLocation loc_n5_n3 = rcCur.translate(-5, -3);
long cost_n5_n3 = heuristicCooldown(loc_n5_n3);
if(cost_n5_n3 < cost_n4_n2) cost_n4_n2 = cost_n5_n3;
MapLocation loc_n5_n2 = rcCur.translate(-5, -2);
long cost_n5_n2 = heuristicCooldown(loc_n5_n2);
if(cost_n5_n2 < cost_n4_n2) cost_n4_n2 = cost_n5_n2;
cost_n4_n2 += smartCooldown(loc_n4_n2);
//best for [-4, -2]
if(cost_n4_n2 < cost_n3_n2) cost_n3_n2 = cost_n4_n2;
cost_n3_n2 += smartCooldown(loc_n3_n2);
//best for [-3, -2]
if(cost_n3_n2 < cost_n2_n2) cost_n2_n2 = cost_n3_n2;
cost_n2_n2 += smartCooldown(loc_n2_n2);
//best for [-2, -2]
if(cost_n2_n2 < cost_n1_n2) cost_n1_n2 = cost_n2_n2;
cost_n1_n2 += smartCooldown(loc_n1_n2);
//best for [-1, -2]
if(cost_n1_n2 < cost_0_n2) cost_0_n2 = cost_n1_n2;
cost_0_n2 += smartCooldown(loc_0_n2);
//best for [0, -2]
if(cost_0_n2 < cost_0_n1) cost_0_n1 = cost_0_n2;
if(cost_1_n2 < cost_0_n1) cost_0_n1 = cost_1_n2;
if(cost_n1_n2 < cost_0_n1) cost_0_n1 = cost_n1_n2;
MapLocation loc_1_n1 = rcCur.translate(1, -1);
long cost_1_n1 = Long.MAX_VALUE;
if(cost_1_n2 < cost_1_n1) cost_1_n1 = cost_1_n2;
if(cost_2_n2 < cost_1_n1) cost_1_n1 = cost_2_n2;
if(cost_0_n2 < cost_1_n1) cost_1_n1 = cost_0_n2;
MapLocation loc_2_n1 = rcCur.translate(2, -1);
long cost_2_n1 = Long.MAX_VALUE;
if(cost_2_n2 < cost_2_n1) cost_2_n1 = cost_2_n2;
if(cost_3_n2 < cost_2_n1) cost_2_n1 = cost_3_n2;
if(cost_1_n2 < cost_2_n1) cost_2_n1 = cost_1_n2;
MapLocation loc_3_n1 = rcCur.translate(3, -1);
long cost_3_n1 = Long.MAX_VALUE;
if(cost_3_n2 < cost_3_n1) cost_3_n1 = cost_3_n2;
if(cost_4_n2 < cost_3_n1) cost_3_n1 = cost_4_n2;
if(cost_2_n2 < cost_3_n1) cost_3_n1 = cost_2_n2;
MapLocation loc_4_n1 = rcCur.translate(4, -1);
long cost_4_n1 = Long.MAX_VALUE;
if(cost_4_n2 < cost_4_n1) cost_4_n1 = cost_4_n2;
if(cost_5_n2 < cost_4_n1) cost_4_n1 = cost_5_n2;
if(cost_3_n2 < cost_4_n1) cost_4_n1 = cost_3_n2;
MapLocation loc_5_n1 = rcCur.translate(5, -1);
long cost_5_n1 = heuristicCooldown(loc_5_n1);
if(cost_5_n1 < cost_4_n1) cost_4_n1 = cost_5_n1;
cost_4_n1 += smartCooldown(loc_4_n1);
//best for [4, -1]
if(cost_4_n1 < cost_3_n1) cost_3_n1 = cost_4_n1;
cost_3_n1 += smartCooldown(loc_3_n1);
//best for [3, -1]
if(cost_3_n1 < cost_2_n1) cost_2_n1 = cost_3_n1;
cost_2_n1 += smartCooldown(loc_2_n1);
//best for [2, -1]
if(cost_2_n1 < cost_1_n1) cost_1_n1 = cost_2_n1;
cost_1_n1 += smartCooldown(loc_1_n1);
//best for [1, -1]
if(cost_1_n1 < cost_0_n1) cost_0_n1 = cost_1_n1;
MapLocation loc_n1_n1 = rcCur.translate(-1, -1);
long cost_n1_n1 = Long.MAX_VALUE;
if(cost_n1_n2 < cost_n1_n1) cost_n1_n1 = cost_n1_n2;
if(cost_0_n2 < cost_n1_n1) cost_n1_n1 = cost_0_n2;
if(cost_n2_n2 < cost_n1_n1) cost_n1_n1 = cost_n2_n2;
MapLocation loc_n2_n1 = rcCur.translate(-2, -1);
long cost_n2_n1 = Long.MAX_VALUE;
if(cost_n2_n2 < cost_n2_n1) cost_n2_n1 = cost_n2_n2;
if(cost_n1_n2 < cost_n2_n1) cost_n2_n1 = cost_n1_n2;
if(cost_n3_n2 < cost_n2_n1) cost_n2_n1 = cost_n3_n2;
MapLocation loc_n3_n1 = rcCur.translate(-3, -1);
long cost_n3_n1 = Long.MAX_VALUE;
if(cost_n3_n2 < cost_n3_n1) cost_n3_n1 = cost_n3_n2;
if(cost_n2_n2 < cost_n3_n1) cost_n3_n1 = cost_n2_n2;
if(cost_n4_n2 < cost_n3_n1) cost_n3_n1 = cost_n4_n2;
MapLocation loc_n4_n1 = rcCur.translate(-4, -1);
long cost_n4_n1 = Long.MAX_VALUE;
if(cost_n4_n2 < cost_n4_n1) cost_n4_n1 = cost_n4_n2;
if(cost_n3_n2 < cost_n4_n1) cost_n4_n1 = cost_n3_n2;
if(cost_n5_n2 < cost_n4_n1) cost_n4_n1 = cost_n5_n2;
MapLocation loc_n5_n1 = rcCur.translate(-5, -1);
long cost_n5_n1 = heuristicCooldown(loc_n5_n1);
if(cost_n5_n1 < cost_n4_n1) cost_n4_n1 = cost_n5_n1;
cost_n4_n1 += smartCooldown(loc_n4_n1);
//best for [-4, -1]
if(cost_n4_n1 < cost_n3_n1) cost_n3_n1 = cost_n4_n1;
cost_n3_n1 += smartCooldown(loc_n3_n1);
//best for [-3, -1]
if(cost_n3_n1 < cost_n2_n1) cost_n2_n1 = cost_n3_n1;
cost_n2_n1 += smartCooldown(loc_n2_n1);
//best for [-2, -1]
if(cost_n2_n1 < cost_n1_n1) cost_n1_n1 = cost_n2_n1;
cost_n1_n1 += smartCooldown(loc_n1_n1);
//best for [-1, -1]
if(cost_n1_n1 < cost_0_n1) cost_0_n1 = cost_n1_n1;
cost_0_n1 += smartCooldown(loc_0_n1);
//best for [0, -1]
if(cost_0_n1 < cost_0_0) { cost_0_0 = cost_0_n1;best_dir = Direction.SOUTH;}
if(cost_1_n1 < cost_0_0) { cost_0_0 = cost_1_n1;best_dir = Direction.SOUTHEAST;}
if(cost_n1_n1 < cost_0_0) { cost_0_0 = cost_n1_n1;best_dir = Direction.SOUTHWEST;}
MapLocation loc_1_0 = rcCur.translate(1, 0);
long cost_1_0 = Long.MAX_VALUE;
if(cost_1_n1 < cost_1_0) cost_1_0 = cost_1_n1;
if(cost_2_n1 < cost_1_0) cost_1_0 = cost_2_n1;
if(cost_0_n1 < cost_1_0) cost_1_0 = cost_0_n1;
MapLocation loc_2_0 = rcCur.translate(2, 0);
long cost_2_0 = Long.MAX_VALUE;
if(cost_2_n1 < cost_2_0) cost_2_0 = cost_2_n1;
if(cost_3_n1 < cost_2_0) cost_2_0 = cost_3_n1;
if(cost_1_n1 < cost_2_0) cost_2_0 = cost_1_n1;
MapLocation loc_3_0 = rcCur.translate(3, 0);
long cost_3_0 = Long.MAX_VALUE;
if(cost_3_n1 < cost_3_0) cost_3_0 = cost_3_n1;
if(cost_4_n1 < cost_3_0) cost_3_0 = cost_4_n1;
if(cost_2_n1 < cost_3_0) cost_3_0 = cost_2_n1;
MapLocation loc_4_0 = rcCur.translate(4, 0);
long cost_4_0 = Long.MAX_VALUE;
if(cost_4_n1 < cost_4_0) cost_4_0 = cost_4_n1;
if(cost_5_n1 < cost_4_0) cost_4_0 = cost_5_n1;
if(cost_3_n1 < cost_4_0) cost_4_0 = cost_3_n1;
MapLocation loc_5_0 = rcCur.translate(5, 0);
long cost_5_0 = heuristicCooldown(loc_5_0);
if(cost_5_0 < cost_4_0) cost_4_0 = cost_5_0;
cost_4_0 += smartCooldown(loc_4_0);
//best for [4, 0]
if(cost_4_0 < cost_3_0) cost_3_0 = cost_4_0;
cost_3_0 += smartCooldown(loc_3_0);
//best for [3, 0]
if(cost_3_0 < cost_2_0) cost_2_0 = cost_3_0;
cost_2_0 += smartCooldown(loc_2_0);
//best for [2, 0]
if(cost_2_0 < cost_1_0) cost_1_0 = cost_2_0;
cost_1_0 += smartCooldown(loc_1_0);
//best for [1, 0]
if(cost_1_0 < cost_0_0) { cost_0_0 = cost_1_0;best_dir = Direction.EAST;}
MapLocation loc_n1_0 = rcCur.translate(-1, 0);
long cost_n1_0 = Long.MAX_VALUE;
if(cost_n1_n1 < cost_n1_0) cost_n1_0 = cost_n1_n1;
if(cost_0_n1 < cost_n1_0) cost_n1_0 = cost_0_n1;
if(cost_n2_n1 < cost_n1_0) cost_n1_0 = cost_n2_n1;
MapLocation loc_n2_0 = rcCur.translate(-2, 0);
long cost_n2_0 = Long.MAX_VALUE;
if(cost_n2_n1 < cost_n2_0) cost_n2_0 = cost_n2_n1;
if(cost_n1_n1 < cost_n2_0) cost_n2_0 = cost_n1_n1;
if(cost_n3_n1 < cost_n2_0) cost_n2_0 = cost_n3_n1;
MapLocation loc_n3_0 = rcCur.translate(-3, 0);
long cost_n3_0 = Long.MAX_VALUE;
if(cost_n3_n1 < cost_n3_0) cost_n3_0 = cost_n3_n1;
if(cost_n2_n1 < cost_n3_0) cost_n3_0 = cost_n2_n1;
if(cost_n4_n1 < cost_n3_0) cost_n3_0 = cost_n4_n1;
MapLocation loc_n4_0 = rcCur.translate(-4, 0);
long cost_n4_0 = Long.MAX_VALUE;
if(cost_n4_n1 < cost_n4_0) cost_n4_0 = cost_n4_n1;
if(cost_n3_n1 < cost_n4_0) cost_n4_0 = cost_n3_n1;
if(cost_n5_n1 < cost_n4_0) cost_n4_0 = cost_n5_n1;
MapLocation loc_n5_0 = rcCur.translate(-5, 0);
long cost_n5_0 = heuristicCooldown(loc_n5_0);
if(cost_n5_0 < cost_n4_0) cost_n4_0 = cost_n5_0;
cost_n4_0 += smartCooldown(loc_n4_0);
//best for [-4, 0]
if(cost_n4_0 < cost_n3_0) cost_n3_0 = cost_n4_0;
cost_n3_0 += smartCooldown(loc_n3_0);
//best for [-3, 0]
if(cost_n3_0 < cost_n2_0) cost_n2_0 = cost_n3_0;
cost_n2_0 += smartCooldown(loc_n2_0);
//best for [-2, 0]
if(cost_n2_0 < cost_n1_0) cost_n1_0 = cost_n2_0;
cost_n1_0 += smartCooldown(loc_n1_0);
//best for [-1, 0]
if(cost_n1_0 < cost_0_0) { cost_0_0 = cost_n1_0;best_dir = Direction.WEST;}
cost_0_0 += smartCooldown(loc_0_0);
//best for [0, 0]
return best_dir;
}
static Direction pathfind_SOUTHWEST() throws GameActionException {
MapLocation loc_0_0 = rcCur.translate(0, 0);
long cost_0_0 = Long.MAX_VALUE;
Direction best_dir = null;
MapLocation loc_n1_n1 = rcCur.translate(-1, -1);
long cost_n1_n1 = Long.MAX_VALUE;
MapLocation loc_n2_n2 = rcCur.translate(-2, -2);
long cost_n2_n2 = Long.MAX_VALUE;
MapLocation loc_n3_n3 = rcCur.translate(-3, -3);
long cost_n3_n3 = Long.MAX_VALUE;
MapLocation loc_n4_n4 = rcCur.translate(-4, -4);
long cost_n4_n4 = heuristicCooldown(loc_n4_n4);
if(cost_n4_n4 < cost_n3_n3) cost_n3_n3 = cost_n4_n4;
MapLocation loc_n3_n4 = rcCur.translate(-3, -4);
long cost_n3_n4 = heuristicCooldown(loc_n3_n4);
if(cost_n3_n4 < cost_n3_n3) cost_n3_n3 = cost_n3_n4;
MapLocation loc_n4_n3 = rcCur.translate(-4, -3);
long cost_n4_n3 = heuristicCooldown(loc_n4_n3);
if(cost_n4_n3 < cost_n3_n3) cost_n3_n3 = cost_n4_n3;
MapLocation loc_n2_n4 = rcCur.translate(-2, -4);
long cost_n2_n4 = Long.MAX_VALUE;
MapLocation loc_n3_n5 = rcCur.translate(-3, -5);
long cost_n3_n5 = heuristicCooldown(loc_n3_n5);
if(cost_n3_n5 < cost_n2_n4) cost_n2_n4 = cost_n3_n5;
MapLocation loc_n2_n5 = rcCur.translate(-2, -5);
long cost_n2_n5 = heuristicCooldown(loc_n2_n5);
if(cost_n2_n5 < cost_n2_n4) cost_n2_n4 = cost_n2_n5;
if(cost_n3_n4 < cost_n2_n4) cost_n2_n4 = cost_n3_n4;
MapLocation loc_n1_n5 = rcCur.translate(-1, -5);
long cost_n1_n5 = heuristicCooldown(loc_n1_n5);
if(cost_n1_n5 < cost_n2_n4) cost_n2_n4 = cost_n1_n5;
cost_n2_n4 += smartCooldown(loc_n2_n4);
//best for [-2, -4]
if(cost_n2_n4 < cost_n3_n3) cost_n3_n3 = cost_n2_n4;
MapLocation loc_n4_n2 = rcCur.translate(-4, -2);
long cost_n4_n2 = Long.MAX_VALUE;
MapLocation loc_n5_n3 = rcCur.translate(-5, -3);
long cost_n5_n3 = heuristicCooldown(loc_n5_n3);
if(cost_n5_n3 < cost_n4_n2) cost_n4_n2 = cost_n5_n3;
if(cost_n4_n3 < cost_n4_n2) cost_n4_n2 = cost_n4_n3;
MapLocation loc_n5_n2 = rcCur.translate(-5, -2);
long cost_n5_n2 = heuristicCooldown(loc_n5_n2);
if(cost_n5_n2 < cost_n4_n2) cost_n4_n2 = cost_n5_n2;
MapLocation loc_n5_n1 = rcCur.translate(-5, -1);
long cost_n5_n1 = heuristicCooldown(loc_n5_n1);
if(cost_n5_n1 < cost_n4_n2) cost_n4_n2 = cost_n5_n1;
cost_n4_n2 += smartCooldown(loc_n4_n2);
//best for [-4, -2]
if(cost_n4_n2 < cost_n3_n3) cost_n3_n3 = cost_n4_n2;
cost_n3_n3 += smartCooldown(loc_n3_n3);
//best for [-3, -3]
if(cost_n3_n3 < cost_n2_n2) cost_n2_n2 = cost_n3_n3;
MapLocation loc_n2_n3 = rcCur.translate(-2, -3);
long cost_n2_n3 = Long.MAX_VALUE;
if(cost_n3_n4 < cost_n2_n3) cost_n2_n3 = cost_n3_n4;
if(cost_n2_n4 < cost_n2_n3) cost_n2_n3 = cost_n2_n4;
if(cost_n3_n3 < cost_n2_n3) cost_n2_n3 = cost_n3_n3;
MapLocation loc_n1_n4 = rcCur.translate(-1, -4);
long cost_n1_n4 = Long.MAX_VALUE;
if(cost_n2_n5 < cost_n1_n4) cost_n1_n4 = cost_n2_n5;
if(cost_n1_n5 < cost_n1_n4) cost_n1_n4 = cost_n1_n5;
if(cost_n2_n4 < cost_n1_n4) cost_n1_n4 = cost_n2_n4;
MapLocation loc_0_n5 = rcCur.translate(0, -5);
long cost_0_n5 = heuristicCooldown(loc_0_n5);
if(cost_0_n5 < cost_n1_n4) cost_n1_n4 = cost_0_n5;
cost_n1_n4 += smartCooldown(loc_n1_n4);
//best for [-1, -4]
if(cost_n1_n4 < cost_n2_n3) cost_n2_n3 = cost_n1_n4;
MapLocation loc_n3_n2 = rcCur.translate(-3, -2);
long cost_n3_n2 = Long.MAX_VALUE;
if(cost_n4_n3 < cost_n3_n2) cost_n3_n2 = cost_n4_n3;
if(cost_n3_n3 < cost_n3_n2) cost_n3_n2 = cost_n3_n3;
if(cost_n4_n2 < cost_n3_n2) cost_n3_n2 = cost_n4_n2;
MapLocation loc_n4_n1 = rcCur.translate(-4, -1);
long cost_n4_n1 = Long.MAX_VALUE;
if(cost_n5_n2 < cost_n4_n1) cost_n4_n1 = cost_n5_n2;
if(cost_n4_n2 < cost_n4_n1) cost_n4_n1 = cost_n4_n2;
if(cost_n5_n1 < cost_n4_n1) cost_n4_n1 = cost_n5_n1;
MapLocation loc_n5_0 = rcCur.translate(-5, 0);
long cost_n5_0 = heuristicCooldown(loc_n5_0);
if(cost_n5_0 < cost_n4_n1) cost_n4_n1 = cost_n5_0;
cost_n4_n1 += smartCooldown(loc_n4_n1);
//best for [-4, -1]
if(cost_n4_n1 < cost_n3_n2) cost_n3_n2 = cost_n4_n1;
cost_n3_n2 += smartCooldown(loc_n3_n2);
//best for [-3, -2]
if(cost_n3_n2 < cost_n2_n3) cost_n2_n3 = cost_n3_n2;
cost_n2_n3 += smartCooldown(loc_n2_n3);
//best for [-2, -3]
if(cost_n2_n3 < cost_n2_n2) cost_n2_n2 = cost_n2_n3;
if(cost_n3_n2 < cost_n2_n2) cost_n2_n2 = cost_n3_n2;
MapLocation loc_n1_n3 = rcCur.translate(-1, -3);
long cost_n1_n3 = Long.MAX_VALUE;
if(cost_n2_n4 < cost_n1_n3) cost_n1_n3 = cost_n2_n4;
if(cost_n1_n4 < cost_n1_n3) cost_n1_n3 = cost_n1_n4;
if(cost_n2_n3 < cost_n1_n3) cost_n1_n3 = cost_n2_n3;
MapLocation loc_0_n4 = rcCur.translate(0, -4);
long cost_0_n4 = Long.MAX_VALUE;
if(cost_n1_n5 < cost_0_n4) cost_0_n4 = cost_n1_n5;
if(cost_0_n5 < cost_0_n4) cost_0_n4 = cost_0_n5;
if(cost_n1_n4 < cost_0_n4) cost_0_n4 = cost_n1_n4;
MapLocation loc_1_n5 = rcCur.translate(1, -5);
long cost_1_n5 = heuristicCooldown(loc_1_n5);
if(cost_1_n5 < cost_0_n4) cost_0_n4 = cost_1_n5;
cost_0_n4 += smartCooldown(loc_0_n4);
//best for [0, -4]
if(cost_0_n4 < cost_n1_n3) cost_n1_n3 = cost_0_n4;
cost_n1_n3 += smartCooldown(loc_n1_n3);
//best for [-1, -3]
if(cost_n1_n3 < cost_n2_n2) cost_n2_n2 = cost_n1_n3;
MapLocation loc_n3_n1 = rcCur.translate(-3, -1);
long cost_n3_n1 = Long.MAX_VALUE;
if(cost_n4_n2 < cost_n3_n1) cost_n3_n1 = cost_n4_n2;
if(cost_n3_n2 < cost_n3_n1) cost_n3_n1 = cost_n3_n2;
if(cost_n4_n1 < cost_n3_n1) cost_n3_n1 = cost_n4_n1;
MapLocation loc_n4_0 = rcCur.translate(-4, 0);
long cost_n4_0 = Long.MAX_VALUE;
if(cost_n5_n1 < cost_n4_0) cost_n4_0 = cost_n5_n1;
if(cost_n4_n1 < cost_n4_0) cost_n4_0 = cost_n4_n1;
if(cost_n5_0 < cost_n4_0) cost_n4_0 = cost_n5_0;
MapLocation loc_n5_1 = rcCur.translate(-5, 1);
long cost_n5_1 = heuristicCooldown(loc_n5_1);
if(cost_n5_1 < cost_n4_0) cost_n4_0 = cost_n5_1;
cost_n4_0 += smartCooldown(loc_n4_0);
//best for [-4, 0]
if(cost_n4_0 < cost_n3_n1) cost_n3_n1 = cost_n4_0;
cost_n3_n1 += smartCooldown(loc_n3_n1);
//best for [-3, -1]
if(cost_n3_n1 < cost_n2_n2) cost_n2_n2 = cost_n3_n1;
cost_n2_n2 += smartCooldown(loc_n2_n2);
//best for [-2, -2]
if(cost_n2_n2 < cost_n1_n1) cost_n1_n1 = cost_n2_n2;
MapLocation loc_n1_n2 = rcCur.translate(-1, -2);
long cost_n1_n2 = Long.MAX_VALUE;
if(cost_n2_n3 < cost_n1_n2) cost_n1_n2 = cost_n2_n3;
if(cost_n1_n3 < cost_n1_n2) cost_n1_n2 = cost_n1_n3;
if(cost_n2_n2 < cost_n1_n2) cost_n1_n2 = cost_n2_n2;
MapLocation loc_0_n3 = rcCur.translate(0, -3);
long cost_0_n3 = Long.MAX_VALUE;
if(cost_n1_n4 < cost_0_n3) cost_0_n3 = cost_n1_n4;
if(cost_0_n4 < cost_0_n3) cost_0_n3 = cost_0_n4;
if(cost_n1_n3 < cost_0_n3) cost_0_n3 = cost_n1_n3;
MapLocation loc_1_n4 = rcCur.translate(1, -4);
long cost_1_n4 = Long.MAX_VALUE;
if(cost_0_n5 < cost_1_n4) cost_1_n4 = cost_0_n5;
if(cost_1_n5 < cost_1_n4) cost_1_n4 = cost_1_n5;
if(cost_0_n4 < cost_1_n4) cost_1_n4 = cost_0_n4;
MapLocation loc_2_n5 = rcCur.translate(2, -5);
long cost_2_n5 = heuristicCooldown(loc_2_n5);
if(cost_2_n5 < cost_1_n4) cost_1_n4 = cost_2_n5;
cost_1_n4 += smartCooldown(loc_1_n4);
//best for [1, -4]
if(cost_1_n4 < cost_0_n3) cost_0_n3 = cost_1_n4;
cost_0_n3 += smartCooldown(loc_0_n3);
//best for [0, -3]
if(cost_0_n3 < cost_n1_n2) cost_n1_n2 = cost_0_n3;
MapLocation loc_n2_n1 = rcCur.translate(-2, -1);
long cost_n2_n1 = Long.MAX_VALUE;
if(cost_n3_n2 < cost_n2_n1) cost_n2_n1 = cost_n3_n2;
if(cost_n2_n2 < cost_n2_n1) cost_n2_n1 = cost_n2_n2;
if(cost_n3_n1 < cost_n2_n1) cost_n2_n1 = cost_n3_n1;
MapLocation loc_n3_0 = rcCur.translate(-3, 0);
long cost_n3_0 = Long.MAX_VALUE;
if(cost_n4_n1 < cost_n3_0) cost_n3_0 = cost_n4_n1;
if(cost_n3_n1 < cost_n3_0) cost_n3_0 = cost_n3_n1;
if(cost_n4_0 < cost_n3_0) cost_n3_0 = cost_n4_0;
MapLocation loc_n4_1 = rcCur.translate(-4, 1);
long cost_n4_1 = Long.MAX_VALUE;
if(cost_n5_0 < cost_n4_1) cost_n4_1 = cost_n5_0;
if(cost_n4_0 < cost_n4_1) cost_n4_1 = cost_n4_0;
if(cost_n5_1 < cost_n4_1) cost_n4_1 = cost_n5_1;
MapLocation loc_n5_2 = rcCur.translate(-5, 2);
long cost_n5_2 = heuristicCooldown(loc_n5_2);
if(cost_n5_2 < cost_n4_1) cost_n4_1 = cost_n5_2;
cost_n4_1 += smartCooldown(loc_n4_1);
//best for [-4, 1]
if(cost_n4_1 < cost_n3_0) cost_n3_0 = cost_n4_1;
cost_n3_0 += smartCooldown(loc_n3_0);
//best for [-3, 0]
if(cost_n3_0 < cost_n2_n1) cost_n2_n1 = cost_n3_0;
cost_n2_n1 += smartCooldown(loc_n2_n1);
//best for [-2, -1]
if(cost_n2_n1 < cost_n1_n2) cost_n1_n2 = cost_n2_n1;
cost_n1_n2 += smartCooldown(loc_n1_n2);
//best for [-1, -2]
if(cost_n1_n2 < cost_n1_n1) cost_n1_n1 = cost_n1_n2;
if(cost_n2_n1 < cost_n1_n1) cost_n1_n1 = cost_n2_n1;
MapLocation loc_0_n2 = rcCur.translate(0, -2);
long cost_0_n2 = Long.MAX_VALUE;
if(cost_n1_n3 < cost_0_n2) cost_0_n2 = cost_n1_n3;
if(cost_0_n3 < cost_0_n2) cost_0_n2 = cost_0_n3;
if(cost_n1_n2 < cost_0_n2) cost_0_n2 = cost_n1_n2;
MapLocation loc_1_n3 = rcCur.translate(1, -3);
long cost_1_n3 = Long.MAX_VALUE;
if(cost_0_n4 < cost_1_n3) cost_1_n3 = cost_0_n4;
if(cost_1_n4 < cost_1_n3) cost_1_n3 = cost_1_n4;
if(cost_0_n3 < cost_1_n3) cost_1_n3 = cost_0_n3;
MapLocation loc_2_n4 = rcCur.translate(2, -4);
long cost_2_n4 = Long.MAX_VALUE;
if(cost_1_n5 < cost_2_n4) cost_2_n4 = cost_1_n5;
if(cost_2_n5 < cost_2_n4) cost_2_n4 = cost_2_n5;
if(cost_1_n4 < cost_2_n4) cost_2_n4 = cost_1_n4;
MapLocation loc_3_n5 = rcCur.translate(3, -5);
long cost_3_n5 = heuristicCooldown(loc_3_n5);
if(cost_3_n5 < cost_2_n4) cost_2_n4 = cost_3_n5;
cost_2_n4 += smartCooldown(loc_2_n4);
//best for [2, -4]
if(cost_2_n4 < cost_1_n3) cost_1_n3 = cost_2_n4;
cost_1_n3 += smartCooldown(loc_1_n3);
//best for [1, -3]
if(cost_1_n3 < cost_0_n2) cost_0_n2 = cost_1_n3;
cost_0_n2 += smartCooldown(loc_0_n2);
//best for [0, -2]
if(cost_0_n2 < cost_n1_n1) cost_n1_n1 = cost_0_n2;
MapLocation loc_n2_0 = rcCur.translate(-2, 0);
long cost_n2_0 = Long.MAX_VALUE;
if(cost_n3_n1 < cost_n2_0) cost_n2_0 = cost_n3_n1;
if(cost_n2_n1 < cost_n2_0) cost_n2_0 = cost_n2_n1;
if(cost_n3_0 < cost_n2_0) cost_n2_0 = cost_n3_0;
MapLocation loc_n3_1 = rcCur.translate(-3, 1);
long cost_n3_1 = Long.MAX_VALUE;
if(cost_n4_0 < cost_n3_1) cost_n3_1 = cost_n4_0;
if(cost_n3_0 < cost_n3_1) cost_n3_1 = cost_n3_0;
if(cost_n4_1 < cost_n3_1) cost_n3_1 = cost_n4_1;
MapLocation loc_n4_2 = rcCur.translate(-4, 2);
long cost_n4_2 = Long.MAX_VALUE;
if(cost_n5_1 < cost_n4_2) cost_n4_2 = cost_n5_1;
if(cost_n4_1 < cost_n4_2) cost_n4_2 = cost_n4_1;
if(cost_n5_2 < cost_n4_2) cost_n4_2 = cost_n5_2;
MapLocation loc_n5_3 = rcCur.translate(-5, 3);
long cost_n5_3 = heuristicCooldown(loc_n5_3);
if(cost_n5_3 < cost_n4_2) cost_n4_2 = cost_n5_3;
cost_n4_2 += smartCooldown(loc_n4_2);
//best for [-4, 2]
if(cost_n4_2 < cost_n3_1) cost_n3_1 = cost_n4_2;
cost_n3_1 += smartCooldown(loc_n3_1);
//best for [-3, 1]
if(cost_n3_1 < cost_n2_0) cost_n2_0 = cost_n3_1;
cost_n2_0 += smartCooldown(loc_n2_0);
//best for [-2, 0]
if(cost_n2_0 < cost_n1_n1) cost_n1_n1 = cost_n2_0;
cost_n1_n1 += smartCooldown(loc_n1_n1);
//best for [-1, -1]
if(cost_n1_n1 < cost_0_0) { cost_0_0 = cost_n1_n1;best_dir = Direction.SOUTHWEST;}
MapLocation loc_0_n1 = rcCur.translate(0, -1);
long cost_0_n1 = Long.MAX_VALUE;
if(cost_n1_n2 < cost_0_n1) cost_0_n1 = cost_n1_n2;
if(cost_0_n2 < cost_0_n1) cost_0_n1 = cost_0_n2;
if(cost_n1_n1 < cost_0_n1) cost_0_n1 = cost_n1_n1;
MapLocation loc_1_n2 = rcCur.translate(1, -2);
long cost_1_n2 = Long.MAX_VALUE;
if(cost_0_n3 < cost_1_n2) cost_1_n2 = cost_0_n3;
if(cost_1_n3 < cost_1_n2) cost_1_n2 = cost_1_n3;
if(cost_0_n2 < cost_1_n2) cost_1_n2 = cost_0_n2;
MapLocation loc_2_n3 = rcCur.translate(2, -3);
long cost_2_n3 = Long.MAX_VALUE;
if(cost_1_n4 < cost_2_n3) cost_2_n3 = cost_1_n4;
if(cost_2_n4 < cost_2_n3) cost_2_n3 = cost_2_n4;
if(cost_1_n3 < cost_2_n3) cost_2_n3 = cost_1_n3;
MapLocation loc_3_n4 = rcCur.translate(3, -4);
long cost_3_n4 = heuristicCooldown(loc_3_n4);
if(cost_3_n4 < cost_2_n3) cost_2_n3 = cost_3_n4;
cost_2_n3 += smartCooldown(loc_2_n3);
//best for [2, -3]
if(cost_2_n3 < cost_1_n2) cost_1_n2 = cost_2_n3;
cost_1_n2 += smartCooldown(loc_1_n2);
//best for [1, -2]
if(cost_1_n2 < cost_0_n1) cost_0_n1 = cost_1_n2;
MapLocation loc_n1_0 = rcCur.translate(-1, 0);
long cost_n1_0 = Long.MAX_VALUE;
if(cost_n2_n1 < cost_n1_0) cost_n1_0 = cost_n2_n1;
if(cost_n1_n1 < cost_n1_0) cost_n1_0 = cost_n1_n1;
if(cost_n2_0 < cost_n1_0) cost_n1_0 = cost_n2_0;
MapLocation loc_n2_1 = rcCur.translate(-2, 1);
long cost_n2_1 = Long.MAX_VALUE;
if(cost_n3_0 < cost_n2_1) cost_n2_1 = cost_n3_0;
if(cost_n2_0 < cost_n2_1) cost_n2_1 = cost_n2_0;
if(cost_n3_1 < cost_n2_1) cost_n2_1 = cost_n3_1;
MapLocation loc_n3_2 = rcCur.translate(-3, 2);
long cost_n3_2 = Long.MAX_VALUE;
if(cost_n4_1 < cost_n3_2) cost_n3_2 = cost_n4_1;
if(cost_n3_1 < cost_n3_2) cost_n3_2 = cost_n3_1;
if(cost_n4_2 < cost_n3_2) cost_n3_2 = cost_n4_2;
MapLocation loc_n4_3 = rcCur.translate(-4, 3);
long cost_n4_3 = heuristicCooldown(loc_n4_3);
if(cost_n4_3 < cost_n3_2) cost_n3_2 = cost_n4_3;
cost_n3_2 += smartCooldown(loc_n3_2);
//best for [-3, 2]
if(cost_n3_2 < cost_n2_1) cost_n2_1 = cost_n3_2;
cost_n2_1 += smartCooldown(loc_n2_1);
//best for [-2, 1]
if(cost_n2_1 < cost_n1_0) cost_n1_0 = cost_n2_1;
cost_n1_0 += smartCooldown(loc_n1_0);
//best for [-1, 0]
if(cost_n1_0 < cost_0_n1) cost_0_n1 = cost_n1_0;
cost_0_n1 += smartCooldown(loc_0_n1);
//best for [0, -1]
if(cost_0_n1 < cost_0_0) { cost_0_0 = cost_0_n1;best_dir = Direction.SOUTH;}
if(cost_n1_0 < cost_0_0) { cost_0_0 = cost_n1_0;best_dir = Direction.WEST;}
MapLocation loc_1_n1 = rcCur.translate(1, -1);
long cost_1_n1 = Long.MAX_VALUE;
if(cost_0_n2 < cost_1_n1) cost_1_n1 = cost_0_n2;
if(cost_1_n2 < cost_1_n1) cost_1_n1 = cost_1_n2;
if(cost_0_n1 < cost_1_n1) cost_1_n1 = cost_0_n1;
MapLocation loc_2_n2 = rcCur.translate(2, -2);
long cost_2_n2 = Long.MAX_VALUE;
if(cost_1_n3 < cost_2_n2) cost_2_n2 = cost_1_n3;
if(cost_2_n3 < cost_2_n2) cost_2_n2 = cost_2_n3;
if(cost_1_n2 < cost_2_n2) cost_2_n2 = cost_1_n2;
MapLocation loc_3_n3 = rcCur.translate(3, -3);
long cost_3_n3 = Long.MAX_VALUE;
if(cost_2_n4 < cost_3_n3) cost_3_n3 = cost_2_n4;
if(cost_3_n4 < cost_3_n3) cost_3_n3 = cost_3_n4;
if(cost_2_n3 < cost_3_n3) cost_3_n3 = cost_2_n3;
MapLocation loc_4_n4 = rcCur.translate(4, -4);
long cost_4_n4 = heuristicCooldown(loc_4_n4);
if(cost_4_n4 < cost_3_n3) cost_3_n3 = cost_4_n4;
cost_3_n3 += smartCooldown(loc_3_n3);
//best for [3, -3]
if(cost_3_n3 < cost_2_n2) cost_2_n2 = cost_3_n3;
cost_2_n2 += smartCooldown(loc_2_n2);
//best for [2, -2]
if(cost_2_n2 < cost_1_n1) cost_1_n1 = cost_2_n2;
cost_1_n1 += smartCooldown(loc_1_n1);
//best for [1, -1]
if(cost_1_n1 < cost_0_0) { cost_0_0 = cost_1_n1;best_dir = Direction.SOUTHEAST;}
MapLocation loc_n1_1 = rcCur.translate(-1, 1);
long cost_n1_1 = Long.MAX_VALUE;
if(cost_n2_0 < cost_n1_1) cost_n1_1 = cost_n2_0;
if(cost_n1_0 < cost_n1_1) cost_n1_1 = cost_n1_0;
if(cost_n2_1 < cost_n1_1) cost_n1_1 = cost_n2_1;
MapLocation loc_n2_2 = rcCur.translate(-2, 2);
long cost_n2_2 = Long.MAX_VALUE;
if(cost_n3_1 < cost_n2_2) cost_n2_2 = cost_n3_1;
if(cost_n2_1 < cost_n2_2) cost_n2_2 = cost_n2_1;
if(cost_n3_2 < cost_n2_2) cost_n2_2 = cost_n3_2;
MapLocation loc_n3_3 = rcCur.translate(-3, 3);
long cost_n3_3 = Long.MAX_VALUE;
if(cost_n4_2 < cost_n3_3) cost_n3_3 = cost_n4_2;
if(cost_n3_2 < cost_n3_3) cost_n3_3 = cost_n3_2;
if(cost_n4_3 < cost_n3_3) cost_n3_3 = cost_n4_3;
MapLocation loc_n4_4 = rcCur.translate(-4, 4);
long cost_n4_4 = heuristicCooldown(loc_n4_4);
if(cost_n4_4 < cost_n3_3) cost_n3_3 = cost_n4_4;
cost_n3_3 += smartCooldown(loc_n3_3);
//best for [-3, 3]
if(cost_n3_3 < cost_n2_2) cost_n2_2 = cost_n3_3;
cost_n2_2 += smartCooldown(loc_n2_2);
//best for [-2, 2]
if(cost_n2_2 < cost_n1_1) cost_n1_1 = cost_n2_2;
cost_n1_1 += smartCooldown(loc_n1_1);
//best for [-1, 1]
if(cost_n1_1 < cost_0_0) { cost_0_0 = cost_n1_1;best_dir = Direction.NORTHWEST;}
cost_0_0 += smartCooldown(loc_0_0);
//best for [0, 0]
return best_dir;
}
static Direction pathfind_WEST() throws GameActionException {
MapLocation loc_0_0 = rcCur.translate(0, 0);
long cost_0_0 = Long.MAX_VALUE;
Direction best_dir = null;
MapLocation loc_n1_0 = rcCur.translate(-1, 0);
long cost_n1_0 = Long.MAX_VALUE;
MapLocation loc_n2_0 = rcCur.translate(-2, 0);
long cost_n2_0 = Long.MAX_VALUE;
MapLocation loc_n3_0 = rcCur.translate(-3, 0);
long cost_n3_0 = Long.MAX_VALUE;
MapLocation loc_n4_0 = rcCur.translate(-4, 0);
long cost_n4_0 = Long.MAX_VALUE;
MapLocation loc_n5_0 = rcCur.translate(-5, 0);
long cost_n5_0 = heuristicCooldown(loc_n5_0);
if(cost_n5_0 < cost_n4_0) cost_n4_0 = cost_n5_0;
MapLocation loc_n5_n1 = rcCur.translate(-5, -1);
long cost_n5_n1 = heuristicCooldown(loc_n5_n1);
if(cost_n5_n1 < cost_n4_0) cost_n4_0 = cost_n5_n1;
MapLocation loc_n5_1 = rcCur.translate(-5, 1);
long cost_n5_1 = heuristicCooldown(loc_n5_1);
if(cost_n5_1 < cost_n4_0) cost_n4_0 = cost_n5_1;
MapLocation loc_n4_n1 = rcCur.translate(-4, -1);
long cost_n4_n1 = Long.MAX_VALUE;
if(cost_n5_n1 < cost_n4_n1) cost_n4_n1 = cost_n5_n1;
MapLocation loc_n5_n2 = rcCur.translate(-5, -2);
long cost_n5_n2 = heuristicCooldown(loc_n5_n2);
if(cost_n5_n2 < cost_n4_n1) cost_n4_n1 = cost_n5_n2;
if(cost_n5_0 < cost_n4_n1) cost_n4_n1 = cost_n5_0;
MapLocation loc_n4_n2 = rcCur.translate(-4, -2);
long cost_n4_n2 = Long.MAX_VALUE;
if(cost_n5_n2 < cost_n4_n2) cost_n4_n2 = cost_n5_n2;
MapLocation loc_n5_n3 = rcCur.translate(-5, -3);
long cost_n5_n3 = heuristicCooldown(loc_n5_n3);
if(cost_n5_n3 < cost_n4_n2) cost_n4_n2 = cost_n5_n3;
if(cost_n5_n1 < cost_n4_n2) cost_n4_n2 = cost_n5_n1;
MapLocation loc_n4_n3 = rcCur.translate(-4, -3);
long cost_n4_n3 = heuristicCooldown(loc_n4_n3);
if(cost_n4_n3 < cost_n4_n2) cost_n4_n2 = cost_n4_n3;
cost_n4_n2 += smartCooldown(loc_n4_n2);
//best for [-4, -2]
if(cost_n4_n2 < cost_n4_n1) cost_n4_n1 = cost_n4_n2;
cost_n4_n1 += smartCooldown(loc_n4_n1);
//best for [-4, -1]
if(cost_n4_n1 < cost_n4_0) cost_n4_0 = cost_n4_n1;
MapLocation loc_n4_1 = rcCur.translate(-4, 1);
long cost_n4_1 = Long.MAX_VALUE;
if(cost_n5_1 < cost_n4_1) cost_n4_1 = cost_n5_1;
if(cost_n5_0 < cost_n4_1) cost_n4_1 = cost_n5_0;
MapLocation loc_n5_2 = rcCur.translate(-5, 2);
long cost_n5_2 = heuristicCooldown(loc_n5_2);
if(cost_n5_2 < cost_n4_1) cost_n4_1 = cost_n5_2;
MapLocation loc_n4_2 = rcCur.translate(-4, 2);
long cost_n4_2 = Long.MAX_VALUE;
if(cost_n5_2 < cost_n4_2) cost_n4_2 = cost_n5_2;
if(cost_n5_1 < cost_n4_2) cost_n4_2 = cost_n5_1;
MapLocation loc_n5_3 = rcCur.translate(-5, 3);
long cost_n5_3 = heuristicCooldown(loc_n5_3);
if(cost_n5_3 < cost_n4_2) cost_n4_2 = cost_n5_3;
MapLocation loc_n4_3 = rcCur.translate(-4, 3);
long cost_n4_3 = heuristicCooldown(loc_n4_3);
if(cost_n4_3 < cost_n4_2) cost_n4_2 = cost_n4_3;
cost_n4_2 += smartCooldown(loc_n4_2);
//best for [-4, 2]
if(cost_n4_2 < cost_n4_1) cost_n4_1 = cost_n4_2;
cost_n4_1 += smartCooldown(loc_n4_1);
//best for [-4, 1]
if(cost_n4_1 < cost_n4_0) cost_n4_0 = cost_n4_1;
cost_n4_0 += smartCooldown(loc_n4_0);
//best for [-4, 0]
if(cost_n4_0 < cost_n3_0) cost_n3_0 = cost_n4_0;
if(cost_n4_n1 < cost_n3_0) cost_n3_0 = cost_n4_n1;
if(cost_n4_1 < cost_n3_0) cost_n3_0 = cost_n4_1;
MapLocation loc_n3_n1 = rcCur.translate(-3, -1);
long cost_n3_n1 = Long.MAX_VALUE;
if(cost_n4_n1 < cost_n3_n1) cost_n3_n1 = cost_n4_n1;
if(cost_n4_n2 < cost_n3_n1) cost_n3_n1 = cost_n4_n2;
if(cost_n4_0 < cost_n3_n1) cost_n3_n1 = cost_n4_0;
MapLocation loc_n3_n2 = rcCur.translate(-3, -2);
long cost_n3_n2 = Long.MAX_VALUE;
if(cost_n4_n2 < cost_n3_n2) cost_n3_n2 = cost_n4_n2;
if(cost_n4_n3 < cost_n3_n2) cost_n3_n2 = cost_n4_n3;
if(cost_n4_n1 < cost_n3_n2) cost_n3_n2 = cost_n4_n1;
MapLocation loc_n3_n3 = rcCur.translate(-3, -3);
long cost_n3_n3 = Long.MAX_VALUE;
if(cost_n4_n3 < cost_n3_n3) cost_n3_n3 = cost_n4_n3;
MapLocation loc_n4_n4 = rcCur.translate(-4, -4);
long cost_n4_n4 = heuristicCooldown(loc_n4_n4);
if(cost_n4_n4 < cost_n3_n3) cost_n3_n3 = cost_n4_n4;
if(cost_n4_n2 < cost_n3_n3) cost_n3_n3 = cost_n4_n2;
MapLocation loc_n3_n4 = rcCur.translate(-3, -4);
long cost_n3_n4 = heuristicCooldown(loc_n3_n4);
if(cost_n3_n4 < cost_n3_n3) cost_n3_n3 = cost_n3_n4;
cost_n3_n3 += smartCooldown(loc_n3_n3);
//best for [-3, -3]
if(cost_n3_n3 < cost_n3_n2) cost_n3_n2 = cost_n3_n3;
cost_n3_n2 += smartCooldown(loc_n3_n2);
//best for [-3, -2]
if(cost_n3_n2 < cost_n3_n1) cost_n3_n1 = cost_n3_n2;
cost_n3_n1 += smartCooldown(loc_n3_n1);
//best for [-3, -1]
if(cost_n3_n1 < cost_n3_0) cost_n3_0 = cost_n3_n1;
MapLocation loc_n3_1 = rcCur.translate(-3, 1);
long cost_n3_1 = Long.MAX_VALUE;
if(cost_n4_1 < cost_n3_1) cost_n3_1 = cost_n4_1;
if(cost_n4_0 < cost_n3_1) cost_n3_1 = cost_n4_0;
if(cost_n4_2 < cost_n3_1) cost_n3_1 = cost_n4_2;
MapLocation loc_n3_2 = rcCur.translate(-3, 2);
long cost_n3_2 = Long.MAX_VALUE;
if(cost_n4_2 < cost_n3_2) cost_n3_2 = cost_n4_2;
if(cost_n4_1 < cost_n3_2) cost_n3_2 = cost_n4_1;
if(cost_n4_3 < cost_n3_2) cost_n3_2 = cost_n4_3;
MapLocation loc_n3_3 = rcCur.translate(-3, 3);
long cost_n3_3 = Long.MAX_VALUE;
if(cost_n4_3 < cost_n3_3) cost_n3_3 = cost_n4_3;
if(cost_n4_2 < cost_n3_3) cost_n3_3 = cost_n4_2;
MapLocation loc_n4_4 = rcCur.translate(-4, 4);
long cost_n4_4 = heuristicCooldown(loc_n4_4);
if(cost_n4_4 < cost_n3_3) cost_n3_3 = cost_n4_4;
MapLocation loc_n3_4 = rcCur.translate(-3, 4);
long cost_n3_4 = heuristicCooldown(loc_n3_4);
if(cost_n3_4 < cost_n3_3) cost_n3_3 = cost_n3_4;
cost_n3_3 += smartCooldown(loc_n3_3);
//best for [-3, 3]
if(cost_n3_3 < cost_n3_2) cost_n3_2 = cost_n3_3;
cost_n3_2 += smartCooldown(loc_n3_2);
//best for [-3, 2]
if(cost_n3_2 < cost_n3_1) cost_n3_1 = cost_n3_2;
cost_n3_1 += smartCooldown(loc_n3_1);
//best for [-3, 1]
if(cost_n3_1 < cost_n3_0) cost_n3_0 = cost_n3_1;
cost_n3_0 += smartCooldown(loc_n3_0);
//best for [-3, 0]
if(cost_n3_0 < cost_n2_0) cost_n2_0 = cost_n3_0;
if(cost_n3_n1 < cost_n2_0) cost_n2_0 = cost_n3_n1;
if(cost_n3_1 < cost_n2_0) cost_n2_0 = cost_n3_1;
MapLocation loc_n2_n1 = rcCur.translate(-2, -1);
long cost_n2_n1 = Long.MAX_VALUE;
if(cost_n3_n1 < cost_n2_n1) cost_n2_n1 = cost_n3_n1;
if(cost_n3_n2 < cost_n2_n1) cost_n2_n1 = cost_n3_n2;
if(cost_n3_0 < cost_n2_n1) cost_n2_n1 = cost_n3_0;
MapLocation loc_n2_n2 = rcCur.translate(-2, -2);
long cost_n2_n2 = Long.MAX_VALUE;
if(cost_n3_n2 < cost_n2_n2) cost_n2_n2 = cost_n3_n2;
if(cost_n3_n3 < cost_n2_n2) cost_n2_n2 = cost_n3_n3;
if(cost_n3_n1 < cost_n2_n2) cost_n2_n2 = cost_n3_n1;
MapLocation loc_n2_n3 = rcCur.translate(-2, -3);
long cost_n2_n3 = Long.MAX_VALUE;
if(cost_n3_n3 < cost_n2_n3) cost_n2_n3 = cost_n3_n3;
if(cost_n3_n4 < cost_n2_n3) cost_n2_n3 = cost_n3_n4;
if(cost_n3_n2 < cost_n2_n3) cost_n2_n3 = cost_n3_n2;
MapLocation loc_n2_n4 = rcCur.translate(-2, -4);
long cost_n2_n4 = Long.MAX_VALUE;
if(cost_n3_n4 < cost_n2_n4) cost_n2_n4 = cost_n3_n4;
MapLocation loc_n3_n5 = rcCur.translate(-3, -5);
long cost_n3_n5 = heuristicCooldown(loc_n3_n5);
if(cost_n3_n5 < cost_n2_n4) cost_n2_n4 = cost_n3_n5;
if(cost_n3_n3 < cost_n2_n4) cost_n2_n4 = cost_n3_n3;
MapLocation loc_n2_n5 = rcCur.translate(-2, -5);
long cost_n2_n5 = heuristicCooldown(loc_n2_n5);
if(cost_n2_n5 < cost_n2_n4) cost_n2_n4 = cost_n2_n5;
cost_n2_n4 += smartCooldown(loc_n2_n4);
//best for [-2, -4]
if(cost_n2_n4 < cost_n2_n3) cost_n2_n3 = cost_n2_n4;
cost_n2_n3 += smartCooldown(loc_n2_n3);
//best for [-2, -3]
if(cost_n2_n3 < cost_n2_n2) cost_n2_n2 = cost_n2_n3;
cost_n2_n2 += smartCooldown(loc_n2_n2);
//best for [-2, -2]
if(cost_n2_n2 < cost_n2_n1) cost_n2_n1 = cost_n2_n2;
cost_n2_n1 += smartCooldown(loc_n2_n1);
//best for [-2, -1]
if(cost_n2_n1 < cost_n2_0) cost_n2_0 = cost_n2_n1;
MapLocation loc_n2_1 = rcCur.translate(-2, 1);
long cost_n2_1 = Long.MAX_VALUE;
if(cost_n3_1 < cost_n2_1) cost_n2_1 = cost_n3_1;
if(cost_n3_0 < cost_n2_1) cost_n2_1 = cost_n3_0;
if(cost_n3_2 < cost_n2_1) cost_n2_1 = cost_n3_2;
MapLocation loc_n2_2 = rcCur.translate(-2, 2);
long cost_n2_2 = Long.MAX_VALUE;
if(cost_n3_2 < cost_n2_2) cost_n2_2 = cost_n3_2;
if(cost_n3_1 < cost_n2_2) cost_n2_2 = cost_n3_1;
if(cost_n3_3 < cost_n2_2) cost_n2_2 = cost_n3_3;
MapLocation loc_n2_3 = rcCur.translate(-2, 3);
long cost_n2_3 = Long.MAX_VALUE;
if(cost_n3_3 < cost_n2_3) cost_n2_3 = cost_n3_3;
if(cost_n3_2 < cost_n2_3) cost_n2_3 = cost_n3_2;
if(cost_n3_4 < cost_n2_3) cost_n2_3 = cost_n3_4;
MapLocation loc_n2_4 = rcCur.translate(-2, 4);
long cost_n2_4 = Long.MAX_VALUE;
if(cost_n3_4 < cost_n2_4) cost_n2_4 = cost_n3_4;
if(cost_n3_3 < cost_n2_4) cost_n2_4 = cost_n3_3;
MapLocation loc_n3_5 = rcCur.translate(-3, 5);
long cost_n3_5 = heuristicCooldown(loc_n3_5);
if(cost_n3_5 < cost_n2_4) cost_n2_4 = cost_n3_5;
MapLocation loc_n2_5 = rcCur.translate(-2, 5);
long cost_n2_5 = heuristicCooldown(loc_n2_5);
if(cost_n2_5 < cost_n2_4) cost_n2_4 = cost_n2_5;
cost_n2_4 += smartCooldown(loc_n2_4);
//best for [-2, 4]
if(cost_n2_4 < cost_n2_3) cost_n2_3 = cost_n2_4;
cost_n2_3 += smartCooldown(loc_n2_3);
//best for [-2, 3]
if(cost_n2_3 < cost_n2_2) cost_n2_2 = cost_n2_3;
cost_n2_2 += smartCooldown(loc_n2_2);
//best for [-2, 2]
if(cost_n2_2 < cost_n2_1) cost_n2_1 = cost_n2_2;
cost_n2_1 += smartCooldown(loc_n2_1);
//best for [-2, 1]
if(cost_n2_1 < cost_n2_0) cost_n2_0 = cost_n2_1;
cost_n2_0 += smartCooldown(loc_n2_0);
//best for [-2, 0]
if(cost_n2_0 < cost_n1_0) cost_n1_0 = cost_n2_0;
if(cost_n2_n1 < cost_n1_0) cost_n1_0 = cost_n2_n1;
if(cost_n2_1 < cost_n1_0) cost_n1_0 = cost_n2_1;
MapLocation loc_n1_n1 = rcCur.translate(-1, -1);
long cost_n1_n1 = Long.MAX_VALUE;
if(cost_n2_n1 < cost_n1_n1) cost_n1_n1 = cost_n2_n1;
if(cost_n2_n2 < cost_n1_n1) cost_n1_n1 = cost_n2_n2;
if(cost_n2_0 < cost_n1_n1) cost_n1_n1 = cost_n2_0;
MapLocation loc_n1_n2 = rcCur.translate(-1, -2);
long cost_n1_n2 = Long.MAX_VALUE;
if(cost_n2_n2 < cost_n1_n2) cost_n1_n2 = cost_n2_n2;
if(cost_n2_n3 < cost_n1_n2) cost_n1_n2 = cost_n2_n3;
if(cost_n2_n1 < cost_n1_n2) cost_n1_n2 = cost_n2_n1;
MapLocation loc_n1_n3 = rcCur.translate(-1, -3);
long cost_n1_n3 = Long.MAX_VALUE;
if(cost_n2_n3 < cost_n1_n3) cost_n1_n3 = cost_n2_n3;
if(cost_n2_n4 < cost_n1_n3) cost_n1_n3 = cost_n2_n4;
if(cost_n2_n2 < cost_n1_n3) cost_n1_n3 = cost_n2_n2;
MapLocation loc_n1_n4 = rcCur.translate(-1, -4);
long cost_n1_n4 = Long.MAX_VALUE;
if(cost_n2_n4 < cost_n1_n4) cost_n1_n4 = cost_n2_n4;
if(cost_n2_n5 < cost_n1_n4) cost_n1_n4 = cost_n2_n5;
if(cost_n2_n3 < cost_n1_n4) cost_n1_n4 = cost_n2_n3;
MapLocation loc_n1_n5 = rcCur.translate(-1, -5);
long cost_n1_n5 = heuristicCooldown(loc_n1_n5);
if(cost_n1_n5 < cost_n1_n4) cost_n1_n4 = cost_n1_n5;
cost_n1_n4 += smartCooldown(loc_n1_n4);
//best for [-1, -4]
if(cost_n1_n4 < cost_n1_n3) cost_n1_n3 = cost_n1_n4;
cost_n1_n3 += smartCooldown(loc_n1_n3);
//best for [-1, -3]
if(cost_n1_n3 < cost_n1_n2) cost_n1_n2 = cost_n1_n3;
cost_n1_n2 += smartCooldown(loc_n1_n2);
//best for [-1, -2]
if(cost_n1_n2 < cost_n1_n1) cost_n1_n1 = cost_n1_n2;
cost_n1_n1 += smartCooldown(loc_n1_n1);
//best for [-1, -1]
if(cost_n1_n1 < cost_n1_0) cost_n1_0 = cost_n1_n1;
MapLocation loc_n1_1 = rcCur.translate(-1, 1);
long cost_n1_1 = Long.MAX_VALUE;
if(cost_n2_1 < cost_n1_1) cost_n1_1 = cost_n2_1;
if(cost_n2_0 < cost_n1_1) cost_n1_1 = cost_n2_0;
if(cost_n2_2 < cost_n1_1) cost_n1_1 = cost_n2_2;
MapLocation loc_n1_2 = rcCur.translate(-1, 2);
long cost_n1_2 = Long.MAX_VALUE;
if(cost_n2_2 < cost_n1_2) cost_n1_2 = cost_n2_2;
if(cost_n2_1 < cost_n1_2) cost_n1_2 = cost_n2_1;
if(cost_n2_3 < cost_n1_2) cost_n1_2 = cost_n2_3;
MapLocation loc_n1_3 = rcCur.translate(-1, 3);
long cost_n1_3 = Long.MAX_VALUE;
if(cost_n2_3 < cost_n1_3) cost_n1_3 = cost_n2_3;
if(cost_n2_2 < cost_n1_3) cost_n1_3 = cost_n2_2;
if(cost_n2_4 < cost_n1_3) cost_n1_3 = cost_n2_4;
MapLocation loc_n1_4 = rcCur.translate(-1, 4);
long cost_n1_4 = Long.MAX_VALUE;
if(cost_n2_4 < cost_n1_4) cost_n1_4 = cost_n2_4;
if(cost_n2_3 < cost_n1_4) cost_n1_4 = cost_n2_3;
if(cost_n2_5 < cost_n1_4) cost_n1_4 = cost_n2_5;
MapLocation loc_n1_5 = rcCur.translate(-1, 5);
long cost_n1_5 = heuristicCooldown(loc_n1_5);
if(cost_n1_5 < cost_n1_4) cost_n1_4 = cost_n1_5;
cost_n1_4 += smartCooldown(loc_n1_4);
//best for [-1, 4]
if(cost_n1_4 < cost_n1_3) cost_n1_3 = cost_n1_4;
cost_n1_3 += smartCooldown(loc_n1_3);
//best for [-1, 3]
if(cost_n1_3 < cost_n1_2) cost_n1_2 = cost_n1_3;
cost_n1_2 += smartCooldown(loc_n1_2);
//best for [-1, 2]
if(cost_n1_2 < cost_n1_1) cost_n1_1 = cost_n1_2;
cost_n1_1 += smartCooldown(loc_n1_1);
//best for [-1, 1]
if(cost_n1_1 < cost_n1_0) cost_n1_0 = cost_n1_1;
cost_n1_0 += smartCooldown(loc_n1_0);
//best for [-1, 0]
if(cost_n1_0 < cost_0_0) { cost_0_0 = cost_n1_0;best_dir = Direction.WEST;}
if(cost_n1_n1 < cost_0_0) { cost_0_0 = cost_n1_n1;best_dir = Direction.SOUTHWEST;}
if(cost_n1_1 < cost_0_0) { cost_0_0 = cost_n1_1;best_dir = Direction.NORTHWEST;}
MapLocation loc_0_n1 = rcCur.translate(0, -1);
long cost_0_n1 = Long.MAX_VALUE;
if(cost_n1_n1 < cost_0_n1) cost_0_n1 = cost_n1_n1;
if(cost_n1_n2 < cost_0_n1) cost_0_n1 = cost_n1_n2;
if(cost_n1_0 < cost_0_n1) cost_0_n1 = cost_n1_0;
MapLocation loc_0_n2 = rcCur.translate(0, -2);
long cost_0_n2 = Long.MAX_VALUE;
if(cost_n1_n2 < cost_0_n2) cost_0_n2 = cost_n1_n2;
if(cost_n1_n3 < cost_0_n2) cost_0_n2 = cost_n1_n3;
if(cost_n1_n1 < cost_0_n2) cost_0_n2 = cost_n1_n1;
MapLocation loc_0_n3 = rcCur.translate(0, -3);
long cost_0_n3 = Long.MAX_VALUE;
if(cost_n1_n3 < cost_0_n3) cost_0_n3 = cost_n1_n3;
if(cost_n1_n4 < cost_0_n3) cost_0_n3 = cost_n1_n4;
if(cost_n1_n2 < cost_0_n3) cost_0_n3 = cost_n1_n2;
MapLocation loc_0_n4 = rcCur.translate(0, -4);
long cost_0_n4 = Long.MAX_VALUE;
if(cost_n1_n4 < cost_0_n4) cost_0_n4 = cost_n1_n4;
if(cost_n1_n5 < cost_0_n4) cost_0_n4 = cost_n1_n5;
if(cost_n1_n3 < cost_0_n4) cost_0_n4 = cost_n1_n3;
MapLocation loc_0_n5 = rcCur.translate(0, -5);
long cost_0_n5 = heuristicCooldown(loc_0_n5);
if(cost_0_n5 < cost_0_n4) cost_0_n4 = cost_0_n5;
cost_0_n4 += smartCooldown(loc_0_n4);
//best for [0, -4]
if(cost_0_n4 < cost_0_n3) cost_0_n3 = cost_0_n4;
cost_0_n3 += smartCooldown(loc_0_n3);
//best for [0, -3]
if(cost_0_n3 < cost_0_n2) cost_0_n2 = cost_0_n3;
cost_0_n2 += smartCooldown(loc_0_n2);
//best for [0, -2]
if(cost_0_n2 < cost_0_n1) cost_0_n1 = cost_0_n2;
cost_0_n1 += smartCooldown(loc_0_n1);
//best for [0, -1]
if(cost_0_n1 < cost_0_0) { cost_0_0 = cost_0_n1;best_dir = Direction.SOUTH;}
MapLocation loc_0_1 = rcCur.translate(0, 1);
long cost_0_1 = Long.MAX_VALUE;
if(cost_n1_1 < cost_0_1) cost_0_1 = cost_n1_1;
if(cost_n1_0 < cost_0_1) cost_0_1 = cost_n1_0;
if(cost_n1_2 < cost_0_1) cost_0_1 = cost_n1_2;
MapLocation loc_0_2 = rcCur.translate(0, 2);
long cost_0_2 = Long.MAX_VALUE;
if(cost_n1_2 < cost_0_2) cost_0_2 = cost_n1_2;
if(cost_n1_1 < cost_0_2) cost_0_2 = cost_n1_1;
if(cost_n1_3 < cost_0_2) cost_0_2 = cost_n1_3;
MapLocation loc_0_3 = rcCur.translate(0, 3);
long cost_0_3 = Long.MAX_VALUE;
if(cost_n1_3 < cost_0_3) cost_0_3 = cost_n1_3;
if(cost_n1_2 < cost_0_3) cost_0_3 = cost_n1_2;
if(cost_n1_4 < cost_0_3) cost_0_3 = cost_n1_4;
MapLocation loc_0_4 = rcCur.translate(0, 4);
long cost_0_4 = Long.MAX_VALUE;
if(cost_n1_4 < cost_0_4) cost_0_4 = cost_n1_4;
if(cost_n1_3 < cost_0_4) cost_0_4 = cost_n1_3;
if(cost_n1_5 < cost_0_4) cost_0_4 = cost_n1_5;
MapLocation loc_0_5 = rcCur.translate(0, 5);
long cost_0_5 = heuristicCooldown(loc_0_5);
if(cost_0_5 < cost_0_4) cost_0_4 = cost_0_5;
cost_0_4 += smartCooldown(loc_0_4);
//best for [0, 4]
if(cost_0_4 < cost_0_3) cost_0_3 = cost_0_4;
cost_0_3 += smartCooldown(loc_0_3);
//best for [0, 3]
if(cost_0_3 < cost_0_2) cost_0_2 = cost_0_3;
cost_0_2 += smartCooldown(loc_0_2);
//best for [0, 2]
if(cost_0_2 < cost_0_1) cost_0_1 = cost_0_2;
cost_0_1 += smartCooldown(loc_0_1);
//best for [0, 1]
if(cost_0_1 < cost_0_0) { cost_0_0 = cost_0_1;best_dir = Direction.NORTH;}
cost_0_0 += smartCooldown(loc_0_0);
//best for [0, 0]
return best_dir;
}
static Direction pathfind_NORTHWEST() throws GameActionException {
MapLocation loc_0_0 = rcCur.translate(0, 0);
long cost_0_0 = Long.MAX_VALUE;
Direction best_dir = null;
MapLocation loc_n1_1 = rcCur.translate(-1, 1);
long cost_n1_1 = Long.MAX_VALUE;
MapLocation loc_n2_2 = rcCur.translate(-2, 2);
long cost_n2_2 = Long.MAX_VALUE;
MapLocation loc_n3_3 = rcCur.translate(-3, 3);
long cost_n3_3 = Long.MAX_VALUE;
MapLocation loc_n4_4 = rcCur.translate(-4, 4);
long cost_n4_4 = heuristicCooldown(loc_n4_4);
if(cost_n4_4 < cost_n3_3) cost_n3_3 = cost_n4_4;
MapLocation loc_n4_3 = rcCur.translate(-4, 3);
long cost_n4_3 = heuristicCooldown(loc_n4_3);
if(cost_n4_3 < cost_n3_3) cost_n3_3 = cost_n4_3;
MapLocation loc_n3_4 = rcCur.translate(-3, 4);
long cost_n3_4 = heuristicCooldown(loc_n3_4);
if(cost_n3_4 < cost_n3_3) cost_n3_3 = cost_n3_4;
MapLocation loc_n4_2 = rcCur.translate(-4, 2);
long cost_n4_2 = Long.MAX_VALUE;
MapLocation loc_n5_3 = rcCur.translate(-5, 3);
long cost_n5_3 = heuristicCooldown(loc_n5_3);
if(cost_n5_3 < cost_n4_2) cost_n4_2 = cost_n5_3;
MapLocation loc_n5_2 = rcCur.translate(-5, 2);
long cost_n5_2 = heuristicCooldown(loc_n5_2);
if(cost_n5_2 < cost_n4_2) cost_n4_2 = cost_n5_2;
if(cost_n4_3 < cost_n4_2) cost_n4_2 = cost_n4_3;
MapLocation loc_n5_1 = rcCur.translate(-5, 1);
long cost_n5_1 = heuristicCooldown(loc_n5_1);
if(cost_n5_1 < cost_n4_2) cost_n4_2 = cost_n5_1;
cost_n4_2 += smartCooldown(loc_n4_2);
//best for [-4, 2]
if(cost_n4_2 < cost_n3_3) cost_n3_3 = cost_n4_2;
MapLocation loc_n2_4 = rcCur.translate(-2, 4);
long cost_n2_4 = Long.MAX_VALUE;
MapLocation loc_n3_5 = rcCur.translate(-3, 5);
long cost_n3_5 = heuristicCooldown(loc_n3_5);
if(cost_n3_5 < cost_n2_4) cost_n2_4 = cost_n3_5;
if(cost_n3_4 < cost_n2_4) cost_n2_4 = cost_n3_4;
MapLocation loc_n2_5 = rcCur.translate(-2, 5);
long cost_n2_5 = heuristicCooldown(loc_n2_5);
if(cost_n2_5 < cost_n2_4) cost_n2_4 = cost_n2_5;
MapLocation loc_n1_5 = rcCur.translate(-1, 5);
long cost_n1_5 = heuristicCooldown(loc_n1_5);
if(cost_n1_5 < cost_n2_4) cost_n2_4 = cost_n1_5;
cost_n2_4 += smartCooldown(loc_n2_4);
//best for [-2, 4]
if(cost_n2_4 < cost_n3_3) cost_n3_3 = cost_n2_4;
cost_n3_3 += smartCooldown(loc_n3_3);
//best for [-3, 3]
if(cost_n3_3 < cost_n2_2) cost_n2_2 = cost_n3_3;
MapLocation loc_n3_2 = rcCur.translate(-3, 2);
long cost_n3_2 = Long.MAX_VALUE;
if(cost_n4_3 < cost_n3_2) cost_n3_2 = cost_n4_3;
if(cost_n4_2 < cost_n3_2) cost_n3_2 = cost_n4_2;
if(cost_n3_3 < cost_n3_2) cost_n3_2 = cost_n3_3;
MapLocation loc_n4_1 = rcCur.translate(-4, 1);
long cost_n4_1 = Long.MAX_VALUE;
if(cost_n5_2 < cost_n4_1) cost_n4_1 = cost_n5_2;
if(cost_n5_1 < cost_n4_1) cost_n4_1 = cost_n5_1;
if(cost_n4_2 < cost_n4_1) cost_n4_1 = cost_n4_2;
MapLocation loc_n5_0 = rcCur.translate(-5, 0);
long cost_n5_0 = heuristicCooldown(loc_n5_0);
if(cost_n5_0 < cost_n4_1) cost_n4_1 = cost_n5_0;
cost_n4_1 += smartCooldown(loc_n4_1);
//best for [-4, 1]
if(cost_n4_1 < cost_n3_2) cost_n3_2 = cost_n4_1;
MapLocation loc_n2_3 = rcCur.translate(-2, 3);
long cost_n2_3 = Long.MAX_VALUE;
if(cost_n3_4 < cost_n2_3) cost_n2_3 = cost_n3_4;
if(cost_n3_3 < cost_n2_3) cost_n2_3 = cost_n3_3;
if(cost_n2_4 < cost_n2_3) cost_n2_3 = cost_n2_4;
MapLocation loc_n1_4 = rcCur.translate(-1, 4);
long cost_n1_4 = Long.MAX_VALUE;
if(cost_n2_5 < cost_n1_4) cost_n1_4 = cost_n2_5;
if(cost_n2_4 < cost_n1_4) cost_n1_4 = cost_n2_4;
if(cost_n1_5 < cost_n1_4) cost_n1_4 = cost_n1_5;
MapLocation loc_0_5 = rcCur.translate(0, 5);
long cost_0_5 = heuristicCooldown(loc_0_5);
if(cost_0_5 < cost_n1_4) cost_n1_4 = cost_0_5;
cost_n1_4 += smartCooldown(loc_n1_4);
//best for [-1, 4]
if(cost_n1_4 < cost_n2_3) cost_n2_3 = cost_n1_4;
cost_n2_3 += smartCooldown(loc_n2_3);
//best for [-2, 3]
if(cost_n2_3 < cost_n3_2) cost_n3_2 = cost_n2_3;
cost_n3_2 += smartCooldown(loc_n3_2);
//best for [-3, 2]
if(cost_n3_2 < cost_n2_2) cost_n2_2 = cost_n3_2;
if(cost_n2_3 < cost_n2_2) cost_n2_2 = cost_n2_3;
MapLocation loc_n3_1 = rcCur.translate(-3, 1);
long cost_n3_1 = Long.MAX_VALUE;
if(cost_n4_2 < cost_n3_1) cost_n3_1 = cost_n4_2;
if(cost_n4_1 < cost_n3_1) cost_n3_1 = cost_n4_1;
if(cost_n3_2 < cost_n3_1) cost_n3_1 = cost_n3_2;
MapLocation loc_n4_0 = rcCur.translate(-4, 0);
long cost_n4_0 = Long.MAX_VALUE;
if(cost_n5_1 < cost_n4_0) cost_n4_0 = cost_n5_1;
if(cost_n5_0 < cost_n4_0) cost_n4_0 = cost_n5_0;
if(cost_n4_1 < cost_n4_0) cost_n4_0 = cost_n4_1;
MapLocation loc_n5_n1 = rcCur.translate(-5, -1);
long cost_n5_n1 = heuristicCooldown(loc_n5_n1);
if(cost_n5_n1 < cost_n4_0) cost_n4_0 = cost_n5_n1;
cost_n4_0 += smartCooldown(loc_n4_0);
//best for [-4, 0]
if(cost_n4_0 < cost_n3_1) cost_n3_1 = cost_n4_0;
cost_n3_1 += smartCooldown(loc_n3_1);
//best for [-3, 1]
if(cost_n3_1 < cost_n2_2) cost_n2_2 = cost_n3_1;
MapLocation loc_n1_3 = rcCur.translate(-1, 3);
long cost_n1_3 = Long.MAX_VALUE;
if(cost_n2_4 < cost_n1_3) cost_n1_3 = cost_n2_4;
if(cost_n2_3 < cost_n1_3) cost_n1_3 = cost_n2_3;
if(cost_n1_4 < cost_n1_3) cost_n1_3 = cost_n1_4;
MapLocation loc_0_4 = rcCur.translate(0, 4);
long cost_0_4 = Long.MAX_VALUE;
if(cost_n1_5 < cost_0_4) cost_0_4 = cost_n1_5;
if(cost_n1_4 < cost_0_4) cost_0_4 = cost_n1_4;
if(cost_0_5 < cost_0_4) cost_0_4 = cost_0_5;
MapLocation loc_1_5 = rcCur.translate(1, 5);
long cost_1_5 = heuristicCooldown(loc_1_5);
if(cost_1_5 < cost_0_4) cost_0_4 = cost_1_5;
cost_0_4 += smartCooldown(loc_0_4);
//best for [0, 4]
if(cost_0_4 < cost_n1_3) cost_n1_3 = cost_0_4;
cost_n1_3 += smartCooldown(loc_n1_3);
//best for [-1, 3]
if(cost_n1_3 < cost_n2_2) cost_n2_2 = cost_n1_3;
cost_n2_2 += smartCooldown(loc_n2_2);
//best for [-2, 2]
if(cost_n2_2 < cost_n1_1) cost_n1_1 = cost_n2_2;
MapLocation loc_n2_1 = rcCur.translate(-2, 1);
long cost_n2_1 = Long.MAX_VALUE;
if(cost_n3_2 < cost_n2_1) cost_n2_1 = cost_n3_2;
if(cost_n3_1 < cost_n2_1) cost_n2_1 = cost_n3_1;
if(cost_n2_2 < cost_n2_1) cost_n2_1 = cost_n2_2;
MapLocation loc_n3_0 = rcCur.translate(-3, 0);
long cost_n3_0 = Long.MAX_VALUE;
if(cost_n4_1 < cost_n3_0) cost_n3_0 = cost_n4_1;
if(cost_n4_0 < cost_n3_0) cost_n3_0 = cost_n4_0;
if(cost_n3_1 < cost_n3_0) cost_n3_0 = cost_n3_1;
MapLocation loc_n4_n1 = rcCur.translate(-4, -1);
long cost_n4_n1 = Long.MAX_VALUE;
if(cost_n5_0 < cost_n4_n1) cost_n4_n1 = cost_n5_0;
if(cost_n5_n1 < cost_n4_n1) cost_n4_n1 = cost_n5_n1;
if(cost_n4_0 < cost_n4_n1) cost_n4_n1 = cost_n4_0;
MapLocation loc_n5_n2 = rcCur.translate(-5, -2);
long cost_n5_n2 = heuristicCooldown(loc_n5_n2);
if(cost_n5_n2 < cost_n4_n1) cost_n4_n1 = cost_n5_n2;
cost_n4_n1 += smartCooldown(loc_n4_n1);
//best for [-4, -1]
if(cost_n4_n1 < cost_n3_0) cost_n3_0 = cost_n4_n1;
cost_n3_0 += smartCooldown(loc_n3_0);
//best for [-3, 0]
if(cost_n3_0 < cost_n2_1) cost_n2_1 = cost_n3_0;
MapLocation loc_n1_2 = rcCur.translate(-1, 2);
long cost_n1_2 = Long.MAX_VALUE;
if(cost_n2_3 < cost_n1_2) cost_n1_2 = cost_n2_3;
if(cost_n2_2 < cost_n1_2) cost_n1_2 = cost_n2_2;
if(cost_n1_3 < cost_n1_2) cost_n1_2 = cost_n1_3;
MapLocation loc_0_3 = rcCur.translate(0, 3);
long cost_0_3 = Long.MAX_VALUE;
if(cost_n1_4 < cost_0_3) cost_0_3 = cost_n1_4;
if(cost_n1_3 < cost_0_3) cost_0_3 = cost_n1_3;
if(cost_0_4 < cost_0_3) cost_0_3 = cost_0_4;
MapLocation loc_1_4 = rcCur.translate(1, 4);
long cost_1_4 = Long.MAX_VALUE;
if(cost_0_5 < cost_1_4) cost_1_4 = cost_0_5;
if(cost_0_4 < cost_1_4) cost_1_4 = cost_0_4;
if(cost_1_5 < cost_1_4) cost_1_4 = cost_1_5;
MapLocation loc_2_5 = rcCur.translate(2, 5);
long cost_2_5 = heuristicCooldown(loc_2_5);
if(cost_2_5 < cost_1_4) cost_1_4 = cost_2_5;
cost_1_4 += smartCooldown(loc_1_4);
//best for [1, 4]
if(cost_1_4 < cost_0_3) cost_0_3 = cost_1_4;
cost_0_3 += smartCooldown(loc_0_3);
//best for [0, 3]
if(cost_0_3 < cost_n1_2) cost_n1_2 = cost_0_3;
cost_n1_2 += smartCooldown(loc_n1_2);
//best for [-1, 2]
if(cost_n1_2 < cost_n2_1) cost_n2_1 = cost_n1_2;
cost_n2_1 += smartCooldown(loc_n2_1);
//best for [-2, 1]
if(cost_n2_1 < cost_n1_1) cost_n1_1 = cost_n2_1;
if(cost_n1_2 < cost_n1_1) cost_n1_1 = cost_n1_2;
MapLocation loc_n2_0 = rcCur.translate(-2, 0);
long cost_n2_0 = Long.MAX_VALUE;
if(cost_n3_1 < cost_n2_0) cost_n2_0 = cost_n3_1;
if(cost_n3_0 < cost_n2_0) cost_n2_0 = cost_n3_0;
if(cost_n2_1 < cost_n2_0) cost_n2_0 = cost_n2_1;
MapLocation loc_n3_n1 = rcCur.translate(-3, -1);
long cost_n3_n1 = Long.MAX_VALUE;
if(cost_n4_0 < cost_n3_n1) cost_n3_n1 = cost_n4_0;
if(cost_n4_n1 < cost_n3_n1) cost_n3_n1 = cost_n4_n1;
if(cost_n3_0 < cost_n3_n1) cost_n3_n1 = cost_n3_0;
MapLocation loc_n4_n2 = rcCur.translate(-4, -2);
long cost_n4_n2 = Long.MAX_VALUE;
if(cost_n5_n1 < cost_n4_n2) cost_n4_n2 = cost_n5_n1;
if(cost_n5_n2 < cost_n4_n2) cost_n4_n2 = cost_n5_n2;
if(cost_n4_n1 < cost_n4_n2) cost_n4_n2 = cost_n4_n1;
MapLocation loc_n5_n3 = rcCur.translate(-5, -3);
long cost_n5_n3 = heuristicCooldown(loc_n5_n3);
if(cost_n5_n3 < cost_n4_n2) cost_n4_n2 = cost_n5_n3;
cost_n4_n2 += smartCooldown(loc_n4_n2);
//best for [-4, -2]
if(cost_n4_n2 < cost_n3_n1) cost_n3_n1 = cost_n4_n2;
cost_n3_n1 += smartCooldown(loc_n3_n1);
//best for [-3, -1]
if(cost_n3_n1 < cost_n2_0) cost_n2_0 = cost_n3_n1;
cost_n2_0 += smartCooldown(loc_n2_0);
//best for [-2, 0]
if(cost_n2_0 < cost_n1_1) cost_n1_1 = cost_n2_0;
MapLocation loc_0_2 = rcCur.translate(0, 2);
long cost_0_2 = Long.MAX_VALUE;
if(cost_n1_3 < cost_0_2) cost_0_2 = cost_n1_3;
if(cost_n1_2 < cost_0_2) cost_0_2 = cost_n1_2;
if(cost_0_3 < cost_0_2) cost_0_2 = cost_0_3;
MapLocation loc_1_3 = rcCur.translate(1, 3);
long cost_1_3 = Long.MAX_VALUE;
if(cost_0_4 < cost_1_3) cost_1_3 = cost_0_4;
if(cost_0_3 < cost_1_3) cost_1_3 = cost_0_3;
if(cost_1_4 < cost_1_3) cost_1_3 = cost_1_4;
MapLocation loc_2_4 = rcCur.translate(2, 4);
long cost_2_4 = Long.MAX_VALUE;
if(cost_1_5 < cost_2_4) cost_2_4 = cost_1_5;
if(cost_1_4 < cost_2_4) cost_2_4 = cost_1_4;
if(cost_2_5 < cost_2_4) cost_2_4 = cost_2_5;
MapLocation loc_3_5 = rcCur.translate(3, 5);
long cost_3_5 = heuristicCooldown(loc_3_5);
if(cost_3_5 < cost_2_4) cost_2_4 = cost_3_5;
cost_2_4 += smartCooldown(loc_2_4);
//best for [2, 4]
if(cost_2_4 < cost_1_3) cost_1_3 = cost_2_4;
cost_1_3 += smartCooldown(loc_1_3);
//best for [1, 3]
if(cost_1_3 < cost_0_2) cost_0_2 = cost_1_3;
cost_0_2 += smartCooldown(loc_0_2);
//best for [0, 2]
if(cost_0_2 < cost_n1_1) cost_n1_1 = cost_0_2;
cost_n1_1 += smartCooldown(loc_n1_1);
//best for [-1, 1]
if(cost_n1_1 < cost_0_0) { cost_0_0 = cost_n1_1;best_dir = Direction.NORTHWEST;}
MapLocation loc_n1_0 = rcCur.translate(-1, 0);
long cost_n1_0 = Long.MAX_VALUE;
if(cost_n2_1 < cost_n1_0) cost_n1_0 = cost_n2_1;
if(cost_n2_0 < cost_n1_0) cost_n1_0 = cost_n2_0;
if(cost_n1_1 < cost_n1_0) cost_n1_0 = cost_n1_1;
MapLocation loc_n2_n1 = rcCur.translate(-2, -1);
long cost_n2_n1 = Long.MAX_VALUE;
if(cost_n3_0 < cost_n2_n1) cost_n2_n1 = cost_n3_0;
if(cost_n3_n1 < cost_n2_n1) cost_n2_n1 = cost_n3_n1;
if(cost_n2_0 < cost_n2_n1) cost_n2_n1 = cost_n2_0;
MapLocation loc_n3_n2 = rcCur.translate(-3, -2);
long cost_n3_n2 = Long.MAX_VALUE;
if(cost_n4_n1 < cost_n3_n2) cost_n3_n2 = cost_n4_n1;
if(cost_n4_n2 < cost_n3_n2) cost_n3_n2 = cost_n4_n2;
if(cost_n3_n1 < cost_n3_n2) cost_n3_n2 = cost_n3_n1;
MapLocation loc_n4_n3 = rcCur.translate(-4, -3);
long cost_n4_n3 = heuristicCooldown(loc_n4_n3);
if(cost_n4_n3 < cost_n3_n2) cost_n3_n2 = cost_n4_n3;
cost_n3_n2 += smartCooldown(loc_n3_n2);
//best for [-3, -2]
if(cost_n3_n2 < cost_n2_n1) cost_n2_n1 = cost_n3_n2;
cost_n2_n1 += smartCooldown(loc_n2_n1);
//best for [-2, -1]
if(cost_n2_n1 < cost_n1_0) cost_n1_0 = cost_n2_n1;
MapLocation loc_0_1 = rcCur.translate(0, 1);
long cost_0_1 = Long.MAX_VALUE;
if(cost_n1_2 < cost_0_1) cost_0_1 = cost_n1_2;
if(cost_n1_1 < cost_0_1) cost_0_1 = cost_n1_1;
if(cost_0_2 < cost_0_1) cost_0_1 = cost_0_2;
MapLocation loc_1_2 = rcCur.translate(1, 2);
long cost_1_2 = Long.MAX_VALUE;
if(cost_0_3 < cost_1_2) cost_1_2 = cost_0_3;
if(cost_0_2 < cost_1_2) cost_1_2 = cost_0_2;
if(cost_1_3 < cost_1_2) cost_1_2 = cost_1_3;
MapLocation loc_2_3 = rcCur.translate(2, 3);
long cost_2_3 = Long.MAX_VALUE;
if(cost_1_4 < cost_2_3) cost_2_3 = cost_1_4;
if(cost_1_3 < cost_2_3) cost_2_3 = cost_1_3;
if(cost_2_4 < cost_2_3) cost_2_3 = cost_2_4;
MapLocation loc_3_4 = rcCur.translate(3, 4);
long cost_3_4 = heuristicCooldown(loc_3_4);
if(cost_3_4 < cost_2_3) cost_2_3 = cost_3_4;
cost_2_3 += smartCooldown(loc_2_3);
//best for [2, 3]
if(cost_2_3 < cost_1_2) cost_1_2 = cost_2_3;
cost_1_2 += smartCooldown(loc_1_2);
//best for [1, 2]
if(cost_1_2 < cost_0_1) cost_0_1 = cost_1_2;
cost_0_1 += smartCooldown(loc_0_1);
//best for [0, 1]
if(cost_0_1 < cost_n1_0) cost_n1_0 = cost_0_1;
cost_n1_0 += smartCooldown(loc_n1_0);
//best for [-1, 0]
if(cost_n1_0 < cost_0_0) { cost_0_0 = cost_n1_0;best_dir = Direction.WEST;}
if(cost_0_1 < cost_0_0) { cost_0_0 = cost_0_1;best_dir = Direction.NORTH;}
MapLocation loc_n1_n1 = rcCur.translate(-1, -1);
long cost_n1_n1 = Long.MAX_VALUE;
if(cost_n2_0 < cost_n1_n1) cost_n1_n1 = cost_n2_0;
if(cost_n2_n1 < cost_n1_n1) cost_n1_n1 = cost_n2_n1;
if(cost_n1_0 < cost_n1_n1) cost_n1_n1 = cost_n1_0;
MapLocation loc_n2_n2 = rcCur.translate(-2, -2);
long cost_n2_n2 = Long.MAX_VALUE;
if(cost_n3_n1 < cost_n2_n2) cost_n2_n2 = cost_n3_n1;
if(cost_n3_n2 < cost_n2_n2) cost_n2_n2 = cost_n3_n2;
if(cost_n2_n1 < cost_n2_n2) cost_n2_n2 = cost_n2_n1;
MapLocation loc_n3_n3 = rcCur.translate(-3, -3);
long cost_n3_n3 = Long.MAX_VALUE;
if(cost_n4_n2 < cost_n3_n3) cost_n3_n3 = cost_n4_n2;
if(cost_n4_n3 < cost_n3_n3) cost_n3_n3 = cost_n4_n3;
if(cost_n3_n2 < cost_n3_n3) cost_n3_n3 = cost_n3_n2;
MapLocation loc_n4_n4 = rcCur.translate(-4, -4);
long cost_n4_n4 = heuristicCooldown(loc_n4_n4);
if(cost_n4_n4 < cost_n3_n3) cost_n3_n3 = cost_n4_n4;
cost_n3_n3 += smartCooldown(loc_n3_n3);
//best for [-3, -3]
if(cost_n3_n3 < cost_n2_n2) cost_n2_n2 = cost_n3_n3;
cost_n2_n2 += smartCooldown(loc_n2_n2);
//best for [-2, -2]
if(cost_n2_n2 < cost_n1_n1) cost_n1_n1 = cost_n2_n2;
cost_n1_n1 += smartCooldown(loc_n1_n1);
//best for [-1, -1]
if(cost_n1_n1 < cost_0_0) { cost_0_0 = cost_n1_n1;best_dir = Direction.SOUTHWEST;}
MapLocation loc_1_1 = rcCur.translate(1, 1);
long cost_1_1 = Long.MAX_VALUE;
if(cost_0_2 < cost_1_1) cost_1_1 = cost_0_2;
if(cost_0_1 < cost_1_1) cost_1_1 = cost_0_1;
if(cost_1_2 < cost_1_1) cost_1_1 = cost_1_2;
MapLocation loc_2_2 = rcCur.translate(2, 2);
long cost_2_2 = Long.MAX_VALUE;
if(cost_1_3 < cost_2_2) cost_2_2 = cost_1_3;
if(cost_1_2 < cost_2_2) cost_2_2 = cost_1_2;
if(cost_2_3 < cost_2_2) cost_2_2 = cost_2_3;
MapLocation loc_3_3 = rcCur.translate(3, 3);
long cost_3_3 = Long.MAX_VALUE;
if(cost_2_4 < cost_3_3) cost_3_3 = cost_2_4;
if(cost_2_3 < cost_3_3) cost_3_3 = cost_2_3;
if(cost_3_4 < cost_3_3) cost_3_3 = cost_3_4;
MapLocation loc_4_4 = rcCur.translate(4, 4);
long cost_4_4 = heuristicCooldown(loc_4_4);
if(cost_4_4 < cost_3_3) cost_3_3 = cost_4_4;
cost_3_3 += smartCooldown(loc_3_3);
//best for [3, 3]
if(cost_3_3 < cost_2_2) cost_2_2 = cost_3_3;
cost_2_2 += smartCooldown(loc_2_2);
//best for [2, 2]
if(cost_2_2 < cost_1_1) cost_1_1 = cost_2_2;
cost_1_1 += smartCooldown(loc_1_1);
//best for [1, 1]
if(cost_1_1 < cost_0_0) { cost_0_0 = cost_1_1;best_dir = Direction.NORTHEAST;}
cost_0_0 += smartCooldown(loc_0_0);
//best for [0, 0]
return best_dir;
}
}
