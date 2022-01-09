package monkey1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static monkey1.Constants.directions;
import static monkey1.utils.Utils.randomInt;
import static monkey1.utils.Utils.rng;

class Miner {

    static int numReached = 0;
    static int numSteps = 0;
    static Direction previousStep = Direction.CENTER;

    static final int minerLowDist = 10;
    static final int minerHighDist = 18;
    static MapLocation destination = null;
    static MapLocation spawn = null;
    static int highX = 0;
    static int lowX = 0;
    static int highY = 0;
    static int lowY = 0;
    static int near = 0;

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

    static double nextMove(RobotController rc, MapLocation cur, int depth, int dist) throws GameActionException {
        int curDistSq = cur.distanceSquaredTo(destination);
//        int curDistSq = Math.max(Math.abs(cur.x - destination.x), Math.abs(cur.y - destination.y));
        if (depth == 3) {
            return rc.senseRubble(cur) + curDistSq + dist; // expected time
        }
        Direction go = Direction.EAST;
        double curScore = rc.senseRubble(cur) + curDistSq + dist;
        double temp = Double.MAX_VALUE;
        for (Direction dir : directions) {
            if (rc.canSenseLocation(cur.add(dir))) {
                MapLocation temp2 = cur.add(dir);
//                int tempDist = Math.max(Math.abs(temp2.x -destination.x), Math.abs(temp2.y - destination.y));
//                if (tempDist < curDistSq) {
                if (cur.add(dir).distanceSquaredTo(destination) < curDistSq) {
//                    int rubble = rc.senseRubble(rc.adjacentLocation(dir));
                    int rubble = rc.senseRubble(cur);
                    double score = nextMove(rc, cur.add(dir), depth+1, dist + rubble);
                    if (score + rubble < temp) {
                        temp = score + rubble;
                        go = dir;
                    }
                }
            }
        }
        if (depth == 0) {
            if (rc.canMove(go)) rc.move(go);
        }
//        return Math.min(temp, curScore);
        return Math.min(temp, 10000000.0);
    }

    static int nextMove(RobotController rc, MapLocation cur, int depth, Direction lastDir) throws GameActionException {
        int curDist = cur.distanceSquaredTo(destination);
        int score = Integer.MAX_VALUE;
        int rubble = rc.senseRubble(cur);
        if (rc.isLocationOccupied(cur) && cur != rc.getLocation()) rubble = 1000;
        Direction nextDirection = Direction.CENTER;

        if (cur.equals(destination)) {
            return 0;
        } else if (depth == 3) {
            return (20 + rubble) + curDist;
        }

        int toDest = cur.directionTo(destination).ordinal();

        for (int i = (7 + toDest); i < (10 + toDest); ++i) {
            Direction dir = directions[i % 8];
            if (dir.equals(lastDir.opposite())) continue;
            MapLocation next = cur.add(dir);
            if (rc.canSenseLocation(next)) {
//                int dist = next.distanceSquaredTo(destination);
//                if (dist <= curDist) {
                    int nextScore = 20 + rubble + nextMove(rc, next, depth + 1, dir);
                    if (score > nextScore) {
                        score = nextScore;
                        nextDirection = dir;
                    }
//                }
            }
        }

//        for (Direction dir : directions) {
//            if (dir.equals(lastDir.opposite())) continue;
//            MapLocation next = cur.add(dir);
//
//            if (rc.canSenseLocation(next)) {
//                int nextDist = next.distanceSquaredTo(destination);
//                if (nextDist <= curDist) {
//                    int nextScore = 10 + rubble + nextMove(rc, next, depth+1, dir);
//                    if (score > nextScore) {
//                        score = nextScore;
//                        nextDirection = dir;
//                    }
//                }
//            }
//        }

        if (depth == 0) {
            if (rc.canMove(nextDirection)) {
                previousStep = nextDirection;
                rc.move(nextDirection);
            } else {
                for (Direction dir : directions) {
                    MapLocation next = cur.add(dir);
                    if (rc.canSenseLocation(next) && next.distanceSquaredTo(destination) <= curDist) {
                        if (rc.canMove(dir)) rc.move(dir);
                    }
                }
            }
        }

        return score;
    }

