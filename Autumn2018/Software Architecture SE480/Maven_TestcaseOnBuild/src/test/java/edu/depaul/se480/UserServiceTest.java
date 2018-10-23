package edu.depaul.se480;

import java.util.ArrayList;
import java.util.List;


import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class UserServiceTest {

	@Test
	public void AdultTest(){
		System.out.println("Running Adult Test...");

		UserService userService = Mockito.mock(UserService.class) ;
		Mockito.when(userService.getAge()).thenReturn(30);
		MovieRecommendationService movieRecommendationService = new MovieRecommendationService(userService);
		
		List<String> expectedMovies = new ArrayList<String>();
		expectedMovies.add("The Godfather");
		expectedMovies.add("Deadpool");
		expectedMovies.add("Saving Private Ryan");
		
		List<String> actual = movieRecommendationService.getRecommendedMovies();

		Assert.assertEquals(actual, expectedMovies);
	}

	@Test
	public void KidsTest(){
		System.out.println("Running Kids Test...");

		UserService userService = Mockito.mock(UserService.class) ;
		Mockito.when(userService.getAge()).thenReturn(2);
		MovieRecommendationService movieRecommendationService = new MovieRecommendationService(userService);
		
		List<String> expectedMovies = new ArrayList<String>();
		expectedMovies.add("Shrek");
		expectedMovies.add("Coco");
		expectedMovies.add("The Incredibles");
		
		List<String> actual = movieRecommendationService.getRecommendedMovies();
		
		Assert.assertEquals(actual, expectedMovies);
	}
	
	@Test
	public void TeensTest(){
		System.out.println("Running Teens Test...");

		UserService userService = Mockito.mock(UserService.class) ;
		Mockito.when(userService.getAge()).thenReturn(15);
		MovieRecommendationService movieRecommendationService = new MovieRecommendationService(userService);
		
		List<String> expectedMovies = new ArrayList<String>();
		expectedMovies.add("The Avengers");
		expectedMovies.add("The Dark Knight");
		expectedMovies.add("Inception");
		
		List<String> actual = movieRecommendationService.getRecommendedMovies();
		
		Assert.assertEquals(actual, expectedMovies);
	}

	@Test
	public void NullPointerExceptionTest(){
		System.out.println("Running NullPointerException Test...");

		UserService userService = Mockito.mock(UserService.class) ;
		Mockito.when(userService.getAge()).thenThrow(NullPointerException.class);
		MovieRecommendationService movieRecommendationService = new MovieRecommendationService(userService);

		List<String> expectedMovies = new ArrayList<String>();
		expectedMovies.add("Shrek");
		expectedMovies.add("Coco");
		expectedMovies.add("The Incredibles");

		List<String> actual = movieRecommendationService.getRecommendedMovies();

		Assert.assertEquals(actual, expectedMovies);
	}

	@Test
	public void UserServiceTimeoutTest() throws InterruptedException {
		System.out.println("Running UserServiceTimeout Test...");

		UserService userService = Mockito.mock(UserService.class) ;
		Mockito.when(userService.getAge()).thenAnswer(
				new Answer<List<String>>() {
					public List<String> answer(InvocationOnMock invocation) throws Throwable {
						Thread.sleep(105); // Sleep 105
						return new ArrayList<String>();
					}
				}
		);
		MovieRecommendationService movieRecommendationService = new MovieRecommendationService(userService);

		List<String> expectedMovies = new ArrayList<String>();
		expectedMovies.add("Shrek");
		expectedMovies.add("Coco");
		expectedMovies.add("The Incredibles");

		List<String> actual = movieRecommendationService.getRecommendedMovies();

		Assert.assertEquals(actual, expectedMovies);
	}

	@Test
	public void UserServiceLongTimeoutTest() throws InterruptedException {
		System.out.println("Running Long UserServiceTimeout Test...");

		UserService userService = Mockito.mock(UserService.class) ;
		Mockito.when(userService.getAge()).thenAnswer(
				new Answer<List<String>>() {
					public List<String> answer(InvocationOnMock invocation) throws Throwable {
						Thread.sleep(5000); // Sleep 105
						return new ArrayList<String>();
					}
				}
		);
		MovieRecommendationService movieRecommendationService = new MovieRecommendationService(userService);

		List<String> expectedMovies = new ArrayList<String>();
		expectedMovies.add("Shrek");
		expectedMovies.add("Coco");
		expectedMovies.add("The Incredibles");

		List<String> actual = movieRecommendationService.getRecommendedMovies();

		Assert.assertEquals(actual, expectedMovies);
	}
}
