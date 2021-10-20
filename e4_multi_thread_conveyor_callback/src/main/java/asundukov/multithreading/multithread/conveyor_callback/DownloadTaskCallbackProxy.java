package asundukov.multithreading.multithread.conveyor_callback;

import asundukov.multithreading.commons.download.DownloadData;
import asundukov.multithreading.multithread.conveyor_completable.DownloadTask;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class DownloadTaskCallbackProxy implements Callable<DownloadData> {
    private final DownloadTask downloadTask;
    private final Consumer<DownloadData> downloadDataConsumer;

    public DownloadTaskCallbackProxy(DownloadTask downloadTask, Consumer<DownloadData> downloadDataConsumer) {
        this.downloadTask = downloadTask;
        this.downloadDataConsumer = downloadDataConsumer;
    }

    @Override
    public DownloadData call() {
        DownloadData data = downloadTask.call();
        downloadDataConsumer.accept(data);
        return data;
    }
}
