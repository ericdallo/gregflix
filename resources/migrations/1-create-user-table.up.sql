CREATE TABLE IF NOT EXISTS user (
    `id` bigint(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(50) NOT NULL,
    `username` varchar(30) NOT NULL,
    `password` varchar(70) NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT NOW(),
    `updated_at` timestamp NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_user_name`(`username`)
);