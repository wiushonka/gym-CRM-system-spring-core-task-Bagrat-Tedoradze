package com.example.entitys.users;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TrainerEntityTest {

    @Test
    void testTrainerConstructorAndGetters() {
        Long id = 1L;
        String firstName = "John";
        String lastName = "Doe";
        String username = "John.Doe";
        String password = "password123";
        String spec = "Yoga";

        Trainer trainer = new Trainer(firstName, lastName, password, username, spec);

        assertNull(trainer.getId());
        assertEquals(firstName, trainer.getFirstName());
        assertEquals(lastName, trainer.getLastName());
        assertEquals(username, trainer.getUsername());
        assertEquals(password, trainer.getPassword());
        assertEquals(spec, trainer.getSpec());
        assertTrue(trainer.isActive());
    }

    @Test
    void testTrainerSetters() {
        Trainer trainer = new Trainer("John", "Doe", "pass", "John.Doe", "Yoga");

        trainer.setId(2L);
        trainer.setFirstName("Jane");
        trainer.setLastName("Smith");
        trainer.setUsername("Jane.Smith");
        trainer.setPassword("newpass");
        trainer.setSpec("Pilates");
        trainer.setActive(false);

        assertEquals(2L, trainer.getId());
        assertEquals("Jane", trainer.getFirstName());
        assertEquals("Smith", trainer.getLastName());
        assertEquals("Jane.Smith", trainer.getUsername());
        assertEquals("newpass", trainer.getPassword());
        assertEquals("Pilates", trainer.getSpec());
        assertFalse(trainer.isActive());
    }
}
