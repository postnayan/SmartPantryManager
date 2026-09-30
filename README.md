# Smart Pantry Manager

Smart Pantry Manager is an Android application developed in Java that allows users to manage the ingredients available in their pantry and receive recipe suggestions based on those ingredients.

The application uses a local SQLite database to store pantry items, recipes and recipe ingredients. Recipes are suggested only when the user's pantry contains all of the ingredients required by the recipe in sufficient quantities.

## Features

- Add ingredients
- View stored ingredients
- Edit existing ingredients
- Delete ingredients
- Store ingredient quantities and units of measurement
- Store optional expiry dates
- Persistent local storage using SQLite
- 20 preloaded recipes
- Strict recipe matching based on pantry contents
- Suggested Recipes screen
- Feedback when no recipes can be made
- Recipe Detail screen
- View the ingredients required for a recipe
- View recipe preparation instructions

## Recipe Matching

The application uses strict matching when determining which recipes can be made.

For a recipe to be suggested:

1. Every required ingredient must exist in the user's pantry.
2. The ingredient name must match the required ingredient.
3. The measurement unit must match.
4. The available quantity must be greater than or equal to the quantity required by the recipe.

If any required ingredient is missing or insufficient, the recipe is excluded from the Suggested Recipes list.

## Database

The application uses SQLite through Android's `SQLiteOpenHelper`.

SQLite was selected because the application requires structured, persistent local storage and does not require an internet connection or external database server.

The database currently stores three main types of data:

- Pantry items
- Recipes
- Recipe ingredients

Recipes and their required ingredients are related using a recipe ID.

The application includes 20 recipes that are automatically seeded into the database when the database is first created.

## Technologies Used

- Java
- Android Studio
- Android SDK
- SQLite
- SQLiteOpenHelper
- RecyclerView
- Material Design components
- Git
- GitHub

## Application Structure

The application separates its functionality into models, database operations, adapters and Android Activities.

Key components include:

- `PantryItem` - represents a pantry ingredient.
- `Recipe` - represents a recipe.
- `RecipeIngredient` - represents an ingredient required by a recipe.
- `DatabaseHelper` - manages the SQLite database, CRUD operations, recipe data and recipe matching.
- `PantryAdapter` - displays pantry items using RecyclerView.
- `RecipeAdapter` - displays suggested recipes using RecyclerView.
- `MainActivity` - displays the user's pantry.
- `AddIngredient` - allows new pantry items to be added.
- `EditIngredient` - allows existing pantry items to be updated or deleted.
- `SuggestedRecipesActivity` - displays recipes that can be made using the current pantry.
- `RecipeDetailActivity` - displays the selected recipe's ingredients and preparation instructions.

## Pantry CRUD Operations

The application supports the following CRUD operations:

- **Create:** Add a new pantry ingredient.
- **Read:** Display stored pantry ingredients.
- **Update:** Edit an existing pantry ingredient.
- **Delete:** Remove an ingredient after confirmation.

Changes are stored in SQLite and remain available after the application is closed and reopened.

## Current Limitations

The current implementation requires measurement units to match between pantry items and recipe requirements. Automatic unit conversion is not currently implemented.

Ingredient matching is case-insensitive and ignores leading or trailing spaces, but more advanced naming criteria such as automatically treating singular and plural ingredient names as equivalent is not currently implemented.

## Future Improvements

Possible future improvements include:

- Unit conversion between compatible measurements
- Improved ingredient-name normalisation
- Expiry-date notifications
- Settings and user preferences
- Enhanced application navigation
- Recipe search and filtering
- Additional recipe information
- Improved user-interface styling