package com.example.repos;

import com.example.entitys.training.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TrainingStorageTest {

    private TrainingStorage storage;

    @BeforeEach
    void setUp() throws Exception {

        File tempFile = File.createTempFile("trainings", ".txt");
        tempFile.deleteOnExit();

        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        writer.write("1,10,20,Yoga Basics,Yoga,2023-01-01,60\n");
        writer.write("2,11,21,Pilates Advanced,Pilates,2023-02-15,45\n");
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

        Training t2 = trainings.get(2L);
        assertNotNull(t2);
        assertEquals("Pilates Advanced", t2.getTrainingName());
        assertEquals("Pilates", t2.getTrainingType());
    }

    @Test
    void testSetAndGetTrainings() {
        Map<Long,Training> newMap;
        try{
            newMap = Map.of(
                    3L, new Training(3L,12L,22L,"Crossfit","Strength",
                                        new SimpleDateFormat("yyyy-MM-dd").parse("2023-03-01"),90L));
        }catch (ParseException e){
            throw new RuntimeException("Error occurred when trying to get the trainings map");
        }

        storage.setTrainings(newMap);

        Map<Long, Training> trainings = storage.getTrainings();
        assertEquals(1, trainings.size());
        assertTrue(trainings.containsKey(3L));
        assertEquals("Crossfit", trainings.get(3L).getTrainingName());
    }
}
