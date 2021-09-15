package goRest;

import goRest.Model.*;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class GoRestTodos {
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
    public void getCommentPojo() {
        List<Todos> todosList =
                given()
                        .when()
                        .get("/todos")
                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .log().body()
                        .extract().jsonPath().getList("data", Todos.class);

        for (Todos t : todosList) {
            System.out.println("t = " + t);
        }
    }

    @Test(enabled = false)
    public void getUserPosts() {
        List<Todos> todosList =
                given()

                        .when()
                        .get("/users/44/todos")

                        .then()
                        .extract().jsonPath().getList("data", Todos.class);
        for (Todos t : todosList) {
            System.out.println("t = " + t);
        }
    }

    @Test(enabled = false)
    public void getDate() {
        // Ayin Sorusu: https://gorest.co.in/public/v1/todos  Api sinden dönen verilerdeki
        // zaman olarak ilk todos nun hangi userId ye ait olduğunu bulunuz

        List<Todos> todosList =
                given()

                        .when()
                        .get("/todos")

                        .then()
                        .extract().jsonPath().getList("data", Todos.class);
        for (Todos t : todosList) {
            if (t.getId() == 1) {
                System.out.println(t.getUser_id());
            }
        }
    }

    @Test(enabled = false)
    public void Task1() {
        // Task 1: https://gorest.co.in/public/v1/todos  Api sinden dönen verilerdeki
        //         en son todo nun id sini bulunuz

        List<Todos> todosList =
                given()

                        .when()
                        .get("/todos")

                        .then()
                        .extract().jsonPath().getList("data", Todos.class);
        int id = 0;
        for (Todos t : todosList) {
            if (t.getId() > id) {
                id = t.getId();
            }
        }
        System.out.println("id = " + id);
    }

    @Test(enabled = false)
    public void Task2() {
        // Task 1: https://gorest.co.in/public/v1/todos  Api sinden dönen verilerdeki
        //         en son todo nun id sini bulunuz

        int id = 0;
        for (int i = 1; i <= 101; i++) {
            List<Todos> todosList =
                    given()
                            .param("page", i)
                            .when()
                            .get("/todos")

                            .then()
                            .extract().jsonPath().getList("data", Todos.class);

            for (Todos t : todosList) {
                if (t.getId() > id) {
                    id = t.getId();
                }
            }

        }
        System.out.println("id = " + id);
    }

    @Test(enabled = false)
    public void Task3() {

        int totalPage = 0, page = 1, maxId = 0;
        do {

            Response response = // bir responsdan 2 tane extract yapacagim icin respons kullandim.
                    given()
                            .param("page", page) // ?page=1
                            .when()
                            .get("/todos")

                            .then()
                            // .log().body()
                            .extract().response();
            //kac sayfa oldugu bulduk.
            if (page == 1)
                totalPage = response.jsonPath().getInt("meta.pagination.pages");
            //Siradaki page in datasini list olarak aldik.
            List<Todos> pageList = response.jsonPath().getList("data", Todos.class);

            // Elimizdeki en son maxID yi alarak pagedeki ID ler karsilastirip en buyuk ID yi bulmus olacak

            for (int i = 0; i < pageList.size(); i++) {
                if (maxId < pageList.get(i).getId()) {
                    maxId = pageList.get(i).getId();
                }
            }
            page++;

        } while (page <= totalPage);
        System.out.println("maxId = " + maxId);
    }

    // Task 3 : https://gorest.co.in/public/v1/todos  Api sinden
    // dönen bütün bütün sayfalardaki bütün idleri tek bir Liste atınız.
    @Test(enabled = false)
    public void task4() {
        int totalPage = 0, page = 1;
        List<Integer> id = new ArrayList<>();
        do {

            Response response =
                    given()
                            .param("page", page) // ?page=1
                            .when()
                            .get("/todos")

                            .then()
                            .extract().response();

            if (page == 1)
                totalPage = response.jsonPath().getInt("meta.pagination.pages");

            List<Integer> pageList = response.jsonPath().getList("data.id");
            id.addAll(pageList);
            page++;
        } while (page <= totalPage);
        System.out.println("id = " + id);
    }

    public String date() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime date = LocalDateTime.now();
        LocalDateTime date2 = LocalDateTime.now().minusMonths(3);
        return date.format(dateTimeFormatter);
    }


    int todoId;

    @Test(priority = 1)
    public void CreateTodo() {
        // Task 4 : https://gorest.co.in/public/v1/todos  Api sine
        // 1 todo Create ediniz.

        Todos todo = new Todos();
        todo.setTitle("Bismillah her hayrin basidir.");
        todo.setStatus("completed");
        todo.setDue_on(date());


        todoId = given()
                .header("Authorization", "Bearer 6238a176dc7534d85666960a9ed27bec54d93f6c37af7eae4023bc203f20b219")
                .contentType(ContentType.JSON)
                .body(todo)
                .when()
                .post("/users/44/todos")
                .then()
                .log().body()
                .statusCode(201)
                .extract().jsonPath().getInt("data.id")
        ;
    }
// Task 5 : Create edilen ToDo yu get yaparak id sini kontrol ediniz.

    @Test(dependsOnMethods = "CreateTodo", priority = 2)
    public void getTodo() {
        // Task 5 : Create edilen ToDoyu get yaparak id sini kontrol ediniz.
        given()
                .pathParam("todoId", todoId)
                .when()
                .get("/todos/{todoId}")
                .then()
                .log().body()
                .statusCode(200)
                .body("data.id", equalTo(todoId));
    }

    @Test(dependsOnMethods = "CreateTodo", priority = 3)
    public void UpdateTodos() {
        // Task 4 : https://gorest.co.in/public/v1/todos  Api sine
        // 1 todo update ediniz.
        String status = "pending";

        given()
                .pathParam("todoId", todoId)
                .header("Authorization", "Bearer 6238a176dc7534d85666960a9ed27bec54d93f6c37af7eae4023bc203f20b219")
                .contentType(ContentType.JSON)
                .log().uri()
                .body("{\"status\":\"" + status + "\"}")
                .when()
                .put("/todos/{todoId}")
                .then()
                .log().body().
                body("data.status", equalTo(status))

        ;
    }

    @Test(dependsOnMethods = "CreateTodo", priority = 4)
    public void DeleteTodos() {
        given()
                .pathParam("todoId", todoId)
                .header("Authorization", "Bearer 6238a176dc7534d85666960a9ed27bec54d93f6c37af7eae4023bc203f20b219")

                .when()
                .delete("/todos/{todoId}")
                .then()
                .log().body()
                .statusCode(204);
    }
    @Test( priority = 5)
    public void DeleteNegativeTodos() {
        given()
                .pathParam("todoId", todoId)

                .when()
                .delete("/todos/{todoId}")
                .then()
                .log().body()
                .statusCode(404);
    }
}


