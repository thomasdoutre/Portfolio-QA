package portfolioProblem;
import java.text.ParseException;
import java.util.Calendar;
import org.apache.commons.math3.stat.descriptive.moment.Mean;

import Optionnel.Risk;
import Optionnel.Tools;
import solver.commun.Etat;

/**
 * This class decribes a portfolio
 * @version 1.0
 * @since   2015-04-30
 */

public class Portfolio extends Etat {
	
	private TickersSet tickersSet;
	private double[] weights;
	private double[] returns;
	private double expectedReturn;
	
	/**
	 * This method is used to construct a portfolio with all its features.
	 * Useful for cloning
	 * @param tickers the tickers
	 * @param weights the weights
	 * @param logReturns the log returns
	 * @param rawReturns the raw returns
	 * @param valueAtRisk the VaR
	 * @param conditionalValueAtRisk the CVaR
	 * @param expectedReturn the expected return
	 */
	// ATTENTION : ici on ne clone pas, les références aux objets type double[] sont les mêmes, on clonera
	// ces pointeurs avant de faire appel à ce constructeur.
	
	public Portfolio(TickersSet tickers,double[] weights,double[] returns){
		this.setTickersSet(tickers);
		this.setWeights(weights);
		this.setReturns(returns);
		this.expectedReturn = computeExpectedReturn();
		
	}
	
	/**
	 * This method is used to construct a portfolio.
	 * Portfolio is initialized with pre-definite weights.
	 * @param tickers we want to invest in.
	 * @param weights the weights of the portfolio.
	 * @throws ParseException 
	 */

	public Portfolio(TickersSet tickers, double[] weights) {
		this.tickersSet = tickers;
		this.weights = weights;
		this.returns = computeReturns(weights);
		this.expectedReturn = computeExpectedReturn();
	}
	
	public Portfolio clone(){
		double[] cWeights = Tools.cloneArray(this.weights);
		Portfolio newPortfolio = new Portfolio(this.tickersSet,cWeights);
		return newPortfolio;
		
	}
	public Portfolio cloneWithDifferentWeights(double[] weights){
		Portfolio newPortfolio = new Portfolio(this.tickersSet,weights);
		return newPortfolio;
		
	}
	

	/**
	 * This method is used to compute the Expected Return of a portfolio.
	 * @return double Expected Return from historical data.
	 */
	public double computeExpectedReturn(){
		Mean mean = new Mean();
		double expectedReturn;
		expectedReturn = mean.evaluate(this.returns);
		System.out.println("Expected Return = " +expectedReturn);
		return expectedReturn;
	}

	/**
	 * This method is used to compute the raw returns of a portfolio.
	 * @param data from YahooFinance.
	 * @param weights Percentage we want to invest in each asset.
	 * @return double[] raw returns are historically computed, the method returns an array of this raw returns, it allows us tio compute VaR.
	 */
	private double[] computeReturns(double[] weights){
		double[][] rawReturnsMatrix = this.tickersSet.getData().getReturnsMatrix();
		int n = rawReturnsMatrix.length;
		double rawReturns[] = new double[n];
		for(int i = 0; i < n; i++){
			for(int j = 0; j < weights.length; j++){
				rawReturns[i] += weights[j]*rawReturnsMatrix[i][j];
			}
		}
		this.returns = rawReturns;
		return rawReturns;
	}
	
	
	/**
	 * This method is used to initialize a portfolio.
	 */
	public void initialiser(){
		
		int n = this.getTickersSet().getLength();
		double[] weights = new double[n];
		
		int low = 0;
		int up = 0;
		double lowReturn = this.getTickersSet().getData().getExpectedReturnsOfEachAsset()[0];
		double upReturn = lowReturn;
		
		for(int i=0; i<n; i++){
			weights[i]=0;

			if(this.getTickersSet().getData().getExpectedReturnsOfEachAsset()[i]<=this.expectedReturn){
				low = i;
				lowReturn = this.getTickersSet().getData().getExpectedReturnsOfEachAsset()[i];
			}
			else {
				up = i;
				upReturn = this.getTickersSet().getData().getExpectedReturnsOfEachAsset()[i];
			}
		}
		
		weights[low] = (upReturn - this.expectedReturn)/(upReturn-lowReturn);
		weights[up] = (this.expectedReturn-lowReturn)/(upReturn-lowReturn);
		
		
	}
	
	/**
	 * This method is used to save a solution at each iteration.
	 */
	public void sauvegarderSolution(){
		//ZEBCEIUYJC
	}

	/**
	 * @return the expectedReturn
	 */
	public double getExpectedReturn() {
		return expectedReturn;
	}

	/**
	 * @param expectedReturn the expectedReturn to set
	 */
	public void setExpectedReturn(double expectedReturn) {
		this.expectedReturn = expectedReturn;
	}

	/**
	 * @param returns the returns to set
	 */
	public void setReturns(double[] returns) {
		this.returns = returns;
	}

	/**
	 * @return the tickersSet
	 */
	public TickersSet getTickersSet() {
		return tickersSet;
	}

	/**
	 * @param tickersSet the tickersSet to set
	 */
	public void setTickersSet(TickersSet tickersSet) {
		this.tickersSet = tickersSet;
	}

	/**
	 * @return the weights
	 */
	public double[] getWeights() {
		return weights;
	}

	/**
	 * @param weights the weights to set
	 */
	public void setWeights(double[] weights) {
		this.weights = weights;
	}

	/**
	 * @return the returns
	 */
	public double[] getReturns() {
		return returns;
	}
}
