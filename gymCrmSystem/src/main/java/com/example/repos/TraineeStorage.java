package com.example.repos;

import com.example.entitys.users.Trainee;
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
public class TraineeStorage {

    private static final Logger logger = LoggerFactory.getLogger(TraineeStorage.class);

    @Value("${trainee.data.file}")
    protected String initDataPath;

    private Map<Long, Trainee> trainees;

    public TraineeStorage() {}

    @PostConstruct
    public void init() {
        trainees = new HashMap<>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        try {
            logger.info("Loading trainee data from file: {}", initDataPath);
            List<String> lines = Files.readAllLines(Paths.get(initDataPath));
            for (String line : lines) {
                String[] parts = line.split(",");
                Long id = Long.parseLong(parts[0]);
                Date date = df.parse(parts[1]);
                Trainee trainee = new Trainee(id, date, parts[2], parts[3], parts[4], parts[5], parts[6]);
                trainees.put(id, trainee);
                logger.debug("Loaded trainee: {} {}", parts[3], parts[4]);
            }
            logger.info("Successfully loaded {} trainees", trainees.size());
        } catch (IOException e) {
            logger.error("Failed to load trainee data from {}", initDataPath, e);
            throw new RuntimeException("Failed to load trainee data from " + initDataPath, e);
        } catch (ParseException e) {
            logger.error("Failed to parse date while loading trainee data", e);
            throw new RuntimeException(e);
        }
    }

    public Map<Long, Trainee> getTrainees() {
        return trainees;
    }

    public void setTrainees(Map<Long, Trainee> trainees) {
        this.trainees = trainees;
    }
}
