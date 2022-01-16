package turtle3;

import battlecode.common.*;

public class Archon {

    static int archonID = 0;
    static int minersBuilt = 0;
    static int soldiersBuilt = 0;
    static int enemiesInVision = 0;

    // TODO: test adding ceil(enemies/(visible allies)) or other averaging schemes
    static int[] enemyEstimates = new int[64];

    static void summonUnitAnywhere(RobotController rc, RobotType type) throws GameActionException {
        Direction build = Direction.EAST;
        int rubble = 1000;
        for (Direction dir : Constants.directions) {
            if (rc.canBuildRobot(type, dir)) {
                int _rubble = rc.senseRubble(rc.adjacentLocation(dir));
                if (_rubble < rubble) {
                    build = dir;
                    rubble = _rubble;
                }
//                rc.buildRobot(type, dir);
//                if (type == RobotType.MINER) ++minersBuilt;
//                if (type == RobotType.SOLDIER) ++soldiersBuilt;
            }
        }
        if (rc.canBuildRobot(type, build)) {
            rc.buildRobot(type, build);
            if (type == RobotType.MINER) ++minersBuilt;
            if (type == RobotType.SOLDIER) ++soldiersBuilt;
        }
    }

    static void heal(RobotController rc) throws GameActionException {
        RobotInfo[] info = rc.senseNearbyRobots(-1, rc.getTeam());
        for (RobotInfo r : info) {
            if (rc.canAttack(r.location)) {
                rc.attack(r.location);
            }
        }
    }

    static void setup(RobotController rc) throws GameActionException {
        int ind = 63;
        while (rc.readSharedArray(ind) != 0) --ind;
        archonID = 64 - ind;
        rc.writeSharedArray(ind, (1 << 15) + (rc.getLocation().x << 6) + rc.getLocation().y);
    }

    static void reset(RobotController rc) throws GameActionException {
        enemiesInVision = 0;
        for (int i = 2; i <= 17; ++i) {
            enemyEstimates[i] = (enemyEstimates[i]*4 + rc.readSharedArray(i))/5;
        }
        if (archonID == rc.getArchonCount()) {
            for (int i = 2; i < 59; ++i) {
                rc.writeSharedArray(i, 0);
            }
        }
    }

    static void summonUnits(RobotController rc) throws GameActionException {
        if (rc.getRoundNum() <= 4) {
            if (enemiesInVision > 0) {
                summonUnitAnywhere(rc, RobotType.SOLDIER);
                ++soldiersBuilt;
            }
            MapLocation[] leadLoc = rc.senseNearbyLocationsWithLead();
            if (leadLoc.length > 0) {
                Direction go = rc.getLocation().directionTo(leadLoc[0]);
                if (rc.canBuildRobot(RobotType.MINER, go)) rc.buildRobot(RobotType.MINER, go);
                else {
                    go = go.rotateLeft();
                    if (rc.canBuildRobot(RobotType.MINER, go)) rc.buildRobot(RobotType.MINER, go);
                    go = go.rotateRight().rotateRight();
                    if (rc.canBuildRobot(RobotType.MINER, go)) rc.buildRobot(RobotType.MINER, go);
                }
                ++minersBuilt;
            } else {
                summonUnitAnywhere(rc, RobotType.MINER);
                ++minersBuilt;
            }
        }
        else {
            // TODO don't spawn on rubble :skull:
            if (rc.readSharedArray(1) < rc.readSharedArray(0) - 10) {
                summonUnitAnywhere(rc, RobotType.SOLDIER);
                ++soldiersBuilt;
                rc.writeSharedArray(1, rc.readSharedArray(1) + 1);
//            } else if (Utils.randomInt(1, rc.getArchonCount() * 2) <= 1) {
            } else if (Utils.randomInt(1, minersBuilt) <= 1) {
//            } else if (Utils.randomInt(1, rc.readSharedArray(34)) <= 1) {
                summonUnitAnywhere(rc, RobotType.MINER);
                rc.writeSharedArray(34, rc.readSharedArray(34) + 1);
                ++minersBuilt;
            } else {
                summonUnitAnywhere(rc, RobotType.SOLDIER);
                ++soldiersBuilt;
                rc.writeSharedArray(1, rc.readSharedArray(1) + 1);
            }
//            if (rc.getRoundNum() % 5 == 0) minersBuilt--;
//            int x = rc.readSharedArray(1);
//            if (rc.getRoundNum() % 10 == 0) rc.writeSharedArray(1, Math.max(x-1, 0));
        }
    }

    static void senseLocalEnemies(RobotController rc) throws GameActionException {
        enemiesInVision += rc.senseNearbyRobots(-1, rc.getTeam().opponent()).length;
//        for (RobotInfo info : rc.senseNearbyRobots(-1, rc.getTeam().opponent())) {}
    }

    static MapLocation destination;

    static void setDestination(RobotController rc) throws GameActionException {
        int score = Integer.MAX_VALUE;

        for (int i = 63; i > 63 - rc.getArchonCount(); --i) {
            int loc = rc.readSharedArray(i);
            MapLocation cur = new MapLocation((loc >> 6) & 63, loc & 63);
            int a = cur.distanceSquaredTo(new MapLocation(0, 0));
            int b = cur.distanceSquaredTo(new MapLocation(rc.getMapWidth()-1, 0));
            int c = cur.distanceSquaredTo(new MapLocation(rc.getMapWidth()-1, rc.getMapHeight()-1));
            int d = cur.distanceSquaredTo(new MapLocation(0, rc.getMapHeight()-1));
            int _score = Math.min(Math.min(a, b), Math.min(c, d));
            if (score > _score) {
                score = _score;
                destination = cur;
            }
        }

        if (!destination.equals(rc.getLocation()) && rc.canTransform()) {
            rc.transform();
            stillMoving = true;
        }
    }

    static boolean stillMoving = false;

    static void move(RobotController rc) throws GameActionException {
        if (stillMoving) Pathfinder.move(rc, destination);
        if (rc.getLocation().distanceSquaredTo(destination) <= 20) {
            stillMoving = false;
            if (rc.canTransform() && rc.getMode().equals(RobotMode.PORTABLE)) rc.transform();
        }
    }
    static void run(RobotController rc) throws GameActionException {
        senseLocalEnemies(rc);
        heal(rc);

//        if (rc.getRoundNum() == 10) setDestination(rc);
//        if (rc.getRoundNum() > 10) move(rc);

        summonUnits(rc);
        reset(rc);
    }

}
