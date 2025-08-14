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
        Long id = storage.getTrainings().keySet().stream()
                .max(Long::compareTo)
                .orElse(0L) + 1;
        training.setId(id);
        storage.getTrainings().put(id, training);
        logger.info("Created training with ID: {}, Name: {}, Type: {}",
                id, training.getTrainingName(), training.getTrainingType());
    }

    public Training getTrainingById(Long id) {
        Training training = storage.getTrainings().get(id);
        if (training != null) {
            logger.debug("Retrieved training by ID: {}", id);
        } else {
            logger.warn("No training found with ID: {}", id);
        }
        return training;
    }

    public List<Training> getTrainingsByType(String type) {
        if (type == null) {
            logger.error("getTrainingsByType called with null type");
            throw new NullPointerException("type is null");
        }
        List<Training> trainings = storage.getTrainings().values().stream()
                .filter(training -> training.getTrainingType().equals(type))
                .toList();
        logger.debug("Retrieved {} trainings of type: {}", trainings.size(), type);
        return trainings;
    }

    public Training getTrainingByName(String name) {
        Training training = storage.getTrainings().values().stream()
                .filter(t -> t.getTrainingName().equals(name))
                .findFirst()
                .orElse(null);
        if (training != null) {
            logger.debug("Found training with name: {}", name);
        } else {
            logger.warn("No training found with name: {}", name);
        }
        return training;
    }

    public List<Training> getTrainingsAfterDate(Date date) {
        List<Training> trainings = storage.getTrainings().values().stream()
                .filter(training -> training.getStartDate().after(date))
                .toList();
        logger.debug("Retrieved {} trainings after date: {}", trainings.size(), date);
        return trainings;
    }

    public List<Training> getAllTrainings() {
        List<Training> all = storage.getTrainings().values().stream().toList();
        logger.debug("Retrieved all trainings, count: {}", all.size());
        return all;
    }
}
