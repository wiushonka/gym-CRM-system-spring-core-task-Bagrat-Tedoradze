package com.example.repos;

import com.example.entitys.users.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TrainerStorageTest {

    private TrainerStorage storage;

    @BeforeEach
    void setUp() throws Exception {
        File tempFile = File.createTempFile("trainers", ".txt");
        tempFile.deleteOnExit();

        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        writer.write("1,John,Doe,pass123,John.Doe,Yoga\n");
        writer.write("2,Jane,Smith,pass456,Jane.Smith,Pilates\n");
        writer.close();

        storage = new TrainerStorage();
        storage.initDataPath = tempFile.getAbsolutePath();
        storage.init();
    }

    @Test
    void testInitLoadsTrainers() {
        Map<Long, Trainer> trainers = storage.getTrainers();

        assertEquals(2, trainers.size());

        Trainer t1 = trainers.get(1L);
        assertNotNull(t1);
        assertEquals("John", t1.getFirstName());
        assertEquals("Doe", t1.getLastName());
        assertEquals("pass123", t1.getPassword());
        assertEquals("John.Doe", t1.getUsername());
        assertEquals("Yoga", t1.getSpec());

        Trainer t2 = trainers.get(2L);
        assertNotNull(t2);
        assertEquals("Jane", t2.getFirstName());
        assertEquals("Smith", t2.getLastName());
        assertEquals("pass456", t2.getPassword());
        assertEquals("Jane.Smith", t2.getUsername());
        assertEquals("Pilates", t2.getSpec());
    }

    @Test
    void testSetAndGetTrainers() {
        Map<Long, Trainer> newMap = Map.of(
                3L, new Trainer(3L, "Alice", "Brown", "pass789",
                            "Alice.Brown", "Strength"));

        storage.setTrainers(newMap);

        Map<Long, Trainer> trainers = storage.getTrainers();
        assertEquals(1, trainers.size());
        assertTrue(trainers.containsKey(3L));
        assertEquals("Alice", trainers.get(3L).getFirstName());
        assertEquals("Strength", trainers.get(3L).getSpec());
    }
}
