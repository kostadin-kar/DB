#4
select tc.card_number, tc.job_during_journey
from travel_cards as tc
order by tc.card_number;

#5
select c.id, concat(c.first_name, ' ', c.last_name) as full_name, c.ucn
from colonists as c
order by c.first_name, c.last_name, c.id;

#6
select j.id, j.journey_start, j.journey_end
from journeys as j
where j.purpose = 'Military'
order by j.journey_start;

#7
select c.id, concat(c.first_name, ' ', c.last_name) as full_name
from colonists as c
inner join travel_cards as tc
	on c.id = tc.colonist_id
where tc.job_during_journey = 'Pilot'
order by c.id;

#8
select count(*) as count
from colonists as c
inner join travel_cards as tc
	on c.id = tc.colonist_id
inner join journeys as j
	on tc.journey_id = j.id
where j.purpose = 'Technical';

#9
select s.name as spaceship_name, sp.name as spaceport_name
from spaceships as s
inner join journeys as j
	on s.id = j.spaceship_id
inner join spaceports as sp
	on j.destination_spaceport_id = sp.id
order by s.light_speed_rate desc
limit 1;

#10
select distinct s.name, s.manufacturer
from spaceships as s
inner join journeys as j
	on s.id = j.spaceship_id
inner join travel_cards as tc
	on j.id = tc.journey_id
inner join colonists as c
	on tc.colonist_id = c.id
where (2019 - year(c.birth_date )) < 30 AND tc.job_during_journey = 'Pilot'
order by s.name;

#11
select p.name as planet_name, s.name as spaceport_name
from planets as p
inner join spaceports as s
	on p.id = s.planet_id
inner join journeys as j
	on s.id = j.destination_spaceport_id
where j.purpose = 'Educational'
order by s.name desc;

#12
select p.name as planet_name, count(*) as journeys_count
from planets as p
inner join spaceports as s
	on p.id = s.planet_id
inner join journeys as j
	on s.id = j.destination_spaceport_id
group by p.name
order by journeys_count desc, p.name;

#13
select j.id, p.name as planet_name, s.name as spaceport_name, 'Military' as journey_purpose
from journeys as j
inner join spaceports as s 
	on j.destination_spaceport_id = s.id
inner join planets as p
	on s.planet_id = p.id
where j.purpose = 'Military'
order by timediff(j.journey_start, j.journey_end)
limit 1;

#14
select j.id as journey_id, tc.job_during_journey as job_name, count(tc.id) as job_counts, c.first_name, tc.colonist_id as colonistz_id
from journeys as j
inner join travel_cards as tc
	on j.id = tc.journey_id
inner join colonists as c
	on tc.colonist_id = c.id
group by tc.job_during_journey
order by timediff(j.journey_start, j.journey_end) desc;

select j.id, timestampdiff(second, j.journey_start, j.journey_end) as duration, c.id as colonists_id, c.first_name, tc.job_during_journey
from journeys as j
inner join travel_cards as tc
	on j.id = tc.journey_id
inner join colonists as c
	on tc.colonist_id = c.id
group by j.id, c.id
order by timestampdiff(second, j.journey_start, j.journey_end) desc;
#---------------
select as job_name 
from (
	select j.id, timestampdiff(second, j.journey_start, j.journey_end) as duration, c.id as colonists_id, c.first_name, tc.job_during_journey
	from journeys as j
	inner join travel_cards as tc
		on j.id = tc.journey_id
	inner join colonists as c
		on tc.colonist_id = c.id
	group by j.id, c.id
	having max(timestampdiff(second, j.journey_start, j.journey_end))
	order by timestampdiff(second, j.journey_start, j.journey_end) desc, tc.job_during_journey desc;
		) as jobs

#---------------
select j.id, timestampdiff(second, j.journey_start, j.journey_end) as duration
from journeys as j
order by timestampdiff(second, j.journey_start, j.journey_end) desc;

##first
select j.id, timestampdiff(second, j.journey_start, j.journey_end) as duration, count(c.id) as count_of_colonists
	from journeys as j
	inner join travel_cards as tc
		on j.id = tc.journey_id
	inner join colonists as c
		on tc.colonist_id = c.id
	group by j.id
	order by max(timestampdiff(second, j.journey_start, j.journey_end)) desc
	limit 1;
##second
select j.id, timestampdiff(second, j.journey_start, j.journey_end) as duration, c.id as colonists_id, c.first_name, tc.job_during_journey
from journeys as j
inner join travel_cards as tc
	on j.id = tc.journey_id
inner join colonists as c
	on tc.colonist_id = c.id
group by j.id, c.id
order by timestampdiff(second, j.journey_start, j.journey_end) desc;


SELECT second_select.job_during_journey as job_name 
from (
	select j.id, count(c.id) as count_of_colonists
	from journeys as j
	inner join travel_cards as tc
		on j.id = tc.journey_id
	inner join colonists as c
		on tc.colonist_id = c.id
	group by j.id
	order by max(timestampdiff(second, j.journey_start, j.journey_end)) desc
	limit 1
) as first_select
inner join (
			select j.id,  tc.job_during_journey
			from journeys as j
			inner join travel_cards as tc
				on j.id = tc.journey_id
			inner join colonists as c
				on tc.colonist_id = c.id
			group by j.id, c.id
			order by timestampdiff(second, j.journey_start, j.journey_end) desc
			) as second_select
on first_select.id = second_select.id
group by second_select.job_during_journey
order by count(second_select.job_during_journey)
limit 1;