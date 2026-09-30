package com.example.smartpantrymanager;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.example.smartpantrymanager.database.DatabaseHelper;
import android.widget.Toast;
import com.example.smartpantrymanager.models.PantryItem;
import androidx.appcompat.app.AlertDialog;

public class EditIngredient extends AppCompatActivity {

    private TextInputEditText etEditIngredientName;
    private TextInputEditText etEditQuantity;
    private TextInputEditText etEditUnit;
    private TextInputEditText etEditExpiryDate;
    private MaterialButton btnUpdateIngredient;
    private MaterialButton btnDeleteIngredient;
    private DatabaseHelper databaseHelper;
    private int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_ingredient);

        // Connects the input fields and buttons to the layout
        etEditIngredientName = findViewById(R.id.etEditIngredientName);
        etEditQuantity = findViewById(R.id.etEditQuantity);
        etEditUnit = findViewById(R.id.etEditUnit);
        etEditExpiryDate = findViewById(R.id.etEditExpiryDate);

        btnUpdateIngredient = findViewById(R.id.btnUpdateIngredient);
        btnDeleteIngredient = findViewById(R.id.btnDeleteIngredient);

        // Initialises the database
        databaseHelper = new DatabaseHelper(this);

        // Gets the selected ingredient details from MainActivity
        itemId = getIntent().getIntExtra("ITEM_ID", -1);

        String name = getIntent().getStringExtra("ITEM_NAME");
        double quantity = getIntent().getDoubleExtra("ITEM_QUANTITY", 0);
        String unit = getIntent().getStringExtra("ITEM_UNIT");
        String expiryDate = getIntent().getStringExtra("ITEM_EXPIRY");

        // Displays the existing ingredient details
        etEditIngredientName.setText(name);
        etEditQuantity.setText(String.valueOf(quantity));
        etEditUnit.setText(unit);
        etEditExpiryDate.setText(expiryDate);

        btnUpdateIngredient.setOnClickListener(v -> updateIngredient());
        btnDeleteIngredient.setOnClickListener(v -> showDeleteConfirmation());
    }

    private void updateIngredient() {

        String name = etEditIngredientName.getText().toString().trim();
        String quantityText = etEditQuantity.getText().toString().trim();
        String unit = etEditUnit.getText().toString().trim();
        String expiryDate = etEditExpiryDate.getText().toString().trim();

        // Validates the ingredient name
        if (name.isEmpty()) {
            etEditIngredientName.setError("Ingredient name is required");
            etEditIngredientName.requestFocus();
            return;
        }

        // Validates the quantity
        if (quantityText.isEmpty()) {
            etEditQuantity.setError("Quantity is required");
            etEditQuantity.requestFocus();
            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            etEditQuantity.setError("Enter a valid quantity");
            etEditQuantity.requestFocus();
            return;
        }

        if (quantity <= 0) {
            etEditQuantity.setError("Quantity must be greater than 0");
            etEditQuantity.requestFocus();
            return;
        }

        // Validates the unit
        if (unit.isEmpty()) {
            etEditUnit.setError("Unit is required");
            etEditUnit.requestFocus();
            return;
        }

        PantryItem updatedItem =
                new PantryItem(
                        itemId,
                        name,
                        quantity,
                        unit,
                        expiryDate
                );

        int rowsAffected =
                databaseHelper.updatePantryItem(updatedItem);

        if (rowsAffected > 0) {
            Toast.makeText(
                    this,
                    "Ingredient updated successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
        } else {
            Toast.makeText(
                    this,
                    "Failed to update ingredient",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void showDeleteConfirmation() {

        new AlertDialog.Builder(this)
                .setTitle("Delete Ingredient")
                .setMessage("Are you sure you want to delete this ingredient?")
                .setPositiveButton("Delete", (dialog, which) -> deleteIngredient())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteIngredient() {

        int rowsDeleted = databaseHelper.deletePantryItem(itemId);

        if (rowsDeleted > 0) {
            Toast.makeText(
                    this,
                    "Ingredient deleted successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
        } else {
            Toast.makeText(
                    this,
                    "Failed to delete ingredient",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
