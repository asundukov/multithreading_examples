package asundukov.multithreading.multithread.conveyor_queue;

import asundukov.multithreading.commons.Runner;

public class Main {
    public static void main(String[] args) {
        Runner runner = new Runner(new WholeProcessMultithreadedConveyorImpl());
        runner.run();
    }
}
