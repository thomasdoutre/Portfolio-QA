import java.util.Calendar;
import java.util.GregorianCalendar;

public class Main {

	public static void main(String[] args) {

		String[] tickers = {"GSPC","AAPL","FCHI","LG.PA","GSZ.PA","KER.PA","RNO.PA","AIR.PA"};
		Calendar startCalendar = new GregorianCalendar(2014,0,27);
		Calendar endCalendar = Calendar.getInstance();

		TickersSet tickersSet = new TickersSet(tickers);
		Data data = new Data(tickersSet,startCalendar,endCalendar);
		tickersSet.setData(data);
		
		double[] weights = {0.125,0.125,0.125,0.125,0.125,0.125,0.125,0.125};
		Portfolio portfolio = new Portfolio(tickersSet, weights);
		
		
	}

}
