package com.qa.moviecatalogservice.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.qa.moviecatalogservice.models.CatalogItem;
import com.qa.moviecatalogservice.models.Movie;
import com.qa.moviecatalogservice.models.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
	
	@Autowired //someone has a bean, give it to me
	private RestTemplate restTemplate;
	
	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

		UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingdata/users/" + userId, UserRating.class);
		// get all rated movie IDs
//		List<Rating> ratings = Arrays.asList(
//			new Rating("1234", 4),
//			new Rating("5678", 3)
//		);
//		
		return ratings.getUserRating().stream().map(rating -> {
			Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);
			return new CatalogItem(movie.getName(),"Test", rating.getRating());
		})
		.collect(Collectors.toList());
		
		// for each movie ID, call movie info service and get details
		
		// put them all together
	}
}
