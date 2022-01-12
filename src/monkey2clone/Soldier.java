package monkey2clone;

import battlecode.common.*;

import java.awt.*;


class Soldier {
    static Direction previousStep = Direction.CENTER;
    static MapLocation destination = null;
    static int sinceLastAttack = 0;

    static void setup(RobotController rc) throws GameActionException {
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

    static void attack(RobotController rc) throws GameActionException{
        int priority = 100000;
        MapLocation target = rc.getLocation();
        for (RobotInfo robotInfo : rc.senseNearbyRobots(-1, rc.getTeam().opponent())) {
            int multiplier;
            switch (robotInfo.getType()) {
                case SAGE: multiplier = 0; break;
                case SOLDIER: multiplier = 1; break;
                case WATCHTOWER: multiplier = 2; break;
                case MINER: multiplier = 3; break;
                case BUILDER: multiplier = 4; break;
                default: multiplier = 5;
            }

            int score = multiplier * 10000 + robotInfo.getHealth();
            if (score < priority) {
                priority = score;
                target = robotInfo.location;
            }
        }

        if (!rc.getLocation().equals(target)) rc.writeSharedArray(0, (target.x << 6) + target.y);
        if (rc.canAttack(target))  {
            rc.attack(target);
            sinceLastAttack = 0;
        } else {
            ++sinceLastAttack;
        }
//        for (RobotInfo robotInfo : rc.senseNearbyRobots(-1, rc.getTeam().opponent())) {
//            if (rc.canAttack(robotInfo.location)) {
//                rc.attack(robotInfo.location);
//                rc.writeSharedArray(0, (robotInfo.location.x << 6) + robotInfo.location.y);
////                rc.writeSharedArray(Utils.randomInt(0,4), (robotInfo.location.x << 6) + robotInfo.location.y);
//            }
//        }
    }

    static void setDestination(RobotController rc) throws GameActionException {
        if (destination == null) {
            destination = new MapLocation((Utils.rng.nextInt(rc.getMapWidth()) - 3) + 3, (Utils.rng.nextInt(rc.getMapHeight() - 3) + 3));
            rc.setIndicatorString(destination.toString());
            rc.setIndicatorLine(rc.getLocation(), destination, 255, 255, 255);
        }

//        if (rc.getRoundNum() % 50 == 0) {
        if (sinceLastAttack >= Constants.soldierPatience) {
            int loc = rc.readSharedArray(0);
            destination = new MapLocation((loc >> 6) & 63, loc & 63);
            rc.setIndicatorString(destination.toString());
            rc.setIndicatorLine(rc.getLocation(), destination, 255, 255, 255);
        }
    }

    static void run(RobotController rc) throws GameActionException {
        setDestination(rc);
        nextMove(rc, rc.getLocation(), 0, previousStep);
        attack(rc);
    }
}
