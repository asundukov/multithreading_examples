package asundukov.multithreading.commons.download;

public class DownloadData {
    private final long id;
    private final long payload;
    private final long payloadSize;

    public DownloadData(long id, long payload, long payloadSize) {
        this.payload = payload;
        this.id = id;
        this.payloadSize = payloadSize;
    }

    public long getId() {
        return id;
    }

    public long getPayload() {
        return payload;
    }

    public long getPayloadSize() {
        return payloadSize;
    }
}
