/*
1. [1pt] Select the names of the products with a price less than or equal to $200
2. [1pt] Select all the products with a price between $60 and $120
3. [1pt] Select the name and price in cents (i.e., the price is in dollars). 
4. [2pt] Select the product name, price, and manufacturer name of all the products.
5. [3pt] Select all manufactures who currently do not have any listed products.
6. [3pt] Select the name of each manufacturer along with the name and price of its most expensive product.
7. [4pt] Select the names and average prices of manufacturer whose products
                        have an average price larger than or equal to $150.	
*/


/* 1 */
SELECT name FROM products
WHERE price <= 200;

/* 2 */
SELECT * FROM products
WHERE price > 60 AND price < 120;

/* 3 */
SELECT name,price*100 AS "Price in Cents" FROM products;

/* 4 */
SELECT p.name,p.price,m.name as "Manufacturer Name" FROM products p
JOIN manufacturers m ON p.manufacturer=m.code;

/* 5 */
SELECT m.name as "Manufacturer Name", p.name,p.price FROM manufacturers m
LEFT OUTER JOIN products p ON m.code=p.manufacturer
WHERE p.name is null;


/* 6 */
SELECT m.name as "Manufacturer Name", p.name,p.price FROM manufacturers m
JOIN products p ON m.code=p.manufacturer
WHERE p.price = (
    SELECT MAX(price) FROM products
    WHERE manufacturer = p.manufacturer
);

/* 7 */
SELECT m.name as "Manufacturer Name", AVG(p.price) FROM manufacturers m
JOIN products p ON m.code=p.manufacturer
WHERE (
    SELECT AVG(price) FROM products
    WHERE manufacturer = p.manufacturer
) > 150
GROUP BY m.name;

