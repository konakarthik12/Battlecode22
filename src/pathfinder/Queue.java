package pathfinder;

public class Queue {
    public int[] q = new int[49];
    public int front = 0;
    public int size = 0;

    public Queue() {

    }

    int poll() {
        return q[front++];
    }

    void push(int X) {
        q[size++] = X;
    }
}
