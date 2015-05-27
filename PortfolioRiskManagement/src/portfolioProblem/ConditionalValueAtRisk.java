package portfolioProblem;
import java.util.HashMap;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;

import Optionnel.Tools;
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


	@Override
	public double calculer(Etat etat) {

		Portfolio port = (Portfolio)etat;
		double[] portfolioReturns = port.getReturns();
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
		System.out.println("compt = "+compt);
		double conditionalVaR = - sum/compt;
		System.out.println("Conditional VaR = " +conditionalVaR);
		return conditionalVaR;
	}



	@Override
	public double calculerDeltaE(Etat etat, MutationElementaire mutation) {
		
		Portfolio portfolio = (Portfolio) etat;
		Portfolio newPortfolio = portfolio.clone();
		SwapAssets m = (SwapAssets) mutation;
		
		double R1 = portfolio.getTickersSet().getData().getExpectedReturnsOfEachAsset()[m.Asset1];
		double R2 = portfolio.getTickersSet().getData().getExpectedReturnsOfEachAsset()[m.Asset2];
		double R3 = portfolio.getTickersSet().getData().getExpectedReturnsOfEachAsset()[m.Asset3];
		System.out.println("avant mutation : ");
		Tools.printArray(newPortfolio.getWeights());
		newPortfolio.getWeights()[m.Asset1]=newPortfolio.getWeights()[m.Asset1]-m.step;
		newPortfolio.getWeights()[m.Asset2]=newPortfolio.getWeights()[m.Asset2]+m.step*((R1-R3)/(R2-R3));
		newPortfolio.getWeights()[m.Asset3]=newPortfolio.getWeights()[m.Asset3]+m.step*((R2-R1)/(R2-R3));
		
		System.out.println("apres mutation : ");
		Tools.printArray(newPortfolio.getWeights());

		double energyPortfolio = calculer(portfolio);
		System.out.println("energyPortfolio : "+energyPortfolio);

		double energyNewPortfolio = calculer(newPortfolio);
		System.out.println("energyNewPortfolio : "+energyNewPortfolio);

		System.out.println("difference : "+(energyNewPortfolio - energyPortfolio));

		return energyNewPortfolio - energyPortfolio;
		
	}



}
