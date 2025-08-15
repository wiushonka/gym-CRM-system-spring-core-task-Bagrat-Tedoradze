package com.example.repos;

import com.example.entitys.users.Trainee;
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

        // ASSUME FORMAT IS : birthDate,address,firstname,lastname,username,password

        try {
            logger.info("Loading trainee data from file: {}", initDataPath);
            List<String> lines = Files.readAllLines(Paths.get(initDataPath));
            for (String line : lines) {
                String[] parts = line.split(",");
                Date date = df.parse(parts[0]);
                Trainee trainee = new Trainee(date, parts[1], parts[2], parts[3], parts[4], parts[5]);
                Long id = save(trainee);
                logger.debug("Loaded trainee: id={}, username={}", id, parts[4]);
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

    public Long save(@NotNull Trainee trainee) {
        Long id=trainees.keySet().stream().max(Long::compareTo).orElse(0L) + 1;
        trainee.setId(id);
        trainees.put(id, trainee);
        logger.debug("Saved trainee: id={}, username={}, in storage", id, trainee.getUsername());
        return id;
    }

    public void delete(@NotNull Long id){
        Trainee removed=trainees.remove(id);
        if(removed == null) {
            logger.warn("Attempted to remove trainee with id={} but none existed", id);
        } else {
            logger.info("Removed Trainee: id={}, username={}", id, removed.getUsername());
        }
    }

    public void update(Long id,@NotNull Trainee trainee) {
        trainee.setId(id);
        trainees.put(id, trainee);
        logger.debug("Updated trainee: id={}, username={}", id, trainee.getUsername());
    }

    public Optional<Trainee> findById(Long id) {
        Trainee trainee = trainees.get(id);
        if(trainee == null) {
            logger.warn("No Trainee found with id={}", id);
            return Optional.empty();
        } else {
            logger.info("Retrieved Trainee: id={}, username={}", id, trainee.getUsername());
            return Optional.of(trainee);
        }
    }

    public Optional<Trainee> findByUsername(String username) {
        for(Trainee trainee : trainees.values()) {
            if(trainee.getUsername().equalsIgnoreCase(username)) {
                logger.info("Found Trainee by username={}", username);
                return Optional.of(trainee);
            }
        }
        logger.warn("No Trainee found with username={}", username);
        return Optional.empty();
    }

    public List<Trainee> findAll(){
        return trainees.values().stream().toList();
    }
}
