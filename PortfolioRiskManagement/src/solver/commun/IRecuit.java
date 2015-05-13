package solver.commun;

/**
 * Template pour le recuit. Tous les recuits doivent implémenter cette interface ou hériter d'une classe le faisant.
 */
public interface IRecuit {
	
	/**
	 * Effectue le recuit sur le probléme.
	 * @param problem
	 * Le probléme sur lequel on veut effectuer le recuit.
	 */
	public void lancer(Probleme problem);
	
}
