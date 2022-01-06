package tutle1;

import battlecode.common.Direction;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

import java.util.Random;

public class Utils {
    static final Direction[] directions = {Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH, Direction.NORTHWEST, Direction.NORTHEAST, Direction.SOUTHEAST, Direction.SOUTHWEST};
    static Random rng = null;

    public static RobotInfo getNearbyArchon(RobotController rc) {
        RobotInfo[] robotInfos = rc.senseNearbyRobots(-1, rc.getTeam());
        for (RobotInfo robotInfo : robotInfos)
            if (robotInfo.type == RobotType.ARCHON) return robotInfo;
        return null;
    }
}
