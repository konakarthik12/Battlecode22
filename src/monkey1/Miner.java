package monkey1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

import static monkey1.Utils.rng;

class Miner {

    static MapLocation destination = null;

    static void run(RobotController rc) throws GameActionException {
        if (destination == null) {
            destination = new MapLocation(rng.nextInt(rc.getMapHeight()), rng.nextInt(rc.getMapWidth()));
//            System.out.println(rc.getMapHeight() + " " +  rc.getMapWidth());
            System.out.println(rc.getID());
            System.out.println(rng.nextInt(32));
        }
        rc.setIndicatorString(destination.toString());

        Direction go = rc.getLocation().directionTo(destination);
        while (rc.canMineLead(rc.getLocation())) {
            rc.mineLead(rc.getLocation());
        }
        if (rc.canMove(go)) rc.move(go);
    }
}
