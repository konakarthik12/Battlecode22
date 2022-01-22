package turtle;

import battlecode.common.*;

class Builder {
    static Direction previousStep = Direction.CENTER;
    static MapLocation destination = null;
    static MapLocation spawn;

    static boolean isLeadFarm = false;
    static boolean assigned = false;

    static int nearbyLeadPatches = 0;
    static int turnsAlive = 0;

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
        nearbyLeadPatches = rc.senseNearbyLocationsWithLead().length;
        if (Utils.randomInt(1,10) <= 7 && nearbyLeadPatches <= 50) {
            isLeadFarm = true;
            ++nearbyLeadPatches;
        }
    }

    static int averageSoldier = 250 + 2 * (Utils.width + Utils.height);

    static MapLocation nearArchon(RobotController rc) throws GameActionException {
        averageSoldier = 250 + 2 * (Utils.width + Utils.height) + rc.getRoundNum() / 50;
        MapLocation cur = rc.getLocation();
        MapLocation res;

        int x = Utils.randomInt(3, Utils.width);
        int y = Utils.randomInt(3, Utils.height);
        res = new MapLocation(x, y);

        int reps = 10;
        while ((res.distanceSquaredTo(cur) > averageSoldier || res.distanceSquaredTo(cur) < 50) && --reps > 0) {
            x = Utils.randomInt(3, Utils.width);
            y = Utils.randomInt(3, Utils.height);
        }

        return res;
    }

    static void setDestination(RobotController rc) throws GameActionException {
        if (isLeadFarm) {
            if (Utils.nearestArchon(rc).distanceSquaredTo(rc.getLocation()) <= 20 && turnsAlive > 15) destination = rc.getLocation();
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

            return;
        }
        if (destination == null) {
            destination = new MapLocation((Utils.rng.nextInt(rc.getMapWidth()) - 3) + 3, (Utils.rng.nextInt(rc.getMapHeight() - 3) + 3));
            destination = nearArchon(rc);
        }

        rc.setIndicatorString(destination.toString());
        rc.setIndicatorLine(rc.getLocation(), destination, 0, 200, 200);
    }

    static void run(RobotController rc) throws GameActionException {
        ++turnsAlive;
        setDestination(rc);
        rc.setIndicatorString(destination.toString());
        rc.setIndicatorLine(rc.getLocation(), destination, 0, 200, 200);
        if (!rc.getLocation().equals(destination)) Pathfinder.move(rc, destination);
        if (rc.getLocation().equals(destination)) buildWatchTowerCardinal(rc);
    }
}