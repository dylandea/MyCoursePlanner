-- ------------------------------------------------------------------------------
-- - Reconstruction de la base de données                                     ---
-- ------------------------------------------------------------------------------

DROP DATABASE IF EXISTS MyCoursePlanner;
CREATE DATABASE MyCoursePlanner;
USE MyCoursePlanner;


-- -----------------------------------------------------------------------------
-- - Construction de la table des utilisateurs                               ---
-- -----------------------------------------------------------------------------
CREATE TABLE T_Users (
	IdUser				INT(4) 		 PRIMARY KEY AUTO_INCREMENT,
	Login				varchar(20)	NOT NULL UNIQUE,
	Password			varchar(20)	NOT NULL
) ENGINE = InnoDB;

INSERT INTO T_Users ( Login, Password) VALUES (  'admin' ,	'Skeletal_Nikotine!2' );

SELECT * FROM T_Users;

-- -----------------------------------------------------------------------------
-- - Construction de la table des admin	                                 ---
-- -----------------------------------------------------------------------------

CREATE TABLE T_Admins (
	IdAdmin					INT(4) 		 PRIMARY KEY AUTO_INCREMENT,
	lastName				varchar(30)	NOT NULL,
	firstName				varchar(30)	NOT NULL,
	email 					varchar(30)	NOT NULL unique,
	idUser					INT(4)		NOT NULL,
	FOREIGN KEY (idUser)	REFERENCES T_Users(idUser)
) ENGINE = InnoDB;

-- -----------------------------------------------------------------------------
-- - Construction de la table des clients	                                 ---
-- -----------------------------------------------------------------------------

CREATE TABLE T_Customers (
	IdCustomer				INT(4) 		 PRIMARY KEY AUTO_INCREMENT,
	lastName				varchar(30)	NOT NULL,
	firstName				varchar(30)	NOT NULL,
	email 					varchar(30)	NOT NULL unique,
	phone 					varchar(20)	,
	address					varchar(50)	,
	idUser					INT(4)		NOT NULL,
	FOREIGN KEY (idUser)	REFERENCES T_Users(idUser)
) ENGINE = InnoDB;

-- -----------------------------------------------------------------------------
-- - Construction de la table des catégories d'articles
-- -----------------------------------------------------------------------------

CREATE TABLE T_Categories (
	IdCategory 				INT(4) 		 PRIMARY KEY AUTO_INCREMENT,
	CatName 				VARCHAR(30)  NOT NULL
) ENGINE = InnoDB;

insert into T_Categories (IdCategory, CatName) values (1 , 'Bureautique');
insert into T_Categories (IdCategory, CatName) values (2 , 'Programmation');
insert into T_Categories (IdCategory, CatName) values (3 , 'Graphisme');

select * from t_categories;

-- -----------------------------------------------------------------------------
-- - Construction de la table des articles en vente                         ---
-- -----------------------------------------------------------------------------
CREATE TABLE T_Courses (
	IdCourse				INT(4) 		 PRIMARY KEY AUTO_INCREMENT,
	Name					varchar(30)	NOT NULL,
	Description				varchar(100)	NOT NULL,
	DaysLength					int(4) NOT NULL,
	IsRemote				tinyint(1) NOT NULL,
	UnitaryPrice			float(8)	NOT NULL DEFAULT 0,
	IdCategory				int(4),
	FOREIGN KEY (IdCategory)	REFERENCES T_Categories(IdCategory)
) ENGINE = InnoDB;

INSERT INTO T_Courses ( Name, Description, DaysLength , IsRemote, UnitaryPrice, IdCategory) VALUES ( 'Java'     ,	'Java SE 8 : Syntaxe et POO', 2, 0, 15.99, 2);
INSERT INTO T_Courses ( Name, Description, DaysLength , IsRemote, UnitaryPrice, IdCategory) VALUES ( 'PHP frameworks'     ,	'Symphony et Laravel', 4, 1, 22, 2);
INSERT INTO T_Courses ( Name, Description, DaysLength , IsRemote, UnitaryPrice, IdCategory) VALUES ( 'Spring'     ,	'Spring Core/MVC/Security', 5, 0, 30, 2);


INSERT INTO T_Courses ( Name, Description, DaysLength , IsRemote, UnitaryPrice, IdCategory) VALUES ( 'Blender'     ,	'Modelisation 3D, Design Objets', 14, 1, 35, 3);

INSERT INTO T_Courses ( Name, Description, DaysLength , IsRemote, UnitaryPrice, IdCategory) VALUES ( 'Microsoft Office'     ,	'Office LTE 21.18, Prise en main', 5, 1, 31, 1);
INSERT INTO T_Courses ( Name, Description, DaysLength , IsRemote, UnitaryPrice, IdCategory) VALUES ( 'Excel avance'     ,	'Computations complexes', 8, 0, 49.99, 1);

SELECT * FROM T_Courses;


-- -----------------------------------------------------------------------------
-- - Construction de la table des commandes    
-- -----------------------------------------------------------------------------


CREATE TABLE T_Orders (
	IdOrder				INT(4) 		 PRIMARY KEY AUTO_INCREMENT,
	Amount				float(4)	NOT NULL DEFAULT 0,
	DateOrder 			DATE		NOT NULL DEFAULT NOW(),
	IdCustomer          INT(4)   	NOT NULL,
	FOREIGN KEY(IdCustomer) REFERENCES T_Customers(IdCustomer)
) ENGINE = InnoDB;



CREATE TABLE T_Order_Items (
	IdOrderItem			INT(4) 		 PRIMARY KEY AUTO_INCREMENT,
	
	IdCourse           INT(4)   NOT NULL,
	FOREIGN KEY(IdCourse) REFERENCES T_Courses(IdCourse),
	
	Price		FLOAT(4)	NOT NULL DEFAULT 0,
	
	IdOrder             INT(4)   NOT NULL,
	FOREIGN KEY(IdOrder) REFERENCES T_Orders(IdOrder)
) ENGINE = InnoDB;