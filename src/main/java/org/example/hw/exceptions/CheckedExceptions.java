package org.example.hw.exceptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

public class CheckedExceptions {

    public void throwsFileNotFoundTest() throws FileNotFoundException {
        File file = new File("this_file_does_not_exist.txt");
        FileInputStream stream = new FileInputStream(file);
    }

}
