package portfolioProblem;
import java.util.HashMap;

import solver.commun.MutationElementaire;

/**
 * This class represents an elementary mutation for portfolios.
 * @author thomasdoutre
 * @version 1.0
 * @since   2015-05-10
 */

public class Swap extends MutationElementaire {
	
	
	HashMap<Integer,Double> vecteur;
	
	public Swap(HashMap<Integer, Double> vect) {
		this.vecteur=vect;
	}

	public HashMap<Integer, Double> getVecteur() {
		return vecteur;
	}

	public void setVecteur(HashMap<Integer, Double> vecteur) {
		this.vecteur = vecteur;
	}
	
}
