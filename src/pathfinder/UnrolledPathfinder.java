package pathfinder;

import battlecode.common.*;

public class UnrolledPathfinder {

    public static Direction pathfind(RobotController rc, MapLocation dest) throws GameActionException {
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
        MapLocation m_location = rc.getLocation();
        Direction best_dir = null;
        MapLocation loc_0 = m_location.translate(-3, 3);
        MapLocation loc_1 = m_location.translate(-2, 3);
        MapLocation loc_2 = m_location.translate(-1, 3);
        MapLocation loc_3 = m_location.translate(0, 3);
        MapLocation loc_4 = m_location.translate(1, 3);
        MapLocation loc_5 = m_location.translate(2, 3);
        MapLocation loc_6 = m_location.translate(3, 3);
        MapLocation loc_7 = m_location.translate(-3, 2);
        MapLocation loc_8 = m_location.translate(-2, 2);
        MapLocation loc_9 = m_location.translate(-1, 2);
        MapLocation loc_10 = m_location.translate(0, 2);
        MapLocation loc_11 = m_location.translate(1, 2);
        MapLocation loc_12 = m_location.translate(2, 2);
        MapLocation loc_13 = m_location.translate(3, 2);
        MapLocation loc_14 = m_location.translate(-3, 1);
        MapLocation loc_15 = m_location.translate(-2, 1);
        MapLocation loc_16 = m_location.translate(-1, 1);
        MapLocation loc_17 = m_location.translate(0, 1);
        MapLocation loc_18 = m_location.translate(1, 1);
        MapLocation loc_19 = m_location.translate(2, 1);
        MapLocation loc_20 = m_location.translate(3, 1);
        MapLocation loc_21 = m_location.translate(-3, 0);
        MapLocation loc_22 = m_location.translate(-2, 0);
        MapLocation loc_23 = m_location.translate(-1, 0);
        MapLocation loc_24 = m_location.translate(0, 0);
        MapLocation loc_25 = m_location.translate(1, 0);
        MapLocation loc_26 = m_location.translate(2, 0);
        MapLocation loc_27 = m_location.translate(3, 0);
        MapLocation loc_28 = m_location.translate(-3, -1);
        MapLocation loc_29 = m_location.translate(-2, -1);
        MapLocation loc_30 = m_location.translate(-1, -1);
        MapLocation loc_31 = m_location.translate(0, -1);
        MapLocation loc_32 = m_location.translate(1, -1);
        MapLocation loc_33 = m_location.translate(2, -1);
        MapLocation loc_34 = m_location.translate(3, -1);
        MapLocation loc_35 = m_location.translate(-3, -2);
        MapLocation loc_36 = m_location.translate(-2, -2);
        MapLocation loc_37 = m_location.translate(-1, -2);
        MapLocation loc_38 = m_location.translate(0, -2);
        MapLocation loc_39 = m_location.translate(1, -2);
        MapLocation loc_40 = m_location.translate(2, -2);
        MapLocation loc_41 = m_location.translate(3, -2);
        MapLocation loc_42 = m_location.translate(-3, -3);
        MapLocation loc_43 = m_location.translate(-2, -3);
        MapLocation loc_44 = m_location.translate(-1, -3);
        MapLocation loc_45 = m_location.translate(0, -3);
        MapLocation loc_46 = m_location.translate(1, -3);
        MapLocation loc_47 = m_location.translate(2, -3);
        MapLocation loc_48 = m_location.translate(3, -3);
// layer 3
        int cost_6 = 10000;
        if (rc.canSenseLocation(loc_6) && !rc.isLocationOccupied(loc_6)) {
            cost_6 = rc.senseRubble(loc_6) + 10 + dest.distanceSquaredTo(loc_6);
        }
        int cost_13 = 10000;
        if (rc.canSenseLocation(loc_13) && !rc.isLocationOccupied(loc_13)) {
            cost_13 = rc.senseRubble(loc_13) + 10 + dest.distanceSquaredTo(loc_13);
        }
        int cost_20 = 10000;
        if (rc.canSenseLocation(loc_20) && !rc.isLocationOccupied(loc_20)) {
            cost_20 = rc.senseRubble(loc_20) + 10 + dest.distanceSquaredTo(loc_20);
        }
        int cost_27 = 10000;
        if (rc.canSenseLocation(loc_27) && !rc.isLocationOccupied(loc_27)) {
            cost_27 = rc.senseRubble(loc_27) + 10 + dest.distanceSquaredTo(loc_27);
        }
        int cost_34 = 10000;
        if (rc.canSenseLocation(loc_34) && !rc.isLocationOccupied(loc_34)) {
            cost_34 = rc.senseRubble(loc_34) + 10 + dest.distanceSquaredTo(loc_34);
        }
        int cost_41 = 10000;
        if (rc.canSenseLocation(loc_41) && !rc.isLocationOccupied(loc_41)) {
            cost_41 = rc.senseRubble(loc_41) + 10 + dest.distanceSquaredTo(loc_41);
        }
        int cost_48 = 10000;
        if (rc.canSenseLocation(loc_48) && !rc.isLocationOccupied(loc_48)) {
            cost_48 = rc.senseRubble(loc_48) + 10 + dest.distanceSquaredTo(loc_48);
        }
// layer 2
        int cost_12 = cost_6;
        if (cost_13 < cost_12) cost_12 = cost_13;
        if (cost_20 < cost_12) cost_12 = cost_20;
        if (rc.canSenseLocation(loc_12) && !rc.isLocationOccupied(loc_12)) {
            cost_12 += rc.senseRubble(loc_12) + 10;
        } else cost_12 += 10000;
        int cost_19 = cost_13;
        if (cost_20 < cost_19) cost_19 = cost_20;
        if (cost_27 < cost_19) cost_19 = cost_27;
        if (rc.canSenseLocation(loc_19) && !rc.isLocationOccupied(loc_19)) {
            cost_19 += rc.senseRubble(loc_19) + 10;
        } else cost_19 += 10000;
        int cost_26 = cost_20;
        if (cost_27 < cost_26) cost_26 = cost_27;
        if (cost_34 < cost_26) cost_26 = cost_34;
        if (rc.canSenseLocation(loc_26) && !rc.isLocationOccupied(loc_26)) {
            cost_26 += rc.senseRubble(loc_26) + 10;
        } else cost_26 += 10000;
        int cost_33 = cost_27;
        if (cost_34 < cost_33) cost_33 = cost_34;
        if (cost_41 < cost_33) cost_33 = cost_41;
        if (rc.canSenseLocation(loc_33) && !rc.isLocationOccupied(loc_33)) {
            cost_33 += rc.senseRubble(loc_33) + 10;
        } else cost_33 += 10000;
        int cost_40 = cost_34;
        if (cost_41 < cost_40) cost_40 = cost_41;
        if (cost_48 < cost_40) cost_40 = cost_48;
        if (rc.canSenseLocation(loc_40) && !rc.isLocationOccupied(loc_40)) {
            cost_40 += rc.senseRubble(loc_40) + 10;
        } else cost_40 += 10000;
// layer 1
        int cost_18 = cost_12;
        if (cost_19 < cost_18) cost_18 = cost_19;
        if (cost_26 < cost_18) cost_18 = cost_26;
        if (rc.canSenseLocation(loc_18) && !rc.isLocationOccupied(loc_18)) {
            cost_18 += rc.senseRubble(loc_18) + 10;
        } else cost_18 += 10000;
        int cost_25 = cost_19;
        if (cost_26 < cost_25) cost_25 = cost_26;
        if (cost_33 < cost_25) cost_25 = cost_33;
        if (rc.canSenseLocation(loc_25) && !rc.isLocationOccupied(loc_25)) {
            cost_25 += rc.senseRubble(loc_25) + 10;
        } else cost_25 += 10000;
        int cost_32 = cost_26;
        if (cost_33 < cost_32) cost_32 = cost_33;
        if (cost_40 < cost_32) cost_32 = cost_40;
        if (rc.canSenseLocation(loc_32) && !rc.isLocationOccupied(loc_32)) {
            cost_32 += rc.senseRubble(loc_32) + 10;
        } else cost_32 += 10000;
        int cost_24 = cost_18;
        best_dir = Direction.NORTHEAST;
        if (cost_25 < cost_24) {
            cost_24 = cost_25;
            best_dir = Direction.EAST;
        }
        if (cost_32 < cost_24) {
            cost_24 = cost_32;
            best_dir = Direction.SOUTHEAST;
        }
        return best_dir;
    }

