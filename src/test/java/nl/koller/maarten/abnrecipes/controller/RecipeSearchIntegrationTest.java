package nl.koller.maarten.abnrecipes.controller;

import nl.koller.maarten.abnrecipes.model.Recipe;
import nl.koller.maarten.abnrecipes.model.RecipeSearchRequest;
import nl.koller.maarten.abnrecipes.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RecipeSearchIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestClient.Builder restClientBuilder;

    @Autowired
    private RecipeRepository recipeRepository;

    private RestClient restClient;

    @BeforeEach
    public void setup() {
        restClient = restClientBuilder.baseUrl("http://localhost:" + port).build();
    }

    @Test
    public void testSearchByVegetarian() {
        RecipeSearchRequest request = new RecipeSearchRequest();
        request.setVegetarian(true);

        List<Recipe> recipes = searchRecipes(request);

        assertNotNull(recipes);
        assertFalse(recipes.isEmpty());

        // All returned recipes should be vegetarian
        for (Recipe recipe : recipes) {
            assertTrue(recipe.isVegetarian());
        }
    }

    @Test
    public void testSearchByMinServings() {
        RecipeSearchRequest request = new RecipeSearchRequest();
        request.setMinServings(6);

        List<Recipe> recipes = searchRecipes(request);

        assertNotNull(recipes);

        // All returned recipes should have at least 6 servings
        for (Recipe recipe : recipes) {
            assertTrue(recipe.getServings() >= 6);
        }
    }

    @Test
    public void testSearchByMaxPrepTime() {
        RecipeSearchRequest request = new RecipeSearchRequest();
        request.setMaxPrepTime(15);

        List<Recipe> recipes = searchRecipes(request);

        assertNotNull(recipes);

        // All returned recipes should have prep time <= 15 minutes
        for (Recipe recipe : recipes) {
            assertTrue(recipe.getPrepTime() <= 15);
        }
    }

    @Test
    public void testSearchByMaxCookTime() {
        RecipeSearchRequest request = new RecipeSearchRequest();
        request.setMaxCookTime(30);

        List<Recipe> recipes = searchRecipes(request);

        assertNotNull(recipes);

        // All returned recipes should have cook time <= 30 minutes
        for (Recipe recipe : recipes) {
            assertTrue(recipe.getCookTime() <= 30);
        }
    }

    @Test
    public void testSearchByIncludeIngredients() {
        RecipeSearchRequest request = new RecipeSearchRequest();
        request.setIncludeIngredients(Collections.singletonList("garlic"));

        List<Recipe> recipes = searchRecipes(request);

        assertNotNull(recipes);

        // All returned recipes should contain garlic
        for (Recipe recipe : recipes) {
            boolean containsGarlic = recipe.getIngredients().stream()
                .anyMatch(ingredient -> ingredient.toLowerCase().contains("garlic"));
            assertTrue(containsGarlic);
        }
    }

    @Test
    public void testSearchByExcludeIngredients() {
        RecipeSearchRequest request = new RecipeSearchRequest();
        request.setExcludeIngredients(Collections.singletonList("meat"));

        List<Recipe> recipes = searchRecipes(request);

        assertNotNull(recipes);

        // None of the returned recipes should contain meat
        for (Recipe recipe : recipes) {
            boolean containsMeat = recipe.getIngredients().stream()
                .anyMatch(ingredient -> ingredient.toLowerCase().contains("meat"));
            assertFalse(containsMeat);
        }
    }

    @Test
    public void testSearchByInstructionsContain() {
        RecipeSearchRequest request = new RecipeSearchRequest();
        request.setInstructionsContain("oven");

        List<Recipe> recipes = searchRecipes(request);

        assertNotNull(recipes);

        // All returned recipes should have instructions containing "oven"
        for (Recipe recipe : recipes) {
            assertTrue(recipe.getInstructions().toLowerCase().contains("oven"));
        }
    }

    @Test
    public void testCombinedSearch() {
        RecipeSearchRequest request = new RecipeSearchRequest();
        request.setVegetarian(true);
        request.setMaxPrepTime(20);
        request.setMinServings(4);
        request.setExcludeIngredients(Arrays.asList("meat", "chicken", "beef"));

        List<Recipe> recipes = searchRecipes(request);

        assertNotNull(recipes);

        // Verify all conditions are met
        for (Recipe recipe : recipes) {
            assertTrue(recipe.isVegetarian());
            assertTrue(recipe.getPrepTime() <= 20);
            assertTrue(recipe.getServings() >= 4);

            boolean containsExcludedIngredients = recipe.getIngredients().stream()
                .anyMatch(ingredient ->
                    ingredient.toLowerCase().contains("meat") ||
                    ingredient.toLowerCase().contains("chicken") ||
                    ingredient.toLowerCase().contains("beef"));
            assertFalse(containsExcludedIngredients);
        }
    }

    private List<Recipe> searchRecipes(RecipeSearchRequest request) {
        return restClient.post()
                .uri("/recipes/search")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Recipe>>() {});
    }
}
