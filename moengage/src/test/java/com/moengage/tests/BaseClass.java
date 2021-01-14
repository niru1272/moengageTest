package com.moengage.tests;

import static io.restassured.RestAssured.given;
import java.util.ArrayList;
import java.util.List;
import io.restassured.response.Response;

public class BaseClass {
	
	 String baseUrl = "https://gateway.marvel.com/v1/public/";
	 String queryParams = "?ts=1&apikey=48c7bde513fefbc0e123a4c021fd349f&hash=62501d99718a034bf24ea390a41c580f";
	 Response charsResponse,seriesResponse,storiesWithCharId;
	 List<String> consolidatedCharIdsWithSeries  = new ArrayList<String>();
	
	
	public static Response getResponse(String url) {
		Response responseObject = given().when().get(url);
		return responseObject;
	}
	
	
}
