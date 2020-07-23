package Restapagestepdefination;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Restapagestepdefination2 {
	
	
		 private static final String USER_ID = "9b5f49ab-eea9-45f4-9d66-bcf56a531b85";
		 private static final String USERNAME = "Test-Test";
		 private static final String PASSWORD = "Test@@123";
		 private static final String BASE_URL = "http://dummy.restapiexample.com/cgi-sys/suspendedpage.cgi";
		 
		 private static String token;
		 private static Response response;
		 private static String jsonString;
		 private static String crudId;
	
	WebDriver driver;
	@Given("^I am an authorized user$")
	public void i_am_an_authorized_user() throws Throwable {
		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given();

		request.header("Content-Type", "application/json");
		response = request.body("{ \"userName\":\"" + USERNAME + "\", \"password\":\"" + PASSWORD + "\"}")
				.post("/Account/v1/GenerateToken");

		String jsonString = response.asString();
		token = JsonPath.from(jsonString).get("token");
		
		/*given().
		get("BASE_URL").
		then().
		statusCode(200).
		body("data.id[1]",equalTo(8)).log().all();*/
	}
	
	@Given("^A list of app data are available$")
	public void a_list_of_app_data_are_available() throws Throwable {
		RestAssured.baseURI = BASE_URL;
		System.out.println(RestAssured.baseURI);
		RequestSpecification request = RestAssured.given();
		response = request.get("/CrudStore/v1/Cruds");

		jsonString = response.asString();
		List<Map<String, String>> crud = JsonPath.from(jsonString).get("crud");
		Assert.assertTrue(crud.size() > 0);

		crudId = crud.get(0).get("isbn");
		
	}
	
	@Then("^A user is verify the all received data$")
	public void a_user_is_verify_the_all_received_data() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}
	@When("^I add a all data in to my  list$")
	public void i_add_a_all_data_in_to_my_list() throws Throwable {
		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given();
		request.header("Authorization", "Bearer " + token)
		.header("Content-Type", "application/json");

		response = request.body("{ \"userId\": \"" + USER_ID + "\", " +
				"\"collectionOfIsbns\": [ { \"isbn\": \"" + crudId + "\" } ]}")
				.post("/CRUDS/v1/cruds");
	}
	@Then("^the list is added$")
	public void the_list_is_added() throws Throwable {
		Assert.assertEquals(201, response.getStatusCode());
	    throw new PendingException();
	}
	@When("^I remove a app data from my  list$")
	public void i_remove_a_app_data_from_my_list() throws Throwable {
		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given();

		request.header("Authorization", "Bearer " + token)
		.header("Content-Type", "application/json");

		response = request.body("{ \"isbn\": \"" + crudId + "\", \"userId\": \"" + USER_ID + "\"}")
				.delete("/BookStore/v1/crud");
	}
	
	@Then("^the list is removed$")
	public void the_list_is_removed() throws Throwable {
		Assert.assertEquals(204, response.getStatusCode());

		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given();

		request.header("Authorization", "Bearer " + token)
		.header("Content-Type", "application/json");

		response = request.get("/Account/v1/User/" + USER_ID);
		Assert.assertEquals(200, response.getStatusCode());

		jsonString = response.asString();
		List<Map<String, String>> booksOfUser = JsonPath.from(jsonString).get("cruds");
		Assert.assertEquals(0, booksOfUser.size());
	}
	
	
	

}
