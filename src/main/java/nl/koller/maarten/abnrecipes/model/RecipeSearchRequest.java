package nl.koller.maarten.abnrecipes.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeSearchRequest {
    private Boolean vegetarian;
    private Integer minServings;
    private Integer maxPrepTime;
    private Integer maxCookTime;
    private List<String> includeIngredients;
    private List<String> excludeIngredients;
    private String instructionsContain;
}
