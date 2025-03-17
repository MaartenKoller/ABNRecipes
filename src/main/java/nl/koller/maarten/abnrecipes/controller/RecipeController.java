package nl.koller.maarten.abnrecipes.controller;

import nl.koller.maarten.abnrecipes.model.Recipe;
import nl.koller.maarten.abnrecipes.model.RecipeRequest;
import nl.koller.maarten.abnrecipes.model.RecipeSearchRequest;
import nl.koller.maarten.abnrecipes.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        List<Recipe> recipes = recipeService.getAllRecipes();
        return ResponseEntity.ok(recipes);
    }

    @PostMapping
    public ResponseEntity<List<Recipe>> addRecipes(@RequestBody RecipeRequest request) {
        List<Recipe> addedRecipes = recipeService.addRecipes(request.getRecipes());
        return ResponseEntity.status(HttpStatus.CREATED).body(addedRecipes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id, @RequestBody Recipe recipe) {
        Recipe updatedRecipe = recipeService.updateRecipe(id, recipe);
        if (updatedRecipe != null) {
            return ResponseEntity.ok(updatedRecipe);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        boolean deleted = recipeService.deleteRecipe(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/search")
    public ResponseEntity<List<Recipe>> searchRecipes(@RequestBody RecipeSearchRequest searchRequest) {
        return ResponseEntity.ok(recipeService.searchRecipes(searchRequest));
    }
}