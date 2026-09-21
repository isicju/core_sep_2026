import org.example.hw.exceptions.CheckedExceptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class CheckedExceptionsTest  {

    @Test
    void throwsFileNotFoundTest() {
        Assertions.assertThrows(
                FileNotFoundException.class,
                () -> new CheckedExceptions().throwsFileNotFoundTest()
        );
    }

}
