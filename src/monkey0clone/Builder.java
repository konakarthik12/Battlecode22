package monkey0clone;

import battlecode.common.*;


class Builder {
    static Direction previousStep = Direction.CENTER;
    static MapLocation destination = null;
    static MapLocation toPlace = null;
    static MapLocation wall = null;
    static MapLocation spawn;

    static int close = 0;
    static int cntr = 0;
    static void setup(RobotController rc) throws GameActionException {
        spawn = rc.getLocation();
        wall = rc.getLocation();
        cntr = 200;
        rc.writeSharedArray(55, 1);
    }

    static void setDestination(RobotController rc) throws GameActionException {
        if (wall != null && rc.getLocation().distanceSquaredTo(wall) <= 4) {
            int cDist = Integer.MAX_VALUE;
            int aDist = Utils.archonDist(wall);
            int rubble = rc.senseRubble(rc.getLocation());
            MapLocation best = wall;
            for (MapLocation loc : rc.getAllLocationsWithinRadiusSquared(wall, 20)) {
                if (!rc.canSenseLocation(loc) || rc.canSenseRobotAtLocation(loc))continue;
                int rubble_ = rc.senseRubble(loc);
                if (rubble_ < rubble) {
                    best = loc;
                    rubble = rubble_;
                    aDist = Utils.archonDist(loc);
                    cDist = Utils.cornerDist(rc, loc);
                } else if (rubble_ == rubble && Utils.cornerDist(rc, loc) < cDist) {
                    best = loc;
                    rubble = rubble_;
                    aDist = Utils.archonDist(loc);
                    cDist = Utils.cornerDist(rc, loc);
                }
            }
            toPlace = best;
            rubble = Integer.MAX_VALUE;
            for (Direction dir : Constants.directions) {
                if (rc.canSenseLocation(toPlace.add(dir)) && !rc.canSenseRobotAtLocation(toPlace.add(dir)) && rc.senseRubble(toPlace.add(dir)) < rubble) {
                    destination = toPlace.add(dir);
                    rubble = rc.senseRubble(toPlace.add(dir));
                }
            }
            wall = null;
        }
    }
    static void act(RobotController rc) throws GameActionException {
        if (rc.getLocation().equals(destination)) {
            Direction dir = null;
            for (Direction o : Constants.directions) {
                if (destination.add(o).equals(toPlace)) dir = o;
            }
            if (rc.canBuildRobot(RobotType.LABORATORY, dir)) {
                rc.writeSharedArray(55, (rc.readSharedArray(55) / 2) * 2);
                rc.writeSharedArray(55, rc.readSharedArray(55) + 2);
                rc.buildRobot(RobotType.LABORATORY, dir);
                close++;
            }
        } else {
            if (destination == null) Pathfinder.move(rc, wall);
            else Pathfinder.move(rc, destination);
        }
    }
    static void repair(RobotController rc) throws GameActionException {
        for (RobotInfo info : rc.senseNearbyRobots()) {
            if (rc.canRepair(info.location) && info.getHealth() < info.getType().getMaxHealth(info.getLevel()))rc.repair(info.location);
        }
    }
    static void run(RobotController rc) throws GameActionException {
        if (rc.getRoundNum() == cntr || (cntr == 200 && rc.readSharedArray(34) == 10)) {
            wall = rc.getLocation();
            rc.writeSharedArray(55, rc.readSharedArray(55) | 1);
            cntr *= 2;
        }
        setDestination(rc);
        act(rc);
        repair(rc);
        if (destination != null) {
            rc.setIndicatorString(destination.toString());
            rc.setIndicatorLine(rc.getLocation(), destination, 255, 255, 255);
        }
    }
}