package com.example.smartpantrymanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import com.example.smartpantrymanager.models.PantryItem;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database information
    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    // Pantry table
    public static final String TABLE_PANTRY = "pantry_items";

    // Pantry table columns
    public static final String COLUMN_PANTRY_ID = "id";
    public static final String COLUMN_PANTRY_NAME = "name";
    public static final String COLUMN_PANTRY_QUANTITY = "quantity";
    public static final String COLUMN_PANTRY_UNIT = "unit";
    public static final String COLUMN_PANTRY_EXPIRY = "expiry_date";

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);

        onCreate(db);
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
}