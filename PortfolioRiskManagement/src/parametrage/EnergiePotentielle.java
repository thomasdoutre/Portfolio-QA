package parametrage;
import modele.Etat;


/**
 * 
 * @author Pierre
 *
 */
public  class EnergiePotentielle {


	/**
	 * N'est pas utilisé en tant que tel. 
	 * On calcule toujours une sous-énergie potentielle qui correspond à un certain problème.
	 * L'utilisateur devra implémenter cette méthode dans la classe fille adaptée.
	 * Ex : EnergiePotentielleTSP pour TSP
	 * @param etat
	 * l'etat sur lequel on calcule l'énergie potentielle
	 * @return 0
	 */
	public static double calculer(Etat etat){
		return 0;
	}

}
