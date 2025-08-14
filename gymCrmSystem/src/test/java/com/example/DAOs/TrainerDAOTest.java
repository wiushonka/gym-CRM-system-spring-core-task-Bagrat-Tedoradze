package com.example.DAOs;

import com.example.entitys.users.Trainer;
import com.example.repos.TrainerStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TrainerDAOTest {

    private TrainerDAO dao;
    private TrainerStorage storage;

    @BeforeEach
    void setUp() {
        storage = new TrainerStorage();
        dao = new TrainerDAO();
        dao.setTrainerStorage(storage);

        storage.setTrainers(new HashMap<>(Map.of(
                1L, new Trainer(1L, "John", "Doe",
                        "pass123", "John.Doe", "Fitness"),
                2L, new Trainer(2L, "Jane", "Smith",
                        "pass456", "Jane.Smith", "Yoga")
        )));
    }

    @Test
    void testAddTrainer() {
        Trainer t = new Trainer(null, "Alice", "Brown",
                    "pass789", "Alice.Brown", "Cardio");
        dao.addTrainer(t);

        assertNotNull(t.getId());
        assertEquals("Alice", storage.getTrainers().get(t.getId()).getFirstName());
    }

    @Test
    void testGetTrainerById() {
        Trainer t = dao.getTrainerById(1L);
        assertNotNull(t);
        assertEquals("John", t.getFirstName());
    }

    @Test
    void testRemoveTrainerById() {
        dao.removeTrainerById(2L);
        assertNull(storage.getTrainers().get(2L));
    }

    @Test
    void testUpdateTrainer() {
        Trainer updated = new Trainer(null, "John", "Doe",
                    "newpass", "John.Doe", "Strength");
        dao.updateTrainer(1L, updated);

        Trainer t = storage.getTrainers().get(1L);
        assertEquals("Strength", t.getSpec());
        assertEquals("newpass", t.getPassword());
    }

    @Test
    void testGetTrainerByUsername() {
        Trainer t = dao.getTrainerByUsername("Jane.Smith");
        assertNotNull(t);
        assertEquals(2L, t.getId());

        assertNull(dao.getTrainerByUsername("Non.Existent"));
    }

    @Test
    void testGetAllTrainers() {
        assertEquals(2, dao.getAllTrainers().size());
    }
}
