package arc.gui.rest;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


public class UserJSONTest {
	@Test
	public void deveVerificarPrimeiroNível() {
		given()
		.when()
			.get("https://restapi.wcaquino.me/users/1")
		.then()
			.statusCode(200)
			.body("id", is(1))
			.body("name", containsString("Silva"))
			.body("age", greaterThan(18))
			;
	}
	
	@SuppressWarnings("removal")
	@Test
	public void deveVerificarPrimeiroNivelOutrasFormas() {
		Response response = RestAssured.request(Method.GET, "https://restapi.wcaquino.me/users/1");
		
		//path
		assertEquals(new Integer(1), response.path("id"));
		
		//json path
		JsonPath jpath = new JsonPath(response.asString());
		assertEquals(1, jpath.getInt("id"));
		
		//from
		int id = JsonPath.from(response.asString()).getInt("id");
		assertEquals(1, id);
		
	}
	
	@Test
	public void deveverificarSegundoNivel() {
		given()
		.when()
			.get("https://restapi.wcaquino.me/users/2")
		.then()
			.statusCode(200)
			.body("name", containsString("Joaquina"))
			.body("endereco.rua", is("Rua dos bobos"))
			;
			
	}
	@Test
	public void deveVerificarTerceiroLista() {
		given()
		.when()
			.get("https://restapi.wcaquino.me/users/3")
		.then()
			.statusCode(200)
			.body("name", containsString("Ana"))
			.body("filhos" , hasSize(2))
			.body("filhos[0].name" , is("Zezinho"))
			.body("filhos[1].name" , is("Luizinho"))
			.body("filhos.name", hasSize(2))
			.body("filhos.name", hasItem("Zezinho"))
			.body("filhos.name", hasItems("Zezinho", "Luizinho"))
			;
	}
	@Test
	public void deveRetornarErroUsuarioInexistente() {
		given()
		.when()
			.get("https://restapi.wcaquino.me/users/4")
		.then()
			.statusCode(404)
			.body("error", is("Usuário inexistente"))
			;
	}
	@Test
	public void deveVerificarListaRaiz() {
		given()
		.when()
			.get("https://restapi.wcaquino.me/users")
		.then()
		.statusCode(200)
		.body("", hasSize(3))
		.body("name", hasItems("João da Silva", "Maria Joaquina", "Ana Júlia"))
		.body("age[1]", is(25))
		.body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho")))
		.body("salary", contains(1234.5678f , 2500, null))	
		;
	}
	@Test
	public void devoFazerVerificacoesAvancadas() {
		given()
		.when()
			.get("https://restapi.wcaquino.me/users")
		.then()
		.statusCode(200)
		.body("", hasSize(3))
		.body("age.findAll{it <= 25}.size()", is(2))
		.body("age.findAll{it <= 25 && it > 20}.size()", is(1))
		.body("findAll{it.age <= 25 && it.age > 20}.name", hasItem("Maria Joaquina"))
		.body("findAll{it.age <= 25}[0].name", is("Maria Joaquina"))
		.body("findAll{it.age <= 25}[-1].name", is("Ana Júlia"))
		.body("find{it.age <= 25}.name", is("Maria Joaquina"))
		.body("findAll{it.name.contains('n')}.name", hasItems("Maria Joaquina", "Ana Júlia"))
		.body("findAll{it.name.length() > 10}.name", hasItems("Maria Joaquina", "João da Silva"))
		.body("name.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
		.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
		.body("age.collect{it * 2}", hasItems(60, 50, 40))
		.body("id.max()", is(3))
		.body("salary.min()", is(1234.5678f))
		.body("salary.findAll{it != null}.sum()", is(closeTo(3734.5678f, 0.001)))
		.body("salary.findAll{it != null}.sum()", allOf(greaterThan(3000d), lessThan(5000d)))
		;
	}
	@Test
	public void devoUnirJsonPathComJAVA() {
		ArrayList<String> names =
		given()
		.when()
			.get("https://restapi.wcaquino.me/users")
		.then()
		.statusCode(200)
		.extract().path("name.findAll{it.startsWith('Maria')}")
		;
		assertEquals(1, names.size());
		assertTrue(names.get(0).equalsIgnoreCase("mArIa Joaquina"));
		assertEquals(names.get(0).toUpperCase(), "maria joaquina".toUpperCase());
		
	}
}
