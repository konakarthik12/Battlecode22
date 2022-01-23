package pathfinder;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

import java.util.*;

class Pathfinder {
    private static final int RUBBLE_ESTIMATE = 35;
    private static RobotController rc;
    static MapLocation rcCur, dest;
    private static HashMap<MapLocation, Pair> memoize;
    private static final HashSet<MapLocation> set = new HashSet<>();

    static Pair recurse(List<MapLocation> path) throws GameActionException {
        count++;
        MapLocation cur = path.get(path.size() - 1);
        if(memoize.containsKey(cur)) return memoize.get(cur);
        List<MapLocation> bestPath = new ArrayList<>();
        long finalScore;

        int distFromRc = rc.getLocation().distanceSquaredTo(cur);
        if (distFromRc > 20) {
            finalScore = manChevy(cur) * (2 * RUBBLE_ESTIMATE + 20);
            StringBuilder builder = new StringBuilder();
            for (MapLocation mapLocation : path) {
                int dx = mapLocation.x - rcCur.x;
                int dy = mapLocation.y - rcCur.y;
                builder.append(rc.getLocation().directionTo(dest)+":"+"[" + dx + "," + dy + "]");
            }
//            System.out.println(builder);
        } else {
            long rubble = smartRubble(cur);
            Pair bestMove = null;

            Direction toDest = rcCur.directionTo(dest);
            Direction beforeDest = toDest.rotateLeft();
            Direction afterDest = toDest.rotateRight();

            Direction beforeBeforeDest = beforeDest.rotateLeft();
            Direction afterAfterDest = afterDest.rotateRight();
            for (Direction dir : new Direction[]{toDest, beforeDest, afterDest, beforeBeforeDest, afterAfterDest}) {
                MapLocation newLoc = cur.add(dir);
                List<MapLocation> newPath = new ArrayList<>(path);
                newPath.add(newLoc);
                if (path.contains(newLoc)) continue;
//                if (set.contains(newLoc)) continue;

                Pair newMove = recurse(newPath);
                if (newMove == null) continue;
                if (bestMove == null || newMove.score < bestMove.score) {
                    bestMove = newMove;
                }
            }

            if (bestMove == null) {
                return null;
            }
//            assert bestMove != null;
            bestPath.addAll(bestMove.list);

            finalScore = rubble + bestMove.score;
        }

        bestPath.add(0, cur);

        Pair pair = new Pair(finalScore, bestPath);
        memoize.put(cur, pair);

        return pair;
    }

    static int count = 0;

    public static Direction pathfind() throws GameActionException {
        if (rcCur.equals(dest)) return Direction.CENTER;
        if (rcCur.isAdjacentTo(dest)) return rcCur.directionTo(dest);
        memoize = new HashMap<>();
        count = 0;
        Pair bestRecurse = recurse(Collections.singletonList(rcCur));
//        assert count == 18 || count == 19;
//        System.out.println(count);
//        System.exit(0);
        assert bestRecurse != null;
        List<MapLocation> bestPath = bestRecurse.list;

        for (int i = 0; i < bestPath.size(); i++) {
            MapLocation start = bestPath.get(i);
            MapLocation end = i + 1 == bestPath.size() ? dest : bestPath.get(i + 1);
            int other = rc.canSenseLocation(end) ? 0 : 255;
            rc.setIndicatorLine(start, end, other, other, 255);
        }


        Direction newDir = rcCur.directionTo(bestPath.get(1));
//        Pair pathfind = pathfind(rc, dest);
//        if (!bestPath.equals(pathfind.list)) {
//            System.out.println(bestRecurse.score + " " + bestPath);
//            System.out.println(pathfind.score + " " + pathfind.list);
//
//            System.out.println("bad move");
//        }
        return newDir;
    }


//    static void addPath(List<MapLocation> bestPath) {
//        MapLocation last = bestPath.get(bestPath.size() - 1);
//        if (rc.canSenseLocation(last)) return;
//        StringBuilder builder = new StringBuilder();
//        MapLocation cur = rc.getLocation();
//        for (int i = 0; i < bestPath.size() - 1; i++) {
//            MapLocation temp = bestPath.get(i);
//            builder.append("(");
//            builder.append(temp.x - cur.x);
//            builder.append(",");
//            builder.append(temp.y - cur.y);
//
//            builder.append(").");
//        }
//        builder.append("(");
//        builder.append(last.x - cur.x);
//        builder.append(",");
//        builder.append(last.y - cur.y);
//
//        builder.append(")");
//
//        paths.add(builder.toString());
//    }

    static boolean inBounds(MapLocation loc) {
        return loc.x >= 0 && loc.x < rc.getMapWidth() && loc.y >= 0 && loc.y < rc.getMapHeight();
    }

//    static boolean manhat

    static boolean occupied(MapLocation loc) throws GameActionException {
        if (rcCur.equals(loc)) return false;
        if (!rc.canSenseLocation(loc)) return true;
        return rc.isLocationOccupied(loc);
    }

    static long rubble(MapLocation loc) throws GameActionException {
        if (rcCur.equals(loc)) return 0;
        return rc.senseRubble(loc);
    }

    static long cooldown(MapLocation loc) throws GameActionException {
        if (rcCur.equals(loc)) return 0;
        else if (rcCur.equals(dest)) return -10000;
        if (!inBounds(loc) || occupied(loc)) return Integer.MAX_VALUE / 1000;

        return 2 * rubble(loc) + 20;
    }

    static int chevyChev(MapLocation a) {
        return Math.max(Math.abs(a.x - dest.x), Math.abs(a.y - dest.y));
    }

    static long manhattan(MapLocation a) {
        return Math.abs(a.x - dest.x) + Math.abs(a.y - dest.y);
    }

    static long manChevy(MapLocation a) {
        return (chevyChev(a) + manhattan(a)) / 2;
    }

    static long smartRubble(MapLocation cur) throws GameActionException {
        if (rc.getLocation().equals(cur)) return 0;
        else if (cur.equals(dest)) return -10000;
        else if (rc.canSenseLocation(cur) && !rc.isLocationOccupied(cur)) return 2L * rc.senseRubble(cur) + 20;
        //out of bounds or is occupied
        return 100000;
    }

    static class Pair {

        private final Long score;
        private final List<MapLocation> list;
//        public Pair() {
//            this.score = Integer.MAX_VALUE;
//            this.list = list;
//        }

        public Pair(Long a, List<MapLocation> list) {
            this.score = a;
            this.list = list;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return score.equals(pair.score) && list.equals(pair.list);
        }

        @Override
        public int hashCode() {
            return Objects.hash(score, list);
        }

        @Override
        public String toString() {
            return score + " " + list;
        }
    }

    static void move(RobotController robotController, MapLocation destination) throws GameActionException {
        rc = robotController;
        rcCur = rc.getLocation();
        dest = destination;
        int curDist = rcCur.distanceSquaredTo(dest);
        Direction nextDirection = pathfind();
        if (rc.canMove(nextDirection)) {
            rc.move(nextDirection);
        } else if (!rc.getLocation().isAdjacentTo(dest)) {
            for (Direction dir : Constants.directions) {
                MapLocation next = rc.getLocation().add(dir);
                if (rc.canSenseLocation(next) && next.distanceSquaredTo(dest) <= curDist) {
                    if (rc.canMove(dir)) rc.move(dir);
                }
            }
        }
    }


}