package com.example.utili;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserUtiliTest {
    @Test
    void testGenerateUsernameWithoutSerial() {
        String firstName = "John";
        String lastName = "Doe";
        String username = UserUtili.generateUsername(firstName, lastName, null);

        assertEquals("John.Doe", username);
    }

    @Test
    void testGeneratePasswordLength() {
        String password = UserUtili.generatePassword();

        assertNotNull(password);
        assertEquals(10, password.length());
    }

    @Test
    void testGeneratePasswordCharacters() {
        String password = UserUtili.generatePassword();
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (char c : password.toCharArray()) {
            assertTrue(allowedChars.indexOf(c) >= 0, "Password contains invalid character: " + c);
        }
    }

    @Test
    void testGeneratePasswordRandomness() {
        String password1 = UserUtili.generatePassword();
        String password2 = UserUtili.generatePassword();

        assertNotEquals(password1, password2, "Two generated passwords should not be equal");
    }
}