    private static Direction pathfind_WEST(RobotController rc, MapLocation dest) throws GameActionException {
        MapLocation m_location = rc.getLocation();
        Direction best_dir = null;
        MapLocation loc_0 = m_location.translate(-3, 3);
        MapLocation loc_1 = m_location.translate(-2, 3);
        MapLocation loc_2 = m_location.translate(-1, 3);
        MapLocation loc_3 = m_location.translate(0, 3);
        MapLocation loc_4 = m_location.translate(1, 3);
        MapLocation loc_5 = m_location.translate(2, 3);
        MapLocation loc_6 = m_location.translate(3, 3);
        MapLocation loc_7 = m_location.translate(-3, 2);
        MapLocation loc_8 = m_location.translate(-2, 2);
        MapLocation loc_9 = m_location.translate(-1, 2);
        MapLocation loc_10 = m_location.translate(0, 2);
        MapLocation loc_11 = m_location.translate(1, 2);
        MapLocation loc_12 = m_location.translate(2, 2);
        MapLocation loc_13 = m_location.translate(3, 2);
        MapLocation loc_14 = m_location.translate(-3, 1);
        MapLocation loc_15 = m_location.translate(-2, 1);
        MapLocation loc_16 = m_location.translate(-1, 1);
        MapLocation loc_17 = m_location.translate(0, 1);
        MapLocation loc_18 = m_location.translate(1, 1);
        MapLocation loc_19 = m_location.translate(2, 1);
        MapLocation loc_20 = m_location.translate(3, 1);
        MapLocation loc_21 = m_location.translate(-3, 0);
        MapLocation loc_22 = m_location.translate(-2, 0);
        MapLocation loc_23 = m_location.translate(-1, 0);
        MapLocation loc_24 = m_location.translate(0, 0);
        MapLocation loc_25 = m_location.translate(1, 0);
        MapLocation loc_26 = m_location.translate(2, 0);
        MapLocation loc_27 = m_location.translate(3, 0);
        MapLocation loc_28 = m_location.translate(-3, -1);
        MapLocation loc_29 = m_location.translate(-2, -1);
        MapLocation loc_30 = m_location.translate(-1, -1);
        MapLocation loc_31 = m_location.translate(0, -1);
        MapLocation loc_32 = m_location.translate(1, -1);
        MapLocation loc_33 = m_location.translate(2, -1);
        MapLocation loc_34 = m_location.translate(3, -1);
        MapLocation loc_35 = m_location.translate(-3, -2);
        MapLocation loc_36 = m_location.translate(-2, -2);
        MapLocation loc_37 = m_location.translate(-1, -2);
        MapLocation loc_38 = m_location.translate(0, -2);
        MapLocation loc_39 = m_location.translate(1, -2);
        MapLocation loc_40 = m_location.translate(2, -2);
        MapLocation loc_41 = m_location.translate(3, -2);
        MapLocation loc_42 = m_location.translate(-3, -3);
        MapLocation loc_43 = m_location.translate(-2, -3);
        MapLocation loc_44 = m_location.translate(-1, -3);
        MapLocation loc_45 = m_location.translate(0, -3);
        MapLocation loc_46 = m_location.translate(1, -3);
        MapLocation loc_47 = m_location.translate(2, -3);
        MapLocation loc_48 = m_location.translate(3, -3);
// layer 3
        int cost_42 = 10000;
        if (rc.canSenseLocation(loc_42) && !rc.isLocationOccupied(loc_42)) {
            cost_42 = rc.senseRubble(loc_42) + 10 + dest.distanceSquaredTo(loc_42);
        }
        int cost_35 = 10000;
        if (rc.canSenseLocation(loc_35) && !rc.isLocationOccupied(loc_35)) {
            cost_35 = rc.senseRubble(loc_35) + 10 + dest.distanceSquaredTo(loc_35);
        }
        int cost_28 = 10000;
        if (rc.canSenseLocation(loc_28) && !rc.isLocationOccupied(loc_28)) {
            cost_28 = rc.senseRubble(loc_28) + 10 + dest.distanceSquaredTo(loc_28);
        }
        int cost_21 = 10000;
        if (rc.canSenseLocation(loc_21) && !rc.isLocationOccupied(loc_21)) {
            cost_21 = rc.senseRubble(loc_21) + 10 + dest.distanceSquaredTo(loc_21);
        }
        int cost_14 = 10000;
        if (rc.canSenseLocation(loc_14) && !rc.isLocationOccupied(loc_14)) {
            cost_14 = rc.senseRubble(loc_14) + 10 + dest.distanceSquaredTo(loc_14);
        }
        int cost_7 = 10000;
        if (rc.canSenseLocation(loc_7) && !rc.isLocationOccupied(loc_7)) {
            cost_7 = rc.senseRubble(loc_7) + 10 + dest.distanceSquaredTo(loc_7);
        }
        int cost_0 = 10000;
        if (rc.canSenseLocation(loc_0) && !rc.isLocationOccupied(loc_0)) {
            cost_0 = rc.senseRubble(loc_0) + 10 + dest.distanceSquaredTo(loc_0);
        }
// layer 2
        int cost_36 = cost_42;
        if (cost_35 < cost_36) cost_36 = cost_35;
        if (cost_28 < cost_36) cost_36 = cost_28;
        if (rc.canSenseLocation(loc_36) && !rc.isLocationOccupied(loc_36)) {
            cost_36 += rc.senseRubble(loc_36) + 10;
        } else cost_36 += 10000;
        int cost_29 = cost_35;
        if (cost_28 < cost_29) cost_29 = cost_28;
        if (cost_21 < cost_29) cost_29 = cost_21;
        if (rc.canSenseLocation(loc_29) && !rc.isLocationOccupied(loc_29)) {
            cost_29 += rc.senseRubble(loc_29) + 10;
        } else cost_29 += 10000;
        int cost_22 = cost_28;
        if (cost_21 < cost_22) cost_22 = cost_21;
        if (cost_14 < cost_22) cost_22 = cost_14;
        if (rc.canSenseLocation(loc_22) && !rc.isLocationOccupied(loc_22)) {
            cost_22 += rc.senseRubble(loc_22) + 10;
        } else cost_22 += 10000;
        int cost_15 = cost_21;
        if (cost_14 < cost_15) cost_15 = cost_14;
        if (cost_7 < cost_15) cost_15 = cost_7;
        if (rc.canSenseLocation(loc_15) && !rc.isLocationOccupied(loc_15)) {
            cost_15 += rc.senseRubble(loc_15) + 10;
        } else cost_15 += 10000;
        int cost_8 = cost_14;
        if (cost_7 < cost_8) cost_8 = cost_7;
        if (cost_0 < cost_8) cost_8 = cost_0;
        if (rc.canSenseLocation(loc_8) && !rc.isLocationOccupied(loc_8)) {
            cost_8 += rc.senseRubble(loc_8) + 10;
        } else cost_8 += 10000;
// layer 1
        int cost_30 = cost_36;
        if (cost_29 < cost_30) cost_30 = cost_29;
        if (cost_22 < cost_30) cost_30 = cost_22;
        if (rc.canSenseLocation(loc_30) && !rc.isLocationOccupied(loc_30)) {
            cost_30 += rc.senseRubble(loc_30) + 10;
        } else cost_30 += 10000;
        int cost_23 = cost_29;
        if (cost_22 < cost_23) cost_23 = cost_22;
        if (cost_15 < cost_23) cost_23 = cost_15;
        if (rc.canSenseLocation(loc_23) && !rc.isLocationOccupied(loc_23)) {
            cost_23 += rc.senseRubble(loc_23) + 10;
        } else cost_23 += 10000;
        int cost_16 = cost_22;
        if (cost_15 < cost_16) cost_16 = cost_15;
        if (cost_8 < cost_16) cost_16 = cost_8;
        if (rc.canSenseLocation(loc_16) && !rc.isLocationOccupied(loc_16)) {
            cost_16 += rc.senseRubble(loc_16) + 10;
        } else cost_16 += 10000;
        int cost_24 = cost_30;
        best_dir = Direction.SOUTHWEST;
        if (cost_23 < cost_24) {
            cost_24 = cost_23;
            best_dir = Direction.WEST;
        }
        if (cost_16 < cost_24) {
            cost_24 = cost_16;
            best_dir = Direction.NORTHWEST;
        }
        return best_dir;
    }

