package solver.commun;



/**
 * Classe abstraite représentant un probléme générique soluble par recuit.
 * <p>
 * Pour résoudre un probléme gréce au recuit, il faut faire une classe fille qui implémente
 * les différentes méthodes abstraites utilisées par l'algorithme.
 * Elle hérite de particules et disposent donc d'attributs en conséquence.
 * On utilise pour l'aléatoire une classe appelée HighQualityRandom.
 * <p>
 * Pour utiliser un recuit, il faut implémenter un Probleme spécifique é notre probléme,
 * et implémenter la fonction initialiser(), qui initialise le probléme ainsi que les états.
 * @see IMutation
 * @see HighQualityRandom
 * @see Particule
 */
public abstract class Probleme extends Particule{
	
	public IMutation mutation;
	private int seed = new HighQualityRandom().nextInt();
	public HighQualityRandom gen = new HighQualityRandom(getSeed());
	
	/**
	 * Initialise le probléme
	 */
	public abstract void initialiser();
	
	/**
	 * sauvegarde la solution actuelle dans une variable
	 */
	public abstract void sauvegarderSolution();
	
	/**
	 * Calcule et retourne l'énergie totale
	 * @return Energie totale de la particule (Energie cinétique + énergie potentielle)
	 */
	public double calculerEnergie(){
		return ( this.Ec.calculer(this) + calculerEnergiePotentielle() );
		//peut-étre pas trés utile
	}
	
	/**
	 * Calcule l'énergie potentielle de chaque etat et les somme
	 * @return Energie potentielle de la particule
	 */
	public double calculerEnergiePotentielle(){
		double energiePotentiel = 0;
		for (Etat etat : this.etats){
			energiePotentiel += etat.Ep.calculer(etat);
		}
		return energiePotentiel;
		// peut-étre renvoyer energiePotentielle / nombre de répliques??
	}
	

	/**
	 * Calcule deltaEp é partir de la mutation proposée (sans effectuer la mutation)
	 * @param etat
	 * Etat que l'on veut muter
	 * @param mutation
	 * Mutation é effectuer
	 * @return La différence d'énergie potentielle aprés que la mutation affecte l'état
	 */
	public double calculerDeltaEp(Etat etat, MutationElementaire mutation){
		return etat.Ep.calculerDeltaE(etat, mutation);
	}
	
	/**
	 * Calcule deltaEc é partir de la mutation proposée (sans effectuer la mutation)
	 * @param etat
	 * Etat que l'on veut muter
	 * @param previous
	 * Etat précédent sur la liste représentant les intéractions quantiques des spins entre particules
	 * @param next
	 * Etat suivant sur la liste représentant les intéractions quantiques des spins entre particules
	 * @param mutation
	 * Mutation é effectuer
	 * @return La différence d'énergie cinétique é un coefficient proportionnel JGamma prés aprés que la mutation affecte l'état
	 */
	public double calculerDeltaEc(Etat etat, Etat previous, Etat next, MutationElementaire mutation){
		return this.Ec.calculerDeltaE(etat, previous, next, mutation);
	}
	
	/**
	 * Calcule une borne supérieure de deltaEc é partir de la mutation proposée (sans effectuer la mutation). Celle ci dépend
	 * des tailles de la classe de coloriage précédant la mutation et de celle suivant la mutation.
	 * @param etat
	 * Etat que l'on veut muter
	 * @param previous
	 * Etat précédent sur la liste représentant les intéractions quantiques des spins entre particules
	 * @param next
	 * Etat suivant sur la liste représentant les intéractions quantiques des spins entre particules
	 * @param mutation
	 * Mutation é effectuer
	 * @return La borne supérieure de la différence d'énergie cinétique é un coefficient proportionnel JGamma prés aprés que la mutation affecte l'état
	 */
	public double calculerDeltaEcUB(Etat etat, Etat previous, Etat next, MutationElementaire mutation) {
		return this.Ec.calculerDeltaEUB(etat, previous, next, mutation); // é voir si cette fonction doit étre conforme (elle sera héritée et changée normalement)
	}
	
	/**
	 * Effectue une mutation élémentaire du probléme
	 * @param etat
	 * Etat sur lequel on veut effectuer une MutationElementaire
	 * @param mutation
	 * MutationElementaire é effectuer
	 */
	public void modifElem(Etat etat, MutationElementaire mutation){
		this.mutation.faire(this, etat, mutation);
	}

	/**
	 * Renvoie une mutation élémentaire de l'état
	 * @param etat
	 * Etat sur lequel on veut obtenir une MutationElementaire
	 * @return Une MutationElementaire possible
	 */
	public MutationElementaire getMutationElementaire(Etat etat){
		return this.mutation.getMutationElementaire(this,etat);
	}
	
	/**
	 * 
	 * @return Seed du probléme
	 */
	public int getSeed() {
		return seed;
	}
	/**
	 * Change le seed du générateur aléatoire du probléme
	 * @param seed
	 * Seed du générateur aléatoire du probléme
	 */
	public void setSeed(int seed) {
		this.seed = seed;
	}
	
}
