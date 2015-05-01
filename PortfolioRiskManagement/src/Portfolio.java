import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;

public class Portfolio{
	
	public String[] tickers;
	private double[] weights;
	private double[] portfolioLogReturn;
	private double[] portfolioRawReturn;
	private double valueAtRisk;
	
	public Portfolio(YahooData data){
		double[] weights = new double[data.getQuoteMatrix()[0].length];

		weights[0]=1;
		for(int i = 1; i < data.getQuoteMatrix()[0].length-1; i++){
			weights[i] = 0;
		}
		this.weights = weights;

		this.portfolioLogReturn = computeLogReturn(data, weights);
		this.valueAtRisk = computeVAR(this.portfolioLogReturn, 5.0);
	}
	
	// constructeur de portefeuille avec une allocation initiale particulière, utile pour la méthode de clonage
	public Portfolio(double[] weights, double[] portfolioLogReturn){
		this.setWeights(weights);
		//this.portfolioRawReturn = portfolioRawReturn;
		this.portfolioLogReturn = portfolioLogReturn;
		this.valueAtRisk = computeVAR(this.portfolioLogReturn, 5.0);
	}
	
	// Permet de cloner un portefeuille avec une certaine allocation initiale
	public Portfolio clone(){
		double[] clonedWeights = new double[this.getWeights().length];
		//double[] clonedPortfolioRawReturn = new double[this.portfolioRawReturn.length];
		double[] clonedPortfolioLogReturn = new double[this.portfolioLogReturn.length];
		for(int i = 0; i < this.getWeights().length; i++){
			clonedWeights[i] = this.getWeights()[i];
		}
		/*for(int i = 0; i < this.getPortfolioRawReturn().length; i++){
			clonedPortfolioRawReturn[i] = this.getPortfolioRawReturn()[i];
		}*/
		for(int i = 0; i < this.getPortfolioLogReturn().length; i++){
			clonedPortfolioLogReturn[i] = this.getPortfolioLogReturn()[i];
		}
		Portfolio clone = new Portfolio(clonedWeights, clonedPortfolioLogReturn);
		return clone;
	}
	
	// Calcul des log-returns d'un portefeuille
	// cette méthode représente environ 70% du temps de calcul... Peut-être amélioré si on n'utilise que les retours arithmétiques
	// en effet, le retour arithmétique du portefeuille est une CL des retours arithmétiques de chacun de ses actifs. En connaissant
	// les actifs que l'on a muté, le retour arithmétique du portefeuille peut être modifié en ne touchant que les actifs concernés.
	public double[] computeLogReturn(YahooData data, double[] weights){
		double[][] rawReturnsMatrix = data.getRawReturnsMatrix();
		int n = rawReturnsMatrix.length;
		double portfolioLogReturn[] = new double[n];
		double portfolioRawReturn = 0;
		
		for(int i = 0; i < n; i++){
			for(int j = 0; j < weights.length; j++){
				portfolioRawReturn += weights[j]*rawReturnsMatrix[i][j];
				System.out.println("portfolioRawReturn = "+portfolioRawReturn);
			}
			if(portfolioRawReturn == 0){
			System.out.println("portfolioRawReturn = 0 !!");
			portfolioRawReturn = 0;
			}
			
			else{
				portfolioLogReturn[i] = Math.log(portfolioRawReturn);
			}
		}
		return portfolioLogReturn;
	}
	
	// calcul des retours arithmétiques d'un portefeuille
	public double[] computeRawReturn(YahooData data, double[] weights){
		double portfolioRawReturn[] = new double[data.getQuoteMatrix().length-1];
		for(int i = 0; i < data.getQuoteMatrix().length-1; i++){
			for(int j = 0; j < data.getQuoteMatrix()[0].length; j++){
				portfolioRawReturn[i] += weights[j]*data.getRawReturnsMatrix()[i][j];
			}
			portfolioRawReturn[i] = portfolioRawReturn[i];
		}
		return portfolioRawReturn;
	}
	
	// calcul d'un centile (VAR...) de la liste des retours du portefeuille (pour le moment, les log-returns)
	// l'appel à cette méthode représente environ 30% du temps de calcul total. Un tri efficace et un calcul du centile à la main
	// peut éventuellement améliorer cela
	public double computeVAR(double[] portfolioReturns, double alpha){
		Percentile percentile = new Percentile();
		double valueAtRisk = 0.0;
		valueAtRisk = percentile.evaluate(portfolioReturns, alpha);
		return valueAtRisk;
	}

	// calcule l'espérance du retour du portefeuille.
	public double computeExpectedReturn(double[] portfolioReturns){
		Mean mean = new Mean();
		double rendement = 0.0;
		rendement = mean.evaluate(portfolioReturns);
		return rendement;
	}

	// rafraichissement des attributs après une mutation. La mutation s'occupe de rafraichir les poids.
	public void update(YahooData data){
			//this.portfolioRawReturn = computeRawReturn(data, this.weights);
			this.portfolioLogReturn = computeLogReturn(data, this.weights);
			this.valueAtRisk = computeVAR(this.portfolioLogReturn, 5.0);
	}
	
	// l'energie est un compromis entre la minimisation du risque et la distance au retour que l'on cherche à obtenir.
	public double getEnergy(double targetReturn){
		double ExpectedReturn = computeExpectedReturn(this.portfolioLogReturn);
		double VAR = computeVAR(this.portfolioLogReturn, 5.0);
		double energy = 100*Math.abs(targetReturn-ExpectedReturn)-VAR;
		return energy;
	}
	
	// getters et setters
	public double[] getWeights(){
		return this.weights;
	}

	public double[] getPortfolioRawReturn(){
		return this.portfolioRawReturn;
	}
	
	public double[] getPortfolioLogReturn(){
		return this.portfolioLogReturn;
	}
	
	public double getValueAtRisk(){
		return this.valueAtRisk;
	}
	
	public void setWeights(double[] weights) {
		this.weights = weights;
	}
	
	/*public void setPortfolioRawReturn(Data data, double[] weights){
		this.portfolioRawReturn = computeRawReturn(data, weights);
	}*/
	
	public void setPortfolioLogReturn(YahooData data, double[] weights){
		this.portfolioLogReturn = computeLogReturn(data, weights);
	}
	
	public void setValueAtRisk(double[] portfolioReturn){
		Percentile percentile = new Percentile();
		this.valueAtRisk = percentile.evaluate(portfolioReturn, 5.0);
	}
	
	// affichage d'un portefeuille (son poids, ses retours logarithmiques, sa var, l'espérance de son rendement)
	public String toString(){
		String strWeights = "poids : [";
		for(int i=0; i < this.weights.length; i++){
			if(i != this.weights.length-1){
			strWeights += this.weights[i]+"|";
			} else{
				strWeights += this.weights[this.weights.length-1]+"]\nretours logarithmiques : [";
			}
		}
		for(int i=0; i < this.portfolioLogReturn.length; i++){
			if(i != this.portfolioLogReturn.length-1){
			strWeights += this.portfolioLogReturn[i]+"|";
			} else{
				strWeights += this.portfolioLogReturn[this.portfolioLogReturn.length-1]+"]\nV@R = "+this.getValueAtRisk()+"\nrendement = "+this.computeExpectedReturn(this.portfolioLogReturn);
			}
		}
		return strWeights;
	}
}
