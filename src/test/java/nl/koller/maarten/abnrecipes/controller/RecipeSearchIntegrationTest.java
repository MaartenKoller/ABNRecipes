package nl.koller.maarten.abnrecipes.controller;

import nl.koller.maarten.abnrecipes.model.NumericSearchCriteria;
import nl.koller.maarten.abnrecipes.model.Recipe;
import nl.koller.maarten.abnrecipes.model.RecipeSearchRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RecipeSearchIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestClient.Builder restClientBuilder;

    private RestClient restClient;

    @BeforeEach
    public void setup() {
        restClient = restClientBuilder.baseUrl("http://localhost:" + port).build();
    }


    @Test
    public void testSearchByVegetarian() {
        RecipeSearchRequest request = RecipeSearchRequest.builder()
                .vegetarian(true)
                .build();

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
        RecipeSearchRequest request = RecipeSearchRequest.builder()
                .servings(NumericSearchCriteria.builder()
                        .value(6)
                        .comparisonType(NumericSearchCriteria.ComparisonType.MINIMUM)
                        .build())
                .build();

        List<Recipe> recipes = searchRecipes(request);

        assertNotNull(recipes);

        // All returned recipes should have at least 6 servings
        for (Recipe recipe : recipes) {
            assertTrue(recipe.getServings() >= 6);
        }
    }

    @Test
    public void testSearchByMaxPrepTime() {
        RecipeSearchRequest request = RecipeSearchRequest.builder()
                .prepTime(NumericSearchCriteria.builder()
                        .value(15)
                        .comparisonType(NumericSearchCriteria.ComparisonType.MAXIMUM)
                        .build())
                .build();

        List<Recipe> recipes = searchRecipes(request);

        assertNotNull(recipes);

        // All returned recipes should have prep time <= 15 minutes
        for (Recipe recipe : recipes) {
            assertTrue(recipe.getPrepTime() <= 15);
        }
    }

    @Test
    public void testSearchByMaxCookTime() {
        RecipeSearchRequest request = RecipeSearchRequest.builder()
                .cookTime(NumericSearchCriteria.builder()
                        .value(30)
                        .comparisonType(NumericSearchCriteria.ComparisonType.MAXIMUM)
                        .build())
                .build();

        List<Recipe> recipes = searchRecipes(request);

        assertNotNull(recipes);

        // All returned recipes should have cook time <= 30 minutes
        for (Recipe recipe : recipes) {
            assertTrue(recipe.getCookTime() <= 30);
        }
    }

    @Test
    public void testSearchByTextSearchForIngredient() {
        RecipeSearchRequest request = RecipeSearchRequest.builder()
                .textSearch("garlic")
                .build();

        List<Recipe> recipes = searchRecipes(request);

        assertNotNull(recipes);

        // All returned recipes should contain garlic in ingredients, name, or instructions
        for (Recipe recipe : recipes) {
            boolean containsGarlic = recipe.getIngredients().stream()
                    .anyMatch(ingredient -> ingredient.toLowerCase().contains("garlic")) ||
                    recipe.getName().toLowerCase().contains("garlic") ||
                    recipe.getInstructions().toLowerCase().contains("garlic");

            assertTrue(containsGarlic);
        }
    }

    @Test
    public void testSearchByTextSearchExcludingIngredient() {
        // First get all recipes
        List<Recipe> allRecipes = searchRecipes(RecipeSearchRequest.builder().build());

        // Then search for recipes without "meat" in text
        RecipeSearchRequest request = RecipeSearchRequest.builder()
                .textSearch("vegetarian")
                .build();

        List<Recipe> recipes = searchRecipes(request);

        assertNotNull(recipes);
        // Verify we found fewer recipes than the total
        assertTrue(recipes.size() < allRecipes.size() || allRecipes.isEmpty());
    }

    @Test
    public void testSearchByTextSearchInInstructions() {
        RecipeSearchRequest request = RecipeSearchRequest.builder()
                .textSearch("oven")
                .build();

        List<Recipe> recipes = searchRecipes(request);

        assertNotNull(recipes);

        // All returned recipes should have instructions containing "oven"
        for (Recipe recipe : recipes) {
            boolean containsOven = recipe.getInstructions().toLowerCase().contains("oven") ||
                    recipe.getName().toLowerCase().contains("oven") ||
                    recipe.getIngredients().stream()
                            .anyMatch(ingredient -> ingredient.toLowerCase().contains("oven"));

            assertTrue(containsOven);
        }
    }

    @Test
    public void testCombinedSearch() {
        RecipeSearchRequest request = RecipeSearchRequest.builder()
                .vegetarian(true)
                .prepTime(NumericSearchCriteria.builder()
                        .value(20)
                        .comparisonType(NumericSearchCriteria.ComparisonType.MAXIMUM)
                        .build())
                .servings(NumericSearchCriteria.builder()
                        .value(4)
                        .comparisonType(NumericSearchCriteria.ComparisonType.MINIMUM)
                        .build())
                .textSearch("vegetable")
                .build();

        List<Recipe> recipes = searchRecipes(request);

        assertNotNull(recipes);

        // Verify all conditions are met
        for (Recipe recipe : recipes) {
            assertTrue(recipe.isVegetarian());
            assertTrue(recipe.getPrepTime() <= 20);
            assertTrue(recipe.getServings() >= 4);

            boolean containsVegetable = recipe.getName().toLowerCase().contains("vegetable") ||
                    recipe.getInstructions().toLowerCase().contains("vegetable") ||
                    recipe.getIngredients().stream()
                            .anyMatch(ingredient -> ingredient.toLowerCase().contains("vegetable"));

            assertTrue(containsVegetable);
        }
    }

    @Test
    public void testExactServingsMatch() {
        RecipeSearchRequest request = RecipeSearchRequest.builder()
                .servings(NumericSearchCriteria.builder()
                        .value(4)
                        .comparisonType(NumericSearchCriteria.ComparisonType.EXACT)
                        .build())
                .build();

        List<Recipe> recipes = searchRecipes(request);

        assertNotNull(recipes);

        // All returned recipes should have exactly 4 servings
        for (Recipe recipe : recipes) {
            assertEquals(4, recipe.getServings());
        }
    }

    @Test
    public void testGreaterThanPrepTime() {
        RecipeSearchRequest request = RecipeSearchRequest.builder()
                .prepTime(NumericSearchCriteria.builder()
                        .value(20)
                        .comparisonType(NumericSearchCriteria.ComparisonType.GREATER)
                        .build())
                .build();

        List<Recipe> recipes = searchRecipes(request);

        assertNotNull(recipes);

        // All returned recipes should have prep time > 20 minutes
        for (Recipe recipe : recipes) {
            assertTrue(recipe.getPrepTime() > 20);
        }
    }

    @Test
    public void testLessThanCookTime() {
        RecipeSearchRequest request = RecipeSearchRequest.builder()
                .cookTime(NumericSearchCriteria.builder()
                        .value(45)
                        .comparisonType(NumericSearchCriteria.ComparisonType.LESS)
                        .build())
                .build();

        List<Recipe> recipes = searchRecipes(request);

        assertNotNull(recipes);

        // All returned recipes should have cook time < 45 minutes
        for (Recipe recipe : recipes) {
            assertTrue(recipe.getCookTime() < 45);
        }
    }

    private List<Recipe> searchRecipes(RecipeSearchRequest request) {
        return restClient.post()
                .uri("/recipes/search")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
