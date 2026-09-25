package org.example.hw.collections;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.hw.collections.model.User;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

//На этапе старта программы считайте пользователей  и создайте
//коллекции которые будут использованы для вызовов методов
//выбирайте коллекции так чтобы время на вызов было минимальным.
//каждому методу ставьте комментарий со сложностью O(?)
public class Api {

    private static List<User> userListWithoutSorting;
    private static List<User> userListSortedByIndex;
    private static Set<User> userSetSortedByName;
    private static List<User> listSortedByName;
    private static Map<LocalDate, List<User>> mapByBirthDate;
    private static Map<Integer, List<User>> mapUsersByYear;
    private static Map<Integer, List<User>> mapUserByMonth;
    private static Map<Long, User> mapUserById;
    private static Map<String, List<User>> mapUserByName;

    private static Map<String, List<User>> cache3Chars;


    private static TreeSet<User> treeSetDobNoDuplicates;
    private static TreeSet<User> treeSetByNamesNoDuplicates;

    private static TreeMap<Long, User> treeSetFromToById;
    private static TreeSet<User> treeSetEmailNoDuplicates;
    private static TreeMap<Long, User> treeMapForRemove;
    private static List<String> allCities;
    private static List<String> allNames;
    private static List<String> listAllAddressesReverseOrder;
    private static TreeMap<LocalDate, List<User>> mapSortedByBirthDate;
    private static Map<String, List<User>> cityWiseUsers;
    private static Map<String, List<User>> cityWiseUsersSorted;

