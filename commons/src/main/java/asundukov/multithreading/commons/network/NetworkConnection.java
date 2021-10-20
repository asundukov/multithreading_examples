package asundukov.multithreading.commons.network;

public class NetworkConnection {
    private final Object lock = new Object();
    private double remainingSize;

    public NetworkConnection(double totalSize) {
        this.remainingSize = totalSize;
    }

    public void reduceRemaining(double currentSpeedPerConnection) {
        remainingSize -= currentSpeedPerConnection;
    }

    public Object getLock() {
        return lock;
    }

    public boolean downloadComplete() {
        return remainingSize <= 0;
    }
}
