package asundukov.multithreading.commons.download;

import java.util.concurrent.atomic.AtomicLong;

public class DownloadService {
    private final AtomicLong counter = new AtomicLong(0);

    public DownloadProcessor createProcessor() {
        return new DownloadProcessor(counter.incrementAndGet());
    }
}
