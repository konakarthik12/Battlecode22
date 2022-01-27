package monkey0clone;

import battlecode.common.*;


class Sage {
    static MapLocation prev = null;
    static MapLocation destination = null;
    static MapLocation enemyLoc = null;
    static int closeAlly = 0;
    static boolean focusFire = false;
    static RobotInfo enemy = null;
    static MapLocation spawn = null;

    static boolean toLeadFarm = false;
    static boolean isBackup = false;

    static int sinceLastAttack = 0;
    static int sinceLastMove = 0;
    static int lastHP = 100;

    static int visibleEnemies = 0;
    static int visibleAttackers = 0;
    static int visibleAllies = 0;

    static void setup(RobotController rc) throws GameActionException {
        spawn = rc.getLocation();
        prev = spawn;
        readQuadrant(rc);
    }

    static void attack(RobotController rc) throws GameActionException{
        // TODO: prefer enemies on lower rubble
        int priority = Integer.MAX_VALUE;
        int units = 0;
        RobotInfo target = null;
        for (RobotInfo robotInfo : rc.senseNearbyRobots(25, rc.getTeam().opponent())) {
            int multiplier;
            units++;
            switch (robotInfo.getType()) {
                case SAGE: multiplier = 0; break;
                case SOLDIER: multiplier = 1; break;
                case BUILDER: multiplier = 2; break;
                case WATCHTOWER: multiplier = 3; break;
                case MINER: multiplier = 4; break;
                default: multiplier = 5;
            }
            int score = 0;
            if (robotInfo.health <= 45) score = multiplier * 1000000 + rc.senseRubble(robotInfo.location) - robotInfo.health - 100;
            else score = multiplier * 1000000 + 10 * rc.senseRubble(robotInfo.location) + robotInfo.health;
            if (score < priority) {
                priority = score;
                target = robotInfo;
            }
        }

        if (target == null) rc.writeSharedArray(0, (rc.getLocation().x << 6) + rc.getLocation().y);
        if (target != null && !target.getType().isBuilding() && ((units > 4 && visibleAllies >= visibleAttackers) || target.health <= 10) && rc.canEnvision(AnomalyType.CHARGE)) {
            rc.envision(AnomalyType.CHARGE);
            sinceLastAttack = 0;
        } else if (target != null && rc.canAttack(target.location))  {
            rc.attack(target.location);
            sinceLastAttack = 0;
        } else {
            ++sinceLastAttack;
        }
    }

    static void setDestination(RobotController rc) throws GameActionException {
        if (toLeadFarm) {
            if (rc.getHealth() >= 94) {
                toLeadFarm = false;
                isBackup = false;
            }
            else destination = spawn;
            return;
        }

        if (sinceLastAttack > 5 && !isBackup || sinceLastMove > 20) {
            readQuadrant(rc);
        }
        if(destination == null) {
            destination = new MapLocation(rc.getMapWidth() / 2 - 5 + Utils.randomInt(0, 10), rc.getMapHeight() / 2 - 5 + Utils.randomInt(0, 10));
        } else if (rc.canSenseLocation(destination)) {
            isBackup = false;
            destination = new MapLocation( Utils.randomInt(1, rc.getMapWidth() - 1), Utils.randomInt(1, rc.getMapHeight() - 1));
            readQuadrant(rc);
        }

        if (rc.getHealth() < 19) {
            destination = spawn;
            // TODO change to closest archon
            toLeadFarm = true;
        }
    }

