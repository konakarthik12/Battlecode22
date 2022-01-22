package pathfinder;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class UnrolledPathfinder {
    static RobotController rc;
    static MapLocation dest;

    static void move(RobotController rc, MapLocation dest) throws GameActionException {
        int curDist = rc.getLocation().distanceSquaredTo(dest);
        Direction nextDirection = pathfind(rc, dest);
        if (rc.canMove(nextDirection)) {
            rc.move(nextDirection);
        } else if (!rc.getLocation().isAdjacentTo(dest)) {
            for (Direction dir : Constants.directions) {
                MapLocation next = rc.getLocation().add(dir);
                if (rc.canSenseLocation(next) && next.distanceSquaredTo(dest) <= curDist) {
                    if (rc.canMove(dir)) rc.move(dir);
                }
            }
        }
    }

    static int smartRubble(MapLocation cur) throws GameActionException {
        if (rc.getLocation().equals(cur)) return 0;
        else if (cur.equals(dest)) return -1000;
        else if (rc.canSenseLocation(cur) && !rc.isLocationOccupied(cur)) return rc.senseRubble(cur) + 10;
        //out of bounds or is occupied
        return 10000;
    }

    public static Direction pathfind(RobotController robotController, MapLocation destination) throws GameActionException {
        rc = robotController;
        dest = destination;
        switch (rc.getLocation().directionTo(dest)) {
            case EAST:
                return pathfind_EAST(rc, dest);
            case WEST:
                return pathfind_WEST(rc, dest);
            case NORTH:
                return pathfind_NORTH(rc, dest);
            case SOUTH:
                return pathfind_SOUTH(rc, dest);
            case NORTHEAST:
                return pathfind_NORTHEAST(rc, dest);
            case NORTHWEST:
                return pathfind_NORTHWEST(rc, dest);
            case SOUTHEAST:
                return pathfind_SOUTHEAST(rc, dest);
            default:
                return pathfind_SOUTHWEST(rc, dest);
        }
    }

    private static Direction pathfind_EAST(RobotController rc, MapLocation dest) throws GameActionException {
        MapLocation loc_24 = rc.getLocation();
        Direction best_dir;
        MapLocation loc_6 = loc_24.translate(3, 3);
        MapLocation loc_12 = loc_24.translate(2, 2);
        MapLocation loc_13 = loc_24.translate(3, 2);
        MapLocation loc_18 = loc_24.translate(1, 1);
        MapLocation loc_19 = loc_24.translate(2, 1);
        MapLocation loc_20 = loc_24.translate(3, 1);
        MapLocation loc_25 = loc_24.translate(1, 0);
        MapLocation loc_26 = loc_24.translate(2, 0);
        MapLocation loc_27 = loc_24.translate(3, 0);
        MapLocation loc_32 = loc_24.translate(1, -1);
        MapLocation loc_33 = loc_24.translate(2, -1);
        MapLocation loc_34 = loc_24.translate(3, -1);
        MapLocation loc_40 = loc_24.translate(2, -2);
        MapLocation loc_41 = loc_24.translate(3, -2);
        MapLocation loc_48 = loc_24.translate(3, -3);
// layer 4
        int cost_27 = smartRubble(loc_27) + loc_27.distanceSquaredTo(dest);
        int cost_20 = smartRubble(loc_20) + loc_20.distanceSquaredTo(dest);
        int cost_34 = smartRubble(loc_34) + loc_34.distanceSquaredTo(dest);
        int cost_13 = smartRubble(loc_13) + loc_13.distanceSquaredTo(dest);
        int cost_41 = smartRubble(loc_41) + loc_41.distanceSquaredTo(dest);
        int cost_6 = smartRubble(loc_6) + loc_6.distanceSquaredTo(dest);
        int cost_48 = smartRubble(loc_48) + loc_48.distanceSquaredTo(dest);
// layer 3
        int cost_26 = cost_27;
        if (cost_20 < cost_26) cost_26 = cost_20;
        if (cost_34 < cost_26) cost_26 = cost_34;
        cost_26 += smartRubble(loc_26);
        int cost_19 = cost_20;
        if (cost_13 < cost_19) cost_19 = cost_13;
        if (cost_27 < cost_19) cost_19 = cost_27;
        cost_19 += smartRubble(loc_19);
        int cost_33 = cost_34;
        if (cost_27 < cost_33) cost_33 = cost_27;
        if (cost_41 < cost_33) cost_33 = cost_41;
        cost_33 += smartRubble(loc_33);
        int cost_12 = cost_13;
        if (cost_6 < cost_12) cost_12 = cost_6;
        if (cost_20 < cost_12) cost_12 = cost_20;
        cost_12 += smartRubble(loc_12);
        int cost_40 = cost_41;
        if (cost_34 < cost_40) cost_40 = cost_34;
        if (cost_48 < cost_40) cost_40 = cost_48;
        cost_40 += smartRubble(loc_40);
// layer 2
        int cost_25 = cost_26;
        if (cost_19 < cost_25) cost_25 = cost_19;
        if (cost_33 < cost_25) cost_25 = cost_33;
        cost_25 += smartRubble(loc_25);
        int cost_18 = cost_19;
        if (cost_12 < cost_18) cost_18 = cost_12;
        if (cost_26 < cost_18) cost_18 = cost_26;
        cost_18 += smartRubble(loc_18);
        int cost_32 = cost_33;
        if (cost_26 < cost_32) cost_32 = cost_26;
        if (cost_40 < cost_32) cost_32 = cost_40;
        cost_32 += smartRubble(loc_32);
        int cost_24 = cost_25;
        best_dir = Direction.EAST;
        if (cost_18 < cost_24) {
            cost_24 = cost_18;
            best_dir = Direction.NORTHEAST;
        }
        if (cost_32 < cost_24) {
            best_dir = Direction.SOUTHEAST;
        }
        return best_dir;
    }

    private static Direction pathfind_WEST(RobotController rc, MapLocation dest) throws GameActionException {
        MapLocation loc_24 = rc.getLocation();
        Direction best_dir;
        MapLocation loc_0 = loc_24.translate(-3, 3);
        MapLocation loc_7 = loc_24.translate(-3, 2);
        MapLocation loc_8 = loc_24.translate(-2, 2);
        MapLocation loc_14 = loc_24.translate(-3, 1);
        MapLocation loc_15 = loc_24.translate(-2, 1);
        MapLocation loc_16 = loc_24.translate(-1, 1);
        MapLocation loc_21 = loc_24.translate(-3, 0);
        MapLocation loc_22 = loc_24.translate(-2, 0);
        MapLocation loc_23 = loc_24.translate(-1, 0);
        MapLocation loc_28 = loc_24.translate(-3, -1);
        MapLocation loc_29 = loc_24.translate(-2, -1);
        MapLocation loc_30 = loc_24.translate(-1, -1);
        MapLocation loc_35 = loc_24.translate(-3, -2);
        MapLocation loc_36 = loc_24.translate(-2, -2);
        MapLocation loc_42 = loc_24.translate(-3, -3);
// layer 4
        int cost_21 = smartRubble(loc_21) + loc_21.distanceSquaredTo(dest);
        int cost_28 = smartRubble(loc_28) + loc_28.distanceSquaredTo(dest);
        int cost_14 = smartRubble(loc_14) + loc_14.distanceSquaredTo(dest);
        int cost_35 = smartRubble(loc_35) + loc_35.distanceSquaredTo(dest);
        int cost_7 = smartRubble(loc_7) + loc_7.distanceSquaredTo(dest);
        int cost_42 = smartRubble(loc_42) + loc_42.distanceSquaredTo(dest);
        int cost_0 = smartRubble(loc_0) + loc_0.distanceSquaredTo(dest);
// layer 3
        int cost_22 = cost_21;
        if (cost_28 < cost_22) cost_22 = cost_28;
        if (cost_14 < cost_22) cost_22 = cost_14;
        cost_22 += smartRubble(loc_22);
        int cost_29 = cost_28;
        if (cost_35 < cost_29) cost_29 = cost_35;
        if (cost_21 < cost_29) cost_29 = cost_21;
        cost_29 += smartRubble(loc_29);
        int cost_15 = cost_14;
        if (cost_21 < cost_15) cost_15 = cost_21;
        if (cost_7 < cost_15) cost_15 = cost_7;
        cost_15 += smartRubble(loc_15);
        int cost_36 = cost_35;
        if (cost_42 < cost_36) cost_36 = cost_42;
        if (cost_28 < cost_36) cost_36 = cost_28;
        cost_36 += smartRubble(loc_36);
        int cost_8 = cost_7;
        if (cost_14 < cost_8) cost_8 = cost_14;
        if (cost_0 < cost_8) cost_8 = cost_0;
        cost_8 += smartRubble(loc_8);
// layer 2
        int cost_23 = cost_22;
        if (cost_29 < cost_23) cost_23 = cost_29;
        if (cost_15 < cost_23) cost_23 = cost_15;
        cost_23 += smartRubble(loc_23);
        int cost_30 = cost_29;
        if (cost_36 < cost_30) cost_30 = cost_36;
        if (cost_22 < cost_30) cost_30 = cost_22;
        cost_30 += smartRubble(loc_30);
        int cost_16 = cost_15;
        if (cost_22 < cost_16) cost_16 = cost_22;
        if (cost_8 < cost_16) cost_16 = cost_8;
        cost_16 += smartRubble(loc_16);
        int cost_24 = cost_23;
        best_dir = Direction.WEST;
        if (cost_30 < cost_24) {
            cost_24 = cost_30;
            best_dir = Direction.SOUTHWEST;
        }
        if (cost_16 < cost_24) {
            best_dir = Direction.NORTHWEST;
        }
        return best_dir;
    }

    private static Direction pathfind_NORTH(RobotController rc, MapLocation dest) throws GameActionException {
        MapLocation loc_24 = rc.getLocation();
        Direction best_dir;
        MapLocation loc_0 = loc_24.translate(-3, 3);
        MapLocation loc_1 = loc_24.translate(-2, 3);
        MapLocation loc_2 = loc_24.translate(-1, 3);
        MapLocation loc_3 = loc_24.translate(0, 3);
        MapLocation loc_4 = loc_24.translate(1, 3);
        MapLocation loc_5 = loc_24.translate(2, 3);
        MapLocation loc_6 = loc_24.translate(3, 3);
        MapLocation loc_8 = loc_24.translate(-2, 2);
        MapLocation loc_9 = loc_24.translate(-1, 2);
        MapLocation loc_10 = loc_24.translate(0, 2);
        MapLocation loc_11 = loc_24.translate(1, 2);
        MapLocation loc_12 = loc_24.translate(2, 2);
        MapLocation loc_16 = loc_24.translate(-1, 1);
        MapLocation loc_17 = loc_24.translate(0, 1);
        MapLocation loc_18 = loc_24.translate(1, 1);
// layer 4
        int cost_3 = smartRubble(loc_3) + loc_3.distanceSquaredTo(dest);
        int cost_2 = smartRubble(loc_2) + loc_2.distanceSquaredTo(dest);
        int cost_4 = smartRubble(loc_4) + loc_4.distanceSquaredTo(dest);
        int cost_1 = smartRubble(loc_1) + loc_1.distanceSquaredTo(dest);
        int cost_5 = smartRubble(loc_5) + loc_5.distanceSquaredTo(dest);
        int cost_0 = smartRubble(loc_0) + loc_0.distanceSquaredTo(dest);
        int cost_6 = smartRubble(loc_6) + loc_6.distanceSquaredTo(dest);
// layer 3
        int cost_10 = cost_3;
        if (cost_2 < cost_10) cost_10 = cost_2;
        if (cost_4 < cost_10) cost_10 = cost_4;
        cost_10 += smartRubble(loc_10);
        int cost_9 = cost_2;
        if (cost_1 < cost_9) cost_9 = cost_1;
        if (cost_3 < cost_9) cost_9 = cost_3;
        cost_9 += smartRubble(loc_9);
        int cost_11 = cost_4;
        if (cost_3 < cost_11) cost_11 = cost_3;
        if (cost_5 < cost_11) cost_11 = cost_5;
        cost_11 += smartRubble(loc_11);
        int cost_8 = cost_1;
        if (cost_0 < cost_8) cost_8 = cost_0;
        if (cost_2 < cost_8) cost_8 = cost_2;
        cost_8 += smartRubble(loc_8);
        int cost_12 = cost_5;
        if (cost_4 < cost_12) cost_12 = cost_4;
        if (cost_6 < cost_12) cost_12 = cost_6;
        cost_12 += smartRubble(loc_12);
// layer 2
        int cost_17 = cost_10;
        if (cost_9 < cost_17) cost_17 = cost_9;
        if (cost_11 < cost_17) cost_17 = cost_11;
        cost_17 += smartRubble(loc_17);
        int cost_16 = cost_9;
        if (cost_8 < cost_16) cost_16 = cost_8;
        if (cost_10 < cost_16) cost_16 = cost_10;
        cost_16 += smartRubble(loc_16);
        int cost_18 = cost_11;
        if (cost_10 < cost_18) cost_18 = cost_10;
        if (cost_12 < cost_18) cost_18 = cost_12;
        cost_18 += smartRubble(loc_18);
        int cost_24 = cost_17;
        best_dir = Direction.NORTH;
        if (cost_16 < cost_24) {
            cost_24 = cost_16;
            best_dir = Direction.NORTHWEST;
        }
        if (cost_18 < cost_24) {
            best_dir = Direction.NORTHEAST;
        }
        return best_dir;
    }

    private static Direction pathfind_SOUTH(RobotController rc, MapLocation dest) throws GameActionException {
        MapLocation loc_24 = rc.getLocation();
        Direction best_dir;
        MapLocation loc_30 = loc_24.translate(-1, -1);
        MapLocation loc_31 = loc_24.translate(0, -1);
        MapLocation loc_32 = loc_24.translate(1, -1);
        MapLocation loc_36 = loc_24.translate(-2, -2);
        MapLocation loc_37 = loc_24.translate(-1, -2);
        MapLocation loc_38 = loc_24.translate(0, -2);
        MapLocation loc_39 = loc_24.translate(1, -2);
        MapLocation loc_40 = loc_24.translate(2, -2);
        MapLocation loc_42 = loc_24.translate(-3, -3);
        MapLocation loc_43 = loc_24.translate(-2, -3);
        MapLocation loc_44 = loc_24.translate(-1, -3);
        MapLocation loc_45 = loc_24.translate(0, -3);
        MapLocation loc_46 = loc_24.translate(1, -3);
        MapLocation loc_47 = loc_24.translate(2, -3);
        MapLocation loc_48 = loc_24.translate(3, -3);
// layer 4
        int cost_45 = smartRubble(loc_45) + loc_45.distanceSquaredTo(dest);
        int cost_46 = smartRubble(loc_46) + loc_46.distanceSquaredTo(dest);
        int cost_44 = smartRubble(loc_44) + loc_44.distanceSquaredTo(dest);
        int cost_47 = smartRubble(loc_47) + loc_47.distanceSquaredTo(dest);
        int cost_43 = smartRubble(loc_43) + loc_43.distanceSquaredTo(dest);
        int cost_48 = smartRubble(loc_48) + loc_48.distanceSquaredTo(dest);
        int cost_42 = smartRubble(loc_42) + loc_42.distanceSquaredTo(dest);
// layer 3
        int cost_38 = cost_45;
        if (cost_46 < cost_38) cost_38 = cost_46;
        if (cost_44 < cost_38) cost_38 = cost_44;
        cost_38 += smartRubble(loc_38);
        int cost_39 = cost_46;
        if (cost_47 < cost_39) cost_39 = cost_47;
        if (cost_45 < cost_39) cost_39 = cost_45;
        cost_39 += smartRubble(loc_39);
        int cost_37 = cost_44;
        if (cost_45 < cost_37) cost_37 = cost_45;
        if (cost_43 < cost_37) cost_37 = cost_43;
        cost_37 += smartRubble(loc_37);
        int cost_40 = cost_47;
        if (cost_48 < cost_40) cost_40 = cost_48;
        if (cost_46 < cost_40) cost_40 = cost_46;
        cost_40 += smartRubble(loc_40);
        int cost_36 = cost_43;
        if (cost_44 < cost_36) cost_36 = cost_44;
        if (cost_42 < cost_36) cost_36 = cost_42;
        cost_36 += smartRubble(loc_36);
// layer 2
        int cost_31 = cost_38;
        if (cost_39 < cost_31) cost_31 = cost_39;
        if (cost_37 < cost_31) cost_31 = cost_37;
        cost_31 += smartRubble(loc_31);
        int cost_32 = cost_39;
        if (cost_40 < cost_32) cost_32 = cost_40;
        if (cost_38 < cost_32) cost_32 = cost_38;
        cost_32 += smartRubble(loc_32);
        int cost_30 = cost_37;
        if (cost_38 < cost_30) cost_30 = cost_38;
        if (cost_36 < cost_30) cost_30 = cost_36;
        cost_30 += smartRubble(loc_30);
        int cost_24 = cost_31;
        best_dir = Direction.SOUTH;
        if (cost_32 < cost_24) {
            cost_24 = cost_32;
            best_dir = Direction.SOUTHEAST;
        }
        if (cost_30 < cost_24) {
            best_dir = Direction.SOUTHWEST;
        }
        return best_dir;
    }

    private static Direction pathfind_NORTHEAST(RobotController rc, MapLocation dest) throws GameActionException {
        MapLocation loc_24 = rc.getLocation();
        Direction best_dir;
        MapLocation loc_3 = loc_24.translate(0, 3);
        MapLocation loc_4 = loc_24.translate(1, 3);
        MapLocation loc_5 = loc_24.translate(2, 3);
        MapLocation loc_6 = loc_24.translate(3, 3);
        MapLocation loc_10 = loc_24.translate(0, 2);
        MapLocation loc_11 = loc_24.translate(1, 2);
        MapLocation loc_12 = loc_24.translate(2, 2);
        MapLocation loc_13 = loc_24.translate(3, 2);
        MapLocation loc_17 = loc_24.translate(0, 1);
        MapLocation loc_18 = loc_24.translate(1, 1);
        MapLocation loc_19 = loc_24.translate(2, 1);
        MapLocation loc_20 = loc_24.translate(3, 1);
        MapLocation loc_25 = loc_24.translate(1, 0);
        MapLocation loc_26 = loc_24.translate(2, 0);
        MapLocation loc_27 = loc_24.translate(3, 0);
// layer 5
        int cost_6 = smartRubble(loc_6) + loc_6.distanceSquaredTo(dest);
        int cost_13 = smartRubble(loc_13) + loc_13.distanceSquaredTo(dest);
        int cost_5 = smartRubble(loc_5) + loc_5.distanceSquaredTo(dest);
// layer 4
        int cost_4 = smartRubble(loc_4) + loc_4.distanceSquaredTo(dest);
        int cost_20 = smartRubble(loc_20) + loc_20.distanceSquaredTo(dest);
        int cost_12 = cost_6;
        if (cost_13 < cost_12) cost_12 = cost_13;
        cost_12 += smartRubble(loc_12);
        int cost_3 = smartRubble(loc_3) + loc_3.distanceSquaredTo(dest);
        int cost_27 = smartRubble(loc_27) + loc_27.distanceSquaredTo(dest);
// layer 3
        int cost_11 = cost_5;
        if (cost_4 < cost_11) cost_11 = cost_4;
        cost_11 += smartRubble(loc_11);
        int cost_19 = cost_13;
        if (cost_20 < cost_19) cost_19 = cost_20;
        cost_19 += smartRubble(loc_19);
        int cost_10 = cost_4;
        if (cost_3 < cost_10) cost_10 = cost_3;
        cost_10 += smartRubble(loc_10);
        int cost_26 = cost_20;
        if (cost_27 < cost_26) cost_26 = cost_27;
        cost_26 += smartRubble(loc_26);
// layer 2
        int cost_18 = cost_12;
        if (cost_11 < cost_18) cost_18 = cost_11;
        if (cost_19 < cost_18) cost_18 = cost_19;
        cost_18 += smartRubble(loc_18);
        int cost_17 = cost_11;
        if (cost_10 < cost_17) cost_17 = cost_10;
        cost_17 += smartRubble(loc_17);
        int cost_25 = cost_19;
        if (cost_26 < cost_25) cost_25 = cost_26;
        cost_25 += smartRubble(loc_25);
        int cost_24 = cost_18;
        best_dir = Direction.NORTHEAST;
        if (cost_17 < cost_24) {
            cost_24 = cost_17;
            best_dir = Direction.NORTH;
        }
        if (cost_25 < cost_24) {
            best_dir = Direction.EAST;
        }
        return best_dir;
    }

    private static Direction pathfind_NORTHWEST(RobotController rc, MapLocation dest) throws GameActionException {
        MapLocation loc_24 = rc.getLocation();
        Direction best_dir;
        MapLocation loc_0 = loc_24.translate(-3, 3);
        MapLocation loc_1 = loc_24.translate(-2, 3);
        MapLocation loc_2 = loc_24.translate(-1, 3);
        MapLocation loc_3 = loc_24.translate(0, 3);
        MapLocation loc_7 = loc_24.translate(-3, 2);
        MapLocation loc_8 = loc_24.translate(-2, 2);
        MapLocation loc_9 = loc_24.translate(-1, 2);
        MapLocation loc_10 = loc_24.translate(0, 2);
        MapLocation loc_14 = loc_24.translate(-3, 1);
        MapLocation loc_15 = loc_24.translate(-2, 1);
        MapLocation loc_16 = loc_24.translate(-1, 1);
        MapLocation loc_17 = loc_24.translate(0, 1);
        MapLocation loc_21 = loc_24.translate(-3, 0);
        MapLocation loc_22 = loc_24.translate(-2, 0);
        MapLocation loc_23 = loc_24.translate(-1, 0);
// layer 5
        int cost_0 = smartRubble(loc_0) + loc_0.distanceSquaredTo(dest);
        int cost_1 = smartRubble(loc_1) + loc_1.distanceSquaredTo(dest);
        int cost_7 = smartRubble(loc_7) + loc_7.distanceSquaredTo(dest);
// layer 4
        int cost_14 = smartRubble(loc_14) + loc_14.distanceSquaredTo(dest);
        int cost_2 = smartRubble(loc_2) + loc_2.distanceSquaredTo(dest);
        int cost_8 = cost_0;
        if (cost_1 < cost_8) cost_8 = cost_1;
        cost_8 += smartRubble(loc_8);
        int cost_21 = smartRubble(loc_21) + loc_21.distanceSquaredTo(dest);
        int cost_3 = smartRubble(loc_3) + loc_3.distanceSquaredTo(dest);
// layer 3
        int cost_15 = cost_7;
        if (cost_14 < cost_15) cost_15 = cost_14;
        cost_15 += smartRubble(loc_15);
        int cost_9 = cost_1;
        if (cost_2 < cost_9) cost_9 = cost_2;
        cost_9 += smartRubble(loc_9);
        int cost_22 = cost_14;
        if (cost_21 < cost_22) cost_22 = cost_21;
        cost_22 += smartRubble(loc_22);
        int cost_10 = cost_2;
        if (cost_3 < cost_10) cost_10 = cost_3;
        cost_10 += smartRubble(loc_10);
// layer 2
        int cost_16 = cost_8;
        if (cost_15 < cost_16) cost_16 = cost_15;
        if (cost_9 < cost_16) cost_16 = cost_9;
        cost_16 += smartRubble(loc_16);
        int cost_23 = cost_15;
        if (cost_22 < cost_23) cost_23 = cost_22;
        cost_23 += smartRubble(loc_23);
        int cost_17 = cost_9;
        if (cost_10 < cost_17) cost_17 = cost_10;
        cost_17 += smartRubble(loc_17);
        int cost_24 = cost_16;
        best_dir = Direction.NORTHWEST;
        if (cost_23 < cost_24) {
            cost_24 = cost_23;
            best_dir = Direction.WEST;
        }
        if (cost_17 < cost_24) {
            best_dir = Direction.NORTH;
        }
        return best_dir;
    }

    private static Direction pathfind_SOUTHEAST(RobotController rc, MapLocation dest) throws GameActionException {
        MapLocation loc_24 = rc.getLocation();
        Direction best_dir;
        MapLocation loc_25 = loc_24.translate(1, 0);
        MapLocation loc_26 = loc_24.translate(2, 0);
        MapLocation loc_27 = loc_24.translate(3, 0);
        MapLocation loc_31 = loc_24.translate(0, -1);
        MapLocation loc_32 = loc_24.translate(1, -1);
        MapLocation loc_33 = loc_24.translate(2, -1);
        MapLocation loc_34 = loc_24.translate(3, -1);
        MapLocation loc_38 = loc_24.translate(0, -2);
        MapLocation loc_39 = loc_24.translate(1, -2);
        MapLocation loc_40 = loc_24.translate(2, -2);
        MapLocation loc_41 = loc_24.translate(3, -2);
        MapLocation loc_45 = loc_24.translate(0, -3);
        MapLocation loc_46 = loc_24.translate(1, -3);
        MapLocation loc_47 = loc_24.translate(2, -3);
        MapLocation loc_48 = loc_24.translate(3, -3);
// layer 5
        int cost_48 = smartRubble(loc_48) + loc_48.distanceSquaredTo(dest);
        int cost_47 = smartRubble(loc_47) + loc_47.distanceSquaredTo(dest);
        int cost_41 = smartRubble(loc_41) + loc_41.distanceSquaredTo(dest);
// layer 4
        int cost_34 = smartRubble(loc_34) + loc_34.distanceSquaredTo(dest);
        int cost_46 = smartRubble(loc_46) + loc_46.distanceSquaredTo(dest);
        int cost_40 = cost_48;
        if (cost_47 < cost_40) cost_40 = cost_47;
        cost_40 += smartRubble(loc_40);
        int cost_27 = smartRubble(loc_27) + loc_27.distanceSquaredTo(dest);
        int cost_45 = smartRubble(loc_45) + loc_45.distanceSquaredTo(dest);
// layer 3
        int cost_33 = cost_41;
        if (cost_34 < cost_33) cost_33 = cost_34;
        cost_33 += smartRubble(loc_33);
        int cost_39 = cost_47;
        if (cost_46 < cost_39) cost_39 = cost_46;
        cost_39 += smartRubble(loc_39);
        int cost_26 = cost_34;
        if (cost_27 < cost_26) cost_26 = cost_27;
        cost_26 += smartRubble(loc_26);
        int cost_38 = cost_46;
        if (cost_45 < cost_38) cost_38 = cost_45;
        cost_38 += smartRubble(loc_38);
// layer 2
        int cost_32 = cost_40;
        if (cost_33 < cost_32) cost_32 = cost_33;
        if (cost_39 < cost_32) cost_32 = cost_39;
        cost_32 += smartRubble(loc_32);
        int cost_25 = cost_33;
        if (cost_26 < cost_25) cost_25 = cost_26;
        cost_25 += smartRubble(loc_25);
        int cost_31 = cost_39;
        if (cost_38 < cost_31) cost_31 = cost_38;
        cost_31 += smartRubble(loc_31);
        int cost_24 = cost_32;
        best_dir = Direction.SOUTHEAST;
        if (cost_25 < cost_24) {
            cost_24 = cost_25;
            best_dir = Direction.EAST;
        }
        if (cost_31 < cost_24) {
            best_dir = Direction.SOUTH;
        }
        return best_dir;
    }

    private static Direction pathfind_SOUTHWEST(RobotController rc, MapLocation dest) throws GameActionException {
        MapLocation loc_24 = rc.getLocation();
        Direction best_dir;
        MapLocation loc_21 = loc_24.translate(-3, 0);
        MapLocation loc_22 = loc_24.translate(-2, 0);
        MapLocation loc_23 = loc_24.translate(-1, 0);
        MapLocation loc_28 = loc_24.translate(-3, -1);
        MapLocation loc_29 = loc_24.translate(-2, -1);
        MapLocation loc_30 = loc_24.translate(-1, -1);
        MapLocation loc_31 = loc_24.translate(0, -1);
        MapLocation loc_35 = loc_24.translate(-3, -2);
        MapLocation loc_36 = loc_24.translate(-2, -2);
        MapLocation loc_37 = loc_24.translate(-1, -2);
        MapLocation loc_38 = loc_24.translate(0, -2);
        MapLocation loc_42 = loc_24.translate(-3, -3);
        MapLocation loc_43 = loc_24.translate(-2, -3);
        MapLocation loc_44 = loc_24.translate(-1, -3);
        MapLocation loc_45 = loc_24.translate(0, -3);
// layer 5
        int cost_42 = smartRubble(loc_42) + loc_42.distanceSquaredTo(dest);
        int cost_35 = smartRubble(loc_35) + loc_35.distanceSquaredTo(dest);
        int cost_43 = smartRubble(loc_43) + loc_43.distanceSquaredTo(dest);
// layer 4
        int cost_44 = smartRubble(loc_44) + loc_44.distanceSquaredTo(dest);
        int cost_28 = smartRubble(loc_28) + loc_28.distanceSquaredTo(dest);
        int cost_36 = cost_42;
        if (cost_35 < cost_36) cost_36 = cost_35;
        cost_36 += smartRubble(loc_36);
        int cost_45 = smartRubble(loc_45) + loc_45.distanceSquaredTo(dest);
        int cost_21 = smartRubble(loc_21) + loc_21.distanceSquaredTo(dest);
// layer 3
        int cost_37 = cost_43;
        if (cost_44 < cost_37) cost_37 = cost_44;
        cost_37 += smartRubble(loc_37);
        int cost_29 = cost_35;
        if (cost_28 < cost_29) cost_29 = cost_28;
        cost_29 += smartRubble(loc_29);
        int cost_38 = cost_44;
        if (cost_45 < cost_38) cost_38 = cost_45;
        cost_38 += smartRubble(loc_38);
        int cost_22 = cost_28;
        if (cost_21 < cost_22) cost_22 = cost_21;
        cost_22 += smartRubble(loc_22);
// layer 2
        int cost_30 = cost_36;
        if (cost_37 < cost_30) cost_30 = cost_37;
        if (cost_29 < cost_30) cost_30 = cost_29;
        cost_30 += smartRubble(loc_30);
        int cost_31 = cost_37;
        if (cost_38 < cost_31) cost_31 = cost_38;
        cost_31 += smartRubble(loc_31);
        int cost_23 = cost_29;
        if (cost_22 < cost_23) cost_23 = cost_22;
        cost_23 += smartRubble(loc_23);
        int cost_24 = cost_30;
        best_dir = Direction.SOUTHWEST;
        if (cost_31 < cost_24) {
            cost_24 = cost_31;
            best_dir = Direction.SOUTH;
        }
        if (cost_23 < cost_24) {
            best_dir = Direction.WEST;
        }
        return best_dir;
    }
}