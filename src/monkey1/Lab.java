package monkey1;

import battlecode.common.*;

class Lab {
    static MapLocation spawn = null;
    static boolean dead = false;
    public static void setup(RobotController rc) throws GameActionException{
        spawn = rc.getLocation();
        rc.writeSharedArray(56, rc.readSharedArray(56) + 1);
        rc.writeSharedArray(56, rc.readSharedArray(56) | 8);
    }
    static void run(RobotController rc) throws GameActionException {
        int lead = rc.getTeamLeadAmount(rc.getTeam());
        int opponentLead = rc.getTeamLeadAmount(rc.getTeam().opponent());
        //transmute if nearest archons tell us to
            if (rc.canTransmute()) rc.transmute();
        if (rc.getHealth() < 13 && !dead) {
            dead = true;
            rc.writeSharedArray(56, rc.readSharedArray(56) - 1);
        }
    }
}