    static void readQuadrant(RobotController rc) throws GameActionException {
        int mDist = Integer.MAX_VALUE;
        int sDist = Integer.MAX_VALUE;
        MapLocation mLoc = null;
        MapLocation sLoc = null;
        for (int quadrant = 2; quadrant <= 26; ++quadrant) {
            int visAttackers = rc.readSharedArray(quadrant) & 31;
            int visEnemies = (rc.readSharedArray(quadrant) >> 14) & 1;
            int x = (quadrant - 2) / 5 * rc.getMapWidth() / 5 + rc.getMapWidth()/10;
            int y = (quadrant - 2) % 5 * rc.getMapHeight() / 5 + rc.getMapHeight()/10;
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


    static void act(RobotController rc) throws GameActionException {
        MapLocation cur = rc.getLocation();
        int rubble = rc.senseRubble(cur);
        boolean action = rc.isActionReady();
        boolean move = rc.isMovementReady();
        Direction go = Direction.CENTER;
        
        if (visibleEnemies > 0) {
            if (!(enemy.type == RobotType.SOLDIER || enemy.type == RobotType.SAGE)) {
                UnrolledPathfinder.move(rc, enemyLoc);
                attack(rc);
                return;
            }
            int dist = rc.getLocation().distanceSquaredTo(enemyLoc);
            for (Direction dir : Constants.directions) {
                switch((action ? 1 : 0) | (move ? 2 : 0)) {
                    case 1:
                        if (rc.canSenseLocation(rc.adjacentLocation(dir)))
                            rubble = Math.min(rubble, rc.senseRubble(rc.adjacentLocation(dir)));
                        break;
                    case 2:
                        if (rc.canMove(dir) && rc.senseRubble(rc.adjacentLocation(dir)) < rubble
                            && rc.adjacentLocation(dir).distanceSquaredTo(enemyLoc) > enemy.type.actionRadiusSquared + 5) {
                            go = dir;
                            rubble = rc.senseRubble(rc.adjacentLocation(dir));
                        }
                        break;
                    case 3:
                        if (closeAlly >= visibleAttackers) {
                            if (rc.canMove(dir) && rc.senseRubble(rc.adjacentLocation(dir)) <= rubble 
                            && rc.adjacentLocation(dir).distanceSquaredTo(enemyLoc) < dist) {
                                go = dir;
                                rubble = rc.senseRubble(rc.adjacentLocation(dir));
                                dist = rc.adjacentLocation(dir).distanceSquaredTo(enemyLoc);
                            }
                        } else if (lastHP - rc.getHealth() >= 45) {
                            if (rc.canMove(dir) && rc.senseRubble(rc.adjacentLocation(dir)) < rubble) {
                                go = dir;
                                rubble = rc.senseRubble(rc.adjacentLocation(dir));
                                dist = rc.adjacentLocation(dir).distanceSquaredTo(enemyLoc);
                            }
                        } else {
                            if (rc.canMove(dir) && rc.senseRubble(rc.adjacentLocation(dir)) <= rubble
                            && rc.adjacentLocation(dir).distanceSquaredTo(enemyLoc) > dist) {
                                go = dir;
                                rubble = rc.senseRubble(rc.adjacentLocation(dir));
                                dist = rc.adjacentLocation(dir).distanceSquaredTo(enemyLoc);
                            }
                        }
                        break;
                    default:
                        return;
                }
            }
            if (!move) {
                if (rc.getMovementCooldownTurns() < 20 && rubble + 10 <= rc.senseRubble(cur)) {
                    return;
                } else attack(rc);
            }
            if (action) {
                if ((rc.adjacentLocation(go).distanceSquaredTo(enemyLoc) <= 25 && rubble < rc.senseRubble(rc.getLocation()))
                    || !rc.canAttack(enemyLoc)) {
                    if (rc.canMove(go)) rc.move(go);
                    attack(rc);
                } else {
                    attack(rc);
                    if (rc.canMove(go)) rc.move(go);
                    else UnrolledPathfinder.move(rc, spawn);
                }
            } else if (rc.canMove(go)) {
                rc.move(go);
            } else {
                UnrolledPathfinder.move(rc, spawn);
            }
        } else if (visibleAttackers > visibleAllies) {
            attack(rc);
            UnrolledPathfinder.move(rc, spawn);
        } else {
            UnrolledPathfinder.move(rc, destination);
            attack(rc);
        }
    }

    static void writeQuadrantInformation(RobotController rc) throws GameActionException {
        // TODO test no random as well
        MapLocation[] leadLocations = rc.senseNearbyLocationsWithLead();
        int lead = leadLocations.length;
        MapLocation cur = rc.getLocation();
        int quadrant = 5 * (5 * cur.x / rc.getMapWidth()) + (5 * cur.y / rc.getMapHeight());
        // indices 2 - 17 are quadrant information for enemies and allies
        if (visibleEnemies > 0) visibleEnemies = 1;
        if (lead > 15) lead = 15;
        if (visibleAllies > 31) visibleAllies = 31;
        if (visibleAttackers > 31) visibleAttackers = 31;
        int writeValue = (visibleEnemies << 14) + (lead<<10) + (visibleAllies<<5) + visibleAttackers + (1<<15);
            rc.writeSharedArray(2 + quadrant, writeValue);
        assert(quadrant < 27);
    }

    static void senseEnemies(RobotController rc) throws GameActionException {
        visibleEnemies = 0;
        visibleAttackers = 0;
        visibleAllies = 0;
        closeAlly = 0;
        focusFire = false;
        int priority = Integer.MAX_VALUE;
        for (RobotInfo info : rc.senseNearbyRobots(-1, rc.getTeam().opponent())) {
            ++visibleEnemies;
            switch (info.type) {
                case SAGE:
                case SOLDIER: 
                case WATCHTOWER: 
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
            int score = multiplier * 1000000 + rc.senseRubble(info.location) - info.getHealth();
            if (score < priority) {
                enemyLoc = info.location;
                enemy = info;
                priority = score;
            }
        }
        int curDist = 0;
        if (visibleAttackers > 0) {
            curDist = rc.getLocation().distanceSquaredTo(enemyLoc);
        }
        for (RobotInfo info : rc.senseNearbyRobots(-1, rc.getTeam())) {
            switch(info.type) {
                case SOLDIER:
                    visibleAllies++;
                    if (visibleAttackers > 0) {
                        if (info.location.distanceSquaredTo(enemyLoc) <= Math.min(13, curDist)) {
                            closeAlly++;
                        }
                    }
                    break;
                case SAGE:
                    visibleAllies++;
                    if (visibleAttackers > 0) {
                        if (info.location.distanceSquaredTo(enemyLoc) <= Math.min(25, curDist)) {
                            closeAlly++;
                        }
                    }
                    break;
            }
        }
        if (visibleEnemies == 0) {
            enemyLoc = null;
            enemy = null;
        }
    }


    static void run(RobotController rc) throws GameActionException {
        senseEnemies(rc);
        setDestination(rc);
        act(rc);

        //rc.setIndicatorString(destination.toString());
        if (enemyLoc != null) {
            rc.setIndicatorLine(rc.getLocation(), enemyLoc, 255, 0, 0);
        } else {
            rc.setIndicatorLine(rc.getLocation(), destination, 255, 255, 255);
        }
        writeQuadrantInformation(rc);
        spawn = Utils.nearestArchon(rc);
        if (rc.getLocation().equals(prev)) {
            sinceLastMove++;
        } else {
            sinceLastMove = 0;
            prev = rc.getLocation();
        }
        lastHP = rc.getHealth();
        rc.writeSharedArray(53, rc.readSharedArray(53) + 1);
    }
}
