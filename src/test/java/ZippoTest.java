import POJO.Location;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {

    @Test
    public void test() {
        given()
                // hazırlık işlemlerini yapcağız

                .when()
                // link ve aksiyon işlemleri

                .then()
        // test ve extract işlemleri
        ;
    }

    @Test
    public void statusCodeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()   //  log.All() -> bütün respons u gösterir
                .statusCode(200)  // status kontrolü
        ;
    }

    @Test
    public void contentTypeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .contentType(ContentType.JSON)
        ;
    }

    @Test
    public void logTest() {
        given()
                .log().all()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
        ;
    }

    @Test
    public void checkStateInResponseBody() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("country", equalTo("United States")) // body.country == United States
                .statusCode(200)
        ;
    }

    @Test
    public void bodyJsonPathTest2() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places[0].state", equalTo("California"))
                .statusCode(200)
        ;
    }

    @Test
    public void bodyJsonPathTestHasItem() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places.state", hasItem("California"))  // bütün state lerde aranan eleman var mı?
                .statusCode(200)
        ;
    }

    @Test
    public void bodyJsonPathTest3() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places[0].'place name'", equalTo("Beverly Hills"))
                .statusCode(200)
        ;
    }

    @Test
    public void bodyJsonPathTest4() {
        given()
                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
                .log().body()
                .body("data[1].'id'", equalTo(29))
                .statusCode(200);
    }

    @Test
    public void bodyArrayHasSizeTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then().
                body("places", hasSize(1))// verilen path deki listin size kontrol
                .log().body().
                statusCode(200)
        ;
    }

    @Test
    public void combiningTest() {
        given().


                when().
                get("http://api.zippopotam.us/us/90210").

                then().
                body("places", hasSize(1))
                .body("places.state", hasItem("California"))
                .body("places[0].'place name'", equalTo("Beverly Hills")).
                statusCode(200)
        ;
    }

    /*
    API ye parametre gönderme

1.Yöntem : parametreler / ayıracı    ile metoda gönderiliyor

http://api.zippopotam.us/us/90210    linki inceleyelim


http://api.zippopotam.us->  API nin adresi
               /us/90210->  us ülke değişkenin değeri
                            90210 zipkodu


2.Yöntem

https://gorest.co.in/public/v1/users  API adresi
https://gorest.co.in/public/v1/users?page=1&count=3
                                     page değişken adı  = sonrası değeri
                                     count değişken adı = sonrası değeri

? işareti sonrası gönderilecek değerler paramatere adı=değeri
                  yine ek parametre için & işareti ve yine adı ve değeri şeklinde gönderilir
     */
    @Test
    public void pathParamTest() {
        String country = "us";
        String zipKod = "zipKod";
        given().pathParam("country", country).
                pathParam("zipKod", zipKod).
                log().uri().


                when().
                get("http://api.zippopotam.us/{country}/{zipKod}").


                then().log().body()
                .body("places", hasSize(1))
        ;
    }

    @Test
    public void pathParamTest2() {

        String country = "us";

        for (Integer i = 90210; i < 90220; i++) {
            String zipKod = i.toString();

            given().pathParam("country", country).
                    pathParam("zipKod", zipKod).
                    log().uri().//request linki


                    when().
                    get("http://api.zippopotam.us/{country}/{zipKod}").


                    then().log().body()
                    .body("places", hasSize(1))
            ;
        }
    }

    //https://gorest.co.in/public/v1/users?page=1
    @Test
    public void queryParamTest() {
        given().
                param("page", 1).
                log().uri().


                when().
                get("https://gorest.co.in/public/v1/users").

                then().body("meta.pagination.page", equalTo(1));
    }


    @Test
    public void queryParamTest2() {
        for (int i = 1; i <= 10; i++) {
            given().
                    param("page", i).
                    log().uri().


                    when().
                    get("https://gorest.co.in/public/v1/users").

                    then().body("meta.pagination.page", equalTo(i));
        }
    }

    private ResponseSpecification responseSpecification;
    private RequestSpecification requestSpecification;

    @BeforeClass
    public void setup() {
        baseURI = "http://api.zippopotam.us";

        requestSpecification = new RequestSpecBuilder().log(LogDetail.URI)
                .setAccept(ContentType.JSON).build();


        responseSpecification = new ResponseSpecBuilder().
                expectStatusCode(200).
                expectContentType(ContentType.JSON).
                log(LogDetail.ALL).
                build();
    }

    @Test
    public void bodyArrayHasSizeTest_RequestSpecification() {
        given().spec(requestSpecification)

                .when()
                .get("/us/90210")

                .then().
                body("places", hasSize(1))// verilen path deki listin size kontrol
                .spec(responseSpecification)
        ;
    }

    @Test
    public void bodyArrayHasSizeTest_ResponseSpecification() {
        given().log().uri()

                .when()
                .get("/us/90210")

                .then().
                body("places", hasSize(1))// verilen path deki listin size kontrol
                .spec(responseSpecification)
        ;
    }

    @Test
    public void bodyArrayHasSizeTest_baseUrlTest() {
        given()

                .when()
                .get("/us/90210")

                .then().
                body("places", hasSize(1))// verilen path deki listin size kontrol
                .statusCode(200)
        ;
    }

    //Json extract
    @Test
    public void extractingJsonPath() {


        String place_name = given()
                //spec(requestSpecification)
                .when()
                .get("/us/90210")
                .then()
                .spec(responseSpecification)
                .extract().path("places[0].'place name'"); // estract metodu ile given ile baslayan satir bir deger dondurur hala geldi


        System.out.println("place_name = " + place_name);
    }
            /*
            {
            "city code": 90, -> int
            "post code": "90210", ->string
            "country": "United States", ->string
            "country abbreviation": "US",
            "places": [
                {
                    "place name": "Beverly Hills",
                    "longitude": "-118.4065",
                    "state": "California",
                    "state abbreviation": "CA",
                    "latitude": "34.0901"
                }
            ]
        }

        place[0].state bu bir tane değer verir
        place.state ->  List<String>
             */

    @Test
    public void extractingJsonPathInt() {
        int limit =
                given().
                        param("page", 1).
                      //  log().uri().


                        when().
                        get("https://gorest.co.in/public/v1/users").


                        then().
                        //log().body().
                        extract().path("meta.pagination.limit");
        System.out.println("limit = " + limit);
    }

    @Test
    public void extractingJsonPathIntList() {


        List<Integer> idler =
                given().
                        param("page", 1).
                      //  log().uri().


                        when().
                        get("https://gorest.co.in/public/v1/users").


                        then().
                        //log().body().
                        extract().path("data.id");
        System.out.println("limit = " + idler);
    }
    @Test
    public void extractingJsonPathIntString() {


        List<String> koyler =
                given().
                        //spec(requestSpecification).



                        when().
                        get("/tr/01000").


                        then().
                        //spec(responseSpecification).
                        extract().path("places.'place name'");
        System.out.println("koyler = " + koyler);
        Assert.assertTrue(koyler.contains("Büyükdikili Köyü"));
    }

    @Test
    public void extractingJsonPOJO(){
        Location location=
        given()


                .when()
                .get("/us/90210")



                .then()
        .extract().as(Location.class)

        ;
        System.out.println("location = " + location);
        System.out.println("location.getCountryabbreviation() = " + location.getCountryabbreviation());
        System.out.println("location.getCountry() = " + location.getCountry());
        System.out.println("location.getPlaces() = " + location.getPlaces());
        System.out.println("location.getPlaces().get(0).getPlacename() = " + location.getPlaces().get(0).getPlacename());

    }
}






