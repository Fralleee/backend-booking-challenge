CREATE TABLE IF NOT EXISTS `bookings`
(
    `id`                        INT UNSIGNED auto_increment PRIMARY KEY,
    `property_id`               INT UNSIGNED NOT NULL,
    `tenant_id`                 INT UNSIGNED NOT NULL,
    `start_date`                DATETIME NOT NULL,
    `end_date`                  DATETIME NOT NULL,

    FOREIGN KEY (`property_id`) REFERENCES `properties` (`id`),
    FOREIGN KEY (`tenant_id`) REFERENCES `tenants` (`id`)
);

CREATE TABLE IF NOT EXISTS `blocks`
(
    `id`                        INT UNSIGNED auto_increment PRIMARY KEY,
    `property_id`               INT UNSIGNED NOT NULL,
    `start_date`                DATETIME NOT NULL,
    `end_date`                  DATETIME NOT NULL,

    FOREIGN KEY (`property_id`) REFERENCES `properties` (`id`)
);

CREATE TABLE IF NOT EXISTS `tenants`
(
    `id`                        INT UNSIGNED auto_increment PRIMARY KEY,
    `name`                      VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS `properties`
(
    `id`                        INT UNSIGNED auto_increment PRIMARY KEY,
    `name`                      VARCHAR(255) NOT NULL
);
