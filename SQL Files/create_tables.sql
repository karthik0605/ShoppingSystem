CREATE TABLE Customers (
	cID INT PRIMARY KEY AUTO_INCREMENT,
    cname VARCHAR(100) NOT NULL,
    address VARCHAR(100) NOT NULL
);

CREATE TABLE Items (
	iID INT PRIMARY KEY AUTO_INCREMENT,
    iname VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description VARCHAR(200) NOT NULL,
    catname ENUM('Men', 'Women', 'House Appliance', 'Plants', 'Electronics'),
    stock INT NOT NULL
);

CREATE TABLE Carted (
	cID INT NOT NULL,
    iID INT NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    PRIMARY KEY (cID, iID),
    FOREIGN KEY (cID) REFERENCES Customers(cID),
    FOREIGN KEY (iID) REFERENCES Items(iID)
);	

CREATE TABLE Purchases (
	pID INT PRIMARY KEY AUTO_INCREMENT,
	cID INT NOT NULL,
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    price double NOT NULL,
    FOREIGN KEY (cID) REFERENCES Customers(cID)
);

CREATE TABLE Reviews (
	rID INT PRIMARY KEY AUTO_INCREMENT,
	iID INT NOT NULL,
    review varchar(200) NOT NULL,
    FOREIGN KEY (iID) REFERENCES Items(iID)
);


-- drop table Carted;
-- drop table Purchases;
-- drop table Items;
-- drop table Reviews;
-- drop table Categories;
-- drop table Customers;