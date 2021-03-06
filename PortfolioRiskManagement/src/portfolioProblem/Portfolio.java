package portfolioProblem;
import java.text.ParseException;

import org.apache.commons.math3.stat.descriptive.moment.Mean;

import solver.commun.EnergiePotentielle;
import solver.commun.Etat;
import Optionnel.Tools;

/**
 * This class decribes a portfolio
 * @author thomasdoutre
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
	 * @param Ep the potential energy
	 * @param tickers the tickersSet
	 * @param weights the weights
	 * @param returns the returns
	 */

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
	 */

	public Portfolio(TickersSet tickers, double[] weights) {
		this.tickersSet = tickers;
		this.weights = weights;
		this.returns = computeReturns(weights);
		this.expectedReturn = computeExpectedReturn();
	}
	
	/**
	 * This method is used to clone a portfolio.
	 */

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
		return expectedReturn;
	}

	/**
	 * This method is used to compute the arithmetic returns of a portfolio.
	 * @param weights Percentage we want to invest in each asset.
	 * @return the arithmetic returns.
	 */
	public double[] computeReturns(double[] weights){
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
	 * This method is used to update and return the arithmetic returns of a portfolio (after changing weights).
	 * @return the arithmetic returns.
	 */
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
	 * This method is used to initialize a portfolio. Only two assets are considered at first.
	 */

	public void initialiserSimple(){
		double R = 0.06;

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

			}
			else {
				up = i;
				upReturn = this.getTickersSet().getData().getExpectedReturnsOfEachAsset()[i];
			}
		}

		if (low == up || upReturn < R || lowReturn>R){
			System.out.println("WARNING : Expected return level may be too extreme");
		}
		weights[low] = (upReturn - R)/(upReturn-lowReturn);
		weights[up] = (R-lowReturn)/(upReturn-lowReturn);


		this.weights = weights;
		this.returns = computeReturns(weights);
		this.setExpectedReturn(this.computeExpectedReturn());

	}
	
	
	
	
	
	/**
	 * This method is used to randomly initialize a portfolio.
	 */

	public void initialiser(){

		
		//Fixe Rbarre
		double R = 0.08;
		
		//On va compter les marges relatives à Rbarre que l'on dispose parmi les actifs du portefeuille
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

		//Deux cas se présentent : si sommeAuDessous<sommeAuDessus ou si sommeAuDessous>sommeAuDessus
		
		//Premier cas
		if(sommeAuDessous<sommeAuDessus){
			//On attribut des poids aléatoires aux actifs dont le retour est inférieur à Rbarre
			double sommeARetablir = 0;
			for(int i=0; i<nombreTickersAuDessous;i++){
				int indiceTicker = indicesTickersAuDessous[i];
				double retourDuTicker = retours[indiceTicker];
				double poidsAleatoire = Math.random();
				poidsTickers[indiceTicker] = poidsAleatoire;
				sommeARetablir = sommeARetablir + poidsAleatoire*(R-retourDuTicker);
			}
			
			//trier indicesAuDessus via tri-bulles
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
			
			
			// Si un actif est suffisant pour combler la marge laissée par les tickers de retours inférieurs à Rbarre
			if(deltaR[nombreTickersAuDessus-1]>sommeARetablir){
				
				for(int i=0;i<nombreTickersAuDessus-1; i++){
					
					if(deltaR[i]<sommeARetablir){
						int indiceTicker = indicesTickersAuDessus[i];
						double poidsAleatoire;
						if(deltaR[nombreTickersAuDessus-1]<sommeARetablir){
							poidsAleatoire = 1.0;
						}
						else {poidsAleatoire = Math.random();}
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
				poidsTickers[indiceTickerFin] = sommeARetablir/deltaR[nombreTickersAuDessus-1];

				double sumWeights = Tools.sumArray(poidsTickers);

				for(int i = 0; i<n; i++){
					poidsTickers[i]=poidsTickers[i]/sumWeights;
				}

				this.weights = poidsTickers;
				this.returns = computeReturns(weights);
				this.setExpectedReturn(this.computeExpectedReturn());
				
			}
			

		}
		
		//Deuxieme cas
		else {
			System.out.println("Initialisation : deuxieme cas");


			System.out.println("2E CAS");
			//On attribut des poids aléatoires aux actifs dont le retour est superieur à Rbarre
			double sommeARetablir = 0;
			for(int i=0; i<nombreTickersAuDessus;i++){
				int indiceTicker = indicesTickersAuDessus[i];
				double retourDuTicker = retours[indiceTicker];
				double poidsAleatoire = Math.random();
				poidsTickers[indiceTicker] = poidsAleatoire;
				sommeARetablir = sommeARetablir - poidsAleatoire*(R-retourDuTicker);
			}
			System.out.println("somme a retablir positive : "+ sommeARetablir);
			
			//trier indicesAuDessous via tri-bulles
			int nbis = indicesTickersAuDessous.length - 1;
			for (int i = nbis; i >= 1; i--)
				for (int j = 1 ; j <= i; j++ ) 
					if (this.getTickersSet().getData().getExpectedReturnsOfEachAsset()[indicesTickersAuDessous[j-1]] < this.getTickersSet().getData().getExpectedReturnsOfEachAsset()[indicesTickersAuDessous[j]])
					{
						int temp = indicesTickersAuDessous[j-1] ;
						indicesTickersAuDessous[j-1] = indicesTickersAuDessous[j] ;
						indicesTickersAuDessous[j] = temp ;
					}
			
			double[] deltaR = new double[nombreTickersAuDessous];
			for (int i=0; i<nombreTickersAuDessous; i++){
				int indiceTicker = indicesTickersAuDessous[i];
				deltaR[i] = -(retours[indiceTicker]-R);
			}
			
			
			
			// Si un actif est suffisant pour combler la marge laissée par les tickers de retours inférieurs à Rbarre
			if(deltaR[nombreTickersAuDessous-1]>sommeARetablir){
				
				for(int i=0;i<nombreTickersAuDessous-1; i++){
					
					if(deltaR[i]<sommeARetablir){
						int indiceTicker = indicesTickersAuDessous[i];
						double poidsAleatoire;
						if(deltaR[nombreTickersAuDessus-1]<sommeARetablir){
							poidsAleatoire = 1.0;
						}
						else {poidsAleatoire = Math.random();}
						poidsTickers[indiceTicker] = poidsAleatoire;
						sommeARetablir = sommeARetablir - deltaR[i]*poidsAleatoire;
					}
					
					else {
						
						int indiceTicker = indicesTickersAuDessous[i];
						double max = sommeARetablir/deltaR[i];
						double poidsAleatoire = Math.random()*max;
						poidsTickers[indiceTicker] = poidsAleatoire;
						sommeARetablir = sommeARetablir - deltaR[i]*poidsAleatoire;
						
					}
					
				}

				int indiceTickerFin = indicesTickersAuDessous[nombreTickersAuDessous-1];
				poidsTickers[indiceTickerFin] = sommeARetablir/deltaR[nombreTickersAuDessous-1];

				double sumWeights = Tools.sumArray(poidsTickers);

				for(int i = 0; i<n; i++){
					poidsTickers[i]=poidsTickers[i]/sumWeights;
				}

				this.weights = poidsTickers;
				this.returns = computeReturns(weights);
				this.setExpectedReturn(this.computeExpectedReturn());

				Tools.printArray(this.weights);
				Tools.printSumArray(this.weights);
				System.out.println("retour des poids : " + this.expectedReturn);
				
			}
			
		}

	}



	/**
	 * This method is used to save a solution at each iteration.
	 */
	public void sauvegarderSolution(){
		this.meilleursPoids = Tools.cloneArray(this.weights);
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
