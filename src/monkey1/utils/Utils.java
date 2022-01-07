package monkey1.utils;

import battlecode.common.RobotController;

import java.util.Random;

public class Utils {

    static Random rng = null;
    private static RobotController rc;

    public static void setup(RobotController rc) {
        Utils.rc = rc;
        rng = new Random(rc.getID() + 420);
    }

    /**
     * Returns random integer in range [a,b]
     *
     * @param a lower bound
     * @param b upper bound
     * @return random integer in range [a,b]
     */
    public static int randomInt(int a, int b) {
        return rng.nextInt(b - a + 1) + a;
    }
}
