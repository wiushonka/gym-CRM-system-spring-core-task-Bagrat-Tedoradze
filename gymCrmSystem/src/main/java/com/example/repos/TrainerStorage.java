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
                Trainer trainer = new Trainer(parts[1], parts[2], parts[3], parts[4], parts[5]);
                Long id=save(trainer);
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

    public Long save(@NotNull Trainer trainer) {
        Long id = trainers.keySet().stream().max(Long::compareTo).orElse(0L) + 1;
        trainer.setId(id);
        trainers.put(id, trainer);
        logger.debug("Saved trainer: id={}, username={}, in storage", id, trainer.getUsername());
        return id;
    }

    public void delete(@NotNull Long id) {
        Trainer removed = trainers.remove(id);
        if(removed == null) {
            logger.warn("Attempted to remove Trainer with id={} but none existed", id);
        } else {
            logger.info("Removed Trainer: id={}, username={}", id, removed.getUsername());
        }
    }

    public void update(Long id,@NotNull Trainer trainer) {
        trainer.setId(id);
        trainers.put(id, trainer);
        logger.debug("Updated trainer: id={}, username={}", id, trainer.getUsername());
    }

    public Optional<Trainer> findById(Long id) {
        Trainer trainer = trainers.get(id);
        if(trainer == null) {
            logger.warn("No Trainer found with id={}", id);
            return Optional.empty();
        } else {
            logger.info("Retrieved Trainer: id={}, username={}", id, trainer.getUsername());
            return Optional.of(trainer);
        }
    }

    public Optional<Trainer> findByUsername(String username) {
        for(Trainer trainer : trainers.values()) {
            if(trainer.getUsername().equalsIgnoreCase(username)) {
                logger.info("Found Trainer by username={}", username);
                return Optional.of(trainer);
            }
        }
        logger.warn("No Trainer found with username={}", username);
        return Optional.empty();
    }

    public List<Trainer> findAll(){
        return trainers.values().stream().toList();
    }
}
