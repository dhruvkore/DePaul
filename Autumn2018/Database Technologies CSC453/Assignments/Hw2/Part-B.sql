/*
1.	[1pt] Find the name of all restaurants offering Indian cuisine
2.	[2pt] Find restaurant names that received a rating of 4 or 5, sort them in increasing order. 
3.	[2pt] Find the names of all restaurants that have no rating.
4.	[2pt] Some reviewers didn't provide a date with their rating. 
    Find the names of all reviewers who have ratings with a NULL value for the date. 
5.	[4pt] For all cases where the same reviewer rated the same restaurant twice 
    and gave it a higher rating the second time, return the reviewer's name and the name of the restaurant.
6.	[4pt] For each restaurant that has at least one rating, 
    find the highest number of stars that a restaurant received. 
    Return the restaurant name and number of stars. Sort by restaurant name. 
7.	[5pt] For each restaurant, return the name and the 'rating spread', that is, 
    the difference between highest and lowest ratings given to that restaurant. 
    Sort by rating spread from highest to lowest, then by restaurant name. 
8.	[5pt] Find the difference between the average rating of Indian restaurants 
    and the average rating of Chinese restaurants. 
    (Make sure to calculate the average rating for each restaurant, 
    then the average of those averages for Indian and Chinese restaurants. 
    Don't just calculate the overall average rating for Indian and Chinese restaurants.)
*/

/* 1 */
SELECT name FROM Restaurant
WHERE cuisine = 'Indian';

/* 2 */
SELECT r.name FROM Restaurant r
JOIN Rating rt ON r.rid=rt.rid
GROUP BY r.name
HAVING AVG(rt.stars) >= 4;

/* 3 */
SELECT r.name FROM Restaurant r
LEFT OUTER JOIN Rating rt ON r.rid=rt.rid
WHERE rt.rid is null;

/* 4 */
SELECT r.name FROM reviewer r
JOIN rating rt ON r.vid=rt.vid
WHERE rt.ratingdate is null;

/* 5 */
SELECT rv.name AS "Reviewer", r.name AS "Restaurant" FROM rating rt
INNER JOIN rating rtt ON rt.vID=rtt.vID
INNER JOIN reviewer rv ON rt.vID=rv.vID
INNER JOIN restaurant r ON r.rID=rt.rID
WHERE rt.stars > rtt.stars 
AND rt.ratingdate > rtt.ratingdate;


/* 6 */
SELECT r.name, MAX(rt.stars) AS "Max Rating" FROM restaurant r
INNER JOIN rating rt
ON r.rid=rt.rid
GROUP BY r.name
ORDER BY r.name;

/* 7 */
SELECT r.name, (MAX(rt.stars) - MIN(rt.stars)) AS "Spread" FROM restaurant r
INNER JOIN rating rt
ON r.rid=rt.rid
GROUP BY r.name
ORDER BY "Spread" DESC,r.name;


/* 8 */
SELECT AVG(rt."stars") - Max("IndianAvg") AS "Avg(Indian) - Avg(Chinese)" FROM restaurant r
INNER JOIN(
    SELECT rid,AVG(stars) as "stars" FROM rating
    GROUP BY rid
) rt ON r.rid=rt.rid
CROSS JOIN(
    SELECT AVG(rtwo."stars") AS "IndianAvg" FROM restaurant b
    INNER JOIN(
        SELECT rid,AVG(stars) as "stars" FROM rating
        GROUP BY rid
    ) rtwo ON b.rid=rtwo.rid
    WHERE b.cuisine='Indian'
    GROUP BY b.cuisine
) c
WHERE r.cuisine='Chinese';




