package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.APPEND;

public class UserStorage {

    private Path analyticsFile;

    public UserStorage(Path analyticsFile) {
        this.analyticsFile = analyticsFile;
        if(!Files.exists(analyticsFile)) {
            try {
                analyticsFile.toFile().createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    boolean fileWasAnalyzed(Path filePath) {
        try {
            return Files.readAllLines(analyticsFile).contains(filePath.getFileName().toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveAsAnalyzed(Path filePath) {
        try {
            Files.writeString(analyticsFile, filePath.getFileName().toString() + "\n", APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
