package pathfinder;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class Utils {

    static Random rng = null;
    private static RobotController rc;

    static void setup(RobotController rc) {
        Utils.rc = rc;
        rng = new Random(rc.getID() + 423);
    }

    /**
     * Returns random integer in range [a,b]
     *
     * @param a lower bound
     * @param b upper bound
     * @return random integer in range [a,b]
     */
    static int randomInt(int a, int b) {
        return Utils.rng.nextInt(b - a + 1) + a;
    }

    static Direction directionTo;

    public static void main(String[] args) {
        List<MapLocation> curPath = new ArrayList<>();
        curPath.add(new MapLocation(0, 0));


        directionTo = Direction.EAST;
        search(new MapLocation(0, 0), 0, Direction.CENTER, curPath);

        for (List<MapLocation> path : EAST) {
            for (MapLocation v : path) {
                System.out.print(v + " ");
            }
            System.out.println();
        }
        System.out.println("HI " + EAST.size());

//        directionTo = Direction.NORTH;
//        search(new MapLocation(0, 0), 0, Direction.CENTER, curPath);
//
//        directionTo = Direction.SOUTH;
//        search(new MapLocation(0, 0), 0, Direction.CENTER, curPath);
//
//        directionTo = Direction.WEST;
//        search(new MapLocation(0, 0), 0, Direction.CENTER, curPath);
//
//        directionTo = Direction.NORTHEAST;
//        search(new MapLocation(0, 0), 0, Direction.CENTER, curPath);
//
//        directionTo = Direction.NORTHWEST;
//        search(new MapLocation(0, 0), 0, Direction.CENTER, curPath);
//
//        directionTo = Direction.SOUTHEAST;
//        search(new MapLocation(0, 0), 0, Direction.CENTER, curPath);
//
//        directionTo = Direction.SOUTHWEST;
//        search(new MapLocation(0, 0), 0, Direction.CENTER, curPath);
    }

    static int MINER_V = 20;
    static ArrayList<List<MapLocation>> EAST = new ArrayList<>();
    static ArrayList<List<MapLocation>> NORTH = new ArrayList<>();
    static ArrayList<List<MapLocation>> SOUTH = new ArrayList<>();
    static ArrayList<List<MapLocation>> WEST = new ArrayList<>();
    static ArrayList<List<MapLocation>> NORTHEAST = new ArrayList<>();
    static ArrayList<List<MapLocation>> NORTHWEST = new ArrayList<>();
    static ArrayList<List<MapLocation>> SOUTHEAST = new ArrayList<>();
    static ArrayList<List<MapLocation>> SOUTHWEST = new ArrayList<>();

    static MapLocation o = new MapLocation(0, 0);

    static void search(MapLocation cur, int depth, Direction last, List<MapLocation> curPath) {
        if (cheb(o, cur) == 4) {
            switch (directionTo) {
                case NORTH: NORTH.add(new ArrayList<>(curPath)); break;
                case EAST: EAST.add(new ArrayList<>(curPath)); break;
                case WEST: WEST.add(new ArrayList<>(curPath)); break;
                case SOUTH: SOUTH.add(new ArrayList<>(curPath)); break;
                case NORTHEAST: NORTHEAST.add(new ArrayList<>(curPath)); break;
                case NORTHWEST: NORTHWEST.add(new ArrayList<>(curPath)); break;
                case SOUTHEAST: SOUTHEAST.add(new ArrayList<>(curPath)); break;
                case SOUTHWEST: SOUTHWEST.add(new ArrayList<>(curPath)); break;
            }
            return;
        }

        if (o.distanceSquaredTo(cur) > MINER_V || depth > 7) {
            return;
        }


        Direction[] dirs = new Direction[]{directionTo, directionTo.rotateRight(), directionTo.rotateRight().rotateRight(),
                directionTo.rotateLeft(), directionTo.rotateLeft().rotateLeft()};

        Direction[] ok = new Direction[]{last, last.rotateRight(), last.rotateRight().rotateRight(),
                last.rotateLeft(), last.rotateLeft().rotateLeft()};

        List<Direction> _dirs = Arrays.asList(ok);

        for (Direction dir : dirs) {
            if (last != Direction.CENTER && !_dirs.contains(dir)) continue;

            MapLocation next = cur.add(dir);

            boolean pathOk = true;

            if (curPath.size() > 1) {
                for (MapLocation pos : curPath.subList(0, curPath.size()-2)) {
                    if (cheb(pos, cur) == 1) {
                        pathOk = false;
                    }
                }

                if (!pathOk) continue;
            }

            curPath.add(new MapLocation(next.x, next.y));
            search(next, depth+1, dir, curPath);
            curPath.remove(curPath.size()-1);
        }
    }

    static int cheb(MapLocation a, MapLocation b) {
        return Math.max(Math.abs(a.x - b.x), Math.abs(a.y - b.y));
    }


}