    private static Direction pathfind_NORTH(RobotController rc, MapLocation dest) throws GameActionException {
        MapLocation m_location = rc.getLocation();
        Direction best_dir = null;
        MapLocation loc_0 = m_location.translate(-3, 3);
        MapLocation loc_1 = m_location.translate(-2, 3);
        MapLocation loc_2 = m_location.translate(-1, 3);
        MapLocation loc_3 = m_location.translate(0, 3);
        MapLocation loc_4 = m_location.translate(1, 3);
        MapLocation loc_5 = m_location.translate(2, 3);
        MapLocation loc_6 = m_location.translate(3, 3);
        MapLocation loc_7 = m_location.translate(-3, 2);
        MapLocation loc_8 = m_location.translate(-2, 2);
        MapLocation loc_9 = m_location.translate(-1, 2);
        MapLocation loc_10 = m_location.translate(0, 2);
        MapLocation loc_11 = m_location.translate(1, 2);
        MapLocation loc_12 = m_location.translate(2, 2);
        MapLocation loc_13 = m_location.translate(3, 2);
        MapLocation loc_14 = m_location.translate(-3, 1);
        MapLocation loc_15 = m_location.translate(-2, 1);
        MapLocation loc_16 = m_location.translate(-1, 1);
        MapLocation loc_17 = m_location.translate(0, 1);
        MapLocation loc_18 = m_location.translate(1, 1);
        MapLocation loc_19 = m_location.translate(2, 1);
        MapLocation loc_20 = m_location.translate(3, 1);
        MapLocation loc_21 = m_location.translate(-3, 0);
        MapLocation loc_22 = m_location.translate(-2, 0);
        MapLocation loc_23 = m_location.translate(-1, 0);
        MapLocation loc_24 = m_location.translate(0, 0);
        MapLocation loc_25 = m_location.translate(1, 0);
        MapLocation loc_26 = m_location.translate(2, 0);
        MapLocation loc_27 = m_location.translate(3, 0);
        MapLocation loc_28 = m_location.translate(-3, -1);
        MapLocation loc_29 = m_location.translate(-2, -1);
        MapLocation loc_30 = m_location.translate(-1, -1);
        MapLocation loc_31 = m_location.translate(0, -1);
        MapLocation loc_32 = m_location.translate(1, -1);
        MapLocation loc_33 = m_location.translate(2, -1);
        MapLocation loc_34 = m_location.translate(3, -1);
        MapLocation loc_35 = m_location.translate(-3, -2);
        MapLocation loc_36 = m_location.translate(-2, -2);
        MapLocation loc_37 = m_location.translate(-1, -2);
        MapLocation loc_38 = m_location.translate(0, -2);
        MapLocation loc_39 = m_location.translate(1, -2);
        MapLocation loc_40 = m_location.translate(2, -2);
        MapLocation loc_41 = m_location.translate(3, -2);
        MapLocation loc_42 = m_location.translate(-3, -3);
        MapLocation loc_43 = m_location.translate(-2, -3);
        MapLocation loc_44 = m_location.translate(-1, -3);
        MapLocation loc_45 = m_location.translate(0, -3);
        MapLocation loc_46 = m_location.translate(1, -3);
        MapLocation loc_47 = m_location.translate(2, -3);
        MapLocation loc_48 = m_location.translate(3, -3);
// layer 3
        int cost_0 = 10000;
        if (rc.canSenseLocation(loc_0) && !rc.isLocationOccupied(loc_0)) {
            cost_0 = rc.senseRubble(loc_0) + 10 + dest.distanceSquaredTo(loc_0);
        }
        int cost_1 = 10000;
        if (rc.canSenseLocation(loc_1) && !rc.isLocationOccupied(loc_1)) {
            cost_1 = rc.senseRubble(loc_1) + 10 + dest.distanceSquaredTo(loc_1);
        }
        int cost_2 = 10000;
        if (rc.canSenseLocation(loc_2) && !rc.isLocationOccupied(loc_2)) {
            cost_2 = rc.senseRubble(loc_2) + 10 + dest.distanceSquaredTo(loc_2);
        }
        int cost_3 = 10000;
        if (rc.canSenseLocation(loc_3) && !rc.isLocationOccupied(loc_3)) {
            cost_3 = rc.senseRubble(loc_3) + 10 + dest.distanceSquaredTo(loc_3);
        }
        int cost_4 = 10000;
        if (rc.canSenseLocation(loc_4) && !rc.isLocationOccupied(loc_4)) {
            cost_4 = rc.senseRubble(loc_4) + 10 + dest.distanceSquaredTo(loc_4);
        }
        int cost_5 = 10000;
        if (rc.canSenseLocation(loc_5) && !rc.isLocationOccupied(loc_5)) {
            cost_5 = rc.senseRubble(loc_5) + 10 + dest.distanceSquaredTo(loc_5);
        }
        int cost_6 = 10000;
        if (rc.canSenseLocation(loc_6) && !rc.isLocationOccupied(loc_6)) {
            cost_6 = rc.senseRubble(loc_6) + 10 + dest.distanceSquaredTo(loc_6);
        }
// layer 2
        int cost_8 = cost_0;
        if (cost_1 < cost_8) cost_8 = cost_1;
        if (cost_2 < cost_8) cost_8 = cost_2;
        if (rc.canSenseLocation(loc_8) && !rc.isLocationOccupied(loc_8)) {
            cost_8 += rc.senseRubble(loc_8) + 10;
        } else cost_8 += 10000;
        int cost_9 = cost_1;
        if (cost_2 < cost_9) cost_9 = cost_2;
        if (cost_3 < cost_9) cost_9 = cost_3;
        if (rc.canSenseLocation(loc_9) && !rc.isLocationOccupied(loc_9)) {
            cost_9 += rc.senseRubble(loc_9) + 10;
        } else cost_9 += 10000;
        int cost_10 = cost_2;
        if (cost_3 < cost_10) cost_10 = cost_3;
        if (cost_4 < cost_10) cost_10 = cost_4;
        if (rc.canSenseLocation(loc_10) && !rc.isLocationOccupied(loc_10)) {
            cost_10 += rc.senseRubble(loc_10) + 10;
        } else cost_10 += 10000;
        int cost_11 = cost_3;
        if (cost_4 < cost_11) cost_11 = cost_4;
        if (cost_5 < cost_11) cost_11 = cost_5;
        if (rc.canSenseLocation(loc_11) && !rc.isLocationOccupied(loc_11)) {
            cost_11 += rc.senseRubble(loc_11) + 10;
        } else cost_11 += 10000;
        int cost_12 = cost_4;
        if (cost_5 < cost_12) cost_12 = cost_5;
        if (cost_6 < cost_12) cost_12 = cost_6;
        if (rc.canSenseLocation(loc_12) && !rc.isLocationOccupied(loc_12)) {
            cost_12 += rc.senseRubble(loc_12) + 10;
        } else cost_12 += 10000;
// layer 1
        int cost_16 = cost_8;
        if (cost_9 < cost_16) cost_16 = cost_9;
        if (cost_10 < cost_16) cost_16 = cost_10;
        if (rc.canSenseLocation(loc_16) && !rc.isLocationOccupied(loc_16)) {
            cost_16 += rc.senseRubble(loc_16) + 10;
        } else cost_16 += 10000;
        int cost_17 = cost_9;
        if (cost_10 < cost_17) cost_17 = cost_10;
        if (cost_11 < cost_17) cost_17 = cost_11;
        if (rc.canSenseLocation(loc_17) && !rc.isLocationOccupied(loc_17)) {
            cost_17 += rc.senseRubble(loc_17) + 10;
        } else cost_17 += 10000;
        int cost_18 = cost_10;
        if (cost_11 < cost_18) cost_18 = cost_11;
        if (cost_12 < cost_18) cost_18 = cost_12;
        if (rc.canSenseLocation(loc_18) && !rc.isLocationOccupied(loc_18)) {
            cost_18 += rc.senseRubble(loc_18) + 10;
        } else cost_18 += 10000;
        int cost_24 = cost_16;
        best_dir = Direction.NORTHWEST;
        if (cost_17 < cost_24) {
            cost_24 = cost_17;
            best_dir = Direction.NORTH;
        }
        if (cost_18 < cost_24) {
            cost_24 = cost_18;
            best_dir = Direction.NORTHEAST;
        }
        return best_dir;
    }

