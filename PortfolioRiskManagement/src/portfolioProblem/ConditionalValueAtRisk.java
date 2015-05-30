package portfolioProblem;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;

import solver.commun.EnergiePotentielle;
import solver.commun.Etat;
import solver.commun.MutationElementaire;
import Optionnel.Tools;

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
		
		return calculer(portfolioReturns);
	}
	
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



	@Override
	public double calculerDeltaE(Etat etat, MutationElementaire mutation) {
		
		Portfolio portfolio = (Portfolio) etat;
		Swap m = (Swap) mutation;
		HashMap<Integer, Double> vect = m.vecteur;
	
		portfolio.getTickersSet().getData().getReturnsMatrix();
		
		double[] newPortfolioReturns = Tools.cloneArray(portfolio.getReturns());
		
		
		for(Map.Entry<Integer, Double> entry : vect.entrySet()){
			
			for(int i =0; i<newPortfolioReturns.length;i++){
			newPortfolioReturns[entry.getKey()] += entry.getValue()*(portfolio.getTickersSet().getData().getReturnsMatrix()[i][entry.getKey()]);
			//System.out.println("=="+ entry.getValue()*(portfolio.getTickersSet().getData().getReturnsMatrix()[i][entry.getKey()]));
			}
			
		}
		return (calculer(newPortfolioReturns)-calculer(portfolio.getReturns()));
		
		
		
		
		/*Portfolio portfolio = (Portfolio) etat;
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

		return energyNewPortfolio - energyPortfolio;*/
		
	}



}
