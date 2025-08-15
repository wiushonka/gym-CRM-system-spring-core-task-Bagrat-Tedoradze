package com.example.repos;

import com.example.entitys.users.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TraineeStorageTest {

    private TraineeStorage storage;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    void setUp() throws Exception {
        File tempFile = File.createTempFile("trainees", ".txt");
        tempFile.deleteOnExit();

        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        writer.write("2000-05-15,123 Main St,John,Doe,John.Doe,password123\n");
        writer.write("1995-12-01,456 Elm St,Jane,Smith,Jane.Smith,password456\n");
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
        assertEquals("123 Main St", t1.getAddr());

        Trainee t2 = trainees.get(2L);
        assertNotNull(t2);
        assertEquals("Jane", t2.getFirstName());
        assertEquals("Smith", t2.getLastName());
        assertEquals("Jane.Smith", t2.getUsername());
        assertEquals("password456", t2.getPassword());
        assertEquals("456 Elm St", t2.getAddr());
    }

    @Test
    void testSetAndGetTrainees() throws Exception {
        Date birth = df.parse("1998-07-20");
        Trainee alice = new Trainee(birth, "789 Oak St", "Alice",
                                    "Brown", "Alice.Brown", "pass789");
        alice.setId(3L);

        Map<Long, Trainee> newMap = Map.of(3L, alice);
        storage.setTrainees(newMap);

        Map<Long, Trainee> trainees = storage.getTrainees();
        assertEquals(1, trainees.size());
        assertTrue(trainees.containsKey(3L));
        assertEquals("Alice", trainees.get(3L).getFirstName());
        assertEquals("789 Oak St", trainees.get(3L).getAddr());
    }

    @Test
    void testSaveAndFindById() throws Exception {
        Date birth = df.parse("2001-03-10");
        Trainee bob = new Trainee(birth, "101 Pine St", "Bob",
                        "Smith", "Bob.Smith", "pass101");

        Long id = storage.save(bob);
        assertEquals(id, bob.getId());

        Optional<Trainee> found = storage.findById(id);
        assertTrue(found.isPresent());
        assertEquals("Bob", found.get().getFirstName());
    }

    @Test
    void testDeleteTrainee() throws Exception {
        Date birth = df.parse("2002-08-22");
        Trainee charlie = new Trainee(birth, "202 Maple Ave", "Charlie",
                                "White", "Charlie.White", "pass202");
        Long id = storage.save(charlie);

        storage.delete(id);
        assertTrue(storage.findById(id).isEmpty());
    }

    @Test
    void testUpdateTrainee() throws Exception {
        Date birth = df.parse("2003-11-05");
        Trainee dave = new Trainee(birth, "303 Birch Rd", "Dave",
                            "Black", "Dave.Black", "pass303");
        Long id = storage.save(dave);

        dave.setAddr("Updated Address");
        storage.update(id, dave);

        Optional<Trainee> updated = storage.findById(id);
        assertTrue(updated.isPresent());
        assertEquals("Updated Address", updated.get().getAddr());
    }

    @Test
    void testFindByUsername() throws Exception {
        Date birth = df.parse("1999-06-30");
        Trainee eve = new Trainee(birth, "404 Cedar Ln", "Eve",
                        "Green", "Eve.Green", "pass404");
        storage.save(eve);

        Optional<Trainee> found = storage.findByUsername("Eve.Green");
        assertTrue(found.isPresent());
        assertEquals("Eve", found.get().getFirstName());

        Optional<Trainee> notFound = storage.findByUsername("Non.Existent");
        assertTrue(notFound.isEmpty());
    }

    @Test
    void testFindAll() throws Exception {
        Date birth = df.parse("2000-01-01");
        Trainee fiona = new Trainee(birth, "505 Spruce St", "Fiona",
                                    "Blue", "Fiona.Blue", "pass505");
        storage.save(fiona);

        assertTrue(storage.findAll().contains(fiona));
    }
}
