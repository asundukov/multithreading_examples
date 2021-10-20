package asundukov.multithreading.multithread.conveyor_callback;

import asundukov.multithreading.multithread.conveyor_completable.CalculateTask;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class CalculateTaskCallbackProxy implements Callable<Long> {
    private final CalculateTask calculateTask;
    private final Consumer<Long> longConsumer;

    public CalculateTaskCallbackProxy(CalculateTask calculateTask, Consumer<Long> longConsumer) {
        this.calculateTask = calculateTask;
        this.longConsumer = longConsumer;
    }

    @Override
    public Long call() {
        Long attempts = calculateTask.call();
        longConsumer.accept(attempts);
        return attempts;
    }
}
