package Aj2;

import battlecode.common.*;


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
                case SAGE:
                    multiplier = 0;
                    break;
                case SOLDIER:
                    multiplier = 1;
                    break;
                case BUILDER:
                    multiplier = 2;
                    break;
                case WATCHTOWER:
                    multiplier = 3;
                    break;
                case MINER:
                    multiplier = 4;
                    break;
                default:
                    multiplier = 5;
            }
            int score = multiplier * 10000 + robotInfo.getHealth();
            if (score < priority) {
                priority = score;
                target = robotInfo.location;
            }
        }

        if (!rc.getLocation().equals(target)) rc.writeSharedArray(0, (target.x << 6) + target.y);

        if (rc.canAttack(target)){
            rc.attack(target);
            sinceLastAttack = 0;
        } else {
            ++sinceLastAttack;
        }

        if(target == rc.getLocation()) return;
        int rubble = rc.senseRubble(rc.getLocation());
        Direction bestDir = Direction.CENTER;
        for(Direction dir : Aj2.Constants.directions){
            if(rc.onTheMap(rc.getLocation().add(dir)) && rc.senseRubble(rc.getLocation().add(dir)) < rubble && target.distanceSquaredTo(rc.getLocation().add(dir)) <= RobotType.SOLDIER.actionRadiusSquared){
                bestDir = dir;
                rubble = rc.senseRubble(rc.getLocation().add(dir));
            }
        }
        if(rc.canMove(bestDir)) rc.move(bestDir);

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
//            destination = new MapLocation(rc.readSharedArray(8) / 64, rc.readSharedArray(8) % 64);
            if(Utils.randomInt(0, 4) == 0 && rc.readSharedArray(8) == 65535){
                destination = new MapLocation((Utils.rng.nextInt(rc.getMapWidth()) - 3) + 3, (Utils.rng.nextInt(rc.getMapHeight() - 3) + 3));
                rc.writeSharedArray(8, destination.x * 64 + destination.y);
            } else {
                destination = new MapLocation(rc.readSharedArray(8) / 64, rc.readSharedArray(8) % 64);
            }
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
    static void checkLead(RobotController rc) throws GameActionException{
        int lead = 0;
        int teamMiner = 0;
        int oppSoldier = 0;
        for(MapLocation loc : rc.getAllLocationsWithinRadiusSquared(rc.getLocation(), 20)){
            if(rc.senseLead(loc) > 1) lead++;
        }
        for (RobotInfo robotInfo : rc.senseNearbyRobots(-1, rc.getTeam().opponent())) {
            if(robotInfo.getType().equals(RobotType.SOLDIER)){
                oppSoldier++;
            }
        }
        for (RobotInfo robotInfo : rc.senseNearbyRobots(-1, rc.getTeam())) {
            if(robotInfo.getType().equals(RobotType.MINER)){
                teamMiner++;
            }
        }
        if(lead - 5 * teamMiner - 1000 * oppSoldier >= 10){
            rc.writeSharedArray(7, rc.getLocation().x * 64 + rc.getLocation().y);
            rc.writeSharedArray(5, rc.readSharedArray(5) + lead - 5 * teamMiner);
        }
    }
    static void run(RobotController rc) throws GameActionException {
        if(sinceLastAttack >= 2) setDestination(rc);
        if(sinceLastAttack >= 2) nextMove(rc, rc.getLocation(), 0, previousStep);
        attack(rc);
        checkLead(rc);
    }
}
