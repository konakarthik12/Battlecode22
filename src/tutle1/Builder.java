package tutle1;

import battlecode.common.*;

import static tutle1.Utils.directions;

public class Builder {
    public static void run(RobotController rc) throws GameActionException {
        for (RobotInfo robotInfo : rc.senseNearbyRobots()) {
            if (robotInfo.type == RobotType.ARCHON) {
                Direction dirToArchon = rc.getLocation().directionTo(robotInfo.location);
                if (rc.getLocation().distanceSquaredTo(robotInfo.location) < 2) {
                    Direction opposite = dirToArchon.opposite();
                    if (rc.canMove(opposite)) {
                        rc.move(opposite);
                    }
                } else {
                    for (Direction dir : directions) {
                        MapLocation dirLoc = rc.getLocation().add(dir);
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

    }
}
