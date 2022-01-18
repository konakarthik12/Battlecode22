package monkey4;

import battlecode.common.*;

import java.awt.*;

public class Archon {

    static MapLocation destination = null;

    static int archonID = 0;
    static int minersBuilt = 0;
    static int soldiersBuilt = 0;
    static int enemiesInVision = 0;

    static int previousMiners = 0;
    static int minerDeaths = 0;

    // TODO: test adding ceil(enemies/(visible allies)) or other averaging schemes
    // TODO: store index representing which quadrant has most fighting
    // TODO: add code to heal lowest health units

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
                rc.writeSharedArray(58, rc.readSharedArray(58) + 1);
                ++minersBuilt;
            } else if (type.equals(RobotType.SOLDIER)) {
                ++soldiersBuilt;
            }
        }
    }

    static void heal(RobotController rc) throws GameActionException {
        MapLocation toHeal = null;
        int leastHealth = Integer.MAX_VALUE;
        RobotInfo[] info = rc.senseNearbyRobots(-1, rc.getTeam());
        for (RobotInfo r : info) {
            if (r.health < leastHealth) {
                leastHealth = r.health;
                toHeal = r.location;
            }
        }
        if (toHeal != null && rc.canRepair(toHeal)) rc.repair(toHeal);
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
            for (int i = 0; i < 36; ++i) {
                int k = rc.readSharedArray(i);
                if (((k >> 15) & 1) == 1) rc.writeSharedArray(i, k - (1 << 15));
                else rc.writeSharedArray(i, 0);
            }
//            rc.writeSharedArray(58, 0);
        }
    }

    static void summonUnits(RobotController rc) throws GameActionException {
        if (rc.getRoundNum() <= 4) {
            if (enemiesInVision > 0) summonUnitAnywhere(rc, RobotType.SOLDIER);
            MapLocation[] leadLoc = rc.senseNearbyLocationsWithLead();
            if (leadLoc.length > 0) {
                Direction go = rc.getLocation().directionTo(leadLoc[0]);
                if (rc.canBuildRobot(RobotType.MINER, go)) {
                    rc.buildRobot(RobotType.MINER, go);
                    rc.writeSharedArray(58, rc.readSharedArray(58)+1);
                } else {
                    go = go.rotateLeft();
                    if (rc.canBuildRobot(RobotType.MINER, go)) {
                        rc.writeSharedArray(58, rc.readSharedArray(58)+1);
                        rc.buildRobot(RobotType.MINER, go);
                    }
                    go = go.rotateRight().rotateRight();
                    if (rc.canBuildRobot(RobotType.MINER, go)) {
                        rc.buildRobot(RobotType.MINER, go);
                        rc.writeSharedArray(58, rc.readSharedArray(58)+1);
                    }
                }
            } else {
                summonUnitAnywhere(rc, RobotType.MINER);
            }
        } else {
            // prioritize soldiers where needed
            int numEnemies = 0;
            int numSoldiers = 0;
            for (int i = 0; i < 36; ++i) {
                int val = rc.readSharedArray(i);
                numEnemies += (val >> 7) & 127;
                numSoldiers += val & 127;
            }

            int carryingCapacity = 10 + rc.getRoundNum() / (100 + rc.getMapHeight() + rc.getMapWidth()) + (rc.getMapHeight() / 10) + (rc.getMapWidth() / 10);
            carryingCapacity = 10 + rc.getRoundNum() / (200 - rc.getMapWidth() - rc.getMapHeight()) + rc.getMapWidth()/10 + rc.getMapHeight();
//            minerDeaths = 0;
            int roll = Utils.randomInt(1, carryingCapacity);
            rc.setIndicatorString(rc.readSharedArray(58) + " ");

            boolean ok = Utils.randomInt(1, rc.getArchonCount()) == 1;

            if (ok && (numSoldiers < numEnemies || enemiesInVision > 0 || roll-minerDeaths < rc.readSharedArray(58))) {
                summonUnitAnywhere(rc, RobotType.SOLDIER);
            } else if (ok) {
                if (Utils.randomInt(1, rc.getArchonCount()) == 1)
                    summonUnitAnywhere(rc, RobotType.MINER);
            }

//            if (numSoldiers-3 < numEnemies || enemiesInVision > 0 || roll < rc.readSharedArray(58)) {
//                summonUnitAnywhere(rc, RobotType.SOLDIER);
//            } else {
//                if (Utils.randomInt(1, rc.getArchonCount()) == 1)
//                summonUnitAnywhere(rc, RobotType.MINER);
//            }
        }
    }

    static void senseLocalEnemies(RobotController rc) throws GameActionException {
        enemiesInVision += rc.senseNearbyRobots(-1, rc.getTeam().opponent()).length;
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
        minerDeaths = previousMiners - rc.readSharedArray(58);
        senseLocalEnemies(rc);
        summonUnits(rc);
        previousMiners = rc.readSharedArray(58);


        if (archonID == 1 && rc.getRoundNum() % 50 == 0) {
            String printVal = "\n";
            for (int i = 5; i >= 0; --i) {
                for (int j = 0; j < 6; ++j) {
                    int quad = (6*i + j);
                    int val = rc.readSharedArray(quad);
                    int allies = val & 255;
                    int enemies = (val >> 8) & 255;
                    printVal += enemies + " ";
                }
                printVal += "\n";
            }
//            System.out.print(printVal);
        }

        heal(rc);
        reset(rc);
    }

}
