CREATE TABLE cdb_order (
    id CHAR(36) NOT NULL PRIMARY KEY,
    customer_id CHAR(36) NOT NULL,
    product_id CHAR(36) NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    transaction_type INT NOT NULL,
    transaction_date DATETIME(6) NOT NULL,

    CONSTRAINT order_customer_FK FOREIGN KEY (customer_id) REFERENCES customer(id),
    CONSTRAINT order_product_FK FOREIGN KEY (product_id) REFERENCES product(id)
);