/**
 * This class describes a QA particle
 * @author thomasdoutre
 * @version 1.0
 * @since   2015-06-20
 */

public class Particule {

	public Portfolio[] etatsParticule;
	
	public Particule(TickersSet tickersSet, int nbEtats, double R){
		this.etatsParticule = new Portfolio[nbEtats];
		for(int i = 0 ; i<nbEtats ; i++){
			Portfolio portfolio = new Portfolio(tickersSet);
			portfolio.initialiser(R);
			this.etatsParticule[i] = portfolio;
		}
	}
	
	public Particule(int nbEtats){
		this.etatsParticule = new Portfolio[nbEtats];
	}
	
	public Particule clone(){
		Particule cloneParticule = new Particule(this.etatsParticule.length);
		for (int i = 0; i < this.etatsParticule.length; i++) {
			cloneParticule.etatsParticule[i]=this.etatsParticule[i].clone();
		}
		return cloneParticule;
	}
	
	public double computeKinetic1(int indice){
		
		double kineticEnergy = 0;
		for (int i = 0; i < etatsParticule.length; i++) {
			if(i!=indice){
				kineticEnergy = kineticEnergy + this.etatsParticule[indice].computeDistance1(this.etatsParticule[i]);
			}
		}
		
		return kineticEnergy;
	}
	
public double computeKinetic2(int indice){
		
		double kineticEnergy = 0;
		for (int i = 0; i < etatsParticule.length; i++) {
			if(i!=indice){
				kineticEnergy = kineticEnergy + this.etatsParticule[indice].computeDistance2(this.etatsParticule[i]);
			}
		}
		
		return kineticEnergy;
	}
	
}
