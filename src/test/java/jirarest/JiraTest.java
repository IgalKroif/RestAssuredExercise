package jirarest;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.given;

import java.io.File;

public class JiraTest {
	@Test
	public static void addComment() {
		RestAssured.baseURI = "http://localhost:8080";
		SessionFilter session = new SessionFilter();
		// Authenticate
		String response =
		given()
		.header("Content-Type", "application/json")
		.filter(session)
		.body("{ \"username\": \"igaldatester\", \"password\": \"Igalkroif1\"}").log().all()
		.when()
		.post("/rest/auth/1/session")
		.then().extract().response().asString();
		
		//Add Comment
		String expectedMessage = "Hi How are you?";
		String addCommentResponse = 
		given()
		.pathParam("id", "10012").log().all()
		.filter(session)
		.header("content-type", "application/json")
		.body("{\r\n"
				+ "    \"body\": \""+expectedMessage+"\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}")
		.when()
		.post("/rest/api/2/issue/{id}/comment")
		.then().log().status()
		.and().log().body()
		.assertThat().statusCode(201)
		.extract().response().asString();
		JsonPath js = new JsonPath(addCommentResponse);
		String commentId =  js.getString("id");
		
		//Add Attachments
		given()
		.header("X-Atlassian-Token", "nocheck")
		.header("Content-Type", "multipart/form-data")
		.pathParam("key", "10012")
		.filter(session)
		.multiPart("file", new File("jira.txt"))
		.when()
		.post("/rest/api/2/issue/{key}/attachments")
		.then().log().all()
		.assertThat().statusCode(200);
		
		//GET ISSUE
		String issueDetails = 
		given()
		.filter(session)
		.queryParam("fields", "comment")
		.pathParam("key", "10012")
		.log().all()
		.when()
		.get("/rest/api/2/issue/{key}")
		
		.then().log().all()
		.extract().response().asString();
		System.out.println(issueDetails);
		
		 JsonPath js1 = new JsonPath(issueDetails);
		int commentCount = js1.getInt("fields.comment.comments.size()");
		for (int i = 0; i < commentCount; i++) {
			String commentIdIssue = js1.get("fields.comment.comments["+i+"].id").toString();
			System.out.println(commentIdIssue);
			
			if(commentIdIssue.equalsIgnoreCase(commentId)) {
				String message = js1.get("fields.comment.comments["+i+"].body").toString();
				
				Assert.assertEquals(message,expectedMessage);
			}
		}
		
	}


	}
