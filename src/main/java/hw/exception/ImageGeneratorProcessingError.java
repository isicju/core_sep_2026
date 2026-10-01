package hw.exception;

public class ImageGeneratorProcessingError extends RuntimeException{
    public ImageGeneratorProcessingError(String message) {
        super(message);
    }
}
