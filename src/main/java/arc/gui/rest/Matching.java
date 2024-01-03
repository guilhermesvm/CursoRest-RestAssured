package arc.gui.rest;

import static io.restassured.RestAssured.*; //Não precisa de RestAssured após usar importar
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class Matching {
	@Test
	public void testOlaMundo() {
		Response response = RestAssured.request(Method.GET, "https://restapi.wcaquino.me/ola");
		
		//System.out.println(response.getBody().asString().equals("Ola Mundo!"));
		//System.out.println(response.statusCode());
		
		Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
		Assert.assertTrue(response.statusCode() == 200);
		Assert.assertTrue("O status code deve ser 200.", response.statusCode() == 200);
		Assert.assertEquals(200, response.statusCode());
		
		
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
	}
	
	@Test
	public void devoConhecerOutrasFormasRestAssured() {
		Response response = request(Method.GET, "https://restapi.wcaquino.me/ola");
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
		
		given() //Pré-condições
		.when() //Verbos
			.get("https://restapi.wcaquino.me/ola") 
		.then() //Verificações
			.statusCode(200); 
	}
	
	@Test
	public void devoConhecerMatchersHamcrest() { //Checagem 
		assertThat("Maria", is("Maria"));
		assertThat("Maria", is("Maria"));
		assertThat("Maria", not("João"));
		assertThat("Maria", anyOf(is("Maria"), is("Joaquim")));
		assertThat("Joaquina", allOf(startsWith("Joa"), endsWith("ina"), containsString("qui")));
		assertThat(128, is(128));
		assertThat(128, isA(Integer.class));
		assertThat(128d, isA(Double.class));
		assertThat(128d, greaterThan(125d));
		assertThat(128d, lessThan(130d));
		 
		List<Integer> impares = Arrays.asList(1,3,5,7,9);
		assertThat(impares, hasSize(5));
		assertThat(impares, contains(1,3,5,7,9));
		assertThat(impares, containsInAnyOrder(3,1,7,5,9));
		assertThat(impares, hasItem(3));
		assertThat(impares, hasItems(1,5));
	}
	
	@Test
	public void devoValidarBody() {
		given()
		.when() 
			.get("https://restapi.wcaquino.me/ola") 
		.then() 
			.statusCode(200)
			.body(is("Ola Mundo!"))
			.body(containsString("Mundo"))
			.body(is(not(nullValue())));
	}
}
