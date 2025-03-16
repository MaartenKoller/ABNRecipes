package nl.koller.maarten.abnrecipes.controller;

import nl.koller.maarten.abnrecipes.model.Recipe;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RecipeControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestClient.Builder restClientBuilder;

    @Test
    public void testGetAllRecipes() {
        RestClient restClient = restClientBuilder.baseUrl("http://localhost:" + port).build();

        List<Recipe> recipes = restClient.get()
                .uri("/recipes")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Recipe>>() {});

        assert recipes != null;
        assertFalse(recipes.isEmpty());
        assertEquals(3, recipes.size());
        assertEquals("Pasta Carbonara", recipes.get(0).getName());
        assertEquals("Chicken Curry", recipes.get(1).getName());
        assertEquals("Caesar Salad", recipes.get(2).getName());
    }
}
