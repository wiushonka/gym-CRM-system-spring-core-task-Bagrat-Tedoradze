package com.example.facade;


import com.example.services.TraineeService;
import com.example.services.TrainerService;
import com.example.services.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component()
public class GymFacade {

    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingService trainingService;

    @Autowired
    public GymFacade(TrainerService traineService, TraineeService traineeService, TrainingService trainingService) {
        this.trainerService = traineService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
    }

    public TrainingService getTrainingService() { return trainingService; }

    public TrainerService getTrainerService() { return trainerService; }

    public TraineeService getTraineeService() { return traineeService; }
}
