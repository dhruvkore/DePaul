package edu.depaul.se480;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

import java.util.ArrayList;
import java.util.List;

public class MovieRecommendationService extends HystrixCommand<List<String>> {
	private IUserService userService;
	
	public MovieRecommendationService(IUserService us) {
		super(Setter.withGroupKey(
				HystrixCommandGroupKey
						.Factory
						.asKey("MovieRecommendation")
				)
						.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
								.withExecutionIsolationThreadTimeoutInMilliseconds(100)) //100ms timeout
				);
		userService = us;
	}
	
	public List<String> getRecommendedMovies() {
		return this.execute();
	}

	@Override
	protected List<String> run() throws Exception {
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

	@Override
	protected List<String> getFallback(){
		List<String> movies = new ArrayList<String>();
		movies.add("Shrek");
		movies.add("Coco");
		movies.add("The Incredibles");
		return movies;
	}
}
