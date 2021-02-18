package br.ce.william.rest.refatoracao;

import org.hamcrest.Matchers;
import org.junit.Test;

import BarrigaRest.BaseTest;
import io.restassured.RestAssured;

public class ContasTest extends BaseTest {

	@Test
	public void deveIncluirContaComSucesso() {
		System.out.println("incluir");
		RestAssured.given().body("{ \"nome\": \"Conta inserida\"}").when().post("/contas").then().statusCode(201)
				.extract().path("id");

	}

	@Test
	public void deveAlterarContaComSucesso() {
		System.out.println("alterar");
		Integer CONTA_ID = getIdContaPeloNome("Conta para alterar");

		RestAssured.given().body("{ \"nome\": \"conta alterada\"}").pathParam("id", CONTA_ID).when().put("/contas/{id}")
				.then().log().all().statusCode(200).body("nome", Matchers.is("conta alterada"));

	}

	@Test
	public void naoDeveIncluirContaComMesmoNome() {

		RestAssured.given().body("{ \"nome\": \"Conta mesmo nome\"}").when().post("/contas").then().statusCode(400)
				.body("error", Matchers.is("Já existe uma conta com esse nome!"));

	}

	public Integer getIdContaPeloNome(String nome) {
		return RestAssured.get("/contas?nome=" + nome).then().extract().path("id[0]");

	}

}
