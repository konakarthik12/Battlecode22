package monkey3;

import battlecode.common.Direction;

class Constants {
    static final Direction[] directions = {
            Direction.NORTH,
            Direction.NORTHEAST,
            Direction.EAST,
            Direction.SOUTHEAST,
            Direction.SOUTH,
            Direction.SOUTHWEST,
            Direction.WEST,
            Direction.NORTHWEST
    };
    static final int earlyMinerCap = 10; // all archon's combined
    static final double minerSoldierRatio = 2.5;
    static final int minLeadForGold = 2000;
    static final int soldierPatience = 20;

    // indices
    // [0,1] stores enemy and friendly visible soldier/enemy counts
    // [2, 17] stores quadrant information about enemies and allies
    // [18, 33] stores lead information and ??
    // [34] is num miners died in this turn
    //bit hack bit 12,13,14,15 store whether archon built miner or not, to keep track of miner built
    //and the lower bits store the number of miner practically dead
    // [35] is num miners built
    // [59, 63] stores Archon locations
}
