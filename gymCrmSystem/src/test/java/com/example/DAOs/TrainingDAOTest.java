package com.example.DAOs;

import com.example.entitys.training.Training;
import com.example.repos.TrainingStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TrainingDAOTest {

    private TrainingDAO dao;
    private TrainingStorage storage;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    void setUp() throws Exception {
        storage = new TrainingStorage();
        dao = new TrainingDAO();
        dao.setTrainingStorage(storage);

        Training t1 = new Training(1L, 1L, "Morning Workout",
                        "Fitness", sdf.parse("2025-08-01"), 60L);
        Training t2 = new Training(2L, 2L, "Yoga Session", "Yoga",
                                    sdf.parse("2025-08-05"), 45L);
        storage.setTrainings(new HashMap<>(Map.of(
                1L, t1,
                2L, t2
        )));
    }

    @Test
    void testCreateTraining() throws Exception {
        Training t = new Training(3L, 3L, "Evening Run",
                        "Cardio", sdf.parse("2025-08-10"), 30L);
        dao.createTraining(t);

        assertNotNull(t.getId());
        assertEquals("Evening Run", storage.getTrainings().get(t.getId()).getTrainingName());
    }

    @Test
    void testGetTrainingById() {
        Optional<Training> opt = dao.getTrainingById(1L);
        assertTrue(opt.isPresent());
        assertEquals("Morning Workout", opt.get().getTrainingName());

        assertTrue(dao.getTrainingById(99L).isEmpty());
    }

    @Test
    void testGetTrainingByName() {
        Optional<Training> opt = dao.getTrainingByName("Yoga Session");
        assertTrue(opt.isPresent());
        assertNull(opt.get().getId());

        assertTrue(dao.getTrainingByName("Nonexistent").isEmpty());
    }

    @Test
    void testGetTrainingsByType() {
        List<Training> fitnessTrainings = dao.getTrainingsByType("Fitness");
        assertEquals(1, fitnessTrainings.size());
        assertEquals("Morning Workout", fitnessTrainings.getFirst().getTrainingName());

        List<Training> yogaTrainings = dao.getTrainingsByType("Yoga");
        assertEquals(1, yogaTrainings.size());
        assertEquals("Yoga Session", yogaTrainings.getFirst().getTrainingName());

        List<Training> nullList = dao.getTrainingsByType(null);
        assertNotNull(nullList);
        assertTrue(nullList.isEmpty());
    }

    @Test
    void testGetTrainingsAfterDate() throws Exception {
        Date cutoff = sdf.parse("2025-08-02");
        List<Training> result = dao.getTrainingsAfterDate(cutoff);
        assertEquals(1, result.size());
        assertEquals("Yoga Session", result.getFirst().getTrainingName());
    }

    @Test
    void testGetAllTrainings() {
        List<Training> all = dao.getAllTrainings();
        assertEquals(2, all.size());
    }
}
