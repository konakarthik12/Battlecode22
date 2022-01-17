package monkey4;

import battlecode.common.*;


class Builder {
    static Direction previousStep = Direction.CENTER;
    static MapLocation destination = null;
    static MapLocation spawn;

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
                int nextScore = 20 + rubble + nextMove(rc, next, depth + 1, dir);
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

    static void buildWatchTowerCardinal(RobotController rc) throws GameActionException {
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
        }
    }

    static void setup(RobotController rc) throws GameActionException {
        spawn = rc.getLocation();
    }

    static void setDestination(RobotController rc) throws GameActionException {
        if (destination == null) {
            destination = new MapLocation((Utils.rng.nextInt(rc.getMapWidth()) - 3) + 3, (Utils.rng.nextInt(rc.getMapHeight() - 3) + 3));
        }
    }

    static void run(RobotController rc) throws GameActionException {
        setDestination(rc);
        nextMove(rc, rc.getLocation(), 0, previousStep);
        if (rc.getLocation().equals(destination)) buildWatchTowerCardinal(rc);
    }
}