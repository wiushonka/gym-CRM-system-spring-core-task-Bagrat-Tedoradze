package com.example.DAOs;

import com.example.entitys.users.Trainer;
import com.example.repos.TrainerStorage;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainerDAO {

    private static final Logger logger = LoggerFactory.getLogger(TrainerDAO.class);

    private TrainerStorage storage;

    public TrainerDAO() {}

    @Autowired
    public void setTrainerStorage(TrainerStorage storage) {
        this.storage = storage;
        logger.info("TrainerStorage injected into TrainerDAO");
    }

    public void addTrainer(@NotNull Trainer trainer) {
        Long id = storage.getTrainers().keySet().stream().max(Long::compareTo).orElse(0L) + 1;
        trainer.setId(id);
        storage.getTrainers().put(id, trainer);
        logger.info("Added Trainer: id={}, username={}", id, trainer.getUsername());
    }

    public Trainer getTrainerById(Long id) {
        Trainer trainer = storage.getTrainers().get(id);
        if(trainer == null) {
            logger.warn("No Trainer found with id={}", id);
        } else {
            logger.info("Retrieved Trainer: id={}, username={}", id, trainer.getUsername());
        }
        return trainer;
    }

    public void removeTrainerById(Long id) {
        Trainer removed = storage.getTrainers().remove(id);
        if(removed == null) {
            logger.warn("Attempted to remove Trainer with id={} but none existed", id);
        } else {
            logger.info("Removed Trainer: id={}, username={}", id, removed.getUsername());
        }
    }

    public void updateTrainer(Long id, @NotNull Trainer trainer) {
        trainer.setId(id);
        storage.getTrainers().put(id, trainer);
        logger.info("Updated Trainer: id={}, username={}", id, trainer.getUsername());
    }

    public Trainer getTrainerByUsername(String username) {
        for(Trainer trainer : storage.getTrainers().values()) {
            if(trainer.getUsername().equals(username)) {
                logger.info("Found Trainer by username={}", username);
                return trainer;
            }
        }
        logger.warn("No Trainer found with username={}", username);
        return null;
    }

    public List<Trainer> getAllTrainers() {
        logger.info("Retrieving all trainers, count={}", storage.getTrainers().size());
        return storage.getTrainers().values().stream().toList();
    }
}
