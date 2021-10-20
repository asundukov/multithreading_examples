package asundukov.multithreading.multithread.simple;

import asundukov.multithreading.commons.Runner;

public class Main {
    public static void main(String[] args) {
        Runner runner = new Runner(new WholeProcessMultithreadedImpl());
        runner.run();
    }
}
