package portfolioProblem;
import java.text.ParseException;

import org.apache.commons.math3.stat.descriptive.moment.Mean;

import solver.commun.EnergiePotentielle;
import solver.commun.Etat;
import Optionnel.Tools;

/**
 * This class decribes a portfolio
 * @version 1.0
 * @since   2015-04-30
 */

public class Portfolio extends Etat {
	
	private TickersSet tickersSet;
	private double[] weights;
	private double[] returns;
	private double expectedReturn;
	private double[] meilleursPoids;

	
	/**
	 * This method is used to construct a portfolio with all its features.
	 * Useful for cloning
	 * @param tickers the tickers
	 * @param weights the weights
	 * @param logReturns the log returns
	 * @param rawReturns the raw returns
	 * @param valueAtRisk the VaR
	 * @param conditionalValueAtRisk the CVaR
	 * @param expectedReturn the expected return
	 */
	// ATTENTION : ici on ne clone pas, les références aux objets type double[] sont les mêmes, on clonera
	// ces pointeurs avant de faire appel à ce constructeur.
	
	public Portfolio(EnergiePotentielle Ep, TickersSet tickers,double[] weights,double[] returns){
		this.Ep=Ep;
		this.setTickersSet(tickers);
		this.setWeights(weights);
		this.setReturns(this.computeReturns(weights));
		this.expectedReturn = computeExpectedReturn();
		
	}
	
	/**
	 * This method is used to construct a portfolio.
	 * Portfolio is initialized with pre-definite weights.
	 * @param tickers we want to invest in.
	 * @param weights the weights of the portfolio.
	 * @throws ParseException 
	 */

	public Portfolio(TickersSet tickers, double[] weights) {
		this.tickersSet = tickers;
		this.weights = weights;
		this.returns = computeReturns(weights);
		this.expectedReturn = computeExpectedReturn();
	}
	
	public Portfolio clone(){
		double[] cWeights = Tools.cloneArray(this.weights);
		Portfolio newPortfolio = new Portfolio(this.tickersSet,cWeights);
		return newPortfolio;
		
	}
	public Portfolio cloneWithDifferentWeights(double[] weights){
		Portfolio newPortfolio = new Portfolio(this.tickersSet,weights);
		return newPortfolio;
		
	}
	

	/**
	 * This method is used to compute the Expected Return of a portfolio.
	 * @return double Expected Return from historical data.
	 */
	public double computeExpectedReturn(){
		Mean mean = new Mean();
		double expectedReturn;
		expectedReturn = mean.evaluate(this.returns);
		
		//Tools.printArray(this.returns);
		
		
	//	System.out.println("Expected Return = " +expectedReturn);
		return expectedReturn;
	}

	/**
	 * This method is used to compute the raw returns of a portfolio.
	 * @param data from YahooFinance.
	 * @param weights Percentage we want to invest in each asset.
	 * @return double[] raw returns are historically computed, the method returns an array of this raw returns, it allows us tio compute VaR.
	 */
	private double[] computeReturns(double[] weights){
		double[][] rawReturnsMatrix = this.tickersSet.getData().getReturnsMatrix();
		int n = rawReturnsMatrix.length;
		double rawReturns[] = new double[n];
		for(int i = 0; i < n; i++){
			for(int j = 0; j < weights.length; j++){
				rawReturns[i] += weights[j]*rawReturnsMatrix[i][j];
			}
		}
		this.returns = rawReturns;
		return rawReturns;
	}
	
	public double[] updateReturns(){
		double[][] rawReturnsMatrix = this.tickersSet.getData().getReturnsMatrix();
		int n = rawReturnsMatrix.length;
		double rawReturns[] = new double[n];
		for(int i = 0; i < n; i++){
			for(int j = 0; j < weights.length; j++){
				rawReturns[i] += weights[j]*rawReturnsMatrix[i][j];
			}
		}
		this.returns = rawReturns;
		return rawReturns;
	}
	
	
	/**
	 * This method is used to initialize a portfolio.
	 */
	
