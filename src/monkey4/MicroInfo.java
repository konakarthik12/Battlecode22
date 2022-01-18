package monkey4;

import battlecode.common.*;

public class MicroInfo {

    int incomingDamage;
    int numAllies;
    int minDistToEnemy;
    int rubble;
    MapLocation loc;
    MapLocation nearestEnemy;

    public MicroInfo(RobotController rc, MapLocation loc) throws GameActionException {
        this.loc = loc;
        if (rc.canSenseLocation(loc)) this.rubble = rc.senseRubble(loc);
        else this.rubble = 100000;
        incomingDamage = 0;
        numAllies = 0;
        minDistToEnemy = 100000;
    }

    void update(RobotController rc, RobotInfo info) throws GameActionException {
        if (info.team == rc.getTeam().opponent()) {
            int dist = loc.distanceSquaredTo(info.location);
            if (dist <= minDistToEnemy) minDistToEnemy = dist;
            if (dist <= RobotType.SOLDIER.visionRadiusSquared)
                incomingDamage += (10 * info.getType().damage) / (10 + rc.senseRubble(info.location));
        } else {
            if (info.type == RobotType.SOLDIER) ++numAllies;
        }
    }

    boolean isBetter(RobotController rc, MicroInfo o) {
        // return if this is better than o
        int attackCooldown = rc.getActionCooldownTurns();
        int moveCooldown = rc.getMovementCooldownTurns();
        if (attackCooldown > moveCooldown) {
            if (this.loc.distanceSquaredTo(nearestEnemy) < minDistToEnemy) return false;
        } else {
            if (this.incomingDamage < o.incomingDamage) return true;
            if (this.rubble < o.rubble) return true;
        }

        return false;
    }
}
