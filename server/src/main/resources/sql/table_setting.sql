CREATE TABLE user
(
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    user_name VARCHAR(255) NOT NULL,
    isDelete VARCHAR(1) NOT NULL,
    reg_date DATETIME NOT NULL,
    mod_date DATETIME NOT NULL,
    snsType VARCHAR(255) NOT NULL
);

CREATE TABLE weather
(
    weather_id INT AUTO_INCREMENT PRIMARY KEY,
    weather_name VARCHAR(10) NOT NULL,
    weather_icon VARCHAR(255) NOT NULL
);

CREATE TABLE conditions
(
    cond_id INT AUTO_INCREMENT PRIMARY KEY,
    cond_name VARCHAR(10) NOT NULL,
    cond_type_id INT NOT NULL,
    cond_value INT NOT NULL,
    FOREIGN KEY (cond_type_id) REFERENCES condition_type(cond_type_id)
);

CREATE TABLE condition_type
(
    cond_type_id INT AUTO_INCREMENT PRIMARY KEY,
    cond_type_name VARCHAR(10) NOT NULL,
    cond_type_value INT NOT NULL
);

CREATE TABLE dairy
(
    dairy_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    score FLOAT NOT NULL,
    cond_id INT NOT NULL,
    content VARCHAR(600) NULL,
    hope VARCHAR(255) NULL,
    thank VARCHAR(255) NULL,
    learn VARCHAR(255) NULL,
    reg_date DATETIME NOT NULL,
    mod_date DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (cond_id) REFERENCES conditions(cond_id)
);

CREATE TABLE daily_score
(
    daily_score_no INT PRIMARY KEY,
    daily_score_icon VARCHAR(255) NOT NULL
);