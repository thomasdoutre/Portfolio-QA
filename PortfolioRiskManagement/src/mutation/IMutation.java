package mutation;
import modele.Etat;
import modele.Probleme;

/**
 * 
 * @author Pierre
 *
 */
public interface IMutation {
	/**
	 * Faire la mutation. Modifie la particule.
	 * Dans le cas oé la mutation porte sur une réplique é la fois, implémenter seulement faire(Probleme p, Etat e) 
	 * L'utilisateur devra implémenter cette méthode dans la classe fille adaptée.
	 * @param p
	 * Probleme sur lequel on mute (exemple : ParticuleTSP)
	 */
public void faire(Probleme p);
	/**
	 * Calcule la différence d'énergie apparue lors de la mutation.
	 * Dans le cas oé la mutation porte sur une réplique é la fois, retourner 0 et implémenter faire(Probleme p, Etat e) 
	 * L'utilisateur devra implémenter cette méthode dans la classe fille adaptée.
	 * @param p
	 * Probleme sur lequel on mute
	 * @return
	 * Valeur numérique de la différence d'énergie aprés mutation (sans pour autant muter !)
	 */
public double calculer(Probleme p);
/**
 * Faire la mutation sur l'état e de la particule. Ex : faire(p,p.getEtat.get(0))
 * L'utilisateur devra implémenter cette méthode dans la classe fille adaptée
 * @param p
 * Probléme sur lequel on mute.
 * @param e
 * Etat de la particule sur lequel la mutation a lieu
 */
public void faire(Probleme p, Etat e);
/**
 * Calcule la différence d'energie suite é la mutation sur l'état e
 * L'utilisateur devra implémenter cette méthode dans la classe fille adaptée
 * @param p
 * Probléme sur lequel on mute
 * @param e
 * Etat de la particule sur lequel la mutation a lieu.
 * @return
 * Différence d'énergie aprés mutation
 */
public double calculer(Probleme p, Etat e);
/**
 * Transforme la mutation courante en une autre mutation, de méme type(méme type d'objet) mais différente(arguments différents)
 * C'est en fait un générateur de mutation aléatoire,créant une mutation indépendante de la mutation traitée mais de méme type : c'est un constructeur en fait
 * L'utilisateur devra implémenter cette méthode dans la classe fille adaptée
 */
public void maj();
}
