package nl.koller.maarten.abnrecipes.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RecipeRequest {
    private List<Recipe> recipes;

    public RecipeRequest() {
    }

    public RecipeRequest(List<Recipe> recipes) {
        this.recipes = recipes;
    }

}
