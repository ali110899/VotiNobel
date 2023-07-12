package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {	
	
	private List<Esame> allEsami;
	private Set<Esame> migliore; //aggiorno con ricorsione
	private double mediaMigliore; //aggiorno con ricorsione
	
	public Model() {
		
		EsameDAO dao = new EsameDAO();
		allEsami = dao.getTuttiEsami();
		
	}
	
	/*
	 * Dobbiamo dare la possibilità alla mia funzione ricorsiva
	 * se un esame vada aggiunto oppure no
	 * Così da evitare soluzioni che siano delle 
	 * permutazioni di soluzioni precedenti!
	 */
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		
		//lista di esami migliori(momentanei)
		migliore = new HashSet<Esame>();
		//media migliore del Set(momentanea)
		mediaMigliore=0;
		
		Set<Esame> parziale = new HashSet<Esame>();
		//inizializzo
		cercaMeglio(parziale, 0, numeroCrediti);
		
		return migliore;		

	}
	
	private void cercaMeglio(Set<Esame> parziale, int L, int numeroCrediti) {
		

		//utilizzo un metodo già fornito per cacolarmi la mia somma
		int sommaC = sommaCrediti(parziale);
		
		if(sommaC>numeroCrediti) { //OPZIONE 1
			//soluzione non trovata->violo il vincolo dell'esercizio
			return;
		}
		
		if(sommaC==numeroCrediti) { //OPZIONE 2
			//potrei avere la mia soluzione
			//controlliamo se è migliore della soluzione già presente
			double mediaVoti = calcolaMedia(parziale);
			if(mediaVoti>mediaMigliore) {
				mediaMigliore = mediaVoti;
				//aggiorno la lista migliore con quello trovato ora
				migliore = new HashSet<Esame>(parziale);
			}
			return;
		}
		
		if(L==allEsami.size()) { //OPZIONE 3
			//ho preso in considerazione tutti i miei esami
			return;
		}
		
		//OPZIONE 4: 
		//provo ad aggiungere il prossimo elemento
		//prendo esame della mia lista al livello L(L è l'indice con cui sto scorrendo la mia lista esami)
		parziale.add(allEsami.get(L));
		cercaMeglio(parziale, L+1, numeroCrediti);
		//poichè sono uscita dal primo cercaMeglio, vuol dire che l'ultimo esame non voglio aggiungerlo
		parziale.remove(allEsami.get(L));
		//provo a non aggiungere il prossimo elemento ma quello dopo ancora(riciclo ma non più da L ma da L+1
		//uscirò da qua solo quando avrò soluzione ottima
		cercaMeglio(parziale, L+1, numeroCrediti);
		
	}
	/*
	private void cerca(Set<Esame> parziale, int L, int numeroCrediti) {
		
		//utilizzo un metodo già fornito per cacolarmi la mia somma
		int sommaC = sommaCrediti(parziale);
		
		if(sommaC>numeroCrediti) { //OPZIONE 1
			//soluzione non trovata->violo il vincolo dell'esercizio
			return;
		}
		
		if(sommaC==numeroCrediti) { //OPZIONE 2
			//potrei avere la mia soluzione
			//controlliamo se è migliore della soluzione già presente
			double mediaVoti = calcolaMedia(parziale);
			if(mediaVoti>mediaMigliore) {
				mediaMigliore = mediaVoti;
				//aggiorno la lista migliore con quello trovato ora
				migliore = new HashSet<Esame>(parziale);
			}
			return;
		}
		
		if(L==allEsami.size()) { //OPZIONE 3
			//ho preso in considerazione tutti i miei esami
			return;
		}
		
		//OPZIONE 4: 
		//devo aggiungere esami per cercare di avvicinarmi a m somma crediti
		
		for(Esame e : allEsami) {
			if(parziale.contains(e)==false) {
				//se sono diversi aggiungo l'esame
				parziale.add(e);
				//ricomincio con la ricorsione-->cerca di Set<>parziale
				cerca(parziale, L+1, numeroCrediti); //ricorda di incrementare livello!!
				//faccio backtracking
				parziale.remove(e);
			}
		}
	}
	*/

	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
