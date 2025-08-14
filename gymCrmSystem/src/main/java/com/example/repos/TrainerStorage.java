package com.example.repos;

import com.example.entitys.users.Trainer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Repository
public class TrainerStorage {

    private static final Logger logger = LoggerFactory.getLogger(TrainerStorage.class);

    @Value("${trainer.data.file}")
    protected String initDataPath;

    private Map<Long, Trainer> trainers;

    public TrainerStorage() {}

    @PostConstruct
    public void init(){
        trainers = new HashMap<>();
        logger.info("Initializing TrainerStorage from file: {}", initDataPath);

        // ASSUME FORMAT IS : id,firstName,lastName,password,username,spec

        try {
            List<String> lines = Files.readAllLines(Paths.get(initDataPath));
            for (String line : lines) {
                String[] parts = line.split(",");
                Long id = Long.parseLong(parts[0]);
                Trainer trainer = new Trainer(id, parts[1], parts[2], parts[3], parts[4], parts[5]);
                trainers.put(id, trainer);
                logger.debug("Loaded trainer: id={}, username={}", id, parts[4]);
            }
            logger.info("Successfully loaded {} trainers", trainers.size());
        } catch (IOException e) {
            logger.error("Failed to load trainer data from {}", initDataPath, e);
            throw new RuntimeException("Failed to load trainer data from " + initDataPath, e);
        }
    }

    public Map<Long, Trainer> getTrainers() {
        return trainers;
    }

    public void setTrainers(@NotNull Map<Long, Trainer> trainers) {
        this.trainers = trainers;
        logger.debug("Trainer map manually set, size={}", trainers.size());
    }
}
