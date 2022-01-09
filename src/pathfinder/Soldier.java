package pathfinder;

import battlecode.common.*;

import java.util.Arrays;

import static monkey1.Constants.*;
import static monkey1.utils.Utils.randomInt;
import static monkey1.utils.Utils.rng;

class Soldier {

    static Direction previousStep = Direction.CENTER;

    static MapLocation destination = null;
    static final int soldierLowDist = 20;
    static final int soldierHighDist = 20;

    static int lowX = 0;
    static int highX = 0;
    static int lowY = 0;
    static int highY = 0;
    static MapLocation spawn;

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
//        int ind = 0;
        MapLocation[] locations = new MapLocation[25];
//        MapLocation[] visible = rc.getAllLocationsWithinRadiusSquared(cur, 18);

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
    static void setup(RobotController rc) throws GameActionException {
        spawn = rc.getLocation();
        lowX = Math.max(spawn.x - soldierLowDist, 0);
        highX = Math.min(spawn.x + soldierHighDist, rc.getMapWidth());
        lowY = Math.max(spawn.y - soldierLowDist, 0);
        highY = Math.min(spawn.y + soldierHighDist, rc.getMapHeight());
    }

    static void run(RobotController rc) throws GameActionException {
        if (destination == null) {
            destination = new MapLocation((rng.nextInt(rc.getMapWidth()) - 3) + 3, (rng.nextInt(rc.getMapHeight() - 3) + 3));
        }

        if (rc.getRoundNum() % 50 == 0) {
            int loc = rc.readSharedArray(0);
            if (loc > 0) destination = new MapLocation((loc) & 63, (loc >> 6) & 63);
        }

//        if (destination == null || rc.getLocation().equals(destination)) {
//            destination = new MapLocation(randomInt(lowX, highX), randomInt(lowY, highY));
//        }


        Team enemy = (rc.getTeam() == Team.A) ? Team.B : Team.A;
//        RobotInfo[] test = rc.senseNearbyRobots();
        RobotInfo[] test = rc.senseNearbyRobots(RobotType.SOLDIER.visionRadiusSquared, enemy);

        System.out.println(rc.readSharedArray(0));

        for (RobotInfo r : test) {
            rc.writeSharedArray(0, (r.location.x << 6) + r.location.y);
            if (rc.canAttack(r.location)) {
                rc.attack(r.location);
            }
        }

//        rc.writeSharedArray(0, ());

        // [N, a, b, c, d, e]

        nextMove(rc, rc.getLocation(), 0, previousStep);
//        Direction go = next(rc);
//        if (rc.canMove(go)) rc.move(go);
    }
}
