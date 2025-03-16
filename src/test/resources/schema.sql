CREATE TABLE IF NOT EXISTS recipe (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    is_vegetarian BOOLEAN,
    prep_time INTEGER,
    cook_time INTEGER,
    instructions VARCHAR(2000),
    servings INTEGER
);

CREATE TABLE IF NOT EXISTS recipe_ingredients (
    recipe_id BIGINT,
    ingredients VARCHAR(1000),
    FOREIGN KEY (recipe_id) REFERENCES recipe(id)
);
