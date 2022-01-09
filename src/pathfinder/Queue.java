package pathfinder;

class Queue {
    int[] q = new int[49];
    int front = 0;
    int size = 0;

    Queue() {

    }

    int poll() {
        return q[front++];
    }

    void push(int X) {
        q[size++] = X;
    }
}
