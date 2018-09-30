/*
1.	List car rental companies which have a mileage of at least 27 miles/gallon. 
2.	List trip IDs taken on train costing strictly more than $150.  
3.	Find trip IDs and their fare that are not taken in the US i.e., `Non-US` trips. 
4.	Find the business class plane trip IDs that are greater than $1000.  
5.	Find any car trip more expensive than a trip taken on a train. 
6.	List a pair of distinct trips that have exactly the same value of mileage.  
7.	List a pair of distinct train trips that do not have the same speed.  
8.	Find those pair of trips in the same state with the same mode of travel. List such pairs only once.  
9.	Find a state in which trips have been taken by all three modes of transportation:  train, plane, and car.  
10.	 Find the details of Find the details of 
    a) the most costly trip, 
    b) the cheapest trip taken by either the air, rail, or or car. Write two separate queries. 
*/


/* 1 */
SELECT rentalcompany FROM BYCAR
WHERE mileage >= 27
GROUP BY rentalcompany;

/* 2 */
SELECT tid FROM TRIPS
WHERE travelmode = 'Train'
AND fare > 150;

/* 3 */
SELECT tid, fare FROM TRIPS
WHERE tripstate = 'Non-US';

/* 4 */
SELECT t.tid FROM BYPLANE b
JOIN TRIPS t ON t.tid = b.tid
WHERE b.class = 'Business'
AND t.fare > 1000;

/* 5 */
SELECT * FROM TRIPS a
INNER JOIN (
    SELECT * FROM TRIPS 
    WHERE travelmode = 'Train'
) b
ON a.tripstate=b.tripstate
WHERE a.travelmode = 'Car'
AND a.fare > b.fare;

/* 6 */
SELECT C.tid, b.tid FROM BYCAR C
INNER JOIN BYCAR B
ON C.mileage=B.mileage AND C.tid < B.tid;

/* 7 */
SELECT C.tid, b.tid FROM BYTRAIN C 
INNER JOIN BYTRAIN B
ON C.trainspeed <> B.trainspeed AND C.tid < B.tid;

/* 8 */
SELECT C.tid, B.tid FROM TRIPS C 
INNER JOIN TRIPS B
ON C.tripstate = B.tripstate AND C.travelmode = B.travelmode AND C.tid < B.tid;

/* 9 */
SELECT DISTINCT a.tripstate FROM TRIPS a
INNER JOIN (
    SELECT tripstate FROM TRIPS
    WHERE travelmode = 'Train'
) b
ON a.tripstate=b.tripstate
INNER JOIN (
    SELECT tripstate FROM TRIPS
    WHERE travelmode = 'Plane'
) c
ON a.tripstate=c.tripstate
WHERE a.travelmode = 'Car';

/* 10 */
/* a */
SELECT * FROM TRIPS a
FULL JOIN BYCAR b
ON a.tid=b.tid
FULL JOIN BYTRAIN c
ON a.tid=c.tid
FULL JOIN BYPLANE d
ON a.tid=d.tid
WHERE a.fare = (SELECT MAX(fare) FROM TRIPS);

/* b */
SELECT a.tripstate, a.travelmode, a.fare, b.rentalcompany, b.mileage, c.type, c.coach, c.trainspeed, c.numberofstops, d.airline, d.class, d.layovertime FROM TRIPS a
FULL JOIN BYCAR b
ON a.tid=b.tid
FULL JOIN BYTRAIN c
ON a.tid=c.tid
FULL JOIN BYPLANE d
ON a.tid=d.tid
WHERE a.fare = (SELECT MIN(fare) FROM TRIPS);

