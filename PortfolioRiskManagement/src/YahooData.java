import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * This class retrieves historical prices and returns from yahoo.fr
 * Data is available in different formats : prices, returns and log returns.
 * A YahooData object is used to store data before creating portfolios.
 * @author  Thomas Doutre
 * @version 1.0
 * @since   2015-04-26
 */


public class YahooData {

	public String[] tickers;
	public double[][] quoteMatrix;
	public double[][] RawReturnsMatrix;
	public double[][] logReturnsMatrix;

	public YahooData(String[] symbols){
		this.tickers = symbols;
		getPricesFromYahoo(symbols);
		getReturnsFromYahoo(symbols);
		getlogReturnsMatrixFromYahoo(symbols);
	}

	/**
	 * @return the logReturnsMatrix
	 */
	public double[][] getLogReturnsMatrix() {
		return logReturnsMatrix;
	}

	/**
	 * @param logReturnsMatrix the logReturnsMatrix to set
	 */
	public void setLogReturnsMatrix(double[][] logReturnsMatrix) {
		this.logReturnsMatrix = logReturnsMatrix;
	}

	/**
	 * @return the rawReturnsMatrix
	 */
	public double[][] getRawReturnsMatrix() {
		return RawReturnsMatrix;
	}

	/**
	 * @param rawReturnsMatrix the rawReturnsMatrix to set
	 */
	public void setRawReturnsMatrix(double[][] rawReturnsMatrix) {
		RawReturnsMatrix = rawReturnsMatrix;
	}

	/**
	 * @return the quoteMatrix
	 */
	public double[][] getQuoteMatrix() {
		return quoteMatrix;
	}


	/**
	 * @param quoteMatrix the quoteMatrix to set
	 */
	public void setQuoteMatrix(double[][] quoteMatrix) {
		this.quoteMatrix = quoteMatrix;
	}



	/**
	 * This method is used to display a matrix of double is the console.
	 * @param twoDm This is the matrix to be displayed
	 */

	private static void printMatrix(double[][] twoDm) {
		System.out.println("=================================================================");
		for(double[] row : twoDm) {
			for (double i : row) {
				System.out.print(i);
				System.out.print("\t");
			}
			System.out.println();
		}
		System.out.println("=================================================================");

	}

	/**
	 * This method is used to construct the URL to retrieve data from.
	 * We want to get the maximum amount of data.
	 * Example : constructURL("AAPL") will give the URL with historical data of Apple.
	 * @param tickerSymbol This is the ticker.
	 * @return String This returns the URL (String format).
	 */

	private static String constructURL(String tickerSymbol){

		Calendar endDate = Calendar.getInstance();
		endDate.setTime(new Date());

		String endDateDay = Integer.toString(endDate.get(Calendar.DATE));
		String endDateMonth = Integer.toString(endDate.get(Calendar.MONTH));
		String endDateYear = Integer.toString(endDate.get(Calendar.YEAR));
		String baseURL = "http://ichart.finance.yahoo.com/table.csv?s=";
		String urlStr = (baseURL + tickerSymbol+"&d="+endDateMonth+"&e="+endDateDay+"&f="+endDateYear+"&g=d"+"&a="+0+"&b="+1+"&c="+1800);
		System.out.println("Retrieving data from : " + urlStr);
		return urlStr;

	}

	/**
	 * This method is used to get the minimum length of all the double[] in a ArrayList<double[]>
	 * in order not to construct portfolios with unavailable data.
	 * @param arrayList This is the arrayList.
	 * @return int This returns the minimum length.
	 */

	private static int minimumTimeInterval(ArrayList<double[]> arrayList){

		int n = arrayList.size();
		System.out.println("n="+n);
		int min = arrayList.get(0).length;
		for(int i=0; i<n; i++){
			if (arrayList.get(i).length<min){
				min = arrayList.get(i).length;
			}
		}
		return min;
	}

	/**
	 * This method returns the number of lines in a text file from an URL.
	 * This method is important to deal with double[][] instead of custom objects.
	 * @param url This is the URL.
	 * @return int This is the number of lines in the text file.
	 */

