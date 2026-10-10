package com.kotojava.lesson3.controller;

import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.function.Supplier;
@Slf4j
public class Utils {
    public static <T> T logCall(String description, Supplier<T> call) {
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

    public static long elapsedMs(long startNanos) {
        return (System.nanoTime() - startNanos) / 1_000_000;
    }

    public static String summarize(Object result) {
        if (result instanceof Collection<?> c) {
            return "collection of " + c.size() + " item(s)";
        }
        return String.valueOf(result);
    }
}
