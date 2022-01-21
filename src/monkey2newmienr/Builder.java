package monkey2newmienr;

import battlecode.common.*;


class Builder {
    static Direction previousStep = Direction.CENTER;
    static MapLocation destination = null;
    static MapLocation spawn;

    static void setup(RobotController rc) throws GameActionException {
        spawn = rc.getLocation();
    }

    static void setDestination(RobotController rc) throws GameActionException {
        if (destination == null) {
            destination = new MapLocation((Utils.rng.nextInt(rc.getMapWidth()) - 3) + 3, (Utils.rng.nextInt(rc.getMapHeight() - 3) + 3));
        }
    }

    static void act(RobotController rc) throws GameActionException {
        
    }
    static void run(RobotController rc) throws GameActionException {
        setDestination(rc);
        act(rc);
        //if (rc.getLocation().equals(destination)) buildWatchTowerCardinal(rc);
    }
}