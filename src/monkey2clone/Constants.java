package monkey2clone;

import battlecode.common.Direction;

public class Constants {
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
    // [2, 26] stores quadrant information about enemies and allies
    // [27, 51] stores lead information and ??
    // [59, 63] stores Archon locations
}
