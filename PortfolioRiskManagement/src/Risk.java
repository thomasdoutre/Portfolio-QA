
public abstract class Risk {

	/**
	 * This class is an abstract gathering of different risk metrics
	 * @version 1.0
	 * @since   2015-05-10
	 */
	private Portfolio portfolio;
	public abstract double computeRisk();
	
	/**
	 * @return the portfolio
	 */
	public Portfolio getPortfolio() {
		return portfolio;
	}
	/**
	 * @param portfolio the portfolio to set
	 */
	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

}
