package monkey3;

import battlecode.common.*;

import java.awt.*;

public class Archon {

    static MapLocation destination = null;

    static int archonID = 0;
    static int minersBuilt = 0;
    static int soldiersBuilt = 0;
    static int enemiesInVision = 0;

    // TODO: test adding ceil(enemies/(visible allies)) or other averaging schemes
    // TODO: store index representing which quadrant has most fighting
    // TODO: change grid from 4 x 4 -> 5 x 5?
//    static int[] enemyEstimates = new int[64];

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
            }
        }
        if (rc.canBuildRobot(type, build)) {
            rc.buildRobot(type, build);
            if (type.equals(RobotType.MINER)) {
                rc.writeSharedArray(34, rc.readSharedArray(34) + 1);
                ++minersBuilt;
            } else if (type.equals(RobotType.SOLDIER)) {
                rc.writeSharedArray(1, rc.readSharedArray(1) + 1);
                ++soldiersBuilt;
            }
        }
    }

    static void heal(RobotController rc) throws GameActionException {
        // TODO add code to heal lowest health units
        RobotInfo[] info = rc.senseNearbyRobots(-1, rc.getTeam());
        for (RobotInfo r : info) {
            if (rc.canRepair(r.location)) {
                rc.repair(r.location);
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
        if (archonID == rc.getArchonCount()) {
//            for (int i = 2; i < 59; ++i) rc.writeSharedArray(i, 0);
            for (int i = 0; i < 59; ++i) if (i != 1 && i != 34) rc.writeSharedArray(i, 0);
//            for (int i = 0; i < 59; ++i) if (i != 1) rc.writeSharedArray(i, 0);
        }
    }

    static void summonUnits(RobotController rc) throws GameActionException {
        if (rc.getRoundNum() <= 4) {
            if (enemiesInVision > 0) summonUnitAnywhere(rc, RobotType.SOLDIER);
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
            } else {
                summonUnitAnywhere(rc, RobotType.MINER);
            }
        } else {
            // prioritize soldiers where needed
            int numEnemies = 0;
            int numSoldiers = rc.readSharedArray(1);
            for (int i = 2; i < 18; ++i) {
                int val = rc.readSharedArray(i);
                numEnemies += (val >> 8) & 255;
            }

//            rc.setIndicatorString(numEnemies + " " + numSoldiers);
            rc.setIndicatorString(numEnemies + " " + numSoldiers + " " + rc.readSharedArray(34));
//            System.out.println(rc.readSharedArray(34));
            int carryingCapacity = 10 + rc.getRoundNum() / 150; // 2000 / 150 = 200 / 15 = 13.5
            int roll = Utils.randomInt(1, carryingCapacity);
            if (numSoldiers-3 < numEnemies || enemiesInVision > 0 || roll < rc.readSharedArray(34) ) {
                summonUnitAnywhere(rc, RobotType.SOLDIER);
            } else {
                if (Utils.randomInt(1, rc.getArchonCount()) == 1)
                summonUnitAnywhere(rc, RobotType.MINER);
            }

        }
    }

    static void senseLocalEnemies(RobotController rc) throws GameActionException {
        enemiesInVision += rc.senseNearbyRobots(-1, rc.getTeam().opponent()).length;
        rc.writeSharedArray(0, rc.readSharedArray(0) + enemiesInVision);
//        for (RobotInfo info : rc.senseNearbyRobots(-1, rc.getTeam().opponent())) {}
    }

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
        summonUnits(rc);

        heal(rc);
        reset(rc);
    }

}
