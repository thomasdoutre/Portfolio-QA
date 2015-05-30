package Optionnel;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;

import portfolioProblem.Portfolio;
import portfolioProblem.Swap;
import solver.commun.EnergiePotentielle;
import solver.commun.Etat;
import solver.commun.MutationElementaire;


/**
 * This class calculates portfolios' VaR
 * @version 1.0
 * @since   2015-05-10
 */

public class ValueAtRisk extends EnergiePotentielle {


	


/*
	*//**
	 * This method is used to compute the Value at Risk of a portfolio.
	 * @return double Value at Risk.
	 *//*

	public double computeRisk(){
		double[] portfolioReturns = this.getPortfolio().getReturns();
		Percentile percentile = new Percentile();
		double valueAtRisk;
		valueAtRisk = - percentile.evaluate(portfolioReturns, 5.0);
		return valueAtRisk;
	}
*/



/*	@Override
	public double calculer(Etat etat) {
		Portfolio port = (Portfolio)etat;
		double[] portfolioReturns = port.getReturns();
		
		return calculer(portfolioReturns);
	}

	private double calculer(double[] tab){
		Percentile percentile = new Percentile();
		double valueAtRisk;
		valueAtRisk = - percentile.evaluate(tab, 5.0);
		return valueAtRisk;
	}*/
	
	@Override
	public double calculer(Etat etat) {
		Portfolio port = (Portfolio)etat;
		double[] portfolioReturns = port.getReturns();
		
		return calculer(portfolioReturns);
	}

	private double calculer(double[] tab){
		Percentile percentile = new Percentile();
		double valueAtRisk;
		valueAtRisk = - percentile.evaluate(tab, 5.0);
		System.out.println("value at risk = "+valueAtRisk);
		return valueAtRisk;
	}


	@Override
	public double calculerDeltaE(Etat etat, MutationElementaire mutation) {
		
		Portfolio portfolio = (Portfolio) etat;
		Swap m = (Swap) mutation;
		HashMap<Integer, Double> vect = m.getVecteur();
	
		// portfolio.getTickersSet().getData().getReturnsMatrix();
		
		double[] newPortfolioReturns = Tools.cloneArray(portfolio.getReturns());
		
		
		for(Map.Entry<Integer, Double> entry : vect.entrySet()){
			
			for(int i =0; i<newPortfolioReturns.length;i++){
			newPortfolioReturns[entry.getKey()] += entry.getValue()*(portfolio.getTickersSet().getData().getReturnsMatrix()[i][entry.getKey()]);
			}
			
		}
		System.out.println("(calculer(newPortfolioReturns)-calculer(etat)) = "+ (calculer(newPortfolioReturns)-calculer(etat)));
		return (calculer(newPortfolioReturns)-calculer(etat));
	
	}

}
