package Basqar;

import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class Basqar {


    /*
    url : https://demo.mersys.io/auth/login
giden body :
{
    "username": "richfield.edu",
    "password": "Richfield2020!",
    "rememberMe": true
}

POST : token oluşturulduğu için
     */
    Cookies cookies;

    @BeforeClass
    public void LoginBasqar() {

        baseURI = "https://demo.mersys.io";
        Map<String, String> credential = new HashMap<>(); // login bilgileri
        credential.put("username", "richfield.edu");
        credential.put("password", "Richfield2020!");
        credential.put("rememberMe", "true");
        cookies = given()
                .body(credential)
                .contentType(ContentType.JSON)
                .when()

                .post("/auth/login")


                .then()
                //.log().body()
                .statusCode(200)
                .extract().response().getDetailedCookies()
        ;

        System.out.println("cookies = " + cookies);
    }

    String randomGenName = RandomStringUtils.randomAlphabetic(6);
    String randomgenCode = RandomStringUtils.randomAlphabetic(3);
    String countryId;

    @Test(priority = 1)
    public void CreateCountry() {

        Country country = new Country();

        country.setName(randomGenName);
        country.setCode(randomgenCode);

        countryId = given()
                .cookies(cookies) // gelen cookies (token bilgileri vs) i geri gonderdi.
                .contentType(ContentType.JSON)
                .body(country)
                .log().uri()

                .when()
                .post("/school-service/api/countries")


                .then()
                .log().body()
                .body("name", equalTo(randomGenName))
                .statusCode(201)
                .extract().jsonPath().getString("id");
    }

    @Test(enabled = false)
    public void CreateCountryNegative() {

        Country country = new Country();

        country.setName(RandomStringUtils.randomAlphabetic(6));


        given()
                .cookies(cookies) // gelen cookies (token bilgileri vs) i geri gonderdi.
                .contentType(ContentType.JSON)
                .body(country)
                .log().uri()

                .when()
                .post("/school-service/api/countries")


                .then()
                .log().body()
                .body("message", equalTo("The Country with Name \"" + randomGenName + "\" already exists."))
                .statusCode(400);
    }

    @Test(dependsOnMethods = "CreateCountry", priority = 2)
    public void updateCountry() {

        String cntry=RandomStringUtils.randomAlphabetic(6);
        String code=RandomStringUtils.randomAlphabetic(4);
        Country country = new Country();

        country.setId(countryId);
        country.setName(cntry);
        country.setCode(code);


        given()

                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)
                .log().uri()

                .when()
                .put("/school-service/api/countries")


                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(countryId))
                .body("name", equalTo(cntry))
                .body("code", equalTo(code));
    }

    @Test(priority = 3)
    public void DeleteCountry() {


        given()
                .pathParam("countryId", countryId)
                .cookies(cookies)


                .log().uri()

                .when()
                .delete("/school-service/api/countries/{countryId}")


                .then()
                .log().body()
                .statusCode(200);

    }

    @Test(priority = 4)
    public void DeleteCountryNegative() {

        given()
                .pathParam("countryId", countryId)
                .cookies(cookies)


                .log().uri()

                .when()
                .delete("/school-service/api/countries/{countryId}")


                .then()
                .log().body()
                .statusCode(404);

    }


}
