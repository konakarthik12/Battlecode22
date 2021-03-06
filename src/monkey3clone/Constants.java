package monkey3clone;

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

    static final int minLeadForGold = 2000;

    // indices
    // [0,1] stores enemy and friendly visible soldier/enemy counts
    // [2, 17] stores quadrant information about enemies and allies
    // [18, 33] stores lead information and ??
    // [34] number of miners
    // [59, 63] stores Archon locations
}
