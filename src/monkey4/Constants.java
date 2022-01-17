package monkey4;

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

    // old indices tracker
    // [0,1] stores enemy and friendly visible soldier/enemy counts
    // [2, 17] stores quadrant information about enemies and allies
    // [18, 33] stores lead information and number of attackers??
    // [34] is num miners alive
    // [35] which quadrant to run to
    // [59, 63] stores Archon locations

    // new indices tracker for monkey 4
    // [0-35] stores 6 x 6 quadrant info about enemies & allies?
    // [36-51] stores 4 x 4 quadrant lead info for miners?
    //
    // [58] number of miners
    // [59, 63] stores Archon locations

    // number of enemies per quadrant
    // number of attackers per quadrant !!!!
    // lead deposits per quadrant
    // number of allied soldiers per quadrant !!!
}
