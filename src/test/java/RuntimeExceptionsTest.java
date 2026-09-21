import org.example.hw.exceptions.RuntimeExceptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RuntimeExceptionsTest {
    @Test
    void exceptionTesting() {
        RuntimeExceptions runtimeExceptions = new RuntimeExceptions();
        assertThrows(
                NullPointerException.class,
                () -> runtimeExceptions.throwsNullPointerException(),
                "Had to trown an exception but it didn't"
        );
    }

    @Test
    void throwsArrayIndexOutOfBoundsException() {
        Assertions.assertThrows(
                IndexOutOfBoundsException.class,
                () -> new RuntimeExceptions().throwsArrayIndexOutOfBoundsException()
        );
    }

    @Test
    void throwsNumberFormatException() {
        Assertions.assertThrows(
                NumberFormatException.class,
                () -> new RuntimeExceptions().throwsNumberFormatException()
        );
    }

    @Test
    void throwsIllegalArgumentException() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new RuntimeExceptions().throwsIllegalArgumentException()
        );
    }

    @Test
    void throwsClassCastException() {
        Assertions.assertThrows(
                ClassCastException.class,
                RuntimeExceptions::throwsClassCastException
        );
    }
}
