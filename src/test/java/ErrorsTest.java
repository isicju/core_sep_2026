import org.example.hw.exceptions.Errors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ErrorsTest {
    @Test
    void throwsOOMException() {
        Assertions.assertThrows(
                OutOfMemoryError.class,
                () -> new Errors().throwsOOMException()
        );
    }
    @Test
    void throwsStackOverFlowError() {
        Assertions.assertThrows(
                StackOverflowError.class,
                () -> new Errors().throwsStackOverFlowError()
        );
    }
}
