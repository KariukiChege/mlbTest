package StatsReturn;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;

import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import io.restassured.path.json.JsonPath;

public class Stats {
	private final String uri = "https://statsapi.mlb.com/";
	private String basePath = "api/v1/schedule/";
	
	@BeforeClass
	public void Setup() {
		RestAssured.baseURI = uri;
		RestAssured.basePath = basePath;
	}
	
	@Test
	public void getTotalGames() {
		String totalGames = getItemsList("dates.totalGames").toString();
		System.out.println("Total Games = " + totalGames);
	}
	
	@Test
	public void getVenueIds() {
		String venueIds = getItemsList("dates.games.venue.id[0]").toString();
		System.out.println("Venue ID's = " + venueIds);
	}
	
	@Test
	public void getVenueNames() {
		String venueNames = getItemsList("dates.games.venue.name[0]").toString();
		System.out.println("Venue Names = " + venueNames);
	}		
	
	private Response getResponseBody() {
		Response res =
				 given()
					.param("sportId", "1")
					.param("date", "06/17/2018")
					.param("hydrate", "team,probablePitcher")
				.when()
					.get()
				.then()
					.statusCode(200)
					.extract()
					.response();
		return res;
	}	
	
	private List<String> getItemsList(String path){
		JsonPath jsonPath = getResponseBody().jsonPath();
		List<String> items = jsonPath.getList(path);
		return items;
	}
	
	//This returns key value pairs for venue id and venue name
	private Map<String,String> venueIdandNames () {
		List<String> venueIds = getItemsList("dates.games.venue.id[0]");
		List<String> venueNames = getItemsList("dates.games.venue.name[0]");
	    if (venueIds.size() != venueNames.size())
	        throw new IllegalArgumentException ("Data lists do not match");
	    Map<String,String> map = new LinkedHashMap<String,String>();
	    for (int i=0; i<venueIds.size(); i++) {
	        map.put(venueIds.get(i), venueNames.get(i));
	    }
	    return map;
	}		
}
