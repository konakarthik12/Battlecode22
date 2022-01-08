package monkey1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static monkey1.Constants.directions;
import static monkey1.utils.Utils.randomInt;

class Miner {

    static final int minerLowDist = 10;
    static final int minerHighDist = 18;
    static MapLocation destination = null;
    static MapLocation spawn = null;
    static int highX = 0;
    static int lowX = 0;
    static int highY = 0;
    static int lowY = 0;

    static MapLocation[] closerToDest(RobotController rc, MapLocation cur) throws GameActionException {
        MapLocation[] ret = new MapLocation[8];
        int tot = 0;
        int curDistSq = cur.distanceSquaredTo(destination);
        for (Direction dir : directions) {
            if (rc.canSenseLocation(cur.add(dir)) && cur.add(dir).distanceSquaredTo(destination) <= curDistSq) {
                ret[tot++] = cur.add(dir);
            }
        }
        return Arrays.copyOfRange(ret, 0, tot);
    }

    static Direction next(RobotController rc) throws GameActionException {
        MapLocation cur = rc.getLocation();
        int ind = 0;
        MapLocation[] locations = new MapLocation[25];
        MapLocation[] visible = rc.getAllLocationsWithinRadiusSquared(cur, 18);

        int[][] score = new int[7][7];
        for (int i = 0; i < 7; ++i) for (int j = 0; j < 7; ++j) score[i][j] = Integer.MAX_VALUE;

        final int rubbleSensitivity = 50;
        final int distSensitivity = 10;

        for (int x = cur.x - 3; x <= cur.x + 3; ++x) {
            int rubble = 0;
            int dist = 0;
            MapLocation loc = cur.translate(x - cur.x, 3);
            if (rc.onTheMap(loc)) {
                rubble = rc.senseRubble(loc);
                dist = loc.distanceSquaredTo(destination);
                score[3 + x - cur.x][0] = rubble * rubbleSensitivity - dist * distSensitivity;
            }
            loc = cur.translate(x - cur.x, -3);
            if (rc.onTheMap(loc)) {
                rubble = rc.senseRubble(loc);
                dist = loc.distanceSquaredTo(destination);
                score[3 + x - cur.x][6] = rubble * rubbleSensitivity - dist * distSensitivity;
            }
        }
        for (int y = cur.y - 3; y <= cur.y + 3; ++y) {
            int rubble = 0;
            int dist = 0;
            MapLocation loc = cur.translate(3, y-cur.y);
            if (rc.onTheMap(loc)) {
                rubble = rc.senseRubble(loc);
                dist = loc.distanceSquaredTo(destination);
                score[0][3 + y - cur.y] = rubble * rubbleSensitivity - dist * distSensitivity;
            }
            loc = cur.translate(-3, y-cur.y);
            if (rc.onTheMap(loc)) {
                rubble = rc.senseRubble(loc);
                dist = loc.distanceSquaredTo(destination);
                score[6][3 + y - cur.y] = rubble * rubbleSensitivity - dist * distSensitivity;
            }
        }

        for (int s = 2; s >= 1; --s) {
            for (int dx = -s; dx <= s; ++dx) {
                if (rc.canSenseLocation(cur.translate(dx, s))) {
                    MapLocation[] reachable = closerToDest(rc, cur.translate(dx, s));
                    for (MapLocation loc : reachable) {
                        score[3 + dx][3 + s] = Math.min(score[3 + dx][3 + s], score[3 + loc.x - cur.x][3 + s + 1]);
                    }
                    int rubble = rc.senseRubble(cur.translate(dx, s));
                    int distSq = cur.translate(dx, s).distanceSquaredTo(destination);
                    int diffSq = cur.distanceSquaredTo(destination) - distSq;
                    score[3 + dx][3 + s] += (4-s)/2 * (rubble * rubbleSensitivity + distSq * distSensitivity);
                }

                if (rc.canSenseLocation(cur.translate(dx, -s))) {
                    MapLocation[] reachable = closerToDest(rc, cur.translate(dx, -s));
                    for (MapLocation loc : reachable) {
                        score[3 + dx][3 + s] = Math.min(score[3 + dx][3 - s], score[3 + loc.x - cur.x][3 - (s + 1)]);
                    }
                    int rubble = rc.senseRubble(cur.translate(dx, -s));
                    int distSq = cur.translate(dx, -s).distanceSquaredTo(destination);
                    int diffSq = cur.distanceSquaredTo(destination) - distSq;
                    score[3 + dx][3 + s] += (4-s)/2 * (rubble * rubbleSensitivity + distSq * distSensitivity);
                }
            }
            for (int dy = -s; dy <= s; ++dy) {
                if (rc.canSenseLocation(cur.translate(s, dy))) {
                    MapLocation[] reachable = closerToDest(rc, cur.translate(s, dy));
                    for (MapLocation loc : reachable) {
                        score[3 + s][3 + dy] = Math.min(score[3 + s][3 + dy], score[3 + s + 1][3 + loc.y - cur.y]);
                    }
                    int rubble = rc.senseRubble(cur.translate(s, dy));
                    int distSq = cur.translate(s, dy).distanceSquaredTo(destination);
                    int diffSq = cur.distanceSquaredTo(destination) - distSq;
                    score[3 + s][3 + dy] += (4-s)/2 * (rubble * rubbleSensitivity + distSq * distSensitivity);
                }

                if (rc.canSenseLocation(cur.translate(-s, dy))) {
                    MapLocation[] reachable = closerToDest(rc, cur.translate(-s, dy));
                    for (MapLocation loc : reachable) {
                        score[3 - s][3 + dy] = Math.min(score[3 - s][3 + dy], score[3 - (s + 1)][3 + loc.y - cur.y]);
                    }
                    int rubble = rc.senseRubble(cur.translate(-s, dy));
                    int distSq = cur.translate(-s, dy).distanceSquaredTo(destination);
                    int diffSq = cur.distanceSquaredTo(destination) - distSq;
                    score[3 - s][3 + dy] += (4-s)/2 * (rubble * rubbleSensitivity + distSq * distSensitivity);
                }
            }
        }

        MapLocation[] reachable = closerToDest(rc, cur);
        int best = Integer.MAX_VALUE;
        Direction go = Direction.EAST;
        for (MapLocation loc : reachable) {
            // 3 + loc.x - cur.x, 3 + loc.y - cur.y
            int curScore = score[3 + loc.x - cur.x][3 + loc.y - cur.y];
            if (curScore < best) {
                best = curScore;
                go = cur.directionTo(loc);
            }
        }

        if (!rc.canMove(go)) go = directions[randomInt(0,7)];

        return go;
    }

