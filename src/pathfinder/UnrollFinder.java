package pathfinder;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UnrollFinder {

    static ArrayList<List<MapLocation>> EAST = new ArrayList<>();
    static ArrayList<List<MapLocation>> NORTH = new ArrayList<>();
    static ArrayList<List<MapLocation>> SOUTH = new ArrayList<>();
    static ArrayList<List<MapLocation>> WEST = new ArrayList<>();
    static ArrayList<List<MapLocation>> NORTHEAST = new ArrayList<>();
    static ArrayList<List<MapLocation>> NORTHWEST = new ArrayList<>();
    static ArrayList<List<MapLocation>> SOUTHEAST = new ArrayList<>();
    static ArrayList<List<MapLocation>> SOUTHWEST = new ArrayList<>();

    public static void setup(RobotController rc) throws GameActionException {
        Utils.main(new String[]{});
        EAST = Utils.EAST;
        WEST = Utils.WEST;
        NORTHEAST = Utils.NORTHEAST;
        NORTH = Utils.NORTH;
        NORTHWEST = Utils.NORTHWEST;
        SOUTHEAST = Utils.SOUTHEAST;
        SOUTHWEST = Utils.SOUTHWEST;
        SOUTH = Utils.SOUTH;
    }

    public Direction findStep(RobotController rc, MapLocation destination) throws GameActionException {
        Direction go = rc.getLocation().directionTo(destination);

        MapLocation cur = rc.getLocation();

        switch (go) {
            case EAST:
                int best = Integer.MAX_VALUE;
                for (List<MapLocation> path : EAST) {
                    int curScore = 0;
                    // cur = (0,0)
                    MapLocation next = cur;
                    for (MapLocation step : path) {
                        if (rc.canSenseLocation(cur)) curScore += 20 + 2 * rc.senseRubble(cur);
                        next = cur.translate(step.x, step.y);
                    }
                }
                break;
            case WEST:
                break;
        }
        return go;
    }

    public void move(RobotController rc, MapLocation destination) throws GameActionException {

    }
}
