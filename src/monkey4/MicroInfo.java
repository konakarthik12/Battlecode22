package monkey4;

import battlecode.common.*;

import java.awt.*;

public class MicroInfo {

    double incomingDamage = 0;
    int visibleAttackers = 0;
    int visibleEnemies = 0;
    int attackersInRange = 0;
    int numAllies = 1;
    int closeAlly = 0;
    int minDistToEnemy = 1000000;
    int bestScore = Integer.MAX_VALUE;
    int rubble;

    RobotInfo target = null;

    MapLocation nearestEnemy = null;
    MapLocation loc;

    int enemyArchon = 0;
    int allyArchon = 0;
    boolean action;
    boolean move;

    public MicroInfo(RobotController rc, MapLocation loc) throws GameActionException {
        this.rubble = (rc.canSenseLocation(loc)) ? rc.senseRubble(loc) : 1000000;
        this.loc = loc;
        this.action = rc.isActionReady();
        this.move = rc.isMovementReady();

        MapLocation cur = rc.getLocation();

        for (RobotInfo info : rc.senseNearbyRobots(-1)) {
            if (info.team == Utils.team) {
                ++visibleEnemies;
                int dist = loc.distanceSquaredTo(info.location);
                if (dist <= minDistToEnemy) {
                    switch (info.type) {
                        case SOLDIER: case WATCHTOWER: case SAGE:
                            ++visibleAttackers;
                            break;
                        case ARCHON:
                            enemyArchon = 1;
                    }
                    nearestEnemy = info.location;
                    minDistToEnemy = dist;
                }

                if (dist <= 13) {
                    ++attackersInRange;
                    int multiplier;
                    switch (info.type) {
                        case SAGE: multiplier = 0; break;
                        case SOLDIER: multiplier = 1; break;
                        case BUILDER: multiplier = 2; break;
                        case WATCHTOWER: multiplier = 3; break;
                        case MINER: multiplier = 4; break;
                        default: multiplier = 5;
                    }
                    int score = multiplier * 10000 + 25 * info.health + rubble;
                    if (bestScore > score) {
                        bestScore = score;
                        target = info;
                    }
                    incomingDamage += info.getType().damage;
                }
            } else {
                switch (info.type) {
                    case SOLDIER:
                        if (target != null && info.location.distanceSquaredTo(target.location) <= 5) ++closeAlly;
                        ++numAllies;
                        break;
                    case ARCHON:
                        allyArchon = 1;
                }
            }
        }
    }

    boolean isBetter(RobotController rc, MicroInfo o) {
        if (visibleAttackers == 0) {
            return this.minDistToEnemy + this.rubble < o.minDistToEnemy + o.rubble;
        } else if (attackersInRange == 1) {
            if (this.minDistToEnemy > 13) return true;
        }

        if (action) {
            if (closeAlly > visibleAttackers) {
                return 10 * this.minDistToEnemy + this.rubble < 10 * o.minDistToEnemy + this.rubble;
            }
        } else {
            return 10 * this.minDistToEnemy - this.rubble > 10 * o.minDistToEnemy - this.rubble;
        }

        return true;
    }
}
