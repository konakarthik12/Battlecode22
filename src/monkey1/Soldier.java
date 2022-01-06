package monkey1;

import battlecode.common.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static monkey1.Constants.*;
import static monkey1.Miner.*;
import static monkey1.utils.Utils.*;

class Soldier {
    private static MapLocation destination = null;
    private static int soldierLowDist = 15;
    private static int soldierHighDist = 20;

    static void setup(RobotController rc) throws GameActionException {
        spawn = rc.getLocation();
        lowX = Math.max(spawn.x - soldierLowDist, 0);
        highX = Math.min(spawn.x + soldierHighDist, rc.getMapWidth());
        lowY = Math.max(spawn.y - soldierLowDist, 0);
        highY = Math.min(spawn.y + soldierHighDist, rc.getMapHeight());
    }

    static void run(RobotController rc) throws GameActionException {
        if (destination == null || rc.getLocation().equals(destination)) {
            destination = new MapLocation(randomInt(lowX, highX), randomInt(lowY, highY));
        }
        rc.setIndicatorString(destination.toString());

        Direction go = rc.getLocation().directionTo(destination);
        for (RobotInfo robotInfo : rc.senseNearbyRobots(-1, rc.getTeam().opponent())) {
            if (rc.canAttack(robotInfo.location)) {
                rc.attack(robotInfo.location);
            }
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
