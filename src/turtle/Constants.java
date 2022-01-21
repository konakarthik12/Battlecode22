package turtle;

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

    // new tracker for monkey 4
    // [0-35] stores 6 x 6 quadrant info about enemies & allies?
    // [36-51] stores 4 x 4 quadrant lead info for miners? not yet
    //
    // [58] number of miners
    // [59, 63] stores Archon locations

    // number of enemies per quadrant
    // number of attackers per quadrant !!!!
    // lead deposits per quadrant
    // number of allied soldiers per quadrant !!!
}
