package Aj2;

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
    // elements [0-4] in array store soldier enemies
    static final int earlyMinerCap = 10; // all archon's combined
    static final double minerSoldierRatio = 2.5;
    static final int minLeadForGold = 2000;
    static final int soldierPatience = 10;
}
