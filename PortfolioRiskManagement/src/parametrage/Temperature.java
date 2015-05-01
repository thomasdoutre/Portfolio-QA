package parametrage;

public class Temperature {
	double value;
	
	/**
	 * Construit un paramètre T
	 * @param value
	 * Valeur numérique le la température
	 */
	public Temperature(double value){
		this.value = value;
	}

	/**
	 * @return
	 * Retourne la valeur numérique de la température
	 */
	public double getValue(){
		return this.value;
	}
	
	/**
	 * Permet d'établir une nouvelle valeur numérique de T
	 * @param value
	 * Nouvelle valeur numérique de T
	 */
	public void setValue(double value){
		this.value = value;
	}
}
