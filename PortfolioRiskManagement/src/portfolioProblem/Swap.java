package portfolioProblem;
import java.util.HashMap;
import solver.commun.MutationElementaire;


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
