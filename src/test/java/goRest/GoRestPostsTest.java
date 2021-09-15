package goRest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

import goRest.Model.*;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Locale;

public class GoRestPostsTest {
    private ResponseSpecification responseSpecification;
    private RequestSpecification requestSpecification;

    @BeforeTest
    public void setup() {
        baseURI = "https://gorest.co.in/public/v1";

        requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setAccept(ContentType.JSON)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();
    }

    @Test(enabled = false)
    public void GetAllPosts() {
        // Task 1 : https://gorest.co.in/public/v1/posts  API sinden dönen data bilgisini bir class yardımıyla
        // List ini alınız.
        List<Posts> posts =
                given()

                        .when()
                        .get("/posts")

                        .then()
                        .extract().jsonPath().getList("data", Posts.class);

        for (Posts p : posts) {
            System.out.println("p = " + p);
        }
    }

    @Test(enabled = false)
    public void GetPostsOneperson() {
        // Task 2 : https://gorest.co.in/public/v1/posts  API sinden sadece 1 kişiye ait postları listeletiniz.
        //  https://gorest.co.in/public/v1/users/87/posts


        List<Posts> posts =
                given()

                        .when()
                        .get("/users/87/posts")

                        .then()
                        .extract().jsonPath().getList("data", Posts.class);

        for (Posts p : posts) {
            System.out.println("p = " + p);
        }
    }

    @Test(enabled = false)
    public void getAllPostsAsObject() {
        // Task 3 : https://gorest.co.in/public/v1/posts
        // API sinden dönen bütün bilgileri tek bir nesneye atınız


        PostBody posts =
                given()

                        .when()
                        .get("/posts")

                        .then()
                        .extract().as(PostBody.class);
    }

    int postId = 0;

    @Test(priority = 1)
    public void PostCreate() {
        // Task 4 : https://gorest.co.in/public/v1/posts  API sine 87 nolu usera ait bir post create ediniz.

        String postBody = "Asistan candir";
        String postTitle = "Asistan can midir";
        Posts posts = new Posts();
        posts.setBody(postBody);
        posts.setTitle(postTitle);
        posts.setUser_id(87);

        postId = given()
                .header("Authorization", "Bearer 6238a176dc7534d85666960a9ed27bec54d93f6c37af7eae4023bc203f20b219")
                .contentType(ContentType.JSON)
                .body(posts)
                .when()
                .post("/posts")
                .then()
                .log().body()
                .body("data.title", equalTo(postTitle))
                .body("data.body", equalTo(postBody))
                .extract().jsonPath().getInt("data.id");


    }

    @Test(priority = 2)
    public void CreateGetPost() {

        given()
                .pathParam("postId", postId)

                .when()
                .get("/posts/{postId}")

                .then()
                .log().body()
                .body("data.id", equalTo(postId));

    }

    @Test(priority = 3)
    public void UpdatePost() {
// Task 6 : Create edilen Post un body sini güncelleyerek, bilgiyi kontrol ediniz.
        String body = "Tesekkurler";


        given()
                .pathParam("postId", postId)
                .header("Authorization", "Bearer 6238a176dc7534d85666960a9ed27bec54d93f6c37af7eae4023bc203f20b219")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        " \"body\": \"" + body + "\"\n" +
                        "    }")
                .when()
                .put("/posts/{postId}")
                .then()
                .log().body()
                .body("data.body", equalTo(body))

        ;
    }

    @Test(priority = 4)
    public void DeletePost() {
// Task 7 : Create edilen Post un body sini delete, bilgiyi kontrol ediniz.


        given()
                .pathParam("postId", postId)
                .header("Authorization", "Bearer 6238a176dc7534d85666960a9ed27bec54d93f6c37af7eae4023bc203f20b219")
                .when()
                .delete("/posts/{postId}")
                .then()
                .statusCode(204)


        ;
    }
}

