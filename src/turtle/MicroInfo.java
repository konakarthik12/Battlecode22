package turtle;

import battlecode.common.*;

public class MicroInfo {

    int incomingDamage;
    int numAllies;
    int minDistToEnemy;
    int rubble;
    int visibleAttackers;
    MapLocation loc;
    MapLocation nearestEnemy;

    public MicroInfo(RobotController rc, MapLocation loc, int visibleAttackers) throws GameActionException {
        this.loc = loc;
        if (rc.canSenseLocation(loc)) this.rubble = rc.senseRubble(loc);
        else this.rubble = 100000;
        this.incomingDamage = 0;
        this.numAllies = 0;
        this.minDistToEnemy = 100000;
        this.visibleAttackers = visibleAttackers;
    }

    public MicroInfo(RobotController rc, MapLocation loc) throws GameActionException {
        this.loc = loc;
        if (rc.canSenseLocation(loc)) this.rubble = rc.senseRubble(loc);
        else this.rubble = 100000;
        this.incomingDamage = 0;
        this.numAllies = 1;
        this.minDistToEnemy = 100000;
    }

    void update(RobotController rc, RobotInfo info) throws GameActionException {
        if (info.team.equals(Utils.opponent)) {
            int dist = loc.distanceSquaredTo(info.location);
            if (dist <= minDistToEnemy) {
                if (info.type != RobotType.ARCHON) {
                    nearestEnemy = info.location;
                    minDistToEnemy = dist;
                }
            }
            if (dist <= RobotType.SOLDIER.actionRadiusSquared)
                incomingDamage += (10 * info.getType().damage) / (10 + rc.senseRubble(info.location));
        } else {
            if (info.type == RobotType.SOLDIER) ++numAllies;
        }
    }

    boolean isBetter(RobotController rc, MicroInfo o) {
        // return if this is better than o
        int attackCooldown = (rc.getActionCooldownTurns() + 9) / 10;
        int moveCooldown = (rc.getMovementCooldownTurns() + 9) / 10;
        if (attackCooldown > moveCooldown && this.incomingDamage > 0) {
            if (nearestEnemy != null && this.loc.distanceSquaredTo(nearestEnemy) < minDistToEnemy)
                return false;
            return this.rubble < o.rubble;
        } else {
            if (this.incomingDamage == 0) {
                return this.minDistToEnemy + this.rubble < o.minDistToEnemy + o.rubble;
            }
            if (4 * visibleAttackers > 3*numAllies+3) return -this.rubble + 10 * this.minDistToEnemy > 10 * o.minDistToEnemy - o.rubble;
            else if (4 * numAllies > 3 * visibleAttackers+3) return 10 * this.minDistToEnemy + this.rubble < 10 * o.minDistToEnemy + o.rubble;
            if (this.incomingDamage < o.incomingDamage) return true;
            if (this.rubble < o.rubble - 10) return true;
        }

        return false;
    }
}
