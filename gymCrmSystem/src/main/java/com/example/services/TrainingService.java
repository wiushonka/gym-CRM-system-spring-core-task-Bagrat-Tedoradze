package com.example.services;

import com.example.DAOs.TrainingDAO;
import com.example.entitys.training.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

@Service
public class TrainingService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);

    private TrainingDAO dao;

    @Autowired
    public void setTrainingDao(TrainingDAO dao) {
        this.dao = dao;
        logger.info("TrainingDAO injected into TrainingService");
    }

    public List<Training> getTrainings() {
        List<Training> trainings = dao.getAllTrainings();
        logger.debug("Retrieved all trainings, count={}", trainings.size());
        return trainings;
    }

    public void addTraining(Long id, Long trainerId, Long traineeId,
                            String name, String type, Date date, Long duration) {
        int x = 1;
        String originalName = name;
        while (dao.getTrainingByName(name) != null) {
            ++x;
            name = originalName + "_" + x;
        }
        Training training = new Training(id, trainerId, traineeId, name, type, date, duration);
        dao.createTraining(training);
        logger.info("Added new training: id={}, name={}, type={}", training.getId(), name, type);
    }

    public Training getTrainingById(Long id) {
        Training t = dao.getTrainingById(id);
        if (t != null) {
            logger.debug("Retrieved training by id={}: name={}", id, t.getTrainingName());
        } else {
            logger.warn("Training with id={} not found", id);
        }
        return t;
    }

    public Training getTrainingByName(String name) {
        Training t = dao.getTrainingByName(name);
        if (t != null) {
            logger.debug("Retrieved training by name={}: id={}", name, t.getId());
        } else {
            logger.warn("Training with name={} not found", name);
        }
        return t;
    }

    public List<Training> getNewerTrainingsThan(Date start) {
        List<Training> trainings = dao.getTrainingsAfterDate(start);
        logger.debug("Retrieved trainings after {}: count={}", start, trainings.size());
        return trainings;
    }
}
