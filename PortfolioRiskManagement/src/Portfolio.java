import java.text.ParseException;
import java.util.Calendar;

import org.apache.commons.math3.stat.descriptive.moment.Mean;


/**
 * This class decribes a portfolio
 * @version 1.0
 * @since   2015-04-30
 */

public class Portfolio {
	
	private TickersSet tickersSet;
	private double[] weights;
	private double[] returns;
	private Risk risk;
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
	
	public Portfolio(TickersSet tickers,double[] weights,double[] returns, Risk risk, double expectedReturn){
		this.setTickersSet(tickers);
		this.setWeights(weights);
		this.setReturns(returns);
		this.setRisk(risk);
		this.setExpectedReturn(expectedReturn);
		
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
		this.risk = new ConditionalValueAtRisk(this);
		this.expectedReturn = computeExpectedReturn();
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
		return rawReturns;
	}

	/**
	 * @return the risk
	 */
	public Risk getRisk() {
		return risk;
	}

	/**
	 * @param risk the risk to set
	 */
	public void setRisk(Risk risk) {
		this.risk = risk;
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
