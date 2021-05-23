package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	MeteoDAO mdao;
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	private List<Citta> leCitta;
	private LinkedList<Citta> best;

	public Model() {

		mdao= new MeteoDAO();
		leCitta= mdao.getAllCitta();
	}
	
	public List<Citta> getAllCitta(){
		return mdao.getAllCitta();
	}

	// of course you can change the String output with what you think works best
	public double getUmiditaMedia(int mese, String localita) {
		return mdao.umiditaMedia(localita, mese);
	}
	
	// of course you can change the String output with what you think works best
	public List<Citta> trovaSequenza(int mese) {
		LinkedList<Citta> result= null;
		LinkedList<Citta> parziale= new LinkedList<>();
		
		for(Citta c: leCitta) {
			c.setRilevamenti(mdao.getAllRilevamentiLocalitaMese(mese, c.getNome()));
			//inserisco nella lista dei rilevamenti della città tutti i rilevamenti relativi al 
			//mese considerato 
		}
		cerca(parziale,0);
		return best;
	}

	private void cerca(LinkedList<Citta> parziale, int livello) {
		// condizione di terminazione
		if(livello==this.NUMERO_GIORNI_TOTALI) {
			Double costo= calcolaCosto(parziale);
			if(best==null || costo<calcolaCosto(best))
				best= new LinkedList<Citta>(parziale);
		}else {
			for(Citta prova: leCitta) {
				if(aggiuntaValida(prova, parziale)) {
					parziale.add(prova);
					cerca(parziale, livello+1);
					parziale.remove(parziale.size()-1);
				}
					
			}
		}
		
	}

	private boolean aggiuntaValida(Citta prova, LinkedList<Citta> parziale) {
		// condizioni
		// 1 devo visitare tutte le citta almeno una volta
		// 2 non posso trascorrere nei 15 giorni più di 6 giorni in una citta
		// 3 devo stare almeno 3 giorni una citta appena ci arrivo 
		
		//guardo quanti giorni ci sono stato
		int countGiorni=0;
		for(Citta c: parziale) {
			if(c.equals(prova))
				countGiorni++;
		}
		if(countGiorni>=this.NUMERO_GIORNI_CITTA_MAX)
			return false;
		
		if(parziale.size()==0) //prima citta 
			return true;
		if(parziale.size()==1 || parziale.size()==2) //se sono al 2 o al 3 giorno va bene solo se mi 
			                                         // mi trovo già in quella citta
			return parziale.get(parziale.size()-1).equals(prova);
		
		//rimango nella stessa citta
		if(parziale.get(parziale.size()-1).equals(prova))
			return true;
		
		//cambio citta -> mi  devo assicurare che in quella prima ci sia rimasto almeno 3 giorni
		if(parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2))&& 
				parziale.get(parziale.size()-2).equals(parziale.get(parziale.size()-3)))
			return true;
			
		return false;
	}

	private Double calcolaCosto(LinkedList<Citta> result) {
		double costo=0;
		
		//costo variabile
		for(int giorno=0; giorno<this.NUMERO_GIORNI_TOTALI; giorno++) {
			double umid= result.get(giorno).getRilevamenti().get(giorno).getUmidita();
			costo+= umid;
		}
		//costo fisso = pago 100 ogni volta che il tecnico cambia citta
		for(int giorno=1; giorno<this.NUMERO_GIORNI_TOTALI; giorno++) {
			if(!result.get(giorno).equals(result.get(giorno-1)))
				costo+=this.COST;
		}
		return costo;
	}
	

}
