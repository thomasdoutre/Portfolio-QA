package solver.commun;

/**
 * 
 * Classe abstraite qui sert de template pour les énergies cinétiques du recuit quantique.
 * Concrétement, cette classe correspond é une méthode de calcul de l'énergie cinétique spécifique é un probléme.
 * <p>
 * Il est nécessaire d'implémenter une sous-classe fille, et d'y implémenter calculer() et calculerDeltaE().
 * Il faut aussi implémenter calculerDeltaEUB() pour pouvoir utiliser le Recuit Quantique Accéléré.
 */
public abstract class EnergieCinetique{
	
	/**
	 * 
	 * @param probleme
	 * 	Le probléme dont on calcule l'énergie cinétique.
	 * @return Un double correspondant é l'énergie cinétique totale de la particule.
	 */
	abstract public double calculer(Probleme probleme);
	
	/**
	 * 
	 * @param etat
	 * 	L'état que l'on modifie.
	 * @param prev
	 * 	L'état situé avant l'état modifié.
	 * @param next
	 * 	L'état situé aprés l'état modifié.
	 * @param mutation
	 * 	La mutation que subit l'état centre et dont l'on veut calculer le changement d'énergie cinétique provoqué.
	 * @return Le changement d'énergie cinétique provoqué par la mutation sur l'état centre.
	 * 		
	 */
	abstract public double calculerDeltaE(Etat etat, Etat prev, Etat next, MutationElementaire mutation);
	
	/**
	 * 
	 * @param etat
	 * 	L'état que l'on modifie.
	 * @param prev
	 * 	L'état situé avant l'état modifié.
	 * @param next
	 * 	L'état situé aprés l'état modifié.
	 * @param mutation
	 * 	La mutation que subit l'état centre et dont l'on veut calculer la borne supérieure du changement d'énergie cinétique provoqué.
	 * @return La borne supérieure du changement d'énergie cinétique provoqué par la mutation sur l'état centre.
	 * 		
	 */
	abstract public double calculerDeltaEUB(Etat etat, Etat prev, Etat next, MutationElementaire mutation);
}
