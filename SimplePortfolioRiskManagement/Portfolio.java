import java.util.Random;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;

/**
 * This class describes a portfolio
 * @author thomasdoutre
 * @version 1.0
 * @since   2015-06-20
 */

public class Portfolio {

	private TickersSet tickersSet;
	private double[] weights;
	private double[] returns;
	private double expectedReturn;

	Portfolio(TickersSet tickersSet){
		this.tickersSet = tickersSet;
		this.weights = new double[tickersSet.getLength()];
		this.returns = new double[this.tickersSet.getData().getReturnsMatrix().length];
		
	}

	public void initialiser(double R){

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

		if(nombreTickersAuDessous==0){
			System.out.println("Retour demandé trop bas");
		}

		if(nombreTickersAuDessus==0){
			System.out.println("Retour demandé trop haut");
		}

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


			/*// Si un actif est suffisant pour combler la marge laissée par les tickers de retours inférieurs à Rbarre
			if(deltaR[nombreTickersAuDessus-1]>sommeARetablir){
			 */
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



			//	}


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



			/*// Si un actif est suffisant pour combler la marge laissée par les tickers de retours inférieurs à Rbarre
			if(deltaR[nombreTickersAuDessous-1]>sommeARetablir){
			 */
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

			/*Tools.printArray(this.weights);
				Tools.printSumArray(this.weights);
				System.out.println("retour des poids : " + this.expectedReturn);*/

		}

		//}

		this.weights = poidsTickers;
		this.returns = computeReturns(weights);
		this.setExpectedReturn(this.computeExpectedReturn());

	}

	public double computeExpectedReturn(){
		Mean mean = new Mean();
		double expectedReturn;
		expectedReturn = mean.evaluate(this.returns);
		return expectedReturn;
	}

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

	public double computeVaR(){
		Percentile percentile = new Percentile();
		double valueAtRisk;
		valueAtRisk = - percentile.evaluate(this.returns, 5.0);
		
		return valueAtRisk;
	}
	
	public double computeCVaR(){
		Percentile percentile = new Percentile();
		double valueAtRisk;
		double[] tab = this.getReturns();
		valueAtRisk = - percentile.evaluate(tab, 5.0);

		double sum = 0;
		int compt = 0;

		for(int i =0 ; i<tab.length;i++){
			if(tab[i]<(-valueAtRisk)){
				sum = sum + tab[i];
				compt++;
			}
		}
		double conditionalVaR = - sum/compt;
		return conditionalVaR;
	}
	
	public double computeVaRWithPenalty(double limit){
		
		boolean shouldBePenalized = false;
		for (int i = 0; i < this.weights.length; i++) {
			if(this.weights[i]<limit){
				shouldBePenalized = true;
			}
		}
		
		Percentile percentile = new Percentile();
		double valueAtRisk;
		valueAtRisk = - percentile.evaluate(this.returns, 5.0);
		
		if(shouldBePenalized){
			return (valueAtRisk+1);
		}
		return valueAtRisk;
	}
	

	public void rebalance(double stepUtilisateur){
		int nombreTickers = this.getTickersSet().getLength();
		double[] newWeights = Tools.cloneArray(this.weights); 

		Random generator = new Random();
		int asset1 = generator.nextInt(nombreTickers);
		int asset2 = generator.nextInt(nombreTickers);
		int asset3 = generator.nextInt(nombreTickers);
		while (asset2==asset1){
			asset2 = generator.nextInt(nombreTickers);
		}
		while (asset3==asset1 || asset3==asset2){
			asset3 = generator.nextInt(nombreTickers);
		}

		double R1 = this.getTickersSet().getData().getExpectedReturnsOfEachAsset()[asset1];
		double R2 = this.getTickersSet().getData().getExpectedReturnsOfEachAsset()[asset2];
		double R3 = this.getTickersSet().getData().getExpectedReturnsOfEachAsset()[asset3];

		double step = stepUtilisateur;
		if(newWeights[asset1]-step<0){
			step = -step;
		}

		double limit = 0;
		if(newWeights[asset2]+step*((R1-R3)/(R2-R3))>limit && newWeights[asset3]+step*((R2-R1)/(R2-R3))>limit){
			newWeights[asset1] = newWeights[asset1]-step;
			newWeights[asset2] = newWeights[asset2]+step*((R1-R3)/(R2-R3));
			newWeights[asset3] = newWeights[asset3]+step*((R2-R1)/(R2-R3));
		}
		else{
		}


		this.weights = newWeights;
		this.returns = this.computeReturns(newWeights);
		
	}

	public Portfolio clone(){
		Portfolio newPortfolio = new Portfolio(this.tickersSet);
		for(int i=0;i<this.tickersSet.getLength();i++){
			newPortfolio.weights[i] = this.weights[i];
		}
		newPortfolio.returns = newPortfolio.computeReturns(newPortfolio.weights);
		
		return newPortfolio;
	}
	
	public double computeDistance1(Portfolio port){
		double[] portWeights = port.getWeights();
		double[] portfolioWeights = this.getWeights();
		double distance = 0;
		
		for (int i = 0 ; i< this.getWeights().length ; i++){
			distance = distance + Math.abs((portfolioWeights[i]-portWeights[i]));
		}
		return distance;
	}
	
	public double computeDistance2(Portfolio port){
		double[] portWeights = port.getWeights();
		double[] portfolioWeights = this.getWeights();
		double distance = 0;
		
		for (int i = 0 ; i< this.getWeights().length ; i++){
			distance = distance + Math.pow((portfolioWeights[i]-portWeights[i]),2);
		}
		return distance;
	}



	public TickersSet getTickersSet() {
		return tickersSet;
	}

	public void setTickersSet(TickersSet tickersSet) {
		this.tickersSet = tickersSet;
	}

	public double[] getWeights() {
		return weights;
	}

	public void setWeights(double[] weights) {
		this.weights = weights;
	}

	public double[] getReturns() {
		return returns;
	}

	public void setReturns(double[] returns) {
		this.returns = returns;
	}

	public double getExpectedReturn() {
		return expectedReturn;
	}

	public void setExpectedReturn(double expectedReturn) {
		this.expectedReturn = expectedReturn;
	}



}