	// ATTENTION :
	// ici le retour fixé R barre n'est pas fixé par l'utilisateur explicitement, il est déduit des poids mis sur chaque ticker.
	
	public void initialiserB(){
		double R = 0.08;
		
		int n = this.getTickersSet().getLength();
		double[] weights = new double[n];
		
		int low = 0;
		int up = 0;
		double lowReturn = this.getTickersSet().getData().getExpectedReturnsOfEachAsset()[0];
		double upReturn = lowReturn;
		
		for(int i=0; i<n; i++){
			weights[i]=0;

			if(this.getTickersSet().getData().getExpectedReturnsOfEachAsset()[i]<=R){
				low = i;
				lowReturn = this.getTickersSet().getData().getExpectedReturnsOfEachAsset()[i];
				/*System.out.println("low = "+low);
				System.out.println("lowReturn = "+lowReturn);*/

			}
			else {
				up = i;
				upReturn = this.getTickersSet().getData().getExpectedReturnsOfEachAsset()[i];
				/*System.out.println("up = "+up);
				System.out.println("upReturn = "+upReturn);*/
			}
		}
		
		if (low == up){
			System.out.println("WARNING : Expected return level may be too extreme");
		}
		weights[low] = (upReturn - R)/(upReturn-lowReturn);
		weights[up] = (R-lowReturn)/(upReturn-lowReturn);
		
		
		this.weights = weights;
		/*System.out.println("weights of this state :");
		Tools.printArray(weights);*/
		this.returns = computeReturns(weights);
		this.setExpectedReturn(this.computeExpectedReturn());
		
	}
	

