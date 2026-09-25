package org.example.hw.functional;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Generator {

    public static void main(String[] args) {
        System.out.println("");
    }

    public static Set<Integer> generateNRandomNumber(int number) {
        Random random = new Random();

        return Stream.generate(() -> random.nextInt(Integer.MAX_VALUE))
                .limit(number)
                .collect(Collectors.toSet());
    }

    //eg 3, 6,12,24 etc
    public static Set<Integer> generateDoublingNumbers(int initialNumber, int limit) {
        return Stream.iterate(initialNumber, i -> i * 2)
                .limit(limit).collect(Collectors.toSet());
    }

    public static List<String> generateUUIDs(int uuidLength, int limit) {
        return Stream.generate(() -> UUID.randomUUID().toString().substring(0, uuidLength))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public static Stream<Integer> makeStreamFromArray(Integer[] values) {
        return Stream.of(values);
    }

    public static Stream<LocalDate> generateAnyLocalDates() {
        Random random = new Random();
        return Stream.generate(() -> LocalDate.ofEpochDay(random.nextLong()));
    }

    //return same string N times eg "hello",2 => "hello", "hello"
    public static List<String> streamGeneratesSameStringNTimes(String input, int times) {
        return Stream.generate(() -> input).limit(times).collect(Collectors.toList());
    }

    //generate number from to eg 3,7 => 3,4,5,6,7
    public static List<Integer> generateNumberFromTo(int from, int to) {
        if (to <= from) {
            throw new IllegalArgumentException("to must be greater than from");
        }
        return Stream.iterate(from, i -> i + 1)
                .limit(to - from)
                .toList();
    }
}
