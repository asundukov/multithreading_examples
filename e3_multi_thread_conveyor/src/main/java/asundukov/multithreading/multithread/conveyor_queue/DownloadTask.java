package asundukov.multithreading.multithread.conveyor_queue;

import asundukov.multithreading.commons.download.DownloadData;
import asundukov.multithreading.commons.download.DownloadProcessor;

import java.util.concurrent.Callable;

public class DownloadTask implements Callable<DownloadData> {
    private final DownloadProcessor downloadProcessor;

    public DownloadTask(DownloadProcessor downloadProcessor) {
        this.downloadProcessor = downloadProcessor;
    }

    @Override
    public DownloadData call() {
        return downloadData(); // IO heavy process
    }

    private DownloadData downloadData() {
        return downloadProcessor.getData();
    }

}
