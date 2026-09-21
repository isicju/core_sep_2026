package org.example.hw.functional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Generator {

    public static void main(String[] args) {
        System.out.println("");
    }

    public static Set<Integer> generateNRandomNumber(int number) {
        return null;
    }

    //eg 3, 6,12,24 etc
    public static Set<Integer> generateDoublingNumbers(int initialNumber, int limit) {
        return null;
    }

    public static List<String> generateUUIDs(int uuidLength, int limit) {
        return null;
    }

    public static Stream<Integer> makeStreamFromArray(Integer[] values) {
        return null;
    }

    public static Stream<LocalDate> generateAnyLocalDates() {
        return null;
    }
    //return same string N times eg "hello",2 => "hello", "hello"
    public static List<String> streamGeneratesSameStringNTimes(String input, int times){
        return null;
    }
    //generate number from to eg 3,7 => 3,4,5,6,7
    public static List<Integer> generateNumberFromTo(int from, int to) {
        return null;
    }
}
