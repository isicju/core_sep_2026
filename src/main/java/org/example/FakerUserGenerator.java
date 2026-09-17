package org.example;

import net.datafaker.Faker;
import org.example.model.Profession;
import org.example.model.User;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class FakerUserGenerator implements UserGenerator {
    private Faker faker;

    private FakerUserGenerator() {
        this.faker = new Faker();
    }

    public List<User> generateUsers(int userCount) {
        return IntStream.range(0, userCount)
                .mapToObj(i -> generateUser())
                .toList();
    }

    private User generateUser() {
        return User.builder()
                .UUID(faker.internet().uuid())
                .name(faker.name().firstName())
                .age(faker.number().numberBetween(10, 100))
                .salary(faker.number().numberBetween(12000, 200000))
                .profession(faker.options().option(Profession.class))
                .build();
    }

    private static FakerUserGenerator instance = new FakerUserGenerator();

    public static FakerUserGenerator getInstance() {
        return instance;
    }

}