    private static Direction pathfind_SOUTH(RobotController rc, MapLocation dest) throws GameActionException {
        MapLocation m_location = rc.getLocation();
        Direction best_dir = null;
        MapLocation loc_0 = m_location.translate(-3, 3);
        MapLocation loc_1 = m_location.translate(-2, 3);
        MapLocation loc_2 = m_location.translate(-1, 3);
        MapLocation loc_3 = m_location.translate(0, 3);
        MapLocation loc_4 = m_location.translate(1, 3);
        MapLocation loc_5 = m_location.translate(2, 3);
        MapLocation loc_6 = m_location.translate(3, 3);
        MapLocation loc_7 = m_location.translate(-3, 2);
        MapLocation loc_8 = m_location.translate(-2, 2);
        MapLocation loc_9 = m_location.translate(-1, 2);
        MapLocation loc_10 = m_location.translate(0, 2);
        MapLocation loc_11 = m_location.translate(1, 2);
        MapLocation loc_12 = m_location.translate(2, 2);
        MapLocation loc_13 = m_location.translate(3, 2);
        MapLocation loc_14 = m_location.translate(-3, 1);
        MapLocation loc_15 = m_location.translate(-2, 1);
        MapLocation loc_16 = m_location.translate(-1, 1);
        MapLocation loc_17 = m_location.translate(0, 1);
        MapLocation loc_18 = m_location.translate(1, 1);
        MapLocation loc_19 = m_location.translate(2, 1);
        MapLocation loc_20 = m_location.translate(3, 1);
        MapLocation loc_21 = m_location.translate(-3, 0);
        MapLocation loc_22 = m_location.translate(-2, 0);
        MapLocation loc_23 = m_location.translate(-1, 0);
        MapLocation loc_24 = m_location.translate(0, 0);
        MapLocation loc_25 = m_location.translate(1, 0);
        MapLocation loc_26 = m_location.translate(2, 0);
        MapLocation loc_27 = m_location.translate(3, 0);
        MapLocation loc_28 = m_location.translate(-3, -1);
        MapLocation loc_29 = m_location.translate(-2, -1);
        MapLocation loc_30 = m_location.translate(-1, -1);
        MapLocation loc_31 = m_location.translate(0, -1);
        MapLocation loc_32 = m_location.translate(1, -1);
        MapLocation loc_33 = m_location.translate(2, -1);
        MapLocation loc_34 = m_location.translate(3, -1);
        MapLocation loc_35 = m_location.translate(-3, -2);
        MapLocation loc_36 = m_location.translate(-2, -2);
        MapLocation loc_37 = m_location.translate(-1, -2);
        MapLocation loc_38 = m_location.translate(0, -2);
        MapLocation loc_39 = m_location.translate(1, -2);
        MapLocation loc_40 = m_location.translate(2, -2);
        MapLocation loc_41 = m_location.translate(3, -2);
        MapLocation loc_42 = m_location.translate(-3, -3);
        MapLocation loc_43 = m_location.translate(-2, -3);
        MapLocation loc_44 = m_location.translate(-1, -3);
        MapLocation loc_45 = m_location.translate(0, -3);
        MapLocation loc_46 = m_location.translate(1, -3);
        MapLocation loc_47 = m_location.translate(2, -3);
        MapLocation loc_48 = m_location.translate(3, -3);
// layer 3
        int cost_48 = 10000;
        if (rc.canSenseLocation(loc_48) && !rc.isLocationOccupied(loc_48)) {
            cost_48 = rc.senseRubble(loc_48) + 10 + dest.distanceSquaredTo(loc_48);
        }
        int cost_47 = 10000;
        if (rc.canSenseLocation(loc_47) && !rc.isLocationOccupied(loc_47)) {
            cost_47 = rc.senseRubble(loc_47) + 10 + dest.distanceSquaredTo(loc_47);
        }
        int cost_46 = 10000;
        if (rc.canSenseLocation(loc_46) && !rc.isLocationOccupied(loc_46)) {
            cost_46 = rc.senseRubble(loc_46) + 10 + dest.distanceSquaredTo(loc_46);
        }
        int cost_45 = 10000;
        if (rc.canSenseLocation(loc_45) && !rc.isLocationOccupied(loc_45)) {
            cost_45 = rc.senseRubble(loc_45) + 10 + dest.distanceSquaredTo(loc_45);
        }
        int cost_44 = 10000;
        if (rc.canSenseLocation(loc_44) && !rc.isLocationOccupied(loc_44)) {
            cost_44 = rc.senseRubble(loc_44) + 10 + dest.distanceSquaredTo(loc_44);
        }
        int cost_43 = 10000;
        if (rc.canSenseLocation(loc_43) && !rc.isLocationOccupied(loc_43)) {
            cost_43 = rc.senseRubble(loc_43) + 10 + dest.distanceSquaredTo(loc_43);
        }
        int cost_42 = 10000;
        if (rc.canSenseLocation(loc_42) && !rc.isLocationOccupied(loc_42)) {
            cost_42 = rc.senseRubble(loc_42) + 10 + dest.distanceSquaredTo(loc_42);
        }
// layer 2
        int cost_40 = cost_48;
        if (cost_47 < cost_40) cost_40 = cost_47;
        if (cost_46 < cost_40) cost_40 = cost_46;
        if (rc.canSenseLocation(loc_40) && !rc.isLocationOccupied(loc_40)) {
            cost_40 += rc.senseRubble(loc_40) + 10;
        } else cost_40 += 10000;
        int cost_39 = cost_47;
        if (cost_46 < cost_39) cost_39 = cost_46;
        if (cost_45 < cost_39) cost_39 = cost_45;
        if (rc.canSenseLocation(loc_39) && !rc.isLocationOccupied(loc_39)) {
            cost_39 += rc.senseRubble(loc_39) + 10;
        } else cost_39 += 10000;
        int cost_38 = cost_46;
        if (cost_45 < cost_38) cost_38 = cost_45;
        if (cost_44 < cost_38) cost_38 = cost_44;
        if (rc.canSenseLocation(loc_38) && !rc.isLocationOccupied(loc_38)) {
            cost_38 += rc.senseRubble(loc_38) + 10;
        } else cost_38 += 10000;
        int cost_37 = cost_45;
        if (cost_44 < cost_37) cost_37 = cost_44;
        if (cost_43 < cost_37) cost_37 = cost_43;
        if (rc.canSenseLocation(loc_37) && !rc.isLocationOccupied(loc_37)) {
            cost_37 += rc.senseRubble(loc_37) + 10;
        } else cost_37 += 10000;
        int cost_36 = cost_44;
        if (cost_43 < cost_36) cost_36 = cost_43;
        if (cost_42 < cost_36) cost_36 = cost_42;
        if (rc.canSenseLocation(loc_36) && !rc.isLocationOccupied(loc_36)) {
            cost_36 += rc.senseRubble(loc_36) + 10;
        } else cost_36 += 10000;
// layer 1
        int cost_32 = cost_40;
        if (cost_39 < cost_32) cost_32 = cost_39;
        if (cost_38 < cost_32) cost_32 = cost_38;
        if (rc.canSenseLocation(loc_32) && !rc.isLocationOccupied(loc_32)) {
            cost_32 += rc.senseRubble(loc_32) + 10;
        } else cost_32 += 10000;
        int cost_31 = cost_39;
        if (cost_38 < cost_31) cost_31 = cost_38;
        if (cost_37 < cost_31) cost_31 = cost_37;
        if (rc.canSenseLocation(loc_31) && !rc.isLocationOccupied(loc_31)) {
            cost_31 += rc.senseRubble(loc_31) + 10;
        } else cost_31 += 10000;
        int cost_30 = cost_38;
        if (cost_37 < cost_30) cost_30 = cost_37;
        if (cost_36 < cost_30) cost_30 = cost_36;
        if (rc.canSenseLocation(loc_30) && !rc.isLocationOccupied(loc_30)) {
            cost_30 += rc.senseRubble(loc_30) + 10;
        } else cost_30 += 10000;
        int cost_24 = cost_32;
        best_dir = Direction.SOUTHEAST;
        if (cost_31 < cost_24) {
            cost_24 = cost_31;
            best_dir = Direction.SOUTH;
        }
        if (cost_30 < cost_24) {
            cost_24 = cost_30;
            best_dir = Direction.SOUTHWEST;
        }
        return best_dir;
    }

