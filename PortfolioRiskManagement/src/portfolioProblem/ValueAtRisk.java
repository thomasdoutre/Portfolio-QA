package portfolioProblem;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;

import solver.commun.EnergiePotentielle;
import solver.commun.Etat;
import solver.commun.MutationElementaire;
import Optionnel.Tools;


/**
 * This class calculates portfolios' VaR
 * @author thomasdoutre
 * @version 1.0
 * @since   2015-05-10
 */

public class ValueAtRisk extends EnergiePotentielle {

	/**
	 * This method is used to calculate the VaR of a portfolio (one of the problem's states).
	 * @param etat the state considered.
	 * @return the VaR.
	 */
	
	@Override
	public double calculer(Etat etat) {
		Portfolio port = (Portfolio)etat;
		double[] portfolioReturns = port.getReturns();
		
		return calculer(portfolioReturns);
	}

	/**
	 * This method is used to calculate the VaR of an array of historical returns.
	 * @param tab the retuns.
	 * @return the VaR.
	 */
	
	private double calculer(double[] tab){
		Percentile percentile = new Percentile();
		double valueAtRisk;
		valueAtRisk = - percentile.evaluate(tab, 5.0);
		return valueAtRisk;
	}

	/**
	 * This method is used to calculate the distance in terms of risk (energy) between a state and its mutation
	 * @param etat the state.
	 * @param mutation the mutation to be applied
	 * @return the energy.
	 */
	@Override
	public double calculerDeltaE(Etat etat, MutationElementaire mutation) {
		
		Portfolio portfolio = (Portfolio) etat;
		Swap m = (Swap) mutation;
		HashMap<Integer, Double> vect = m.getVecteur();
	
		portfolio.getTickersSet().getData().getReturnsMatrix();

		double[] newPortfolioWeights = Tools.cloneArray(portfolio.getWeights());
		
		
		for(Map.Entry<Integer, Double> entry : vect.entrySet()){

			newPortfolioWeights[entry.getKey()] += entry.getValue();
		}
		Portfolio newPortfolio = new Portfolio(portfolio.getTickersSet(), newPortfolioWeights);
		double[] newPortfolioReturns =  newPortfolio.computeReturns(newPortfolioWeights);
		
		return (calculer(newPortfolioReturns)-calculer(etat));
	}

}
