package pathfinder;

import battlecode.common.*;


class Miner {

    static int numReached = 0;
    static int numSteps = 0;
    static Direction previousStep = Direction.CENTER;

    static final int minerLowDist = 10;
    static final int minerHighDist = 18;
    static MapLocation destination = null;
    static MapLocation spawn = null;
    static int highX = 0;
    static int lowX = 0;
    static int highY = 0;
    static int lowY = 0;
    static int near = 0;

    static int nextMove(RobotController rc, MapLocation cur, int depth, Direction lastDir) throws GameActionException {
        int curDist = cur.distanceSquaredTo(destination);
        int score = Integer.MAX_VALUE;
        int rubble = rc.senseRubble(cur);
        if (rc.isLocationOccupied(cur) && cur != rc.getLocation()) rubble = 1000;
        Direction nextDirection = Direction.CENTER;

        if (cur.equals(destination)) {
            return 0;
        } else if (depth == 3) {
            return (10 + rubble) + curDist;
        }

        int toDest = cur.directionTo(destination).ordinal();

        for (int i = (7 + toDest); i < (10 + toDest); ++i) {
            Direction dir = Constants.directions[i % 8];
            if (dir.equals(lastDir.opposite())) continue;
            MapLocation next = cur.add(dir);
            if (rc.canSenseLocation(next)) {
                int nextScore = 10 + rubble + nextMove(rc, next, depth + 1, dir);
                if (score > nextScore) {
                    score = nextScore;
                    nextDirection = dir;
                }
            }
        }

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

    static Direction directMove(RobotController rc) throws GameActionException {
        return rc.getLocation().directionTo(destination);
    }

    static void setup(RobotController rc) throws GameActionException {
        spawn = rc.getLocation();
        lowX = Math.max(spawn.x - minerLowDist, 0);
        highX = Math.min(spawn.x + minerLowDist, rc.getMapWidth());
        lowY = Math.max(spawn.y - minerLowDist, 0);
        highY = Math.min(spawn.y + minerLowDist, rc.getMapHeight());
    }

    static int called = 0;

    static void run(RobotController rc) throws GameActionException {
        MapLocation curLoc = rc.getLocation();
        if (destination == null || (curLoc.equals(destination))) {
            ++numReached;
            destination = new MapLocation((Utils.rng.nextInt(rc.getMapWidth()) - 3) + 3, (Utils.rng.nextInt(rc.getMapHeight() - 3) + 3));
        }
        rc.setIndicatorLine(curLoc, destination, 255, 255, 255);

        int curDist = curLoc.distanceSquaredTo(destination);

        int startCount = Clock.getBytecodeNum();
        Direction nextDirection = UnrolledPathfinder.pathfind(rc, destination);
//        Direction nextDirection = UnrolledPathfinder.pathfind(rc, destination);

        if (rc.canMove(nextDirection)) {
            previousStep = nextDirection;
            rc.move(nextDirection);
        } else if (curDist > 2) {
            for (Direction dir : Constants.directions) {
                MapLocation next = curLoc.add(dir);
                if (rc.canSenseLocation(next) && next.distanceSquaredTo(destination) <= curDist) {
                    if (rc.canMove(dir)) rc.move(dir);
                }
            }
        }


        if (rc.getRoundNum() >= 1998) {
            System.out.println(rc.getID() + " Reached " + numReached + " Locations " + called);
            int x = rc.readSharedArray(20);
            rc.writeSharedArray(20, x + numReached);
            rc.disintegrate();
        }


    }
}
