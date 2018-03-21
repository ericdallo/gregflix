CREATE TABLE IF NOT EXISTS login_audit (
    `id` bigint(11) NOT NULL AUTO_INCREMENT,
    `ip` varchar(15) NOT NULL,
    `user_id` bigint(11) NOT NULL,
    `device` varchar(20) NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT NOW(),
    `updated_at` timestamp NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);
