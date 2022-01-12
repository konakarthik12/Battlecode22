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
    }

    static void move(RobotController rc) throws GameActionException {
//        if (rc.getLocation().distanceSquaredTo(destination) <= 2) {
//            Direction go = directMove(rc);
//            if (rc.canMove(go)) rc.move(go);
//        } else {
//            nextMove(rc, rc.getLocation(), 0, previousStep);
//        }
        MapLocation loc = rc.getLocation();
        int curDist = loc.distanceSquaredTo(destination);
        Direction nextDirection = Pathfinder.pathfind(rc, destination);

        if (rc.canMove(nextDirection)) {
            previousStep = nextDirection;
            rc.move(nextDirection);
        } else if (curDist > 2) {
            for (Direction dir : Constants.directions) {
                MapLocation next = loc.add(dir);
                if (rc.canSenseLocation(next) && next.distanceSquaredTo(destination) <= curDist) {
                    if (rc.canMove(dir)) rc.move(dir);
                }
            }
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

    static void excessLead(RobotController rc) throws GameActionException{
        int excess = 0;
        int minerCount = 0;
        for(RobotInfo robotinfo : rc.senseNearbyRobots()){
            if(robotinfo.getTeam().equals(rc.getTeam()) && robotinfo.getType().equals(RobotType.MINER)){
                minerCount++;
            }
        }
        for(MapLocation loc : rc.getAllLocationsWithinRadiusSquared(rc.getLocation(), 16)){
             if(rc.senseLead(loc) > 10 && !rc.canMineLead(loc)){
                excess++;
            }
        }
        rc.writeSharedArray(5, rc.readSharedArray(5) + Math.max(0, excess - 4 * minerCount));
    }

    static void run(RobotController rc) throws GameActionException {
        setDestination(rc);
        move(rc);
        excessLead(rc);
        int lead = 2;
        for (MapLocation loc : rc.getAllLocationsWithinRadiusSquared(rc.getLocation(), 5)) {
            int newlead = rc.senseLead(loc);
            if (newlead > 2 && Utils.randomInt(0, near) <= newlead - lead) {
                int set = 1;
                for (Direction dir: Constants.directions) {
                    if (loc.add(dir) == rc.getLocation() || (!rc.canSenseLocation(loc.add(dir)))) continue;
                    RobotInfo x = rc.senseRobotAtLocation(loc.add(dir));
                    if (x != null && x.type == rc.getType() && x.team == rc.getTeam()) {
                        set = 0;
                        break;
                    }
                }
                if (loc != rc.getLocation() && rc.canSenseLocation(loc)) {
                    RobotInfo x = rc.senseRobotAtLocation(loc);
                    if (x != null && x.type == rc.getType() && x.team == rc.getTeam()) set = 0;
                }
                if (set == 1) {
                    destination = loc;
                    near += 3;
                    lead = rc.senseLead(loc);
                }
            }
            while (rc.canMineLead(loc)) {
                if (rc.senseLead(loc) > 1) {
                    rc.mineLead(loc);
                }
                else{
                    break;
                }
            }
        }
        near = 0;

        for (RobotInfo robotInfo : rc.senseNearbyRobots(-1, rc.getTeam().opponent())) {
            rc.writeSharedArray(0, (robotInfo.location.x << 6) + robotInfo.location.y);
            break;
        }
    }
}
