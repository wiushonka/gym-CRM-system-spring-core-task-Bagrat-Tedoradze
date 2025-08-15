package com.example.DAOs;

import com.example.entitys.users.Trainer;
import com.example.repos.TrainerStorage;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
        storage.save(trainer);
        logger.info("Added Trainer: id={}, username={}", trainer.getId(), trainer.getUsername());
    }

    public void removeTrainerById(Long id) {
        storage.delete(id);
        logger.debug("Removed Trainer: id={}", id);
    }

    public void updateTrainer(Long id, @NotNull Trainer trainer) {
        storage.update(id,trainer);
        logger.info("Updated Trainer: id={}, username={}", id, trainer.getUsername());
    }

    public Optional<Trainer> getTrainerById(Long id) {
        Optional<Trainer> opTrainer=storage.findById(id);
        if(opTrainer.isPresent()) {
            Trainer trainer = opTrainer.get();
            logger.info("Retrieved Trainer: id={}, username={}", id, trainer.getUsername());
            return Optional.of(trainer);
        }else{
            logger.info("Trainer with that id could not be found");
            return Optional.empty();
        }
    }

    public Optional<Trainer> getTrainerByUsername(String username) {
        Optional<Trainer> opTrainer=storage.findByUsername(username);
        if(opTrainer.isPresent()) {
            Trainer trainer = opTrainer.get();
            logger.info("Retrieved Trainer: username={}", username);
            return Optional.of(trainer);
        }else{
            logger.info("Trainer with that username could not be found");
            return Optional.empty();
        }
    }

    public List<Trainer> getAllTrainers() {
        List<Trainer> trainers = storage.findAll();
        logger.info("Retrieving all trainers, count={}", trainers.size());
        return trainers;
    }
}
