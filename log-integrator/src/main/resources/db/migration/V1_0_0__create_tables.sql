CREATE TABLE IF NOT EXISTS log
(
    id          bigint       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    logger_name varchar(50)  NOT NULL,
    thread_name varchar(50)  NOT NULL,
    `type`      varchar(50)  NOT NULL,
    message     varchar(255) NOT NULL,
    stack_trace text
);

CREATE TABLE IF NOT EXISTS service
(
    id          bigint      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name        varchar(50) NOT NULL,
    instance_id varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS error
(
    id         UUID              DEFAULT RANDOM_UUID() PRIMARY KEY,
    date_time  datetime NOT NULL DEFAULT NOW(),
    service_id bigint references service (id),
    log_id     bigint references log (id)
);