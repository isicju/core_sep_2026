package com.kotojava.lesson3.controller;

import com.kotojava.lesson3.User;
import com.kotojava.lesson3.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getUsers(@RequestParam(value = "position", required = false) Integer position,
                                      @RequestParam(value = "sorted", required = false) String sorting,
                                      @RequestParam(value = "dob", required = false) LocalDate dob,
                                      @RequestParam(value = "olderThan", required = false) LocalDate olderThan,
                                      @RequestParam(value = "dobBefore", required = false) LocalDate dobBefore,
                                      @RequestParam(value = "dobAfter", required = false) LocalDate dobAfter,
                                      @RequestParam(value = "dobYear", required = false) Integer dobYear,
                                      @RequestParam(value = "email3Chars", required = false) String email3Chars
    ) {
        if (email3Chars != null) {
            return ResponseEntity.ok(userService.getUsersBy3EmailChars(email3Chars));
        }
        if (dobYear != null) {
            return ResponseEntity.ok(userService.getUsersByTheYear(dobYear));
        }
        if (dobBefore != null && dobAfter != null) {
            return ResponseEntity.ok(userService.getAllUsersBetweenBirthDates(dobBefore, dobAfter));
        }
        if (olderThan != null) {
            return ResponseEntity.ok(userService.getAllUsersOlderThan(olderThan));
        }

        if (dob != null) {
            return ResponseEntity.ok(userService.getUsersByBirthDate(dob));
        }
        if ("name".equals(sorting)) {
            return ResponseEntity.ok(userService.getUsersSortedByName());
        }
        if (position == null) {
            return ResponseEntity.ok(userService.getAllUsers());
        }
        return ResponseEntity.ok(userService.getUserByIndexWithoutSorting(position));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getAllUsers(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

}
