

import modele.Etat;
import java.util.Random;
import modele.Probleme;
import mutation.IMutation;


public class SwapAssets implements IMutation {
	int Asset1;
	int Asset2;
	int Asset3;
	double step;
	
	
	

	public SwapAssets(int asset1, int asset2, int asset3, double step) {
		Asset1 = asset1;
		Asset2 = asset2;
		Asset3 = asset3;
		this.step = step;
	}

	@Override
	public void faire(Probleme p) {
		
		
	}

	@Override
	public double calculer(Probleme p) {
		
		return 0;
	}
	
	
	/**
	 * Seule mÃ©thode utilisÃ©e dans la classe SwapAssets. Cette méthode utilise les attributs de la classe, qui sont pour l'instant générés aléatoirement par initialize()
	 * Swap dÃ©signe une mutation du problème de portefeuille qui consiste à 
	 * @param p
	 * Probleme considéré
	 * @param e
	 * Etat du problème considéré
	 */
	@Override
	public void faire(Probleme p, Etat e) {
		
		
		
		Portfolio portfolio = (Portfolio) e;
		int NombreTickers = portfolio.getTickers().length;
		
		// Ici initialisation un peu au pif pour tout avouer
		this.initialize(NombreTickers);
		
		double R1 = portfolio.getRawReturns()[Asset1];
		double R2 = portfolio.getRawReturns()[Asset2];
		double R3 = portfolio.getRawReturns()[Asset3];
		
		double[] weights = portfolio.getWeights();
		
		weights[Asset1]=-step;
		weights[Asset2]=+step*((R1-R3)/(R2-R3));
		weights[Asset3]=+step*((R2-R1)/(R2-R3));
		
		portfolio.setWeights(weights);
	}


	/**
	 * Initialize() initialise aléatoirement les numéros des 3 actifs que l'on va ensuite pouvoir swap, ainsi que la valeur de step
	 * @param nombreTickers
	 * Nombre de Tickers 

	 */

	private void initialize(int nombreTickers) {
	
			Random generator = new Random();
		
			
			this.Asset1 = generator.nextInt(nombreTickers-1);
			
			do {
				this.Asset2 = generator.nextInt(nombreTickers-1);
			} while (Asset2==Asset1);
			
			do {
				this.Asset3 = generator.nextInt(nombreTickers-1);
			} while (Asset2==Asset3 || Asset3 == Asset1);
			
			this.step = 0.1*Math.random();
			
			
		}
		
	

	@Override
	public double calculer(Probleme p, Etat e) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void maj() {
		// TODO Auto-generated method stub
		
	}

	
	
	
}
