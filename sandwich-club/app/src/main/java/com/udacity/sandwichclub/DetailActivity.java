package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.json.JSONException;
import android.databinding.DataBindingUtil;
import com.udacity.sandwichclub.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {

    /* constants for our Intent positions */
    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    /* we will use Activity Binding to load sandwich information to the UI */
    ActivityDetailBinding dBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /* get a reference to our Image View so that we can bind sandwich images to it */
        ImageView ingredientsIv = (ImageView) findViewById(R.id.image_iv);

        /* initialize our Activity Binding object by pointing to the Activity Detail layout */
        dBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        /* get the user's intention and close app if it doesn't exist */
        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        /* find the sandwich selected by the user and close app if not found */
        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        /* create the JSON */
        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];

        /* parse the JSON into a Sandwich object and close the app if Sandwich not created */
        Sandwich sandwich = null;
        try { sandwich = JsonUtils.parseSandwichJson(json); }
        catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        /* display all sandwich details on the screen
           * CREDITS:
           * loading.gif drawable borrowed from:
           * https://dribbble.com/shots/470914-Animated-8-Bit-Hourglass
           * error.jpg drawable borrowed from:
           * http://blog.jaringanhosting.com/wp-content/uploads/2016/01/Fix-503-Service-Unavailable-Error-in-ASP.NET_.jpeg */
        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(dBinding.imageIv);

        setTitle(sandwich.getMainName());
    }

    /* This method passes a detailed error message to the user via Toast */
    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    /* This method parses a Sandwich object, binding the sandwich details to the screen */
    private void populateUI(Sandwich sandwich) {

        /* Sandwich description */
        dBinding.descriptionTv.setText(sandwich.getDescription());

        /* Sandwich also-known-as name(s)
         * NOTE: if none given, will default to "N/A" */
        StringBuilder akaText = new StringBuilder();
        if (!sandwich.getAlsoKnownAs().isEmpty()) {
            for (int i = 0; i < sandwich.getAlsoKnownAs().size(); i++) {
                akaText.append(sandwich.getAlsoKnownAs().get(i));
                if (i < (sandwich.getAlsoKnownAs().size() - 1)) akaText.append(", ");
            }
        }else akaText.append("N/A");
        dBinding.alsoKnownTv.setText(akaText.toString());

        /* Sandwich ingredients list
         * NOTE: if not specified, will default to "Unknown" */
        StringBuilder ingredientsText = new StringBuilder();
        if (sandwich.getIngredients().size() > 0) {
            for (int i = 0; i < sandwich.getIngredients().size(); i++) {
                ingredientsText.append(sandwich.getIngredients().get(i));
                if (i != (sandwich.getIngredients().size() - 1)) ingredientsText.append(", ");
            }
        }else ingredientsText.append(R.string.unknown_info_message);
        dBinding.ingredientsTv.setText(ingredientsText.toString());

        /* Sandwich place of origin
         * NOTE: if not specified, will default to "Unknown" */
        if (sandwich.getPlaceOfOrigin().isEmpty()) dBinding.originTv.setText(R.string.unknown_info_message);
        else dBinding.originTv.setText(sandwich.getPlaceOfOrigin());
    }
}
