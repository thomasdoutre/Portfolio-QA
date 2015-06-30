package portfolioProblem;

import java.util.HashMap;
import java.util.Map;

import solver.commun.EnergiePotentielle;
import solver.commun.Etat;
import solver.commun.MutationElementaire;
import Optionnel.Tools;

/**
 * This class represents a fake risk metric in order to debug the program.
 * @author thomasdoutre
 * @version 1.0
 * @since   2015-06-04
 */

public class DebugRisk extends EnergiePotentielle {
	
	@Override
	public double calculer(Etat etat) {

		Portfolio port = (Portfolio)etat;
		double[] portfolioWeights = port.getWeights();
		
		System.out.println("portfolioWeights[2] = "+ portfolioWeights[2]);
		System.out.println("CALCULER ETAT : portfolioWeights :");
		Tools.printArray(portfolioWeights);
		return portfolioWeights[2];
	}
	
	private double calculer(double[] tab){
		
		return tab[2];
	}



	@Override
	public double calculerDeltaE(Etat etat, MutationElementaire mutation) {
		
		System.out.println("Calcul du deltaE sur le DebugRisk");
		Portfolio portfolio = (Portfolio) etat;
		Swap m = (Swap) mutation;
		HashMap<Integer, Double> vect = m.vecteur;
	
		portfolio.getTickersSet().getData().getReturnsMatrix();
		
		double[] newPortfolioWeights = Tools.cloneArray(portfolio.getWeights());
		
		int longDeVecteur = 0;
		for(Map.Entry<Integer, Double> entry : vect.entrySet()){

			longDeVecteur++;
			newPortfolioWeights[entry.getKey()] += entry.getValue();
			
		}
		System.out.println("longDeVecteur = "+longDeVecteur);
		System.out.println("(calculer(newPortfolioReturns)-calculer(etat)) = "+(calculer(newPortfolioWeights)-calculer(etat)));
		return (calculer(newPortfolioWeights)-calculer(portfolio.getWeights()));
		
		
		
		
	}


}
