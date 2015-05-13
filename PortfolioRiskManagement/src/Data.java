import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * This class retrieves historical data from YahooFinance csv files.
 * @version 1.0
 * @since   2015-05-10
 */

public class Data {

	private TickersSet tickersSet;
	private double[] expectedReturnsOfEachAsset;
	private double[] pricesOfEachAsset;
	private double[][] returnsMatrix;
	private double[][] pricesMatrix;
	private Calendar startCalendar;
	private Calendar endCalendar;
	
	public Data(TickersSet tickersSet, Calendar startCalendar, Calendar endCalendar){
		
		this.tickersSet = tickersSet;
		this.startCalendar = startCalendar;
		this.endCalendar = endCalendar;
		
		this.pricesMatrix = this.computePricesMatrix();
		this.returnsMatrix = this.computeReturnsMatrix();
		this.expectedReturnsOfEachAsset = this.computeExpectedReturnsOfEachAsset();
		
	}

	/**
	 * This method stores the historical prices in a double[][] matrix.
	 * Columns represents differents tickers.
	 * Lines represents trading days.
	 * The resulting matrix may not include all data available, because we only include data that is relevant for creating a portfolio.
	 */
	
	private double[][] computePricesMatrix() {
		
		ArrayList<double[]> pricesArrayList = new ArrayList<double[]>();
		int n = this.tickersSet.getLength();
		for(int i=0;i<n;i++) {
			try {

				URL url  = new URL(constructURL(this.tickersSet.getTickerString(i),this.startCalendar,this.endCalendar));

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
					String[] string = stringLine.split("\\,");
					double adjClose = Double.parseDouble(string[6]);				
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

		double[][] pricesMatrix = new double[min][this.tickersSet.getLength()];

		for(int i=0; i<tickersSet.getLength(); i++){
			for(int j=0; j<min; j++){
				pricesMatrix[j][i] = pricesArrayList.get(i)[j];
			}
		}
		
		return pricesMatrix;
		
	}

	/**
	 * Get the expected returns of each assets.
	 * Important for the elementary mutation.
	 * @return the array with each expected returns.
	 */
	
	private double[] computeExpectedReturnsOfEachAsset() {

		double[] expectedReturns = new double[this.tickersSet.getLength()];
		for(int i = 0; i<this.tickersSet.getLength(); i++){
			double sum =0;
			int compt =0;
			for(int j = 0; j<this.returnsMatrix.length-1; j++){
				sum = sum + this.returnsMatrix[j][i];
				compt ++;
			}
			expectedReturns[i] = sum/compt;
			System.out.println("expectedReturnsOfEachAssets[i] = "+ expectedReturns[i]);

		}
		return expectedReturns;
		
	}
	
	/**
	 * Get the prices of each assets.
	 * @return the array with each price.
	 */
	
	private double[] getPricesOfEachAsset() {

		int n = this.tickersSet.getLength();
		double[] pricesOfEachAsset = new double[n];
		for (int i = 0; i<n;i++){
			pricesOfEachAsset[i]=this.pricesMatrix[0][i];
			//Vérifier les indices
		}
		return pricesOfEachAsset;
		
	}

	
	/**
	 * This method is used to construct the URL to retrieve data from.
	 * We want to get the maximum amount of data.
	 * Example : constructURL("AAPL") will give the URL with historical data of Apple.
	 * @param tickerSymbol This is the ticker.
	 * @return String This returns the URL (String format).
	 */

	private static String constructURL(String tickerSymbol, Calendar startCalendar, Calendar endCalendar){

		String endCalendarDay = Integer.toString(endCalendar.get(Calendar.DATE));
		String endCalendarMonth = Integer.toString(endCalendar.get(Calendar.MONTH));
		String endCalendarYear = Integer.toString(endCalendar.get(Calendar.YEAR));
		
		String startCalendarDay = Integer.toString(startCalendar.get(Calendar.DATE));
		String startCalendarMonth = Integer.toString(startCalendar.get(Calendar.MONTH));
		String startCalendarYear = Integer.toString(startCalendar.get(Calendar.YEAR));
		
		String baseURL = "http://ichart.finance.yahoo.com/table.csv?s=";
		String urlStr = (baseURL + tickerSymbol+"&d="+endCalendarMonth+"&e="+endCalendarDay+"&f="+endCalendarYear+"&g=d"+"&a="+startCalendarMonth+"&b="+startCalendarDay+"&c="+startCalendarYear);
		System.out.println("Retrieving data from : " + urlStr);
		return urlStr;

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
	 * This method stores the historical returns in a double[][] matrix.
	 * Columns represents differents tickers.
	 * Lines represents trading days.
	 * The resulting matrix may not include all data available, because we only include data that is relevant for creating a portfolio.
	 */

	private double[][] computeReturnsMatrix(){

		if(this.pricesMatrix == null){

			ArrayList<double[]> pricesArrayList = new ArrayList<double[]>();
			int n = this.tickersSet.getLength();
			for(int i=0;i<n;i++) {
				try {

					URL url  = new URL(constructURL(this.tickersSet.getTickerString(i),this.startCalendar,this.endCalendar));

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
						String[] string = stringLine.split("\\,");
						double adjClose = Double.parseDouble(string[6]);				
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

			double[][] pricesMatrix = new double[min][this.tickersSet.getLength()];

			for(int i=0; i<tickersSet.getLength(); i++){
				for(int j=0; j<min; j++){
					pricesMatrix[j][i] = pricesArrayList.get(i)[j];
				}
			}
		
			
			double[][] returnsMatrix = new double[min-1][n]; 
			for(int i=0; i<n; i++){
				double mem = pricesMatrix[0][i];
				for(int j=0; j<(min-1); j++){
					returnsMatrix[j][i] = (mem/pricesMatrix[j+1][i] - 1)*100;
					mem = pricesMatrix[j][i];
				}
			}
			
			return returnsMatrix;
		}
		
		else {
			
			
			int n = this.tickersSet.getLength();
			int min = this.pricesMatrix.length;
			System.out.println("n="+ n);
			System.out.println("min="+ min);

			double[][] returnsMatrix = new double[min-1][n]; 
			for(int i=0; i<n; i++){
				double mem = this.pricesMatrix[0][i];
				for(int j=0; j<(min-1); j++){
					returnsMatrix[j][i] = (mem/this.pricesMatrix[j+1][i] - 1)*100;
					mem = this.pricesMatrix[j][i];
				}
			}
			
			return returnsMatrix;
			
		}

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
	 * @return the expectedReturnsOfEachAsset
	 */
	public double[] getExpectedReturnsOfEachAsset() {
		return expectedReturnsOfEachAsset;
	}

	/**
	 * @param expectedReturnsOfEachAsset the expectedReturnsOfEachAsset to set
	 */
	public void setExpectedReturnsOfEachAsset(double[] expectedReturnsOfEachAsset) {
		this.expectedReturnsOfEachAsset = expectedReturnsOfEachAsset;
	}

	/**
	 * @return the returnsMatrix
	 */
	public double[][] getReturnsMatrix() {
		return returnsMatrix;
	}

	/**
	 * @param returnsMatrix the returnsMatrix to set
	 */
	public void setReturnsMatrix(double[][] returnsMatrix) {
		this.returnsMatrix = returnsMatrix;
	}

	/**
	 * @return the pricesMatrix
	 */
	public double[][] getPricesMatrix() {
		return pricesMatrix;
	}

	/**
	 * @param pricesMatrix the pricesMatrix to set
	 */
	public void setPricesMatrix(double[][] pricesMatrix) {
		this.pricesMatrix = pricesMatrix;
	}

	/**
	 * @return the startCalendar
	 */
	public Calendar getStartCalendar() {
		return startCalendar;
	}

	/**
	 * @param startCalendar the startCalendar to set
	 */
	public void setStartCalendar(Calendar startCalendar) {
		this.startCalendar = startCalendar;
	}

	/**
	 * @return the endCalendar
	 */
	public Calendar getEndCalendar() {
		return endCalendar;
	}

	/**
	 * @param endCalendar the endCalendar to set
	 */
	public void setEndCalendar(Calendar endCalendar) {
		this.endCalendar = endCalendar;
	}

	/**
	 * @param pricesOfEachAsset the pricesOfEachAsset to set
	 */
	public void setPricesOfEachAsset(double[] pricesOfEachAsset) {
		this.pricesOfEachAsset = pricesOfEachAsset;
	}

	
	
}
