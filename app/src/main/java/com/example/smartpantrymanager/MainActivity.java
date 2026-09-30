package com.example.smartpantrymanager;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartpantrymanager.adapters.PantryAdapter;
import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.models.PantryItem;
import java.util.List;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPantry;
    private PantryAdapter pantryAdapter;
    private DatabaseHelper databaseHelper;
    private List<PantryItem> pantryItems;
    private com.google.android.material.button.MaterialButton btnAddIngredient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialises the database
        databaseHelper = new DatabaseHelper(this);

        // Get the views from the layout
        recyclerViewPantry = findViewById(R.id.recyclerViewPantry);
        btnAddIngredient = findViewById(R.id.btnAddIngredient);

        // RecyclerView Setup
        recyclerViewPantry.setLayoutManager(new LinearLayoutManager(this));

        pantryItems = databaseHelper.getAllPantryItems();

        pantryAdapter = new PantryAdapter(pantryItems);

        recyclerViewPantry.setAdapter(pantryAdapter);

        // Opens the Add Ingredient screen
        btnAddIngredient.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddIngredient.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null && pantryAdapter != null) {

            pantryItems.clear();
            pantryItems.addAll(databaseHelper.getAllPantryItems());

            pantryAdapter.notifyDataSetChanged();
        }
    }
}