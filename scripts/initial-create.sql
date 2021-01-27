CREATE TABLE ip_data (
    ip_data_id BIGINT NOT NULL AUTO_INCREMENT,
    country VARCHAR(50),
    iso_code VARCHAR(10) UNIQUE,
    estimated_distance BIGINT,
    currency VARCHAR(3),
    invocations BIGINT,
    PRIMARY KEY(ip_data_id)
);

CREATE TABLE ip_data_timezone (
    ip_data_timezone_id BIGINT NOT NULL AUTO_INCREMENT,
    ip_data_timezone_descr VARCHAR(20),
    ip_data_id BIGINT REFERENCES ip_data(ip_data_id),
    PRIMARY KEY (ip_data_timezone_id)
);

CREATE TABLE ip_data_language (
    ip_data_language_id BIGINT NOT NULL AUTO_INCREMENT,
    ip_data_language_descr VARCHAR(20),
    ip_data_id BIGINT REFERENCES ip_data(ip_data_id),
    PRIMARY KEY (ip_data_language_id)
);