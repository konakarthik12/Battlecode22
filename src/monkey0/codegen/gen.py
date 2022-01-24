from utils import *
from function import *

def imports(pack='monkey0'):
    ret = ''
    ret += f'package {pack};' + '\n'
    ret += 'import battlecode.common.*;' + '\n'
    return ret

def dirMove():
    # NWSE
    # NE NW SW SE
    funcs = ""
    funcs += generate_cardinal('NORTH', 0) + '\n'
    funcs += generate_cardinal('WEST', 1) + '\n'
    funcs += generate_cardinal('SOUTH', 2) + '\n'
    funcs += generate_cardinal('EAST', 3) + '\n'
    funcs += generate_ordinal('NORTHEAST', 0) + '\n'
    funcs += generate_ordinal('NORTHWEST', 1) + '\n'
    funcs += generate_ordinal('SOUTHWEST', 2) + '\n'
    funcs += generate_ordinal('SOUTHEAST', 3) + '\n'

    return funcs


def move():
    ret = ''
    ret += 'static void move(RobotController rc, MapLocation destination) throws GameActionException {' + '\n'
    ret += 'Direction go = rc.getLocation().directionTo(destination);' + '\n'
    ret += 'switch (go) {' + '\n'
    ret += 'case EAST: EAST(rc, destination); break;' + '\n'
    ret += 'case NORTH: NORTH(rc, destination); break;' + '\n'
    ret += 'case WEST: WEST(rc, destination); break;' + '\n'
    ret += 'case SOUTH: SOUTH(rc, destination); break;' + '\n'
    ret += 'case NORTHEAST: NORTHEAST(rc, destination); break;' + '\n'
    ret += 'case SOUTHEAST: SOUTHEAST(rc, destination); break;' + '\n'
    ret += 'case SOUTHWEST: SOUTHWEST(rc, destination); break;' + '\n'
    ret += 'case NORTHWEST: NORTHWEST(rc, destination); break;' + '\n'
    ret += '}' + '\n'

    ret += '}'
    return ret

def main():
    program = imports() + '\n'
    program += "class UnrolledPathfinder {" + '\n'
    program += dirMove() + '\n'
    program += move() + '\n'
    program += '}' + '\n'
    f = open('../../Pathfinder/UnrolledPathfinder.java', 'w')
    f.write(program)
    f.close()
#     print(program)

if __name__ == '__main__':
    main()
