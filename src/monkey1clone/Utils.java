package monkey1clone;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

import java.util.Random;

class Utils {

    static Random rng = null;
    static int width = 0;
    static int height = 0;
    private static RobotController rc;

    static void setup(RobotController rc) {
        width = rc.getMapWidth();
        height = rc.getMapHeight();
        Utils.rc = rc;
        rng = new Random(rc.getID() + 422);
    }

    static MapLocation getWall(int roll) {
        int x = 0;
        int y = 0;
        switch (roll) {
            case 1: {
                x = Utils.randomInt(3, Utils.width-3);
                y = 0;
                break;
            }
            case 2: {
                x = Utils.randomInt(3, Utils.width-3);
                y = Utils.height-1;
                break;
            }
            case 3: {
                y = Utils.randomInt(3, Utils.height-3);
                x = 0;
                break;
            }
            case 4: {
                y = Utils.randomInt(3, Utils.height-3);
                x = Utils.width-1;
                break;
            }
        }
        return new MapLocation(x, y);
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

    static int nearestArchonID(RobotController rc) throws GameActionException {
        int dist = Integer.MAX_VALUE;
        MapLocation cur = rc.getLocation();
        MapLocation best = cur;
        int ret = 0;
        for (int i = 1; i <= rc.getArchonCount(); ++i) {
            int readPos = rc.readSharedArray(64 - i);
            MapLocation archonPos = new MapLocation((readPos >> 6) & 63, readPos & 63);
            if (cur.distanceSquaredTo(archonPos) < dist) {
                best = archonPos;
                dist = cur.distanceSquaredTo(archonPos);
                ret = i;
            }
        }
        return ret;
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

    static int cornerDist(RobotController rc, MapLocation loc) throws GameActionException{
        int dist = Integer.MAX_VALUE;
        MapLocation corner = new MapLocation(0, 0);
        dist = Math.min(dist, loc.distanceSquaredTo(corner));
        corner = new MapLocation(0, rc.getMapHeight() - 1);
        dist = Math.min(dist, loc.distanceSquaredTo(corner));
        corner = new MapLocation(rc.getMapWidth() - 1, 0);
        dist = Math.min(dist, loc.distanceSquaredTo(corner));
        corner = new MapLocation(rc.getMapWidth() - 1, rc.getMapHeight() - 1);
        dist = Math.min(dist, loc.distanceSquaredTo(corner));
        return dist;
    }

    static int strength(RobotController rc, RobotInfo info) throws GameActionException {
        int rubble = rc.senseRubble(info.location);
        return info.health / (3 * (rubble / 10) + 1) + 1;
    }
}
