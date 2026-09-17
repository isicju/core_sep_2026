package org.example;

import org.example.model.User;

import java.util.List;

public interface UserGenerator {
    List<User> generateUsers(int userCount);
}
