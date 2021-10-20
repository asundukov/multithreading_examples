package asundukov.multithreading.multithread.conveyor_queue;

import asundukov.multithreading.commons.GeneralException;
import asundukov.multithreading.commons.WholeProcess;
import asundukov.multithreading.commons.calculate.CalculateProcessor;
import asundukov.multithreading.commons.calculate.CalculateService;
import asundukov.multithreading.commons.download.DownloadProcessor;
import asundukov.multithreading.commons.download.DownloadService;
import asundukov.multithreading.commons.network.Network;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

public class WholeProcessMultithreadedCallbackConveyorImpl implements WholeProcess {
    private static final int CONNECTIONS_COUNT = (int) Math.round(Network.TOTAL_SPEED / Network.MAX_SPEED_PER_CONNECTION);
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private DownloadService downloadService = new DownloadService();
    private CalculateService calculateService = new CalculateService();

    private ExecutorService downloadExecutorService = Executors.newFixedThreadPool(CONNECTIONS_COUNT, r -> {
        Thread t = new Thread(r);
        t.setPriority(2);
        return t;
    });
    private ExecutorService calculateExecutorService = Executors.newFixedThreadPool(CPU_COUNT);

    private int tasksAvailable = TASKS_AVAILABLE;

    @Override
    public long run() {
        System.out.println("Connection count: " + CONNECTIONS_COUNT);
        System.out.println("Processor count: " + CPU_COUNT);

        LongAdder totalAttempts = new LongAdder();

        while (isNextDownloadTaskAvailable()) {
            tasksAvailable--;
            DownloadProcessor downloadProcessor = downloadService.createProcessor();
            DownloadTask downloadTask = new DownloadTask(downloadProcessor);
            final LongAdder finalTotalAttempts = totalAttempts;
            DownloadTaskCallbackProxy downloadTaskCallbackProxy = new DownloadTaskCallbackProxy(downloadTask, data -> {
                CalculateProcessor calculateProcessor = calculateService.createProcessor(data);
                CalculateTask calculateTask = new CalculateTask(calculateProcessor);
                calculateExecutorService.submit(new CalculateTaskCallbackProxy(calculateTask, finalTotalAttempts::add));
            });
            downloadExecutorService.submit(downloadTaskCallbackProxy);
        }

        boolean completed = awaitCompleting();

        if (!completed) {
            throw new GeneralException("Process stuck.");
        }

        return totalAttempts.longValue();
    }

    private boolean awaitCompleting() {
        try {
            downloadExecutorService.shutdown();
            boolean completed = downloadExecutorService.awaitTermination(1, TimeUnit.HOURS);
            calculateExecutorService.shutdown();
            return completed && calculateExecutorService.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new GeneralException(e);
        }
    }

    private boolean isNextDownloadTaskAvailable() {
        return tasksAvailable > 0;
    }
}
