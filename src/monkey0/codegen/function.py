from utils import *

def adj(a, b):
    return cheby(a, origin) == cheby(b, origin) and manh(a,b) == 1

def declare_loc(a):
    return f'MapLocation {loc_var_name(a)};'

def declare_loc_init(a, val):
    return f'MapLocation {loc_var_name(a)} = new MapLocation({val})' 

def declare_var(a, type):
    return f'{type} {a};' 

def declare_init(a, type, val):
    return f'{type} {a} = {val};'

def loc_var_name(a):
    return f'loc_{a[0]+shift}_{a[1]+shift}'

def rubble_name(a):
    return f'rubble_{a[0]+shift}_{a[1]+shift}'

def var_loc(a, name):
    return f'{name}_{a[0]+shift}_{a[1]+shift}'

def header(direction):
    return f'static void {direction}(RobotController rc, MapLocation destination) throws GameActionException {{'

def closer():
    return '}'

shift = 4 # keep variables positive
debug = False

def generate_cardinal(direction, rot):
    ret = header(direction) + '\n'
    ret += declare_init('cur', 'MapLocation', 'rc.getLocation()') + '\n'

    order = [[rotate(x, rot) for x in cheb] for cheb in cardinal_order]
    delta = [[rotate(a, rot), rotate(b, rot)] for a, b in cardinal_delta]

    if debug:
        from pprint import pprint
        pprint(order)
        pprint(delta)

    for cheb in order:
        for v in cheb:
            ret += declare_init(loc_var_name(v), 'MapLocation', f'cur.translate({v[0]}, {v[1]})') + '\n'

    for cheb in order:
        for v in cheb:
            ternary = f'(rc.canSenseLocation({loc_var_name(v)}) && !rc.isLocationOccupied({loc_var_name(v)}))? 20 + rc.senseRubble({loc_var_name(v)}) : 100000'
            # ternary = f'rc.canSenseLocation({loc_var_name(v)}) ? 20 + rc.senseRubble({loc_var_name(v)}) : 100000'
            ret += declare_init(rubble_name(v), 'int', ternary) + '\n'

    for cheb in order:
        for v in cheb:
            ret += declare_init(var_loc(v, 'score'), 'int', 1000000000) + '\n'

    for v in order[0]:
        # estimate the distances
        ret += f'{var_loc(v, "score")} = {var_loc(v, "loc")}.distanceSquaredTo(destination) + {var_loc(v, "rubble")};' + '\n'
        # ret += f'{var_loc(v, "score")} = {var_loc(v, "loc")}.distanceSquaredTo(destination) * 100;' + '\n'

    for relaxation in delta:
        a, b = relaxation
        x = b
        if cheby(a, origin) != cheby(b, origin):
            x = a
        ret += f'{var_loc(a, "score")} = Math.min({var_loc(a, "score")}, {var_loc(b, "score")} + {var_loc(x, "rubble")});' + '\n'

    ret += 'Direction go = Direction.CENTER;' + '\n'
    ret += 'int best = Integer.MAX_VALUE;' + '\n'
    for v in order[-1]:
        ret += f'if ({var_loc(v, "score")} < best) {{' + '\n'
        ret += f'best = {var_loc(v, "score")};' + '\n'
        ret += f'go = cur.directionTo({var_loc(v, "loc")});' + '\n'
        ret += '}' + '\n'
    

    ret += 'if (rc.canMove(go)) rc.move(go);' + '\n'
    ret += closer()
    return ret

def generate_ordinal(direction, rot):
    ret = header(direction) + '\n'
    ret += declare_init('cur', 'MapLocation', 'rc.getLocation()') + '\n'

    order = [[rotate(x, rot) for x in cheb] for cheb in ordinal_order]
    delta = [[rotate(a, rot), rotate(b, rot)] for a, b in ordinal_delta]

    if debug:
        from pprint import pprint
        pprint(order)
        pprint(delta)

    for cheb in order:
        for v in cheb:
            ret += declare_init(loc_var_name(v), 'MapLocation', f'cur.translate({v[0]}, {v[1]})') + '\n'

    for cheb in order:
        for v in cheb:
            # ternary = f'rc.canSenseLocation({loc_var_name(v)}) ? 20 + rc.senseRubble({loc_var_name(v)}) : 100000'
            ternary = f'rc.canSenseLocation({loc_var_name(v)}) && !rc.isLocationOccupied({loc_var_name(v)}) ? 20 + rc.senseRubble({loc_var_name(v)}) : 100000'
            ret += declare_init(rubble_name(v), 'int', ternary) + '\n'

    for cheb in order:
        for v in cheb:
            ret += declare_init(var_loc(v, 'score'), 'int', 1000000000) + '\n'

    for i in range(13):
        ret += declare_init(f'temp{i}', 'int', 0) + '\n' 

    ret += declare_init('runningMin', 'int', 100000) + '\n' 

    for v in order[0]:
        # estimate the distances
        # ret += f'{var_loc(v, "score")} = {var_loc(v, "loc")}.distanceSquaredTo(destination);' + '\n'
        # ret += f'{var_loc(v, "score")} = {var_loc(v, "loc")}.distanceSquaredTo(destination) * 100;' + '\n'
        ret += f'{var_loc(v, "score")} = {var_loc(v, "loc")}.distanceSquaredTo(destination) + {var_loc(v, "rubble")};' + '\n'

    for relaxation in delta:
        a, b = relaxation
        x = b
        if cheby(a, origin) != cheby(b, origin):
            x = a
        ret += f'{var_loc(a, "score")} = Math.min({var_loc(a, "score")}, {var_loc(b, "score")} + {var_loc(x, "rubble")});' + '\n'

    ret += 'Direction go = Direction.CENTER;' + '\n'
    ret += 'int best = Integer.MAX_VALUE;' + '\n'
    for v in order[-1]:
        ret += f'if ({var_loc(v, "score")} < best) {{' + '\n'
        ret += f'best = {var_loc(v, "score")};' + '\n'
        ret += f'go = cur.directionTo({var_loc(v, "loc")});' + '\n'
        ret += '}' + '\n'
    

    ret += 'if (rc.canMove(go)) rc.move(go);' + '\n'

    ret += closer()
    return ret
    pass

def main():

    # print(generate_cardinal('WEST', 1))
    print(generate_ordinal('NORTHWEST', 1))

if __name__ == '__main__':
    main()
