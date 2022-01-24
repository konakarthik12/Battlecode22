cardinal_order = [[(-4, 0), (-4,1), (-4,2), (-2,4), (-1,4), (0,4), (1,4), (2,4), (4,2), (4,1), (4,0)],
                [(-3, 0), (-3,1), (-3, 2), (-3,3), (-2,3), (-1,3), (0,3), (1,3), (2,3), (3,3), (3,2), (3,1), (3,0)],
                [(-2,0), (-2,1), (-2,2), (-1,2), (0,2), (1,2), (2,2), (2,1), (2,0)],
                [(-1,0), (-1,1), (0,1), (1,1), (1,0)]]

ordinal_order = [[(-2,4), (-1,4), (0,4), (1,4), (2,4), (4,2), (4,1), (4,0), (4,-1), (4,-2)],
                [(-3,3), (-2,3), (-1,3), (0,3), (1,3), (2,3), (3,3), (3,2), (3,1), (3,0), (3,-1), (3,-2), (3,-3)],
                [(-2,2), (-1,2), (0,2), (1,2), (2,2), (2,1), (2,0), (2,-1), (2,-2)],
                [(-1,1), (0,1), (1,1), (1,0), (1,-1)]]

f = open("N.txt", 'r')
cardinal_delta = eval(f.readline())
f.close()
f = open("NE.txt", 'r')
ordinal_delta = eval(f.readline())
f.close()

rotation = [(0, -1), (1, 0)]

cardinal = ['NORTH', 'WEST', 'SOUTH', 'EAST']
ordinal = ['NORTHEAST', 'NORTHWEST', 'SOUTHWEST', 'SOUTHEAST']

origin = (0,0)

def cheby(a, b):
    return max(abs(a[0]-b[0]), abs(a[1]-b[1]))

def manh(a, b):
    return abs(a[0]-b[0]) + abs(a[1]-b[1])

def rot_helper(point):
    return (-point[1], point[0])

def rotate(point, times=1):
    for _ in range(times):
        point = rot_helper(point)

    return point

def main():
    pass

if __name__ == '__main__':
    main()
