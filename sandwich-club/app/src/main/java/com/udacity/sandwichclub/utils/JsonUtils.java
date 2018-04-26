package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.R;
import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    /* Constants representing the JSON structure
     * NOTE: if the JSON structure is changed, these constants will need to be updated */
    private static final String SCM_NAMELIST = "name";
    private static final String SCM_MAINNAME = "mainName";
    private static final String SCM_AKA = "alsoKnownAs";
    private static final String SCM_PLACEOFORIGIN = "placeOfOrigin";
    private static final String SCM_DESC = "description";
    private static final String SCM_IMAGE = "image";
    private static final String SCM_INGREDIENTSLIST = "ingredients";

    /* This method will parse a JSON string into a returnable Sandwich object */
    public static Sandwich parseSandwichJson(String json) throws JSONException{

        Sandwich returnSandwich = new Sandwich();

        /* Create a JSON object from the passed-in string */
        JSONObject sandwichJson = null;
        try { sandwichJson = new JSONObject(json); }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        /* Set the Sandwich main name */
        returnSandwich.setMainName(sandwichJson.getJSONObject(SCM_NAMELIST).getString(SCM_MAINNAME));

        /* Set the Sandwich also-known-as name(s) */
        JSONArray akaArray = sandwichJson.getJSONObject(SCM_NAMELIST).getJSONArray(SCM_AKA);
        List<String> akaNames = new ArrayList<String>();
        if (akaArray.length() > 0) {
            for (int i = 0; i < akaArray.length(); i++) {
                akaNames.add(akaArray.getString(i));
            }
        }
        returnSandwich.setAlsoKnownAs(akaNames);

        /* Set the Sandwich place of origin */
        returnSandwich.setPlaceOfOrigin(sandwichJson.getString(SCM_PLACEOFORIGIN));

        /* Set the Sandwich description */
        returnSandwich.setDescription(sandwichJson.getString(SCM_DESC));

        /* Set the Sandwich image */
        returnSandwich.setImage(sandwichJson.getString(SCM_IMAGE));

        /* Set the Sandwich ingredients */
        JSONArray ingredientsArray = sandwichJson.getJSONArray(SCM_INGREDIENTSLIST);
        List<String> ingredients = new ArrayList<String>();
        for (int i = 0; i < ingredientsArray.length(); i++) {
            ingredients.add(ingredientsArray.getString(i));
        }
        returnSandwich.setIngredients(ingredients);

        return returnSandwich;
    }
}
