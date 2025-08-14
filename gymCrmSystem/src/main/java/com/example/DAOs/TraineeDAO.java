package com.example.DAOs;

import com.example.repos.TraineeStorage;
import com.example.entitys.users.Trainee;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TraineeDAO {

    private static final Logger logger = LoggerFactory.getLogger(TraineeDAO.class);

    private TraineeStorage storage;

    public TraineeDAO() {}

    @Autowired
    public void setStorage(TraineeStorage storage) {
        this.storage = storage;
        logger.debug("TraineeStorage injected into TraineeDAO");
    }

    public void addTrainee(@NotNull Trainee trainee) {
        Long id = storage.getTrainees().keySet().stream()
                .max(Long::compareTo)
                .orElse(0L) + 1;
        trainee.setId(id);
        storage.getTrainees().put(id, trainee);
        logger.info("Added trainee with ID: {} and username: {}", id, trainee.getUsername());
    }

    public Trainee getTraineeById(Long id) {
        Trainee trainee = storage.getTrainees().get(id);
        if (trainee != null) {
            logger.debug("Retrieved trainee by ID: {}", id);
        } else {
            logger.warn("No trainee found with ID: {}", id);
        }
        return trainee;
    }

    public void removeTraineeById(Long id) {
        if (storage.getTrainees().remove(id) != null) {
            logger.info("Removed trainee with ID: {}", id);
        } else {
            logger.warn("Attempted to remove trainee with ID: {}, but not found", id);
        }
    }

    public void updateTrainee(Long id, @NotNull Trainee trainee) {
        trainee.setId(id);
        storage.getTrainees().put(id, trainee);
        logger.info("Updated trainee with ID: {} and username: {}", id, trainee.getUsername());
    }

    public Trainee getTraineeByUsername(String username) {
        for (Trainee trainee : storage.getTrainees().values()) {
            if (trainee.getUsername().equals(username)) {
                logger.debug("Found trainee with username: {}", username);
                return trainee;
            }
        }
        logger.warn("No trainee found with username: {}", username);
        return null;
    }

    public List<Trainee> getAllTrainees() {
        List<Trainee> all = storage.getTrainees().values().stream().toList();
        logger.debug("Retrieved all trainees, count: {}", all.size());
        return all;
    }
}
