package pathfinder;

import battlecode.common.*;


class Soldier {
    static Direction previousStep = Direction.CENTER;
    private static MapLocation destination = null;
    private static int soldierLowDist = 15;
    private static int soldierHighDist = 20;
    static MapLocation spawn;
    static void setup(RobotController rc) throws GameActionException {
        spawn = rc.getLocation();
    }

    static int nextMove(RobotController rc, MapLocation cur, int depth, Direction lastDir) throws GameActionException {
        int curDist = cur.distanceSquaredTo(destination);
        int score = Integer.MAX_VALUE;
        int rubble = rc.senseRubble(cur);
        if (rc.isLocationOccupied(cur) && cur != rc.getLocation()) rubble = 1000;
        Direction nextDirection = Direction.CENTER;

        if (cur.equals(destination)) {
            return 0;
        } else if (depth == 3) {
            return (20 + rubble) + curDist;
        }

        int toDest = cur.directionTo(destination).ordinal();

        for (int i = (7 + toDest); i < (10 + toDest); ++i) {
            Direction dir = Constants.directions[i % 8];
            if (dir.equals(lastDir.opposite())) continue;
            MapLocation next = cur.add(dir);
            if (rc.canSenseLocation(next)) {
//                int dist = next.distanceSquaredTo(destination);
//                if (dist <= curDist) {
                int nextScore = 20 + rubble + nextMove(rc, next, depth + 1, dir);
                if (score > nextScore) {
                    score = nextScore;
                    nextDirection = dir;
                }
//                }
            }
        }

//        for (Direction dir : Constants.directions) {
//            if (dir.equals(lastDir.opposite())) continue;
//            MapLocation next = cur.add(dir);
//
//            if (rc.canSenseLocation(next)) {
//                int nextDist = next.distanceSquaredTo(destination);
//                if (nextDist <= curDist) {
//                    int nextScore = 10 + rubble + nextMove(rc, next, depth+1, dir);
//                    if (score > nextScore) {
//                        score = nextScore;
//                        nextDirection = dir;
//                    }
//                }
//            }
//        }

        if (depth == 0) {
            if (rc.canMove(nextDirection)) {
                previousStep = nextDirection;
                rc.move(nextDirection);
            } else {
                for (Direction dir : Constants.directions) {
                    MapLocation next = cur.add(dir);
                    if (rc.canSenseLocation(next) && next.distanceSquaredTo(destination) <= curDist) {
                        if (rc.canMove(dir)) rc.move(dir);
                    }
                }
            }
        }

        return score;
    }

    static void run(RobotController rc) throws GameActionException {
//        if (destination == null || rc.getLocation().equals(destination)) {
//            destination = new MapLocation((Utils.rng.nextInt(rc.getMapWidth()) - 3) + 3, (Utils.rng.nextInt(rc.getMapHeight() - 3) + 3));
//        }
        if (destination == null) {
            destination = new MapLocation((Utils.rng.nextInt(rc.getMapWidth()) - 3) + 3, (Utils.rng.nextInt(rc.getMapHeight() - 3) + 3));
        }
        rc.setIndicatorString(destination.toString());

        nextMove(rc, rc.getLocation(), 0, previousStep);

        if (rc.getRoundNum() % 100 == 0) {
//            if (rc.getRoundNum() % 200 == 0) {
                int loc = rc.readSharedArray(0);
                destination = new MapLocation((loc >> 6) & 63, loc & 63);
//            } else {
//                int loc = rc.readSharedArray(63);
//                destination = new MapLocation((loc >> 6) & 63, loc & 63);
//            }
        }


        for (RobotInfo robotInfo : rc.senseNearbyRobots(-1, rc.getTeam().opponent())) {
            if (rc.canAttack(robotInfo.location)) {
                rc.attack(robotInfo.location);
                rc.writeSharedArray(0, (robotInfo.location.x << 6) + robotInfo.location.y);
            }
        }

    }
}
