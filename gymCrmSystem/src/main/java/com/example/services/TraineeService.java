package com.example.services;

import com.example.DAOs.TraineeDAO;
import com.example.entitys.users.Trainee;
import com.example.utili.UserUtili;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TraineeService {

    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    private TraineeDAO dao;

    @Autowired
    public void setTraineeDao(TraineeDAO dao){
        this.dao = dao;
        logger.debug("TraineeDAO injected into TraineeService");
    }

    public void createTraineeProfile(String firstName, String lastName, Date birthDate, String addr) {
        String password = UserUtili.generatePassword();
        String username = "" ;// = UserUtili.generateUsername(firstName, lastName, null);
//        int x = 1;
//        while (dao.getTraineeByUsername(username) != null) {
//            username = UserUtili.generateUsername(firstName, lastName, x);
//            ++x;
//        }

        Trainee trainee = new Trainee(null, birthDate, addr, firstName, lastName, username, password);
        dao.addTrainee(trainee);
        logger.info("Created trainee profile: username={}, firstName={}, lastName={}", username, firstName, lastName);
    }

    public void deleteTraineeProfile(String username){
        Trainee trainee = dao.getTraineeByUsername(username);
        if (trainee == null) {
            logger.warn("Attempted to delete non-existent trainee: username={}", username);
            return;
        }
        dao.removeTraineeById(trainee.getId());
        logger.info("Deleted trainee profile: username={}", username);
    }

    public void updateTraineeProfile(@NotNull Trainee trainee){
        Long id = trainee.getId();
        Trainee old = dao.getTraineeById(id);
        if (id == null || old == null) {
            logger.warn("Attempted to update non-existent trainee: id={}", id);
            return;
        }
        dao.updateTrainee(id, trainee);
        logger.info("Updated trainee profile: id={}, username={}", id, trainee.getUsername());
    }

    public void updateTraineeProfileById(@NotNull Long id, @NotNull Trainee trainee){
        Trainee old = dao.getTraineeById(id);
        if (old == null) {
            logger.warn("Attempted to update non-existent trainee by ID: id={}", id);
            return;
        }
        dao.updateTrainee(id, trainee);
        logger.info("Updated trainee profile by ID: id={}, username={}", id, trainee.getUsername());
    }

    public Trainee getTraineeProfile(String username){
        Trainee trainee = dao.getTraineeByUsername(username);
        if (trainee != null) {
            logger.debug("Retrieved trainee profile: username={}", username);
        } else {
            logger.warn("No trainee found with username={}", username);
        }
        return trainee;
    }

    public List<Trainee> getAllTrainees() {
        List<Trainee> all = dao.getAllTrainees();
        logger.debug("Retrieved all trainees, count={}", all.size());
        return all;
    }
}
