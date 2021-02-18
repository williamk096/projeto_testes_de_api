package br.ce.william.rest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.hamcrest.Matchers;
import org.junit.Test;

import io.restassured.RestAssured;

//classe para testes de envio de arquivo
public class FileTest {

	@Test
	public void deveObrigarEnvioArquivo() {
		RestAssured.given()
			.log().all()
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(404)    //deveria ser 400
			.body("error", Matchers.is("Arquivo não enviado"))
		;	
	}
	
	@Test
	public void deveFazerUploadArquivo() {
		RestAssured.given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/users.pdf"))
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(200)    //deveria ser 400
			.body("name", Matchers.is("users.pdf"))
		;
		
	}
	
	//usar um arquivo com maos de um 1mb
	@Test
	public void naoDeveFazerUploadArquivo() {
		RestAssured.given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/users.pdf"))
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			//.time(lessThan(5000l))
			.statusCode(200)
		;
		
	}
	
	@Test
	public void deveBaixarArquivo() throws IOException {
		byte[] img = RestAssured.given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/download")
		.then()
			.log().all()
			.statusCode(200)
			.extract().asByteArray();
		;
		
		File imagem = new File("src/main/resources/file.jpg");
		OutputStream out = new FileOutputStream(imagem);
		out.write(img);
		out.close();
	}

}
