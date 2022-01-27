package monkey0;

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
    static final int soldierPatience = 20;

    // indices
    // [0,1] stores enemy and friendly visible soldier/enemy counts
    // [2, 26] stores quadrant information about enemies and allies and lead and att
    // [27, 33] is focus fire
    // 35/36 is miner macro help
    // 37 builder
    // 55 is [wanted labs][labcount]
    // 56 is lab/archon comm
    // 57 is sages
    // 58 is moving archons
    // [59, 63] stores Archon locations

    // 48 is miners count
}
