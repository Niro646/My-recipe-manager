package com.example.asdasdad.Services;

import android.os.StrictMode;

import com.example.asdasdad.Models.ApiObject;
import com.example.asdasdad.Models.DataClass;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DataService {

    private ArrayList<DataClass> arrRecipes = new ArrayList<>();
    public DataService(){}

    public ArrayList<ApiObject> getRecipesFromApi(char recipeCode){

        ArrayList<ApiObject> arrR = new ArrayList<>();
        String rURL = "https://www.themealdb.com/api/json/v1/1/search.php?f=" + recipeCode;
        String name = null;
        DataClass r = null;

        URL url = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            url = new  URL(rURL);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            HttpURLConnection request = (HttpURLConnection) url.openConnection();

            request.connect();

            String jsonString = convertStreamToString(request.getInputStream());

            if(!jsonString.equals("{\"meals\":null}")){
                JSONObject json = new JSONObject(jsonString);


                JSONArray recipes = json.getJSONArray("meals");


                for(int i = 0; i < recipes.length(); i++){

                    JSONObject currentRecipe = recipes.getJSONObject(i);

                    String recipeName = currentRecipe.getString("strMeal");
                    String instructions = currentRecipe.getString("strInstructions");
                    String  imageUrl = currentRecipe.getString("strMealThumb");
                    String ingredient = "";

                    for(int j = 0; j < 20; j++){
                        String toGetIn = "strIngredient" + (j + 1);
                        String toGetMe = "strMeasure" + (j + 1);
                        //String toAdd = currentRecipe.getString(toGetIn);
                        if(currentRecipe.getString(toGetIn).length() > 0 && currentRecipe.getString(toGetIn) != "null"){
                            ingredient = ingredient + currentRecipe.getString(toGetIn) + " " + currentRecipe.getString(toGetMe) + ", ";
                        }
                    }

                    ApiObject recipeToAdd = new ApiObject(imageUrl,recipeName,ingredient,instructions);
                    arrR.add(recipeToAdd);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return arrR;
    }

    private String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
