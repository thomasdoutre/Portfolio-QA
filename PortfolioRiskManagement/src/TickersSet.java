
/**
 * This class decribes set of tickers
 * @author  Thomas Doutre
 * @version 1.0
 * @since   2015-05-10
 */

public class TickersSet {

	String[] tickers;
	
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
