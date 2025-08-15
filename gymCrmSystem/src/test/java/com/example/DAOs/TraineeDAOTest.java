package com.example.DAOs;

import com.example.entitys.users.Trainee;
import com.example.repos.TraineeStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TraineeDAOTest {

    private TraineeDAO dao;
    private TraineeStorage storage;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    void setUp() throws Exception {
        storage = new TraineeStorage();
        dao = new TraineeDAO();
        dao.setTraineeStorage(storage);

        Trainee t1 = new Trainee(sdf.parse("2000-01-01"), "123 Main St", "John",
                        "Doe", "John.Doe", "pass123");
        t1.setId(1L);
        Trainee t2 = new Trainee(sdf.parse("1995-05-10"), "456 Elm St", "Jane",
                        "Smith", "Jane.Smith", "pass456");
        t2.setId(2L);

        storage.setTrainees(new HashMap<>(Map.of(1L, t1, 2L, t2)));
    }

    @Test
    void testAddTrainee() throws Exception {
        Trainee t = new Trainee(sdf.parse("1998-07-20"), "789 Oak St", "Alice",
                        "Brown", "Alice.Brown", "pass789");
        dao.addTrainee(t);

        assertNotNull(t.getId(), "ID should be assigned on add");
        assertEquals("Alice", storage.getTrainees().get(t.getId()).getFirstName());
    }

    @Test
    void testGetTraineeById() {
        Optional<Trainee> tOpt = dao.getTraineeById(1L);
        assertTrue(tOpt.isPresent());
        assertEquals("John", tOpt.get().getFirstName());
    }

    @Test
    void testRemoveTraineeById() {
        dao.removeTraineeById(2L);
        assertFalse(storage.getTrainees().containsKey(2L));
    }

    @Test
    void testUpdateTrainee() throws Exception {
        Trainee updated = new Trainee(sdf.parse("2001-02-02"), "999 Maple St", "John",
                            "Doe", "John.Doe", "newpass");
        dao.updateTrainee(1L, updated);

        Trainee t = storage.getTrainees().get(1L);
        assertEquals("999 Maple St", t.getAddr());
        assertEquals("newpass", t.getPassword());
    }

    @Test
    void testGetTraineeByUsername() {
        Optional<Trainee> tOpt = dao.getTraineeByUsername("Jane.Smith");
        assertTrue(tOpt.isPresent());
        assertEquals(2L, tOpt.get().getId());

        Optional<Trainee> missing = dao.getTraineeByUsername("Non.Existent");
        assertTrue(missing.isEmpty());
    }

    @Test
    void testGetAllTrainees() {
        assertEquals(2, dao.getAllTrainees().size());
    }
}
