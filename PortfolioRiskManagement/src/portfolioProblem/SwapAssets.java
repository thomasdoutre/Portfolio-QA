package portfolioProblem;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import solver.commun.Etat;
import solver.commun.IMutation;
import solver.commun.MutationElementaire;
import solver.commun.Probleme;
import Optionnel.Tools;

/**
 * This class decribes a mutation that keeps returns unchanged by swapping 3 assets
 * @version 1.0
 * @since   2015-05-09
 */
// extend MutationElementaire ?
public class SwapAssets implements IMutation {
	int Asset1;
	int Asset2;
	int Asset3;
	double step;
	double delta1;
	double delta2;
	double delta3;


	@Override

	/**
	 * This method is used to get an elementary mutation of asset's weights in the portfolio
	 * This elementary mutation is randomly generated
	 * @param probleme the problem chosen
	 * @param etat the replica chosen
	 * @throws ParseException 
	 */

	public MutationElementaire getMutationElementaire(Probleme probleme,
			Etat etat) {
		Portfolio portfolio = (Portfolio) etat;
		int NombreTickers = portfolio.getTickersSet().getLength();

		this.initialize(NombreTickers);

		double R1 = portfolio.getTickersSet().getData().getExpectedReturnsOfEachAsset()[Asset1];
		double R2 = portfolio.getTickersSet().getData().getExpectedReturnsOfEachAsset()[Asset2];
		double R3 = portfolio.getTickersSet().getData().getExpectedReturnsOfEachAsset()[Asset3];

		HashMap<Integer,Double> vect = new HashMap<Integer,Double>();

		vect.put(Asset1, -step);
		vect.put(Asset2, step*((R1-R3)/(R2-R3)));
		vect.put(Asset3, step*((R2-R1)/(R2-R3)));

		return new Swap(vect);
	}



	public void initialize(int nombreTickers) {

		Random generator = new Random();
		this.Asset1 = generator.nextInt(nombreTickers);

		do {
			this.Asset2 = generator.nextInt(nombreTickers);
		} while (Asset2==Asset1);

		do {
			this.Asset3 = generator.nextInt(nombreTickers);
		} while (Asset2==Asset3 || Asset3 == Asset1);

		this.step = 0.1*Math.random();
	}


	public void faire(Probleme probleme, Etat etat, MutationElementaire mutation) {
		Portfolio portfolio = (Portfolio) etat;
		HashMap<Integer,Double> vect = ((Swap) mutation).getVecteur();

		double[] weights = portfolio.getWeights();
		boolean weightIsNegative = false;
		for(Map.Entry<Integer, Double> entry : vect.entrySet()){
			if((weights[entry.getKey()]+entry.getValue())<0){
				weightIsNegative = true;
			}

		}


		if(weightIsNegative==false){


			for(Map.Entry<Integer, Double> entry : vect.entrySet()){

			    double delta = entry.getValue();
			    weights[entry.getKey()] += delta;
			}
		}

		portfolio.setWeights(weights);
		portfolio.setReturns(portfolio.updateReturns());
	}



}
