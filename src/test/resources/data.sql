-- Recipe 1: Spaghetti Bolognese
INSERT INTO recipe (id, name, is_vegetarian, prep_time, cook_time, instructions, servings)
VALUES (1, 'Spaghetti Bolognese', false, 15, 45, 'Sauté onions and garlic. Add beef and brown. Add vegetables, tomatoes, and herbs. Simmer for 30 minutes. Cook pasta separately. Serve sauce over pasta with grated cheese.', 4);

-- Recipe 2: Vegetable Stir Fry
INSERT INTO recipe (id, name, is_vegetarian, prep_time, cook_time, instructions, servings)
VALUES (2, 'Vegetable Stir Fry', true, 20, 15, 'Heat oils in wok. Add garlic and ginger, stir for 30 seconds. Add vegetables and stir-fry for 5-7 minutes. Add soy sauce and cook for 2 more minutes. Serve over rice.', 4);

-- Recipe 3: Grilled Salmon with Lemon Butter
INSERT INTO recipe (id, name, is_vegetarian, prep_time, cook_time, instructions, servings)
VALUES (3, 'Grilled Salmon with Lemon Butter', false, 10, 15, 'Season salmon with oil, salt, and pepper. Grill for 4-5 minutes per side. Meanwhile, make sauce by melting butter with garlic, lemon juice, zest, and dill. Pour over grilled salmon and serve with lemon wedges.', 4);

-- Recipe 4: Mushroom Risotto
INSERT INTO recipe (id, name, is_vegetarian, prep_time, cook_time, instructions, servings)
VALUES (4, 'Mushroom Risotto', true, 15, 30, 'Sauté mushrooms until browned, set aside. In same pan, cook onion and garlic. Add rice and toast for 2 minutes. Add wine and stir until absorbed. Add broth 1/2 cup at a time, stirring until absorbed before adding more. Fold in mushrooms, butter, and cheese. Season and garnish with parsley.', 4);

-- Recipe 5: Beef Tacos
INSERT INTO recipe (id, name, is_vegetarian, prep_time, cook_time, instructions, servings)
VALUES (5, 'Beef Tacos', false, 20, 15, 'Brown beef in pan, drain excess fat. Add taco seasoning and water as per packet instructions. Simmer for 5 minutes. Warm taco shells. Fill shells with beef and top with lettuce, cheese, tomato, sour cream, salsa, and avocado. Serve with lime wedges.', 4);

-- Recipe 6: Spinach and Feta Stuffed Peppers
INSERT INTO recipe (id, name, is_vegetarian, prep_time, cook_time, instructions, servings)
VALUES (6, 'Spinach and Feta Stuffed Peppers', true, 15, 35, 'Preheat oven to 375°F. Sauté onion and garlic in oil. Mix with quinoa, spinach, feta, and oregano. Season with salt and pepper. Fill pepper halves with mixture. Bake for 30 minutes. Top with toasted pine nuts before serving.', 4);

-- Recipe 7: Chicken Alfredo Pasta
INSERT INTO recipe (id, name, is_vegetarian, prep_time, cook_time, instructions, servings)
VALUES (7, 'Chicken Alfredo Pasta', false, 15, 25, 'Cook pasta according to package directions. Season chicken with salt and pepper, then cook in oil until done. Remove chicken. In same pan, add garlic and butter. Add cream and bring to simmer. Stir in cheese until melted. Add chicken back to sauce. Toss with drained pasta. Garnish with parsley.', 4);

-- Recipe 8: Thai Green Curry with Tofu
INSERT INTO recipe (id, name, is_vegetarian, prep_time, cook_time, instructions, servings)
VALUES (8, 'Thai Green Curry with Tofu', true, 20, 20, 'Press and drain tofu. Heat oil and fry tofu until golden. Set aside. In same pan, cook curry paste for 1 minute. Add coconut milk, soy sauce, and sugar. Bring to simmer. Add vegetables and cook for 5 minutes. Return tofu to pan and warm through. Garnish with basil and serve with rice and lime wedges.', 4);

-- Recipe 9: BBQ Pulled Pork Sandwiches
INSERT INTO recipe (id, name, is_vegetarian, prep_time, cook_time, instructions, servings)
VALUES (9, 'BBQ Pulled Pork Sandwiches', false, 20, 240, 'Mix spices and rub over pork. Place in slow cooker with broth. Cook on low for 8 hours. Shred meat and mix with BBQ sauce. Serve on buns topped with coleslaw.', 8);

-- Recipe 10: Roasted Ratatouille
INSERT INTO recipe (id, name, is_vegetarian, prep_time, cook_time, instructions, servings)
VALUES (10, 'Roasted Ratatouille', true, 25, 45, 'Preheat oven to 400°F. Toss all vegetables with oil, garlic, herbs, salt, and pepper. Spread on baking sheets in single layer. Roast for 40-45 minutes, stirring halfway through. Combine in serving dish and garnish with fresh basil.', 6);

