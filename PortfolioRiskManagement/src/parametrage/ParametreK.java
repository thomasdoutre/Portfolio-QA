package parametrage;

public class ParametreK {

	double K =1;
	
	/**
	 * Construit un paramètre K intervenant dans la probabilité d'acceptation de l'état muté. En génral, dans QA, K = 1
	 * @param e
	 * Valeur numérique de K
	 */
	public ParametreK(double e){
		this.K = e;
	}
	/**
	 * @return
	 * Retourne la valeur numérique de K
	 */
	public double getK(){
		return this.K;
	}

	/**
	 * Permet d'établir la nouvelle valeur numérique de K
	 * @param e
	 * Nouvelle valeur numérique de K
	 */
	public void setK(double e){
		this.K = e;
	}
	

	
}
