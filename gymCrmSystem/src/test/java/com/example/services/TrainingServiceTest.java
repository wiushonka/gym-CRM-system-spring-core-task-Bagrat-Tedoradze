package com.example.services;

import com.example.DAOs.TrainingDAO;
import com.example.entitys.training.Training;
import com.example.repos.TrainingStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TrainingServiceTest {

    private TrainingService service;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    void setUp() {
        TrainingDAO dao = new TrainingDAO();
        service = new TrainingService();
        service.setTrainingDao(dao);

        TrainingStorage storage = new TrainingStorage();
        storage.setTrainings(new java.util.HashMap<>());

        dao.setTrainingStorage(storage);
    }

    @Test
    void testAddTraining() throws Exception {
        Date now = sdf.parse("2025-08-01");
        Training t = new Training(1L, 1L, "Cardio",
                        "Fitness", now, 60L);
        service.addTraining(t);

        Optional<Training> opt = service.getTrainingByName("cardio");
        assertTrue(opt.isPresent());
        Training saved = opt.get();
        assertEquals("cardio", saved.getTrainingName());
        assertEquals("Fitness", saved.getTrainingType());
        assertEquals(60L, saved.getTrainingDuration());
    }

    @Test
    void testAddTrainingWithDuplicateName() throws Exception {
        Date now = sdf.parse("2025-08-02");

        Training t1 = new Training(1L, 1L, "Yoga",
                        "Wellness", now, 45L);
        Training t2 = new Training(2L, 2L, "Yoga",
                        "Wellness", now, 50L);

        service.addTraining(t1);
        service.addTraining(t2);

        Optional<Training> saved1 = service.getTrainingByName("yoga");
        Optional<Training> saved2 = service.getTrainingByName("yoga_2");
        Optional<Training> saved3 = service.getTrainingByName("yoga_1");

        assertTrue(saved1.isPresent());
        assertFalse(saved2.isPresent());
        assertEquals("yoga", saved1.get().getTrainingName());
        assertTrue(saved3.isPresent());
        assertEquals("yoga_1", saved3.get().getTrainingName());
    }

    @Test
    void testGetTrainingById() throws Exception {
        Date now = sdf.parse("2025-08-03");
        Training t = new Training(1L, 1L, "Pilates",
                        "Fitness", now, 30L);
        service.addTraining(t);

        Optional<Training> opt = service.getTrainingById(t.getId());
        assertTrue(opt.isPresent());
        assertEquals("pilates", opt.get().getTrainingName());
    }

    @Test
    void testGetNewerTrainingsThan() throws Exception {
        Date oldDate = sdf.parse("2025-07-30");
        Date newDate = sdf.parse("2025-08-05");

        Training t1 = new Training(1L, 1L, "Boxing",
                        "Sport", oldDate, 60L);
        Training t2 = new Training(2L, 2L, "Swimming",
                            "Sport", newDate, 50L);

        service.addTraining(t1);
        service.addTraining(t2);

        Date cutoff = sdf.parse("2025-08-01");
        List<Training> recent = service.getNewerTrainingsThan(cutoff);
        assertEquals(1, recent.size());
        assertEquals("swimming", recent.get(0).getTrainingName());
    }

    @Test
    void testGetAllTrainings() throws Exception {
        Training t1 = new Training(1L, 1L, "Zumba",
                        "Dance", sdf.parse("2025-08-01"), 40L);
        Training t2 = new Training(2L, 2L, "Crossfit",
                        "Fitness", sdf.parse("2025-08-01"), 55L);

        service.addTraining(t1);
        service.addTraining(t2);

        List<Training> all = service.getTrainings();
        assertEquals(2, all.size());
    }
}
