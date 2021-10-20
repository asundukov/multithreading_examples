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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WholeProcessMultithreadedConveyorImpl implements WholeProcess {
    private static final int CONNECTIONS_COUNT = (int) Math.round(Network.TOTAL_SPEED / Network.MAX_SPEED_PER_CONNECTION);
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private final DownloadService downloadService = new DownloadService();
    private final CalculateService calculateService = new CalculateService();

    private final ExecutorService downloadExecutorService = Executors.newFixedThreadPool(CONNECTIONS_COUNT, r -> {
        Thread t = new Thread(r);
        t.setPriority(2);
        return t;
    });
    private final ExecutorService calculateExecutorService = Executors.newFixedThreadPool(CPU_COUNT);

    private int tasksAvailable = TASKS_AVAILABLE;

    @Override
    public long run() {
        System.out.println("Connection count: " + CONNECTIONS_COUNT);
        System.out.println("Processor count: " + CPU_COUNT);
        List<Future<DownloadData>> downloadFutures = new ArrayList<>();
        while (isNextDownloadTaskAvailable()) {
            tasksAvailable--;
            DownloadProcessor downloadProcessor = downloadService.createProcessor();
            DownloadTask downloadTask = new DownloadTask(downloadProcessor);
            downloadFutures.add(downloadExecutorService.submit(downloadTask));
        }

        List<Future<Long>> calculateFutures = new ArrayList<>();
        for (Future<DownloadData> downloadDataFuture : downloadFutures) {
            try {
                CalculateProcessor calculateProcessor = calculateService.createProcessor(downloadDataFuture.get());
                CalculateTask calculateTask = new CalculateTask(calculateProcessor);
                calculateFutures.add(calculateExecutorService.submit(calculateTask));
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                throw new GeneralException(e);
            }
        }

        downloadExecutorService.shutdown();
        calculateExecutorService.shutdown();

        long totalAttempts = 0;
        for (Future<Long> result : calculateFutures) {
            try {
                totalAttempts += result.get();
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                throw new GeneralException(e);
            }
        }

        return totalAttempts;
    }


    private boolean isNextDownloadTaskAvailable() {
        return tasksAvailable > 0;
    }
}
