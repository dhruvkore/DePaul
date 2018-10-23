<Html>

<h1>Coded using IntelliJ</h1>

<body>
MovieRecommenderService calls userService.

UserService returns age when getage is invoked.



The Netflix Hystrix library is used to handle when 

	- the UserService call throws an exception
	
	- and the UserService exceeds 100 ms to return.
	

Test cases are given for each of the above senarios.


To run build and run test cases ...

mvn package

</body>

</Html>
