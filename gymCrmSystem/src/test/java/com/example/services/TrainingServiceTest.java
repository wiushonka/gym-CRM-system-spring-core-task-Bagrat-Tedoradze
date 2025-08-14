package com.example.services;

import com.example.DAOs.TrainingDAO;
import com.example.entitys.training.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrainingServiceTest {

    private TrainingService service;

    @BeforeEach
    void setUp() {
        TrainingDAO dao = new TrainingDAO();
        service = new TrainingService();
        service.setTrainingDao(dao);

        dao.setTrainingStorage(new com.example.repos.TrainingStorage() {
            @Override
            public java.util.Map<Long, Training> getTrainings() {
                if (super.getTrainings() == null) super.setTrainings(new java.util.HashMap<>());
                return super.getTrainings();
            }
        });
    }

    @Test
    void testAddTraining() {
        Date now = new Date();
        service.addTraining(null, 1L, 1L, "Cardio", "Fitness", now, 60L);

        Training t = service.getTrainingByName("Cardio");
        assertNotNull(t);
        assertEquals("Cardio", t.getTrainingName());
        assertEquals("Fitness", t.getTrainingType());
        assertEquals(60L, t.getTrainingDuration());
    }

    @Test
    void testAddTrainingWithDuplicateName() {
        Date now = new Date();
        service.addTraining(null, 1L, 1L, "Yoga", "Wellness", now, 45L);
        service.addTraining(null, 2L, 2L, "Yoga", "Wellness", now, 50L);

        Training t1 = service.getTrainingByName("Yoga");
        Training t2 = service.getTrainingByName("Yoga_2");

        assertNotNull(t1);
        assertNotNull(t2);
        assertEquals("Yoga", t1.getTrainingName());
        assertEquals("Yoga_2", t2.getTrainingName());
    }

    @Test
    void testGetTrainingById() {
        Date now = new Date();
        service.addTraining(null, 1L, 1L, "Pilates", "Fitness", now, 30L);

        Training t = service.getTrainingById(1L);
        assertNotNull(t);
        assertEquals("Pilates", t.getTrainingName());
    }

    @Test
    void testGetNewerTrainingsThan() throws InterruptedException {
        Date past = new Date(System.currentTimeMillis() - 10000);
        Date now = new Date();

        service.addTraining(null, 1L, 1L, "Boxing", "Sport", past, 60L);
        service.addTraining(null, 2L, 2L, "Swimming", "Sport", now, 50L);

        List<Training> recentTrainings = service.getNewerTrainingsThan(new Date(System.currentTimeMillis() - 5000));
        assertEquals(1, recentTrainings.size());
        assertEquals("Swimming", recentTrainings.get(0).getTrainingName());
    }

    @Test
    void testGetAllTrainings() {
        Date now = new Date();
        service.addTraining(null, 1L, 1L, "Zumba", "Dance", now, 40L);
        service.addTraining(null, 2L, 2L, "Crossfit", "Fitness", now, 55L);

        List<Training> trainings = service.getTrainings();
        assertEquals(2, trainings.size());
    }
}
