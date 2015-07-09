/**
 * This class implements the QA algorithm
 * @author thomasdoutre
 * @version 1.0
 * @since   2015-06-20
 */

public class RecuitQuantique {

	double gammaInitial;
	double gammaFinal;
	double nbIterations;
	double temperature;
	TickersSet tickersSet;

	double[] meilleursPoids;
	double meilleureEnergie;

	public RecuitQuantique(double gammaInitial, double gammaFinal, double nbIterations, TickersSet tickersSet, double temperature){
		this.gammaInitial = gammaInitial;
		this.gammaFinal = gammaFinal;
		this.nbIterations = nbIterations;
		this.tickersSet = tickersSet;
		this.temperature = temperature;
		this.meilleureEnergie = Double.MAX_VALUE;
	}

	public void run(double step,double R,int nbEtats){

		//System.out.println("Demarrage du recuit Quantique");

		double K = 0.21;
		double gamma = this.gammaInitial;
		
		double coeff = 0;
		if(this.gammaInitial>=0 && this.gammaFinal>=0){
			coeff = -1/nbIterations*Math.log(this.gammaFinal/this.gammaInitial);
		}
		else{
			System.out.println("GammaFinal et GammaInitial ne peuvent etre négatifs ou nuls pour une déscente exponentielle");
		}
		
		double pas = (this.gammaFinal - this.gammaInitial)/this.nbIterations;

		Particule particule = new Particule(tickersSet,nbEtats,R);

		double Jr = 0;

		
		int compteur = 0;
		while(gamma>this.gammaFinal){
			compteur++;
			Jr = (this.temperature/2)*Math.log(Math.tanh(gamma/(nbEtats*this.temperature)));
			
			for (int i = 0; i < particule.etatsParticule.length; i++) {

				double Ec = particule.computeKinetic2(i);
				double Ep = particule.etatsParticule[i].computeVaR();

				Portfolio clonePortfolio = particule.etatsParticule[i].clone();
				clonePortfolio.rebalance(step);

				Particule cloneParticule = particule.clone();
				cloneParticule.etatsParticule[i] = clonePortfolio;

				double EpClone = clonePortfolio.computeVaR();
				double EcClone = cloneParticule.computeKinetic2(i);

				double deltaEc = (EcClone - Ec);
				double deltaEp = EpClone - Ep;
				double deltaE = deltaEp/nbEtats - deltaEc*Jr;
				double proba = -1;


				if( deltaE <= 0 || deltaEp < 0) {
					proba = 1;
				}
				else {
					
					proba = Math.exp(-deltaE / (K * this.temperature));
				}

				if (proba == 1 || proba >= Math.random()) {

					particule = cloneParticule;

					if(deltaEp<0){

						if(EpClone<this.meilleureEnergie){

							this.meilleureEnergie = EpClone;
							this.meilleursPoids = particule.etatsParticule[i].getWeights();
							//System.out.println("ACTUALISATION : MEILLEURE ENERGIE POUR L ETAT "+i+" = " + this.meilleureEnergie);
						}
					}

				}

			}

			gamma = gamma*Math.exp(-coeff);
			
		}

		System.out.println(this.meilleureEnergie);
		
		//System.out.println("distance du port 1 = "+particule.computeKinetic2(0));
		/*System.out.println("Meilleurs poids : ");
		Tools.printArray(this.meilleursPoids);*/

	}

}
