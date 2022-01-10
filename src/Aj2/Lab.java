package Aj2;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

class Lab {
    static void run(RobotController rc) throws GameActionException {

        int lead = rc.getTeamLeadAmount(rc.getTeam());
        int opponentLead = rc.getTeamLeadAmount(rc.getTeam().opponent());
        if(lead >= Constants.minLeadForGold) {
            rc.transmute();
        }
    }
}
