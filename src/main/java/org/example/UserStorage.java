package org.example;

import org.example.model.User;

import java.util.List;

public interface UserStorage {
    void persistUsers(List<User> users);
}
