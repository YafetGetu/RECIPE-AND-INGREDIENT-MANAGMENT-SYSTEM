CREATE DATABASE REC_ING13;
USE REC_ING13;

-- Recipe Table
CREATE TABLE Recipe (
    rec_id INT PRIMARY KEY AUTO_INCREMENT,
    rec_name VARCHAR(100) NOT NULL CHECK (CHAR_LENGTH(rec_name) >= 3 AND CHAR_LENGTH(rec_name) <= 100) UNIQUE,
    description TEXT,
    cooking_time INT NOT NULL CHECK (cooking_time > 0),
    servings INT NOT NULL CHECK(servings > 0),
    instruction TEXT NOT NULL,
    price DECIMAL(10,2) NOT NULL CHECK(price > 0),
    INDEX (rec_name)
);

-- Ingredient Table
CREATE TABLE Ingredient(
    ing_id INT PRIMARY KEY AUTO_INCREMENT,
    ing_name VARCHAR(50) NOT NULL CHECK (CHAR_LENGTH(ing_name) >= 3 AND CHAR_LENGTH(ing_name) < 50) UNIQUE,
    category VARCHAR(100),
    unit VARCHAR(50) NOT NULL CHECK (unit IN ('kg', 'g', 'liters', 'ml', 'pcs')),
    INDEX (ing_name)
);

-- Recipe_Ingredient Table
CREATE TABLE Recipe_Ingredient(
    rec_id INT NOT NULL, 
    ing_id INT NOT NULL, 
    quantity DECIMAL(10,2) NOT NULL CHECK (quantity > 0),
    unit VARCHAR(50) NOT NULL CHECK (unit IN ('kg', 'g', 'liters', 'ml', 'pcs')),
    PRIMARY KEY (rec_id, ing_id),
    FOREIGN KEY (rec_id) REFERENCES Recipe(rec_id) ON DELETE CASCADE,
    FOREIGN KEY (ing_id) REFERENCES Ingredient(ing_id) ON DELETE CASCADE,
    INDEX (rec_id),
    INDEX (ing_id)
);

-- Nutrition Table
CREATE TABLE Nutrition (
    nut_id INT PRIMARY KEY AUTO_INCREMENT,
    ing_id INT NOT NULL, 
    calories DECIMAL(10,2) NOT NULL CHECK (calories >= 0),
    carbohydrates DECIMAL(10,2) DEFAULT 0 CHECK (carbohydrates >= 0),
    protein DECIMAL(10,2) NOT NULL CHECK (protein >= 0),
    fat DECIMAL(10,2) NOT NULL CHECK (fat >= 0),
    sugar DECIMAL(10,2) DEFAULT 0 CHECK (sugar >= 0),
    fiber DECIMAL(10,2) DEFAULT 0 CHECK (fiber >= 0),
    FOREIGN KEY (ing_id) REFERENCES Ingredient(ing_id) ON DELETE CASCADE,
    INDEX (ing_id)
);

-- Allerg_desc Table
CREATE TABLE Allerg_desc (
    allerg_name VARCHAR(50)  PRIMARY KEY,
    allerg_desc VARCHAR(200) NULL,
    INDEX (allerg_name)
);

-- Allergy Table
CREATE TABLE Allergy (
    allerg_id INT PRIMARY KEY AUTO_INCREMENT,
    ing_id INT NOT NULL, 
    allerg_name VARCHAR(50) NOT NULL,
    FOREIGN KEY (ing_id) REFERENCES Ingredient(ing_id) ON DELETE CASCADE,
    FOREIGN KEY (allerg_name) REFERENCES Allerg_desc(allerg_name) ON DELETE CASCADE,
    INDEX (ing_id),
    INDEX (allerg_name)
);

-- Menu Table
CREATE TABLE Menu (
    menu_id INT PRIMARY KEY AUTO_INCREMENT,
    meal_type ENUM('breakfast', 'lunch', 'dinner', 'drinks', 'dessert') NOT NULL,
    menu_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX (menu_date)
);

-- Menu_Recipe Table
CREATE TABLE Menu_Recipe (
    menu_id INT NOT NULL, 
    rec_id INT NOT NULL,
    PRIMARY KEY (menu_id, rec_id),
    FOREIGN KEY (menu_id) REFERENCES Menu(menu_id) ON DELETE CASCADE,
    FOREIGN KEY (rec_id) REFERENCES Recipe(rec_id) ON DELETE CASCADE,
    INDEX (menu_id),
    INDEX (rec_id)
);

