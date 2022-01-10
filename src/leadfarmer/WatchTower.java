package leadfarmer;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

class WatchTower {
    static void run(RobotController rc) throws GameActionException {
        RobotInfo[] robots = rc.senseNearbyRobots();
        int RobotsAttacked = 0;
        for (RobotInfo robot : robots){
            if (rc.canAttack(robot.location)){
                rc.attack(robot.location);
            }
        }
    }
}
