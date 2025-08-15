package com.example.services;

import com.example.DAOs.TrainingDAO;
import com.example.entitys.training.Training;
import com.example.utili.Utili;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    public void addTraining(@NotNull Training training) {
        String name=training.getTrainingName().toLowerCase();
        name=Utili.generateUniqueNameForTraining(name,genName->dao.getTrainingByName(genName).isPresent());
        training.setTrainingName(name);
        dao.createTraining(training);
        logger.info("Added new training: id={}, name={}, type={}", training.getId(),
                        training.getTrainingName(),training.getTrainingType());
    }

    public Optional<Training> getTrainingById(Long id) {
        Optional<Training> t = dao.getTrainingById(id);
        if (t.isPresent()) {
            logger.debug("Retrieved training by id={}: name={}", id, t.get().getTrainingName());
            return t;
        } else {
            logger.warn("Training with id={} not found", id);
            return Optional.empty();
        }
    }

    public Optional<Training> getTrainingByName(String name) {
        Optional<Training> t = dao.getTrainingByName(name);
        if (t.isPresent()) {
            logger.debug("Retrieved training by name={}: id={}", name, t.get().getId());
            return t;
        } else {
            logger.warn("Training with name={} not found", name);
            return Optional.empty();
        }
    }

    public List<Training> getNewerTrainingsThan(Date start) {
        List<Training> trainings = dao.getTrainingsAfterDate(start);
        logger.debug("Retrieved trainings after {}: count={}", start, trainings.size());
        return trainings;
    }
}
