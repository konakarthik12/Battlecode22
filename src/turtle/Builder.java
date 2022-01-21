package turtle;

import battlecode.common.*;

class Builder {
    static Direction previousStep = Direction.CENTER;
    static MapLocation destination = null;
    static MapLocation spawn;

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

    static int averageSoldier = 250 + 2 * (Utils.width + Utils.height);

    static MapLocation nearArchon(RobotController rc) throws GameActionException {
        MapLocation cur = rc.getLocation();
        MapLocation res;

        int x = Utils.randomInt(3, Utils.width);
        int y = Utils.randomInt(3, Utils.height);
        res = new MapLocation(x, y);

        int reps = 10;
        while (res.distanceSquaredTo(cur) > averageSoldier && --reps > 0) {
            x = Utils.randomInt(3, Utils.width);
            y = Utils.randomInt(3, Utils.height);
        }

        return res;
    }

    static void setDestination(RobotController rc) throws GameActionException {
        if (destination == null) {
            destination = new MapLocation((Utils.rng.nextInt(rc.getMapWidth()) - 3) + 3, (Utils.rng.nextInt(rc.getMapHeight() - 3) + 3));
            destination = nearArchon(rc);
        }

        rc.setIndicatorString(destination.toString());
        rc.setIndicatorLine(rc.getLocation(), destination, 0, 200, 200);
    }

    static void run(RobotController rc) throws GameActionException {
        setDestination(rc);
        rc.setIndicatorString(destination.toString());
        rc.setIndicatorLine(rc.getLocation(), destination, 0, 200, 200);
        if (!rc.getLocation().equals(destination)) Pathfinder.move(rc, destination);
        if (rc.getLocation().equals(destination)) buildWatchTowerCardinal(rc);
    }
}