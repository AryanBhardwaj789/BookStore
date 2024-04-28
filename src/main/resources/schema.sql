CREATE TABLE bookdata(
id LONG AUTO_INCREMENT,
title VARCHAR(255) PRIMARY KEY,
author VARCHAR(255),
isbn LONG,
price VARCHAR(255),
description VARCHAR(255)
);

CREATE TABLE userdata(
username VARCHAR(255) PRIMARY KEY,
password VARCHAR(255),
email VARCHAR(255)
);

CREATE TABLE orderdata (
id LONG AUTO_INCREMENT,
book_title VARCHAR(255),
username VARCHAR(255),
quantity INT,
PRIMARY KEY (id),
FOREIGN KEY (book_title) REFERENCES bookdata(title),
FOREIGN KEY (username) REFERENCES userdata(username)
);


