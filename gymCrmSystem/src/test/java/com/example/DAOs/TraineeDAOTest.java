package com.example.DAOs;

import com.example.entitys.users.Trainee;
import com.example.repos.TraineeStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TraineeDAOTest {

    private TraineeDAO dao;
    private TraineeStorage storage;

    @BeforeEach
    void setUp() throws Exception {
        storage = new TraineeStorage();
        dao = new TraineeDAO();
        dao.setStorage(storage);

        storage.setTrainees(new HashMap<>(Map.of(
                1L, new Trainee(1L, new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"),
                        "123 Main St", "John", "Doe", "John.Doe", "pass123"),
                2L, new Trainee(2L, new SimpleDateFormat("yyyy-MM-dd").parse("1995-05-10"),
                        "456 Elm St", "Jane", "Smith", "Jane.Smith", "pass456")
        )));
    }

    @Test
    void testAddTrainee() throws Exception {
        Trainee t = new Trainee(null, new SimpleDateFormat("yyyy-MM-dd").parse("1998-07-20"),
                "789 Oak St", "Alice", "Brown", "Alice.Brown", "pass789");
        dao.addTrainee(t);

        assertNotNull(t.getId());
        assertEquals("Alice", storage.getTrainees().get(t.getId()).getFirstName());
    }

    @Test
    void testGetTraineeById() {
        Trainee t = dao.getTraineeById(1L);
        assertNotNull(t);
        assertEquals("John", t.getFirstName());
    }

    @Test
    void testRemoveTraineeById() {
        dao.removeTraineeById(2L);
        assertNull(storage.getTrainees().get(2L));
    }

    @Test
    void testUpdateTrainee() throws Exception {
        Trainee updated = new Trainee(null, new SimpleDateFormat("yyyy-MM-dd").parse("2001-02-02"),
                "999 Maple St", "John", "Doe", "John.Doe", "newpass");
        dao.updateTrainee(1L, updated);

        Trainee t = storage.getTrainees().get(1L);
        assertEquals("999 Maple St", t.getAddr());
        assertEquals("newpass", t.getPassword());
    }

    @Test
    void testGetTraineeByUsername() {
        Trainee t = dao.getTraineeByUsername("Jane.Smith");
        assertNotNull(t);
        assertEquals(2L, t.getId());

        assertNull(dao.getTraineeByUsername("Non.Existent"));
    }

    @Test
    void testGetAllTrainees() {
        assertEquals(2, dao.getAllTrainees().size());
    }
}
