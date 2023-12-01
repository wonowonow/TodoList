package com.sparta.todoapp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class POJOTest {

    private boolean isOne(String str) {
        return str.equals("one");
    }

    @Test
    void assert_연습_Equals() {
        // given
        int num1 = 1;
        int num2 = 2;
        // when
        int answer = num1 + num2;
        // then
        Assertions.assertEquals(3, answer);
    }

    @Test
    void assert_연습_Boolean() {
        // given
        String str = "one!";
        // when
        boolean answer = isOne(str);
        // then
        Assertions.assertFalse(answer);
    }

    @Test
    void assert_연습_Null() {
        // given
        String str = null;
        // when / then
        Assertions.assertNull(str);
    }
}
