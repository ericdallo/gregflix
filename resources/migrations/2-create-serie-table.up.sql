CREATE TABLE IF NOT EXISTS serie (
    `id` bigint(11) NOT NULL AUTO_INCREMENT,
    `title` varchar(50) NOT NULL,
    `slug` varchar(100) NOT NULL,
    `description` varchar(250) NOT NULL,
    `preview` varchar(200) NOT NULL,
    `url` varchar(200) NOT NULL,
    `season` smallint(2) NOT NULL,
    `episode` smallint(2) NOT NULL,
    PRIMARY KEY (`id`)
);