package com.kotojava.lesson3.service;

import com.kotojava.lesson3.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private List<User> allUsers;
    private List<User> listSortedByName;
    private Map<LocalDate, List<User>> mapByBirthDate;
    private Map<Long, User> mapUserById;
    private TreeMap<LocalDate, List<User>> mapSortedByBirthDate;
    private Map<Integer, List<User>> mapUsersByYear;
    private Map<String, List<User>> email3CharsIndex;

    public List<User> getAllUsers() {
        return allUsers;
    }

    public User getUserById(long userId) {
        return mapUserById.get(userId);
    }

    public User getUserByIndexWithoutSorting(int position) {
        return allUsers.get(position);
    }

    public Collection<User> getUsersSortedByName() {
        return listSortedByName;
    }

    public Collection<User> getUsersByBirthDate(LocalDate birthDate) {
        return mapByBirthDate.get(birthDate);
    }

    public Collection<User> getAllUsersOlderThan(LocalDate birthDate) {
        List<User> users = new ArrayList<>();
        Map<LocalDate, List<User>> olderThanSubMap = mapSortedByBirthDate.tailMap(birthDate);
        for (List<User> userList : olderThanSubMap.values()) {
            users.addAll(userList);
        }

        return users;
    }

    public Collection<User> getAllUsersBetweenBirthDates(LocalDate birthDateFrom, LocalDate birthDateTo) {
        List<User> users = new ArrayList<>();
        Map<LocalDate, List<User>> olderThanSubMap = mapSortedByBirthDate.subMap(birthDateFrom, birthDateTo);
        for (List<User> userList : olderThanSubMap.values()) {
            users.addAll(userList);
        }
        return users;
    }

    public Collection<User> getUsersByTheYear(int year) {
        return mapUsersByYear.get(year);
    }

    public Collection<User> getUsersBy3EmailChars(String threeMatchingChars) {
        return allUsers.stream().filter(u -> u.getEmail().contains(threeMatchingChars)).collect(Collectors.toList());
//        return email3CharsIndex.get(threeMatchingChars);
    }
}
