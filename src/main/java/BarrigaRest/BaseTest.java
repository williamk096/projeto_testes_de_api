package BarrigaRest;

import org.hamcrest.Matchers;
import org.junit.BeforeClass;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;

public class BaseTest implements Constantes {

	// esse método implementa a interface Constantes, é chamado antes de todo e qualquer teste que será executado
	// o rest assured está implementando os valores que foram descritos no "contrato" da interface
	// usando atributos estaticos do rest assured para colocar essas config que vao rodar antes dos testes
	@BeforeClass
	public static void setup() {
		System.out.println("before base");
		RestAssured.baseURI = APP_BASE_URL;
		RestAssured.port = APP_PORT;
		RestAssured.basePath = APP_BASE_PATH;
		
		
		RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
		reqBuilder.setContentType(APP_CONTENT_TYPE);
		RestAssured.requestSpecification = reqBuilder.build();
		
		ResponseSpecBuilder respBuilder = new ResponseSpecBuilder();
		respBuilder.expectResponseTime(Matchers.lessThan(MAX_TIMEOUT));
		RestAssured.responseSpecification = respBuilder.build();
		
		//habilitar o registro de solicitação e resposta se a validação falhar
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}
	
	
	
}
