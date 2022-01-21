package monkey2newmienr;

import battlecode.common.*;

import java.util.Random;

class Utils {

    static Random rng = null;
    private static RobotController rc;

    static void setup(RobotController rc) {
        Utils.rc = rc;
        rng = new Random(rc.getID() + 422);
    }

    static MapLocation nearestArchon(RobotController rc) throws GameActionException {
        int dist = Integer.MAX_VALUE;
        MapLocation cur = rc.getLocation();
        MapLocation best = cur;
        for (int i = 1; i <= rc.getArchonCount(); ++i) {
            int readPos = rc.readSharedArray(64 - i);
            MapLocation archonPos = new MapLocation((readPos >> 6) & 63, readPos & 63);
            if (cur.distanceSquaredTo(archonPos) < dist) {
                best = archonPos;
                dist = cur.distanceSquaredTo(archonPos);
            }
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

    static int archonDist(MapLocation loc) throws GameActionException{
        int dist = Integer.MAX_VALUE;
        for (int i = 1; i <= rc.getArchonCount(); i++) {
            int readPos = rc.readSharedArray(64 - i);
            MapLocation archonPos = new MapLocation((readPos >> 6) & 63, readPos & 63);
            dist = Math.min(dist, loc.distanceSquaredTo(archonPos));
        }
        return dist;
    }

    static int strength(RobotController rc, RobotInfo info) throws GameActionException {
        int rubble = rc.senseRubble(info.location);
        return info.health / (3 * (rubble / 10) + 1) + 1;
    }

    static int wallDist(RobotController rc, MapLocation loc) throws GameActionException {
        int ret = 1000;
        if (loc.x < ret) ret = loc.x;
        if (loc.y < ret) ret = loc.y;
        if (rc.getMapWidth() - loc.x < ret) ret = (rc.getMapWidth() - loc.x);
        if (rc.getMapHeight() - loc.y < ret) ret = (rc.getMapHeight() - loc.y);
        return ret;
    }
}
