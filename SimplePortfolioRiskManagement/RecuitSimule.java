/**
 * This class implements the SA algorithm
 * @author thomasdoutre
 * @version 1.0
 * @since   2015-06-20
 */


public class RecuitSimule {

	double temperatureInitiale;
	double temperatureFinale;
	double nbIterations;
	TickersSet tickersSet;
	
	double[] meilleursPoids;
	double meilleureEnergie;
	
	public RecuitSimule(double temperatureInitiale, double temperatureFinale, double nbIterations, TickersSet tickersSet){
		this.temperatureInitiale = temperatureInitiale;
		this.temperatureFinale = temperatureFinale;
		this.nbIterations = nbIterations;
		this.tickersSet = tickersSet;
	}
	
	public void run(double step,double R){
		
	//	System.out.println("Demarrage du recuit Simule");
		
		double kb = 0.21;
		
		
		Portfolio portfolio = new Portfolio(this.tickersSet);
		portfolio.initialiser(R);
		this.meilleureEnergie = portfolio.computeVaR();
		this.meilleursPoids = portfolio.getWeights();
		
		double temperature = this.temperatureInitiale;
		double pas = (this.temperatureFinale - this.temperatureInitiale)/this.nbIterations;
		
		while(temperature>this.temperatureFinale){
			
			double energiePortfolio = portfolio.computeVaR();
			
			Portfolio clonePortfolio = portfolio.clone();
			clonePortfolio.rebalance(step);
			double energieClone = clonePortfolio.computeVaR();
			double deltaE = energieClone - energiePortfolio;
			
			if(deltaE<0){
				
				portfolio = clonePortfolio;
				
				if(energiePortfolio<this.meilleureEnergie){
					this.meilleureEnergie = energieClone;
					this.meilleursPoids = clonePortfolio.getWeights();
					//System.out.println("ACTUALISATION : MEILLEURE ENERGIE = " + this.meilleureEnergie);
				}
				
			}
			
			else {
				double probaAcceptation = Math.exp(-(deltaE)/kb*temperature);
				if(probaAcceptation>Math.random()){
					portfolio = clonePortfolio;
				}
			}
			temperature = temperature + pas;
			
		}
		
		System.out.println(this.meilleureEnergie);
		/*System.out.println("Meilleurs poids : ");
		Tools.printArray(this.meilleursPoids)*/;
		
	}
	
}
