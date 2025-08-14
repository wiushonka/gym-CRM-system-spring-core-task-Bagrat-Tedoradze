package com.example.entitys.users;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TraineeEntityTest {
    @Test
    void testTraineeConstructorAndGetters() {
        Long id = 1L;
        Date birthDate = new Date(631152000000L); // 1990-01-01
        String addr = "123 Main St";
        String firstName = "Alice";
        String lastName = "Johnson";
        String username = "Alice.Johnson";
        String password = "password123";

        Trainee trainee = new Trainee(id, birthDate, addr, firstName, lastName, username, password);

        assertEquals(id, trainee.getId());
        assertEquals(addr, trainee.getAddr());
        assertEquals(firstName, trainee.getFirstName());
        assertEquals(lastName, trainee.getLastName());
        assertEquals(username, trainee.getUsername());
        assertEquals(password, trainee.getPassword());
        assertEquals(birthDate, trainee.getBirthDate());
        assertTrue(trainee.isActive());
    }

    @Test
    void testTraineeSetters() {
        Date birthDate = new Date(631152000000L); // 1990-01-01
        Trainee trainee = new Trainee(1L, birthDate, "123 Main St", "Alice", "Johnson", "Alice.Johnson", "pass");

        trainee.setId(2L);
        trainee.setAddr("456 Oak Ave");
        trainee.setFirstName("Bob");
        trainee.setLastName("Smith");
        trainee.setUsername("Bob.Smith");
        trainee.setPassword("newpass");
        trainee.setActive(false);

        assertEquals(2L, trainee.getId());
        assertEquals("456 Oak Ave", trainee.getAddr());
        assertEquals("Bob", trainee.getFirstName());
        assertEquals("Smith", trainee.getLastName());
        assertEquals("Bob.Smith", trainee.getUsername());
        assertEquals("newpass", trainee.getPassword());
        assertFalse(trainee.isActive());
    }

    @Test
    void testBirthDateImmutability() {
        Date birthDate = new Date(631152000000L); // 1990-01-01
        Trainee trainee = new Trainee(1L, birthDate, "123 Main St", "Alice", "Johnson", "Alice.Johnson", "pass");

        birthDate.setTime(0L);
        assertNotEquals(0L, trainee.getBirthDate().getTime());

        Date returnedDate = trainee.getBirthDate();
        returnedDate.setTime(0L);

        assertNotEquals(0L, trainee.getBirthDate().getTime());
    }
}