    static double betterNextMove(RobotController rc, MapLocation cur, int depth, Direction lastDir) throws GameActionException {
        int curDist = cur.distanceSquaredTo(destination);
//        curDist = Math.abs(destination.x - cur.x) + Math.abs(destination.y - cur.y);
//        int curDist = Math.max(Math.abs(cur.x - destination.x), Math.abs(cur.y - destination.y));
        double score = Double.MAX_VALUE;
        Direction go = Direction.CENTER;

        if (cur.equals(destination)) return 0;
        if (depth == 3) {
            return rc.senseRubble(cur) + curDist + 10;
        }

//        if (rc.getRoundNum() > 300) rc.resign();

        for (Direction dir : directions) {
            if (dir.equals(lastDir.opposite())) continue;
            if (rc.getID() == 13192)
                System.out.println(dir + " " + lastDir);
            MapLocation loc = cur.add(dir);
            if (rc.canSenseLocation(loc)) {
//                int nextDist = Math.max(Math.abs(loc.x - destination.x), Math.abs(loc.y - destination.y));
                int nextDist = loc.distanceSquaredTo(destination);
//                nextDist = Math.abs(destination.x - loc.x) + Math.abs(destination.y - loc.y);
                if (nextDist <= curDist) {
                    int rubble = rc.senseRubble(cur);
                    if (rc.isLocationOccupied(cur) && !cur.equals(rc.getLocation())) rubble = 100;
                    double res = 10 + rubble + betterNextMove(rc, loc, depth + 1, dir);
//                    if (cur.equals(new MapLocation(7, 8))) System.out.println("WTF " + res + " " + dir.toString());
//                    if (rc.getLocation().equals(new MapLocation(7, 8)) && cur.equals(new MapLocation(7, 8)))
//                        System.out.println("WTF " + res + " " + dir);
//                    if (rc.getLocation().equals(new MapLocation(13, 10)))
//                        System.out.println(res + " " + dir + " " + depth + " " + path);
//                    if (rc.getLocation().equals(new MapLocation(13, 10)))
//                        System.out.println(path + " " + dir + " " + res);
                    if (res < score) {
                        score = res;
                        go = dir;
                    }
                }
            }
        }

        if (depth == 0) {
//            if (rc.getLocation().equals(new MapLocation(13, 10))) System.out.println("Chose: " + go.toString());
            if (rc.canMove(go)) {
                previousStep = go;
                rc.move(go);
                ++numSteps;
            }
        }

        return score;
    }

    static Direction directMove(RobotController rc) throws GameActionException {
        return rc.getLocation().directionTo(destination);
    }

    static void setup(RobotController rc) throws GameActionException {
        spawn = rc.getLocation();
        lowX = Math.max(spawn.x - minerLowDist, 0);
        highX = Math.min(spawn.x + minerLowDist, rc.getMapWidth());
        lowY = Math.max(spawn.y - minerLowDist, 0);
        highY = Math.min(spawn.y + minerLowDist, rc.getMapHeight());
    }

    static int called = 0;
    static void run(RobotController rc) throws GameActionException {
        if (destination == null || (rc.getLocation().equals(destination)) && rc.senseLead(destination) <= 2) {
            ++numReached;
//            destination = new MapLocation(randomInt(lowX, highX-1), randomInt(lowY, highY-1));
//            destination = new MapLocation(randomInt(lowX, highX), randomInt(lowY, highY));
            destination = new MapLocation((rng.nextInt(rc.getMapWidth()) - 3) + 3, (rng.nextInt(rc.getMapHeight() - 3) + 3));
        }
        rc.setIndicatorString(destination.toString());
        rc.setIndicatorLine(rc.getLocation(), destination, 255, 255, 255);

//        Direction go = rc.getLocation().directionTo(destination);
//        Direction go = next(rc);
        if (rc.getLocation().distanceSquaredTo(destination) <= 2) {
            Direction go = directMove(rc);
            if (rc.canMove(go)) rc.move(go);
//        } else optimizedNext(rc, encode(rc.getLocation(), 0, previousStep));
        } else nextMove(rc, rc.getLocation(), 0, previousStep);
//        } else betterNextMove(rc, rc.getLocation(), 0, previousStep);

        for (MapLocation loc : rc.getAllLocationsWithinRadiusSquared(rc.getLocation(), 2)) {
            if (rc.senseLead(loc) > 2 && randomInt(0, near) == 0) {
                destination = loc;
                near += 3;
            }
            if (rc.canMineLead(loc)) {
                // if see enemy mine all lead
                if (rc.senseLead(loc) > 1) {
                    rc.mineLead(loc);
                }
            }
        }
        near = 0;

        if (rc.getRoundNum() >= 1998) {
            System.out.println(rc.getID() + " Reached " + numReached + " Locations " + called);
            rc.disintegrate();
        }

//        if (rc.canMove(go)) rc.move(go);
//        else {
//            List<Direction> shuffledDirections = Arrays.asList(directions);
//            Collections.shuffle(shuffledDirections);
//            for (Direction dir : shuffledDirections) {
//                if (rc.canMove(dir)) {
//                    rc.move(dir);
//                    break;
//                }
//            }
//        }
    }
}
