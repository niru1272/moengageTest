package com.moengage.tests;

import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.Test;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;

public class MoEngageTest extends BaseClass {
	
	List<Integer> seriesIds= new ArrayList<Integer>();
	List<Integer> consolidatedSeriesIds = new ArrayList<Integer>();

	@Test(groups = "MARVEL")
	public void getCharactersTest() {	
		
		String charsUrl=baseUrl+"characters"+queryParams;
		
		charsResponse= getResponse(charsUrl);
		
		List<Integer> charIds = JsonPath.read(charsResponse.asString(), "$.data.results[?(@.description empty false)].id");
		List<Integer> charNamesNoDescription = JsonPath.read(charsResponse.asString(), "$.data.results[?(@.description empty false)].id");
		System.out.println("List of character contains description");
		System.out.println("\n"+charNamesNoDescription);
		
		String seriesUrl = baseUrl+"characters/seriesId/series"+queryParams;
		
		System.out.println("List of Series those characters which have description\n");
		for(int i=0;i<charIds.size();i++) {
			seriesResponse= getResponse(seriesUrl.replaceAll("seriesId", charIds.get(i).toString()));
			seriesIds = JsonPath.read(seriesResponse.asString(), "$.data.results[*].id");
			consolidatedSeriesIds.addAll(seriesIds);
		}
		System.out.println(consolidatedSeriesIds);
	}
	
	
	@Test(groups = "MARVEL")
	public void getSeriesTest() {
		String charsWithSeriesIdUrl= baseUrl+"series/seriesId/characters"+queryParams;
		consolidatedCharIdsWithSeries.addAll(getCharacters(charsWithSeriesIdUrl,consolidatedSeriesIds.get(0).toString()));
		consolidatedCharIdsWithSeries.addAll(getCharacters(charsWithSeriesIdUrl,consolidatedSeriesIds.get(1).toString()));
		System.out.println("list of characters with Index\n");
		for(int i=0;i<consolidatedCharIdsWithSeries.size();i++)
			System.out.println((i+1)+" "+consolidatedCharIdsWithSeries.get(i));
	}
	
	@Test(groups = "MARVEL")
	public void getStoriesTest() {
		String storiesUrl=baseUrl+"characters/characterId/stories"+queryParams;
		List<String> storiesTitle= new ArrayList<String>();
		List<String> consolidatedStoriesTitle= new ArrayList<String>();
		List<Integer> charIdsWithoutDesc = JsonPath.read(charsResponse.asString(), "$.data.results[?(@.description empty true)].id");
		for(int i=0;i<charIdsWithoutDesc.size();i++) {
			storiesWithCharId=getResponse(storiesUrl.replaceAll("characterId",charIdsWithoutDesc.get(i).toString()));
			storiesTitle = JsonPath.read(storiesWithCharId.asString(), "$.data.results[*].title");
			consolidatedStoriesTitle.addAll(storiesTitle);
		}
		System.out.println("\n stories contains characters without description\n "+ consolidatedStoriesTitle);
		
	}
	
	public static List<String> getCharacters(String url, String seriesId) {
		Response charListWithSeriesResponse = getResponse(url.replaceAll("seriesId", seriesId));
		List<String> charNamesWithSeries =  JsonPath.read(charListWithSeriesResponse.asString(), "$.data.results[*].title");
		return charNamesWithSeries;
	}
}