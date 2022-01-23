package pathfinder;

import battlecode.common.MapLocation;
import battlecode.common.RobotController;

import java.util.ArrayList;
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


    public static void main(String[] args) {
        ArrayList<List<MapLocation>> paths;
    }

    static void search(MapLocation cur) {

    }
}
