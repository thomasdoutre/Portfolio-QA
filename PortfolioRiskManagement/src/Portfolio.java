
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
