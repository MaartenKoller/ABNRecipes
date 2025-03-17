package nl.koller.maarten.abnrecipes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import nl.koller.maarten.abnrecipes.model.Recipe;
import nl.koller.maarten.abnrecipes.model.RecipeRequest;
import nl.koller.maarten.abnrecipes.model.RecipeSearchRequest;
import nl.koller.maarten.abnrecipes.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing recipes.
 * Provides endpoints for creating, reading, updating, and deleting recipes,
 * as well as searching recipes based on various criteria.
 */
@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/recipes")
@Tag(name = "Recipe Controller", description = "API for recipe management operations")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    /**
     * Retrieves all recipes from the database.
     *
     * @return List of all recipes
     */
    @Operation(summary = "Get all recipes", description = "Retrieves a list of all available recipes")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all recipes",
            content = @Content(schema = @Schema(implementation = Recipe.class)))
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        List<Recipe> recipes = recipeService.getAllRecipes();
        return ResponseEntity.ok(recipes);
    }

    /**
     * Adds one or more recipes to the database.
     *
     * @param request The request containing a list of recipes to add
     * @return List of added recipes with generated IDs
     */
    @Operation(summary = "Add new recipes", description = "Adds one or more new recipes to the database")
    @ApiResponse(responseCode = "201", description = "Recipes successfully created",
            content = @Content(schema = @Schema(implementation = Recipe.class)))
    @PostMapping
    public ResponseEntity<List<Recipe>> addRecipes(@RequestBody RecipeRequest request) {
        List<Recipe> addedRecipes = recipeService.addRecipes(request.getRecipes());
        return ResponseEntity.status(HttpStatus.CREATED).body(addedRecipes);
    }

    /**
     * Updates an existing recipe.
     *
     * @param id     The ID of the recipe to update
     * @param recipe The updated recipe data
     * @return The updated recipe if found, or 404 if not found
     */
    @Operation(summary = "Update a recipe", description = "Updates an existing recipe by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe successfully updated",
                    content = @Content(schema = @Schema(implementation = Recipe.class))),
            @ApiResponse(responseCode = "404", description = "Recipe not found",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(
            @Parameter(description = "ID of the recipe to update") @PathVariable Long id,
            @RequestBody Recipe recipe) {
        Recipe updatedRecipe = recipeService.updateRecipe(id, recipe);
        if (updatedRecipe != null) {
            return ResponseEntity.ok(updatedRecipe);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a recipe by its ID.
     *
     * @param id The ID of the recipe to delete
     * @return 204 No Content if deleted successfully, or 404 if not found
     */
    @Operation(summary = "Delete a recipe", description = "Deletes a recipe by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Recipe successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Recipe not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(
            @Parameter(description = "ID of the recipe to delete") @PathVariable Long id) {
        boolean deleted = recipeService.deleteRecipe(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Searches for recipes based on advanced criteria.
     *
     * @param searchRequest The search criteria
     * @return List of recipes matching the search criteria
     */
    @Operation(summary = "Search recipes", description = "Searches for recipes based on various criteria")
    @ApiResponse(responseCode = "200", description = "Search completed successfully",
            content = @Content(schema = @Schema(implementation = Recipe.class)))
    @PostMapping("/search")
    public ResponseEntity<List<Recipe>> searchRecipes(@RequestBody RecipeSearchRequest searchRequest) {
        return ResponseEntity.ok(recipeService.searchRecipes(searchRequest));
    }
}
