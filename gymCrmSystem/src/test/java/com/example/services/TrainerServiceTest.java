package com.example.services;

import com.example.DAOs.TrainerDAO;
import com.example.entitys.users.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrainerServiceTest {

    private TrainerService service;

    @BeforeEach
    void setUp() {
        TrainerDAO dao = new TrainerDAO();
        service = new TrainerService();
        service.setTrainerDao(dao);

        dao.setTrainerStorage(new com.example.repos.TrainerStorage() {
            @Override
            public java.util.Map<Long, Trainer> getTrainers() {
                if (super.getTrainers() == null) super.setTrainers(new java.util.HashMap<>());
                return super.getTrainers();
            }
        });
    }

    @Test
    void testCreateTrainerProfile() {
        service.createTrainerProfile("John", "Doe");
        Trainer t = service.getTrainerProfile("John.Doe");
        assertNotNull(t);
        assertEquals("John", t.getFirstName());
        assertEquals("Doe", t.getLastName());
        assertNotNull(t.getPassword());
    }

    @Test
    void testCreateTrainerProfileWithDuplicateUsername() {
        service.createTrainerProfile("Jane", "Smith");
        service.createTrainerProfile("Jane", "Smith");

        List<Trainer> trainers = service.getAllTrainers();
        assertEquals(2, trainers.size());
        assertEquals("Jane.Smith", trainers.get(0).getUsername());
        assertEquals("Jane.Smith1", trainers.get(1).getUsername());
    }

    @Test
    void testDeleteTrainerProfile() {
        service.createTrainerProfile("Alice", "Brown");
        assertNotNull(service.getTrainerProfile("Alice.Brown"));

        service.deleteTrainerProfile("Alice.Brown");
        assertNull(service.getTrainerProfile("Alice.Brown"));
    }

    @Test
    void testUpdateTrainerProfile() {
        service.createTrainerProfile("Bob", "Green");
        Trainer t = service.getTrainerProfile("Bob.Green");
        t.setSpec("Yoga");
        service.updateTrainerProfile(t);

        Trainer updated = service.getTrainerProfile("Bob.Green");
        assertEquals("Yoga", updated.getSpec());
    }

    @Test
    void testUpdateTrainerProfileById() {
        service.createTrainerProfile("Charlie", "White");
        Trainer t = service.getTrainerProfile("Charlie.White");
        t.setSpec("Cardio");
        service.updateTrainerProfileById(t.getId(), t);

        Trainer updated = service.getTrainerProfile("Charlie.White");
        assertEquals("Cardio", updated.getSpec());
    }

    @Test
    void testGetAllTrainers() {
        service.createTrainerProfile("Tom", "Blue");
        service.createTrainerProfile("Jerry", "Yellow");

        List<Trainer> trainers = service.getAllTrainers();
        assertEquals(2, trainers.size());
    }
}
