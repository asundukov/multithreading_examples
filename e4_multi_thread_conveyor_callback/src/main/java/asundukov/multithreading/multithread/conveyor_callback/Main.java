package asundukov.multithreading.multithread.conveyor_callback;

import asundukov.multithreading.commons.Runner;

public class Main {
    public static void main(String[] args) {
        Runner runner = new Runner(new WholeProcessMultithreadedCallbackConveyorImpl());
        runner.run();
    }
}
