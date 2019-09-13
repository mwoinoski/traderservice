package com.acme.acmetrade.endpoints;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

//import static org.assertj.core.api.Assertions.*;

import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class MockQuotesGeneratorEndpointIntegrationTest {
    @LocalServerPort
    private int serverPort;
    
    @Before
    public void init(){
        RestAssured.port = serverPort;
    }

    @Test
    public void getVersion() throws Exception {
         given()
     		.accept("application/json")
        .when()
            .get("/version")
        .then()
            .statusCode(200)
            .body("version", equalTo("1"));
    }
    
    @Test
    public void generateMockQuotes() throws Exception {
        given()
        	.contentType("application/json")
        	.accept("application/json")
        	.body(new MockQuotesGeneratorEndpoint.GenerateMockQuotesRequest(
        				"AAPL", "2019-01-01", 100, "LinearIncrease"))
        .when()
        	.post("/mock-quotes")
	    .then()
	        .statusCode(200)
	        .body("filename", equalTo("AAPL_20190101_100_LinearIncrease.csv"));
    }

}
