CREATE TABLE member
(
    member_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    is_dark VARCHAR(1) NOT NULL,
    is_delete VARCHAR(1) NOT NULL,
    reg_date DATETIME NOT NULL,
    mod_date DATETIME NOT NULL,
    sns_type VARCHAR(255) NOT NULL,
    oauth2_client_id VARCHAR(255) NOT NULL,
    role VARCHAR(10) NOT NULL
);

CREATE TABLE weather
(
    weather_id INT AUTO_INCREMENT PRIMARY KEY,
    weather_name VARCHAR(10) NOT NULL
);

CREATE TABLE condition_type
(
    cond_type_id INT AUTO_INCREMENT PRIMARY KEY,
    cond_type_name VARCHAR(10) NOT NULL,
    cond_type_value INT NOT NULL
);

CREATE TABLE conditions
(
    cond_id INT AUTO_INCREMENT PRIMARY KEY,
    cond_name VARCHAR(10) NOT NULL,
    cond_type_id INT NOT NULL,
    cond_value INT NOT NULL,
    FOREIGN KEY (cond_type_id) REFERENCES condition_type(cond_type_id)
);

CREATE TABLE dairy
(
    dairy_id INT AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL,
    member_id INT NOT NULL,
    weather_id INT NOT NULL,
    score DOUBLE NOT NULL,
    content VARCHAR(600) NULL,
    hope VARCHAR(255) NULL,
    thank VARCHAR(255) NULL,
    learn VARCHAR(255) NULL,
    is_delete VARCHAR(1) NOT NULL,
    reg_date DATETIME NOT NULL,
    mod_date DATETIME NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member(member_id),
    FOREIGN KEY (weather_id) REFERENCES weather(weather_id)
);

CREATE TABLE dairy_condition
(
    dairy_cond_id INT AUTO_INCREMENT PRIMARY KEY,
    dairy_id INT NOT NULL,
    cond_id INT NOT NULL,
    is_delete VARCHAR(1) NOT NULL,
    reg_date DATETIME NOT NULL,
    mod_date DATETIME NOT NULL,
    FOREIGN KEY (dairy_id) REFERENCES dairy(dairy_id),
    FOREIGN KEY (cond_id) REFERENCES conditions(cond_id)
);