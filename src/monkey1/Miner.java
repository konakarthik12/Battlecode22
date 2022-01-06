package monkey1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static monkey1.utils.Utils.*;
import static monkey1.Constants.*;

class Miner {

    static MapLocation destination = null;
    static MapLocation spawn = null;

    static int highX = 0;
    static int lowX = 0;
    static int highY = 0;
    static int lowY = 0;

    static final int minerLowDist = 10;
    static final int minerHighDist = 18;

    static void setup(RobotController rc) throws GameActionException {
        spawn = rc.getLocation();
        lowX = Math.max(spawn.x - minerLowDist, 0);
        highX = Math.min(spawn.x + minerLowDist, rc.getMapWidth());
        lowY = Math.max(spawn.y - minerLowDist, 0);
        highY = Math.min(spawn.y + minerLowDist, rc.getMapHeight());
    }

    static void run(RobotController rc) throws GameActionException {
        if (destination == null || rc.getLocation().equals(destination)) {
            destination = new MapLocation(randomInt(lowX, highX), randomInt(lowY, highY));
        }
        rc.setIndicatorString(destination.toString());

        Direction go = rc.getLocation().directionTo(destination);
        while (rc.canMineLead(rc.getLocation())) {
            rc.mineLead(rc.getLocation());
        }

        if (rc.canMove(go)) rc.move(go);
        else {
            List<Direction> shuffledDirections = Arrays.asList(directions);
            Collections.shuffle(shuffledDirections);
            for (Direction dir : shuffledDirections) {
                if (rc.canMove(dir)) {
                    rc.move(dir);
                    break;
                }
            }
        }
    }
}
