package monkey0;

import battlecode.common.*;

class Lab {
    static MapLocation spawn = null;
    static boolean dead = false;
    public static void setup(RobotController rc) throws GameActionException{
        spawn = rc.getLocation();
    }
    static void run(RobotController rc) throws GameActionException {
        int lead = rc.getTeamLeadAmount(rc.getTeam());
        int opponentLead = rc.getTeamLeadAmount(rc.getTeam().opponent());
        //transmute if nearest archons tell us to
        if (rc.getRoundNum() > 1500) {
//            System.out.println((rc.readSharedArray(56) & 1));
        }
        if (rc.canTransmute() && (lead >= 100 || (rc.readSharedArray(56) & 1) == 0)) rc.transmute();
    }
}
