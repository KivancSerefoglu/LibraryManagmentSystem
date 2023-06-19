CREATE TABLE Book(
	bookID INT NOT NULL,
	name text NOT NULL,
    amount INT,
    genreName varchar(100),
    isActive INT,
	primary key (bookID),
    FOREIGN KEY (genreName) REFERENCES Genre (genreName));

CREATE TABLE User(
	userID INT NOT NULL,
	name text NOT NULL,
    email text,
    address text,
    isActive INT,
    primary key(userID));
    
CREATE TABLE History(
	checkoutID INT NOT NULL,
	userID INT NOT NULL,
    bookID INT NOT NULL,
    returned INT default 0,

    PRIMARY KEY (userID, bookID, checkoutID),
    FOREIGN KEY (bookID) REFERENCES Book (bookID),
    FOREIGN KEY (userID) REFERENCES User (userID));
    
    
CREATE TABLE Genre(
	genreID INT,
	genreName varchar(100) UNIQUE,
	primary key(genreID)
);
    
