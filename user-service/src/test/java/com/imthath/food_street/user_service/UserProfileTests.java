package com.imthath.food_street.user_service;

import com.imthath.food_street.user_service.error.UserServiceError;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserProfileTests {
    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUser() {
        var request = new UserController.UserRequest(
                "9876543210",
                "John Doe",
                User.Role.USER
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/user")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", equalTo("John Doe"))
                .body("phoneNumber", equalTo("9876543210"))
                .body("role", equalTo("USER"))
                .body("id", notNullValue());
    }

    @Test
    void shouldNotCreateDuplicateUser() {
        var request = new UserController.UserRequest(
                "9876543210",
                "John Doe",
                User.Role.USER
        );
    }

    @Test
    void shouldGetAllUsers() {
        // Create users with different roles
        createUser("9876543210", "John Doe", User.Role.USER);
        createUser("9876543211", "Jane Doe", User.Role.R_ADMIN);

        given()
                .when()
                .get("/user")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(2))
                .body("name", hasItems("John Doe", "Jane Doe"))
                .body("phoneNumber", hasItems("9876543210", "9876543211"))
                .body("role", hasItems("USER", "R_ADMIN"));
    }

    @Test
    void shouldGetUserByPhone() {
        createUser("9876543210", "John Doe", User.Role.FC_ADMIN);

        given()
                .when()
                .get("/user/phone/9876543210")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("John Doe"))
                .body("phoneNumber", equalTo("9876543210"))
                .body("role", equalTo("FC_ADMIN"));
    }

    @Test
    void shouldUpdateUserName() {
        // Create user first
        var userId = createUser("9876543210", "John Doe", User.Role.USER)
                .path("id");

        given()
                .queryParam("name", "John Updated")
                .when()
                .patch("/user/" + userId)
                .then()
                .statusCode(HttpStatus.ACCEPTED.value());

        // Verify the update
        given()
                .when()
                .get("/user/phone/9876543210")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("John Updated"));
    }

    @Test
    void shouldReturnErrorWhenUpdatingNonExistentUser() {
        given()
                .queryParam("name", "John Updated")
                .when()
                .patch("/user/non-existent-id")
                .then()
                .statusCode(UserServiceError.USER_NOT_FOUND.getCode());
    }

    private Response createUser(String phone, String name, User.Role role) {
        var request = new UserController.UserRequest(phone, name, role);
        var response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/user");
        response.then().statusCode(HttpStatus.CREATED.value());
        return response;
    }
}