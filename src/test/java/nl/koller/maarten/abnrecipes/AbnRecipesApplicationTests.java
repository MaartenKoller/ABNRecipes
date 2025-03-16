package nl.koller.maarten.abnrecipes;

import nl.koller.maarten.abnrecipes.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AbnRecipesApplicationTests {

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    void contextLoads() {
        // Basic context load test (already present)
    }

    @Test
    void testDataLoaded() {
        // Check if data is loaded.  We know there are 10 recipes.
        long count = recipeRepository.count();
        assertEquals(10, count, "The number of recipes loaded should be 10");

        // Fetch a specific recipe and check its properties
        assertTrue(recipeRepository.findById(1L).isPresent());
        assertEquals("Spaghetti Bolognese", recipeRepository.findById(1L).get().getName());
    }
}
