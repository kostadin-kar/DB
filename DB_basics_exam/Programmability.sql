DELIMITER $$
create function udf_count_colonists_by_destination_planet(planet_name VARCHAR(30)) 
RETURNS INT
DETERMINISTIC
BEGIN
	DECLARE people_sent INT;
	SET people_sent := (
					SELECT COUNT(c.id) as people_count
					FROM planets AS p
					inner join spaceports as s
						on p.id = s.planet_id
					inner join journeys as j
						on s.id = j.destination_spaceport_id
					inner join travel_cards as tc
						on j.id = tc.journey_id
					inner join colonists as c
						on tc.colonist_id = c.id
					WHERE p.name = planet_name
					GROUP BY p.id
					);
	RETURN people_sent;
END $$
DELIMITER ;

SELECT p.name, udf_count_colonists_by_destination_planet('Otroyphus') AS count
FROM planets AS p
WHERE p.name = 'Otroyphus';
#----------------------------------
DELIMITER $$
create function udf_count_colonists_by_destination_planet(planet_name VARCHAR(30)) 
RETURNS INT
DETERMINISTIC
BEGIN
	DECLARE people_sent INT;
	SET people_sent := (
					SELECT COUNT(tc.id) as people_count
					FROM planets AS p
					left join spaceports as s
						on p.id = s.planet_id
					left join journeys as j
						on s.id = j.destination_spaceport_id
					left join travel_cards as tc
						on j.id = tc.journey_id
					WHERE p.name = planet_name
					GROUP BY p.id
					);
	RETURN people_sent;
END $$
DELIMITER ;

drop procedure udf_count_colonists_by_destination_planet;

#16
drop procedure udp_modify_spaceship_light_speed_rate;

DELIMITER $$
CREATE PROCEDURE udp_modify_spaceship_light_speed_rate(spaceship_name VARCHAR(50), light_speed_rate_increse INT(11))
BEGIN 
	START TRANSACTION;
	IF 1 != (SELECT COUNT(s.name) FROM spaceships AS s WHERE s.name = spaceship_name)
		THEN 
			SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'Spaceship you are trying to modify does not exists.';
			ROLLBACK;
	END IF;
	
	UPDATE spaceships
	SET light_speed_rate = light_speed_rate + light_speed_rate_increse
	WHERE name = spaceship_name;
	COMMIT;
END $$
DELIMITER ;

CALL udp_modify_spaceship_light_speed_rate ('USS Templar', 5);
SELECT name, light_speed_rate FROM spaceships WHERE name = 'USS Templar';


UPDATE spaceships
	SET light_speed_rate = 1
	WHERE name = 'USS Templar';
					-- SELECT COUNT(c.id) as people_count
-- FROM planets AS p
-- inner join spaceports as s
-- on p.id = s.planet_id
-- inner join journeys as j
-- on s.id = j.destination_spaceport_id
-- inner join travel_cards as tc
-- on j.id = tc.journey_id
-- inner join colonists as c
-- on tc.colonist_id = c.id
-- WHERE p.name = 'Otroyphus'
-- 					GROUP BY p.id;
					
					