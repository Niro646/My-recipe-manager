package com.example.asdasdad;

public class DataClass {
    public String dataImage;
    public String dataName;
    public String dataIngredients;
    public String dataDescription;
    public String dataDifficultyLevel;
    public String dataPreparationTime;
    //public Boolean dataVegan;
   // public Boolean dataVegetarian;



    public DataClass(String dataImage, String dataName, String dataIngredients, String dataDescription, String dataDifficultyLevel, String dataPreparationTime) {
        this.dataImage = dataImage;
        this.dataName = dataName;
        this.dataIngredients = dataIngredients;
        this.dataDescription = dataDescription;
        this.dataDifficultyLevel = dataDifficultyLevel;
        this.dataPreparationTime = dataPreparationTime;
        //this.dataVegan = dataVegan;
        //this.dataVegetarian = dataVegetarian;
    }

    public DataClass(){}

    public String getDataImage() {
        return dataImage;
    }

    public String getDataName() {
        return dataName;
    }

    public String getDataIngredients() {
        return dataIngredients;
    }

    public String getDataDescription() {
        return dataDescription;
    }

    public String getDataDifficultyLevel() {
        return dataDifficultyLevel;
    }

    public String getDataPreparationTime() {
        return dataPreparationTime;
    }


}
