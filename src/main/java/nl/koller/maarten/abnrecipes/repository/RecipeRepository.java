package nl.koller.maarten.abnrecipes.repository;

import nl.koller.maarten.abnrecipes.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
