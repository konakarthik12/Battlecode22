package turtle1;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

class WatchTower {
    static void run(RobotController rc) throws GameActionException {
        RobotInfo[] robotInfos = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
        for (RobotInfo robotInfo : robotInfos) {

            if (rc.canAttack(robotInfo.location)) {

                rc.attack(robotInfo.location);
            }
        }
    }
}
