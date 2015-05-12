import org.apache.commons.math3.stat.descriptive.rank.Percentile;


public class ValueAtRisk extends Risk {

	/**
	 * This method is used to compute the Value at Risk of a portfolio.
	 * @param alpha percentile.
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
