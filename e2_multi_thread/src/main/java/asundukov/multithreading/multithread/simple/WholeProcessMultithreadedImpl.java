package asundukov.multithreading.multithread.simple;

import asundukov.multithreading.commons.GeneralException;
import asundukov.multithreading.commons.WholeProcess;
import asundukov.multithreading.commons.calculate.CalculateService;
import asundukov.multithreading.commons.download.DownloadService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WholeProcessMultithreadedImpl implements WholeProcess {
    private final DownloadService downloadService = new DownloadService();
    private final CalculateService calculateService = new CalculateService();

    private final ExecutorService executorService = Executors.newFixedThreadPool(18);

    private int tasksAvailable = TASKS_AVAILABLE;

    @Override
    public long run() {
        List<Future<Long>> results = new ArrayList<>();
        while (isNextDownloadTaskAvailable()) {
            tasksAvailable--;
            WholeProcessTask wholeProcessTask = new WholeProcessTask(downloadService, calculateService);
            results.add(executorService.submit(wholeProcessTask));
        }

        executorService.shutdown();

        long totalAttempts = 0;
        for (Future<Long> result : results) {
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
