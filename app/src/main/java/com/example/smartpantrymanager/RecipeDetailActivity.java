package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.models.RecipeIngredient;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView tvRecipeDetailName;
    private TextView tvRecipeIngredients;
    private TextView tvRecipeInstructions;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        tvRecipeDetailName =
                findViewById(R.id.tvRecipeDetailName);

        tvRecipeIngredients =
                findViewById(R.id.tvRecipeIngredients);

        tvRecipeInstructions =
                findViewById(R.id.tvRecipeInstructions);

        databaseHelper = new DatabaseHelper(this);

        // Receive the selected recipe information
        int recipeId =
                getIntent().getIntExtra("RECIPE_ID", -1);

        String recipeName =
                getIntent().getStringExtra("RECIPE_NAME");

        String recipeInstructions =
                getIntent().getStringExtra("RECIPE_INSTRUCTIONS");

        // Display recipe name and preparation method
        tvRecipeDetailName.setText(recipeName);
        tvRecipeInstructions.setText(recipeInstructions);

        // Retrieve this recipe's ingredients from SQLite
        List<RecipeIngredient> ingredients =
                databaseHelper.getRecipeIngredients(recipeId);

        StringBuilder ingredientText = new StringBuilder();

        for (RecipeIngredient ingredient : ingredients) {

            ingredientText
                    .append("• ")
                    .append(ingredient.getQuantity())
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append(" ")
                    .append(ingredient.getIngredientName())
                    .append("\n");
        }

        tvRecipeIngredients.setText(
                ingredientText.toString().trim()
        );
    }
}