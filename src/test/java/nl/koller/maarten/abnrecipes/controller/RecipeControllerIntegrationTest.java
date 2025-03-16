package nl.koller.maarten.abnrecipes.controller;

import nl.koller.maarten.abnrecipes.model.Recipe;
import nl.koller.maarten.abnrecipes.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
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
    private RecipeRepository recipeRepository;

    @Test
    public void testGetAllRecipes() {
        RestClient restClient = restClientBuilder.baseUrl("http://localhost:" + port).build();

        List<Recipe> recipes = restClient.get()
                .uri("/recipes")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Recipe>>() {
                });

        assert recipes != null;
        assertFalse(recipes.isEmpty());
        assertEquals(10, recipes.size());
        assertEquals("Spaghetti Bolognese", recipes.get(0).getName());
        assertEquals("Chicken Alfredo Pasta", recipes.get(6).getName());
        assertEquals("Roasted Ratatouille", recipes.get(9).getName());
    }

    @Test
    public void testAddRecipe() throws IOException {
        RestClient restClient = restClientBuilder.baseUrl("http://localhost:" + port).build();

        // First, verify we have 10 recipes initially
        List<Recipe> initialRecipes = restClient.get()
                .uri("/recipes")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Recipe>>() {
                });

        assert initialRecipes != null;
        assertEquals(10, initialRecipes.size());

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
        List<Recipe> updatedRecipes = restClient.get()
                .uri("/recipes")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Recipe>>() {
                });

        assert updatedRecipes != null;
        assertEquals(11, updatedRecipes.size());

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
    }
}
