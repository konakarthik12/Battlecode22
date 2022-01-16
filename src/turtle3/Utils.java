package turtle3;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

import java.util.Random;

class Utils {

    static Random rng = null;
    private static RobotController rc;

    static void setup(RobotController rc) {
        Utils.rc = rc;
        rng = new Random(rc.getID() + 422);
    }

    static MapLocation nearestArchon(RobotController rc) throws GameActionException {
        int bestDist = Integer.MAX_VALUE;
        MapLocation cur = rc.getLocation();
        MapLocation best = cur;
        for (int i = 0; i < rc.getArchonCount(); ++i) {

        }
        return best;
    }

    /**
     * Returns random integer in range [a,b]
     *
     * @param a lower bound
     * @param b upper bound
     * @return random integer in range [a,b]
     */
    static int randomInt(int a, int b) {
        if (b < a) return -1;
        return Utils.rng.nextInt(b - a + 1) + a;
    }
}
