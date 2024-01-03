package arc.gui.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class VerbosTest {
	
	@Test
	public void deveSalvarUsuario() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{ \"name\": \"Guilherme\",\"age\": 21 }")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Guilherme"))
			.body("age", is(21))
			;
	}
	@Test
	public void naoDeveSalvarUsuarioSemNome() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"age\": 21 }")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(400)
			.body("id", is(nullValue()))
			.body("error", is("Name é um atributo obrigatório"))
			;
	}
	
	@Test
	public void deveAlterarUsuario() {
		given()
		.log().all()
		.contentType("application/json")
		.body("{ \"name\": \"Marcos\",\"age\": 35 }")
	.when()
		.put("https://restapi.wcaquino.me/users/1")
	.then()
		.log().all()
		.statusCode(200)
		.body("id", is(1))
		.body("name", is("Marcos"))
		.body("age", is(35))
		;
	}
	@Test
	public void devoCustomizarURL() {
		given()
		.log().all()
		.contentType("application/json")
		.body("{ \"name\": \"Marcos\",\"age\": 35 }")
	.when()
		.put("https://restapi.wcaquino.me/{entidade}/{userId}", "users", "1")
	.then()
		.log().all()
		.statusCode(200)
		.body("id", is(1))
		.body("name", is("Marcos"))
		.body("age", is(35))
		;
	}
	@Test
	public void devoCustomizarURLParte2() {
		given()
		.log().all()
		.contentType("application/json")
		.body("{ \"name\": \"Marcos\",\"age\": 35 }")
		.pathParam("entidade", "users")
		.pathParam("userId", 1)
	.when()
		.put("https://restapi.wcaquino.me/{entidade}/{userId}")
	.then()
		.log().all()
		.statusCode(200)
		.body("id", is(1))
		.body("name", is("Marcos"))
		.body("age", is(35))
		;
	}
	@Test
	public void deveRemoverUsuario() {
		given()
			.log().all()
		.when()
			.delete("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(204)
		;
	}
	@Test
	public void naoDeveRemoverUsuarioInexistente() {
		given()
			.log().all()
		.when()
			.delete("https://restapi.wcaquino.me/users/10")
		.then()
			.log().all()
			.statusCode(400)
			.body("error", is("Registro inexistente"))
		;
	}
}
