package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.adapters.RecipeAdapter;
import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.models.Recipe;

import java.util.List;
import android.content.Intent;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSuggestedRecipes;
    private TextView tvNoRecipes;

    private RecipeAdapter recipeAdapter;
    private DatabaseHelper databaseHelper;
    private List<Recipe> suggestedRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        recyclerViewSuggestedRecipes =
                findViewById(R.id.recyclerViewSuggestedRecipes);

        tvNoRecipes =
                findViewById(R.id.tvNoRecipes);

        databaseHelper = new DatabaseHelper(this);

        recyclerViewSuggestedRecipes.setLayoutManager(
                new LinearLayoutManager(this)
        );

        loadSuggestedRecipes();
    }

    private void loadSuggestedRecipes() {

        // Gets only recipes that can be made
        // using the current pantry ingredients
        suggestedRecipes =
                databaseHelper.getSuggestedRecipes();

        recipeAdapter = new RecipeAdapter(
                suggestedRecipes,
                recipe -> {

                    Intent intent = new Intent(
                            SuggestedRecipesActivity.this,
                            RecipeDetailActivity.class
                    );

                    intent.putExtra(
                            "RECIPE_ID",
                            recipe.getId()
                    );

                    intent.putExtra(
                            "RECIPE_NAME",
                            recipe.getName()
                    );

                    intent.putExtra(
                            "RECIPE_INSTRUCTIONS",
                            recipe.getInstructions()
                    );

                    startActivity(intent);
                }
        );

        recyclerViewSuggestedRecipes.setAdapter(recipeAdapter);

        // Shows a message instead of a blank screen
        // when no recipes match the pantry
        if (suggestedRecipes.isEmpty()) {

            recyclerViewSuggestedRecipes.setVisibility(View.GONE);
            tvNoRecipes.setVisibility(View.VISIBLE);

        } else {

            recyclerViewSuggestedRecipes.setVisibility(View.VISIBLE);
            tvNoRecipes.setVisibility(View.GONE);
        }
    }
}