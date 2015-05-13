import org.apache.commons.math3.stat.descriptive.rank.Percentile;

/**
 * This class calculates portfolios' VaR
 * @version 1.0
 * @since   2015-05-10
 */

public class ValueAtRisk extends Risk {

	/**
	 * This method is used to compute the Value at Risk of a portfolio.
	 * @return double Value at Risk.
	 */

	public double computeRisk(){
		double[] portfolioReturns = super.portfolio.getReturns();
		Percentile percentile = new Percentile();
		double valueAtRisk;
		valueAtRisk = - percentile.evaluate(portfolioReturns, 5.0);
		System.out.println("VaR = "+valueAtRisk);
		return valueAtRisk;
	}

}
