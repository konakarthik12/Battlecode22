package monkey3;

import battlecode.common.*;

import java.awt.*;


class Soldier {
    static Direction previousStep = Direction.CENTER;
    static MapLocation destination = null;
    static MapLocation spawn = null;
    static boolean toLeadFarm = false;
    static int sinceLastAttack = 0;
    static int visibleEnemies = 0;
    static int visibleAttackers = 0;
    static int visibleAllies = 0;

    static void setup(RobotController rc) throws GameActionException {
        spawn = rc.getLocation();
    }


    static void attack(RobotController rc) throws GameActionException{
        int priority = 100000;
        MapLocation target = rc.getLocation();
        for (RobotInfo robotInfo : rc.senseNearbyRobots(-1, rc.getTeam().opponent())) {
            int multiplier;
            switch (robotInfo.getType()) {
                case SAGE: multiplier = 0; break;
                case SOLDIER: multiplier = 1; break;
                case BUILDER: multiplier = 2; break;
                case WATCHTOWER: multiplier = 3; break;
                case MINER: multiplier = 4; break;
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
    }

    static void quadrantInformation(RobotController rc) throws GameActionException {
        MapLocation cur = rc.getLocation();
        int quadrant = 4 * (4 * cur.x / rc.getMapWidth()) + (4 * cur.y / rc.getMapHeight());
    }

    static void setDestination(RobotController rc) throws GameActionException {
        if (destination == null || destination.equals(rc.getLocation())) {
            destination = new MapLocation((Utils.rng.nextInt(rc.getMapWidth()) - 3) + 3, (Utils.rng.nextInt(rc.getMapHeight() - 3) + 3));
        }

        if (toLeadFarm && rc.getLocation().distanceSquaredTo(spawn) < 15) {
            if (rc.senseLead(rc.getLocation()) == 0) {
                rc.disintegrate();
                return;
            }

            int bestDist = 10000;
            for (MapLocation loc : rc.getAllLocationsWithinRadiusSquared(rc.getLocation(), RobotType.BUILDER.visionRadiusSquared)) {
                if (rc.canSenseLocation(loc) && rc.senseLead(loc) == 0 && !rc.isLocationOccupied(loc)) {
                    if (loc.distanceSquaredTo(rc.getLocation()) < bestDist) {
                        bestDist = loc.distanceSquaredTo(rc.getLocation());
                        destination = loc;
                    }
                }
            }
            return;
        }

        if (rc.getHealth() < 10) {
            destination = spawn;
            toLeadFarm = true;
            int soldiers = rc.readSharedArray(1) - 1;
            rc.writeSharedArray(1, Math.max(soldiers, 0));
        }
    }

    static void micro(RobotController rc) throws GameActionException {
        RobotInfo[] team = rc.senseNearbyRobots(RobotType.SOLDIER.visionRadiusSquared, rc.getTeam());
    }

    static void move(RobotController rc) throws GameActionException {
        MapLocation cur = rc.getLocation();
        int rubble = rc.senseRubble(cur);

        if (visibleEnemies > 0 && visibleEnemies <= visibleAllies+1) {
            Direction go = Direction.CENTER;
            for (Direction dir : Constants.directions) {
                if (rc.canSenseLocation(rc.adjacentLocation(dir)) &&  rc.senseRubble(rc.adjacentLocation(dir)) < rubble) {
                    rubble = rc.senseRubble(rc.adjacentLocation(dir));
                    if (rc.canMove(dir)) go = dir;
                }
            }
            if (rc.canMove(go)) rc.move(go);
        } else if (visibleEnemies > visibleAllies) {
            Pathfinder.move(rc, spawn);
        } else {
            Pathfinder.move(rc, destination);
        }
    }

    static void senseEnemies(RobotController rc) throws GameActionException {
//        int enemies = 0;
//        int attackers = 0;
        visibleEnemies = 0;
        visibleAttackers = 0;
        visibleAllies = 0;

//        visibleAllies = 1 + rc.senseNearbyRobots(-1, rc.getTeam()).length;
//        for (RobotInfo info : rc.senseNearbyRobots(-1, rc.getTeam().opponent())) {
        for (RobotInfo info : rc.senseNearbyRobots(-1)) {
            if (info.team.equals(rc.getTeam().opponent())) {
                ++visibleEnemies;
                switch (info.type) {
                    case SOLDIER: case WATCHTOWER: case SAGE:
                        ++visibleAttackers;
                }
            } else {
                if (info.type.equals(RobotType.SOLDIER)) ++visibleAllies;
            }
        }

        if (Utils.randomInt(1, visibleAllies) == 1) {
            rc.writeSharedArray(0, rc.readSharedArray(0) + visibleEnemies);
        }
    }

    static void run(RobotController rc) throws GameActionException {
        senseEnemies(rc);
        setDestination(rc);
        move(rc);
        attack(rc);

        rc.setIndicatorString(destination.toString());
        rc.setIndicatorLine(rc.getLocation(), destination, 255, 255, 255);
    }
}
