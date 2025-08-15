package com.example.services;

import com.example.DAOs.TrainerDAO;
import com.example.entitys.users.Trainer;
import com.example.utili.UserUtili;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerService {

    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    private TrainerDAO dao;

    @Autowired
    public void setTrainerDao(TrainerDAO dao){
        this.dao = dao;
        logger.debug("TrainerDAO injected into TrainerService");
    }

    public void createTrainerProfile(@NotNull Trainer trainer) {
        String firstName = trainer.getFirstName();
        String lastName = trainer.getLastName();
        firstName=firstName.toLowerCase();
        lastName=lastName.toLowerCase();
        String password = UserUtili.generatePassword();
        String username = UserUtili.generateUsername(firstName, lastName,
                                    genName->dao.getTrainerByUsername(genName).isPresent());

        trainer.setFirstName(firstName);
        trainer.setLastName(lastName);
        trainer.setUsername(username);
        trainer.setPassword(password);

        dao.addTrainer(trainer);
        logger.info("Created trainer profile: username={}, firstName={}, lastName={}", username, firstName, lastName);
    }

    public void deleteTrainerProfile(String username){
        Optional<Trainer> opTrainer = dao.getTrainerByUsername(username);
        if (opTrainer.isEmpty()) {
            logger.warn("Attempted to delete non-existent trainer: username={}", username);
            return;
        }
        Trainer trainer = opTrainer.get();
        dao.removeTrainerById(trainer.getId());
        logger.info("Deleted trainer profile: username={}", username);
    }

    public void updateTrainerProfile(@NotNull Trainer trainer){
        Long id = trainer.getId();
        Optional<Trainer> oldOpt = dao.getTrainerById(id);
        if (oldOpt.isEmpty()) {
            logger.warn("Attempted to update non-existent trainer: id={}", id);
            return;
        }
        dao.updateTrainer(id, trainer);
        logger.info("Updated trainer profile: id={}, username={}", id, trainer.getUsername());
    }

    public void updateTrainerProfileById(@NotNull Long id, @NotNull Trainer trainer){
        Optional<Trainer> oldOpt = dao.getTrainerById(id);
        if (oldOpt.isEmpty()) {
            logger.warn("Attempted to update non-existent trainer by ID: id={}", id);
            return;
        }
        dao.updateTrainer(id, trainer);
        logger.info("Updated trainer profile by ID: id={}, username={}", id, trainer.getUsername());
    }

    public Optional<Trainer> getTrainerProfile(String username){
        Optional<Trainer> trainerOpt = dao.getTrainerByUsername(username);
        if (trainerOpt.isPresent()) {
            logger.debug("Retrieved trainer profile: username={}", username);
            return trainerOpt;
        } else {
            logger.warn("No trainer found with username={}", username);
            return Optional.empty();
        }
    }

    public List<Trainer> getAllTrainers(){
        List<Trainer> all = dao.getAllTrainers();
        logger.debug("Retrieved all trainers, count={}", all.size());
        return all;
    }
}
