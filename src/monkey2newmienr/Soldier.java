package monkey2newmienr;

import battlecode.common.*;
import monkey2newmienr.Utils;


class Soldier {
    static Direction previousStep = Direction.CENTER;
    private static MapLocation destination = null;
    static boolean target = false;
    static void setup(RobotController rc) throws GameActionException {
    }

    static int nextMove(RobotController rc, MapLocation cur, int depth, Direction lastDir) throws GameActionException {
        if (destination == null) {
            int offset = Utils.randomInt(0, 7);
            while(!rc.canMove(Constants.directions[offset%8]))offset++;
            rc.move(Constants.directions[offset%8]);
            return 0;
        }
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
            Direction dir = Constants.directions[i % 8];
            if (dir.equals(lastDir.opposite())) continue;
            MapLocation next = cur.add(dir);
            if (rc.canSenseLocation(next)) {
                int nextScore = 20 + rubble + nextMove(rc, next, depth + 1, dir);
                if (score > nextScore) {
                    score = nextScore;
                    nextDirection = dir;
                }
            }
        }

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

    static void attack(RobotController rc) throws GameActionException {
        for (RobotInfo robotInfo : rc.senseNearbyRobots(-1, rc.getTeam().opponent())) {
            if (rc.canAttack(robotInfo.location)) {
                rc.attack(robotInfo.location);
                rc.writeSharedArray(0, (robotInfo.location.x << 6) + robotInfo.location.y);
            }
        }
    }

    static void setDestination(RobotController rc) throws GameActionException {
        if (destination == null) {
            destination = new MapLocation((Utils.rng.nextInt(rc.getMapWidth()) - 3) + 3, (Utils.rng.nextInt(rc.getMapHeight() - 3) + 3));
            rc.setIndicatorString(destination.toString());
        }

        if (rc.getRoundNum() % 50 == 0) {
            int loc = rc.readSharedArray(0);
            destination = new MapLocation((loc >> 6) & 63, loc & 63);
            rc.setIndicatorString(destination.toString());
            target = true;
        }
        /*else if (rc.getRoundNum() % 10 == 0 && target == false) {
            int loc = rc.readSharedArray(1 + Utils.randomInt(0, rc.getArchonCount()));
            destination = new MapLocation((loc >> 6) & 63, loc & 63);
            rc.setIndicatorString(destination.toString());
        }*/
    }

    static void run(RobotController rc) throws GameActionException {
        setDestination(rc);
        nextMove(rc, rc.getLocation(), 0, previousStep);
        attack(rc);
    }
}
