package monkey2;

import battlecode.common.*;

import java.util.Arrays;

import static monkey2.Constants.directions;


class Builder {
    static Direction previousStep = Direction.CENTER;
    static MapLocation destination = null;
    static MapLocation spawn;

    static MapLocation[] closerToDest(RobotController rc, MapLocation cur) throws GameActionException {
        MapLocation[] ret = new MapLocation[8];
        int tot = 0;
        int curDistSq = cur.distanceSquaredTo(destination);
        for (Direction dir : directions) {
            if (rc.canSenseLocation(cur.add(dir)) && cur.add(dir).distanceSquaredTo(destination) <= curDistSq) {
                ret[tot++] = cur.add(dir);
            }
        }
        return Arrays.copyOfRange(ret, 0, tot);
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
            Direction dir = directions[i % 8];
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
                for (Direction dir : directions) {
                    MapLocation next = cur.add(dir);
                    if (rc.canSenseLocation(next) && next.distanceSquaredTo(destination) <= curDist) {
                        if (rc.canMove(dir)) rc.move(dir);
                    }
                }
            }
        }

        return score;
    }

    static void setup(RobotController rc) throws GameActionException {
        spawn = rc.getLocation();
    }

    static void run(RobotController rc) throws GameActionException {
        if (destination == null) {
//            destination = new MapLocation(Utils.randomInt(lowX, highX), Utils.randomInt(lowY, highY));
//            destination = new MapLocation(Utils.randomInt(lowX, highX), Utils.randomInt(lowY, highY));
            destination = new MapLocation((Utils.rng.nextInt(rc.getMapWidth()) - 3) + 3, (Utils.rng.nextInt(rc.getMapHeight() - 3) + 3));
        }

        nextMove(rc, rc.getLocation(), 0, previousStep);

        // FIX BUILDER
        if (rc.getLocation().equals(destination)) {
            boolean arePrototypes = false;
            for (Direction dir : Direction.cardinalDirections()) {
                MapLocation dirLoc = rc.getLocation().add(dir);
                if (rc.onTheMap(dirLoc)) {
                    RobotInfo watchTower = rc.senseRobotAtLocation(dirLoc);
                    if (watchTower != null && watchTower.team.isPlayer() && watchTower.mode == RobotMode.PROTOTYPE && rc.canRepair(dirLoc)) {
                        rc.repair(dirLoc);
                    } else if (rc.canBuildRobot(RobotType.WATCHTOWER, dir)) {
                        rc.buildRobot(RobotType.WATCHTOWER, dir);
                    }
                }
            }
//            for (Direction dir : Direction.cardinalDirections()) {
//                RobotInfo info = rc.senseRobotAtLocation(rc.adjacentLocation(dir));
//                if (info != null && info.mode == RobotMode.PORTABLE) arePrototypes = true;
//            }
//
//            for (Direction dir : Direction.cardinalDirections()) {
//                RobotInfo info = rc.senseRobotAtLocation(rc.adjacentLocation(dir));
//                if (info != null && info.mode == RobotMode.PROTOTYPE)
//                if (rc.canRepair(rc.adjacentLocation(dir))) rc.repair(rc.adjacentLocation(dir));
//            }
//
//            if (!arePrototypes) {
//                for (Direction dir : Direction.cardinalDirections()) {
//                    if (rc.canBuildRobot(RobotType.WATCHTOWER, dir)) {
//                        rc.buildRobot(RobotType.WATCHTOWER, dir);
//                        break;
//                    }
//                }
//            }

//            if (rc.canRepair(rc.adjacentLocation(Direction.EAST))) rc.repair(rc.adjacentLocation(Direction.EAST));
//            if (rc.canRepair(rc.adjacentLocation(Direction.NORTH))) rc.repair(rc.adjacentLocation(Direction.NORTH));
//            if (rc.canRepair(rc.adjacentLocation(Direction.WEST))) rc.repair(rc.adjacentLocation(Direction.WEST));
//            if (rc.canRepair(rc.adjacentLocation(Direction.SOUTH))) rc.repair(rc.adjacentLocation(Direction.SOUTH));
//
//            if (rc.canBuildRobot(RobotType.WATCHTOWER, Direction.EAST)) rc.buildRobot(RobotType.WATCHTOWER, Direction.EAST);
//            if (rc.canBuildRobot(RobotType.WATCHTOWER, Direction.NORTH)) rc.buildRobot(RobotType.WATCHTOWER, Direction.NORTH);
//            if (rc.canBuildRobot(RobotType.WATCHTOWER, Direction.WEST)) rc.buildRobot(RobotType.WATCHTOWER, Direction.WEST);
//            if (rc.canBuildRobot(RobotType.WATCHTOWER, Direction.SOUTH)) rc.buildRobot(RobotType.WATCHTOWER, Direction.SOUTH);
        }
    }
}