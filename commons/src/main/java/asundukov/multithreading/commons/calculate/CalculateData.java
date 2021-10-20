package asundukov.multithreading.commons.calculate;

import asundukov.multithreading.commons.download.DownloadData;

public class CalculateData {
    private final long payload;
    private final long id;
    private final long payloadSize;

    private CalculateData(long payload, long id, long payloadSize) {
        this.payload = payload;
        this.id = id;
        this.payloadSize = payloadSize;
    }

    public static CalculateData formDownloadData(DownloadData downloadData) {
        return new CalculateData(
                downloadData.getPayload(),
                downloadData.getId(),
                downloadData.getPayloadSize()
        );
    }

    public boolean checkPayload(long attempt) {
        return this.payload == attempt;
    }

    public long getId() {
        return id;
    }

    public long getPayloadSize() {
        return payloadSize;
    }
}
