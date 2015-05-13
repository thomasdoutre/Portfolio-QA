
/**
 * This class decribes set of tickers
 * @version 1.0
 * @since   2015-05-10
 */

public class TickersSet {

	private String[] tickers;
	private Data data;
	
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
	 * @return the data
	 */
	public Data getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Data data) {
		this.data = data;
	}

	
	
	/**
	 * This method returns the number of tickers in the set.
	 * @return double number of tickers.
	 */
	
	public int getLength() {
		return this.tickers.length;
	}

	/**
	 * This method returns the ith ticker, in String format.
	 * @param i no of the ticker to get.
	 * @return String string associated with the ith ticker.
	 */
	public String getTickerString(int i) {
		return this.tickers[i];
	}

}
