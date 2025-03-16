package nl.koller.maarten.abnrecipes.controller;

import nl.koller.maarten.abnrecipes.model.Recipe;
import nl.koller.maarten.abnrecipes.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RecipeControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestClient.Builder restClientBuilder;

    @Autowired
    private  RecipeRepository recipeRepository;

    private RestClient restClient;

    // Store the initial state of the database
    private List<Recipe> initialRecipes;


    @BeforeEach
    public void setup() {
        restClient = restClientBuilder.baseUrl("http://localhost:" + port).build();

        // Save the initial state of the database
        initialRecipes = recipeRepository.findAll();
    }

    @Test
    public void testGetAllRecipes() {
        List<Recipe> recipes = getAllRecipes();

        assert recipes != null;
        assertFalse(recipes.isEmpty());
        assertEquals(10, recipes.size());
        assertEquals("Spaghetti Bolognese", recipes.get(0).getName());
        assertEquals("Chicken Alfredo Pasta", recipes.get(6).getName());
        assertEquals("Roasted Ratatouille", recipes.get(9).getName());
    }

    @Test
    public void testAddRecipe() throws IOException {
        List<Recipe> recipes = getAllRecipes();

        assert recipes != null;
        int currentAmount = recipes.size();

        // Check if "Maarten's Toetje" already exists and delete it if it does
        Optional<Recipe> existingRecipe = recipeRepository.findByName("Maarten's Toetje");
        existingRecipe.ifPresent(recipe -> recipeRepository.delete(recipe));

        // Read the JSON file
        ClassPathResource resource = new ClassPathResource("add-recipe.json");
        String requestBody = new String(Files.readAllBytes(resource.getFile().toPath()));

        // Add the new recipe
        List<Recipe> addedRecipes = restClient.post()
                .uri("/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Recipe>>() {
                });

        assert addedRecipes != null;
        assertEquals(1, addedRecipes.size());
        assertEquals("Maarten's Toetje", addedRecipes.getFirst().getName());
        assertTrue(addedRecipes.getFirst().isVegetarian());

        // Verify we now have 11 recipes
        List<Recipe> updatedRecipes = getAllRecipes();

        assert updatedRecipes != null;
        assertEquals(currentAmount+1, updatedRecipes.size());

        // Find the added recipe in the list
        Recipe addedRecipe = updatedRecipes.stream()
                .filter(r -> "Maarten's Toetje".equals(r.getName()))
                .findFirst()
                .orElse(null);

        assertNotNull(addedRecipe);
        assertTrue(addedRecipe.isVegetarian());
        assertEquals(15, addedRecipe.getPrepTime());
        assertEquals(0, addedRecipe.getCookTime());
        assertEquals(6, addedRecipe.getServings());

        // Check some ingredients
        List<String> ingredients = addedRecipe.getIngredients();
        assertTrue(ingredients.contains("1 pint Ben & Jerry's Chocolate Ice Cream"));
        assertTrue(ingredients.contains("1 pint Ben & Jerry's Vanilla Ice Cream"));
        assertTrue(ingredients.contains("1 pint Ben & Jerry's Chocolate Chip Cookie Dough Ice Cream"));

        // Reset the database by removing the added recipe
        Optional<Recipe> maartenToetje = recipeRepository.findByName("Maarten's Toetje");
        maartenToetje.ifPresent(recipe -> recipeRepository.delete(recipe));
    }

    @Test
    public void testDeleteRecipe() {
        List<Recipe> recipes = getAllRecipes();

        assert recipes != null;
        assertFalse(recipes.isEmpty());
        int initialCount = recipes.size();

        // Get the ID of the first recipe
        Long recipeIdToDelete = recipes.getFirst().getId();

        // Delete the recipe
        restClient.delete()
                .uri("/recipes/" + recipeIdToDelete)
                .retrieve()
                .toBodilessEntity();

        // Verify the recipe was deleted
        List<Recipe> remainingRecipes = getAllRecipes();

        assert remainingRecipes != null;
        assertEquals(initialCount - 1, remainingRecipes.size());

        // Verify the deleted recipe is no longer in the list
        boolean recipeStillExists = remainingRecipes.stream()
                .anyMatch(recipe -> recipeIdToDelete.equals(recipe.getId()));
        assertFalse(recipeStillExists);

        // Try to delete a non-existent recipe
        try {
            restClient.delete()
                    .uri("/recipes/999999")
                    .retrieve()
                    .toBodilessEntity();
            fail("Expected HttpClientErrorException.NotFound");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    public void testUpdateRecipe() {
        List<Recipe> recipes = getAllRecipes();
        int updatedPrepTime = 111;
        int updatedCookTime = 222;
        int servingsAmount = 333;

        assert recipes != null;
        assertFalse(recipes.isEmpty());

        // Get the first recipe to update
        Recipe recipeToUpdate = recipes.getFirst();
        Long recipeId = recipeToUpdate.getId();

        // Create an updated version of the recipe
        Recipe updatedRecipeData = new Recipe();
        updatedRecipeData.setId(recipeId);
        updatedRecipeData.setName(recipeToUpdate.getName() + " (Updated)");
        updatedRecipeData.setVegetarian(!recipeToUpdate.isVegetarian()); // Toggle vegetarian status
        updatedRecipeData.setPrepTime(updatedPrepTime);
        updatedRecipeData.setCookTime(updatedCookTime);
        updatedRecipeData.setIngredients(recipeToUpdate.getIngredients());
        updatedRecipeData.setInstructions(recipeToUpdate.getInstructions());
        updatedRecipeData.setServings(servingsAmount);

        // Update the recipe
        Recipe updatedRecipe = restClient.put()
                .uri("/recipes/" + recipeId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(updatedRecipeData)
                .retrieve()
                .body(Recipe.class);

        // Verify the recipe was updated correctly
        assertNotNull(updatedRecipe);
        assertEquals(recipeId, updatedRecipe.getId());
        assertEquals(recipeToUpdate.getName() + " (Updated)", updatedRecipe.getName());
        assertEquals(!recipeToUpdate.isVegetarian(), updatedRecipe.isVegetarian());
        assertEquals(updatedPrepTime, updatedRecipe.getPrepTime());
        assertEquals(updatedCookTime, updatedRecipe.getCookTime());
        assertEquals(servingsAmount, updatedRecipe.getServings());

        // Verify the update is reflected in the database
        List<Recipe> updatedRecipes = getAllRecipes();
        Recipe updatedRecipeInList = updatedRecipes.stream()
                .filter(r -> r.getId().equals(recipeId))
                .findFirst()
                .orElse(null);

        assertNotNull(updatedRecipeInList);
        assertEquals(recipeToUpdate.getName() + " (Updated)", updatedRecipeInList.getName());
        assertEquals(!recipeToUpdate.isVegetarian(), updatedRecipeInList.isVegetarian());
        assertEquals(updatedPrepTime, updatedRecipeInList.getPrepTime());
        assertEquals(updatedCookTime, updatedRecipeInList.getCookTime());
        assertEquals(servingsAmount, updatedRecipeInList.getServings());

        // Try to update a non-existent recipe
        try {
            restClient.put()
                    .uri("/recipes/999999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(updatedRecipeData)
                    .retrieve()
                    .body(Recipe.class);
            fail("Expected HttpClientErrorException.NotFound");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }

        // Reset the recipe to its original state
        recipeRepository.save(recipeToUpdate);
    }

    private List<Recipe> getAllRecipes() {
        return restClient.get()
                .uri("/recipes")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Recipe>>() {
                });
    }
}