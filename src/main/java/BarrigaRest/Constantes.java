package BarrigaRest;

import io.restassured.http.ContentType;


// interface onde a Classe que a implementa deve obrigatoriamente obedecer, o método contem as config que serão
// estaticas e padrão em todos os testes
public interface Constantes {

	String APP_BASE_URL = "https://barrigarest.wcaquino.me";
	int APP_PORT = 443; //http = 80
	String APP_BASE_PATH = "";
	ContentType APP_CONTENT_TYPE = ContentType.JSON;		
	long MAX_TIMEOUT = 5000L;
	
	
	
}
