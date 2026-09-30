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

        // Recipe 1 - Scrambled Eggs
        long scrambledEggsId = insertRecipe(
                db,
                "Scrambled Eggs",
                "Beat the eggs with the milk. Melt the butter in a frying pan. " +
                        "Add the egg mixture and cook gently while stirring to your satisfaction."
        );

        insertRecipeIngredient(
                db,
                scrambledEggsId,
                "Eggs",
                3,
                "units"
        );

        insertRecipeIngredient(
                db,
                scrambledEggsId,
                "Milk",
                50,
                "ml"
        );

        insertRecipeIngredient(
                db,
                scrambledEggsId,
                "Butter",
                20,
                "g"
        );


        // Recipe 2 - Buttered Rice
        long butteredRiceId = insertRecipe(
                db,
                "Buttered Rice",
                "Cook the rice until soft. Drain if necessary, " +
                        "then mix with the butter while the rice is hot."
        );

        insertRecipeIngredient(
                db,
                butteredRiceId,
                "Rice",
                200,
                "g"
        );

        insertRecipeIngredient(
                db,
                butteredRiceId,
                "Butter",
                20,
                "g"
        );


        // Recipe 3 - Garlic Prawns
        long garlicPrawnsId = insertRecipe(
                db,
                "Garlic Prawns",
                "Melt the butter in a pan. Add the garlic and cook for 5 minutes. " +
                        "Add the prawns and stir occasionally until heated through and fully cooked."
        );

        insertRecipeIngredient(
                db,
                garlicPrawnsId,
                "Prawns",
                250,
                "g"
        );

        insertRecipeIngredient(
                db,
                garlicPrawnsId,
                "Butter",
                30,
                "g"
        );

        insertRecipeIngredient(
                db,
                garlicPrawnsId,
                "Garlic",
                10,
                "g"
        );
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