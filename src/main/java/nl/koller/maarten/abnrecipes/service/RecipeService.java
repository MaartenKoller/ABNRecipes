package nl.koller.maarten.abnrecipes.service;

import nl.koller.maarten.abnrecipes.model.Recipe;
import nl.koller.maarten.abnrecipes.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
}