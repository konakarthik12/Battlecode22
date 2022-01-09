package monkey2;

import battlecode.common.*;

import java.util.Arrays;


class Miner {

    static int numReached = 0;
    static int numSteps = 0;
    static Direction previousStep = Direction.CENTER;

    static final int minerLowDist = 10;
    static final int minerHighDist = 18;
    static MapLocation destination = null;
    static MapLocation spawn = null;
    static int near = 0;


    static double nextMove(RobotController rc, MapLocation cur, int depth, int dist) throws GameActionException {
        int curDistSq = cur.distanceSquaredTo(destination);
//        int curDistSq = Math.max(Math.abs(cur.x - destination.x), Math.abs(cur.y - destination.y));
        if (depth == 3) {
            return rc.senseRubble(cur) + curDistSq + dist; // expected time
        }
        Direction go = Direction.EAST;
        double curScore = rc.senseRubble(cur) + curDistSq + dist;
        double temp = Double.MAX_VALUE;
        for (Direction dir : Constants.directions) {
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
            return (10 + rubble) + curDist;
        }

        int toDest = cur.directionTo(destination).ordinal();

        for (int i = (7 + toDest); i < (10 + toDest); ++i) {
            Direction dir = Constants.directions[i % 8];
            if (dir.equals(lastDir.opposite())) continue;
            MapLocation next = cur.add(dir);
            if (rc.canSenseLocation(next)) {
//                int dist = next.distanceSquaredTo(destination);
//                if (dist <= curDist) {
                    int nextScore = 10 + rubble + nextMove(rc, next, depth + 1, dir);
                    if (score > nextScore) {
                        score = nextScore;
                        nextDirection = dir;
                    }
//                }
            }
        }

//        for (Direction dir : Constants.directions) {
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
                for (Direction dir : Constants.directions) {
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

        for (Direction dir : Constants.directions) {
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
    }

    static int called = 0;
    static void run(RobotController rc) throws GameActionException {
        if (destination == null || (rc.getLocation().equals(destination)) && rc.senseLead(destination) <= 2) {
            ++numReached;
//            destination = new MapLocation(Utils.randomInt(lowX, highX-1), Utils.randomInt(lowY, highY-1));
//            destination = new MapLocation(Utils.randomInt(lowX, highX), Utils.randomInt(lowY, highY));
            destination = new MapLocation((Utils.rng.nextInt(rc.getMapWidth()) - 3) + 3, (Utils.rng.nextInt(rc.getMapHeight() - 3) + 3));
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

        for (MapLocation loc : rc.getAllLocationsWithinRadiusSquared(rc.getLocation(), 20)) {
//            if (rc.senseLead(loc) > 7) {
//                destination = loc;
//            }
            if (rc.senseLead(loc) > 2 && Utils.randomInt(0, near) == 0) {
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

        for (RobotInfo robotInfo : rc.senseNearbyRobots(-1, rc.getTeam().opponent())) {
            rc.writeSharedArray(0, (robotInfo.location.x << 6) + robotInfo.location.y);
            break;
        }

//        if (rc.canMove(go)) rc.move(go);
//        else {
//            List<Direction> shuffledConstants.directions = Arrays.asList(Constants.directions);
//            Collections.shuffle(shuffledConstants.directions);
//            for (Direction dir : shuffledConstants.directions) {
//                if (rc.canMove(dir)) {
//                    rc.move(dir);
//                    break;
//                }
//            }
//        }
    }
}
