CREATE TABLE IF NOT EXISTS current_serie (
    `id` bigint(11) NOT NULL AUTO_INCREMENT,
    `user_id` bigint(11) NOT NULL,
    `serie_slug` varchar(100) NOT NULL,
    `season` smallint(2) NOT NULL,
    `episode` smallint(2) NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT NOW(),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    FOREIGN KEY (`serie_id`) REFERENCES `serie` (`id`)
);