    private static Direction pathfind_NORTHEAST(RobotController rc, MapLocation dest) throws GameActionException {
        MapLocation m_location = rc.getLocation();
        Direction best_dir = null;
        MapLocation loc_0 = m_location.translate(-3, 3);
        MapLocation loc_1 = m_location.translate(-2, 3);
        MapLocation loc_2 = m_location.translate(-1, 3);
        MapLocation loc_3 = m_location.translate(0, 3);
        MapLocation loc_4 = m_location.translate(1, 3);
        MapLocation loc_5 = m_location.translate(2, 3);
        MapLocation loc_6 = m_location.translate(3, 3);
        MapLocation loc_7 = m_location.translate(-3, 2);
        MapLocation loc_8 = m_location.translate(-2, 2);
        MapLocation loc_9 = m_location.translate(-1, 2);
        MapLocation loc_10 = m_location.translate(0, 2);
        MapLocation loc_11 = m_location.translate(1, 2);
        MapLocation loc_12 = m_location.translate(2, 2);
        MapLocation loc_13 = m_location.translate(3, 2);
        MapLocation loc_14 = m_location.translate(-3, 1);
        MapLocation loc_15 = m_location.translate(-2, 1);
        MapLocation loc_16 = m_location.translate(-1, 1);
        MapLocation loc_17 = m_location.translate(0, 1);
        MapLocation loc_18 = m_location.translate(1, 1);
        MapLocation loc_19 = m_location.translate(2, 1);
        MapLocation loc_20 = m_location.translate(3, 1);
        MapLocation loc_21 = m_location.translate(-3, 0);
        MapLocation loc_22 = m_location.translate(-2, 0);
        MapLocation loc_23 = m_location.translate(-1, 0);
        MapLocation loc_24 = m_location.translate(0, 0);
        MapLocation loc_25 = m_location.translate(1, 0);
        MapLocation loc_26 = m_location.translate(2, 0);
        MapLocation loc_27 = m_location.translate(3, 0);
        MapLocation loc_28 = m_location.translate(-3, -1);
        MapLocation loc_29 = m_location.translate(-2, -1);
        MapLocation loc_30 = m_location.translate(-1, -1);
        MapLocation loc_31 = m_location.translate(0, -1);
        MapLocation loc_32 = m_location.translate(1, -1);
        MapLocation loc_33 = m_location.translate(2, -1);
        MapLocation loc_34 = m_location.translate(3, -1);
        MapLocation loc_35 = m_location.translate(-3, -2);
        MapLocation loc_36 = m_location.translate(-2, -2);
        MapLocation loc_37 = m_location.translate(-1, -2);
        MapLocation loc_38 = m_location.translate(0, -2);
        MapLocation loc_39 = m_location.translate(1, -2);
        MapLocation loc_40 = m_location.translate(2, -2);
        MapLocation loc_41 = m_location.translate(3, -2);
        MapLocation loc_42 = m_location.translate(-3, -3);
        MapLocation loc_43 = m_location.translate(-2, -3);
        MapLocation loc_44 = m_location.translate(-1, -3);
        MapLocation loc_45 = m_location.translate(0, -3);
        MapLocation loc_46 = m_location.translate(1, -3);
        MapLocation loc_47 = m_location.translate(2, -3);
        MapLocation loc_48 = m_location.translate(3, -3);
// layer 3
        int cost_3 = 10000;
        if (rc.canSenseLocation(loc_3) && !rc.isLocationOccupied(loc_3)) {
            cost_3 = rc.senseRubble(loc_3) + 10 + dest.distanceSquaredTo(loc_3);
        }
        int cost_4 = 10000;
        if (rc.canSenseLocation(loc_4) && !rc.isLocationOccupied(loc_4)) {
            cost_4 = rc.senseRubble(loc_4) + 10 + dest.distanceSquaredTo(loc_4);
        }
        int cost_5 = 10000;
        if (rc.canSenseLocation(loc_5) && !rc.isLocationOccupied(loc_5)) {
            cost_5 = rc.senseRubble(loc_5) + 10 + dest.distanceSquaredTo(loc_5);
        }
        int cost_12 = 10000;
        if (rc.canSenseLocation(loc_12) && !rc.isLocationOccupied(loc_12)) {
            cost_12 = rc.senseRubble(loc_12) + 10 + dest.distanceSquaredTo(loc_12);
        }
        int cost_6 = 10000;
        if (rc.canSenseLocation(loc_6) && !rc.isLocationOccupied(loc_6)) {
            cost_6 = rc.senseRubble(loc_6) + 10 + dest.distanceSquaredTo(loc_6);
        }
        int cost_13 = 10000;
        if (rc.canSenseLocation(loc_13) && !rc.isLocationOccupied(loc_13)) {
            cost_13 = rc.senseRubble(loc_13) + 10 + dest.distanceSquaredTo(loc_13);
        }
        int cost_20 = 10000;
        if (rc.canSenseLocation(loc_20) && !rc.isLocationOccupied(loc_20)) {
            cost_20 = rc.senseRubble(loc_20) + 10 + dest.distanceSquaredTo(loc_20);
        }
        int cost_27 = 10000;
        if (rc.canSenseLocation(loc_27) && !rc.isLocationOccupied(loc_27)) {
            cost_27 = rc.senseRubble(loc_27) + 10 + dest.distanceSquaredTo(loc_27);
        }
// layer 2
        int cost_10 = cost_3;
        if (cost_4 < cost_10) cost_10 = cost_4;
        if (rc.canSenseLocation(loc_10) && !rc.isLocationOccupied(loc_10)) {
            cost_10 += rc.senseRubble(loc_10) + 10;
        } else cost_10 += 10000;
        int cost_11 = cost_4;
        if (cost_5 < cost_11) cost_11 = cost_5;
        if (cost_12 < cost_11) cost_11 = cost_12;
        if (rc.canSenseLocation(loc_11) && !rc.isLocationOccupied(loc_11)) {
            cost_11 += rc.senseRubble(loc_11) + 10;
        } else cost_11 += 10000;
        if (cost_5 < cost_12) cost_12 = cost_5;
        if (cost_6 < cost_12) cost_12 = cost_6;
        if (cost_13 < cost_12) cost_12 = cost_13;
        if (rc.canSenseLocation(loc_12) && !rc.isLocationOccupied(loc_12)) {
            cost_12 += rc.senseRubble(loc_12) + 10;
        } else cost_12 += 10000;
        int cost_19 = cost_13;
        if (cost_20 < cost_19) cost_19 = cost_20;
        if (rc.canSenseLocation(loc_19) && !rc.isLocationOccupied(loc_19)) {
            cost_19 += rc.senseRubble(loc_19) + 10;
        } else cost_19 += 10000;
        int cost_26 = cost_20;
        if (cost_27 < cost_26) cost_26 = cost_27;
        if (rc.canSenseLocation(loc_26) && !rc.isLocationOccupied(loc_26)) {
            cost_26 += rc.senseRubble(loc_26) + 10;
        } else cost_26 += 10000;
// layer 1
        int cost_17 = cost_10;
        if (cost_11 < cost_17) cost_17 = cost_11;
        if (rc.canSenseLocation(loc_17) && !rc.isLocationOccupied(loc_17)) {
            cost_17 += rc.senseRubble(loc_17) + 10;
        } else cost_17 += 10000;
        int cost_18 = cost_11;
        if (cost_12 < cost_18) cost_18 = cost_12;
        if (cost_19 < cost_18) cost_18 = cost_19;
        if (rc.canSenseLocation(loc_18) && !rc.isLocationOccupied(loc_18)) {
            cost_18 += rc.senseRubble(loc_18) + 10;
        } else cost_18 += 10000;
        int cost_25 = cost_19;
        if (cost_26 < cost_25) cost_25 = cost_26;
        if (rc.canSenseLocation(loc_25) && !rc.isLocationOccupied(loc_25)) {
            cost_25 += rc.senseRubble(loc_25) + 10;
        } else cost_25 += 10000;
        int cost_24 = cost_17;
        best_dir = Direction.NORTH;
        if (cost_18 < cost_24) {
            cost_24 = cost_18;
            best_dir = Direction.NORTHEAST;
        }
        if (cost_25 < cost_24) {
            cost_24 = cost_25;
            best_dir = Direction.EAST;
        }
        return best_dir;
    }