    static double nextMove(RobotController rc, MapLocation cur, int depth) throws GameActionException {
        int curDistSq = cur.distanceSquaredTo(destination);
        if (depth == 3) {
            return rc.senseRubble(cur) + curDistSq; // expected time
        }
        Direction go = Direction.EAST;
        double curScore = rc.senseRubble(cur) + curDistSq;
        for (Direction dir : directions) {
            if (rc.canSenseLocation(cur.add(dir))) {
                if (cur.add(dir).distanceSquaredTo(destination) < curDistSq) {
//                    int rubble = rc.senseRubble(rc.adjacentLocation(dir));
                    double score = nextMove(rc, cur.add(dir), depth+1);


                }
            }
        }
        if (depth == 0) {
            if (rc.canMove(go)) rc.move(go);
        }
        return curScore;
    }

    static void setup(RobotController rc) throws GameActionException {
        spawn = rc.getLocation();
        lowX = Math.max(spawn.x - minerLowDist, 0);
        highX = Math.min(spawn.x + minerLowDist, rc.getMapWidth());
        lowY = Math.max(spawn.y - minerLowDist, 0);
        highY = Math.min(spawn.y + minerLowDist, rc.getMapHeight());
    }

    static void run(RobotController rc) throws GameActionException {
        if (destination == null || rc.getLocation().equals(destination)) {
            destination = new MapLocation(randomInt(lowX, highX), randomInt(lowY, highY));
        }
        rc.setIndicatorString(destination.toString());

//        Direction go = rc.getLocation().directionTo(destination);
        Direction go = next(rc);
        for (MapLocation loc : rc.getAllLocationsWithinRadiusSquared(rc.getLocation(), 2)) {
            if (rc.canMineLead(loc)) rc.mineLead(loc);
        }

        if (rc.canMove(go)) rc.move(go);
        else {
            List<Direction> shuffledDirections = Arrays.asList(directions);
            Collections.shuffle(shuffledDirections);
            for (Direction dir : shuffledDirections) {
                if (rc.canMove(dir)) {
                    rc.move(dir);
                    break;
                }
            }
        }
    }
}
