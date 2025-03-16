package nl.koller.maarten.abnrecipes.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @JsonProperty("isVegetarian")
    private boolean isVegetarian;
    private int prepTime;
    private int cookTime;

    @ElementCollection
    @Column(length = 1000) // Adjust length as needed
    private List<String> ingredients;

    @Column(length = 2000) // Adjust length as needed
    private String instructions;

    private int servings;
}
