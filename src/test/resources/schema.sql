CREATE SCHEMA IF NOT EXISTS dbtechchallange;

CREATE TABLE IF NOT EXISTS dbtechchallange."order" (
    `id` VARCHAR(255) NOT NULL,
    `number_order` INT NOT NULL,
    `status` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS dbtechchallange."item" (
    `order_id` VARCHAR(255) NOT NULL,
    `sku` VARCHAR(255) NOT NULL,
    `quantity` INT NOT NULL,
    `unit_value` DECIMAL(10, 0) NOT NULL,
    FOREIGN KEY (`order_id`) REFERENCES dbtechchallange."order"(`id`)
);

SET REFERENTIAL_INTEGRITY TRUE;