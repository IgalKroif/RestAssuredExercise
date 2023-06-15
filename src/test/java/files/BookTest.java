package files;

import static io.restassured.RestAssured.given;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

/**
 * 
 * 
 * @author AnkurBakshi
 * This class does complete test of adding and deleting - It is a complete test in itself
 *
 */


public class BookTest {
  @Test(dataProvider="BooksData", enabled=true)
  public void f(String isbn, String aisle) {
	
	  RestAssured.baseURI = "https://rahulshettyacademy.com";
	  
	  Response res=  given().body(BodyPost.getBody(isbn,aisle)).when().post("/Library/Addbook.php").then().extract().response();
	  System.out.println("Response starts " + res.asString() + " ends");

	  //System.out.println("ID returned : " + BodyPost.getDeleteBody(isbn, aisle));
  }

  
  @Test(dataProvider="BooksData", enabled=false)
  public void f2(String isbn, String aisle) {
	
	  RestAssured.baseURI = "https://rahulshettyacademy.com";
	  
	  Response res=  given().body(BodyPost.getDeleteBody(isbn, aisle)).when().post("/Library/DeleteBook.php").then().extract().response();
	  System.out.println("Response starts " + res.asString() + " ends");

  }
  
  
  @DataProvider(name="BooksData")
  public Object[][] getData(){
	  //Multidimensional Array object [][]
	  return new Object[][] {{"chch","1989"}, {"td","2222"}, {"cd","3333"}};
  }
  

}

