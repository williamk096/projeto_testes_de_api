package br.ce.william.rest.refatoracao;

import org.hamcrest.Matchers;
import org.junit.Test;

import BarrigaRest.BaseTest;
import BarrigaRest.DataUtils;
import br.ce.william.rest.BarrigaTestAplicacaoWebMovimentacao;
import io.restassured.RestAssured;

public class MovimentacaoTest extends BaseTest {


	@Test
	public void deveIncluirMovimentacaoComSucesso() {

		BarrigaTestAplicacaoWebMovimentacao mov = getMovimentacaoValida();

		RestAssured.given().body(mov).when().post("/transacoes").then().statusCode(201);

	}

	@Test
	public void deveValidarCamposObrigatoriosMovimentacao() {

		RestAssured.given().body("{}").when().post("/transacoes").then().statusCode(400).body("$", Matchers.hasSize(8));

	}

	@Test
	public void naoDeveInserirMovimentacaoComDataFutura() {

		BarrigaTestAplicacaoWebMovimentacao mov = getMovimentacaoValida();
		mov.setData_transacao(DataUtils.getDataDiferencaDias(2));

		RestAssured.given().body(mov).when().post("/transacoes").then().statusCode(400).body("msg",
				Matchers.hasItem("Data da Movimentação deve ser menor ou igual à data atual"));

	}

	@Test
	public void deveRemoverMovimentacao() {

		Integer MOV_ID = getIdMovimentacaoPelaDescricao("Movimentacao para exclusao");

		RestAssured.given().pathParam("id", MOV_ID).when().delete("/transacoes/{id}").then().statusCode(204);

	}

	@Test
	public void naoDeveRemoverContaComMovimentacao() {

		Integer CONTA_ID = getIdContaPeloNome("Conta com movimentacao");

		RestAssured.given().pathParam("id", CONTA_ID).when().delete("/contas/{id}").then().statusCode(500)
				.body("constraint", Matchers.is("transacoes_conta_id_foreign"));

	}

	public Integer getIdContaPeloNome(String nome) {
		return RestAssured.get("/contas?nome=" + nome).then().extract().path("id[0]");

	}

	public Integer getIdMovimentacaoPelaDescricao(String desc) {
		return RestAssured.get("/transacoes?descricao=" + desc).then().extract().path("id[0]");

	}

	private BarrigaTestAplicacaoWebMovimentacao getMovimentacaoValida() {
		BarrigaTestAplicacaoWebMovimentacao mov = new BarrigaTestAplicacaoWebMovimentacao();
		mov.setConta_id(getIdContaPeloNome("Conta para movimentacoes"));
		// movimentacao.setUsuario_id();
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
