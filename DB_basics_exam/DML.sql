#1
INSERT INTO travel_cards(card_number, job_during_journey, colonist_id, journey_id)
SELECT 
	IF(c.birth_date > '1980-01-01', 
		CONCAT(YEAR(c.birth_date), DAY(c.birth_date), LEFT(c.ucn, 4)), 
		CONCAT(YEAR(c.birth_date), MONTH(c.birth_date), RIGHT(c.ucn, 4))),
	IF(c.id % 2 = 0, 'Pilot', IF(c.id % 3 = 0, 'Cook', 'Engineer')),
	c.id,
	SUBSTRING(c.ucn, 1, 1)
FROM colonists AS c
WHERE c.id BETWEEN 96 AND 100;

#2
UPDATE journeys 
SET purpose =
	CASE
		WHEN id % 2 = 0
			THEN 'Medical'
		WHEN id % 3 = 0
			THEN 'Technical' 
		WHEN id % 5 = 0
			THEN 'Educational'
		WHEN id % 7 = 0
			THEN 'Military' 
		ELSE purpose
	END;
	

#3
DELETE FROM colonists
WHERE id IN (
	SELECT * FROM (
		SELECT c.id
		FROM colonists AS c
		LEFT JOIN travel_cards AS tc
		ON c.id = tc.colonist_id
		WHERE tc.journey_id IS NULL
	) AS p
);

