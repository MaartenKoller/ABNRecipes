package nl.koller.maarten.abnrecipes.service;

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
        List<Recipe> allRecipes = recipeRepository.findAll();

        return allRecipes.stream()
                .filter(recipe -> filterByVegetarian(recipe, searchRequest.getVegetarian()))
                .filter(recipe -> filterByMinServings(recipe, searchRequest.getMinServings()))
                .filter(recipe -> filterByMaxPrepTime(recipe, searchRequest.getMaxPrepTime()))
                .filter(recipe -> filterByMaxCookTime(recipe, searchRequest.getMaxCookTime()))
                .filter(recipe -> filterByIncludeIngredients(recipe, searchRequest.getIncludeIngredients()))
                .filter(recipe -> filterByExcludeIngredients(recipe, searchRequest.getExcludeIngredients()))
                .filter(recipe -> filterByInstructionsContain(recipe, searchRequest.getInstructionsContain()))
                .collect(Collectors.toList());
    }

    private boolean filterByVegetarian(Recipe recipe, Boolean vegetarian) {
        if (vegetarian == null) {
            return true; // No filter applied
        }
        return recipe.isVegetarian() == vegetarian;
    }

    private boolean filterByMinServings(Recipe recipe, Integer minServings) {
        if (minServings == null) {
            return true; // No filter applied
        }
        return recipe.getServings() >= minServings;
    }

    private boolean filterByMaxPrepTime(Recipe recipe, Integer maxPrepTime) {
        if (maxPrepTime == null) {
            return true; // No filter applied
        }
        return recipe.getPrepTime() <= maxPrepTime;
    }

    private boolean filterByMaxCookTime(Recipe recipe, Integer maxCookTime) {
        if (maxCookTime == null) {
            return true; // No filter applied
        }
        return recipe.getCookTime() <= maxCookTime;
    }

    private boolean filterByIncludeIngredients(Recipe recipe, List<String> includeIngredients) {
        if (includeIngredients == null || includeIngredients.isEmpty()) {
            return true; // No filter applied
        }

        List<String> recipeIngredients = recipe.getIngredients();
        return includeIngredients.stream()
                .allMatch(ingredient ->
                        recipeIngredients.stream()
                                .anyMatch(recipeIngredient ->
                                        recipeIngredient.toLowerCase().contains(ingredient.toLowerCase())
                                )
                );
    }

    private boolean filterByExcludeIngredients(Recipe recipe, List<String> excludeIngredients) {
        if (excludeIngredients == null || excludeIngredients.isEmpty()) {
            return true; // No filter applied
        }

        List<String> recipeIngredients = recipe.getIngredients();
        return excludeIngredients.stream()
                .noneMatch(ingredient ->
                        recipeIngredients.stream()
                                .anyMatch(recipeIngredient ->
                                        recipeIngredient.toLowerCase().contains(ingredient.toLowerCase())
                                )
                );
    }

    private boolean filterByInstructionsContain(Recipe recipe, String instructionsContain) {
        if (instructionsContain == null || instructionsContain.isEmpty()) {
            return true; // No filter applied
        }

        return recipe.getInstructions().toLowerCase().contains(instructionsContain.toLowerCase());
    }
}