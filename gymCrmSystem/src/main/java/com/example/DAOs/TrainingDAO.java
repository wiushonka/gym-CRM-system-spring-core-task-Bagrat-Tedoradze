package com.example.DAOs;

import com.example.repos.TrainingStorage;
import com.example.entitys.training.Training;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class TrainingDAO {

    private static final Logger logger = LoggerFactory.getLogger(TrainingDAO.class);

    private TrainingStorage storage;

    public TrainingDAO() {}

    @Autowired
    public void setTrainingStorage(TrainingStorage storage) {
        this.storage = storage;
        logger.debug("TrainingStorage injected into TrainingDAO");
    }

    public void createTraining(@NotNull Training training) {
        storage.save(training);
        logger.debug("Training created: id={}", training.getId());
    }

    public Optional<Training> getTrainingById(Long id) {
        Optional<Training> optTraining=storage.findById(id);
        if(optTraining.isPresent()) {
            logger.debug("Retrieved training by ID: {}", id);
            return optTraining;
        }else{
            logger.debug("Training not found");
            return Optional.empty();
        }
    }

    public List<Training> getTrainingsByType(String type) {
        logger.debug("searching for trainings by type: {}", type);
        return storage.findByType(type);
    }

    public Optional<Training> getTrainingByName(String name) {
        logger.debug("searching training by name: {}", name);
        return storage.getTrainingByName(name);
    }

    public List<Training> getTrainingsAfterDate(Date date) {
        logger.debug("searching training after date: {}", date);
        return storage.findTrainingsAfterCutoff(date);
    }

    public List<Training> getAllTrainings() {
        logger.debug("getting all trainings");
        return storage.findAll();
    }
}
