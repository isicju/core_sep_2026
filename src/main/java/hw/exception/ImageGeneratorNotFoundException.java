package hw.exception;

public class ImageGeneratorNotFoundException extends RuntimeException {
    public ImageGeneratorNotFoundException(String message) {
        super(message);
    }
}
