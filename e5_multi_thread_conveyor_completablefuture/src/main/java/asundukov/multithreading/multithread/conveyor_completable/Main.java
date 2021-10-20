package asundukov.multithreading.multithread.conveyor_completable;

import asundukov.multithreading.commons.Runner;

public class Main {
    public static void main(String[] args) {
        Runner runner = new Runner(new WholeProcessMultithreadedCompletableFutureConveyorImpl());
        runner.run();
    }
}
