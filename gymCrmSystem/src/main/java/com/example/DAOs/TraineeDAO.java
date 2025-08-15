package com.example.DAOs;

import com.example.repos.TraineeStorage;
import com.example.entitys.users.Trainee;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TraineeDAO {

    private static final Logger logger = LoggerFactory.getLogger(TraineeDAO.class);

    private TraineeStorage storage;

    public TraineeDAO() {}

    @Autowired
    public void setTraineeStorage(TraineeStorage storage) {
        this.storage = storage;
        logger.debug("TraineeStorage injected into TraineeDAO");
    }

    public void addTrainee(@NotNull Trainee trainee) {
        storage.save(trainee);
        logger.info("Added Trainee: id={}, username={}", trainee.getId(), trainee.getUsername());
    }

    public void removeTraineeById(Long id) {
        storage.delete(id);
        logger.debug("Removed Trainee: id={}", id);
    }

    public void updateTrainee(Long id, @NotNull Trainee trainee) {
        storage.update(id,trainee);
        logger.info("Updated Trainee: id={}, username={}", id, trainee.getUsername());
    }

    public Optional<Trainee> getTraineeById(Long id) {
        Optional<Trainee> opTrainee=storage.findById(id);
        if(opTrainee.isPresent()) {
            Trainee trainee = opTrainee.get();
            logger.info("Retrieved Trainee: id={}, username={}", id, trainee.getUsername());
            return Optional.of(trainee);
        }else{
            logger.info("Trainee with that id could not be found");
            return Optional.empty();
        }
    }

    public Optional<Trainee> getTraineeByUsername(String username) {
        Optional<Trainee> opTrainee=storage.findByUsername(username);
        if(opTrainee.isPresent()) {
            Trainee trainee = opTrainee.get();
            logger.info("Retrieved Trainee: username={}", username);
            return Optional.of(trainee);
        }else{
            logger.info("Trainee with that username could not be found");
            return Optional.empty();
        }
    }

    public List<Trainee> getAllTrainees() {
        List<Trainee> trainees = storage.findAll();
        logger.info("Retrieving all trainees, count={}", trainees.size());
        return trainees;
    }
}