CREATE TABLE Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL
);

-- Insert login page info username and password
INSERT INTO Users (username, password) VALUES 
('yafet', 'yaf123');


-- populating data recipe 
INSERT INTO Recipe (rec_name, description, cooking_time, servings, instruction, price) VALUES
    ('Doro Wot', 'Chicken stew with hard-boiled eggs and berbere', 120, 4, 'Cook chicken, mix with berbere and onions, and simmer.', 300.00),
    ('Tibs', 'Sautéed meat (beef, lamb, or goat) with onions and peppers', 40, 3, 'Sauté meat, onions, and peppers in oil.', 450.00),
    ('Kitfo', 'Minced raw beef seasoned with mitmita (spices) and kibbe (spiced butter)', 30, 2, 'Season minced beef with spices and serve.', 700.00),
    ('Gored Gored', 'Cubes of raw beef seasoned with spices and butter', 20, 2, 'Mix beef cubes with spices and butter.', 600.00),
    ('Siga Wot', 'Beef stew cooked with berbere spices', 90, 4, 'Simmer beef with berbere and spices.', 400.00),
    ('Alicha Tibs', 'Mild sautéed meat with onions and turmeric', 45, 3, 'Sauté meat with onions and turmeric.', 350.00),
    ('Zilzil Tibs', 'Strips of beef sautéed with onions and rosemary', 50, 3, 'Cook beef strips with onions and rosemary.', 300.00),
    ('Ayib', 'Ethiopian cottage cheese', 10, 4, 'Prepare cottage cheese as a side dish.', 150.00),
    ('Shiro Wot', 'Spicy chickpea or lentil stew', 60, 4, 'Cook chickpeas or lentils with berbere.', 100.00),
    ('Misir Wot', 'Lentil stew with berbere spices', 60, 4, 'Cook lentils with berbere.', 100.00),
    ('Gomen', 'Stewed collard greens', 45, 4, 'Cook collard greens with spices.', 80.00),
    ('Dinich Wot', 'Potato stew with Ethiopian spices', 50, 4, 'Simmer potatoes with spices.', 150.00),
    ('Fosolia', 'Sautéed string beans and carrots', 30, 3, 'Sauté beans and carrots.', 70.00),
    ('Alicha Wot', 'Mild stew made with lentils or split peas', 50, 4, 'Cook lentils or peas with mild spices.', 80.00),
    ('Timatim Salad', 'Fresh tomato and onion salad with lime and oil', 10, 2, 'Mix tomatoes and onions with lime and oil.', 40.00),
    ('Enguday Tibs', 'Sautéed mushrooms with spices', 30, 3, 'Sauté mushrooms with spices.', 80.00),
    ('Injera', 'Traditional Ethiopian sourdough flatbread', 24, 10, 'Mix teff flour, ferment, and cook.', 50.00),
    ('Dabo', 'Traditional Ethiopian bread', 120, 6, 'Mix flour, sugar, salt, and yeast.', 10.00),
    ('Beso Chibto', 'A traditional Ethiopian snack or breakfast', 10, 2, 'Spiced butter (niter kibbe), roasted barley flour,
    and sugar (if desired). Serve warm or at room temperature.', 70.00),
    ('Kita', 'Unleavened flatbread', 15, 6, 'Prepare flatbread by frying dough.', 50.00),
    ('Chechebsa (Fasting Version)', 'Fried flatbread mixed with berbere and oil', 20, 3, 'Fry flatbread and mix with berbere.', 60.00),
    ('Beso', 'Roasted barley flour mixed with water and sugar', 5, 1, 'Mix barley flour with water and sugar.', 20.00),
    ('Telba', 'Flaxseed drink', 5, 1, 'Blend flaxseeds with water and sugar.', 25.00),
    ('Cake', 'A sweet and savory cake with Ethiopian flavors', 90, 8, 'Prepare dough, fill with sweet fillings, bake until golden brown.', 120.00),
    ('Shenkora (Sugarcane)', 'Fresh sugarcane sticks', 10, 1, 'Peel and cut sugarcane into sticks for chewing.', 30.00);


-- Populating Ingredient Table
INSERT INTO Ingredient (ing_id, ing_name, category, unit) VALUES
    (1, 'Chicken', 'Meat', 'kg'),
    (2, 'Barley Flour', 'Grain', 'kg'),
    (3, 'Wheat Flour', 'Grain', 'kg'),
    (4, 'Teff Flour', 'Grain', 'kg'),
    (5, 'Meat (Beef, Lamb, or Goat)', 'Meat', 'kg'),
    (6, 'Beef Cubes', 'Meat', 'kg'),
    (7, 'Potatoes', 'Vegetable', 'kg'),
    (8, 'Chickpea Flour', 'Legume', 'kg'),
    (9, 'Lentils', 'Legume', 'kg'),
    (10, 'Spiced Butter (Niter Kibbeh)', 'Dairy', 'g'),
    (11, 'Onions', 'Vegetable', 'kg'),
    (12, 'Cottage Cheese (Ayib)', 'Dairy', 'g'),
    (13, 'Green Peppers', 'Vegetable', 'kg'),
    (14, 'Carrots', 'Vegetable', 'kg'),
    (15, 'Collard Greens', 'Vegetable', 'kg'),
    (16, 'Garlic', 'Vegetable', 'g'),
    (17, 'Rosemary', 'Herb', 'g'),
    (18, 'Green Beans', 'Vegetable', 'kg'),
    (19, 'Oil', 'Fat', 'ml'),
    (20, 'Mushrooms', 'Vegetable', 'kg'),
    (21, 'Berbere', 'Spice', 'g'),
    (22, 'Mitmita', 'Spice', 'g'),
    (23, 'Turmeric', 'Spice', 'g'),
    (24, 'Tomatoes', 'Vegetable', 'kg'),
    (25, 'Salt', 'Salt', 'g'),
    (26, 'Yeast', 'Leavening Agent', 'g'),
    (27, 'Water', 'Water', 'liters'),
    (28, 'Sugar', 'Sugar', 'g'),
    (29, 'Flaxseeds', 'Seed', 'g'),
    (30, 'Eggs', 'Eggs', 'pcs'),
    (31, 'Butter', 'Dairy', 'g'),
    (32, 'Baking Powder', 'Leavening Agent', 'g'),
    (33, 'Sugarcane', 'Vegetable', 'kg');


INSERT INTO Recipe_Ingredient (rec_id, ing_id, quantity, unit) VALUES
    -- Doro Wot
    (1, 1, 1, 'pcs'),       -- Chicken
    (1, 11, 300, 'g'),      -- Onions
    (1, 21, 20, 'g'),       -- Berbere
    (1, 30, 4, 'pcs'),      -- Eggs
    (1, 19, 50, 'ml'),      -- Oil

    -- Tibs
    (2, 5, 500, 'g'),       -- Meat (beef, lamb, or goat)
    (2, 11, 100, 'g'),      -- Onions
    (2, 13, 100, 'g'),      -- Green Peppers
    (2, 19, 30, 'ml'),      -- Oil

    -- Kitfo
    (3, 5, 300, 'g'),       -- Raw Beef
    (3, 22, 10, 'g'),       -- Mitmita
    (3, 10, 50, 'g'),       -- Spiced Butter (Niter Kibbeh)

    -- Gored Gored
    (4, 6, 400, 'g'),       -- Beef Cubes
    (4, 22, 10, 'g'),       -- Mitmita
    (4, 31, 50, 'g'),       -- Butter

    -- Siga Wot
    (5, 6, 500, 'g'),       -- Beef
    (5, 21, 20, 'g'),       -- Berbere
    (5, 11, 200, 'g'),      -- Onions
    (5, 19, 30, 'ml'),      -- Oil

    -- Alicha Tibs
    (6, 5, 400, 'g'),       -- Meat
    (6, 11, 150, 'g'),      -- Onions
    (6, 23, 10, 'g'),       -- Turmeric
    (6, 19, 30, 'ml'),      -- Oil

    -- Zilzil Tibs
    (7, 6, 500, 'g'),       -- Beef Strips
    (7, 11, 150, 'g'),      -- Onions
    (7, 17, 10, 'g'),       -- Rosemary
    (7, 19, 30, 'ml'),      -- Oil

    -- Ayib
    (8, 12, 400, 'g'),      -- Cottage Cheese

    -- Shiro Wot
    (9, 8, 300, 'g'),       -- Chickpea Flour
    (9, 21, 20, 'g'),       -- Berbere
    (9, 11, 100, 'g'),      -- Onions
    (9, 19, 30, 'ml'),      -- Oil

    -- Misir Wot
    (10, 9, 300, 'g'),      -- Lentils
    (10, 21, 20, 'g'),      -- Berbere
    (10, 11, 100, 'g'),     -- Onions
    (10, 19, 30, 'ml'),     -- Oil

    -- Gomen
    (11, 15, 500, 'g'),     -- Collard Greens
    (11, 11, 100, 'g'),     -- Onions
    (11, 16, 10, 'g'),      -- Garlic
    (11, 19, 30, 'ml'),     -- Oil

    -- Dinich Wot
    (12, 7, 500, 'g'),      -- Potatoes
    (12, 21, 20, 'g'),      -- Berbere
    (12, 11, 100, 'g'),     -- Onions
    (12, 19, 30, 'ml'),     -- Oil

    -- Fosolia
    (13, 18, 300, 'g'),     -- Green Beans
    (13, 14, 100, 'g'),     -- Carrots
    (13, 11, 100, 'g'),     -- Onions
    (13, 19, 30, 'ml'),     -- Oil

    -- Alicha Wot
    (14, 9, 300, 'g'),      -- Lentils
    (14, 23, 10, 'g'),      -- Turmeric
    (14, 11, 100, 'g'),     -- Onions
    (14, 19, 30, 'ml'),     -- Oil
	
    -- timatim salad
    (15, 24, 200, 'g'),
    (15, 11, 100, 'g'),
    (15, 19, 10, 'ml'),

    -- Enguday Tibs
    (16, 20, 300, 'g'),     -- Mushrooms
    (16, 11, 100, 'g'),     -- Onions
    (16, 17, 10, 'g'),      -- Rosemary
    (16, 19, 30, 'ml'),     -- Oil

    -- Injera
    (17, 4, 500, 'g'),      -- Teff Flour
    (17, 27, 500, 'ml'),    -- Water
    (17, 25, 5, 'g'),       -- Salt

    -- Dabo
    (18, 3, 500, 'g'),      -- Wheat Flour
    (18, 28, 100, 'g'),     -- Sugar
    (18, 26, 10, 'g'),      -- Yeast
    (18, 25, 5, 'g'),       -- Salt
    (18, 27, 250, 'ml'),    -- Water

    -- Beso Chibto
    (19, 2, 300, 'g'),      -- Barley Flour
    (19, 10, 100, 'g'),     -- Spiced Butter (Niter Kibbeh)
    (19, 28, 50, 'g'),      -- Sugar

    -- Kita
    (20, 3, 400, 'g'),      -- Wheat Flour
    (20, 25, 5, 'g'),       -- Salt
    (20, 19, 20, 'ml'),     -- Oil
    (20, 27, 200, 'ml'),    -- Water

    -- Chechebsa (Fasting Version)
    (21, 3, 300, 'g'),      -- Wheat Flour
    (21, 21, 30, 'g'),      -- Berbere
    (21, 19, 50, 'ml'),     -- Oil

    -- Beso
    (22, 2, 200, 'g'),      -- Barley Flour
    (22, 28, 20, 'g'),      -- Sugar
    (22, 27, 250, 'ml'),    -- Water

    -- Telba
    (23, 29, 50, 'g'),      -- Flaxseeds
    (23, 28, 20, 'g'),      -- Sugar
    (23, 27, 250, 'ml'),    -- Water

    -- Cake
    (24, 3, 500, 'g'),      -- Wheat Flour
    (24, 28, 200, 'g'),     -- Sugar
    (24, 30, 3, 'pcs'),     -- Eggs
    (24, 31, 100, 'g'),     -- Butter
    (24, 32, 5, 'g'),       -- Baking Powder

    -- Shenkora (Sugarcane)
    (25, 33, 500, 'g');     -- Sugarcane
    




INSERT INTO Nutrition (ing_id, calories, carbohydrates, protein, fat, sugar, fiber) VALUES
(1, 239.00, 0.00, 27.00, 14.00, 0.00, 0.00),     -- Chicken
(2, 354.00, 73.00, 10.00, 2.00, 1.00, 17.00),    -- Barley Flour
(3, 364.00, 76.00, 12.00, 1.00, 0.40, 2.70),     -- Wheat Flour
(4, 367.00, 73.00, 13.00, 2.00, 1.00, 8.00),     -- Teff Flour
(5, 250.00, 0.00, 26.00, 20.00, 0.00, 0.00),     -- Meat (Beef, Lamb, or Goat)
(6, 250.00, 0.00, 26.00, 20.00, 0.00, 0.00),     -- Beef Cubes
(7, 77.00, 17.00, 2.00, 0.10, 0.80, 2.20),       -- Potatoes
(8, 387.00, 58.00, 22.00, 6.00, 10.00, 10.00),   -- Chickpea Flour
(9, 116.00, 20.00, 9.00, 0.40, 1.80, 8.00),      -- Lentils
(10, 717.00, 0.00, 0.85, 81.00, 0.06, 0.00),     -- Spiced Butter (Niter Kibbeh)
(11, 40.00, 9.00, 1.10, 0.10, 4.20, 1.70),       -- Onions
(12, 98.00, 3.40, 11.00, 4.00, 0.10, 0.00),      -- Cottage Cheese (Ayib)
(13, 20.00, 4.64, 0.86, 0.17, 2.40, 1.70),       -- Green Peppers
(14, 41.00, 10.00, 0.90, 0.20, 4.70, 2.80),      -- Carrots
(15, 33.00, 7.00, 2.00, 0.50, 0.00, 4.00),       -- Collard Greens
(16, 149.00, 33.00, 6.30, 0.50, 1.00, 2.10),     -- Garlic
(17, 131.00, 20.00, 4.90, 5.90, 0.00, 14.00),    -- Rosemary
(18, 31.00, 7.10, 1.80, 0.20, 3.60, 3.40),       -- Green Beans
(19, 884.00, 0.00, 0.00, 100.00, 0.00, 0.00),    -- Oil
(20, 22.00, 3.26, 3.09, 0.34, 0.92, 1.00),       -- Mushrooms
(21, 300.00, 58.00, 12.00, 4.00, 0.00, 8.00),    -- Berbere
(22, 278.00, 50.00, 11.00, 3.00, 0.00, 7.00),    -- Mitmita
(23, 312.00, 70.00, 7.00, 4.00, 0.00, 20.00),    -- Turmeric
(24, 18.00, 3.90, 0.90, 0.20, 2.60, 1.20),       -- Tomatoes
(25, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00),        -- Salt
(26, 325.00, 66.00, 12.00, 1.20, 0.00, 0.00),    -- Yeast
(27, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00),        -- Water
(28, 387.00, 100.00, 0.00, 0.00, 100.00, 0.00),  -- Sugar
(29, 534.00, 28.88, 18.29, 42.16, 1.55, 27.30),  -- Flaxseeds
(30, 143.00, 1.10, 12.60, 9.90, 0.10, 0.00),     -- Eggs
(31, 717.00, 0.00, 0.85, 81.00, 0.06, 0.00),     -- Butter
(32, 53.00, 27.00, 0.50, 0.00, 0.00, 1.00),      -- Baking Powder
(33, 269.00, 63.00, 0.60, 0.00, 63.00, 0.00);    -- Sugarcane


INSERT INTO Allerg_desc (allerg_name, allerg_desc) VALUES
    ('Gluten', 'A protein found in wheat, barley, rye, or related grains, potentially present in processed ingredients.'),
    ('Dairy', 'Common allergen found in dairy products such as butter, cheese, and milk.'),
    ('Eggs', 'A common allergen found in egg whites and yolks.'),
    ('Soy', 'Common allergen found in soybeans and soy-based products.'),
    ('Nuts', 'Common allergen found in tree nuts and peanuts, which can cause severe allergic reactions.'),
    ('Sesame', 'Common allergen found in sesame seeds and products containing sesame oil or sesame paste.'),
    ('Fish', 'Common allergen found in fish such as salmon, tuna, and other seafood.'),
    ('Shellfish', 'Common allergen found in shellfish, including shrimp, crab, and lobster.'),
    ('None', 'Ingredient does not contain common allergens but may have cross-contamination risks.');


-- Insert allergen data for ingredients with known allergens
INSERT INTO Allergy (ing_id, allerg_name) VALUES
    (3, 'Gluten'),  -- Wheat Flour (Gluten)
    (2, 'Gluten'),  -- Barley Flour (Gluten)
    (10, 'Dairy'),  -- Niter Kibbe (Dairy)
    (12, 'Dairy'),  -- Cottage Cheese (Ayib) (Dairy)
    (31, 'Dairy'),  -- Butter (Dairy)
    (30, 'Eggs'),   -- Eggs
    (26, 'Gluten'); -- Flour (Gluten)
    
-- Populate the Menu table with meal types
INSERT INTO Menu (meal_type) VALUES
    ('breakfast'),
    ('lunch'),
    ('dinner'),
    ('drinks'),
    ('dessert');
    

-- Inserting into Menu_Recipe with corrected recipe IDs
INSERT INTO Menu_Recipe (menu_id, rec_id) VALUES
-- Breakfast Menu
(1, 17), -- Injera
(1, 18), -- Dabo
(1, 19), -- Beso Chibto
(1, 23), -- Telba
(1, 20), -- Kita
(1, 21), -- Chechebsa (Fasting Version)

-- Lunch Menu
(2, 1),  -- Doro Wot
(2, 2),  -- Tibs
(2, 5),  -- Siga Wot
(2, 11), -- Gomen
(2, 3),  -- Kitfo
(2, 12), -- Dinich Wot
(2, 13), -- Fosolia

-- Dinner Menu
(3, 1),  -- Doro Wot
(3, 6),  -- Alicha Tibs
(3, 8),  -- Ayib
(3, 9),  -- Shiro Wot
(3, 4),  -- Gored Gored
(3, 10), -- Misir Wot
(3, 7),  -- Zilzil Tibs

-- Drinks Menu
(4, 23), -- Telba
(4, 22), -- Beso

-- Dessert Menu
(5, 24), -- Cake
(5, 25); -- Shenkora (Sugarcane)






-- ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
-- show all the recipe with their ingredient 

CREATE VIEW RecipeIngredientsView AS
SELECT 
    r.rec_id,
    r.rec_name,
    i.ing_name,
    ri.quantity,
    ri.unit
FROM 
    Recipe r
JOIN 
    Recipe_Ingredient ri ON r.rec_id = ri.rec_id
JOIN 
    Ingredient i ON ri.ing_id = i.ing_id;
    
select * from RecipeIngredientsView;

-- ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

CREATE VIEW MenuView AS
SELECT 
    m.menu_id,
    m.meal_type,
    r.rec_id,
    r.rec_name,
    r.description,
    r.cooking_time,
    r.servings,
    r.price
FROM 
    Menu m
JOIN 
    Menu_Recipe mr ON m.menu_id = mr.menu_id
JOIN 
    Recipe r ON mr.rec_id = r.rec_id;

SELECT * FROM  MenuView;

-- ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

-- each ingredient nutritional value 

CREATE VIEW IngredientNutritionView AS
SELECT DISTINCT
    i.ing_name,
    n.calories,
    n.carbohydrates,
    n.protein,
    n.fat,
    n.sugar,
    n.fiber
FROM 
    Ingredient i
JOIN 
    Nutrition n ON i.ing_id = n.ing_id;

SELECT * FROM IngredientNutritionView;

-- ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

 -- recipe and all their nutritional value
 
 CREATE VIEW All_Recipe_Nutrition_View AS
SELECT 
    R.rec_id,
    R.rec_name,
    SUM(N.calories) AS total_calories,
    SUM(N.carbohydrates) AS total_carbohydrates,
    SUM(N.protein) AS total_protein,
    SUM(N.fat) AS total_fat,
    SUM(N.sugar) AS total_sugar,
    SUM(N.fiber) AS total_fiber
FROM 
    Recipe R
JOIN 
    Recipe_Ingredient RI ON R.rec_id = RI.rec_id
JOIN 
    Nutrition N ON RI.ing_id = N.ing_id
GROUP BY 
    R.rec_id, R.rec_name
ORDER BY 
    R.rec_id;
select * from All_Recipe_Nutrition_View;

-- ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

-- recipe and their allergen

CREATE VIEW RecipeAllergensView AS
SELECT DISTINCT 
    r.rec_id,
    r.rec_name,
    a.allerg_name
FROM 
    Recipe r
JOIN 
    Recipe_Ingredient ri ON r.rec_id = ri.rec_id
JOIN 
    Allergy a ON ri.ing_id = a.ing_id;
    
SELECT * FROM RecipeAllergensView;


-- ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

-- allergy free recipes

CREATE VIEW AllergyFreeRecipesView AS  
SELECT 
    r.rec_id,
    r.rec_name,
    r.description,
    r.cooking_time,
    r.servings,
    r.price
FROM 
    Recipe r
WHERE 
    r.rec_id NOT IN (
        SELECT DISTINCT ri.rec_id
        FROM Recipe_Ingredient ri
        JOIN Allergy a ON ri.ing_id = a.ing_id
        WHERE a.allerg_name IN ('Gluten', 'Dairy', 'Eggs'   )
    );

SELECT * FROM AllergyFreeRecipesView;


-- ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

-- vegiterian recipes

CREATE VIEW VegetarianRecipesView AS
SELECT 
    r.rec_id,
    r.rec_name,
    r.description,
    r.cooking_time,
    r.servings,
    r.price
FROM 
    Recipe r
WHERE 
    r.rec_id NOT IN (
        SELECT DISTINCT ri.rec_id 
        FROM Recipe_Ingredient ri
        JOIN Ingredient i ON ri.ing_id = i.ing_id
        WHERE i.category = 'Meat'
    );
    
SELECT * FROM VegetarianRecipesView;

-- ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

-- fasting recipes 

CREATE VIEW FastingRecipesView AS
SELECT 
    r.rec_id,
    r.rec_name,
    r.description,
    r.cooking_time,
    r.servings,
    r.price
FROM 
    Recipe r
WHERE 
    r.rec_id NOT IN (
        SELECT DISTINCT ri.rec_id
        FROM Recipe_Ingredient ri
        JOIN Ingredient i ON ri.ing_id = i.ing_id
        WHERE i.category IN ('Meat', 'Dairy', 'Eggs' )
    );
    
-- ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

-- recipe for specific fasting days

SELECT * FROM FastingRecipesView;

SELECT r.rec_id, r.rec_name
FROM Recipe r
WHERE NOT EXISTS (
    SELECT 1
    FROM Recipe_Ingredient ri
    JOIN Ingredient i ON ri.ing_id = i.ing_id
    WHERE r.rec_id = ri.rec_id AND i.category IN ('meat', 'dairy' , 'Eggs')
)
AND DAYNAME(CURDATE()) IN ('Wednesday', 'Friday'); 

-- ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

-- recipe and the number of ingredient they use 

CREATE VIEW Recipes_With_Multiple_Ingredients AS
SELECT R.rec_name, COUNT(RI.ing_id) AS ingredient_count
FROM Recipe R
JOIN Recipe_Ingredient RI ON R.rec_id = RI.rec_id
GROUP BY R.rec_name
HAVING COUNT(RI.ing_id) > 3;

select * from Recipes_With_Multiple_Ingredients;

-- ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

-- filter high calories ingredient 

CREATE VIEW High_Calorie_Ingredients AS
SELECT I.ing_name, N.calories
FROM Ingredient I
JOIN Nutrition N ON I.ing_id = N.ing_id
WHERE N.calories > 500;

select * from High_Calorie_Ingredients;

-- ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

-- recipe with price above average recipe price 

CREATE VIEW Expensive_Recipes AS
SELECT rec_name, price
FROM Recipe
WHERE price < (SELECT AVG(price) FROM Recipe);

select * from Expensive_Recipes;

-- ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

-- recipe with price above average recipe price 

CREATE VIEW Dairy_Ingredients AS
SELECT ing_name, category
FROM Ingredient
WHERE category LIKE '%dairy%';

select * from  Dairy_Ingredients;

-- ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

-- sugar free recipe

CREATE VIEW Sugar_Free_Recipes AS
SELECT R.rec_name
FROM Recipe R
WHERE R.rec_id NOT IN (
    SELECT RI.rec_id
    FROM Recipe_Ingredient RI
    JOIN Ingredient I ON RI.ing_id = I.ing_id
    WHERE I.ing_name LIKE '%sugar%'
);

select * from Sugar_Free_Recipes;

