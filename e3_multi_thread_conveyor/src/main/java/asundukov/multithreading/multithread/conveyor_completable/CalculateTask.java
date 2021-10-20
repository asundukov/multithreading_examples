package asundukov.multithreading.multithread.conveyor_completable;

import asundukov.multithreading.commons.calculate.CalculateProcessor;

import java.util.concurrent.Callable;

public class CalculateTask implements Callable<Long> {
    private final CalculateProcessor calculateProcessor;
    public CalculateTask(CalculateProcessor calculateProcessor) {
        this.calculateProcessor = calculateProcessor;
    }

    @Override
    public Long call() {
        calculateProcessor.calculate();
        return calculateProcessor.getTotalAttempts();
    }
}
