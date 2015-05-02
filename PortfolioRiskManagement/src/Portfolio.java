import java.text.ParseException;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;

/**
 * This class decribes a portfolio
 * A portfolio is characterized by tickers associated with weights (percentages of investment)
 * Thanks to Data collected from Yahoo.fr, we calculate its returns and log returns.
 * The Value at Risk can be estimated.
 * @author  Thomas Doutre & Pierre-Alain Belaube
 * @version 1.0
 * @since   2015-04-30
 */

public class Portfolio{

	public String[] tickers;
	private double[] weights;
	private double[] logReturns;
	private double[] rawReturns;
	private double valueAtRisk=-1;
	private double conditionalValueAtRisk;
	private double expectedReturn;

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
	 * @return the conditionalValueAtRisk
	 */
	public double getConditionalValueAtRisk() {
		return conditionalValueAtRisk;
	}

	/**
	 * @param conditionalValueAtRisk the conditionalValueAtRisk to set
	 */
	public void setConditionalValueAtRisk(double conditionalValueAtRisk) {
		this.conditionalValueAtRisk = conditionalValueAtRisk;
	}

	/**
	 * @return the tickers
	 */
	public String[] getTickers() {
		return tickers;
	}

	/**
	 * @param tickers the tickers to set
	 */
	public void setTickers(String[] tickers) {
		this.tickers = tickers;
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
	 * @return the logReturns
	 */
	public double[] getLogReturns() {
		return logReturns;
	}

	/**
	 * @param logReturns the logReturns to set
	 */
	public void setLogReturns(double[] logReturns) {
		this.logReturns = logReturns;
	}

	/**
	 * @return the rawReturns
	 */
	public double[] getRawReturns() {
		return rawReturns;
	}

	/**
	 * @param rawReturns the rawReturns to set
	 */
	public void setRawReturns(double[] rawReturns) {
		this.rawReturns = rawReturns;
	}

	/**
	 * @return the valueAtRisk
	 */
	public double getValueAtRisk() {
		return valueAtRisk;
	}

	/**
	 * @param valueAtRisk the valueAtRisk to set
	 */
	public void setValueAtRisk(double valueAtRisk) {
		this.valueAtRisk = valueAtRisk;
	}

	/**
	 * This method is used to construct a portfolio with all its features.
	 * @param tickers the tickers
	 * @param weights the weights
	 * @param logReturns the log returns
	 * @param rawReturns the raw returns
	 * @param valueAtRisk the VaR
	 * @param conditionalValueAtRisk the CVaR
	 * @param expectedReturn the expected return
	 */

	public Portfolio(String[] tickers,double[] weights,double[] logReturns,double[] rawReturns, double valueAtRisk, double conditionalValueAtRisk, double expectedReturn){
		this.tickers = tickers;
		this.weights = weights;
		this.logReturns = logReturns;
		this.rawReturns = rawReturns;
		this.valueAtRisk = valueAtRisk;
		this.conditionalValueAtRisk = conditionalValueAtRisk;
		this.expectedReturn = expectedReturn;
		
	}
	
	/**
	 * This method is used to construct a portfolio.
	 * Portfolio is initialized with a total investment on the first asset.
	 * @param data YahooData object including assets names we want to invest in.
	 */

	public Portfolio(YahooData data){
		double[] weights = new double[data.getQuoteMatrix()[0].length];

		weights[0]=1;
		for(int i = 1; i < data.getQuoteMatrix()[0].length-1; i++){
			weights[i] = 0;
		}
		this.setWeights(weights);
		this.setLogReturns(computeLogReturns(data, weights));
		this.setValueAtRisk(computeVaR(5.0));
		this.setConditionalValueAtRisk(computeConditionalValueAtRisk(5.0));
		this.setExpectedReturn(computeExpectedReturn());
	}

	/**
	 * This method is used to construct a portfolio.
	 * Portfolio is initialized with pre-definite weights.
	 * @param data YahooData object including assets names we want to invest in.
	 * @param weights the weights of the portfolio.
	 */

	public Portfolio(YahooData data, double[] weights){

		this.tickers = data.tickers;
		this.weights = weights;
		this.computeLogReturns(data, weights);
		this.rawReturns = computeRawReturns(data,weights);
		this.valueAtRisk = computeVaR(5.0);
		this.conditionalValueAtRisk = computeConditionalValueAtRisk(5.0);
		this.expectedReturn = computeExpectedReturn();
	}

	/**
	 * This method is used to construct a portfolio.
	 * Portfolio is initialized with pre-definite weights.
	 * @param tickers we want to invest in.
	 * @param weights the weights of the portfolio.
	 * @throws ParseException 
	 */

	public Portfolio(String[] tickers, double[] weights) throws ParseException{
		YahooData data = new YahooData(tickers);
		this.tickers = tickers;
		this.weights = weights;
		this.logReturns = computeLogReturns(data, weights);
		this.rawReturns = computeRawReturns(data, weights);
		this.valueAtRisk = computeVaR(5.0);
		this.conditionalValueAtRisk = computeConditionalValueAtRisk(5.0);
		this.expectedReturn = computeExpectedReturn();
	}


	/**
	 * This method is used to duplicate portfolio.
	 * @return the duplicated portfolio.
	 */

	public Portfolio clone(){
		String[] tickers = this.tickers;
		double[] cWeights = cloneArray(this.weights);
		double[] cLogReturns = cloneArray(this.logReturns);
		double[] cRawReturns = cloneArray(this.rawReturns);
		double cVar = this.valueAtRisk;
		double cCVar = this.conditionalValueAtRisk;
		double cExpectedReturn = this.expectedReturn;

		Portfolio clone = new Portfolio(tickers, cWeights,cLogReturns,cRawReturns,cVar,cCVar,cExpectedReturn);
		return clone;

	}


	/**
	 * This method is used to compute the log-returns of a portfolio.
	 * @param data from YahooFinance.
	 * @param weights percentages we want to invest in each asset.
	 * @return double[] log-returns are historically computed, the method returns an array of this log returns, it allows us tio compute VaR.
	 */

	public double[] computeLogReturns(YahooData data, double[] weights){
		double[][] rawReturnsMatrix = data.getRawReturnsMatrix();
		int n = rawReturnsMatrix.length;
		double logReturns[] = new double[n];
		double rawReturns = 0;

		for(int i = 0; i < n; i++){
			for(int j = 0; j < weights.length; j++){
				rawReturns += weights[j]*rawReturnsMatrix[i][j];
			}
			if(rawReturns == 0){
				System.out.println("rawReturns["+i+"] = 0 !!");
				rawReturns = 0;
			}
			else{
				logReturns[i] = Math.log(rawReturns);
			}
			rawReturns = 0;
		}
		return logReturns;
	}

	/**
	 * This method is used to compute the raw returns of a portfolio.
	 * @param data from YahooFinance.
	 * @param weights Percentage we want to invest in each asset.
	 * @return double[] raw returns are historically computed, the method returns an array of this raw returns, it allows us tio compute VaR.
	 */
	public double[] computeRawReturns(YahooData data, double[] weights){
		double[][] rawReturnsMatrix = data.getRawReturnsMatrix();
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
	 * This method is used to compute the Value at Risk of a portfolio.
	 * @param alpha percentile.
	 * @return double Value at Risk.
	 */

	public double computeVaR(double alpha){
		double[] portfolioRawReturns = this.getRawReturns();
		Percentile percentile = new Percentile();
		double valueAtRisk;
		valueAtRisk = - percentile.evaluate(portfolioRawReturns, 5.0);
		System.out.println("VaR = "+valueAtRisk);
		return valueAtRisk;
	}

	/**
	 * This method is used to compute the Conditional Value at Risk of a portfolio.
	 * @param alpha percentile.
	 * @return double Value at Risk.
	 */

	public double computeConditionalValueAtRisk(double alpha){
		double[] portfolioRawReturns = this.getRawReturns();
		double valueAtRisk = 0;
		if(this.valueAtRisk == -1){
			valueAtRisk = computeVaR(alpha);
		}
		else valueAtRisk = this.valueAtRisk;
		
		double sum = 0;
		int compt = 0;
		
		for(int i =0 ; i<portfolioRawReturns.length;i++){
			if(portfolioRawReturns[i]<(-valueAtRisk)){
				sum = sum + portfolioRawReturns[i];
				compt++;
			}
		}
		double conditionalVaR = - sum/compt;
		System.out.println("Conditional VaR = " +conditionalVaR);
		return conditionalVaR;
	}

	/**
	 * This method is used to compute the Expected Return of a portfolio.
	 * @return double Expected Return from historical data.
	 */
	public double computeExpectedReturn(){
		Mean mean = new Mean();
		double expectedReturn;
		expectedReturn = mean.evaluate(this.rawReturns);
		System.out.println("Expected Return = " +expectedReturn);
		return expectedReturn;
	}

	/**
	 * This method is used to store log returns and Value at Risk computations in a portfolio.
	 * @param data are the historical data.
	 */
	public void update(YahooData data){
		//this.rawReturns = computeRawReturn(data, this.weights);
		this.logReturns = computeLogReturns(data, this.weights);
		this.valueAtRisk = computeVaR(5.0);
	}

	/**
	 * This method is used to compute the 'Energy' for the Annealing algorithm.
	 * @param targetReturn is the return an investor wants to achieve, minimizing the risk.
	 * @return double 'Energy' to be minimized.
	 */
	public double getEnergy(double targetReturn){
		double ExpectedReturn = computeExpectedReturn();
		double VAR = computeVaR(5.0);
		double energy = 100*Math.abs(targetReturn-ExpectedReturn)-VAR;
		return energy;
	}

	/*
	 *//**
	 * This method is used to store the Raw Returns in the portfolio object.
	 * @param data the historical data
	 * @param weights percentages of investment.
	 *//*
	public void setrawReturns(YahooData data, double[] weights){
		this.rawReturns = computeRawReturn(data, weights);
	}

	  *//**
	  * This method is used to store the Log Returns in the portfolio object.
	  * @param data the historical data
	  * @param weights percentages of investment.
	  *//*
	public void setlogReturns(YahooData data, double[] weights){
		this.logReturns = computeLogReturn(data, weights);
	}

	   *//**
	   * This method is used to store the VaR in the portfolio object.
	   * @param portfolioReturn historical returns.
	   *//*
	public void setValueAtRisk(double[] portfolioReturn){
		Percentile percentile = new Percentile();
		this.valueAtRisk = percentile.evaluate(portfolioReturn, 5.0);
	}*/

	/**
	 * This method displays portfolios features on the console.
	 */	
	public String toString(){
		String strWeights = "poids : [";
		for(int i=0; i < this.weights.length; i++){
			if(i != this.weights.length-1){
				strWeights += this.weights[i]+"|";
			} else{
				strWeights += this.weights[this.weights.length-1]+"]\nretours logarithmiques : [";
			}
		}
		for(int i=0; i < this.logReturns.length; i++){
			if(i != this.logReturns.length-1){
				strWeights += this.logReturns[i]+"|";
			} else{
				strWeights += this.logReturns[this.logReturns.length-1]+"]\nV@R = "+this.getValueAtRisk()+"\nrendement = "+this.computeExpectedReturn();
			}
		}
		return strWeights;
	}

	/**
	 * This method is used to clone an array of double.
	 * @param array the array to be cloned
	 * @return the cloned array.
	 */
	public double[] cloneArray(double[] array){
		double[] cloned = new double[array.length];
		for(int i=0; i<array.length;i++){
			cloned[i]=array[i];
		}
		return cloned;
	}
}
