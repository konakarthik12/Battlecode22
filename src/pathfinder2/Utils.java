package pathfinder2;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Utils {

    static Random rng = null;
    private static RobotController rc;

    public static void setup(RobotController rc) {
        Utils.rc = rc;
        rng = new Random(rc.getID() + 420);
    }

    /**
     * Returns random integer in range [a,b]
     *
     * @param a lower bound
     * @param b upper bound
     * @return random integer in range [a,b]
     */
    public static int randomInt(int a, int b) {
        return rng.nextInt(b - a + 1) + a;
    }

    public static void main(String[] args) {
        recurse("m", 0, 0);
//        AtomicIntegerArray lengths = new AtomicIntegerArray(25);

//        AtomicInteger sum = new AtomicInteger();
        ArrayList<String> allPaths = new ArrayList<>();
        paths.forEach((mapLocation, strings) -> {
            allPaths.addAll(strings);
        });
        allPaths.sort(Comparator.comparingInt(String::length));
        System.out.println("done");
    }

    static Map<MapLocation, ArrayList<String>> paths = new HashMap<>();

    private static void recurse(String m, int x, int y) {
        MapLocation loc = new MapLocation(x, y);
        if (paths.containsKey(loc)) {
            Iterator<String> iter = paths.get(loc).iterator();
            while (iter.hasNext()) {
                String old = iter.next();
                if (isSubset(old, m)) {
                    iter.remove();
                } else if (isSubset(m, old)) {
                    return;
                }
            }


        } else {
            paths.put(loc, new ArrayList<>());
        }

        paths.get(loc).add(m);
        for (Direction direction : Constants.directions) {
            MapLocation add = loc.add(direction);
            if (!bounds(add)) continue;
            char newChar = toChar(add);
            if (m.indexOf(newChar) != -1) continue;
            recurse(m + newChar, add.x, add.y);
        }


    }

    private static boolean bounds(MapLocation add) {
        return Math.abs(add.x) <= 2 && Math.abs(add.y) <= 2;
    }


    static String s = "abcdefghijklmnopqrstuvwxy";

    public static MapLocation toLocation(char c) {
        int x = s.indexOf(c) % 5;
        int y = s.indexOf(c) / 5;
        return new MapLocation(x - 2, -y + 2);

    }

    public static char toChar(MapLocation loc) {
        return s.charAt((loc.x + 2) + -(loc.y - 2) * 5);
    }


    public static boolean isSubset(String big, String small) {
        for (char c : small.toCharArray()) {
            if (big.indexOf(c) == -1) return false;
        }
        return true;
    }

    public static Direction randomDirection() {
        return Constants.directions[rng.nextInt(8)];
    }
}
