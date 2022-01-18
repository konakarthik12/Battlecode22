package monkey2clone;

import battlecode.common.*;

class Soldier {
    static Direction previousStep = Direction.CENTER;
    static MapLocation destination = null;
    static MapLocation enemyLoc = null;
    static int closeAlly = 0;
    static RobotInfo enemy = null;
    static MapLocation spawn = null;

    static boolean toLeadFarm = false;
    static boolean isBackup = false;

    static int sinceLastAttack = 0;
    static int visibleEnemies = 0;
    static int visibleAttackers = 0;
    static int visibleAllies = 0;

    static void setup(RobotController rc) throws GameActionException {
        spawn = rc.getLocation();
    }

    static void attack(RobotController rc) throws GameActionException{
        // TODO: prefer enemies on lower rubble
        int priority = Integer.MAX_VALUE;
        MapLocation target = rc.getLocation();
        for (RobotInfo robotInfo : rc.senseNearbyRobots(13, rc.getTeam().opponent())) {
            int multiplier;
            switch (robotInfo.getType()) {
                case SAGE: multiplier = 0; break;
                case SOLDIER: multiplier = 1; break;
                case BUILDER: multiplier = 2; break;
                case WATCHTOWER: multiplier = 3; break;
                case MINER: multiplier = 4; break;
                default: multiplier = 5;
            }

            int score = multiplier * 1000000 + 1000 * rc.senseRubble(robotInfo.location) + robotInfo.getHealth();
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

    static void setDestination(RobotController rc) throws GameActionException {
        if (sinceLastAttack > 5 && !isBackup) {
            readQuadrant(rc);
        }
        if(destination == null) {
            destination = new MapLocation(rc.getMapWidth() / 2 - 5 + Utils.randomInt(0, 10), rc.getMapHeight() / 2 - 5 + Utils.randomInt(0, 10));
        } else if (rc.canSenseLocation(destination)) {
            isBackup = false;
            destination = new MapLocation( Utils.randomInt(1, rc.getMapWidth() - 1), Utils.randomInt(1, rc.getMapHeight() - 1));
            readQuadrant(rc);
        }

        if (toLeadFarm && rc.getLocation().distanceSquaredTo(spawn) < 10) {
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

        if (rc.getHealth() < 4) {
            destination = spawn;
            // TODO change to closest archon
            toLeadFarm = true;
            int soldiers = rc.readSharedArray(1) - 1;
            rc.writeSharedArray(1, Math.max(soldiers, 0));
        }
    }

    static void readQuadrant(RobotController rc) throws GameActionException {
        int mDist = Integer.MAX_VALUE;
        int sDist = Integer.MAX_VALUE;
        MapLocation mLoc = null;
        MapLocation sLoc = null;
        for (int quadrant = 2; quadrant <= 26; ++quadrant) {
            int visAttackers = rc.readSharedArray(quadrant + 25) & 127;
            int visEnemies = (rc.readSharedArray(quadrant) >> 8) & 127;
            int x = (quadrant - 2) / 5 * rc.getMapWidth() / 5 + Utils.randomInt(0, rc.getMapWidth()/5 - 1);
            int y = (quadrant - 2) % 5 * rc.getMapHeight() / 5 + Utils.randomInt(0, rc.getMapHeight()/5 - 1);
            MapLocation target = new MapLocation(x, y);
            if (visAttackers > 0 && rc.getLocation().distanceSquaredTo(target) < sDist) {
                isBackup = true;
                sLoc = target;
                sDist = rc.getLocation().distanceSquaredTo(target);
            } else if (visEnemies > 0 && visAttackers == 0 && rc.getLocation().distanceSquaredTo(target) < mDist) {
                isBackup = true;
                mLoc = target;
                mDist = rc.getLocation().distanceSquaredTo(target);
            }
        }
        if (mLoc != null && mDist + 500 < sDist) {
            destination = mLoc;
        } else if (sLoc != null) {
            destination = sLoc;
        }
    }

    static void move(RobotController rc) throws GameActionException {
        MapLocation cur = rc.getLocation();
        int rubble = rc.senseRubble(cur);

        if (visibleEnemies > 0 && visibleAttackers <= visibleAllies) {
            if (!(enemy.type == RobotType.SOLDIER)) {
                Pathfinder.move(rc, enemyLoc);
            }
            Direction go = Direction.CENTER;
            int dist = rc.getLocation().distanceSquaredTo(enemyLoc);
            for (Direction dir : Constants.directions) {
                if (closeAlly >= visibleAttackers) {
                    if (rc.canSenseLocation(rc.adjacentLocation(dir)) && rc.senseRubble(rc.adjacentLocation(dir)) <= rubble
                            && rc.adjacentLocation(dir).distanceSquaredTo(enemyLoc) < dist) {
                        if (rc.canMove(dir)) {
                            go = dir;
                            rubble = rc.senseRubble(rc.adjacentLocation(dir));
                            dist = rc.adjacentLocation(dir).distanceSquaredTo(enemyLoc);
                        }
                    }
                } else {
                    if (rc.canSenseLocation(rc.adjacentLocation(dir)) && rc.senseRubble(rc.adjacentLocation(dir)) <= rubble
                            && rc.adjacentLocation(dir).distanceSquaredTo(enemyLoc) > dist) {
                        if (rc.canMove(dir)) {
                            go = dir;
                            rubble = rc.senseRubble(rc.adjacentLocation(dir));
                            dist = rc.adjacentLocation(dir).distanceSquaredTo(enemyLoc);
                        }
                    }
                }
            }
            if (rc.canMove(go)) rc.move(go);
        } else if (visibleAttackers > visibleAllies) {
            Pathfinder.move(rc, spawn);
        } else {
            Pathfinder.move(rc, destination);
        }
    }

    static void writeQuadrantInformation(RobotController rc) throws GameActionException {
        // TODO test no random as well
        MapLocation[] leadLocations = rc.senseNearbyLocationsWithLead();
        int lead = leadLocations.length;
        MapLocation cur = rc.getLocation();
        int quadrant = 5 * (5 * cur.x / rc.getMapWidth()) + (5 * cur.y / rc.getMapHeight());
        // indices 2 - 17 are quadrant information for enemies and allies
        int writeValue = (visibleEnemies << 8) + visibleAllies + (1<<15);
        rc.writeSharedArray(2 + quadrant, writeValue);

        writeValue = (lead << 8) + visibleAttackers + (1<<15);
        rc.writeSharedArray(27 + quadrant, writeValue);

        assert(quadrant < 27);
    }

    static void senseEnemies(RobotController rc) throws GameActionException {
        visibleEnemies = 0;
        visibleAttackers = 0;
        visibleAllies = 0;
        closeAlly = 0;
        int priority = Integer.MAX_VALUE;
        for (RobotInfo info : rc.senseNearbyRobots(-1)) {
            if (info.team.equals(rc.getTeam().opponent())) {
                ++visibleEnemies;
                switch (info.type) {
                    case SOLDIER: case WATCHTOWER: case SAGE:
                        ++visibleAttackers;
                }
                int multiplier;
                switch (info.getType()) {
                    case SAGE: multiplier = 0; break;
                    case SOLDIER: multiplier = 1; break;
                    case BUILDER: multiplier = 2; break;
                    case WATCHTOWER: multiplier = 3; break;
                    case MINER: multiplier = 4; break;
                    default: multiplier = 5;
                }
                int score = multiplier * 1000000 + 1000 * rc.senseRubble(info.location) + info.getHealth() + rc.getLocation().distanceSquaredTo(info.location);
                if (score < priority) {
                    enemyLoc = info.location;
                    enemy = info;
                    priority = score;
                }
            }
        }
        int curDist = 0;
        if (visibleAttackers > 0) {
            curDist = rc.getLocation().distanceSquaredTo(enemyLoc);
        }
        for (RobotInfo info : rc.senseNearbyRobots(-1)) {
            if (info.team == rc.getTeam()) {
                visibleAllies++;
                if (visibleAttackers > 0) {
                    if (info.location.distanceSquaredTo(enemyLoc) <= curDist) {
                        closeAlly++;
                    }
                }
            }
        }
        if (visibleEnemies == 0) {
            enemyLoc = null;
            enemy = null;
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
        if (enemyLoc != null) {
            rc.setIndicatorLine(rc.getLocation(), enemyLoc, 255, 0, 0);
        } else {
            rc.setIndicatorLine(rc.getLocation(), destination, 255, 255, 255);
        }
        writeQuadrantInformation(rc);
    }
}
