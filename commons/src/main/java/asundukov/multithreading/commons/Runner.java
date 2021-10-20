package asundukov.multithreading.commons;

import asundukov.multithreading.commons.network.Network;

import java.io.IOException;

public class Runner {
    private final WholeProcess process;

    public Runner(WholeProcess process) {
        this.process = process;
    }

    public void run() {
        long start = System.currentTimeMillis();
        long attempts = process.run();
        long end = System.currentTimeMillis();

        long totalTime = end - start;
        System.out.println();
        System.out.printf("Total duration: %d ms%n", totalTime);
        System.out.printf("Attempts: %d. CPU utilization: %.2f / msec%n", attempts, (double) attempts / (end - start));
        System.out.printf("Avg network utilisation: %.2f mbit/s%n", Network.getNetworkUtilisation());

    }
}
