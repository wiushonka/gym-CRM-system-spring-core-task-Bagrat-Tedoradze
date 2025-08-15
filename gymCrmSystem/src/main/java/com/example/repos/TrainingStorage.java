package com.example.repos;

import com.example.entitys.training.Training;
import jakarta.annotation.PostConstruct;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class TrainingStorage {

    private static final Logger logger = LoggerFactory.getLogger(TrainingStorage.class);

    @Value("${training.data.file}")
    protected String initDataPath;

    private Map<Long, Training> trainings;

    public TrainingStorage() {}

    @PostConstruct
    public void init() {
        trainings = new HashMap<>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        // ASSUME FORMAT IS : trainerId,traineeId,trainingName,trainingType,startDate,trainingDuration

        try {
            List<String> lines = Files.readAllLines(Paths.get(initDataPath));
            for (String line : lines) {
                String[] parts = line.split(",");
                Long trainerId = Long.parseLong(parts[0]);
                Long traineeId = Long.parseLong(parts[1]);
                Date date = df.parse(parts[4]);
                Long trainingDuration = Long.parseLong(parts[5]);
                Training training = new Training(trainerId, traineeId, parts[2], parts[3], date, trainingDuration);
                Long id = save(training);
                logger.debug("Loaded training from file: id={}, name={}, type={}", id, parts[2], parts[3]);
            }
            logger.info("Finished loading {} trainings from {}", trainings.size(), initDataPath);
        } catch (IOException e) {
            logger.error("Failed to load training data from {}", initDataPath, e);
            throw new RuntimeException("Failed to load training data from " + initDataPath, e);
        } catch (ParseException e) {
            logger.error("Failed to parse date in training data", e);
            throw new RuntimeException(e);
        }
    }

    public Map<Long, Training> getTrainings() { return trainings; }

    public void setTrainings(Map<Long, Training> trainings) { this.trainings = trainings; }

    public Long save(@NotNull Training training) {
        Long id = trainings.keySet().stream().max(Long::compareTo).orElse(0L) + 1;
        training.setId(id);
        trainings.put(id, training);
        logger.info("Created training with ID: {}, Name: {}, Type: {}",
                    id, training.getTrainingName(), training.getTrainingType());

        return id;
    }

    public Optional<Training> findById(Long id){
        Training training = trainings.get(id);
        if (training != null) {
            logger.debug("Retrieved training by ID: {}", id);
            return Optional.of(training);
        } else {
            logger.warn("No training found with ID: {}", id);
            return Optional.empty();
        }
    }

    public List<Training> findByType(String type) {
        List<Training> result=trainings.values().stream()
                .filter(training -> training.getTrainingType().equals(type))
                .toList();
        logger.debug("Retrieved {} trainings of type: {}", trainings.size(), type);
        return result;
    }

    public Optional<Training> getTrainingByName(String name) {
        Optional<Training> training = trainings.values().stream()
                .filter(t -> t.getTrainingName().equals(name))
                .findFirst();
        if (training.isPresent()) {
            logger.debug("Found training with name: {}", name);
            return training;
        } else {
            logger.warn("No training found with name: {}", name);
            return Optional.empty();
        }
    }

    public List<Training> findTrainingsAfterCutoff(Date date) {
        List<Training> result = trainings.values().stream()
                .filter(training -> training.getStartDate().after(date))
                .toList();
        logger.debug("Retrieved {} trainings after date: {}", trainings.size(), date);
        return result;
    }

    public List<Training> findAll(){
        List<Training> all = trainings.values().stream().toList();
        logger.debug("Retrieved all trainings, count: {}", all.size());
        return all;
    }
}
