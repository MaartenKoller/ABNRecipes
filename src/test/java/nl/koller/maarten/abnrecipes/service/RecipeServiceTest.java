package nl.koller.maarten.abnrecipes.service;

import nl.koller.maarten.abnrecipes.model.Recipe;
import nl.koller.maarten.abnrecipes.model.RecipeSearchRequest;
import nl.koller.maarten.abnrecipes.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    private Recipe vegetarianRecipe;
    private Recipe nonVegetarianRecipe;
    private List<Recipe> allRecipes;

    @BeforeEach
    void setUp() {
        // Setup test data using Builder pattern
        vegetarianRecipe = Recipe.builder()
                .id(1L)
                .name("Vegetarian Pasta")
                .isVegetarian(true)
                .prepTime(15)
                .cookTime(20)
                .servings(4)
                .ingredients(Arrays.asList("pasta", "tomato sauce", "garlic", "basil"))
                .instructions("Boil pasta. Mix with sauce. Add herbs.")
                .build();

        nonVegetarianRecipe = Recipe.builder()
                .id(2L)
                .name("Beef Stew")
                .isVegetarian(false)
                .prepTime(30)
                .cookTime(120)
                .servings(6)
                .ingredients(Arrays.asList("beef", "potatoes", "carrots", "onion", "beef stock"))
                .instructions("Brown beef. Add vegetables and stock. Simmer for 2 hours.")
                .build();

        allRecipes = Arrays.asList(vegetarianRecipe, nonVegetarianRecipe);
    }
    @Test
    void getAllRecipes_ShouldReturnAllRecipes() {
        // Arrange
        when(recipeRepository.findAll()).thenReturn(allRecipes);

        // Act
        List<Recipe> result = recipeService.getAllRecipes();

        // Assert
        assertEquals(2, result.size());
        assertEquals(allRecipes, result);
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    void getAllRecipes_WhenNoRecipes_ShouldReturnEmptyList() {
        // Arrange
        when(recipeRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Recipe> result = recipeService.getAllRecipes();

        // Assert
        assertTrue(result.isEmpty());
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    void addRecipes_WithNewRecipes_ShouldSaveAndReturnAll() {
        // Arrange
        Recipe newRecipe1 = new Recipe();
        newRecipe1.setName("New Recipe 1");

        Recipe newRecipe2 = new Recipe();
        newRecipe2.setName("New Recipe 2");

        List<Recipe> newRecipes = Arrays.asList(newRecipe1, newRecipe2);

        when(recipeRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(recipeRepository.save(any(Recipe.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        List<Recipe> result = recipeService.addRecipes(newRecipes);

        // Assert
        assertEquals(2, result.size());
        verify(recipeRepository, times(2)).findByName(anyString());
        verify(recipeRepository, times(2)).save(any(Recipe.class));
    }

    @Test
    void addRecipes_WithExistingRecipe_ShouldSkipExisting() {
        // Arrange
        Recipe existingRecipe = new Recipe();
        existingRecipe.setName("Existing Recipe");

        Recipe newRecipe = new Recipe();
        newRecipe.setName("New Recipe");

        List<Recipe> recipes = Arrays.asList(existingRecipe, newRecipe);

        when(recipeRepository.findByName("Existing Recipe")).thenReturn(Optional.of(existingRecipe));
        when(recipeRepository.findByName("New Recipe")).thenReturn(Optional.empty());
        when(recipeRepository.save(any(Recipe.class))).thenReturn(newRecipe);

        // Act
        List<Recipe> result = recipeService.addRecipes(recipes);

        // Assert
        assertEquals(1, result.size());
        assertEquals("New Recipe", result.getFirst().getName());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }

    @Test
    void addRecipes_WithEmptyList_ShouldReturnEmptyList() {
        // Act
        List<Recipe> result = recipeService.addRecipes(Collections.emptyList());

        // Assert
        assertTrue(result.isEmpty());
        verify(recipeRepository, never()).save(any(Recipe.class));
    }

    @Test
    void deleteRecipe_WhenExists_ShouldReturnTrue() {
        // Arrange
        when(recipeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(recipeRepository).deleteById(1L);

        // Act
        boolean result = recipeService.deleteRecipe(1L);

        // Assert
        assertTrue(result);
        verify(recipeRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteRecipe_WhenNotExists_ShouldReturnFalse() {
        // Arrange
        when(recipeRepository.existsById(999L)).thenReturn(false);

        // Act
        boolean result = recipeService.deleteRecipe(999L);

        // Assert
        assertFalse(result);
        verify(recipeRepository, never()).deleteById(anyLong());
    }

    @Test
    void updateRecipe_WhenExists_ShouldUpdateAndReturn() {
        // Arrange
        Recipe updatedRecipe = Recipe.builder()
                .name("Updated Recipe")
                .isVegetarian(true)
                .prepTime(10)
                .cookTime(15)
                .servings(2)
                .ingredients(Arrays.asList("ingredient1", "ingredient2"))
                .instructions("Updated instructions")
                .build();

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(vegetarianRecipe));
        when(recipeRepository.save(any(Recipe.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Recipe result = recipeService.updateRecipe(1L, updatedRecipe);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Recipe", result.getName());
        assertTrue(result.isVegetarian());
        assertEquals(10, result.getPrepTime());
        assertEquals(15, result.getCookTime());
        assertEquals(2, result.getServings());
        assertEquals(Arrays.asList("ingredient1", "ingredient2"), result.getIngredients());
        assertEquals("Updated instructions", result.getInstructions());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }

    @Test
    void updateRecipe_WhenNotExists_ShouldReturnNull() {
        // Arrange
        when(recipeRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Recipe result = recipeService.updateRecipe(999L, new Recipe());

        // Assert
        assertNull(result);
        verify(recipeRepository, never()).save(any(Recipe.class));
    }

    @Test
    void searchRecipes_WithVegetarianFilter_ShouldReturnOnlyVegetarian() {
        // Arrange
        when(recipeRepository.findAll()).thenReturn(allRecipes);
        RecipeSearchRequest searchRequest = new RecipeSearchRequest();
        searchRequest.setVegetarian(true);

        // Act
        List<Recipe> result = recipeService.searchRecipes(searchRequest);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.getFirst().isVegetarian());
        assertEquals("Vegetarian Pasta", result.getFirst().getName());
    }

    @Test
    void searchRecipes_WithMinServings_ShouldFilterCorrectly() {
        // Arrange
        when(recipeRepository.findAll()).thenReturn(allRecipes);
        RecipeSearchRequest searchRequest = new RecipeSearchRequest();
        searchRequest.setMinServings(5);

        // Act
        List<Recipe> result = recipeService.searchRecipes(searchRequest);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Beef Stew", result.getFirst().getName());
    }

    @Test
    void searchRecipes_WithMaxPrepTime_ShouldFilterCorrectly() {
        // Arrange
        when(recipeRepository.findAll()).thenReturn(allRecipes);
        RecipeSearchRequest searchRequest = new RecipeSearchRequest();
        searchRequest.setMaxPrepTime(20);

        // Act
        List<Recipe> result = recipeService.searchRecipes(searchRequest);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Vegetarian Pasta", result.getFirst().getName());
    }

    @Test
    void searchRecipes_WithMaxCookTime_ShouldFilterCorrectly() {
        // Arrange
        when(recipeRepository.findAll()).thenReturn(allRecipes);
        RecipeSearchRequest searchRequest = new RecipeSearchRequest();
        searchRequest.setMaxCookTime(30);

        // Act
        List<Recipe> result = recipeService.searchRecipes(searchRequest);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Vegetarian Pasta", result.getFirst().getName());
    }

    @Test
    void searchRecipes_WithIncludeIngredients_ShouldFilterCorrectly() {
        // Arrange
        when(recipeRepository.findAll()).thenReturn(allRecipes);
        RecipeSearchRequest searchRequest = new RecipeSearchRequest();
        searchRequest.setIncludeIngredients(Arrays.asList("pasta", "tomato"));

        // Act
        List<Recipe> result = recipeService.searchRecipes(searchRequest);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Vegetarian Pasta", result.getFirst().getName());
    }

    @Test
    void searchRecipes_WithExcludeIngredients_ShouldFilterCorrectly() {
        // Arrange
        when(recipeRepository.findAll()).thenReturn(allRecipes);
        RecipeSearchRequest searchRequest = new RecipeSearchRequest();
        searchRequest.setExcludeIngredients(Collections.singletonList("beef"));

        // Act
        List<Recipe> result = recipeService.searchRecipes(searchRequest);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Vegetarian Pasta", result.getFirst().getName());
    }

    @Test
    void searchRecipes_WithInstructionsContain_ShouldFilterCorrectly() {
        // Arrange
        when(recipeRepository.findAll()).thenReturn(allRecipes);
        RecipeSearchRequest searchRequest = new RecipeSearchRequest();
        searchRequest.setInstructionsContain("simmer");

        // Act
        List<Recipe> result = recipeService.searchRecipes(searchRequest);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Beef Stew", result.getFirst().getName());
    }

    @Test
    void searchRecipes_WithMultipleFilters_ShouldApplyAllFilters() {
        // Arrange
        when(recipeRepository.findAll()).thenReturn(allRecipes);
        RecipeSearchRequest searchRequest = new RecipeSearchRequest();
        searchRequest.setVegetarian(false);
        searchRequest.setMinServings(5);
        searchRequest.setIncludeIngredients(Arrays.asList("beef", "potatoes"));

        // Act
        List<Recipe> result = recipeService.searchRecipes(searchRequest);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Beef Stew", result.getFirst().getName());
    }

    @Test
    void searchRecipes_WithNoMatchingRecipes_ShouldReturnEmptyList() {
        // Arrange
        when(recipeRepository.findAll()).thenReturn(allRecipes);
        RecipeSearchRequest searchRequest = new RecipeSearchRequest();
        searchRequest.setVegetarian(true);
        searchRequest.setIncludeIngredients(Collections.singletonList("beef"));

        // Act
        List<Recipe> result = recipeService.searchRecipes(searchRequest);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void searchRecipes_WithNoFilters_ShouldReturnAllRecipes() {
        // Arrange
        when(recipeRepository.findAll()).thenReturn(allRecipes);
        RecipeSearchRequest searchRequest = new RecipeSearchRequest();

        // Act
        List<Recipe> result = recipeService.searchRecipes(searchRequest);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void searchRecipes_WithCaseInsensitiveIngredientSearch_ShouldMatchCorrectly() {
        // Arrange
        when(recipeRepository.findAll()).thenReturn(allRecipes);
        RecipeSearchRequest searchRequest = new RecipeSearchRequest();
        searchRequest.setIncludeIngredients(Collections.singletonList("PASTA"));

        // Act
        List<Recipe> result = recipeService.searchRecipes(searchRequest);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Vegetarian Pasta", result.getFirst().getName());
    }

    @Test
    void searchRecipes_WithCaseInsensitiveInstructionsSearch_ShouldMatchCorrectly() {
        // Arrange
        when(recipeRepository.findAll()).thenReturn(allRecipes);
        RecipeSearchRequest searchRequest = new RecipeSearchRequest();
        searchRequest.setInstructionsContain("BOIL");

        // Act
        List<Recipe> result = recipeService.searchRecipes(searchRequest);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Vegetarian Pasta", result.getFirst().getName());
    }

    @Test
    void searchRecipes_WithPartialIngredientMatch_ShouldMatchCorrectly() {
        // Arrange
        when(recipeRepository.findAll()).thenReturn(allRecipes);
        RecipeSearchRequest searchRequest = new RecipeSearchRequest();
        searchRequest.setIncludeIngredients(Collections.singletonList("toma"));  // Partial match for "tomato sauce"

        // Act
        List<Recipe> result = recipeService.searchRecipes(searchRequest);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Vegetarian Pasta", result.getFirst().getName());
    }

    @Test
    void searchRecipes_WithEmptyRepository_ShouldReturnEmptyList() {
        // Arrange
        when(recipeRepository.findAll()).thenReturn(Collections.emptyList());
        RecipeSearchRequest searchRequest = new RecipeSearchRequest();
        searchRequest.setVegetarian(true);

        // Act
        List<Recipe> result = recipeService.searchRecipes(searchRequest);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void searchRecipes_WithNullSearchRequest_ShouldHandleGracefully() {
        // Arrange
        when(recipeRepository.findAll()).thenReturn(allRecipes);

        // Act
        List<Recipe> result = recipeService.searchRecipes(null);

        // Assert - This test verifies that the method doesn't throw an exception
        // Note: The actual implementation might need to be updated to handle null search requests
        assertNotNull(result);
    }

    @Test
    void addRecipes_WithNullList_ShouldHandleGracefully() {
        // Act & Assert
        assertDoesNotThrow(() -> recipeService.addRecipes(null));
    }

    @Test
    void addRecipes_WithRecipeHavingNullName_ShouldHandleGracefully() {
        // Arrange
        Recipe recipeWithNullName = new Recipe();
        // Name is null
        recipeWithNullName.setVegetarian(true);

        List<Recipe> recipes = Collections.singletonList(recipeWithNullName);

        when(recipeRepository.findByName(null)).thenReturn(Optional.empty());
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipeWithNullName);

        // Act
        List<Recipe> result = recipeService.addRecipes(recipes);

        // Assert
        assertEquals(1, result.size());
        verify(recipeRepository).save(any(Recipe.class));
    }

    @Test
    void searchRecipes_WithEmptyIncludeIngredients_ShouldNotApplyFilter() {
        // Arrange
        when(recipeRepository.findAll()).thenReturn(allRecipes);
        RecipeSearchRequest searchRequest = new RecipeSearchRequest();
        searchRequest.setIncludeIngredients(Collections.emptyList());

        // Act
        List<Recipe> result = recipeService.searchRecipes(searchRequest);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void searchRecipes_WithEmptyExcludeIngredients_ShouldNotApplyFilter() {
        // Arrange
        when(recipeRepository.findAll()).thenReturn(allRecipes);
        RecipeSearchRequest searchRequest = new RecipeSearchRequest();
        searchRequest.setExcludeIngredients(Collections.emptyList());

        // Act
        List<Recipe> result = recipeService.searchRecipes(searchRequest);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void searchRecipes_WithEmptyInstructionsContain_ShouldNotApplyFilter() {
        // Arrange
        when(recipeRepository.findAll()).thenReturn(allRecipes);
        RecipeSearchRequest searchRequest = new RecipeSearchRequest();
        searchRequest.setInstructionsContain("");

        // Act
        List<Recipe> result = recipeService.searchRecipes(searchRequest);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void searchRecipes_WithExactServingsMatch_ShouldFilterCorrectly() {
        // Arrange
        when(recipeRepository.findAll()).thenReturn(allRecipes);
        RecipeSearchRequest searchRequest = new RecipeSearchRequest();
        searchRequest.setMinServings(4);

        // Act
        List<Recipe> result = recipeService.searchRecipes(searchRequest);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void searchRecipes_WithExactPrepTimeMatch_ShouldFilterCorrectly() {
        // Arrange
        when(recipeRepository.findAll()).thenReturn(allRecipes);
        RecipeSearchRequest searchRequest = new RecipeSearchRequest();
        searchRequest.setMaxPrepTime(15);

        // Act
        List<Recipe> result = recipeService.searchRecipes(searchRequest);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Vegetarian Pasta", result.getFirst().getName());
    }

    @Test
    void searchRecipes_WithExactCookTimeMatch_ShouldFilterCorrectly() {
        // Arrange
        when(recipeRepository.findAll()).thenReturn(allRecipes);
        RecipeSearchRequest searchRequest = new RecipeSearchRequest();
        searchRequest.setMaxCookTime(20);

        // Act
        List<Recipe> result = recipeService.searchRecipes(searchRequest);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Vegetarian Pasta", result.getFirst().getName());
    }

    @Test
    void searchRecipes_WithComplexCombination_ShouldFilterCorrectly() {
        // Arrange
        when(recipeRepository.findAll()).thenReturn(allRecipes);
        RecipeSearchRequest searchRequest = new RecipeSearchRequest();
        searchRequest.setVegetarian(false);
        searchRequest.setMinServings(4);
        searchRequest.setMaxPrepTime(40);
        searchRequest.setMaxCookTime(150);
        searchRequest.setIncludeIngredients(Arrays.asList("beef", "onion"));
        searchRequest.setExcludeIngredients(Collections.singletonList("chicken"));
        searchRequest.setInstructionsContain("simmer");

        // Act
        List<Recipe> result = recipeService.searchRecipes(searchRequest);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Beef Stew", result.getFirst().getName());
    }

    @Test
    void updateRecipe_WithNullUpdatedRecipe_ShouldHandleGracefully() {
        // Act & Assert
        assertDoesNotThrow(() -> recipeService.updateRecipe(1L, null));
    }

    @Test
    void deleteRecipe_WithNullId_ShouldHandleGracefully() {
        // Act & Assert
        assertDoesNotThrow(() -> recipeService.deleteRecipe(null));
    }
}