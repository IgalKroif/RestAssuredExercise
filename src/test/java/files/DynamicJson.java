package files;

import static io.restassured.RestAssured.given;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class DynamicJson {
	@Test(dataProvider = "BooksDataProvider")
	public void addBook(String isbn, String aisle) {
	    RestAssured.baseURI = "https://rahulshettyacademy.com/";
	    
	    // Create the payload using the addBook() method
	    String payload = Payload.addBook(isbn, aisle);
	    
	    // Make the POST request with the payload
	    String response = given()
	        .header("Content-Type", "application/json")
	        .body(payload)
	    .when()
	        .post("Library/Addbook.php")
	    .then()
	        .assertThat()
	        .statusCode(200)
	        .extract()
	        .response()
	        .asString();

	    // Parse the response JSON
	    JsonPath js = new JsonPath(response);
	    String getsId = js.get("ID");
	    System.out.println(getsId);
	    
	    given()
	    .header("Content-Type", "application/json")
	    .body(payload)
	    .when()
	    .delete("Library/DeleteBook.php")
	    .then().assertThat().statusCode(200)
	    .extract().response().asString();
	    JsonPath js2 = new JsonPath(response);
	    String getsId2 = js2.get("ID");
	    System.out.println(getsId2);
	}
	
	@DataProvider(name = "BooksDataProvider")
	public Object[][] getData() {
		return new Object[][] {{"OmFWjTy3","9363"}, {"adghjk3", "9362"}, {"WfGhA31","1354"}};
		
	}

}
