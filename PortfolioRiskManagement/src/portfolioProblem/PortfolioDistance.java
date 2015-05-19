package portfolioProblem;

import java.util.HashMap;
import java.util.Map;

import solver.commun.EnergieCinetique;
import solver.commun.Etat;
import solver.commun.IMutation;
import solver.commun.MutationElementaire;
import solver.commun.Particule;
import solver.commun.Probleme;




public class PortfolioDistance extends EnergieCinetique {

	@Override
	public double calculer(Probleme probleme) {
		
		PortfolioParticule portfolioParticule = (PortfolioParticule) probleme;
		
		for (int p = 0; p < portfolioParticule.getNombrerepliques()-1 ; p++){
			Portfolio etat = (Portfolio) probleme.etats[p];
			Portfolio etatNext = (Portfolio) probleme.etats[p+1];
			
			
			
		}
		
		return 0;
	}

	@Override
	public double calculerDeltaE(Etat etat, Etat prev, Etat next,
			MutationElementaire mutation) {
		
		Portfolio portfolio=(Portfolio) etat;
		Portfolio portfolionext=(Portfolio) next;
		Portfolio portfolioprev=(Portfolio) prev;
		
		Swap mutationAssets = (Swap) mutation;
		HashMap<Integer,Double> vect = mutationAssets.getVecteur();
		
		int longueur = portfolio.getWeights().length;
		
		double[] poidsnext=portfolionext.getWeights();
		double[] poids=portfolio.getWeights();
		double[] poidsprev=portfolioprev.getWeights();
		
		double EcPrecedent =0;
		
		for (int i=0;i<longueur;i++) {
			EcPrecedent=+Math.pow(poidsnext[i]-poids[i], 2)+Math.pow(poidsprev[i]-poids[i], 2);
		}
		
		double EcSuivant=0; 
		
		for(Map.Entry<Integer, Double> entry : vect.entrySet()){
			poids[entry.getKey()]=+entry.getValue();
		}
		
		for (int i=0;i<longueur;i++) {
			EcSuivant=+Math.pow(poidsnext[i]-poids[i], 2)+Math.pow(poidsprev[i]-poids[i], 2);
		}
		
		return Math.sqrt(EcSuivant-EcPrecedent);
	}

	@Override
	public double calculerDeltaEUB(Etat etat, Etat prev, Etat next,
			MutationElementaire mutation) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	

	
	
}
