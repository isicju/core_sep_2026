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
    private ObjectMapper mapper;
    private String folderName;

    public FileUserStorage(String folderName) {
        this.mapper = new ObjectMapper();
        this.folderName = folderName;
    }

    @Override
    public void persistUsers(List<User> users) {
        try {
            File file = new File(folderName + "/" + System.currentTimeMillis() + "_data.txt");
            file.createNewFile();
            Files.writeString(Path.of(file.getAbsolutePath()), mapper.writeValueAsString(users));
            log.info("Generated {} users and saved to {}", users, file.getAbsolutePath());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
