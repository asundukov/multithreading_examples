package asundukov.multithreading.commons.network;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Thread.sleep;

public class Network {
    public static final double TOTAL_SPEED = 1000;
    public static final double MAX_SPEED_PER_CONNECTION = 100;
    private static final int RECALC_DURATION = 100;
    private static final double TICKS_PER_SECOND = (1000.0 / RECALC_DURATION);
    private static final Set<NetworkConnection> CONNECTIONS = ConcurrentHashMap.newKeySet();

    private static long counter = 0;
    private static double totalDataDownloaded = 0;

    static {
        Thread t = new Thread(Network::downloadNextChunks);
        t.setDaemon(true);
        t.setPriority(7);
        t.start();
    }

    public static void downloadData(double size) {
        NetworkConnection networkConnection = new NetworkConnection(size);
        CONNECTIONS.add(networkConnection);
        synchronized (networkConnection.getLock()) {
            while (!networkConnection.downloadComplete()) {
                try {
                    networkConnection.getLock().wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        CONNECTIONS.remove(networkConnection);
    }

    public static double getNetworkUtilisation() {
        return totalDataDownloaded / counter * TICKS_PER_SECOND;
    }

    private static void downloadNextChunks() {
        while(true) {
            try {
                sleep(RECALC_DURATION);
            } catch (InterruptedException e) {
                return;
            }
            double currentSpeedPerConnection = Math.min(TOTAL_SPEED / CONNECTIONS.size(), MAX_SPEED_PER_CONNECTION);
            double currentChunkSize = currentSpeedPerConnection / TICKS_PER_SECOND;
            for (NetworkConnection networkConnection : CONNECTIONS) {
                synchronized (networkConnection.getLock()) {
                    networkConnection.reduceRemaining(currentChunkSize);
                    networkConnection.getLock().notifyAll();
                }
                totalDataDownloaded += currentChunkSize;
            }
            counter += 1;
        }
    }
}