    public static void main(String[] args) throws Exception {
        userListWithoutSorting = new ArrayList<>();
        // данные для загрузки лежат тут
        String url = "https://gist.githubusercontent.com/isicju/f40e58017d64533a2660cf04285f6cb9/raw/5fde264d0758aab1df4afe92f33bd83ad3d7b9ad/gistfile1.txt";
//        String url = "https://gist.githubusercontent.com/isicju/761203c098852ab6e3a90ab6b9d48241/raw/f1d640622c7cb1b4664ef6f47114cb23843b8f09/users.json";

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        userListWithoutSorting = mapper.readValue(
                new URL(url),
                new TypeReference<ArrayList<User>>() {
                }
        );

        System.out.println("");

        userSetSortedByName = new TreeSet<>(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if (o1.getName().compareTo(o2.getName()) == 0) {
                    Long o1Id = o1.getId();
                    Long o2Id = o2.getId();
                    return o1Id.compareTo(o2Id);
                }
                return o1.getName().compareTo(o2.getName());
            }
        });

        treeSetDobNoDuplicates = new TreeSet<>(Comparator.comparing(User::getId));

        treeSetByNamesNoDuplicates = new TreeSet<>(Comparator.comparing(User::getName)
                .thenComparing(User::getId));


        treeSetEmailNoDuplicates = new TreeSet<>(Comparator.comparing(User::getEmail));
        treeMapForRemove = new TreeMap<>();

        userSetSortedByName.addAll(userListWithoutSorting);

        listSortedByName = new ArrayList<>(userListWithoutSorting);
        listSortedByName.sort(Comparator.comparing(User::getName));

        mapByBirthDate = new HashMap<>();
        mapUserById = new HashMap<>();
        treeSetFromToById = new TreeMap<>();
        allCities = new ArrayList<>();
        allNames = new ArrayList<>();
        List<String> listAllAddressesReverseOrderTemp = new ArrayList<>();
        mapUserByName = new HashMap<>();
        cityWiseUsers = new HashMap<>();
        cityWiseUsersSorted = new TreeMap<>();

        for (User user : userListWithoutSorting) {

            cityWiseUsers.computeIfAbsent(user.getCity(), k -> new ArrayList<>()).add(user);
            cityWiseUsersSorted.computeIfAbsent(user.getCity(), k -> new ArrayList<>()).add(user);

            if (!listAllAddressesReverseOrderTemp.contains(user.getAddress())) {
                listAllAddressesReverseOrderTemp.add(user.getAddress());
            }

            mapUserByName.computeIfAbsent(user.getName(), k -> new ArrayList<>())
                    .add(user);

            if (!allCities.contains(user.getCity())) {
                allCities.add(user.getCity());
            }

            if (!allNames.contains(user.getName())) {
                allNames.add(user.getName());
            }

            treeSetFromToById.put(user.getId(), user);

            mapUserById.put(user.getId(), user);


            mapByBirthDate.computeIfAbsent(user.getBirthDate(),
                    k -> new ArrayList<>()).add(user);



        }

        cache3Chars = new HashMap<>();
        for (char first = 'a'; first <= 'z'; first++) {
            for (char second = 'a'; second <= 'z'; second++) {
                for (char third = 'a'; second <= 'z'; second++) {
                    String indexKey = String.valueOf(first) + second + third;

                    for (User tempUser: userListWithoutSorting) {
                        if(tempUser.getEmail().contains(indexKey)) {
                            cache3Chars.computeIfAbsent(indexKey, k -> new ArrayList<>()).add(tempUser);
                        }
                    }

                }
            }
        }


        listAllAddressesReverseOrder = new ArrayList<>(listAllAddressesReverseOrderTemp);
        listAllAddressesReverseOrder.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });

        System.out.println("");

        mapUsersByYear = new HashMap<>();
        for (User user : userListWithoutSorting) {
            mapUsersByYear.computeIfAbsent(user.getBirthDate().getYear(),
                    k -> new ArrayList<>()).add(user);
        }

        mapUserByMonth = new HashMap<>();
        for (User user : userListWithoutSorting) {
            mapUserByMonth.computeIfAbsent(user.getBirthDate().getMonthValue(),
                    k -> new ArrayList<>()).add(user);
        }


        mapSortedByBirthDate = new TreeMap<>();

        for (User user : userListWithoutSorting) {
            mapSortedByBirthDate.computeIfAbsent(user.getBirthDate(),
                    k -> new ArrayList<>()).add(user);
        }

        for (User user : userListWithoutSorting) {
            treeMapForRemove.put(user.getId(), user);
        }

        userListSortedByIndex = new ArrayList<>();
        userListSortedByIndex.sort(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

    }

    // O(1)
    public User getUserByIndexWithoutSorting(int position) {
        return userListWithoutSorting.get(position);
    }

    // O(N)
    public Collection<User> getUsersSortedByName() {
        return listSortedByName;
    }

    // O(1)
    public Collection<User> getUsersByBirthDate(LocalDate birthDate) {
        return mapByBirthDate.get(birthDate);
    }

    // O(LOG)
    public Collection<User> getAllUsersOlderThan(LocalDate birthDate) {
        List<User> users = new ArrayList<>();
        Map<LocalDate, List<User>> olderThanSubMap = mapSortedByBirthDate.tailMap(birthDate);
        for (List<User> userList : olderThanSubMap.values()) {
            users.addAll(userList);
        }

        return users;
    }

    // O(LOG)
    public Collection<User> getAllUsersBetweenBirthDates(LocalDate birthDateFrom, LocalDate birthDateTo) {
        List<User> users = new ArrayList<>();
        Map<LocalDate, List<User>> olderThanSubMap = mapSortedByBirthDate.subMap(birthDateFrom, birthDateTo);
        for (List<User> userList : olderThanSubMap.values()) {
            users.addAll(userList);
        }
        return users;
    }

    // O(1)
    public User getUserSortedByIndex(int position) {
        return userListSortedByIndex.get(position);
    }

    // O(1)
    public Collection<User> getUsersByTheYear(int year) {
        return mapUsersByYear.get(year);
    }

    // O(1)
    //month can be 0,1,2..,11
    public Collection<User> getUsersByMonth(int month) {
        return mapUserByMonth.get(month);
    }

    //add to collection but replace users with the same date of birth (no date of birth duplicates)
    //replace existent if existed, don't use maps
    //O(LOG)
    public User addUserWithoutMapNoDOBDuplicates(User user) {
        treeSetDobNoDuplicates.add(user);
        return user;
    }

    //add to collection but replace users with the same email (no email duplicates)
    // don't use maps
    //O(LOG)
    public User addUserWithoutMapNoEmailDuplicates(User user) {
        treeSetEmailNoDuplicates.add(user);
        return user;
    }

    //collection should be as small as possible
    //O(LOG)
    public User deleteUserById(long userId) {
        return treeMapForRemove.remove(userId);
    }

    //O(1)
    public User getUserById(long userId) {
        return mapUserById.get(userId);
    }

    //must be sorted by userId
    //O(LOG)
    public Collection<User> getSubCollectionId(long userIdFrom, long userIdTo) {
        return treeSetFromToById.subMap(userIdFrom, userIdTo).values();
    }

    //O(1)
    public Collection<String> getAllCitiesWithoutDuplicates() {
        return allCities;
    }

    //O(1)
    public Collection<String> getAllNamesWithoutDuplicates() {
        return allNames;
    }

    //O(1)
    public Collection<User> getUsersByName(String name) {
        return mapUserByName.get(name);
    }

    //O(1)
    public Collection<String> getAllAddressesSortedByNaturalReverseOrder() {
        return listAllAddressesReverseOrder;
    }

    //O(1)
    // поменяйте сигнатуру, нужно вернуть всех пользователей разбитых по городам
    public Map<String, List<User>> getAllUsersCityWise() {
        return cityWiseUsers;
    }

    //O(1)
    // поменяйте сигнатуру, нужно вернуть всех пользователей разбитых с сортировкой
    public Map<String, List<User>> getAllUsersByCityWiseSorted() {
        return cityWiseUsersSorted;
    }

    //O(1)
    // Создайте трисет который будет возвращать всех пользователей с сортировкой по имени но
    // при этом пользователи с одинаковым именем не должны быть искючены. при этом нужно помнить
    // что все пользователи имеют уникальный userId и этим можно воспользоваться.
    public TreeSet<User> getSortedUsersByName() {
        return treeSetByNamesNoDuplicates;
    }

    //O(1)
    //Верните всех пользователей по 3 символам (идущим вместе) в емейле например vas должен вернуть vasya@gmail.com, 123vaskek@gmail.com
    //проще говорят должен отработать contains(3chars)
    public Collection<User> getUsersBy3EmailChars(String threeMatchingChars) {
        //   threeMatchingChars should be valid for userEmail.contains(threeMatchingChars)
        return cache3Chars.get(threeMatchingChars);
//        return null;
    }

//Сигнатура метода не идеальна, хотелось бы чтобы методы ниже (точнее их сигнатуры)
//   были более общны. В зависимости от содержания метода поменяйте сигнатуры чтобы они подходили не только под
// List интерфейс

    private void printAllUserNames(Iterable<User> usersToBePrinted) {
        for (User user : usersToBePrinted) {
            System.out.println(user.getName());
        }
    }

    private boolean existentUser(Collection<User> users, User toBeExistent) {
        return users.contains(toBeExistent);
    }

}