    private static Direction pathfind_NORTHWEST(RobotController rc, MapLocation dest) throws GameActionException {
        MapLocation m_location = rc.getLocation();
        Direction best_dir = null;
        MapLocation loc_0 = m_location.translate(-3, 3);
        MapLocation loc_1 = m_location.translate(-2, 3);
        MapLocation loc_2 = m_location.translate(-1, 3);
        MapLocation loc_3 = m_location.translate(0, 3);
        MapLocation loc_4 = m_location.translate(1, 3);
        MapLocation loc_5 = m_location.translate(2, 3);
        MapLocation loc_6 = m_location.translate(3, 3);
        MapLocation loc_7 = m_location.translate(-3, 2);
        MapLocation loc_8 = m_location.translate(-2, 2);
        MapLocation loc_9 = m_location.translate(-1, 2);
        MapLocation loc_10 = m_location.translate(0, 2);
        MapLocation loc_11 = m_location.translate(1, 2);
        MapLocation loc_12 = m_location.translate(2, 2);
        MapLocation loc_13 = m_location.translate(3, 2);
        MapLocation loc_14 = m_location.translate(-3, 1);
        MapLocation loc_15 = m_location.translate(-2, 1);
        MapLocation loc_16 = m_location.translate(-1, 1);
        MapLocation loc_17 = m_location.translate(0, 1);
        MapLocation loc_18 = m_location.translate(1, 1);
        MapLocation loc_19 = m_location.translate(2, 1);
        MapLocation loc_20 = m_location.translate(3, 1);
        MapLocation loc_21 = m_location.translate(-3, 0);
        MapLocation loc_22 = m_location.translate(-2, 0);
        MapLocation loc_23 = m_location.translate(-1, 0);
        MapLocation loc_24 = m_location.translate(0, 0);
        MapLocation loc_25 = m_location.translate(1, 0);
        MapLocation loc_26 = m_location.translate(2, 0);
        MapLocation loc_27 = m_location.translate(3, 0);
        MapLocation loc_28 = m_location.translate(-3, -1);
        MapLocation loc_29 = m_location.translate(-2, -1);
        MapLocation loc_30 = m_location.translate(-1, -1);
        MapLocation loc_31 = m_location.translate(0, -1);
        MapLocation loc_32 = m_location.translate(1, -1);
        MapLocation loc_33 = m_location.translate(2, -1);
        MapLocation loc_34 = m_location.translate(3, -1);
        MapLocation loc_35 = m_location.translate(-3, -2);
        MapLocation loc_36 = m_location.translate(-2, -2);
        MapLocation loc_37 = m_location.translate(-1, -2);
        MapLocation loc_38 = m_location.translate(0, -2);
        MapLocation loc_39 = m_location.translate(1, -2);
        MapLocation loc_40 = m_location.translate(2, -2);
        MapLocation loc_41 = m_location.translate(3, -2);
        MapLocation loc_42 = m_location.translate(-3, -3);
        MapLocation loc_43 = m_location.translate(-2, -3);
        MapLocation loc_44 = m_location.translate(-1, -3);
        MapLocation loc_45 = m_location.translate(0, -3);
        MapLocation loc_46 = m_location.translate(1, -3);
        MapLocation loc_47 = m_location.translate(2, -3);
        MapLocation loc_48 = m_location.translate(3, -3);
// layer 3
        int cost_21 = 10000;
        if (rc.canSenseLocation(loc_21) && !rc.isLocationOccupied(loc_21)) {
            cost_21 = rc.senseRubble(loc_21) + 10 + dest.distanceSquaredTo(loc_21);
        }
        int cost_14 = 10000;
        if (rc.canSenseLocation(loc_14) && !rc.isLocationOccupied(loc_14)) {
            cost_14 = rc.senseRubble(loc_14) + 10 + dest.distanceSquaredTo(loc_14);
        }
        int cost_7 = 10000;
        if (rc.canSenseLocation(loc_7) && !rc.isLocationOccupied(loc_7)) {
            cost_7 = rc.senseRubble(loc_7) + 10 + dest.distanceSquaredTo(loc_7);
        }
        int cost_8 = 10000;
        if (rc.canSenseLocation(loc_8) && !rc.isLocationOccupied(loc_8)) {
            cost_8 = rc.senseRubble(loc_8) + 10 + dest.distanceSquaredTo(loc_8);
        }
        int cost_0 = 10000;
        if (rc.canSenseLocation(loc_0) && !rc.isLocationOccupied(loc_0)) {
            cost_0 = rc.senseRubble(loc_0) + 10 + dest.distanceSquaredTo(loc_0);
        }
        int cost_1 = 10000;
        if (rc.canSenseLocation(loc_1) && !rc.isLocationOccupied(loc_1)) {
            cost_1 = rc.senseRubble(loc_1) + 10 + dest.distanceSquaredTo(loc_1);
        }
        int cost_2 = 10000;
        if (rc.canSenseLocation(loc_2) && !rc.isLocationOccupied(loc_2)) {
            cost_2 = rc.senseRubble(loc_2) + 10 + dest.distanceSquaredTo(loc_2);
        }
        int cost_3 = 10000;
        if (rc.canSenseLocation(loc_3) && !rc.isLocationOccupied(loc_3)) {
            cost_3 = rc.senseRubble(loc_3) + 10 + dest.distanceSquaredTo(loc_3);
        }
// layer 2
        int cost_22 = cost_21;
        if (cost_14 < cost_22) cost_22 = cost_14;
        if (rc.canSenseLocation(loc_22) && !rc.isLocationOccupied(loc_22)) {
            cost_22 += rc.senseRubble(loc_22) + 10;
        } else cost_22 += 10000;
        int cost_15 = cost_14;
        if (cost_7 < cost_15) cost_15 = cost_7;
        if (cost_8 < cost_15) cost_15 = cost_8;
        if (rc.canSenseLocation(loc_15) && !rc.isLocationOccupied(loc_15)) {
            cost_15 += rc.senseRubble(loc_15) + 10;
        } else cost_15 += 10000;
        if (cost_7 < cost_8) cost_8 = cost_7;
        if (cost_0 < cost_8) cost_8 = cost_0;
        if (cost_1 < cost_8) cost_8 = cost_1;
        if (rc.canSenseLocation(loc_8) && !rc.isLocationOccupied(loc_8)) {
            cost_8 += rc.senseRubble(loc_8) + 10;
        } else cost_8 += 10000;
        int cost_9 = cost_1;
        if (cost_2 < cost_9) cost_9 = cost_2;
        if (rc.canSenseLocation(loc_9) && !rc.isLocationOccupied(loc_9)) {
            cost_9 += rc.senseRubble(loc_9) + 10;
        } else cost_9 += 10000;
        int cost_10 = cost_2;
        if (cost_3 < cost_10) cost_10 = cost_3;
        if (rc.canSenseLocation(loc_10) && !rc.isLocationOccupied(loc_10)) {
            cost_10 += rc.senseRubble(loc_10) + 10;
        } else cost_10 += 10000;
// layer 1
        int cost_23 = cost_22;
        if (cost_15 < cost_23) cost_23 = cost_15;
        if (rc.canSenseLocation(loc_23) && !rc.isLocationOccupied(loc_23)) {
            cost_23 += rc.senseRubble(loc_23) + 10;
        } else cost_23 += 10000;
        int cost_16 = cost_15;
        if (cost_8 < cost_16) cost_16 = cost_8;
        if (cost_9 < cost_16) cost_16 = cost_9;
        if (rc.canSenseLocation(loc_16) && !rc.isLocationOccupied(loc_16)) {
            cost_16 += rc.senseRubble(loc_16) + 10;
        } else cost_16 += 10000;
        int cost_17 = cost_9;
        if (cost_10 < cost_17) cost_17 = cost_10;
        if (rc.canSenseLocation(loc_17) && !rc.isLocationOccupied(loc_17)) {
            cost_17 += rc.senseRubble(loc_17) + 10;
        } else cost_17 += 10000;
        int cost_24 = cost_23;
        best_dir = Direction.WEST;
        if (cost_16 < cost_24) {
            cost_24 = cost_16;
            best_dir = Direction.NORTHWEST;
        }
        if (cost_17 < cost_24) {
            cost_24 = cost_17;
            best_dir = Direction.NORTH;
        }
        return best_dir;
    }

