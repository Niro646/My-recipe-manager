package com.example.asdasdad;

public class DataClass {
    private String dataName;
    private String dataDifficultyLevel;
    private String dataPreparationTime;
    private String dataDescription;
    private String dataImage;
    private Integer dataVigen;

    public DataClass(String dataName, String dataDifficultyLevel, String dataPreparationTime, String dataDescription, String dataImage, Integer dataVigen) {
        this.dataName = dataName;
        this.dataDifficultyLevel = dataDifficultyLevel;
        this.dataPreparationTime = dataPreparationTime;
        this.dataDescription = dataDescription;
        this.dataImage = dataImage;
        this.dataVigen = dataVigen;
    }

    public String getDataName() {
        return dataName;
    }

    public String getDataDifficultyLevel() {
        return dataDifficultyLevel;
    }

    public String getDataPreparationTime() {
        return dataPreparationTime;
    }

    public String getDataDescription() {
        return dataDescription;
    }

    public String getDataImage() {
        return dataImage;
    }

    public Integer getDataVigen() {
        return dataVigen;
    }
}
