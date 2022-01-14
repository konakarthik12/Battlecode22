package Aj2;

import battlecode.common.*;

class Miner {

    static int numReached = 0;
    static Direction previousStep = Direction.CENTER;
    static MapLocation destination = null;
    static MapLocation spawn = null;
    static int near = 0;


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
                int nextScore = 10 + rubble + nextMove(rc, next, depth + 1, dir);
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

    static Direction directMove(RobotController rc) throws GameActionException {
        return rc.getLocation().directionTo(destination);
    }

    static void setup(RobotController rc) throws GameActionException {
        spawn = rc.getLocation();
        int dist = 63;
        destination = new MapLocation((Utils.rng.nextInt(rc.getMapWidth()) - 3) + 3, (Utils.rng.nextInt(rc.getMapHeight() - 3) + 3));
        rc.setIndicatorString(destination.toString());
        rc.setIndicatorLine(rc.getLocation(), destination, 255, 255, 255);
        if (rc.getRoundNum() <= 20) {
            for (MapLocation loc : rc.senseNearbyLocationsWithLead()) {
                if (loc.distanceSquaredTo(rc.getLocation()) < dist) {
                    destination = loc;
                    dist = loc.distanceSquaredTo(rc.getLocation());
                }
            }
        }
    }

    static void move(RobotController rc) throws GameActionException {
        for (RobotInfo info : rc.senseNearbyRobots(-1, rc.getTeam().opponent())) {
            if (info.type.equals(RobotType.SOLDIER)) {
                int curr = rc.getLocation().distanceSquaredTo(info.location) + rc.senseRubble(rc.getLocation());
                Direction best = null;
                for (Direction dir: Constants.directions) {
                    if (rc.adjacentLocation(dir).distanceSquaredTo(info.location) + rc.senseRubble(rc.getLocation()) > curr) {
                        curr = rc.adjacentLocation(dir).distanceSquaredTo(info.location) + rc.senseRubble(rc.getLocation());
                        best = dir;
                    }
                }
                if (best == null) {
                    Pathfinder.move(rc, spawn);
                } else {
                    if(rc.canMove(best)) rc.move(best);
                }
                return;
            }
        }
        if (rc.getLocation().distanceSquaredTo(destination) <= 2) {
            Direction go = directMove(rc);
            if (rc.canMove(go)) rc.move(go);
        } else {
            nextMove(rc, rc.getLocation(), 0, previousStep);
        }
    }

    static void setDestination(RobotController rc) throws GameActionException {
        if (destination == null || (rc.getLocation().equals(destination)) && rc.senseLead(destination) <= 2) {
            ++numReached;
            destination = new MapLocation((Utils.rng.nextInt(rc.getMapWidth()) - 3) + 3, (Utils.rng.nextInt(rc.getMapHeight() - 3) + 3));
        }
        rc.setIndicatorString(destination.toString());
        rc.setIndicatorLine(rc.getLocation(), destination, 255, 255, 255);
    }

    static void run(RobotController rc) throws GameActionException {
        if (spawn == null) {
            setup(rc);
            move(rc);
            return;
        }
        setDestination(rc);
        move(rc);
        for (RobotInfo rob : rc.senseNearbyRobots(12)) {
            if (rob.type == rc.getType() && rob.team == rc.getTeam()) near++;
        }
        int lead = 2;
        int cnt = 0;
        for (MapLocation loc : rc.getAllLocationsWithinRadiusSquared(rc.getLocation(), 12)) {
            int newlead = rc.senseLead(loc);
            if (newlead > 2 && Utils.randomInt(0, cnt) <= newlead - lead && near <= 2) {
                destination = loc;
                cnt += 3;
                lead = rc.senseLead(loc);
            }
            while (rc.canMineLead(loc) && rc.senseLead(loc) > 1) rc.mineLead(loc);
        }
        near = 0;

        for (RobotInfo robotInfo : rc.senseNearbyRobots(-1, rc.getTeam().opponent())) {
            rc.writeSharedArray(0, (robotInfo.location.x << 6) + robotInfo.location.y);
            break;
        }
    }
}
