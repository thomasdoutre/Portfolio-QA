package solver.commun;

/**
 * Interface représentant le concept abstrait de mutation dans l'algorithme de recuit.
 * <p>
 * Il est nécessaire de coder une classe qui implémente cette interface. Elle doit implémenter les deux fonctions 
 * getMutationElementaire() et faire().
 */
public interface IMutation {

	/**
	 * Renvoie une MutationElementaire possible selon le probléme et l'état envoyés, sans l'effectuer.
	 * @param probleme
	 * Le probléme dont on cherche une mutation élementaire possible.
	 * @param etat
	 * L'état dont on cherche une mutation élementaire possible.
	 * @return Un objet MutationElementaire
	 */
	
	
	abstract public MutationElementaire getMutationElementaire(Probleme probleme, Etat etat);
	
	/**
	 * Le probléme fait une mutation sur demande.
	 * @param probleme
	 * Le probléme sur lequel on cherche é réaliser la mutation.
	 * @param etat
	 * L'état sur lequel on cherche é réaliser la mutation.
	 * @param mutation
	 * La mutation é réaliser.
	 */
	public void faire(Probleme probleme, Etat etat, MutationElementaire mutation);
	
}
