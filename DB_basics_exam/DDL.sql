create table planets (
	id	INT PRIMARY KEY AUTO_INCREMENT,
	name	VARCHAR(30) NOT NULL
);

create table spaceports(
	id	INT PRIMARY KEY AUTO_INCREMENT,
   name VARCHAR(50) NOT NULL,	
	planet_id INT
);

create table spaceships(
	id	INT Primary Key AUTO_INCREMENT,
	name	VARCHAR(50) NOT NULL, 
	manufacturer VARCHAR(30) NOT NULL,
	light_speed_rate INT DEFAULT 0
);

create table colonists(
	id	INT Primary Key 	AUTO_INCREMENT,
	first_name VARCHAR(20) NOT NULL,
	last_name VARCHAR(20) NOT NULL,
	ucn CHAR(10) NOT NULL UNIQUE,
	birth_date DATE NOT NULL
);

create table journeys(
	id	INT Primary Key AUTO_INCREMENT,
	journey_start DATETIME NOT NULL,
	journey_end	DATETIME NOT NULL,
	purpose ENUM('Medical', 'Technical', 'Educational', 'Military'),
	destination_spaceport_id INT,
	spaceship_id INT
);

create table travel_cards(
	id	INT Primary Key	AUTO_INCREMENT,	
	card_number CHAR(10) NOT NULL UNIQUE,
	job_during_journey ENUM('Pilot', 'Engineer', 'Trooper', 'Cleaner', 'Cook'),
	colonist_id	INT,
	journey_id	INT
);

ALTER TABLE spaceports
ADD CONSTRAINT `fk_spaceports_planets` 
FOREIGN KEY (planet_id)
REFERENCES planets(id);

ALTER TABLE journeys
ADD CONSTRAINT `fk_journeyes_spaceports` 
FOREIGN KEY (destination_spaceport_id)
REFERENCES spaceports(id);

ALTER TABLE journeys
ADD CONSTRAINT `fk_journeyes_spaceships` 
FOREIGN KEY (spaceship_id)
REFERENCES spaceships(id);

ALTER TABLE travel_cards
ADD CONSTRAINT `fk_travel_cards_colonists` 
FOREIGN KEY (colonist_id)
REFERENCES colonists(id);

ALTER TABLE travel_cards
ADD CONSTRAINT `fk_travel_cards_journeys` 
FOREIGN KEY (journey_id)
REFERENCES journeys(id);