package monkey00clone;

import battlecode.common.*;


class Builder {
    static Direction previousStep = Direction.CENTER;
    static MapLocation destination = null;
    static MapLocation toPlace = null;
    static MapLocation wall = null;
    static MapLocation spawn;

    static int close = 0;
<<<<<<< HEAD
    static int cntr = 0;
    static void setup(RobotController rc) throws GameActionException {
        spawn = rc.getLocation();
        wall = rc.getLocation();
        cntr = 200;
        rc.writeSharedArray(55, 1);
    }

    static void setDestination(RobotController rc) throws GameActionException {
        if (wall != null && rc.getLocation().distanceSquaredTo(wall) <= 4) {
=======

    static void setup(RobotController rc) throws GameActionException {
        spawn = rc.getLocation();
        int best = 1000;
        if (spawn.distanceSquaredTo(new MapLocation(0, spawn.y)) < best) {
            wall = new MapLocation(0, spawn.y);
            best = spawn.distanceSquaredTo(new MapLocation(0, spawn.y));
        } 
        if (spawn.distanceSquaredTo(new MapLocation(rc.getMapWidth() - 1, spawn.y)) < best) {
            wall = new MapLocation(rc.getMapWidth() - 1, spawn.y);
            best = spawn.distanceSquaredTo(new MapLocation(rc.getMapWidth() - 1, spawn.y));
        } 
        if (spawn.distanceSquaredTo(new MapLocation(spawn.x, 0)) < best) {
            wall = new MapLocation(spawn.x, 0);
            best = spawn.distanceSquaredTo(new MapLocation(spawn.x, 0));
        } 
        if (spawn.distanceSquaredTo(new MapLocation(spawn.x, rc.getMapHeight() - 1)) < best) {
            wall = new MapLocation(spawn.x, rc.getMapHeight() - 1);
            best = spawn.distanceSquaredTo(new MapLocation(spawn.x, rc.getMapHeight() - 1));
        } 
    }

    static void setDestination(RobotController rc) throws GameActionException {
        if (wall != null && rc.getLocation().isAdjacentTo(wall)) {
>>>>>>> fc089691009a21324fdc4f206be78c1fe51ea6e7
            int cDist = Integer.MAX_VALUE;
            int aDist = Utils.archonDist(wall);
            int rubble = rc.senseRubble(rc.getLocation());
            MapLocation best = wall;
<<<<<<< HEAD
            for (MapLocation loc : rc.getAllLocationsWithinRadiusSquared(wall, 20)) {
                if (!rc.canSenseLocation(loc) || rc.canSenseRobotAtLocation(loc))continue;
                int rubble_ = rc.senseRubble(loc);
                if (rubble_ < rubble) {
=======
            for (MapLocation loc : rc.getAllLocationsWithinRadiusSquared(wall, 12)) {
                if (!rc.canSenseLocation(loc))continue;
                int rubble_ = rc.senseRubble(loc);
                if (rubble_ < rubble && Utils.archonDist(loc) >= aDist) {
>>>>>>> fc089691009a21324fdc4f206be78c1fe51ea6e7
                    best = loc;
                    rubble = rubble_;
                    aDist = Utils.archonDist(loc);
                    cDist = Utils.cornerDist(rc, loc);
<<<<<<< HEAD
                } else if (rubble_ == rubble && Utils.cornerDist(rc, loc) < cDist) {
=======
                } else if (rubble_ == rubble && Utils.archonDist(loc) >= aDist
                && Utils.cornerDist(rc, loc) >= cDist) {
>>>>>>> fc089691009a21324fdc4f206be78c1fe51ea6e7
                    best = loc;
                    rubble = rubble_;
                    aDist = Utils.archonDist(loc);
                    cDist = Utils.cornerDist(rc, loc);
                }
            }
            toPlace = best;
            rubble = Integer.MAX_VALUE;
            for (Direction dir : Constants.directions) {
<<<<<<< HEAD
                if (rc.canSenseLocation(toPlace.add(dir)) && !rc.canSenseRobotAtLocation(toPlace.add(dir)) && rc.senseRubble(toPlace.add(dir)) < rubble) {
=======
                if (rc.canSenseLocation(toPlace.add(dir)) && rc.senseRubble(toPlace.add(dir)) < rubble) {
>>>>>>> fc089691009a21324fdc4f206be78c1fe51ea6e7
                    destination = toPlace.add(dir);
                    rubble = rc.senseRubble(toPlace.add(dir));
                }
            }
            wall = null;
        }
    }
<<<<<<< HEAD
=======

>>>>>>> fc089691009a21324fdc4f206be78c1fe51ea6e7
    static void act(RobotController rc) throws GameActionException {
        if (rc.getLocation().equals(destination)) {
            Direction dir = null;
            for (Direction o : Constants.directions) {
                if (destination.add(o).equals(toPlace)) dir = o;
            }
            if (rc.canBuildRobot(RobotType.LABORATORY, dir)) {
<<<<<<< HEAD
                rc.writeSharedArray(55, (rc.readSharedArray(55) / 2) * 2);
                rc.writeSharedArray(55, rc.readSharedArray(55) + 2);
                rc.buildRobot(RobotType.LABORATORY, dir);
                close++;
=======
                rc.writeSharedArray(55, 0);
                rc.buildRobot(RobotType.LABORATORY, dir);
>>>>>>> fc089691009a21324fdc4f206be78c1fe51ea6e7
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
<<<<<<< HEAD
        if (rc.getRoundNum() == cntr || (cntr == 200 && rc.readSharedArray(34) == 10)) {
            wall = rc.getLocation();
            rc.writeSharedArray(55, rc.readSharedArray(55) | 1);
            cntr *= 2;
=======
        if (rc.getRoundNum() == 400) {
            wall = rc.getLocation();
            rc.writeSharedArray(55, 1);
>>>>>>> fc089691009a21324fdc4f206be78c1fe51ea6e7
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