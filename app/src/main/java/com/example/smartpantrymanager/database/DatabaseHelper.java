package com.example.smartpantrymanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import com.example.smartpantrymanager.models.PantryItem;
import java.util.ArrayList;
import java.util.List;
import com.example.smartpantrymanager.models.Recipe;
import com.example.smartpantrymanager.models.RecipeIngredient;
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database information
    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 2;

    // Pantry table
    public static final String TABLE_PANTRY = "pantry_items";

    // Pantry table columns
    public static final String COLUMN_PANTRY_ID = "id";
    public static final String COLUMN_PANTRY_NAME = "name";
    public static final String COLUMN_PANTRY_QUANTITY = "quantity";
    public static final String COLUMN_PANTRY_UNIT = "unit";
    public static final String COLUMN_PANTRY_EXPIRY = "expiry_date";

    // Recipe table
    public static final String TABLE_RECIPE = "recipes";
    public static final String COLUMN_RECIPE_ID = "id";
    public static final String COLUMN_RECIPE_NAME = "name";
    public static final String COLUMN_RECIPE_INSTRUCTIONS = "instructions";

    // Recipe ingredients table
    public static final String TABLE_RECIPE_INGREDIENT = "recipe_ingredients";
    public static final String COLUMN_RECIPE_INGREDIENT_ID = "id";
    public static final String COLUMN_RECIPE_INGREDIENT_RECIPE_ID = "recipe_id";
    public static final String COLUMN_RECIPE_INGREDIENT_NAME = "ingredient_name";
    public static final String COLUMN_RECIPE_INGREDIENT_QUANTITY = "quantity";
    public static final String COLUMN_RECIPE_INGREDIENT_UNIT = "unit";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createPantryTable =
                "CREATE TABLE " + TABLE_PANTRY + " (" +
                        COLUMN_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_PANTRY_NAME + " TEXT NOT NULL, " +
                        COLUMN_PANTRY_QUANTITY + " REAL NOT NULL, " +
                        COLUMN_PANTRY_UNIT + " TEXT NOT NULL, " +
                        COLUMN_PANTRY_EXPIRY + " TEXT" +
                        ")";

        db.execSQL(createPantryTable);

        String createRecipeTable =
                "CREATE TABLE " + TABLE_RECIPE + " (" +
                        COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                        COLUMN_RECIPE_INSTRUCTIONS + " TEXT NOT NULL" +
                        ")";

        db.execSQL(createRecipeTable);


        String createRecipeIngredientTable =
                "CREATE TABLE " + TABLE_RECIPE_INGREDIENT + " (" +
                        COLUMN_RECIPE_INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_RECIPE_INGREDIENT_RECIPE_ID + " INTEGER NOT NULL, " +
                        COLUMN_RECIPE_INGREDIENT_NAME + " TEXT NOT NULL, " +
                        COLUMN_RECIPE_INGREDIENT_QUANTITY + " REAL NOT NULL, " +
                        COLUMN_RECIPE_INGREDIENT_UNIT + " TEXT NOT NULL, " +
                        "FOREIGN KEY (" + COLUMN_RECIPE_INGREDIENT_RECIPE_ID + ") " +
                        "REFERENCES " + TABLE_RECIPE + "(" + COLUMN_RECIPE_ID + ")" +
                        ")";

        db.execSQL(createRecipeIngredientTable);

        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Upgrade from version 1 to version 2
        if (oldVersion < 2) {

            String createRecipeTable =
                    "CREATE TABLE " + TABLE_RECIPE + " (" +
                            COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                            COLUMN_RECIPE_INSTRUCTIONS + " TEXT NOT NULL" +
                            ")";

            db.execSQL(createRecipeTable);


            String createRecipeIngredientTable =
                    "CREATE TABLE " + TABLE_RECIPE_INGREDIENT + " (" +
                            COLUMN_RECIPE_INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            COLUMN_RECIPE_INGREDIENT_RECIPE_ID + " INTEGER NOT NULL, " +
                            COLUMN_RECIPE_INGREDIENT_NAME + " TEXT NOT NULL, " +
                            COLUMN_RECIPE_INGREDIENT_QUANTITY + " REAL NOT NULL, " +
                            COLUMN_RECIPE_INGREDIENT_UNIT + " TEXT NOT NULL, " +
                            "FOREIGN KEY (" + COLUMN_RECIPE_INGREDIENT_RECIPE_ID + ") " +
                            "REFERENCES " + TABLE_RECIPE + "(" + COLUMN_RECIPE_ID + ")" +
                            ")";

            db.execSQL(createRecipeIngredientTable);

            seedRecipes(db);
        }
    }

    // CREATE - Add a new pantry item
    public long addPantryItem(PantryItem item) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PANTRY_NAME, item.getName());
        values.put(COLUMN_PANTRY_QUANTITY, item.getQuantity());
        values.put(COLUMN_PANTRY_UNIT, item.getUnit());
        values.put(COLUMN_PANTRY_EXPIRY, item.getExpiryDate());

        long id = db.insert(TABLE_PANTRY, null, values);

        db.close();

        return id;
    }

    // READ - Retrieve all pantry items
    public List<PantryItem> getAllPantryItems() {

        List<PantryItem> pantryItems = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_PANTRY,
                null,
                null,
                null,
                null,
                null,
                COLUMN_PANTRY_NAME + " ASC"
        );

        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_PANTRY_ID)
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_PANTRY_NAME)
                );

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(COLUMN_PANTRY_QUANTITY)
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_PANTRY_UNIT)
                );

                String expiryDate = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_PANTRY_EXPIRY)
                );

                PantryItem item = new PantryItem(
                        id,
                        name,
                        quantity,
                        unit,
                        expiryDate
                );

                pantryItems.add(item);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return pantryItems;
    }

    // UPDATE - Update an existing pantry item
    public int updatePantryItem(PantryItem item) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PANTRY_NAME, item.getName());
        values.put(COLUMN_PANTRY_QUANTITY, item.getQuantity());
        values.put(COLUMN_PANTRY_UNIT, item.getUnit());
        values.put(COLUMN_PANTRY_EXPIRY, item.getExpiryDate());

        int rowsAffected = db.update(
                TABLE_PANTRY,
                values,
                COLUMN_PANTRY_ID + " = ?",
                new String[]{String.valueOf(item.getId())}
        );

        db.close();

        return rowsAffected;
    }

    // DELETE - Delete a pantry item
    public int deletePantryItem(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        int rowsDeleted = db.delete(
                TABLE_PANTRY,
                COLUMN_PANTRY_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        db.close();

        return rowsDeleted;
    }

    private long insertRecipe(
            SQLiteDatabase db,
            String name,
            String instructions
    ) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_RECIPE_NAME, name);
        values.put(COLUMN_RECIPE_INSTRUCTIONS, instructions);

        return db.insert(TABLE_RECIPE, null, values);
    }

    private void insertRecipeIngredient(
            SQLiteDatabase db,
            long recipeId,
            String ingredientName,
            double quantity,
            String unit
    ) {
        ContentValues values = new ContentValues();

        values.put(
                COLUMN_RECIPE_INGREDIENT_RECIPE_ID,
                recipeId
        );

        values.put(
                COLUMN_RECIPE_INGREDIENT_NAME,
                ingredientName
        );

        values.put(
                COLUMN_RECIPE_INGREDIENT_QUANTITY,
                quantity
        );

        values.put(
                COLUMN_RECIPE_INGREDIENT_UNIT,
                unit
        );

        db.insert(
                TABLE_RECIPE_INGREDIENT,
                null,
                values
        );
    }

    private void seedRecipes(SQLiteDatabase db) {

        // 1. Scrambled Eggs
        long scrambledEggsId = insertRecipe(
                db,
                "Scrambled Eggs",
                "Beat the eggs with the milk. Melt the butter in a pan. " +
                        "Add the egg mixture and stir until satisfied."
        );

        insertRecipeIngredient(db, scrambledEggsId, "Eggs", 3, "units");
        insertRecipeIngredient(db, scrambledEggsId, "Milk", 50, "ml");
        insertRecipeIngredient(db, scrambledEggsId, "Butter", 20, "g");


        // 2. Buttered Rice
        long butteredRiceId = insertRecipe(
                db,
                "Buttered Rice",
                "Cook the rice until soft. Drain if necessary, " +
                        "then thoroughly mix with butter until melted."
        );

        insertRecipeIngredient(db, butteredRiceId, "Rice", 200, "g");
        insertRecipeIngredient(db, butteredRiceId, "Butter", 20, "g");


        // 3. Garlic Prawns
        long garlicPrawnsId = insertRecipe(
                db,
                "Garlic Prawns",
                "Melt the butter in a pan. Add the garlic and cook briefly. " +
                        "Add the prawns and cook until fully done."
        );

        insertRecipeIngredient(db, garlicPrawnsId, "Prawns", 250, "g");
        insertRecipeIngredient(db, garlicPrawnsId, "Butter", 30, "g");
        insertRecipeIngredient(db, garlicPrawnsId, "Garlic", 10, "g");


        // 4. Cheese Omelette
        long cheeseOmeletteId = insertRecipe(
                db,
                "Cheese Omelette",
                "Beat the eggs and pour them into a heated pan with butter. " +
                        "Add the cheese, fold the omelette and cook until set."
        );

        insertRecipeIngredient(db, cheeseOmeletteId, "Eggs", 3, "units");
        insertRecipeIngredient(db, cheeseOmeletteId, "Cheese", 50, "g");
        insertRecipeIngredient(db, cheeseOmeletteId, "Butter", 10, "g");


        // 5. French Toast
        long frenchToastId = insertRecipe(
                db,
                "French Toast",
                "Beat the eggs and milk together. Dip the bread into the mixture. " +
                        "Melt butter in a pan and fry the bread until golden."
        );

        insertRecipeIngredient(db, frenchToastId, "Bread", 2, "slices");
        insertRecipeIngredient(db, frenchToastId, "Eggs", 2, "units");
        insertRecipeIngredient(db, frenchToastId, "Milk", 50, "ml");
        insertRecipeIngredient(db, frenchToastId, "Butter", 10, "g");


        // 6. Garlic Bread
        long garlicBreadId = insertRecipe(
                db,
                "Garlic Bread",
                "Mix the garlic with softened butter. Spread over the bread " +
                        "and toast until golden and crisp."
        );

        insertRecipeIngredient(db, garlicBreadId, "Bread", 4, "slices");
        insertRecipeIngredient(db, garlicBreadId, "Butter", 30, "g");
        insertRecipeIngredient(db, garlicBreadId, "Garlic", 10, "g");


        // 7. Mashed Potatoes
        long mashedPotatoesId = insertRecipe(
                db,
                "Mashed Potatoes",
                "Boil the potatoes until soft. Drain and mash with the butter " +
                        "and milk until smooth."
        );

        insertRecipeIngredient(db, mashedPotatoesId, "Potatoes", 500, "g");
        insertRecipeIngredient(db, mashedPotatoesId, "Butter", 30, "g");
        insertRecipeIngredient(db, mashedPotatoesId, "Milk", 100, "ml");


        // 8. Tuna Sandwich
        long tunaSandwichId = insertRecipe(
                db,
                "Tuna Sandwich",
                "Spread mayonnaise onto the bread. Add the tuna and cheese, " +
                        "then close the sandwich and serve."
        );

        insertRecipeIngredient(db, tunaSandwichId, "Bread", 2, "slices");
        insertRecipeIngredient(db, tunaSandwichId, "Tuna", 100, "g");
        insertRecipeIngredient(db, tunaSandwichId, "Mayonnaise", 20, "g");
        insertRecipeIngredient(db, tunaSandwichId, "Cheese", 30, "g");


        // 9. Egg Fried Rice
        long eggFriedRiceId = insertRecipe(
                db,
                "Egg Fried Rice",
                "Cook the egg in a pan and break it into small pieces. " +
                        "Add the cooked rice and soy sauce, then stir-fry together."
        );

        insertRecipeIngredient(db, eggFriedRiceId, "Rice", 200, "g");
        insertRecipeIngredient(db, eggFriedRiceId, "Eggs", 2, "units");
        insertRecipeIngredient(db, eggFriedRiceId, "Soy Sauce", 20, "ml");


        // 10. Tomato Pasta
        long tomatoPastaId = insertRecipe(
                db,
                "Tomato Pasta",
                "Cook the pasta until soft. Heat the tomato sauce with garlic, " +
                        "then combine with the drained pasta."
        );

        insertRecipeIngredient(db, tomatoPastaId, "Pasta", 200, "g");
        insertRecipeIngredient(db, tomatoPastaId, "Tomato Sauce", 150, "ml");
        insertRecipeIngredient(db, tomatoPastaId, "Garlic", 10, "g");


        // 11. Cheese Toast
        long cheeseToastId = insertRecipe(
                db,
                "Cheese Toast",
                "Place the cheese onto the bread and toast until the bread is crisp " +
                        "and the cheese has melted."
        );

        insertRecipeIngredient(db, cheeseToastId, "Bread", 2, "slices");
        insertRecipeIngredient(db, cheeseToastId, "Cheese", 60, "g");


        // 12. Boiled Eggs on Toast
        long eggsOnToastId = insertRecipe(
                db,
                "Boiled Eggs on Toast",
                "Boil the eggs until cooked. Toast the bread, peel and slice " +
                        "the eggs, then place them on top of the toast."
        );

        insertRecipeIngredient(db, eggsOnToastId, "Eggs", 2, "units");
        insertRecipeIngredient(db, eggsOnToastId, "Bread", 2, "slices");


        // 13. Creamy Pasta
        long creamyPastaId = insertRecipe(
                db,
                "Creamy Pasta",
                "Cook the pasta until soft. Heat the cream gently, add the cheese " +
                        "and stir until melted. Combine with the pasta."
        );

        insertRecipeIngredient(db, creamyPastaId, "Pasta", 200, "g");
        insertRecipeIngredient(db, creamyPastaId, "Cream", 150, "ml");
        insertRecipeIngredient(db, creamyPastaId, "Cheese", 50, "g");


        // 14. Chicken and Rice
        long chickenRiceId = insertRecipe(
                db,
                "Chicken and Rice",
                "Cook the rice until soft. Cook the chicken thoroughly in a pan, " +
                        "then serve it together with the rice."
        );

        insertRecipeIngredient(db, chickenRiceId, "Chicken", 250, "g");
        insertRecipeIngredient(db, chickenRiceId, "Rice", 200, "g");


        // 15. Chicken Pasta
        long chickenPastaId = insertRecipe(
                db,
                "Chicken Pasta",
                "Cook the pasta until soft. Cook the chicken thoroughly, " +
                        "heat the tomato sauce and combine all ingredients."
        );

        insertRecipeIngredient(db, chickenPastaId, "Chicken", 250, "g");
        insertRecipeIngredient(db, chickenPastaId, "Pasta", 200, "g");
        insertRecipeIngredient(db, chickenPastaId, "Tomato Sauce", 150, "ml");


        // 16. Potato and Egg Hash
        long potatoEggHashId = insertRecipe(
                db,
                "Potato and Egg Hash",
                "Cook the potatoes in butter until golden and tender. " +
                        "Add the eggs and cook until the eggs are set."
        );

        insertRecipeIngredient(db, potatoEggHashId, "Potatoes", 300, "g");
        insertRecipeIngredient(db, potatoEggHashId, "Eggs", 2, "units");
        insertRecipeIngredient(db, potatoEggHashId, "Butter", 20, "g");


        // 17. Cheesy Mashed Potatoes
        long cheesyMashId = insertRecipe(
                db,
                "Cheesy Mashed Potatoes",
                "Boil the potatoes until soft. Mash with the butter and milk, " +
                        "then stir in the cheese until melted."
        );

        insertRecipeIngredient(db, cheesyMashId, "Potatoes", 500, "g");
        insertRecipeIngredient(db, cheesyMashId, "Butter", 30, "g");
        insertRecipeIngredient(db, cheesyMashId, "Milk", 100, "ml");
        insertRecipeIngredient(db, cheesyMashId, "Cheese", 50, "g");


        // 18. Tuna Pasta
        long tunaPastaId = insertRecipe(
                db,
                "Tuna Pasta",
                "Cook the pasta until soft. Drain it and combine with the tuna " +
                        "and mayonnaise until evenly mixed."
        );

        insertRecipeIngredient(db, tunaPastaId, "Pasta", 200, "g");
        insertRecipeIngredient(db, tunaPastaId, "Tuna", 100, "g");
        insertRecipeIngredient(db, tunaPastaId, "Mayonnaise", 30, "g");


        // 19. Cheese and Egg Sandwich
        long cheeseEggSandwichId = insertRecipe(
                db,
                "Cheese and Egg Sandwich",
                "Cook the eggs to your preference. Place the eggs and cheese " +
                        "between the bread slices."
        );

        insertRecipeIngredient(db, cheeseEggSandwichId, "Bread", 2, "slices");
        insertRecipeIngredient(db, cheeseEggSandwichId, "Eggs", 2, "units");
        insertRecipeIngredient(db, cheeseEggSandwichId, "Cheese", 40, "g");


        // 20. Garlic Butter Chicken
        long garlicButterChickenId = insertRecipe(
                db,
                "Garlic Butter Chicken",
                "Melt the butter in a pan and cook the garlic briefly. " +
                        "Add the chicken and cook thoroughly until golden."
        );

        insertRecipeIngredient(db, garlicButterChickenId, "Chicken", 250, "g");
        insertRecipeIngredient(db, garlicButterChickenId, "Butter", 30, "g");
        insertRecipeIngredient(db, garlicButterChickenId, "Garlic", 10, "g");
    }

    public List<RecipeIngredient> getRecipeIngredients(int recipeId) {

        List<RecipeIngredient> ingredients = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RECIPE_INGREDIENT,
                null,
                COLUMN_RECIPE_INGREDIENT_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_RECIPE_INGREDIENT_ID
                        )
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_RECIPE_INGREDIENT_NAME
                        )
                );

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_RECIPE_INGREDIENT_QUANTITY
                        )
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_RECIPE_INGREDIENT_UNIT
                        )
                );

                ingredients.add(
                        new RecipeIngredient(
                                id,
                                recipeId,
                                name,
                                quantity,
                                unit
                        )
                );

            } while (cursor.moveToNext());
        }

        cursor.close();

        return ingredients;
    }

    private boolean canMakeRecipe(
            Recipe recipe,
            List<PantryItem> pantryItems
    ) {

        List<RecipeIngredient> requiredIngredients =
                getRecipeIngredients(recipe.getId());

        for (RecipeIngredient required : requiredIngredients) {

            boolean ingredientFound = false;

            for (PantryItem pantryItem : pantryItems) {

                if (pantryItem.getName().trim().equalsIgnoreCase(
                        required.getIngredientName().trim()
                )) {

                    if (pantryItem.getUnit().trim().equalsIgnoreCase(
                            required.getUnit().trim()
                    )) {

                        if (pantryItem.getQuantity()
                                >= required.getQuantity()) {

                            ingredientFound = true;
                            break;
                        }
                    }
                }
            }

            // Even one missing ingredient disqualifies the recipe
            if (!ingredientFound) {
                return false;
            }
        }

        return true;
    }

    public List<Recipe> getSuggestedRecipes() {

        List<Recipe> suggestedRecipes = new ArrayList<>();
        List<PantryItem> pantryItems = getAllPantryItems();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RECIPE,
                null,
                null,
                null,
                null,
                null,
                COLUMN_RECIPE_NAME + " ASC"
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_RECIPE_ID
                        )
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_RECIPE_NAME
                        )
                );

                String instructions = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_RECIPE_INSTRUCTIONS
                        )
                );

                Recipe recipe = new Recipe(
                        id,
                        name,
                        instructions
                );

                // Only adds the recipe if every required
                // ingredient is available in the pantry
                if (canMakeRecipe(recipe, pantryItems)) {
                    suggestedRecipes.add(recipe);
                }

            } while (cursor.moveToNext());
        }

        cursor.close();

        return suggestedRecipes;
    }
}