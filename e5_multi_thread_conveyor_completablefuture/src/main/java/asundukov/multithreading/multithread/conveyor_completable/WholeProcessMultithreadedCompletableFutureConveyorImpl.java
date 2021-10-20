package asundukov.multithreading.multithread.conveyor_completable;

import asundukov.multithreading.commons.GeneralException;
import asundukov.multithreading.commons.WholeProcess;
import asundukov.multithreading.commons.calculate.CalculateProcessor;
import asundukov.multithreading.commons.calculate.CalculateService;
import asundukov.multithreading.commons.download.DownloadData;
import asundukov.multithreading.commons.download.DownloadProcessor;
import asundukov.multithreading.commons.download.DownloadService;
import asundukov.multithreading.commons.network.Network;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

import static java.lang.Thread.sleep;

public class WholeProcessMultithreadedCompletableFutureConveyorImpl implements WholeProcess {
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

    List<CompletableFuture> finalFutures = new ArrayList<>(tasksAvailable);

    @Override
    public long run() {
        System.out.println("Connection count: " + CONNECTIONS_COUNT);
        System.out.println("Processor count: " + CPU_COUNT);

        LongAdder totalAttempts = new LongAdder();

        while (isNextDownloadTaskAvailable()) {
            tasksAvailable--;
            DownloadProcessor downloadProcessor = downloadService.createProcessor();
            DownloadTask downloadTask = new DownloadTask(downloadProcessor);

            CompletableFuture<DownloadData> downloadResultFuture = CompletableFuture
                    .supplyAsync(downloadTask::call, downloadExecutorService);
            CompletableFuture<Long> calcResultFuture = downloadResultFuture
                    .thenApplyAsync(data -> {
                        CalculateProcessor calculateProcessor = calculateService.createProcessor(data);
                        CalculateTask calculateTask = new CalculateTask(calculateProcessor);
                        return calculateTask.call();
                    }, calculateExecutorService);
            calcResultFuture.thenAccept(totalAttempts::add);
            finalFutures.add(calcResultFuture);
        }

        awaitCompleting();

        return totalAttempts.longValue();
    }

    private void awaitCompleting() {
        finalFutures.forEach(f -> {
            try {
                f.get();
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                throw new GeneralException(e);
            }
        });

        downloadExecutorService.shutdown();
        calculateExecutorService.shutdown();
    }

    private boolean isNextDownloadTaskAvailable() {
        return tasksAvailable > 0;
    }
}