    private static Direction pathfind_SOUTHEAST(RobotController rc, MapLocation dest) throws GameActionException {
        MapLocation m_location = rc.getLocation();
        Direction best_dir = null;
        MapLocation loc_0 = m_location.translate(-3, 3);
        MapLocation loc_1 = m_location.translate(-2, 3);
        MapLocation loc_2 = m_location.translate(-1, 3);
        MapLocation loc_3 = m_location.translate(0, 3);
        MapLocation loc_4 = m_location.translate(1, 3);
        MapLocation loc_5 = m_location.translate(2, 3);
        MapLocation loc_6 = m_location.translate(3, 3);
        MapLocation loc_7 = m_location.translate(-3, 2);
        MapLocation loc_8 = m_location.translate(-2, 2);
        MapLocation loc_9 = m_location.translate(-1, 2);
        MapLocation loc_10 = m_location.translate(0, 2);
        MapLocation loc_11 = m_location.translate(1, 2);
        MapLocation loc_12 = m_location.translate(2, 2);
        MapLocation loc_13 = m_location.translate(3, 2);
        MapLocation loc_14 = m_location.translate(-3, 1);
        MapLocation loc_15 = m_location.translate(-2, 1);
        MapLocation loc_16 = m_location.translate(-1, 1);
        MapLocation loc_17 = m_location.translate(0, 1);
        MapLocation loc_18 = m_location.translate(1, 1);
        MapLocation loc_19 = m_location.translate(2, 1);
        MapLocation loc_20 = m_location.translate(3, 1);
        MapLocation loc_21 = m_location.translate(-3, 0);
        MapLocation loc_22 = m_location.translate(-2, 0);
        MapLocation loc_23 = m_location.translate(-1, 0);
        MapLocation loc_24 = m_location.translate(0, 0);
        MapLocation loc_25 = m_location.translate(1, 0);
        MapLocation loc_26 = m_location.translate(2, 0);
        MapLocation loc_27 = m_location.translate(3, 0);
        MapLocation loc_28 = m_location.translate(-3, -1);
        MapLocation loc_29 = m_location.translate(-2, -1);
        MapLocation loc_30 = m_location.translate(-1, -1);
        MapLocation loc_31 = m_location.translate(0, -1);
        MapLocation loc_32 = m_location.translate(1, -1);
        MapLocation loc_33 = m_location.translate(2, -1);
        MapLocation loc_34 = m_location.translate(3, -1);
        MapLocation loc_35 = m_location.translate(-3, -2);
        MapLocation loc_36 = m_location.translate(-2, -2);
        MapLocation loc_37 = m_location.translate(-1, -2);
        MapLocation loc_38 = m_location.translate(0, -2);
        MapLocation loc_39 = m_location.translate(1, -2);
        MapLocation loc_40 = m_location.translate(2, -2);
        MapLocation loc_41 = m_location.translate(3, -2);
        MapLocation loc_42 = m_location.translate(-3, -3);
        MapLocation loc_43 = m_location.translate(-2, -3);
        MapLocation loc_44 = m_location.translate(-1, -3);
        MapLocation loc_45 = m_location.translate(0, -3);
        MapLocation loc_46 = m_location.translate(1, -3);
        MapLocation loc_47 = m_location.translate(2, -3);
        MapLocation loc_48 = m_location.translate(3, -3);
// layer 3
        int cost_27 = 10000;
        if (rc.canSenseLocation(loc_27) && !rc.isLocationOccupied(loc_27)) {
            cost_27 = rc.senseRubble(loc_27) + 10 + dest.distanceSquaredTo(loc_27);
        }
        int cost_34 = 10000;
        if (rc.canSenseLocation(loc_34) && !rc.isLocationOccupied(loc_34)) {
            cost_34 = rc.senseRubble(loc_34) + 10 + dest.distanceSquaredTo(loc_34);
        }
        int cost_41 = 10000;
        if (rc.canSenseLocation(loc_41) && !rc.isLocationOccupied(loc_41)) {
            cost_41 = rc.senseRubble(loc_41) + 10 + dest.distanceSquaredTo(loc_41);
        }
        int cost_40 = 10000;
        if (rc.canSenseLocation(loc_40) && !rc.isLocationOccupied(loc_40)) {
            cost_40 = rc.senseRubble(loc_40) + 10 + dest.distanceSquaredTo(loc_40);
        }
        int cost_48 = 10000;
        if (rc.canSenseLocation(loc_48) && !rc.isLocationOccupied(loc_48)) {
            cost_48 = rc.senseRubble(loc_48) + 10 + dest.distanceSquaredTo(loc_48);
        }
        int cost_47 = 10000;
        if (rc.canSenseLocation(loc_47) && !rc.isLocationOccupied(loc_47)) {
            cost_47 = rc.senseRubble(loc_47) + 10 + dest.distanceSquaredTo(loc_47);
        }
        int cost_46 = 10000;
        if (rc.canSenseLocation(loc_46) && !rc.isLocationOccupied(loc_46)) {
            cost_46 = rc.senseRubble(loc_46) + 10 + dest.distanceSquaredTo(loc_46);
        }
        int cost_45 = 10000;
        if (rc.canSenseLocation(loc_45) && !rc.isLocationOccupied(loc_45)) {
            cost_45 = rc.senseRubble(loc_45) + 10 + dest.distanceSquaredTo(loc_45);
        }
// layer 2
        int cost_26 = cost_27;
        if (cost_34 < cost_26) cost_26 = cost_34;
        if (rc.canSenseLocation(loc_26) && !rc.isLocationOccupied(loc_26)) {
            cost_26 += rc.senseRubble(loc_26) + 10;
        } else cost_26 += 10000;
        int cost_33 = cost_34;
        if (cost_41 < cost_33) cost_33 = cost_41;
        if (cost_40 < cost_33) cost_33 = cost_40;
        if (rc.canSenseLocation(loc_33) && !rc.isLocationOccupied(loc_33)) {
            cost_33 += rc.senseRubble(loc_33) + 10;
        } else cost_33 += 10000;
        if (cost_41 < cost_40) cost_40 = cost_41;
        if (cost_48 < cost_40) cost_40 = cost_48;
        if (cost_47 < cost_40) cost_40 = cost_47;
        if (rc.canSenseLocation(loc_40) && !rc.isLocationOccupied(loc_40)) {
            cost_40 += rc.senseRubble(loc_40) + 10;
        } else cost_40 += 10000;
        int cost_39 = cost_47;
        if (cost_46 < cost_39) cost_39 = cost_46;
        if (rc.canSenseLocation(loc_39) && !rc.isLocationOccupied(loc_39)) {
            cost_39 += rc.senseRubble(loc_39) + 10;
        } else cost_39 += 10000;
        int cost_38 = cost_46;
        if (cost_45 < cost_38) cost_38 = cost_45;
        if (rc.canSenseLocation(loc_38) && !rc.isLocationOccupied(loc_38)) {
            cost_38 += rc.senseRubble(loc_38) + 10;
        } else cost_38 += 10000;
// layer 1
        int cost_25 = cost_26;
        if (cost_33 < cost_25) cost_25 = cost_33;
        if (rc.canSenseLocation(loc_25) && !rc.isLocationOccupied(loc_25)) {
            cost_25 += rc.senseRubble(loc_25) + 10;
        } else cost_25 += 10000;
        int cost_32 = cost_33;
        if (cost_40 < cost_32) cost_32 = cost_40;
        if (cost_39 < cost_32) cost_32 = cost_39;
        if (rc.canSenseLocation(loc_32) && !rc.isLocationOccupied(loc_32)) {
            cost_32 += rc.senseRubble(loc_32) + 10;
        } else cost_32 += 10000;
        int cost_31 = cost_39;
        if (cost_38 < cost_31) cost_31 = cost_38;
        if (rc.canSenseLocation(loc_31) && !rc.isLocationOccupied(loc_31)) {
            cost_31 += rc.senseRubble(loc_31) + 10;
        } else cost_31 += 10000;
        int cost_24 = cost_25;
        best_dir = Direction.EAST;
        if (cost_32 < cost_24) {
            cost_24 = cost_32;
            best_dir = Direction.SOUTHEAST;
        }
        if (cost_31 < cost_24) {
            cost_24 = cost_31;
            best_dir = Direction.SOUTH;
        }
        return best_dir;
    }

