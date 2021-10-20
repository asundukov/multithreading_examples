package asundukov.multithreading.multithread.simple;

import asundukov.multithreading.commons.calculate.CalculateProcessor;
import asundukov.multithreading.commons.calculate.CalculateService;
import asundukov.multithreading.commons.download.DownloadData;
import asundukov.multithreading.commons.download.DownloadProcessor;
import asundukov.multithreading.commons.download.DownloadService;

import java.util.concurrent.Callable;

public class WholeProcessTask implements Callable<Long> {
    private final DownloadService downloadService;
    private final CalculateService calculateService;

    public WholeProcessTask(DownloadService downloadService, CalculateService calculateService) {
        this.downloadService = downloadService;
        this.calculateService = calculateService;
    }

    @Override
    public Long call() {
        DownloadData data = downloadData(); // IO heavy process
        return process(data); // CPU heavy process
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

}
