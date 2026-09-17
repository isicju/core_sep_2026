package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class UserReader {
    private ObjectMapper objectMapper;

    public UserReader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<User> readUserFromFile(Path path) {
        try {
            String fileContent = Files.readString(path);
            return objectMapper.readValue(fileContent, new TypeReference<List<User>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
