
package com.kotojava.lesson3.controller;

import com.kotojava.lesson3.User;
import com.kotojava.lesson3.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getUsers(
            @RequestParam(value = "position", required = false) Integer position,
            @RequestParam(value = "sorted", required = false) String sorting,
            @RequestParam(value = "dob", required = false) LocalDate dob,
            @RequestParam(value = "olderThan", required = false) LocalDate olderThan,
            @RequestParam(value = "dobBefore", required = false) LocalDate dobBefore,
            @RequestParam(value = "dobAfter", required = false) LocalDate dobAfter,
            @RequestParam(value = "dobYear", required = false) Integer dobYear,
            @RequestParam(value = "email3Chars", required = false) String email3Chars
    ) {
        log.info("GET /users called with position={}, sorted={}, dob={}, " +
                        "olderThan={}, dobBefore={}, dobAfter={}, dobYear={}, " +
                        "email3CharsProvided={}",
                position, sorting, dob, olderThan, dobBefore, dobAfter,
                dobYear, email3Chars != null);

        if (email3Chars != null) {
            log.info("Selected branch: getUsersBy3EmailChars");
            var result = userService.getUsersBy3EmailChars(email3Chars);
            log.info("getUsersBy3EmailChars completed");
            return ResponseEntity.ok(result);
        }

        if (dobYear != null) {
            log.info("Selected branch: getUsersByTheYear, dobYear={}", dobYear);
            var result = userService.getUsersByTheYear(dobYear);
            log.info("getUsersByTheYear completed");
            return ResponseEntity.ok(result);
        }

        if (dobBefore != null && dobAfter != null) {
            log.info("Selected branch: getAllUsersBetweenBirthDates, " +
                    "dobBefore={}, dobAfter={}", dobBefore, dobAfter);

            var result = userService.getAllUsersBetweenBirthDates(
                    dobBefore, dobAfter);

            log.info("getAllUsersBetweenBirthDates completed");
            return ResponseEntity.ok(result);
        }

        if (olderThan != null) {
            log.info("Selected branch: getAllUsersOlderThan, olderThan={}",
                    olderThan);

            var result = userService.getAllUsersOlderThan(olderThan);
            log.info("getAllUsersOlderThan completed");
            return ResponseEntity.ok(result);
        }

        if (dob != null) {
            log.info("Selected branch: getUsersByBirthDate, dob={}", dob);
            var result = userService.getUsersByBirthDate(dob);
            log.info("getUsersByBirthDate completed");
            return ResponseEntity.ok(result);
        }

        if ("name".equals(sorting)) {
            log.info("Selected branch: getUsersSortedByName");
            var result = userService.getUsersSortedByName();
            log.info("getUsersSortedByName completed");
            return ResponseEntity.ok(result);
        }

        if (position == null) {
            log.info("Selected branch: getAllUsers");
            var result = userService.getAllUsers();
            log.info("getAllUsers completed");
            return ResponseEntity.ok(result);
        }

        log.info("Selected branch: getUserByIndexWithoutSorting, position={}",
                position);

        var result = userService.getUserByIndexWithoutSorting(position);
        log.info("getUserByIndexWithoutSorting completed, position={}",
                position);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getAllUsers(
            @PathVariable("userId") Long userId) {

        log.info("GET /users/{} called", userId);

        User user = userService.getUserById(userId);

        log.info("getUserById completed, userFound={}", user != null);

        return ResponseEntity.ok(user);
    }
}
