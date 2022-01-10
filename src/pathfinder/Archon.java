package pathfinder;

import battlecode.common.*;



class Archon {

    static int minersBuilt = 0;
    static int soldiersBuilt = 0;

    static void run(RobotController rc) throws GameActionException {
        MapLocation loc = rc.getLocation();
        if (minersBuilt < 4) {
            for (Direction dir : Constants.directions) {
                if (rc.canBuildRobot(RobotType.MINER, dir))  {
                    rc.buildRobot(RobotType.MINER, dir);
                    minersBuilt++;
                    break;
                }
            }
        }

    }

}
