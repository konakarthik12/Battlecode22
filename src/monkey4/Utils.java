package monkey4;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.Team;

import java.util.Random;

class Utils {

    static int width = 0;
    static int height = 0;

    static int b_width = 0;
    static int b_height = 0;

    static Random rng = null;
    private static RobotController rc;

    static Team team;
    static Team opponent;


    static void setup(RobotController rc) {
        team = rc.getTeam();
        opponent = rc.getTeam().opponent();

        width = rc.getMapWidth();
        height = rc.getMapHeight();

        b_height = height/6;
        b_width = width/6;

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
        if (b-a < 0) return -1;
        return Utils.rng.nextInt(b - a + 1) + a;
    }
}
