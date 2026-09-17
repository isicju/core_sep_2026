package org.example;

import org.example.model.User;

import java.util.List;

public class Faker2UserGenerator implements UserGenerator {
    @Override
    public List<User> generateUsers(int userCount) {
        return List.of();
    }
}
