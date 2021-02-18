package br.ce.william.rest.refatoracao.suite;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import BarrigaRest.BaseTest;
import br.ce.william.rest.refatoracao.ContasTest;
import br.ce.william.rest.refatoracao.MovimentacaoTest;
import io.restassured.RestAssured;

//suites das classes de teste

@RunWith(org.junit.runners.Suite.class)
@SuiteClasses({
	ContasTest.class,
	MovimentacaoTest.class
	
})
public class Suite extends BaseTest {
	
	@BeforeClass
	public static void login() {
		System.out.println("Before Conta");
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "williambatista096@gmail.com");
		login.put("senha", "789456");

		String token = RestAssured.given().body(login).when().post("/signin").then().statusCode(200).extract()
				.path("token");

		RestAssured.requestSpecification.header("Authorization", "JWT " + token);
		RestAssured.get("/reset").then().statusCode(200);
	}
	
}
