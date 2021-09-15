package goRest;

import goRest.Model.Comment;
import goRest.Model.CommentsBody;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class GoRestCommentsTest {
    // https://gorest.co.in/public/v1/comments apisinden donen verilerdeki datayi bir
    // nesne yardimiyla List olarak aliniz.
    @Test
    public void getCommentPojo() {
        List<Comment> commentList =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/comments")
                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .log().body()
                        .extract().jsonPath().getList("data", Comment.class);

        for (Comment c : commentList) {
            System.out.println("c = " + c);
        }
    }


    // Bütün Comment lardaki emailleri bir list olarak alınız ve
    //    // içinde "acharya_rajinder@ankunding.biz" olduğunu doğrulayınız.
    @Test(enabled = false)
    public void getEmailRespons() {
        Response donenSonuc =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/comments")
                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().response();


        List<Comment> userList = donenSonuc.jsonPath().getList("data.email");
        Assert.assertTrue(userList.contains("acharya_rajinder@ankunding.biz"));
    }

    @Test(enabled = false)
    public void getEmailList2() {
        List<Comment> commentList =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/comments")
                        .then()
                        .log().body()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .log().body()
                        .extract().jsonPath().getList("data.email");
        Assert.assertTrue(commentList.contains("acharya_rajinder@ankunding.biz"));
    }

    // Task 3 : https://gorest.co.in/public/v1/comments  Api sinden
    // dönen bütün verileri tek bir nesneye dönüştürünüz

    @Test(enabled = false)
    public void GetObject() {
        CommentsBody donenSonuc =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/comments")
                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().as(CommentsBody.class);
        System.out.println("firstUser = " + donenSonuc);

        //Artik elimde response yani tum datanin nesne hali var.

    }

    int commentsId = 0;

    @Test(priority = 1)
    public void CreateComments() {
        // Task 4 : https://gorest.co.in/public/v1/comments  Api sine
        // 1 Comments Create ediniz.

        Comment comments = new Comment();
        comments.setName("Mesut Assurence");
        comments.setEmail("mesut@gmail.com");
        comments.setBody("Asistan");

        commentsId = given()
                .header("Authorization", "Bearer 6238a176dc7534d85666960a9ed27bec54d93f6c37af7eae4023bc203f20b219")
                .contentType(ContentType.JSON)
                .body(comments)
                .when()
                .post("https://gorest.co.in/public/v1/posts/68/comments")
                .then()
                .log().body()
                .extract().jsonPath().getInt("data.id")
        ;
    }

    @Test(dependsOnMethods = "CreateComments")
    public void getComment() {
        given()
                .pathParam("commentsId", commentsId)


                .when()
                .get("https://gorest.co.in/public/v1/comments/{commentsId}")


                .then()
                .body("data.id", equalTo(commentsId))
                .statusCode(200);
    }


    @Test(priority = 2)
    public void UpdateComments() {
        // Task 4 : https://gorest.co.in/public/v1/comments  Api sine
        // 1 Comments update ediniz.
        String body = "Restassured konusunun üzerinde oldukça duruyoruz";


        given()
                .pathParam("commentsId", commentsId)
                .header("Authorization", "Bearer 6238a176dc7534d85666960a9ed27bec54d93f6c37af7eae4023bc203f20b219")
                .contentType(ContentType.JSON)
                .body("{\"body\":\"" + body + "\"}")
                .when()
                .put("https://gorest.co.in/public/v1/comments/{commentsId}")
                .then()
                .log().body()
                .body("data.body", equalTo(body))
        ;
    }

    @Test(priority = 3)
    public void deleteComments() {


        given()
                .pathParam("commentsId", commentsId)
                .header("Authorization", "Bearer 6238a176dc7534d85666960a9ed27bec54d93f6c37af7eae4023bc203f20b219")
                .when()
                .delete("https://gorest.co.in/public/v1/comments/{commentsId}")
                .then()
                .log().body()
                .statusCode(204)
        ;
    }

    @Test(priority = 4)
    public void deleteCommentsNegative() {
        // Task 4 : https://gorest.co.in/public/v1/comments  Api sine
        // 1 Comments Create ediniz.

        given()
                .pathParam("commentsId", commentsId)
                .header("Authorization", "Bearer 6238a176dc7534d85666960a9ed27bec54d93f6c37af7eae4023bc203f20b219")
                .when()
                .delete("https://gorest.co.in/public/v1/comments/{commentsId}")
                .then()
                .statusCode(404)
        ;
    }

}

