package parametrage;

import modele.Graphe;

public class ParametreurGamma {
	
	//Fonction réciproque é tanh
	/**
	 * Cette méthode permet le passage de J vers Gamma. Si J=f(Gamma), cette méthode correspond é la réciproque de f
	 * @param x
	 * Valeur numérique de la pondération
	 * @return
	 * Retourne le Gamma associé
	 */
	public static double argth(double x){
		return 0.5*Math.log((1+x)/(1-x)); 
	}

	// Ce parametreur donne les 3 attributs d'un parametre Gamma pour un recuit
	// Il essaie de comparer l'influence d'une amelioration d'Epot et d'Ecin
	// Etant donne un deltaE, le parametreur decrete qu'é la fin du recuit, une amelioration moyenne d'Ecin a la méme influence qu'une amelioration deltaE/p.size() de Epot
	// Le poids permet de faie varier linéairement l'influence d'une amelioration sur l'autre
	/**
	 * Paramétre le Gamma pour le recuit. On fixe Jfin = deltaE/(4*P) et Jdeb = 100*Jfin. 
	 * @param nombreIterations
	 * Nombre d'iterations du recuit
	 * @param nombreEtat
	 * Nombre de répliques pour le recuit
	 * @param temp
	 * Température du recuit
	 * @param deltaE
	 * Delta d'énergie potentielle. Représente le deuxiéme décile de la distribution des deltaE pour le graphe considéré
	 * @return
	 * Le paramétre Gamma que l'on va utiliser pour le recuit
	 */
	public static ParametreGamma parametrageGamma( int nombreIterations,int nombreEtat,Temperature temp, double deltaE){
		double t = temp.getValue();
		double finGamma = nombreEtat*t*argth(Math.exp(-deltaE/(2*t*nombreEtat)));
		double facteur = 1.0-Math.pow(0.01,1.0/nombreIterations);
		ParametreGamma gamma = new ParametreGamma(100*finGamma,facteur,finGamma);
		return gamma;
	}
}
