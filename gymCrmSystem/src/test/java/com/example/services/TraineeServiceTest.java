package com.example.services;

import com.example.DAOs.TraineeDAO;
import com.example.entitys.users.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TraineeServiceTest {

    private TraineeService service;

    @BeforeEach
    void setUp() {
        TraineeDAO dao = new TraineeDAO();
        service = new TraineeService();
        service.setTraineeDao(dao);

        dao.setStorage(new com.example.repos.TraineeStorage() {
            @Override
            public java.util.Map<Long, Trainee> getTrainees() {
                if (super.getTrainees() == null) super.setTrainees(new java.util.HashMap<>());
                return super.getTrainees();
            }
        });
    }

    @Test
    void testCreateTraineeProfile() {
        service.createTraineeProfile("John", "Doe", new Date(), "123 Street");
        Trainee t = service.getTraineeProfile("John.Doe");
        assertNotNull(t);
        assertEquals("John", t.getFirstName());
        assertEquals("Doe", t.getLastName());
        assertEquals("123 Street", t.getAddr());
        assertNotNull(t.getPassword());
    }

    @Test
    void testCreateTraineeProfileWithDuplicateUsername() {
        service.createTraineeProfile("Jane", "Smith", new Date(), "456 Avenue");
        service.createTraineeProfile("Jane", "Smith", new Date(), "789 Avenue");

        List<Trainee> trainees = service.getAllTrainees();
        assertEquals(2, trainees.size());
        assertEquals("Jane.Smith", trainees.get(0).getUsername());
        assertEquals("Jane.Smith1", trainees.get(1).getUsername());
    }

    @Test
    void testDeleteTraineeProfile() {
        service.createTraineeProfile("Alice", "Brown", new Date(), "Addr1");
        assertNotNull(service.getTraineeProfile("Alice.Brown"));

        service.deleteTraineeProfile("Alice.Brown");
        assertNull(service.getTraineeProfile("Alice.Brown"));
    }

    @Test
    void testUpdateTraineeProfile() {
        service.createTraineeProfile("Bob", "Green", new Date(), "Addr2");
        Trainee t = service.getTraineeProfile("Bob.Green");
        t.setAddr("NewAddr");
        service.updateTraineeProfile(t);

        Trainee updated = service.getTraineeProfile("Bob.Green");
        assertEquals("NewAddr", updated.getAddr());
    }

    @Test
    void testUpdateTraineeProfileById() {
        service.createTraineeProfile("Charlie", "White", new Date(), "Addr3");
        Trainee t = service.getTraineeProfile("Charlie.White");
        t.setAddr("UpdatedAddr");
        service.updateTraineeProfileById(t.getId(), t);

        Trainee updated = service.getTraineeProfile("Charlie.White");
        assertEquals("UpdatedAddr", updated.getAddr());
    }

    @Test
    void testGetAllTrainees() {
        service.createTraineeProfile("Tom", "Blue", new Date(), "Addr4");
        service.createTraineeProfile("Jerry", "Yellow", new Date(), "Addr5");

        List<Trainee> trainees = service.getAllTrainees();
        assertEquals(2, trainees.size());
    }
}
