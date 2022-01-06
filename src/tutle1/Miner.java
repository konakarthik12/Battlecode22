package tutle1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

import static tutle1.Utils.rng;


public class Miner {

    static MapLocation destination = null;

    public static void run(RobotController rc) throws GameActionException {

        while (destination == null || destination.equals(rc.getLocation()) || destination.distanceSquaredTo(rc.getLocation()) < 36) {
            destination = new MapLocation(rng.nextInt(rc.getMapHeight()), rng.nextInt(rc.getMapWidth()));
        }

        rc.setIndicatorString(destination.toString());

        Direction go = rc.getLocation().directionTo(destination);
        while (rc.canMineLead(rc.getLocation())) rc.mineLead(rc.getLocation());

        if (rc.canMove(go)) rc.move(go);
        rc.setIndicatorString("goal: " + destination);


    }
}
