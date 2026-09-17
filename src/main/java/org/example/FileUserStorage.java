package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileUserStorage implements UserStorage {

    private static final Logger log = LoggerFactory.getLogger(FileUserStorage.class);
    private final ObjectMapper mapper;
    private final Path storageDirectory;

    public FileUserStorage(String folderName) {
        this.mapper = new ObjectMapper();
        this.storageDirectory = Path.of(folderName);
    }

    @Override
    public void persistUsers(List<User> users) {
        try {
            Files.createDirectories(storageDirectory);
            Path file = storageDirectory.resolve(System.currentTimeMillis() + "_data.txt");
            Files.writeString(file, mapper.writeValueAsString(users));
            log.info("Saved {} users to {}", users.size(), file.toAbsolutePath());
        } catch (Exception e) {
            log.error("Failed to persist users to {}", storageDirectory.toAbsolutePath(), e);
        }
    }
}
