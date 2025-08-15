package com.example.services;

import com.example.DAOs.TraineeDAO;
import com.example.entitys.users.Trainee;
import com.example.repos.TraineeStorage;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TraineeServiceTest {

    private TraineeService service;

    @BeforeEach
    void setUp() {
        TraineeDAO dao = new TraineeDAO();

        TraineeStorage storage = new TraineeStorage() {
            @Override
            @PostConstruct
            public void init() {
                setTrainees(new java.util.HashMap<>());
            }
        };
        storage.init();
        dao.setTraineeStorage(storage);

        service = new TraineeService();
        service.setTraineeDao(dao);
    }

    @Test
    void testCreateTraineeProfile() {
        Trainee trainee = new Trainee(new Date(), "123 Street", "John",
                            "Doe", "", "");
        service.createTraineeProfile(trainee);

        Optional<Trainee> tOpt = service.getTraineeProfile(trainee.getUsername());
        assertTrue(tOpt.isPresent());
        Trainee t = tOpt.get();
        assertEquals("john", t.getFirstName());
        assertEquals("doe", t.getLastName());
        assertEquals("123 Street", t.getAddr());
        assertNotNull(t.getPassword());
        assertNotNull(t.getUsername());
    }

    @Test
    void testCreateTraineeProfileWithDuplicateUsername() {
        Trainee t1 = new Trainee(new Date(), "456 Avenue", "Jane",
                        "Smith", "", "");
        Trainee t2 = new Trainee(new Date(), "789 Avenue", "Jane",
                        "Smith", "", "");

        service.createTraineeProfile(t1);
        service.createTraineeProfile(t2);

        List<Trainee> trainees = service.getAllTrainees();
        assertEquals(2, trainees.size());
        assertEquals("jane.smith", trainees.get(0).getUsername());
        assertEquals("jane.smith1", trainees.get(1).getUsername());
    }

    @Test
    void testDeleteTraineeProfile() {
        Trainee t = new Trainee(new Date(), "Addr1", "Alice",
                                "Brown", "", "");
        service.createTraineeProfile(t);
        assertTrue(service.getTraineeProfile(t.getUsername()).isPresent());

        service.deleteTraineeProfile(t.getUsername());
        assertTrue(service.getTraineeProfile(t.getUsername()).isEmpty());
    }

    @Test
    void testUpdateTraineeProfile() {
        Trainee t = new Trainee(new Date(), "Addr2", "Bob",
                        "Green", "", "");
        service.createTraineeProfile(t);

        t.setAddr("NewAddr");
        service.updateTraineeProfile(t);

        Trainee updated = service.getTraineeProfile(t.getUsername()).orElseThrow();
        assertEquals("NewAddr", updated.getAddr());
    }

    @Test
    void testUpdateTraineeProfileById() {
        Trainee t = new Trainee(new Date(), "Addr3", "Charlie",
                        "White", "", "");
        service.createTraineeProfile(t);

        t.setAddr("UpdatedAddr");
        service.updateTraineeProfileById(t.getId(), t);

        Trainee updated = service.getTraineeProfile(t.getUsername()).orElseThrow();
        assertEquals("UpdatedAddr", updated.getAddr());
    }

    @Test
    void testGetAllTrainees() {
        Trainee t1 = new Trainee(new Date(), "Addr4", "Tom",
                        "Blue", "", "");
        Trainee t2 = new Trainee(new Date(), "Addr5", "Jerry",
                        "Yellow", "", "");
        service.createTraineeProfile(t1);
        service.createTraineeProfile(t2);

        List<Trainee> trainees = service.getAllTrainees();
        assertEquals(2, trainees.size());
    }
}
