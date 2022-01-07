package pathfinder;

import battlecode.common.*;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.Arrays;
import java.util.Map;

import static pathfinder.Utils.*;

class Miner {

//    static MapLocation destination = new MapLocation(28,5);
    static MapLocation destination = null;
    static MapLocation nearestLead = null;

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

        final int rubbleSensitivity = 5;
        final int distSensitivity = 5;

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
                    score[3 + dx][3 + s] += (4-s) * (rubble * rubbleSensitivity + distSq * distSensitivity);
                }

                if (rc.canSenseLocation(cur.translate(dx, -s))) {
                    MapLocation[] reachable = closerToDest(rc, cur.translate(dx, -s));
                    for (MapLocation loc : reachable) {
                        score[3 + dx][3 + s] = Math.min(score[3 + dx][3 - s], score[3 + loc.x - cur.x][3 - (s + 1)]);
                    }
                    int rubble = rc.senseRubble(cur.translate(dx, -s));
                    int distSq = cur.translate(dx, -s).distanceSquaredTo(destination);
                    int diffSq = cur.distanceSquaredTo(destination) - distSq;
                    score[3 + dx][3 + s] += (4-s) * (rubble * rubbleSensitivity + distSq * distSensitivity);
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
                    score[3 + s][3 + dy] += (4-s) * (rubble * rubbleSensitivity + distSq * distSensitivity);
                }

                if (rc.canSenseLocation(cur.translate(-s, dy))) {
                    MapLocation[] reachable = closerToDest(rc, cur.translate(-s, dy));
                    for (MapLocation loc : reachable) {
                        score[3 - s][3 + dy] = Math.min(score[3 - s][3 + dy], score[3 - (s + 1)][3 + loc.y - cur.y]);
                    }
                    int rubble = rc.senseRubble(cur.translate(-s, dy));
                    int distSq = cur.translate(-s, dy).distanceSquaredTo(destination);
                    int diffSq = cur.distanceSquaredTo(destination) - distSq;
                    score[3 - s][3 + dy] += (4-s) * (rubble * rubbleSensitivity + distSq * distSensitivity);
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

        if (best == Integer.MAX_VALUE) {
//            go = cur.directionTo(reachable[rng.nextInt(reachable.length)]);
        }

        return go;
    }

    static Direction betterNextMove(RobotController rc) throws GameActionException {
        int farthestAdjacentDist = -1;
        for (Direction dir : directions) {
            if (rc.onTheMap(rc.adjacentLocation(dir))) {
                farthestAdjacentDist = Math.max(farthestAdjacentDist, rc.adjacentLocation(dir).distanceSquaredTo(destination));
            }
        }

        MapLocation cur = rc.getLocation();
        int ind = 0;
        MapLocation[] locations = new MapLocation[25];
        MapLocation[] visible = rc.getAllLocationsWithinRadiusSquared(cur, 18);

        int[][] score = new int[7][7];

        for (int s = 3; s >= 1; --s) {
            for (int dx = -s; dx <= s; ++dx) {
                if (rc.onTheMap(cur.translate(dx, s))) {
                    int rubble = rc.senseRubble(cur.translate(dx, s));
                    if (s == 3)  score[dx][3+s] = cur.translate(dx, s).distanceSquaredTo(destination) + rubble / 5;
                    else {
                        score[dx][3+s] = Math.min(Math.min(score[dx + Integer.signum(dx)][3+s], score[dx][3+s + 1]), score[dx + Integer.signum(dx)][3+s+1]) + rubble / 5;
                    }
                }

                if (rc.onTheMap(cur.translate(dx, -s))) {
                    int rubble = rc.senseRubble(cur.translate(dx, -s));
                    if (s == 3)  score[dx][3-s] = cur.translate(dx, s).distanceSquaredTo(destination) + rubble / 5;
                    else {
                        score[dx][3-s] = Math.min(Math.min(score[dx + Integer.signum(dx)][3-s], score[dx][3-s - 1]), score[dx + Integer.signum(dx)][3-s-1]) + rubble / 5;
                    }
                }

                if (rc.onTheMap(cur.translate(dx, s))) {
                    int rubble = rc.senseRubble(cur.translate(dx, s));
                    if (s == 3)  score[dx][3+s] = cur.translate(dx, s).distanceSquaredTo(destination) + rubble / 5;
                    else {
                        int[] temp = score[dx + Integer.signum(dx)];
                        score[dx][3+s] = Math.min(Math.min(temp[s], score[dx][3+s + 1]), temp[s+1]) + rubble / 5;
                    }
                }

                if (rc.onTheMap(cur.translate(dx, -s))) {
                    int rubble = rc.senseRubble(cur.translate(dx, -s));
                    if (s == 3)  score[dx][3-s] = cur.translate(dx, s).distanceSquaredTo(destination) + rubble / 5;
                    else {
                        score[dx][-s] = Math.min(Math.min(score[dx + Integer.signum(dx)][3-s], score[dx][3-s - 1]), score[dx + Integer.signum(dx)][3-s-1]) + rubble / 5;
                    }
                }
            }

        }

//        for (MapLocation location : visible) {
//            if (location.distanceSquaredTo(destination) < farthestAdjacentDist) {
//                locations[ind++] = location;
//            }
//        }

        return null;

    }

    static Direction nextMove(RobotController rc) throws GameActionException {
        MapLocation cur = rc.getLocation();
        int visionSq = RobotType.MINER.visionRadiusSquared;

        MapLocation[] nearbyLead = rc.senseNearbyLocationsWithLead(10000);

        if (nearbyLead.length > 0 && nearestLead == null) {
//            nearestLead = nearbyLead[0];
        }

        Direction go = (nearestLead != null) ? rc.getLocation().directionTo(nearestLead) :  rc.getLocation().directionTo(destination);
        go = cur.directionTo(destination);

        Direction newGo = null;

        int score = Integer.MAX_VALUE;
        final int sensitivity = 5;
        final int EPS = 3;

        MapLocation[] locations = rc.getAllLocationsWithinRadiusSquared(cur, 8);

        int[] moveScores = new int[8]; // score for east, north, ... northeast northwest...
        Arrays.fill(moveScores,0);

        int curDistSq = cur.distanceSquaredTo(destination);

        int i = 0;
        int c1 = 1;
        int c2 = 4;
        for (Direction dir : directions) {
            for (MapLocation location : locations) {
                int distToDest = location.distanceSquaredTo(destination) - rc.adjacentLocation(dir).distanceSquaredTo(destination);
                int distToMove = rc.adjacentLocation(dir).distanceSquaredTo(location);
//                int rubble = rc.senseRubble(location);
                int rubble = 100;

                if (rc.onTheMap(location.add(dir))) {
                    rubble = rc.senseRubble(location.add(dir));
                }

                int scoreDelta = 1000000 * rubble - c1 * distToDest + c2 * distToMove;
                moveScores[i] += scoreDelta;
            }
            i++;
        }

//        for (MapLocation location : locations) {
//            int i = 0;
//
//            int newDistSq = location.distanceSquaredTo(destination);
//            int diffSq = -(curDistSq - newDistSq);
//
//
//            // distance to destination, rubble, distance to other move
//
//            for (Direction dir : directions) {
//                int distToMove = rc.adjacentLocation(dir).distanceSquaredTo(location);
//                int scoreDelta = 1000 * rc.senseRubble(location) + sensitivity * distToMove;
//                moveScores[i] += scoreDelta;
//                i++;
//            }
//        }

        rc.setIndicatorString(Arrays.toString(moveScores));

        int minIndex = 0;
        for (int j = 0; j < 8; ++j) {
            if (moveScores[j] < score) {
                score = moveScores[j];
                minIndex = j;
            }
        }

//        int curDistSq = cur.distanceSquaredTo(destination);
//        for (Direction dir : directions) {
//            int newDistSq = rc.adjacentLocation(dir).distanceSquaredTo(destination);
//            int distSq = -(curDistSq-newDistSq);
//            int newScore = rc.senseRubble(rc.adjacentLocation(dir)) + sensitivity * distSq;
//
//            if (newScore < score) {
//                newGo = dir;
//                score = newScore;
//            }
//        }

//        if (newGo != null) go = newGo;
        go = directions[minIndex];
        return go;
    }

    static void run(RobotController rc) throws GameActionException {
        if (destination == null) {
            destination = new MapLocation(rng.nextInt(40) + 5, rng.nextInt(15) + 5);
//            destination = new MapLocation(22, 12);
//            next(rc);
        }
        rc.setIndicatorString(destination.toString());

        MapLocation cur = rc.getLocation();
//        Direction go = nextMove(rc);
        Direction go = next(rc);

        for (MapLocation location : rc.getAllLocationsWithinRadiusSquared(cur, 2)) {
            if (rc.canMineLead(location)) {
//                rc.mineLead(location);
            }
        }
        if (rc.canMove(go)) rc.move(go);
    }
}
