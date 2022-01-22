package monkey1clone;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

class Lab {
    static MapLocation spawn = null;
    static boolean dead = false;
    public static void setup(RobotController rc) throws GameActionException{
        spawn = rc.getLocation();
    }
    static void run(RobotController rc) throws GameActionException {
        int lead = rc.getTeamLeadAmount(rc.getTeam());
        if (lead > 300 && rc.canMutate(rc.getLocation())) rc.mutate(rc.getLocation());
        int opponentLead = rc.getTeamLeadAmount(rc.getTeam().opponent());
        //transmute if nearest archons tell us to
        if (rc.canTransmute() && ((rc.readSharedArray(56) & 1) == 0)) rc.transmute();
    }
}
