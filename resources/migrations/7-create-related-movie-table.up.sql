CREATE TABLE IF NOT EXISTS related_movie (
    `id` bigint(11) NOT NULL AUTO_INCREMENT,
    `current_movie_id` bigint(11) NOT NULL,
    `related_movie_id` bigint(11) NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`current_movie_id`) REFERENCES `movie` (`id`),
    FOREIGN KEY (`related_movie_id`) REFERENCES `movie` (`id`)
);