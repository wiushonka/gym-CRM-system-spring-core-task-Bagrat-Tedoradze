package com.example.entitys.training;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Training {

    private Long id;
    private Long traineeId;
    private Long trainerId;
    private String trainingName;
    private String trainingType;
    private Date startDate;
    private Long trainingDuration;

    public Training(Long id,Long trainerId, Long traineeId, String trainingName,
                    String trainingType, @NotNull Date startDate, Long trainingDuration) {
        this.id = id;
        this.trainerId=trainerId;
        this.traineeId=traineeId;
        this.trainingName=trainingName;
        this.trainingType=trainingType;
        this.startDate=(Date) startDate.clone();
        this.trainingDuration=trainingDuration;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getTraineeId() { return traineeId; }

    public void setTraineeId(Long traineeId) { this.traineeId = traineeId; }

    public Long getTrainerId() { return trainerId; }

    public void setTrainerId(Long trainerId) { this.trainerId = trainerId; }

    public String getTrainingName() { return trainingName; }

    public void setTrainingName(String trainingName) { this.trainingName = trainingName; }

    public String getTrainingType() { return trainingType; }

    public void setTrainingType(String trainingType) { this.trainingType = trainingType; }

    public Date getStartDate() { return (Date) startDate.clone(); }

    public void setStartDate(@NotNull Date startDate) { this.startDate = (Date) startDate.clone(); }

    public Long getTrainingDuration() { return trainingDuration; }

    public void setTrainingDuration(Long trainingDuration) { this.trainingDuration = trainingDuration; }
}
