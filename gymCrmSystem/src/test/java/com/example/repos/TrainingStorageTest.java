package com.example.repos;

import com.example.entitys.training.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TrainingStorageTest {

    private TrainingStorage storage;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    void setUp() throws Exception {
        File tempFile = File.createTempFile("trainings", ".txt");
        tempFile.deleteOnExit();

        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        writer.write("10,20,Yoga Basics,Yoga,2023-01-01,60\n");
        writer.write("11,21,Pilates Advanced,Pilates,2023-02-15,45\n");
        writer.close();

        storage = new TrainingStorage();
        storage.initDataPath = tempFile.getAbsolutePath();
        storage.init();
    }

    @Test
    void testInitLoadsTrainings() {
        Map<Long, Training> trainings = storage.getTrainings();
        assertEquals(2, trainings.size());

        Training t1 = trainings.get(1L);
        assertNotNull(t1);
        assertEquals("Yoga Basics", t1.getTrainingName());
        assertEquals("Yoga", t1.getTrainingType());
        assertEquals(10L, t1.getTrainerId());
        assertEquals(20L, t1.getTraineeId());

        Training t2 = trainings.get(2L);
        assertNotNull(t2);
        assertEquals("Pilates Advanced", t2.getTrainingName());
        assertEquals("Pilates", t2.getTrainingType());
        assertEquals(11L, t2.getTrainerId());
        assertEquals(21L, t2.getTraineeId());
    }

    @Test
    void testSetAndGetTrainings() throws Exception {
        Map<Long, Training> newMap = Map.of(3L, new Training(12L, 22L, "Crossfit",
                                            "Strength", sdf.parse("2023-03-01"), 90L));

        storage.setTrainings(newMap);

        Map<Long, Training> trainings = storage.getTrainings();
        assertEquals(1, trainings.size());
        Training t = trainings.get(3L);
        assertEquals("Crossfit", t.getTrainingName());
        assertEquals("Strength", t.getTrainingType());
        assertEquals(12L, t.getTrainerId());
        assertEquals(22L, t.getTraineeId());
    }

    @Test
    void testSaveAssignsIdAndStores() throws Exception {
        Training t = new Training(13L, 23L, "Boxing", "Combat", sdf.parse("2023-04-01"), 75L);
        Long id = storage.save(t);

        assertEquals(3L, id);
        assertEquals(t, storage.getTrainings().get(id));
    }

    @Test
    void testFindById() {
        Optional<Training> t1 = storage.findById(1L);
        assertTrue(t1.isPresent());
        assertEquals("Yoga Basics", t1.get().getTrainingName());

        Optional<Training> missing = storage.findById(99L);
        assertTrue(missing.isEmpty());
    }

    @Test
    void testGetTrainingByName() {
        Optional<Training> t = storage.getTrainingByName("Pilates Advanced");
        assertTrue(t.isPresent());
        assertEquals(21L, t.get().getTraineeId());

        Optional<Training> missing = storage.getTrainingByName("Nonexistent");
        assertTrue(missing.isEmpty());
    }

    @Test
    void testFindByType() {
        List<Training> yogaList = storage.findByType("Yoga");
        assertEquals(1, yogaList.size());
        assertEquals("Yoga Basics", yogaList.getFirst().getTrainingName());

        List<Training> strengthList = storage.findByType("Strength");
        assertTrue(strengthList.isEmpty());
    }

    @Test
    void testFindTrainingsAfterCutoff() throws Exception {
        Date cutoff = sdf.parse("2023-01-31");
        List<Training> list = storage.findTrainingsAfterCutoff(cutoff);
        assertEquals(1, list.size());
        assertEquals("Pilates Advanced", list.getFirst().getTrainingName());
    }

    @Test
    void testFindAll() {
        List<Training> all = storage.findAll();
        assertEquals(2, all.size());
    }
}
