package org.example.hw.collections;


import org.example.hw.collections.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

//На этапе старта программы считайте пользователей  и создайте
//коллекции которые будут использованы для вызовов методов
//выбирайте коллекции так чтобы время на вызов было минимальным.
//каждому методу ставьте комментарий со сложностью O(?)
public class Api {

    private static List<User> userListWithoutSorting;

    public static void main(String[] args) {
        userListWithoutSorting = new ArrayList<>();
        // данные для загрузки лежат тут
        //https://gist.githubusercontent.com/isicju/761203c098852ab6e3a90ab6b9d48241/raw/f1d640622c7cb1b4664ef6f47114cb23843b8f09/users.json
    }

    // O(1)
    public User getUserByIndexWithoutSorting(int position) {
        return userListWithoutSorting.get(position);
    }

    public Collection<User> getUsersSortedByName() {
        return null;
    }

    public Collection<User> getUsersByBirthDate(LocalDate birthDate) {
        return null;
    }

    public Collection<User> getAllUsersOlderThan(LocalDate birthDate) {
        return null;
    }

    public Collection<User> getAllUsersBetweenBirthDates(LocalDate birthDateFrom, LocalDate birthDateTo) {
        return null;
    }

    public User getUserSortedByIndex(int position) {
        return null;
    }


    public Collection<User> getUsersByTheYear(int year) {
        return null;
    }
    //month can be 0,1,2..,11
    public Collection<User> getUsersByMonth(int month) {
        return null;
    }

    //add to collection but replace users with the same date of birth (no date of birth duplicates)
    //replace existent if existed, don't use maps
    public User addUserWithoutMapNoDOBDuplicates(User user) {
        return null;
    }

    //add to collection but replace users with the same email (no email duplicates)
    // don't use maps
    public User addUserWithoutMapNoEmailDuplicates(User user) {
        return null;
    }

    //collection should be as small as possible
    public User deleteUserById(long userId) {
        return null;
    }

    public User getUserById(long userId) {
        return null;
    }
    //must be sorted by userId
    public Collection<User> getSubCollectionId(long userIdFrom, long userIdTo) {
        return null;
    }

    public Collection<String> getAllCitiesWithoutDuplicates() {
        return null;
    }

    public Collection<String> getAllNamesWithoutDuplicates() {
        return null;
    }

    public Collection<User> getUsersByName(String name) {
        return null;
    }

    public Collection<String> getAllAddressesSortedByNaturalReverseOrder() {
        return null;
    }

    // поменяйте сигнатуру, нужно вернуть всех пользователей разбитых по городам
    public Object getAllUsersCityWise() {
        return null;
    }

    // поменяйте сигнатуру, нужно вернуть всех пользователей разбитых с сортировкой
    public Object getAllUsersByCityWiseSorted() {
        return null;
    }

// Создайте трисет который будет возвращать всех пользователей с сортировкой по имени но
// при этом пользователи с одинаковым именем не должны быть искючены. при этом нужно помнить
// что все пользователи имеют уникальный userId и этим можно воспользоваться.
    public TreeSet<User> getSortedUsersByName() {
        return null;
    }

    //Верните всех пользователей по 3 символам (идущим вместе) в емейле например vas должен вернуть vasya@gmail.com, 123vaskek@gmail.com
    //проще говорят должен отработать contains(3chars)
    public Collection<User> getUsersBy3EmailChars(String threeMatchingChars) {
        //   threeMatchingChars should be valid for userEmail.contains(threeMatchingChars)
        return null;
    }

//Сигнатура метода не идеальна, хотелось бы чтобы методы ниже (точнее их сигнатуры)
//   были более общны. В зависимости от содержания метода поменяйте сигнатуры чтобы они подходили не только под
// List интерфейс

    private void printAllUserNames(List<User> usersToBePrinted) {
        for (User user : usersToBePrinted) {
            System.out.println(user.getName());
        }
    }

    private boolean existentUser(List<User> users, User toBeExistent) {
        return users.contains(toBeExistent);
    }

}
