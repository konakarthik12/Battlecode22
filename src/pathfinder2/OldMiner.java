package pathfinder2;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

import java.util.Arrays;

class OldMiner {

    static MapLocation destination;
    static int[] best = new int[25];
    static int[] pre = new int[25];

    static Direction nextMove(RobotController rc) throws GameActionException {
        MapLocation cur = rc.getLocation();
        int dist = cur.distanceSquaredTo(destination);
        if (dist == 0) return null;
        else if (dist <= 2) {
            return cur.directionTo(destination);
        } else {
            int destIndex = (destination.x - cur.x + 2) * 5 + (destination.y - cur.y + 2);
            if (destIndex < 0 || destIndex >= 25) return cur.directionTo(destination);
            Arrays.fill(best, 10000);
            Arrays.fill(pre, -1);

            best[2 * 5 + 2] = 0;

            for (int a = 0; a < 25; a++) {
                for (int i = 0; i < 25; i++) {
                    int x = (i / 5) - 2;
                    int y = (i % 5) - 2;
                    MapLocation start = new MapLocation(x, y);

                    for (Direction direction : Constants.directions) {
                        MapLocation neighbor = start.add(direction);
                        int j = (neighbor.x + 2) * 5 + (neighbor.y + 2);
                        MapLocation actualLoc = cur.translate(neighbor.x, neighbor.y);
                        if (!rc.onTheMap(actualLoc) || j < 0 || j >= 25 || rc.isLocationOccupied(actualLoc)) {
                            continue;
                        }

                        int w = (10 + (rc.senseRubble(actualLoc)) * 100);
                        if (best[i] + w < best[j]) {
                            best[j] = best[i] + w;
                            pre[j] = i;
                        }
                    }
                }
            }
            System.out.println(Arrays.toString(best));
            System.out.println(Arrays.toString(pre));
            System.out.println(best[destIndex]);
            while (pre[destIndex] != 12) {
                destIndex = pre[destIndex];
            }
            System.out.println(destIndex);
            switch (destIndex) {

                case 16:
                    return Direction.SOUTHEAST;
                case 17:
                    return Direction.EAST;
                case 18:
                    return Direction.NORTHEAST;
                default:
                    System.out.println("bork");
                    System.exit(0);

            }

        }
        return null;

    }

    static void setup(RobotController rc) throws GameActionException {
    }

    static void run(RobotController rc) throws GameActionException {
        destination = rc.getLocation();

        while (rc.canMineLead(rc.getLocation())) {
            rc.mineLead(rc.getLocation());
        }

        rc.setIndicatorString(destination.toString());
        Direction go = nextMove(rc);
        if (go != null && rc.canMove(go)) rc.move(go);
    }
}
