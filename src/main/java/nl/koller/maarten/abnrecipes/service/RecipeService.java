package nl.koller.maarten.abnrecipes.service;

import nl.koller.maarten.abnrecipes.model.NumericSearchCriteria;
import nl.koller.maarten.abnrecipes.model.Recipe;
import nl.koller.maarten.abnrecipes.model.RecipeSearchRequest;
import nl.koller.maarten.abnrecipes.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @Transactional
    public List<Recipe> addRecipes(List<Recipe> recipes) {
        if (recipes == null) {
            return new ArrayList<>(); // Return empty list if input is null
        }

        List<Recipe> savedRecipes = new ArrayList<>();

        for (Recipe recipe : recipes) {
            // Check if a recipe with the same name already exists
            Optional<Recipe> existingRecipe = recipeRepository.findByName(recipe.getName());

            if (existingRecipe.isEmpty()) {
                // Ensure ID is null to generate a new one
                recipe.setId(null);
                savedRecipes.add(recipeRepository.save(recipe));
            } else {
                // Skip this recipe or update it if needed
                // For now, we'll skip it
                System.out.println("Recipe with name '" + recipe.getName() + "' already exists. Skipping.");
            }
        }

        return savedRecipes;
    }

    @Transactional
    public boolean deleteRecipe(Long id) {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public Recipe updateRecipe(Long id, Recipe updatedRecipe) {
        if (updatedRecipe == null) {
            return null;
        }

        return recipeRepository.findById(id)
                .map(existingRecipe -> {
                    // Update the existing recipe with new values
                    existingRecipe.setName(updatedRecipe.getName());
                    existingRecipe.setVegetarian(updatedRecipe.isVegetarian());
                    existingRecipe.setPrepTime(updatedRecipe.getPrepTime());
                    existingRecipe.setCookTime(updatedRecipe.getCookTime());
                    existingRecipe.setIngredients(updatedRecipe.getIngredients());
                    existingRecipe.setInstructions(updatedRecipe.getInstructions());
                    existingRecipe.setServings(updatedRecipe.getServings());

                    // Save and return the updated recipe
                    return recipeRepository.save(existingRecipe);
                })
                .orElse(null);
    }

    public List<Recipe> searchRecipes(RecipeSearchRequest searchRequest) {
        // Handle null search request by returning all recipes
        if (searchRequest == null) {
            return recipeRepository.findAll();
        }

        List<Recipe> allRecipes = recipeRepository.findAll();

        return allRecipes.stream()
                .filter(recipe -> matchesTextSearch(recipe, searchRequest.getTextSearch()))
                .filter(recipe -> filterByVegetarian(recipe, searchRequest.getVegetarian()))
                .filter(recipe -> matchesNumericCriteria(recipe.getServings(), searchRequest.getServings()))
                .filter(recipe -> matchesNumericCriteria(recipe.getPrepTime(), searchRequest.getPrepTime()))
                .filter(recipe -> matchesNumericCriteria(recipe.getCookTime(), searchRequest.getCookTime()))
                .collect(Collectors.toList());
    }

    // Helper methods for search
    private boolean matchesTextSearch(Recipe recipe, String textSearch) {
        if (textSearch == null || textSearch.isEmpty()) {
            return true; // No filter applied
        }

        String searchLower = textSearch.toLowerCase();

        // Check if the text appears in name, instructions, or any ingredient
        if (recipe.getName() != null && recipe.getName().toLowerCase().contains(searchLower)) {
            return true;
        }

        if (recipe.getInstructions() != null && recipe.getInstructions().toLowerCase().contains(searchLower)) {
            return true;
        }

        if (recipe.getIngredients() != null) {
            for (String ingredient : recipe.getIngredients()) {
                if (ingredient.toLowerCase().contains(searchLower)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean matchesNumericCriteria(int value, NumericSearchCriteria criteria) {
        if (criteria == null || criteria.getValue() == null) {
            return true; // No filter applied
        }

        int criteriaValue = criteria.getValue();

        switch (criteria.getComparisonType()) {
            case EXACT -> {
                return value == criteriaValue;
            }
            case MINIMUM -> {
                return value >= criteriaValue;
            }
            case MAXIMUM -> {
                return value <= criteriaValue;
            }
            case GREATER -> {
                return value > criteriaValue;
            }
            case LESS -> {
                return value < criteriaValue;
            }
            default -> {
                return true; // Unknown comparison type, no filter applied
            }
        }
    }

    // Original helper methods (kept for backward compatibility)
    private boolean filterByVegetarian(Recipe recipe, Boolean vegetarian) {
        if (vegetarian == null) {
            return true; // No filter applied
        }
        return recipe.isVegetarian() == vegetarian;
    }
}