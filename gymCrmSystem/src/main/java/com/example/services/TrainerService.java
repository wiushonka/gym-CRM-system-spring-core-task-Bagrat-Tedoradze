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

@Service
public class TrainerService {

    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    private TrainerDAO dao;

    @Autowired
    public void setTrainerDao(TrainerDAO dao){
        this.dao = dao;
        logger.debug("TrainerDAO injected into TrainerService");
    }

    public void createTrainerProfile(String firstName, String lastName){
        String password = UserUtili.generatePassword();
        String username = UserUtili.generateUsername(firstName, lastName, null);
        int x = 1;
        while (dao.getTrainerByUsername(username) != null){
            username = UserUtili.generateUsername(firstName, lastName, x);
            ++x;
        }

        Trainer trainer = new Trainer(null, firstName, lastName, password, username, "");
        dao.addTrainer(trainer);
        logger.info("Created trainer profile: username={}, firstName={}, lastName={}", username, firstName, lastName);
    }

    public void deleteTrainerProfile(String username){
        Trainer trainer = dao.getTrainerByUsername(username);
        if (trainer == null) {
            logger.warn("Attempted to delete non-existent trainer: username={}", username);
            return;
        }
        dao.removeTrainerById(trainer.getId());
        logger.info("Deleted trainer profile: username={}", username);
    }

    public void updateTrainerProfile(@NotNull Trainer trainer){
        Long id = trainer.getId();
        Trainer old = dao.getTrainerById(id);
        if (id == null || old == null) {
            logger.warn("Attempted to update non-existent trainer: id={}", id);
            return;
        }
        dao.updateTrainer(id, trainer);
        logger.info("Updated trainer profile: id={}, username={}", id, trainer.getUsername());
    }

    public void updateTrainerProfileById(@NotNull Long id, @NotNull Trainer trainer){
        Trainer old = dao.getTrainerById(id);
        if (old == null) {
            logger.warn("Attempted to update non-existent trainer by ID: id={}", id);
            return;
        }
        dao.updateTrainer(id, trainer);
        logger.info("Updated trainer profile by ID: id={}, username={}", id, trainer.getUsername());
    }

    public Trainer getTrainerProfile(String username){
        Trainer trainer = dao.getTrainerByUsername(username);
        if (trainer != null) {
            logger.debug("Retrieved trainer profile: username={}", username);
        } else {
            logger.warn("No trainer found with username={}", username);
        }
        return trainer;
    }

    public List<Trainer> getAllTrainers(){
        List<Trainer> all = dao.getAllTrainers();
        logger.debug("Retrieved all trainers, count={}", all.size());
        return all;
    }
}
