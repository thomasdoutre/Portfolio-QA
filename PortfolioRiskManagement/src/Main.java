import java.util.Calendar;
import java.util.GregorianCalendar;

public class Main {

	public static void main(String[] args) {
		
		// EXEMPLE DE FORMATION ET MUTATION D'UN PORTEFEUILLE
		
		/* On indique les actifs dans lesquels on veut investir et les dates pour la simulation historique */
		String[] tickers = {"GSPC","AAPL","FCHI","LG.PA","GSZ.PA","KER.PA","RNO.PA","AIR.PA"};
		Calendar startCalendar = new GregorianCalendar(2014,0,27);
		Calendar endCalendar = Calendar.getInstance();

		//On récupère les données et on fait les liens avec l'ensemble d'actifs.
		TickersSet tickersSet = new TickersSet(tickers);
		Data data = new Data(tickersSet,startCalendar,endCalendar);
		tickersSet.setData(data);
		
		//Création d'un portefeuille avec poids initiaux
		double[] weights = {0.125,0.125,0.125,0.125,0.125,0.125,0.125,0.125};
		Portfolio portfolio = new Portfolio(tickersSet, weights);

		//Pour la mutation
		double[] weightsClone = {0,0,0,0,0.25,0.25,0.25,0.25};
		Portfolio portfolioClone = portfolio.cloneWithDifferentWeights(weightsClone);
	

		
		
	}

}