    private static Direction pathfind_SOUTHWEST(RobotController rc, MapLocation dest) throws GameActionException {
        MapLocation m_location = rc.getLocation();
        Direction best_dir = null;
        MapLocation loc_0 = m_location.translate(-3, 3);
        MapLocation loc_1 = m_location.translate(-2, 3);
        MapLocation loc_2 = m_location.translate(-1, 3);
        MapLocation loc_3 = m_location.translate(0, 3);
        MapLocation loc_4 = m_location.translate(1, 3);
        MapLocation loc_5 = m_location.translate(2, 3);
        MapLocation loc_6 = m_location.translate(3, 3);
        MapLocation loc_7 = m_location.translate(-3, 2);
        MapLocation loc_8 = m_location.translate(-2, 2);
        MapLocation loc_9 = m_location.translate(-1, 2);
        MapLocation loc_10 = m_location.translate(0, 2);
        MapLocation loc_11 = m_location.translate(1, 2);
        MapLocation loc_12 = m_location.translate(2, 2);
        MapLocation loc_13 = m_location.translate(3, 2);
        MapLocation loc_14 = m_location.translate(-3, 1);
        MapLocation loc_15 = m_location.translate(-2, 1);
        MapLocation loc_16 = m_location.translate(-1, 1);
        MapLocation loc_17 = m_location.translate(0, 1);
        MapLocation loc_18 = m_location.translate(1, 1);
        MapLocation loc_19 = m_location.translate(2, 1);
        MapLocation loc_20 = m_location.translate(3, 1);
        MapLocation loc_21 = m_location.translate(-3, 0);
        MapLocation loc_22 = m_location.translate(-2, 0);
        MapLocation loc_23 = m_location.translate(-1, 0);
        MapLocation loc_24 = m_location.translate(0, 0);
        MapLocation loc_25 = m_location.translate(1, 0);
        MapLocation loc_26 = m_location.translate(2, 0);
        MapLocation loc_27 = m_location.translate(3, 0);
        MapLocation loc_28 = m_location.translate(-3, -1);
        MapLocation loc_29 = m_location.translate(-2, -1);
        MapLocation loc_30 = m_location.translate(-1, -1);
        MapLocation loc_31 = m_location.translate(0, -1);
        MapLocation loc_32 = m_location.translate(1, -1);
        MapLocation loc_33 = m_location.translate(2, -1);
        MapLocation loc_34 = m_location.translate(3, -1);
        MapLocation loc_35 = m_location.translate(-3, -2);
        MapLocation loc_36 = m_location.translate(-2, -2);
        MapLocation loc_37 = m_location.translate(-1, -2);
        MapLocation loc_38 = m_location.translate(0, -2);
        MapLocation loc_39 = m_location.translate(1, -2);
        MapLocation loc_40 = m_location.translate(2, -2);
        MapLocation loc_41 = m_location.translate(3, -2);
        MapLocation loc_42 = m_location.translate(-3, -3);
        MapLocation loc_43 = m_location.translate(-2, -3);
        MapLocation loc_44 = m_location.translate(-1, -3);
        MapLocation loc_45 = m_location.translate(0, -3);
        MapLocation loc_46 = m_location.translate(1, -3);
        MapLocation loc_47 = m_location.translate(2, -3);
        MapLocation loc_48 = m_location.translate(3, -3);
// layer 3
        int cost_45 = 10000;
        if (rc.canSenseLocation(loc_45) && !rc.isLocationOccupied(loc_45)) {
            cost_45 = rc.senseRubble(loc_45) + 10 + dest.distanceSquaredTo(loc_45);
        }
        int cost_44 = 10000;
        if (rc.canSenseLocation(loc_44) && !rc.isLocationOccupied(loc_44)) {
            cost_44 = rc.senseRubble(loc_44) + 10 + dest.distanceSquaredTo(loc_44);
        }
        int cost_43 = 10000;
        if (rc.canSenseLocation(loc_43) && !rc.isLocationOccupied(loc_43)) {
            cost_43 = rc.senseRubble(loc_43) + 10 + dest.distanceSquaredTo(loc_43);
        }
        int cost_36 = 10000;
        if (rc.canSenseLocation(loc_36) && !rc.isLocationOccupied(loc_36)) {
            cost_36 = rc.senseRubble(loc_36) + 10 + dest.distanceSquaredTo(loc_36);
        }
        int cost_42 = 10000;
        if (rc.canSenseLocation(loc_42) && !rc.isLocationOccupied(loc_42)) {
            cost_42 = rc.senseRubble(loc_42) + 10 + dest.distanceSquaredTo(loc_42);
        }
        int cost_35 = 10000;
        if (rc.canSenseLocation(loc_35) && !rc.isLocationOccupied(loc_35)) {
            cost_35 = rc.senseRubble(loc_35) + 10 + dest.distanceSquaredTo(loc_35);
        }
        int cost_28 = 10000;
        if (rc.canSenseLocation(loc_28) && !rc.isLocationOccupied(loc_28)) {
            cost_28 = rc.senseRubble(loc_28) + 10 + dest.distanceSquaredTo(loc_28);
        }
        int cost_21 = 10000;
        if (rc.canSenseLocation(loc_21) && !rc.isLocationOccupied(loc_21)) {
            cost_21 = rc.senseRubble(loc_21) + 10 + dest.distanceSquaredTo(loc_21);
        }
// layer 2
        int cost_38 = cost_45;
        if (cost_44 < cost_38) cost_38 = cost_44;
        if (rc.canSenseLocation(loc_38) && !rc.isLocationOccupied(loc_38)) {
            cost_38 += rc.senseRubble(loc_38) + 10;
        } else cost_38 += 10000;
        int cost_37 = cost_44;
        if (cost_43 < cost_37) cost_37 = cost_43;
        if (cost_36 < cost_37) cost_37 = cost_36;
        if (rc.canSenseLocation(loc_37) && !rc.isLocationOccupied(loc_37)) {
            cost_37 += rc.senseRubble(loc_37) + 10;
        } else cost_37 += 10000;
        if (cost_43 < cost_36) cost_36 = cost_43;
        if (cost_42 < cost_36) cost_36 = cost_42;
        if (cost_35 < cost_36) cost_36 = cost_35;
        if (rc.canSenseLocation(loc_36) && !rc.isLocationOccupied(loc_36)) {
            cost_36 += rc.senseRubble(loc_36) + 10;
        } else cost_36 += 10000;
        int cost_29 = cost_35;
        if (cost_28 < cost_29) cost_29 = cost_28;
        if (rc.canSenseLocation(loc_29) && !rc.isLocationOccupied(loc_29)) {
            cost_29 += rc.senseRubble(loc_29) + 10;
        } else cost_29 += 10000;
        int cost_22 = cost_28;
        if (cost_21 < cost_22) cost_22 = cost_21;
        if (rc.canSenseLocation(loc_22) && !rc.isLocationOccupied(loc_22)) {
            cost_22 += rc.senseRubble(loc_22) + 10;
        } else cost_22 += 10000;
// layer 1
        int cost_31 = cost_38;
        if (cost_37 < cost_31) cost_31 = cost_37;
        if (rc.canSenseLocation(loc_31) && !rc.isLocationOccupied(loc_31)) {
            cost_31 += rc.senseRubble(loc_31) + 10;
        } else cost_31 += 10000;
        int cost_30 = cost_37;
        if (cost_36 < cost_30) cost_30 = cost_36;
        if (cost_29 < cost_30) cost_30 = cost_29;
        if (rc.canSenseLocation(loc_30) && !rc.isLocationOccupied(loc_30)) {
            cost_30 += rc.senseRubble(loc_30) + 10;
        } else cost_30 += 10000;
        int cost_23 = cost_29;
        if (cost_22 < cost_23) cost_23 = cost_22;
        if (rc.canSenseLocation(loc_23) && !rc.isLocationOccupied(loc_23)) {
            cost_23 += rc.senseRubble(loc_23) + 10;
        } else cost_23 += 10000;
        int cost_24 = cost_31;
        best_dir = Direction.SOUTH;
        if (cost_30 < cost_24) {
            cost_24 = cost_30;
            best_dir = Direction.SOUTHWEST;
        }
        if (cost_23 < cost_24) {
            cost_24 = cost_23;
            best_dir = Direction.WEST;
        }
        return best_dir;
    }
}