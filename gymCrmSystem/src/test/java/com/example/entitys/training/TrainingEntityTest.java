package com.example.entitys.training;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TrainingEntityTest {
    @Test
    void testTrainingConstructorAndGetters() {
        Long id = 1L;
        Long trainerId = 10L;
        Long traineeId = 20L;
        String trainingName = "Yoga Basics";
        String trainingType = "Yoga";
        Date startDate = new Date(1672531200000L); // 2023-01-01
        Long duration = 60L;

        Training training = new Training(id, trainerId, traineeId, trainingName, trainingType, startDate, duration);

        assertEquals(id, training.getId());
        assertEquals(trainerId, training.getTrainerId());
        assertEquals(traineeId, training.getTraineeId());
        assertEquals(trainingName, training.getTrainingName());
        assertEquals(trainingType, training.getTrainingType());
        assertEquals(startDate, training.getStartDate());
        assertEquals(duration, training.getTrainingDuration());
    }

    @Test
    void testTrainingSetters() {
        Training training = new Training(1L, 10L, 20L, "Yoga Basics", "Yoga", new Date(), 60L);

        training.setId(2L);
        training.setTrainerId(11L);
        training.setTraineeId(21L);
        training.setTrainingName("Advanced Yoga");
        training.setTrainingType("Advanced");
        Date newDate = new Date(1680000000000L);
        training.setStartDate(newDate);
        training.setTrainingDuration(90L);

        assertEquals(2L, training.getId());
        assertEquals(11L, training.getTrainerId());
        assertEquals(21L, training.getTraineeId());
        assertEquals("Advanced Yoga", training.getTrainingName());
        assertEquals("Advanced", training.getTrainingType());
        assertEquals(newDate, training.getStartDate());
        assertEquals(90L, training.getTrainingDuration());
    }

    @Test
    void testStartDateImmutability() {
        Date startDate = new Date(1672531200000L); // 2023-01-01
        Training training = new Training(1L, 10L, 20L, "Yoga Basics", "Yoga", startDate, 60L);

        startDate.setTime(0L);
        assertNotEquals(0L, training.getStartDate().getTime());

        Date returnedDate = training.getStartDate();
        returnedDate.setTime(0L);
        assertNotEquals(0L, training.getStartDate().getTime());
    }
}