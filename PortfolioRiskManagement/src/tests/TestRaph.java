package tests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import Optionnel.Tools;
import portfolioProblem.ConditionalValueAtRisk;
import portfolioProblem.Data;
import portfolioProblem.PortfolioDistance;
import portfolioProblem.PortfolioParticule;
import portfolioProblem.SwapAssets;
import portfolioProblem.TickersSet;
import solver.commun.Etat;
import solver.parametres.ConstanteKConstant;
import solver.parametres.FonctionLineaire;
import solver.quantique.RecuitQuantique;
import solver.quantique.RecuitQuantiqueAccelere;

public class TestRaph {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Initialisation des Data
		
		
		String[] tickers = {"GSPC","AAPL","FCHI","LG.PA","GSZ.PA",/*"KER.PA","RNO.PA","AIR.PA"*/};
		Calendar startCalendar = new GregorianCalendar(2010,0,27);
		Calendar endCalendar = Calendar.getInstance();

		//On récupère les données et on fait les liens avec l'ensemble d'actifs.
		TickersSet tickersSet = new TickersSet(tickers);
		Data data = new Data(tickersSet,startCalendar,endCalendar);
		tickersSet.setData(data);
		data.setExpectedReturnsOfEachAsset(data.computeExpectedReturnsOfEachAsset());
		int nombreTickers = tickersSet.getLength();
		
		// Initialisation
		
		ConditionalValueAtRisk Ep = new ConditionalValueAtRisk();
		PortfolioDistance Ec = new PortfolioDistance();
		
		SwapAssets mutation = new SwapAssets();
		mutation.initialize(nombreTickers);

		int k = 1;
		int M = 50;
		double G0 = 0.55;
		int P = 10;
		int maxSteps = (int) Math.pow(10,2);
		int seed = 22;
		double T = 0.35/P;
		
		
		// construire liste d'etats
			
		double[] weights = new double[nombreTickers];
		
	/*	double sum=0;
		for(int i=0; i < nombreTickers;i++){
			weights[i]=1/nombreTickers;
		}*/
		double[] returns = new double[nombreTickers];
		
		PortfolioParticule particule = new PortfolioParticule(Ep, mutation, Ec, P, tickersSet, weights, returns);
		particule.initialiser();
		
		FonctionLineaire Tparam = new FonctionLineaire(G0,0,maxSteps);
		ConstanteKConstant Kparam = new ConstanteKConstant(k);
		RecuitQuantique recuit = new RecuitQuantique(Tparam,Kparam, M, T);
		
		long startTime = System.nanoTime();
		recuit.lancer(particule);
		long endTime = System.nanoTime();
		
	
	}

}
