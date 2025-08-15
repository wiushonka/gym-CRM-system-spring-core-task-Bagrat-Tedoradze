package com.example.services;

import com.example.DAOs.TraineeDAO;
import com.example.entitys.users.Trainee;
import com.example.utili.Utili;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TraineeService {

    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    private TraineeDAO dao;

    @Autowired
    public void setTraineeDao(TraineeDAO dao){
        this.dao = dao;
        logger.debug("TraineeDAO injected into TraineeService");
    }

    public void createTraineeProfile(@NotNull Trainee trainee) {
        String firstName=trainee.getFirstName().toLowerCase();
        String lastName=trainee.getLastName().toLowerCase();
        String password = Utili.generatePassword();
        String username = Utili.generateUsername(firstName,lastName,
                                            genName->dao.getTraineeByUsername(genName).isPresent());
        trainee.setUsername(username);
        trainee.setPassword(password);
        trainee.setFirstName(firstName);
        trainee.setLastName(lastName);

        dao.addTrainee(trainee);
        logger.info("Created trainee profile: username={}, firstName={}, lastName={}", username, firstName, lastName);
    }

    public void deleteTraineeProfile(String username){
        Optional<Trainee> trainee = dao.getTraineeByUsername(username);
        if (trainee.isEmpty()) {
            logger.warn("Attempted to delete non-existent trainee: username={}", username);
            return;
        }
        dao.removeTraineeById(trainee.get().getId());
        logger.info("Deleted trainee profile: username={}", username);
    }

    public void updateTraineeProfile(@NotNull Trainee trainee){
        Long id = trainee.getId();
        Optional<Trainee> old = dao.getTraineeById(id);
        if (old.isEmpty()) {
            logger.warn("Attempted to update non-existent trainee: id={}", id);
            return;
        }
        dao.updateTrainee(id, trainee);
        logger.info("Updated trainee profile: id={}, username={}", id, trainee.getUsername());
    }

    public void updateTraineeProfileById(@NotNull Long id, @NotNull Trainee trainee){
        Optional<Trainee> old = dao.getTraineeById(id);
        if (old.isEmpty()) {
            logger.warn("Attempted to update non-existent trainee by ID: id={}", id);
            return;
        }
        dao.updateTrainee(id, trainee);
        logger.info("Updated trainee profile by ID: id={}, username={}", id, trainee.getUsername());
    }

    public Optional<Trainee> getTraineeProfile(String username){
        Optional<Trainee> traineeOpt = dao.getTraineeByUsername(username);
        if (traineeOpt.isPresent()) {
            logger.debug("Retrieved trainee profile: username={}", username);
            return traineeOpt;
        } else {
            logger.warn("No trainee found with username={}", username);
            return Optional.empty();
        }
    }

    public List<Trainee> getAllTrainees() {
        List<Trainee> all = dao.getAllTrainees();
        logger.debug("Retrieved all trainees, count={}", all.size());
        return all;
    }
}
