import POJO.ToDo;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.Test;


import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Task {

    @Test
    public void Test1() {
        /** Task 1
         * create a request to https://httpstat.us/203
         * expect status 203
         * expect content type TEXT
         */

        given().

                when().
                get("https://httpstat.us/203")


                .then().
                contentType(ContentType.TEXT).
                statusCode(203);
    }

    private ResponseSpecification responseSpecification;
    private RequestSpecification requestSpecification;

    @Test
    public void Test2() {

        /** Task 2
         * create a request to https://httpstat.us/203
         * expect status 203
         * expect content type TEXT
         * expect BODY to be equal to "203 Non-Authoritative Information"
         */

        given().

                when().
                get("https://httpstat.us/203")


                .then().log().body().
                contentType(ContentType.TEXT).
                statusCode(203)
                .body(equalTo("203 Non-Authoritative Information"))
        ;
        //extract().body().asString();

    }

    @Test
    public void Test3() {
        /** Task 3
         *  create a request to https://jsonplaceholder.typicode.com/todos/2
         *  expect status 200
         *  expect content type JSON
         *  expect title in response body to be "quis ut nam facilis et officia qui"
         */
        given()


                .when().
                get("https://jsonplaceholder.typicode.com/todos/2")


                .then()
                .body("title", equalTo("quis ut nam facilis et officia qui"))
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    public void Test4() {
        /** Task 4
         * create a request to https://jsonplaceholder.typicode.com/todos/2
         *  expect status 200
         *  expect content type JSON
         *  expect response completed status to be false
         */
        given()

                .when().
                get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .body("completed", equalTo(false))
                .statusCode(200)
                .contentType(ContentType.JSON);


    }

    @Test
    public void Test5() {
        /** Task 5
         * create a request to https://jsonplaceholder.typicode.com/todos
         * expect status 200
         * expect content type JSON
         * expect third item have:
         *      title = "fugiat veniam minus"
         *      userId = 1
         */
        given()

                .when().
                get("https://jsonplaceholder.typicode.com/todos")
                .then()
                .body("userId[2]", equalTo(1))
                .body("title[2]", equalTo("fugiat veniam minus"))
                .statusCode(200)
                .contentType(ContentType.JSON);

    }

    @Test
    public void Test6() {
        /** Task 6
         * create a request to https://jsonplaceholder.typicode.com/todos/2
         * expect status 200
         * Converting Into POJO
         */
        ToDo todo =
                given()

                        .when().
                        get("https://jsonplaceholder.typicode.com/todos/2")
                        .then()
                        .statusCode(200)
                        .extract().as(ToDo.class);

        System.out.println("pojo.getTitle() = " + todo.getTitle());
        System.out.println("pojo.getTitle() = " + todo.getId());
        System.out.println("pojo.getTitle() = " + todo.isCompleted());
        System.out.println("pojo.getTitle() = " + todo.getUserId());
    }
    @Test
    public void Test7() {
        /** Task 7
         * create a request to https://jsonplaceholder.typicode.com/todos
         * expect status 200
         * Converting Array Into Array of POJOs
         */
        ToDo[] todo =
                given()

                        .when().
                        get("https://jsonplaceholder.typicode.com/todos")
                        .then()
                        .statusCode(200)
                        .extract().as(ToDo[].class);

        System.out.println("Arrays.toString(todo) = " + Arrays.toString(todo));    }
    @Test
    public void Test8() {
        /** Task 8 - Ödev 2
         * create a request to https://jsonplaceholder.typicode.com/todos
         * expect status 200
         * Converting Array Into List of POJOs
         */
        ToDo[] todo =
                given()

                        .when().
                        get("https://jsonplaceholder.typicode.com/todos")
                        .then()
                        .statusCode(200)
                        .extract().as(ToDo[].class);

        List<ToDo> ListTodo= Arrays.asList(todo);
        System.out.println("ListTodo = " + ListTodo);
    }
}
