package monkey2clone;

import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

@SuppressWarnings("InfiniteLoopStatement")
public strictfp class RobotPlayer {
    public static void run(RobotController rc) throws GameActionException {
//        Constants.rng = new Random(rc.getID() + 1);
        Utils.setup(rc);
        switch (rc.getType()) {
//                case ARCHON: Archon.setup(rc); break;
            case MINER:
                Miner.setup(rc);
                break;
//                case WATCHTOWER: WatchTower.run(rc); break;
//                case LABORATORY: Lab.run(rc); break;
//                case SAGE: Sage.run(rc); break;
//                case BUILDER: Builder.run(rc); break;
            case SOLDIER:
                Soldier.setup(rc);
                break;
        }

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
