package portfolioProblem;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;

import solver.commun.EnergiePotentielle;
import solver.commun.Etat;
import solver.commun.MutationElementaire;

/**
 * This class calculates portfolios' CVaR
 * @author  Thomas Doutre
 * @version 1.0
 * @since   2015-05-10
 */

public class ConditionalValueAtRisk extends EnergiePotentielle {

	/*
	
	public ConditionalValueAtRisk(Portfolio portfolio) {
		super.setPortfolio(portfolio);
	}

	
	
	/**
	 * This method is used to compute the Conditional Value at Risk of a portfolio.
	 * @return double Conditional Value at Risk.
	 */

	/*
	public double computeRisk(){
		double[] portfolioReturns = super.getPortfolio().getReturns();
		Percentile percentile = new Percentile();
		double valueAtRisk;
		valueAtRisk = - percentile.evaluate(portfolioReturns, 5.0);
		System.out.println("VaR = "+valueAtRisk);
		
		double sum = 0;
		int compt = 0;
		
		for(int i =0 ; i<portfolioReturns.length;i++){
			if(portfolioReturns[i]<(-valueAtRisk)){
				sum = sum + portfolioReturns[i];
				compt++;
			}
		}
		double conditionalVaR = - sum/compt;
		System.out.println("Conditional VaR = " +conditionalVaR);
		return conditionalVaR;
	}
	*/


	@Override
	public double calculer(Etat etat) {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public double calculerDeltaE(Etat etat, MutationElementaire mutation) {
		// TODO Auto-generated method stub
		return 0;
	}





}
