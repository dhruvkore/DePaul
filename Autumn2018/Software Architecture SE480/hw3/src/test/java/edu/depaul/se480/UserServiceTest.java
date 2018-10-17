package edu.depaul.se480;

import java.util.ArrayList;
import java.util.List;


import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class UserServiceTest {
	
	@Test
	public void AdultTest(){
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
}