	public void initialiser(){
		
		double R = 0.06;
		
		int n = this.getTickersSet().getLength();
		int nombreTickersAuDessus = 0;
		double[] retours = this.getTickersSet().getData().getExpectedReturnsOfEachAsset();
		
		for(int i=0;i<n;i++){
			if(retours[i]>R){
				nombreTickersAuDessus++;
			}
		}
		int[] indicesTickersAuDessus = new int[nombreTickersAuDessus];
		int compt = 0;
		for(int i=0;i<n;i++){
			if(retours[i]>R){
				indicesTickersAuDessus[compt]=i;
				compt++;
			}
		}

		int nombreTickersAuDessous = n-nombreTickersAuDessus;
		int[] indicesTickersAuDessous = new int[nombreTickersAuDessous];
		compt = 0;
		for(int i=0;i<n;i++){
			if(retours[i]<=R){
				indicesTickersAuDessous[compt]=i;
				compt++;
			}
		}

		double sommeAuDessus = 0;
		for(int i=0;i<nombreTickersAuDessus;i++){
			int indiceDuTicker = indicesTickersAuDessus[i];
			double retourDuTicker = this.getTickersSet().getData().getExpectedReturnsOfEachAsset()[indiceDuTicker];
			sommeAuDessus = sommeAuDessus + retourDuTicker - R;
		}

		double sommeAuDessous = 0;
		for(int i=0;i<nombreTickersAuDessous;i++){
			int indiceDuTicker = indicesTickersAuDessous[i];
			double retourDuTicker = this.getTickersSet().getData().getExpectedReturnsOfEachAsset()[indiceDuTicker];
			sommeAuDessous = sommeAuDessous + retourDuTicker - R;
		}

		
		
		double[] poidsTickers = new double[n];

		//faire l'autre cas
		if(sommeAuDessous<sommeAuDessus){

			double sommeARetablir = 0;
			for(int i=0; i<nombreTickersAuDessous;i++){
				int indiceTicker = indicesTickersAuDessous[i];
				double retourDuTicker = retours[indiceTicker];
				double poidsAleatoire = Math.random();
				poidsTickers[indiceTicker] = poidsAleatoire;
				sommeARetablir = sommeARetablir + poidsAleatoire*(R-retourDuTicker);
			}

			//trier indicesAuDessus
			int nbis = indicesTickersAuDessus.length - 1;
			for (int i = nbis; i >= 1; i--)
				for (int j = 1 ; j <= i; j++ ) 
					if (this.getTickersSet().getData().getExpectedReturnsOfEachAsset()[indicesTickersAuDessus[j-1]] > this.getTickersSet().getData().getExpectedReturnsOfEachAsset()[indicesTickersAuDessus[j]])
					{
						int temp = indicesTickersAuDessus[j-1] ;
						indicesTickersAuDessus[j-1] = indicesTickersAuDessus[j] ;
						indicesTickersAuDessus[j] = temp ;
					}
			
			double[] deltaR = new double[nombreTickersAuDessus];
			for (int i=0; i<nombreTickersAuDessus; i++){
				int indiceTicker = indicesTickersAuDessus[i];
				deltaR[i] = retours[indiceTicker]-R;
			}
			
			System.out.println("indicesAuDessus = ");
			Tools.printArray(indicesTickersAuDessus);
			
			System.out.println("indicesAuDessous = ");
			Tools.printArray(indicesTickersAuDessous);
			
			System.out.println("deltaR = ");
			Tools.printArray(deltaR);
			
			System.out.println("sommeARetablir = "+sommeARetablir);
			

			
			// FAIRE L'AUTRE CAS
			if(deltaR[nombreTickersAuDessus-1]>sommeARetablir){
				
				System.out.println("r1 > sommeARetablir");

				for(int i=0;i<nombreTickersAuDessus-1; i++){
					
					if(deltaR[i]<sommeARetablir){
						int indiceTicker = indicesTickersAuDessus[i];
						double poidsAleatoire = Math.random();
						poidsTickers[indiceTicker] = poidsAleatoire;
						sommeARetablir = sommeARetablir - deltaR[i]*poidsAleatoire;
					}
					
					else {
						
						int indiceTicker = indicesTickersAuDessus[i];
						double max = sommeARetablir/deltaR[i];
						double poidsAleatoire = Math.random()*max;
						poidsTickers[indiceTicker] = poidsAleatoire;
						sommeARetablir = sommeARetablir - deltaR[i]*poidsAleatoire;
						
					}
					
				}

				int indiceTickerFin = indicesTickersAuDessus[nombreTickersAuDessus-1];
				poidsTickers[indiceTickerFin] = sommeARetablir/deltaR[indiceTickerFin];


				double sumWeights = Tools.sumArray(poidsTickers);

				for(int i = 0; i<n; i++){
					poidsTickers[i]=poidsTickers[i]/sumWeights;
				}

				System.out.println("somme des poids : ");
				Tools.printSumArray(poidsTickers);
				
				
				this.weights = poidsTickers;
				/*System.out.println("weights of this state :");
				Tools.printArray(weights);*/
				this.returns = computeReturns(weights);
				this.setExpectedReturn(this.computeExpectedReturn());

				System.out.println("retour des poids : " + this.expectedReturn);
				
			}

		}

	}

	
	/**
	 * This method is used to save a solution at each iteration.
	 */
	public void sauvegarderSolution(){
		//Affectation des poids
		this.meilleursPoids = Tools.cloneArray(this.weights);
			/*	for (int j = 0; j < this.tickersSet.getLength(); j++) {
					this.meilleursPoids[j] = this.weights[j];
					
				}*/
	}

	/**
	 * @return the expectedReturn
	 */
	public double getExpectedReturn() {
		return expectedReturn;
	}

	/**
	 * @param expectedReturn the expectedReturn to set
	 */
	public void setExpectedReturn(double expectedReturn) {
		this.expectedReturn = expectedReturn;
	}

	/**
	 * @param returns the returns to set
	 */
	public void setReturns(double[] returns) {
		this.returns = returns;
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
	 * @return the weights
	 */
	public double[] getWeights() {
		return this.weights;
	}

	/**
	 * @param weights the weights to set
	 */
	public void setWeights(double[] weights) {
		this.weights = weights;
	}

	/**
	 * @return the returns
	 */
	public double[] getReturns() {
		return returns;
	}
}
