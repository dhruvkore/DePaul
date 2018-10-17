package edu.depaul.se480;

import java.util.ArrayList;
import java.util.List;

public class MovieRecommendationService {
	private IUserService userService;
	
	public MovieRecommendationService(IUserService us) {
		userService = us;
	}
	
	public List<String> getRecommendedMovies() {
		List<String> movies = new ArrayList<String>();
		int age = userService.getAge();
		if(age < 13) {
			movies.add("Shrek");
			movies.add("Coco");
			movies.add("The Incredibles");
		}
		else if(age >= 13 && age < 17) {
			movies.add("The Avengers");
			movies.add("The Dark Knight");
			movies.add("Inception");
		}
		else if(age >= 17) {
			movies.add("The Godfather");
			movies.add("Deadpool");
			movies.add("Saving Private Ryan");
		}
		else {
			movies.add("Shrek");
			movies.add("Coco");
			movies.add("The Incredibles");
		}
		
		return movies;
	}
}
