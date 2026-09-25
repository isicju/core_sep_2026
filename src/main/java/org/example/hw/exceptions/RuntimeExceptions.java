package org.example.hw.exceptions;

public class RuntimeExceptions {

    public void throwsNullPointerException() {
        Object o = null;
        o.toString();
    }

    public void throwsArrayIndexOutOfBoundsException() {
        int[] array = new int[10];
        array[20] = 1;
    }

    public void throwsNumberFormatException() {
        Integer myInt = Integer.valueOf("kek");
    }

    public void throwsIllegalArgumentException() {
        Integer myInt = Integer.valueOf("kek");
    }

    public static void throwsClassCastException() {
        Object myInt = 12;
        String myString = (String) myInt;
    }

}
