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

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    ActivityDetailBinding dBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = (ImageView) findViewById(R.id.image_iv);
        dBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
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

        populateUI(sandwich);
        //TEST CODE - WILL BE DELETED ingredientsIv.setImageResource(R.mipmap.ic_launcher);
        //TEST CODE - WILL BE DELETED     .placeholder(R.drawable.loading)
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

        dBinding.descriptionTv.setText(sandwich.getDescription());

        StringBuilder akaText = new StringBuilder();
        if (sandwich.getAlsoKnownAs().size() > 0) {
            for (int i = 0; i < sandwich.getAlsoKnownAs().size(); i++) {
                akaText.append(sandwich.getAlsoKnownAs().get(i));
                if (i < (sandwich.getAlsoKnownAs().size() - 1)) akaText.append(", ");
            }
        }else akaText.append("N/A");
        dBinding.alsoKnownTv.setText(akaText.toString());

        StringBuilder ingredientsText = new StringBuilder();
        if (sandwich.getIngredients().size() > 0) {
            for (int i = 0; i < sandwich.getIngredients().size(); i++) {
                ingredientsText.append(sandwich.getIngredients().get(i));
                if (i != (sandwich.getIngredients().size() - 1)) ingredientsText.append(", ");
            }
        }else ingredientsText.append("Unknown");
        dBinding.ingredientsTv.setText(ingredientsText.toString());

        if (sandwich.getPlaceOfOrigin().isEmpty()) dBinding.originTv.setText("Unknown");
        else dBinding.originTv.setText(sandwich.getPlaceOfOrigin());
    }
}
