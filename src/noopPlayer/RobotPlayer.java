package noopPlayer;

import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

/**
 * This player's job does nothing, idk we have this
 */

public strictfp class RobotPlayer {

    /**
     * run() is the method that is called when a robot is instantiated in the BattleCode world.
     * It is like the main function for your robot. If this method returns, the robot dies!
     *
     * @param rc The RobotController object. You use it to perform actions from this robot, and to get
     *           information on its current status. Essentially your portal to interacting with the world.
     **/
    @SuppressWarnings({"unused", "InfiniteLoopStatement"})
    public static void run(RobotController rc) throws GameActionException {
        while (true) Clock.yield();
    }

}
