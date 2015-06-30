package portfolioProblem;

import java.util.HashMap;
import java.util.Map;

import solver.commun.EnergieCinetique;
import solver.commun.Etat;
import solver.commun.MutationElementaire;
import solver.commun.Probleme;
import Optionnel.Tools;

/**
 * This class decribes a portfolio
 * @author thomasdoutre
 * @version 1.0
 * @since   2015-05-10
 */

public class PortfolioDistance extends EnergieCinetique {

	/**
	 * This method is used to calculate the Kinetic Energy of the problem.
	 * @param probleme the problem considered.
	 * @return the Kinetic Energy.
	 */
	@Override
	public double calculer(Probleme probleme) {

		PortfolioParticule portfolioParticule = (PortfolioParticule) probleme;
		double Ec=0;
		int NombreRepliques = portfolioParticule.getNombrerepliques();
		int longueur = ((Portfolio)probleme.etats[0]).getWeights().length;

		for (int p = 0; p < NombreRepliques-1 ; p++){

			Portfolio etat = (Portfolio) probleme.etats[p];
			Portfolio etatNext = (Portfolio) probleme.etats[p+1];

			double[] poidsActuel = etat.getWeights();
			double[] poidsNext = etatNext.getWeights();

			for (int i=0;i<longueur;i++) {
				Ec+=Math.pow(poidsNext[i]-poidsActuel[i], 2);
			}
		}

		Portfolio dernierEtat = (Portfolio) probleme.etats[NombreRepliques];
		Portfolio premierEtat = (Portfolio) probleme.etats[0];

		double[] poidsActuel = dernierEtat.getWeights();
		double[] poidsNext = premierEtat.getWeights();

		for (int i=0;i<longueur;i++) {
			Ec+=Math.pow(poidsNext[i]-poidsActuel[i], 2);
		}

		Ec=Math.sqrt(2*Ec);

		return Ec;
	}

	/**
	 * This method is used to calculate the distance in terms of energy between a state and its mutation
	 * @param etat the state.
	 * @param prev the previous state
	 * @param next the next state
	 * @param mutation the mutation to be applied
	 * @return the energy.
	 */
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
			EcPrecedent+=Math.pow(poidsnext[i]-poids[i], 2)+Math.pow(poidsprev[i]-poids[i], 2);
		}

		double EcSuivant=0; 

		double[] nouveauxPoidsFactices = Tools.cloneArray(poids);
		for(Map.Entry<Integer, Double> entry : vect.entrySet()){
			nouveauxPoidsFactices[entry.getKey()] += entry.getValue();
		}

		for (int i=0;i<longueur;i++) {
			EcSuivant+=Math.pow(poidsnext[i]-nouveauxPoidsFactices[i], 2)+Math.pow(poidsprev[i]-nouveauxPoidsFactices[i], 2);
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
