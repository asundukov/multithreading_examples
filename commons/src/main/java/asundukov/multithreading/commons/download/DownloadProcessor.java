package asundukov.multithreading.commons.download;

import asundukov.multithreading.commons.network.Network;

import static asundukov.multithreading.commons.Utils.RANGE;
import static asundukov.multithreading.commons.Utils.randomPayload;

public class DownloadProcessor {
    private static final int DOWNLOAD_MIN_DATA_SIZE = 20;
    private static final int DOWNLOAD_MAX_DATA_SIZE = 200;

    private final long id;

    public DownloadProcessor(long id) {
        this.id = id;
    }

    public DownloadData getData() {
        double dataSize = getDataSize();
        Network.downloadData(dataSize);
        System.out.print(".");

        long payloadSize = getPayloadSize();
        long payload = randomPayload(payloadSize);
        return new DownloadData(id, payload, payloadSize);
    }

    private double getDataSize() {
        if (id % 200 < 100) {
            return DOWNLOAD_MIN_DATA_SIZE;
        } else {
            return DOWNLOAD_MAX_DATA_SIZE;
        }
    }

    private long getPayloadSize() {
        if (id % 200 < 100) {
            return RANGE;
        } else {
            return 50;
        }
    }
}