-- Ingredients for Recipe 1: Spaghetti Bolognese
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (1, '500g ground beef');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (1, '1 onion, diced');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (1, '2 garlic cloves, minced');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (1, '2 carrots, diced');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (1, '2 celery stalks, diced');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (1, '400g canned tomatoes');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (1, '2 tbsp tomato paste');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (1, '1 tsp dried oregano');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (1, '1 tsp dried basil');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (1, '500g spaghetti');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (1, 'Parmesan cheese for serving');

-- Ingredients for Recipe 2: Vegetable Stir Fry
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (2, '2 cups broccoli florets');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (2, '1 red bell pepper, sliced');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (2, '1 yellow bell pepper, sliced');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (2, '1 carrot, julienned');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (2, '1 cup snap peas');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (2, '3 garlic cloves, minced');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (2, '1 tbsp ginger, grated');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (2, '3 tbsp soy sauce');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (2, '1 tbsp sesame oil');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (2, '2 tbsp vegetable oil');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (2, '2 cups cooked rice');

-- Ingredients for Recipe 3: Grilled Salmon with Lemon Butter
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (3, '4 salmon fillets');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (3, '2 tbsp olive oil');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (3, '1 tsp salt');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (3, '1/2 tsp black pepper');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (3, '4 tbsp butter');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (3, '2 garlic cloves, minced');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (3, '1 lemon, juiced and zested');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (3, '1 tbsp fresh dill, chopped');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (3, 'Lemon wedges for serving');

-- Ingredients for Recipe 4: Mushroom Risotto
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (4, '1.5 cups arborio rice');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (4, '4 cups vegetable broth, warm');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (4, '300g mixed mushrooms, sliced');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (4, '1 onion, finely diced');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (4, '2 garlic cloves, minced');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (4, '1/2 cup white wine');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (4, '3 tbsp butter');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (4, '1/2 cup grated Parmesan cheese');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (4, '2 tbsp fresh parsley, chopped');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (4, 'Salt and pepper to taste');

-- Ingredients for Recipe 5: Beef Tacos
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (5, '500g ground beef');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (5, '1 packet taco seasoning');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (5, '8 taco shells');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (5, '1 cup lettuce, shredded');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (5, '1 cup cheddar cheese, grated');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (5, '1 tomato, diced');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (5, '1/2 cup sour cream');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (5, '1/4 cup salsa');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (5, '1 avocado, sliced');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (5, '1 lime, cut into wedges');

-- Ingredients for Recipe 6: Spinach and Feta Stuffed Peppers
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (6, '4 bell peppers, halved and seeded');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (6, '2 cups cooked quinoa');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (6, '200g spinach, wilted and chopped');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (6, '150g feta cheese, crumbled');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (6, '1 onion, diced');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (6, '2 garlic cloves, minced');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (6, '1 tbsp olive oil');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (6, '1 tsp dried oregano');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (6, 'Salt and pepper to taste');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (6, '2 tbsp pine nuts, toasted');

-- Ingredients for Recipe 7: Chicken Alfredo Pasta
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (7, '500g fettuccine pasta');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (7, '2 chicken breasts, sliced');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (7, '2 tbsp olive oil');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (7, '3 garlic cloves, minced');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (7, '2 cups heavy cream');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (7, '1 cup Parmesan cheese, grated');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (7, '2 tbsp butter');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (7, '1 tsp Italian seasoning');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (7, 'Salt and pepper to taste');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (7, 'Fresh parsley for garnish');

-- Ingredients for Recipe 8: Thai Green Curry with Tofu
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (8, '400g firm tofu, cubed');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (8, '1 can coconut milk');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (8, '3 tbsp green curry paste');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (8, '1 zucchini, sliced');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (8, '1 red bell pepper, sliced');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (8, '1 cup snow peas');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (8, '1 tbsp vegetable oil');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (8, '1 tbsp soy sauce');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (8, '1 tbsp brown sugar');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (8, 'Fresh basil leaves');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (8, 'Lime wedges for serving');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (8, 'Steamed rice for serving');

-- Ingredients for Recipe 9: BBQ Pulled Pork Sandwiches
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (9, '1.5kg pork shoulder');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (9, '2 tbsp brown sugar');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (9, '1 tbsp paprika');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (9, '1 tbsp garlic powder');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (9, '1 tsp cumin');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (9, '1 tsp salt');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (9, '1/2 tsp black pepper');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (9, '1 cup BBQ sauce');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (9, '1/2 cup chicken broth');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (9, '8 burger buns');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (9, 'Coleslaw for serving');

-- Ingredients for Recipe 10: Roasted Ratatouille
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (10, '1 eggplant, cubed');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (10, '2 zucchini, sliced');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (10, '1 red bell pepper, chunked');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (10, '1 yellow bell pepper, chunked');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (10, '1 red onion, chunked');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (10, '3 tomatoes, quartered');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (10, '4 garlic cloves, minced');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (10, '3 tbsp olive oil');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (10, '1 tsp dried thyme');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (10, '1 tsp dried rosemary');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (10, 'Salt and pepper to taste');
INSERT INTO recipe_ingredients (recipe_id, ingredients) VALUES (10, 'Fresh basil for garnish');
