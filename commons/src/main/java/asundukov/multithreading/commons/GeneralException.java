package asundukov.multithreading.commons;

public class GeneralException extends RuntimeException {
    public GeneralException(Exception e) {
        super(e);
    }

    public GeneralException(String message) {
        super(message);
    }
}
