package leadfarmer;

import battlecode.common.*;

import static leadfarmer.Constants.directions;


class Builder {
    static Direction previousStep = Direction.CENTER;
    static MapLocation destination = null;
    static MapLocation spawn;

    static boolean isLeadFarm = false;
    static boolean assigned = false;

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
        if (assigned && rc.getLocation().equals(destination)) rc.disintegrate();
//        if (destination != null && rc.canSenseLocation(destination) && rc.senseLead(destination) != 0) assigned = false;
        if (destination != null && rc.canSenseLocation(destination) && rc.senseLead(destination) != 0) {
            destination = new MapLocation((Utils.rng.nextInt(rc.getMapWidth()) - 3) + 3, (Utils.rng.nextInt(rc.getMapHeight() - 3) + 3));
            assigned = false;
        }

        if (destination == null) {
            destination = new MapLocation((Utils.rng.nextInt(rc.getMapWidth()) - 3) + 3, (Utils.rng.nextInt(rc.getMapHeight() - 3) + 3));
        } else if (isLeadFarm && !assigned){
            if (rc.senseLead(rc.getLocation()) == 0) {
                rc.disintegrate();
                return;
            }
            int bestDist = 10000;
            for (MapLocation loc : rc.getAllLocationsWithinRadiusSquared(rc.getLocation(), RobotType.BUILDER.visionRadiusSquared)) {
                if (rc.canSenseLocation(loc) && rc.senseLead(loc) == 0 && !rc.isLocationOccupied(loc)) {
                    if (loc.distanceSquaredTo(rc.getLocation()) < bestDist) {
                        bestDist = loc.distanceSquaredTo(rc.getLocation());
                        destination = loc;
                    }
                    assigned = true;
                }
            }
        }
        rc.setIndicatorString(destination.toString());
        rc.setIndicatorLine(rc.getLocation(), destination, 255, 255, 255);
    }

    static void run(RobotController rc) throws GameActionException {
        isLeadFarm = true;
        setDestination(rc);
        nextMove(rc, rc.getLocation(), 0, previousStep);
        if (rc.getLocation().equals(destination)) buildWatchTowerCardinal(rc);
    }
}