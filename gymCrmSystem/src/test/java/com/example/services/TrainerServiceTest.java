package com.example.services;

import com.example.DAOs.TrainerDAO;
import com.example.entitys.users.Trainer;
import com.example.repos.TrainerStorage;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TrainerServiceTest {

    private TrainerService service;

    @BeforeEach
    void setUp() {
        TrainerDAO dao = new TrainerDAO();

        TrainerStorage storage = new TrainerStorage() {
            @Override
            @PostConstruct
            public void init() {
                setTrainers(new java.util.HashMap<>());
            }
        };
        storage.init();
        dao.setTrainerStorage(storage);

        service = new TrainerService();
        service.setTrainerDao(dao);
    }

    @Test
    void testCreateTrainerProfile() {
        Trainer trainer = new Trainer("John", "Doe", null, null, "");
        service.createTrainerProfile(trainer);

        Optional<Trainer> t = service.getTrainerProfile("john.doe");
        assertTrue(t.isPresent());
        assertEquals("john", t.get().getFirstName());
        assertEquals("doe", t.get().getLastName());
        assertEquals("john.doe", t.get().getUsername());
        assertNotNull(t.get().getPassword());
    }

    @Test
    void testCreateTrainerProfileWithDuplicateUsername() {
        Trainer t1 = new Trainer("Jane", "Smith", null, null, "");
        Trainer t2 = new Trainer("Jane", "Smith", null, null, "");

        service.createTrainerProfile(t1);
        service.createTrainerProfile(t2);

        List<Trainer> trainers = service.getAllTrainers();
        assertEquals(2, trainers.size());

        assertEquals("jane.smith", trainers.get(0).getUsername());
        assertEquals("jane.smith1", trainers.get(1).getUsername());
    }

    @Test
    void testDeleteTrainerProfile() {
        Trainer trainer = new Trainer("Alice", "Brown", null, null, "");
        service.createTrainerProfile(trainer);

        assertTrue(service.getTrainerProfile("alice.brown").isPresent());

        service.deleteTrainerProfile("alice.brown");
        assertTrue(service.getTrainerProfile("alice.brown").isEmpty());
    }

    @Test
    void testUpdateTrainerProfile() {
        Trainer trainer = new Trainer("Bob", "Green", null, null, "");
        service.createTrainerProfile(trainer);

        Optional<Trainer> t = service.getTrainerProfile("bob.green");
        assertTrue(t.isPresent());

        t.get().setSpec("Yoga");
        service.updateTrainerProfile(t.get());

        Optional<Trainer> updated = service.getTrainerProfile("bob.green");
        assertTrue(updated.isPresent());
        assertEquals("Yoga", updated.get().getSpec());
    }

    @Test
    void testUpdateTrainerProfileById() {
        Trainer trainer = new Trainer("Charlie", "White", null, null, "");
        service.createTrainerProfile(trainer);

        Optional<Trainer> t = service.getTrainerProfile("charlie.white");
        assertTrue(t.isPresent());

        t.get().setSpec("Cardio");
        service.updateTrainerProfileById(t.get().getId(), t.get());

        Optional<Trainer> updated = service.getTrainerProfile("charlie.white");
        assertTrue(updated.isPresent());
        assertEquals("Cardio", updated.get().getSpec());
    }

    @Test
    void testGetAllTrainers() {
        service.createTrainerProfile(new Trainer("Tom", "Blue", null, null, ""));
        service.createTrainerProfile(new Trainer("Jerry", "Yellow", null, null, ""));

        List<Trainer> trainers = service.getAllTrainers();
        assertEquals(2, trainers.size());

        assertEquals("tom.blue", trainers.get(0).getUsername());
        assertEquals("jerry.yellow", trainers.get(1).getUsername());
    }
}
