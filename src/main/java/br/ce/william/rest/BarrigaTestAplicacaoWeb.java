package br.ce.william.rest;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import BarrigaRest.BaseTest;
import BarrigaRest.DataUtils;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;

//executa os testes em ordem alfabetica / entretanto não é uma solução viavel
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BarrigaTestAplicacaoWeb extends BaseTest{

	private static String CONTA_NAME = "conta" + System.nanoTime();
	private static Integer CONTA_ID;
	private static Integer MOV_ID;
	
	@BeforeClass
	public static void login() {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "williambatista096@gmail.com");
		login.put("senha", "789456");
		
		String token = RestAssured.given()
			.body(login)
		.when()
			.post("/signin")
		.then()
			.statusCode(200)
			.extract().path("token")
		;
		
		RestAssured.requestSpecification.header("Authorization", "JWT " + token);
	}
	
	
	@Test
	public void naoDeveAcessarApisemToken() {
		
		FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
		req.removeHeader("Authorization");
		RestAssured.given()
			.when()
				.get("/contas")
			.then()
				.statusCode(401)
			;
	}
	
	@Test
	public void deveIncluirContaComSucesso() {
		
		CONTA_ID = RestAssured.given()
		.body("{ \"nome\": \"" +CONTA_NAME+"\"}")
		.when()
			.post("/contas")
		.then()
			.statusCode(201)
			.extract().path("id")
		;
		
	}
	
	@Test
	public void deveAlterarContaComSucesso() {
		
		RestAssured.given()
			.body("{ \"nome\": \""+CONTA_NAME+"conta de teste editada\"}")
			.pathParam("id", CONTA_ID)
		.when()
			.put("/contas/{id}")
		.then()
			.log().all()
			.statusCode(200)
			.body("nome", Matchers.is(CONTA_NAME+"conta de teste editada"))
		;
		
	}
	
	@Test
	public void naoDeveIncluirContaComMesmoNome() {
		
		RestAssured.given()
			.body("{ \"nome\": \""+CONTA_NAME+"conta de teste editada\"}")
		.when()
			.post("/contas")
		.then()
			.statusCode(400)
			.body("error", Matchers.is("Já existe uma conta com esse nome!"))
		;
		
	}
	
	@Test
	public void deveIncluirMovimentacaoComSucesso() {
		
		BarrigaTestAplicacaoWebMovimentacao mov = getMovimentacaoValida();
		
		MOV_ID = RestAssured.given()
			.body(mov)
		.when()
			.post("/transacoes")
		.then()
			.statusCode(201)
			.extract().path("id")
		;
		
	}
	
	@Test
	public void deveValidarCamposObrigatoriosMovimentacao() {
		
		RestAssured.given()
			.body("{}")
		.when()
			.post("/transacoes")
		.then()
			.statusCode(400)
			.body("$", Matchers.hasSize(8))
		;
		
	}
	
	@Test
	public void naoDeveInserirMovimentacaoComDataFutura() {
		
		BarrigaTestAplicacaoWebMovimentacao mov = getMovimentacaoValida();
		mov.setData_transacao(DataUtils.getDataDiferencaDias(2));
		
		RestAssured.given()
			.body(mov)
		.when()
			.post("/transacoes")
		.then()
			.statusCode(400)
			.body("msg", Matchers.hasItem("Data da Movimentacao deve ser menor ou igual � data atual"))
		;
		
	}
	
	@Test
	public void naoDeveRemoverContaComMovimentacao() {
		
		RestAssured.given()
		.when()
			.delete("/contas/11588")
		.then()
			.statusCode(500)
			.body("constraint", Matchers.is("transacoes_conta_id_foreign"))
		;
		
	}
	
	
	
	@Test
	public void deveRemoverMovimentacao() {
		
		RestAssured.given()
			.pathParam("id", MOV_ID)
		.when()
			.delete("/transacoes/11588")
		.then()
			.statusCode(400)
		;
		
	}
	

	private BarrigaTestAplicacaoWebMovimentacao getMovimentacaoValida() {
		BarrigaTestAplicacaoWebMovimentacao mov = new BarrigaTestAplicacaoWebMovimentacao();
		mov.setConta_id(CONTA_ID);
		//movimentacao.setUsuario_id();
		mov.setDescricao("testando movimentacao");
		mov.setEnvolvido("fulano");
		mov.setTipo("REC");
		mov.setData_transacao(DataUtils.getDataDiferencaDias(-1));
		mov.setData_pagamento(DataUtils.getDataDiferencaDias(5));
		mov.setValor(100f);
		mov.setStatus(true);
		
		return mov;
		
	}
	
}



