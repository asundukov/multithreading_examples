package asundukov.multithreading.singlethread;

import asundukov.multithreading.commons.WholeProcess;
import asundukov.multithreading.commons.calculate.CalculateProcessor;
import asundukov.multithreading.commons.calculate.CalculateService;
import asundukov.multithreading.commons.download.DownloadData;
import asundukov.multithreading.commons.download.DownloadProcessor;
import asundukov.multithreading.commons.download.DownloadService;

public class WholeProcessImpl implements WholeProcess {
    private final DownloadService downloadService = new DownloadService();
    private final CalculateService calculateService = new CalculateService();

    private int tasksAvailable = TASKS_AVAILABLE;

    @Override
    public long run() {
        long totalAttempts = 0;
        while (isNextDownloadTaskAvailable()) {
            tasksAvailable--;
            DownloadData data = downloadData(); // IO heavy process
            totalAttempts += process(data); // CPU heavy process
        }
        return totalAttempts;
    }

    private DownloadData downloadData() {
        DownloadProcessor downloadProcessor = downloadService.createProcessor();
        return downloadProcessor.getData();
    }

    private long process(DownloadData data) {
        CalculateProcessor calculateProcessor = calculateService.createProcessor(data);
        calculateProcessor.calculate();
        return calculateProcessor.getTotalAttempts();
    }

    private boolean isNextDownloadTaskAvailable() {
        return tasksAvailable > 0;
    }
}
