package it.polito.tdp.meteo.model;

import java.util.List;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	MeteoDAO mdao;
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	public Model() {

		mdao= new MeteoDAO();
	}
	
	public List<Citta> getAllCitta(){
		return mdao.getAllCitta();
	}

	// of course you can change the String output with what you think works best
	public double getUmiditaMedia(int mese, String localita) {
		return mdao.umiditaMedia(localita, mese);
	}
	
	// of course you can change the String output with what you think works best
	public String trovaSequenza(int mese) {
		return "TODO!";
	}
	

}
