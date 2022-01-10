package leadfarmer;

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
        if (rc.getLocation().distanceSquaredTo(destination) <= 2) {
            Direction go = directMove(rc);
            if (rc.canMove(go)) rc.move(go);
        } else {
            nextMove(rc, rc.getLocation(), 0, previousStep);
        }
    }

    static void setDestination(RobotController rc) throws GameActionException {
        if (destination == null || (rc.getLocation().equals(destination)) && rc.senseLead(destination) <= 0) {
            ++numReached;
            destination = new MapLocation((Utils.rng.nextInt(rc.getMapWidth()) - 3) + 3, (Utils.rng.nextInt(rc.getMapHeight() - 3) + 3));
        }
        rc.setIndicatorString(destination.toString());
        rc.setIndicatorLine(rc.getLocation(), destination, 255, 255, 255);
    }

    static void run(RobotController rc) throws GameActionException {
        setDestination(rc);
        move(rc);
        /*
        for (MapLocation loc : rc.getAllLocationsWithinRadiusSquared(rc.getLocation(), 20)) {
            if (rc.senseLead(loc) > 2 && Utils.randomInt(0, near) == 0) {
                destination = loc;
                near += 3;
            }
            if (rc.canMineLead(loc)) {
                if (rc.senseLead(loc) > 2) {
                    rc.mineLead(loc);
                }
            }
        }
        near = 0;
        */
//      RobotInfo[] robots = rc.senseNearbyRobots();
        RobotInfo[] robots = rc.senseNearbyRobots(-1, rc.getTeam());
        int minerCount = 0;
        // ...
        for(RobotInfo robot : robots){
            if(robot.type.equals(RobotType.MINER)) {
                minerCount++;
            }
        }
        // ...
        for(MapLocation loc : rc.getAllLocationsWithinRadiusSquared(rc.getLocation(), 16)){
            if(rc.senseLead(loc) > 2 && minerCount < 2){
                destination = loc;
            }
            if(rc.canMineLead(loc) && rc.senseLead(loc) > 5){
                rc.mineLead(loc);
            }
        }
        for (RobotInfo robotInfo : rc.senseNearbyRobots(-1, rc.getTeam().opponent())) {
            rc.writeSharedArray(Utils.randInt(0, 4), (1 << 12) + (robotInfo.location.x << 6) + robotInfo.location.y);
            break;
        }
    }
}
