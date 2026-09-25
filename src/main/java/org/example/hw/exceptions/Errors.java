package org.example.hw.exceptions;

public class Errors {

    public void throwsOOMException() {
        int[][] array = new int[Integer.MAX_VALUE][Integer.MAX_VALUE];
    }

    public void throwsStackOverFlowError() {
        throwsStackOverFlowError();
    }
}
