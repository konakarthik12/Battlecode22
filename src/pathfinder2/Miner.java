package pathfinder2;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

class Miner {

    static MapLocation destination;
    static MapLocation lastLoc;

    static Direction nextMove(RobotController rc) throws GameActionException {

        MapLocation curLoc = rc.getLocation();
        int dist = curLoc.distanceSquaredTo(destination);

        boolean near_map_edge = curLoc.x < 2 || curLoc.y < 2 || curLoc.x + 2 >= rc.getMapWidth() || curLoc.y + 2 >= rc.getMapHeight();
        if (dist <= 2 || near_map_edge) {
            return curLoc.directionTo(destination);
        } else {

            MapLocation pathfind = UnrolledPathfinder.pathfind(rc, destination);
            if(pathfind==null) return Utils.randomDirection();
            Direction direction = curLoc.directionTo(pathfind);

            if (curLoc.add(direction).equals(lastLoc)) {
                return curLoc.directionTo(destination);

            }
            return direction;
        }

    }


    static void setup(RobotController rc) {
        lastLoc = rc.getLocation();
    }

    static int locations = 0;

    static void run(RobotController rc) throws GameActionException {
        if (destination == null || destination.equals(rc.getLocation())) {
            // todo: out of bounds
            destination = new MapLocation((Utils.rng.nextInt(rc.getMapWidth()) - 3) + 3, (Utils.rng.nextInt(rc.getMapHeight() - 3) + 3));
            locations++;
        }
        for (MapLocation mapLocation : rc.getAllLocationsWithinRadiusSquared(rc.getLocation(), rc.getType().actionRadiusSquared)) {
            if (rc.canMineLead(mapLocation)) rc.mineLead(mapLocation);
        }
        rc.setIndicatorLine(rc.getLocation(), destination, 255, 255, 255);
        Direction go = nextMove(rc);

        if (go != null && rc.canMove(go)) {
            lastLoc = rc.getLocation();
            rc.move(go);
        }
        if (rc.getRoundNum() == 2000) {
            System.out.println("Visited " + locations + " locations");
        }
    }
}
