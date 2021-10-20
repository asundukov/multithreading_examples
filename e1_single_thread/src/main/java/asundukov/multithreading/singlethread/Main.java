package asundukov.multithreading.singlethread;

import asundukov.multithreading.commons.Runner;

public class Main {
    public static void main(String[] args) {
        Runner runner = new Runner(new WholeProcessImpl());
        runner.run();
    }
}
