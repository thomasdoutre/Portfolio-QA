package portfolioProblem;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;

import solver.commun.EnergiePotentielle;
import solver.commun.Etat;
import solver.commun.MutationElementaire;
import Optionnel.Tools;

/**
 * This class calculates portfolios' Conditional Value at Risk
 * @author thomasdoutre
 * @version 1.0
 * @since   2015-05-10
 */

public class ConditionalValueAtRisk extends EnergiePotentielle {


	/**
	 * This method is used to calculate the CVaR of a portfolio (one of the problem's states).
	 * @param etat the state considered.
	 * @return the CVaR.
	 */
	@Override
	public double calculer(Etat etat) {

		Portfolio port = (Portfolio)etat;
		double[] portfolioReturns = port.getReturns();

		return calculer(portfolioReturns);
	}

	/**
	 * This method is used to calculate the CVaR of an array of historical returns.
	 * @param tab the retuns.
	 * @return the CVaR.
	 */
	private double calculer(double[] tab){
		Percentile percentile = new Percentile();
		double valueAtRisk;
		valueAtRisk = - percentile.evaluate(tab, 5.0);

		double sum = 0;
		int compt = 0;

		for(int i =0 ; i<tab.length;i++){
			if(tab[i]<(-valueAtRisk)){
				sum = sum + tab[i];
				compt++;
			}
		}
		double conditionalVaR = - sum/compt;
		return conditionalVaR;
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
		HashMap<Integer, Double> vect = m.vecteur;

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
