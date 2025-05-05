CREATE TABLE carts (
                       id BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) NOT NULL PRIMARY KEY,
                       date_created DATE DEFAULT (CURDATE()) NOT NULL
);

CREATE TABLE cart_Items (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           cart_id BINARY(16) NOT NULL,
                           product_id BIGINT NOT NULL,
                           quantity INT DEFAULT 1 NOT NULL,
                           CONSTRAINT cartItems_cart_product_unique UNIQUE (cart_id, product_id),
                           CONSTRAINT cartItems_carts_id_fk FOREIGN KEY (cart_id) REFERENCES carts (id) ON DELETE CASCADE,
                           CONSTRAINT cartItems_products_id_fk FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE
);
