package monkey3;

import battlecode.common.*;

public class Archon {

    static int archonID = 0;
    static int minersAlive = 0;
    static int soldiersAlive = 0;
    static int enemiesInVision = 0;
    static int minersBuilt = 0;
    static int soldiersBuilt = 0;
    // TODO: test adding ceil(enemies/(visible allies)) or other averaging schemes
    static int[] enemyEstimates = new int[64];
    static void analyseMapState(RobotController rc) throws GameActionException{
        minersAlive = rc.readSharedArray(34);
    }
    static void updateMapState(RobotController rc) throws GameActionException{
        if(archonID == rc.getArchonCount()) rc.writeSharedArray(34, 0);
    }
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
//                if (type == RobotType.MINER) ++minersAlive;
//                if (type == RobotType.SOLDIER) ++soldiersAlive;
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
            for (int i = 2; i < 34; ++i) {
                rc.writeSharedArray(i, 0);
            }
        }
        minersBuilt = 0;
    }

    static void summonUnits(RobotController rc) throws GameActionException {
        if (rc.getRoundNum() <= 4) {
            if (enemiesInVision > 0) {
                summonUnitAnywhere(rc, RobotType.SOLDIER);
                ++soldiersAlive;
            }
            MapLocation[] leadLoc = rc.senseNearbyLocationsWithLead();
            if (leadLoc.length > 0) {
                Direction go = rc.getLocation().directionTo(leadLoc[0]);
                if (rc.canBuildRobot(RobotType.MINER, go)){
                    rc.buildRobot(RobotType.MINER, go);
                    ++minersAlive;
                }
                else {
                    go = go.rotateLeft();
                    if (rc.canBuildRobot(RobotType.MINER, go)){
                        rc.buildRobot(RobotType.MINER, go);
                        ++minersAlive;
                    }
                    go = go.rotateRight().rotateRight();
                    if (rc.canBuildRobot(RobotType.MINER, go)){
                        rc.buildRobot(RobotType.MINER, go);
                        ++minersAlive;
                    }
                }
            } else {
                summonUnitAnywhere(rc, RobotType.MINER);
            }
        }
        else {
            // TODO don't spawn on rubble :skull:
            if (rc.readSharedArray(1) < rc.readSharedArray(0) - 10) {
                summonUnitAnywhere(rc, RobotType.SOLDIER);
                ++soldiersAlive;
                rc.writeSharedArray(1, rc.readSharedArray(1) + 1);
//            } else if (Utils.randomInt(1, rc.getArchonCount() * 2) <= 1) {
            } else if (Utils.randomInt(1, 3 * minersAlive / 2) <= 1) {
//            } else if (Utils.randomInt(1, rc.readSharedArray(34)) <= 1) {
                summonUnitAnywhere(rc, RobotType.MINER);
            } else {
                summonUnitAnywhere(rc, RobotType.SOLDIER);
                ++soldiersAlive;
                rc.writeSharedArray(1, rc.readSharedArray(1) + 1);
            }
//            if (rc.getRoundNum() % 5 == 0) minersAlive--;
//            int x = rc.readSharedArray(1);
//            if (rc.getRoundNum() % 10 == 0) rc.writeSharedArray(1, Math.max(x-1, 0));
        }
    }

    static void senseLocalEnemies(RobotController rc) throws GameActionException {
        enemiesInVision += rc.senseNearbyRobots(-1, rc.getTeam().opponent()).length;
//        for (RobotInfo info : rc.senseNearbyRobots(-1, rc.getTeam().opponent())) {}
    }

    static void run(RobotController rc) throws GameActionException {
        analyseMapState(rc);
        senseLocalEnemies(rc);
        heal(rc);
        summonUnits(rc);
        updateMapState(rc);
        reset(rc);
        rc.setIndicatorString(Integer.toString(minersAlive) + " " + Integer.toString(rc.readSharedArray(34), 2));
    }

}
