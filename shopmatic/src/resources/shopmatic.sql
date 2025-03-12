CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    credit_card_info VARCHAR(100),
    age INT,
    gender VARCHAR(20),
    location VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    seller_id INT,
    category VARCHAR(50),
    subcategory VARCHAR(50),
    description TEXT,
    price DECIMAL(10,2),
    FOREIGN KEY (seller_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS cart_items (
    cart_item_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    item_id INT,
    quantity INT DEFAULT 1,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (item_id) REFERENCES items(item_id)
);

CREATE TABLE IF NOT EXISTS purchases (
    purchase_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    total_items INT,
    total_amount DECIMAL(10,2),
    purchase_date DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);


INSERT INTO users (name, email) VALUES ('Test User', 'test@example.com');

INSERT INTO items (seller_id, category, subcategory, description, price)
VALUES (1, 'Men', 'Shirt', 'Casual Cotton Shirt', 19.99);
