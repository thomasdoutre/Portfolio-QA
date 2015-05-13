import org.apache.commons.math3.stat.descriptive.rank.Percentile;

/**
 * This class calculates portfolios' VaR
 * @version 1.0
 * @since   2015-05-10
 */

public class ValueAtRisk extends Risk {
	
	private double vaR;
	
	public ValueAtRisk() {
		super();
		this.vaR = this.computeRisk();
	}

	/**
	 * This method is used to compute the Value at Risk of a portfolio.
	 * @return double Value at Risk.
	 */

	public double computeRisk(){
		double[] portfolioReturns = super.getPortfolio().getReturns();
		Percentile percentile = new Percentile();
		double valueAtRisk;
		valueAtRisk = - percentile.evaluate(portfolioReturns, 5.0);
		System.out.println("VaR = "+valueAtRisk);
		return valueAtRisk;
	}

}
