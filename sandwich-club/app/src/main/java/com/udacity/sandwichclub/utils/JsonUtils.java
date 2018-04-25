package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String SCM_NAMELIST = "name";
    private static final String SCM_MAINNAME = "mainName";
    private static final String SCM_AKA = "alsoKnownAs";
    private static final String SCM_PLACEOFORIGIN = "placeOfOrigin";
    private static final String SCM_DESC = "description";
    private static final String SCM_IMAGE = "image";
    private static final String SCM_INGREDIENTSLIST = "ingredients";

    public static Sandwich parseSandwichJson(String json) throws JSONException{

        Sandwich returnSandwich = new Sandwich();

        JSONObject sandwichJson = null;

        try { sandwichJson = new JSONObject(json); }
        catch (Exception e) { return null; }

        JSONObject mainNameObject = sandwichJson.getJSONArray(SCM_NAMELIST).getJSONObject(0);
        returnSandwich.setMainName(mainNameObject.getString(SCM_MAINNAME));

        JSONArray akaArray = mainNameObject.getJSONArray(SCM_AKA);
        List<String> akaNames = new ArrayList<String>();
        for (int i = 0; i < akaArray.length(); i++) {
            akaNames.add(akaArray.getString(i));
        }
        returnSandwich.setAlsoKnownAs(akaNames);

        returnSandwich.setPlaceOfOrigin(sandwichJson.getString(SCM_PLACEOFORIGIN));

        returnSandwich.setDescription(sandwichJson.getString(SCM_DESC));

        returnSandwich.setImage(sandwichJson.getString(SCM_IMAGE));

        JSONArray ingredientsArray = sandwichJson.getJSONArray(SCM_INGREDIENTSLIST);
        List<String> ingredients = new ArrayList<String>();
        for (int i = 0; i < ingredientsArray.length(); i++) {
            ingredients.add(ingredientsArray.getString(i));
        }
        returnSandwich.setIngredients(ingredients);

        return returnSandwich;
    }
}
