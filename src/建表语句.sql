CREATE TABLE user(
    username VARCHAR(15) PRIMARY KEY,
    timestamp TIMESTAMP,
    password CHAR(64),
    level TINYINT
);
CREATE TABLE department(
    id INT PRIMARY KEY,
    name VARCHAR(10)
);
CREATE TABLE staff(
    id INT PRIMARY KEY,
    name VARCHAR(10),
    gender ENUM('男','女'),
    birthday DATE,
    telephone CHAR(11),
    department INT,
    position VARCHAR(10),
    salary DOUBLE,
    CONSTRAINT FOREIGN KEY(department) REFERENCES department(id)
);