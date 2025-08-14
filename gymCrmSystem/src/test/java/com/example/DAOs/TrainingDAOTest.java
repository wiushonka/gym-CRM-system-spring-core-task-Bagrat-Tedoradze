package com.example.DAOs;

import com.example.entitys.training.Training;
import com.example.repos.TrainingStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrainingDAOTest {

    private TrainingDAO dao;
    private TrainingStorage storage;

    @BeforeEach
    void setUp() {
        storage = new TrainingStorage();
        dao = new TrainingDAO();
        dao.setTrainingStorage(storage);

        Training t1 = new Training(1L, 1L, 1L, "Morning Workout",
                        "Fitness", new Date(2025-1900, 7, 1), 60L);
        Training t2 = new Training(2L, 2L, 2L, "Yoga Session",
                        "Yoga", new Date(2025-1900, 7, 5), 45L);
        storage.setTrainings(new HashMap<>(Map.of(
                1L, t1,
                2L, t2
        )));
    }

    @Test
    void testCreateTraining() {
        Training t = new Training(null, 3L, 3L, "Evening Run",
                        "Cardio", new Date(2025-1900, 7, 10), 30L);
        dao.createTraining(t);

        assertNotNull(t.getId());
        assertEquals("Evening Run", storage.getTrainings().get(t.getId()).getTrainingName());
    }

    @Test
    void testGetTrainingById() {
        Training t = dao.getTrainingById(1L);
        assertNotNull(t);
        assertEquals("Morning Workout", t.getTrainingName());
    }

    @Test
    void testGetTrainingByName() {
        Training t = dao.getTrainingByName("Yoga Session");
        assertNotNull(t);
        assertEquals(2L, t.getId());

        assertNull(dao.getTrainingByName("Nonexistent"));
    }

    @Test
    void testGetTrainingsByType() {
        List<Training> fitnessTrainings = dao.getTrainingsByType("Fitness");
        assertEquals(1, fitnessTrainings.size());
        assertEquals("Morning Workout", fitnessTrainings.get(0).getTrainingName());

        assertThrows(NullPointerException.class, () -> dao.getTrainingsByType(null));
    }

    @Test
    void testGetTrainingsAfterDate() {
        Date date = new Date(2025-1900, 7, 2);
        List<Training> result = dao.getTrainingsAfterDate(date);
        assertEquals(1, result.size());
        assertEquals("Yoga Session", result.get(0).getTrainingName());
    }

    @Test
    void testGetAllTrainings() {
        List<Training> all = dao.getAllTrainings();
        assertEquals(2, all.size());
    }
}
