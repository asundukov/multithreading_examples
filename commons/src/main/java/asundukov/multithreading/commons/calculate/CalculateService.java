package asundukov.multithreading.commons.calculate;

import asundukov.multithreading.commons.download.DownloadData;

public class CalculateService {
    public CalculateProcessor createProcessor(DownloadData downloadData) {
        CalculateData calculateData = CalculateData.formDownloadData(downloadData);
        return new CalculateProcessor(calculateData);
    }
}
