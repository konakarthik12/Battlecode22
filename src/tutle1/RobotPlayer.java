package tutle1;

import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

import java.util.Random;

@SuppressWarnings("InfiniteLoopStatement")
public class RobotPlayer {
    public static void run(RobotController rc) throws GameActionException {
        Utils.rng = new Random(rc.getID() + 1);
//        Utils.rng.setSeed(20);
        while (true) {

            switch (rc.getType()) {
                case ARCHON:
                    Archon.run(rc);
                    break;
                case MINER:
                    Miner.run(rc);
                    break;
                case WATCHTOWER:
                    WatchTower.run(rc);
                    break;
                case LABORATORY:
                    Lab.run(rc);
                    break;
                case SAGE:
                    Sage.run(rc);
                    break;
                case BUILDER:
                    Builder.run(rc);
                    break;
                case SOLDIER:
                    Soldier.run(rc);
                    break;
            }

            Clock.yield();
        }
    }
}
