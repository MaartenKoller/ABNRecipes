package nl.koller.maarten.abnrecipes.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeSearchRequest {
    // Text search (will be applied to name, ingredients, instructions)
    private String textSearch;

    // Boolean search
    private Boolean vegetarian;

    // Numeric searches with comparison types
    private NumericSearchCriteria servings;
    private NumericSearchCriteria prepTime;
    private NumericSearchCriteria cookTime;
}
