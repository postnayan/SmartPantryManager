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

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPantry;
    private PantryAdapter pantryAdapter;
    private DatabaseHelper databaseHelper;
    private List<PantryItem> pantryItems;

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

        // Get the RecyclerView from the layout
        recyclerViewPantry = findViewById(R.id.recyclerViewPantry);

        // Set how the RecyclerView arranges the items
        recyclerViewPantry.setLayoutManager(new LinearLayoutManager(this));

        // Retrieves all pantry items from SQLite
        pantryItems = databaseHelper.getAllPantryItems();

        // Creates the adapter
        pantryAdapter = new PantryAdapter(pantryItems);

        // Connect the adapter to the RecyclerView
        recyclerViewPantry.setAdapter(pantryAdapter);
    }
}