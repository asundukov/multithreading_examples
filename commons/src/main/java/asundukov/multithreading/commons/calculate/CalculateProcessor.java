package asundukov.multithreading.commons.calculate;

import static asundukov.multithreading.commons.Utils.randomPayload;

public class CalculateProcessor {
    private final CalculateData calculateData;
    private long totalAttempts = 0;

    public CalculateProcessor(CalculateData calculateData) {
        this.calculateData = calculateData;
    }

    public void calculate() {
        do {
            totalAttempts++;
        } while (!calculateData.checkPayload(randomPayload(calculateData.getPayloadSize())));
        System.out.print(",");
    }

    public long getTotalAttempts() {
        return totalAttempts;
    }
}
