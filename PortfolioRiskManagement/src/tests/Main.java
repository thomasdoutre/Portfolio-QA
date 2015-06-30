package tests;
import java.util.Calendar;
import java.util.GregorianCalendar;

import portfolioProblem.ConditionalValueAtRisk;
import portfolioProblem.Data;
import portfolioProblem.PortfolioDistance;
import portfolioProblem.PortfolioParticule;
import portfolioProblem.SwapAssets;
import portfolioProblem.TickersSet;
import portfolioProblem.ValueAtRisk;
import solver.parametres.ConstanteKConstant;
import solver.parametres.FonctionLineaire;
import solver.quantique.RecuitQuantique;

/**
 * @author thomasdoutre
 * @version 1.0
 * @since   2015-05-10
 */

public class Main {

	public static void main(String[] args) {


		String[] tickers = {"GSPC","AAPL","FCHI","LG.PA","GSZ.PA"/*"KER.PA","RNO.PA","AIR.PA"*/};
		Calendar startCalendar = new GregorianCalendar(2010,0,27);
		Calendar endCalendar = Calendar.getInstance();

		TickersSet tickersSet = new TickersSet(tickers);
		Data data = new Data(tickersSet,startCalendar,endCalendar);
		tickersSet.setData(data);
		data.setExpectedReturnsOfEachAsset(data.computeExpectedReturnsOfEachAsset());
		int nombreTickers = tickersSet.getLength();

		for(int nbRecuits = 0; nbRecuits <20; nbRecuits++){

			ValueAtRisk Ep = new ValueAtRisk();
			PortfolioDistance Ec = new PortfolioDistance();

			SwapAssets mutation = new SwapAssets();
			mutation.initialize(nombreTickers);

			int k = 1;
			int M = 500;
			double G0 = 0.55;
			int P = 10;
			int maxSteps = 100;
			int seed = 22;
			double T = 0.35/P;

			double[] weights = new double[nombreTickers];
			double[] returns = new double[nombreTickers];

			PortfolioParticule particule = new PortfolioParticule(Ep, mutation, Ec, P, tickersSet, weights, returns);
			particule.initialiser();

			FonctionLineaire Tparam = new FonctionLineaire(0,G0,maxSteps);
			ConstanteKConstant Kparam = new ConstanteKConstant(k);
			RecuitQuantique recuit = new RecuitQuantique(Tparam,Kparam, M, T);

			long startTime = System.nanoTime();
			recuit.lancer(particule);
			long endTime = System.nanoTime();
		}
	}
}
