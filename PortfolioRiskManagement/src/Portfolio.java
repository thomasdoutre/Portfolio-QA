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
	private double[] portfolioLogReturn;
	private double[] portfolioRawReturn;
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
	 * @return the portfolioLogReturn
	 */
	public double[] getPortfolioLogReturn() {
		return portfolioLogReturn;
	}

	/**
	 * @param portfolioLogReturn the portfolioLogReturn to set
	 */
	public void setPortfolioLogReturn(double[] portfolioLogReturn) {
		this.portfolioLogReturn = portfolioLogReturn;
	}

	/**
	 * @return the portfolioRawReturn
	 */
	public double[] getPortfolioRawReturn() {
		return portfolioRawReturn;
	}

	/**
	 * @param portfolioRawReturn the portfolioRawReturn to set
	 */
	public void setPortfolioRawReturn(double[] portfolioRawReturn) {
		this.portfolioRawReturn = portfolioRawReturn;
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
		this.weights = weights;

		this.portfolioLogReturn = computeLogReturn(data, weights);
		this.valueAtRisk = computeVAR(this.portfolioLogReturn, 5.0);
	}
	
	/**
	 * This method is used to construct a portfolio.
	 * Portfolio is initialized with pre-defined weights.
	 * @param weights the weights
	 * @param portfolioLogReturn the log returns.
	 */
	public Portfolio(double[] weights, double[] portfolioLogReturn){
		this.setWeights(weights);
		//this.portfolioRawReturn = portfolioRawReturn;
		this.portfolioLogReturn = portfolioLogReturn;
		this.valueAtRisk = computeVAR(this.portfolioLogReturn, 5.0);
	}
	
	/**
	 * This method is used to duplicate portfolio.
	 * @return the duplicated portfolio.
	 */

	public Portfolio clone(){
		double[] clonedWeights = new double[this.getWeights().length];
		//double[] clonedPortfolioRawReturn = new double[this.portfolioRawReturn.length];
		double[] clonedPortfolioLogReturn = new double[this.portfolioLogReturn.length];
		for(int i = 0; i < this.getWeights().length; i++){
			clonedWeights[i] = this.getWeights()[i];
		}
		/*for(int i = 0; i < this.getPortfolioRawReturn().length; i++){
			clonedPortfolioRawReturn[i] = this.getPortfolioRawReturn()[i];
		}*/
		for(int i = 0; i < this.getPortfolioLogReturn().length; i++){
			clonedPortfolioLogReturn[i] = this.getPortfolioLogReturn()[i];
		}
		Portfolio clone = new Portfolio(clonedWeights, clonedPortfolioLogReturn);
		return clone;
	}
	
	
	/**
	 * This method is used to compute the log-returns of a portfolio.
	 * @param data from YahooFinance.
	 * @param weights percentages we want to invest in each asset.
	 * @return double[] log-returns are historically computed, the method returns an array of this log returns, it allows us tio compute VaR.
	 */

	public double[] computeLogReturn(YahooData data, double[] weights){
		double[][] rawReturnsMatrix = data.getRawReturnsMatrix();
		int n = rawReturnsMatrix.length;
		double portfolioLogReturn[] = new double[n];
		double portfolioRawReturn = 0;
		
		for(int i = 0; i < n; i++){
			for(int j = 0; j < weights.length; j++){
				portfolioRawReturn += weights[j]*rawReturnsMatrix[i][j];
				System.out.println("portfolioRawReturn = "+portfolioRawReturn);
			}
			if(portfolioRawReturn == 0){
			System.out.println("portfolioRawReturn = 0 !!");
			portfolioRawReturn = 0;
			}
			
			else{
				portfolioLogReturn[i] = Math.log(portfolioRawReturn);
			}
		}
		return portfolioLogReturn;
	}
	
	/**
	 * This method is used to compute the raw returns of a portfolio.
	 * @param data from YahooFinance.
	 * @param weights Percentage we want to invest in each asset.
	 * @return double[] raw returns are historically computed, the method returns an array of this raw returns, it allows us tio compute VaR.
	 */
	public double[] computeRawReturn(YahooData data, double[] weights){
		double portfolioRawReturn[] = new double[data.getQuoteMatrix().length-1];
		for(int i = 0; i < data.getQuoteMatrix().length-1; i++){
			for(int j = 0; j < data.getQuoteMatrix()[0].length; j++){
				portfolioRawReturn[i] += weights[j]*data.getRawReturnsMatrix()[i][j];
			}
			portfolioRawReturn[i] = portfolioRawReturn[i];
		}
		return portfolioRawReturn;
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
			//this.portfolioRawReturn = computeRawReturn(data, this.weights);
			this.portfolioLogReturn = computeLogReturn(data, this.weights);
			this.valueAtRisk = computeVAR(this.portfolioLogReturn, 5.0);
	}
	
	/**
	 * This method is used to compute the 'Energy' for the Annealing algorithm.
	 * @param targetReturn is the return an investor wants to achieve, minimizing the risk.
	 * @return double 'Energy' to be minimized.
	 */
	public double getEnergy(double targetReturn){
		double ExpectedReturn = computeExpectedReturn(this.portfolioLogReturn);
		double VAR = computeVAR(this.portfolioLogReturn, 5.0);
		double energy = 100*Math.abs(targetReturn-ExpectedReturn)-VAR;
		return energy;
	}
	

	/**
	 * This method is used to store the Raw Returns in the portfolio object.
	 * @param data the historical data
	 * @param weights percentages of investment.
	 */
	public void setPortfolioRawReturn(YahooData data, double[] weights){
		this.portfolioRawReturn = computeRawReturn(data, weights);
	}
	
	/**
	 * This method is used to store the Log Returns in the portfolio object.
	 * @param data the historical data
	 * @param weights percentages of investment.
	 */
	public void setPortfolioLogReturn(YahooData data, double[] weights){
		this.portfolioLogReturn = computeLogReturn(data, weights);
	}
	
	/**
	 * This method is used to store the VaR in the portfolio object.
	 * @param portfolioReturn historical returns.
	 */
	public void setValueAtRisk(double[] portfolioReturn){
		Percentile percentile = new Percentile();
		this.valueAtRisk = percentile.evaluate(portfolioReturn, 5.0);
	}
	
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
		for(int i=0; i < this.portfolioLogReturn.length; i++){
			if(i != this.portfolioLogReturn.length-1){
			strWeights += this.portfolioLogReturn[i]+"|";
			} else{
				strWeights += this.portfolioLogReturn[this.portfolioLogReturn.length-1]+"]\nV@R = "+this.getValueAtRisk()+"\nrendement = "+this.computeExpectedReturn(this.portfolioLogReturn);
			}
		}
		return strWeights;
	}
}
