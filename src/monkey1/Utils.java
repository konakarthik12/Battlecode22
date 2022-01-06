package monkey1;

import battlecode.common.Direction;

import java.util.Random;

public class Utils {
    static Random rng = null;
    static final Direction[] directions = {
        Direction.EAST,
        Direction.WEST,
        Direction.NORTH,
        Direction.SOUTH,
        Direction.NORTHWEST,
        Direction.NORTHEAST,
        Direction.SOUTHEAST,
        Direction.SOUTHWEST
    };
}