	private static int getLinesNumber(URL url) throws IOException{
		URLConnection urlConn = url.openConnection();
		InputStreamReader inStream = new InputStreamReader(urlConn.getInputStream());
		BufferedReader buffCountLines = new BufferedReader(inStream);
		int lines = 0;

		while (buffCountLines.readLine() != null) lines++;
		buffCountLines.close();
		inStream.close();
		return lines;
	}

	/**
	 * This method stores the historical prices in a double[][] matrix.
	 * Columns represents differents tickers.
	 * Lines represents trading days.
	 * The resulting matrix may not include all data available, because we only include data that is relevant for creating a portfolio.
	 * @param symbols This is an array of String, with all the tickers. Historical prices of these tickers will be in the resulting matrix.
	 */

	private void getPricesFromYahoo(String[] symbols){

		ArrayList<double[]> pricesArrayList = new ArrayList<double[]>();
		int n = symbols.length;
		for(int i=0;i<n;i++) {
			try {

				URL url  = new URL(constructURL(symbols[i]));

				int lines = getLinesNumber(url);
				System.out.println("lines = "+lines);

				URLConnection urlConn = url.openConnection();
				InputStreamReader inStream = new InputStreamReader(urlConn.getInputStream());
				BufferedReader buff= new BufferedReader(inStream);
				String stringLine;
				buff.readLine();

				int j = 0;
				double[] prices = new double[lines-1];

				while((stringLine = buff.readLine()) != null)
				{
					String[] dohlcav = stringLine.split("\\,"); //date, ohlc, adjustedclose
					double adjClose = Double.parseDouble(dohlcav[6]);				
					prices[j] = adjClose;
					j++;
				}

				pricesArrayList.add(prices);


			}catch (MalformedURLException e) {
				System.out.println(e.getMessage());
			}catch(IOException e){
				System.out.println(e.getMessage());
			}


		}
		int min = minimumTimeInterval(pricesArrayList);
		System.out.println("minimumTimeInterval(pricesArrayList) = "+min);

		double[][] pricesMatrix = new double[min][symbols.length];

		for(int i=0; i<symbols.length; i++){
			for(int j=0; j<min; j++){
				pricesMatrix[j][i] = pricesArrayList.get(i)[j];
			}
		}
		this.quoteMatrix = pricesMatrix;

	}

	/**
	 * This method stores the historical returns in a double[][] matrix.
	 * Columns represents differents tickers.
	 * Lines represents trading days.
	 * The resulting matrix may not include all data available, because we only include data that is relevant for creating a portfolio.
	 * @param symbols This is an array of String, with all the tickers. Historical returns of these tickers will be in the resulting matrix.
	 */

	private void getReturnsFromYahoo(String[] symbols){
		if(this.quoteMatrix == null){
			this.getPricesFromYahoo(symbols);
		}
		int n = symbols.length;
		int m = this.quoteMatrix.length;
		double[][] returnsMatrix = new double[m-1][n]; 
		for(int i=0; i<n; i++){
			double mem = this.quoteMatrix[0][i];
			for(int j=0; j<(m-1); j++){
				returnsMatrix[j][i] = mem/this.quoteMatrix[j+1][i];
				mem = this.quoteMatrix[j][i];
			}
		}
		this.RawReturnsMatrix = returnsMatrix;
	}

	/**
	 * This method stores the historical log returns in a double[][] matrix.
	 * Columns represents differents tickers.
	 * Lines represents trading days.
	 * The resulting matrix may not include all data available, because we only include data that is relevant for creating a portfolio.
	 * @param symbols This is an array of String, with all the tickers. Historical log returns of these tickers will be in the resulting matrix.
	 */

	private void getlogReturnsMatrixFromYahoo(String[] symbols){
		if(this.quoteMatrix == null){
			this.getPricesFromYahoo(symbols);
		}
		int n = symbols.length;
		int m = this.quoteMatrix.length;
		double[][] logReturnsMatrix = new double[m-1][n]; 
		for(int i=0; i<n; i++){
			double mem = this.quoteMatrix[0][i];
			for(int j=0; j<(m-1); j++){
				logReturnsMatrix[j][i] = Math.log10(mem/this.quoteMatrix[j+1][i]);
				mem = this.quoteMatrix[j][i];
			}
		}
		this.logReturnsMatrix = logReturnsMatrix;
	}
}
