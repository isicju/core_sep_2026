package com.kotojava.lesson3.controller;

import com.kotojava.lesson3.User;
import com.kotojava.lesson3.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
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
        log.info("GET /users: position={}, sorted={}, dob={}, olderThan={}, dobBefore={}, dobAfter={}, dobYear={}, email3Chars={}",
                position, sorting, dob, olderThan, dobBefore, dobAfter, dobYear, email3Chars);

        if (email3Chars != null) {
            return ResponseEntity.ok(logCall("getUsersBy3EmailChars(" + email3Chars + ")",
                    () -> userService.getUsersBy3EmailChars(email3Chars)));
        }
        if (dobYear != null) {
            return ResponseEntity.ok(logCall("getUsersByTheYear(" + dobYear + ")",
                    () -> userService.getUsersByTheYear(dobYear)));
        }
        if (dobBefore != null && dobAfter != null) {
            return ResponseEntity.ok(logCall("getAllUsersBetweenBirthDates(" + dobBefore + ", " + dobAfter + ")",
                    () -> userService.getAllUsersBetweenBirthDates(dobBefore, dobAfter)));
        }
        if (olderThan != null) {
            return ResponseEntity.ok(logCall("getAllUsersOlderThan(" + olderThan + ")",
                    () -> userService.getAllUsersOlderThan(olderThan)));
        }

        if (dob != null) {
            return ResponseEntity.ok(logCall("getUsersByBirthDate(" + dob + ")",
                    () -> userService.getUsersByBirthDate(dob)));
        }
        if ("name".equals(sorting)) {
            return ResponseEntity.ok(logCall("getUsersSortedByName()",
                    () -> userService.getUsersSortedByName()));
        }
        if (position == null) {
            return ResponseEntity.ok(logCall("getAllUsers()",
                    () -> userService.getAllUsers().stream().limit(500)));
        }
        return ResponseEntity.ok(logCall("getUserByIndexWithoutSorting(" + position + ")",
                () -> userService.getUserByIndexWithoutSorting(position)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getAllUsers(@PathVariable("userId") Long userId) {
        log.info("GET /users/{}", userId);
        return ResponseEntity.ok(logCall("getUserById(" + userId + ")",
                () -> userService.getUserById(userId)));
    }

    private <T> T logCall(String description, Supplier<T> call) {
        log.debug("-> calling {}", description);
        long start = System.nanoTime();
        try {
            T result = call.get();
            log.debug("<- {} finished in {} ms, result: {}",
                    description, elapsedMs(start), summarize(result));
            return result;
        } catch (RuntimeException e) {
            log.error("<- {} failed after {} ms: {}",
                    description, elapsedMs(start), e.toString(), e);
            throw e;
        }
    }

    private static long elapsedMs(long startNanos) {
        return (System.nanoTime() - startNanos) / 1_000_000;
    }

    private static String summarize(Object result) {
        if (result instanceof Collection<?> c) {
            return "collection of " + c.size() + " item(s)";
        }
        return String.valueOf(result);
    }

}