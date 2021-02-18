package BarrigaRest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataUtils {
	
	//isso retorna uma instancia do calendar representando a data atual
	//método que retorna a data atual para utilizar de forma dinamica nas requisições
	public static String getDataDiferencaDias(Integer qtdDias) {
		Calendar calendario = Calendar.getInstance();
		calendario.add(Calendar.DAY_OF_MONTH, qtdDias);
		//formatar em strig, com a mascara usada no teste
		return getDataFormatada(calendario.getTime());
	}
	
	public static String getDataFormatada(Date data) {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(data);
	}

}
