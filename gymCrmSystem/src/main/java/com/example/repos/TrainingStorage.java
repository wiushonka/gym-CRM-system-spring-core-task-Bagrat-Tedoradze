package com.example.repos;

import com.example.entitys.training.Training;
import jakarta.annotation.PostConstruct;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        // ASSUME FORMAT IS : id,trainerId,traineeId,trainingName,trainingType,startDate,trainingDuration

        try {
            List<String> lines = Files.readAllLines(Paths.get(initDataPath));
            for (String line : lines) {
                String[] parts = line.split(",");
                Long id = Long.parseLong(parts[0]);
                Long trainerId = Long.parseLong(parts[1]);
                Long traineeId = Long.parseLong(parts[2]);
                Date date = df.parse(parts[5]);
                Long trainingDuration = Long.parseLong(parts[6]);
                Training training = new Training(id, trainerId, traineeId, parts[3], parts[4], date, trainingDuration);
                trainings.put(id, training);
                logger.debug("Loaded training from file: id={}, name={}, type={}", id, parts[3], parts[4]);
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
}
