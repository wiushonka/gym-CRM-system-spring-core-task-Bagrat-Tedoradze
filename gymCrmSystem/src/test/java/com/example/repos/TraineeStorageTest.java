package com.example.repos;

import com.example.entitys.users.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TraineeStorageTest {

    private TraineeStorage storage;

    @BeforeEach
    void setUp() throws Exception {
        File tempFile = File.createTempFile("trainees", ".txt");
        tempFile.deleteOnExit();

        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        writer.write("1,2000-05-15,123 Main St,John,Doe,John.Doe,password123\n");
        writer.write("2,1995-12-01,456 Elm St,Jane,Smith,Jane.Smith,password456\n");
        writer.close();

        storage = new TraineeStorage();
        storage.initDataPath = tempFile.getAbsolutePath();
        storage.init();
    }

    @Test
    void testInitLoadsTrainees() {
        Map<Long, Trainee> trainees = storage.getTrainees();

        assertEquals(2, trainees.size());

        Trainee t1 = trainees.get(1L);
        assertNotNull(t1);
        assertEquals("John", t1.getFirstName());
        assertEquals("Doe", t1.getLastName());
        assertEquals("John.Doe", t1.getUsername());
        assertEquals("password123", t1.getPassword());

        Trainee t2 = trainees.get(2L);
        assertNotNull(t2);
        assertEquals("Jane", t2.getFirstName());
        assertEquals("Smith", t2.getLastName());
        assertEquals("Jane.Smith", t2.getUsername());
        assertEquals("password456", t2.getPassword());
    }

    @Test
    void testSetAndGetTrainees() throws Exception {
        Map<Long, Trainee> newMap = Map.of(
                3L, new Trainee(3L, new SimpleDateFormat("yyyy-MM-dd").parse("1998-07-20"),
                        "789 Oak St", "Alice", "Brown", "Alice.Brown", "pass789"));

        storage.setTrainees(newMap);

        Map<Long, Trainee> trainees = storage.getTrainees();
        assertEquals(1, trainees.size());
        assertTrue(trainees.containsKey(3L));
        assertEquals("Alice", trainees.get(3L).getFirstName());
        assertEquals("789 Oak St", trainees.get(3L).getAddr());
    }
}
