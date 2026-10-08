package com.kotojava.lesson3;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Configuration
public class MyUserConfig {

    @Value("${user.url}")
    private String userUrl;

    @Bean
    public ObjectMapper myObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Bean
    public List<User> allUsers(ObjectMapper objectMapper) throws Exception {
        return objectMapper.readValue(
                new ClassPathResource("users.json").getInputStream(),
                new TypeReference<ArrayList<User>>() {}
        );
    }

//    @Bean
//    public List<User> allUsers(ObjectMapper objectMapper) throws Exception {
//        return objectMapper.readValue(
//                new URL(userUrl),
//                new TypeReference<ArrayList<User>>() {
//                }
//        );
//    }

    @Bean
    public Map<Long, User> mapUserById(List<User> allUsers) {
        return allUsers.stream()
                .collect(Collectors.toMap(User::getId, user -> user));
    }

    @Bean
    public List<User> listSortedByName(List<User> allUsers) {
        return allUsers.stream().sorted(Comparator.comparing(User::getName))
                .collect(Collectors.toList());
    }

    @Bean
    public Map<LocalDate, List<User>> mapByBirthDate(List<User> allUsers) {
        return allUsers.stream().collect(groupingBy(User::getBirthDate));
    }

    @Bean
    public TreeMap<LocalDate, List<User>> mapSortedByBirthDate(List<User> allUsers) {
        return allUsers.stream()
                .collect(groupingBy(
                        User::getBirthDate,
                        TreeMap::new,
                        Collectors.toList()
                ));
    }

    @Bean
    public Map<Integer, List<User>> mapUsersByYear(List<User> allUsers) {
        return allUsers.stream().collect(groupingBy(user -> user.getBirthDate().getYear()));
    }

    @Bean
    public Map<String, List<User>> email3CharsIndex(List<User> allUsers) {
//        Map<String, List<User>> cache3Chars = new HashMap<>();
//        for (char first = 'a'; first <= 'z'; first++) {
//            for (char second = 'a'; second <= 'z'; second++) {
//                for (char third = 'a'; third <= 'z'; third++) {
//                    String indexKey = String.valueOf(first) + second + third;
//
//                    for (User tempUser : allUsers) {
//                        if (tempUser.getEmail().contains(indexKey)) {
//                            cache3Chars.computeIfAbsent(indexKey, k -> new ArrayList<>()).add(tempUser);
//                        }
//                    }
//
//                }
//            }
//        }
//        return cache3Chars;
        return Map.of();
    }

}
