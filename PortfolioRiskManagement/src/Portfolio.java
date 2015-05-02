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
	private double valueAtRisk;
	
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
	 */
	
	public Portfolio(String[] tickers,double[] weights,double[] logReturns,double[] rawReturns, double valueAtRisk){
		this.tickers = tickers;
		this.weights = weights;
		this.logReturns = logReturns;
		this.rawReturns = rawReturns;
		this.valueAtRisk = valueAtRisk;
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
		this.setValueAtRisk(computeVAR(this.logReturns, 5.0));
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
		this.valueAtRisk = computeVAR(this.logReturns, 5.0);
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
		this.weights = weights;
		this.logReturns = computeLogReturns(data, weights);
		this.valueAtRisk = computeVAR(this.logReturns, 5.0);
	}
	
	/**
	 * This method is used to construct a portfolio.
	 * Portfolio is initialized with pre-defined weights.
	 * @param weights the weights
	 * @param logReturns the log returns.
	 */
	public Portfolio(double[] weights, double[] logReturns){
		this.setWeights(weights);
		this.logReturns = logReturns;
		this.valueAtRisk = computeVAR(this.logReturns, 5.0);
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

		Portfolio clone = new Portfolio(tickers, cWeights,cLogReturns,cRawReturns,cVar);
		return clone;

	}
	
	/*public Portfolio cloneB(){
		double[] clonedWeights = new double[this.getWeights().length];
		//double[] clonedrawReturns = new double[this.rawReturns.length];
		double[] clonedlogReturns = new double[this.logReturns.length];
		for(int i = 0; i < this.getWeights().length; i++){
			clonedWeights[i] = this.getWeights()[i];
		}
		for(int i = 0; i < this.getrawReturns().length; i++){
			clonedrawReturns[i] = this.getrawReturns()[i];
		}
		for(int i = 0; i < this.getLogReturns().length; i++){
			clonedlogReturns[i] = this.getLogReturns()[i];
		}
		Portfolio clone = new Portfolio(clonedWeights, clonedlogReturns);
		return clone;
	}*/
	
	
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
				System.out.println("rawReturns = "+rawReturns);
			}
			if(rawReturns == 0){
			System.out.println("rawReturns = 0 !!");
			rawReturns = 0;
			}
			
			else{
				logReturns[i] = Math.log(rawReturns);
			}
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
		double rawReturns[] = new double[data.getQuoteMatrix().length-1];
		for(int i = 0; i < data.getQuoteMatrix().length-1; i++){
			for(int j = 0; j < data.getQuoteMatrix()[0].length; j++){
				rawReturns[i] += weights[j]*data.getRawReturnsMatrix()[i][j];
			}
			rawReturns[i] = rawReturns[i];
		}
		return rawReturns;
	}
	
	/**
	 * This method is used to compute the Value at Risk of a portfolio.
	 * @param portfolioReturns are the historical returns of the portfolio.
	 * @param alpha percentile.
	 * @return double Value at Risk.
	 */
	
	public double computeVAR(double[] portfolioReturns, double alpha){
		Percentile percentile = new Percentile();
		double valueAtRisk = 0.0;
		valueAtRisk = percentile.evaluate(portfolioReturns, alpha);
		return valueAtRisk;
	}

	/**
	 * This method is used to compute the Expected Return of a portfolio.
	 * @param portfolioReturns historical returns of the portfolio
	 * @return double Expected Return from historical data.
	 */
	public double computeExpectedReturn(double[] portfolioReturns){
		Mean mean = new Mean();
		double rendement = 0.0;
		rendement = mean.evaluate(portfolioReturns);
		return rendement;
	}

	/**
	 * This method is used to store log returns and Value at Risk computations in a portfolio.
	 * @param data are the historical data.
	 */
	public void update(YahooData data){
			//this.rawReturns = computeRawReturn(data, this.weights);
			this.logReturns = computeLogReturns(data, this.weights);
			this.valueAtRisk = computeVAR(this.logReturns, 5.0);
	}
	
	/**
	 * This method is used to compute the 'Energy' for the Annealing algorithm.
	 * @param targetReturn is the return an investor wants to achieve, minimizing the risk.
	 * @return double 'Energy' to be minimized.
	 */
	public double getEnergy(double targetReturn){
		double ExpectedReturn = computeExpectedReturn(this.logReturns);
		double VAR = computeVAR(this.logReturns, 5.0);
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
				strWeights += this.logReturns[this.logReturns.length-1]+"]\nV@R = "+this.getValueAtRisk()+"\nrendement = "+this.computeExpectedReturn(this.logReturns);
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
