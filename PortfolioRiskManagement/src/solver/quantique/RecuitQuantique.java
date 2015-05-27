package solver.quantique;


import java.util.ArrayList;
import java.util.Collections;

import portfolioProblem.Portfolio;
import solver.commun.Etat;
import solver.commun.IRecuit;
import solver.commun.MutationElementaire;
import solver.commun.Probleme;
import solver.parametres.ConstanteK;
import solver.parametres.Fonction;
import Optionnel.Tools;

/**
 * Classe implémentant le Recuit Quantique, qui prend en paramétres un Gamma et un K modulables ainsi qu'une température constante
 * et un nombre d'itérations consécutives sur un état.
 * <p>
 * Le recuit quantique est une variante du recuit simulé. Au lieu d'avoir une seule instance du probléme, on en a ici plusieurs
 * qui peuvent intéragir entre elles.
 * On va parler d'une particule, qui disposera de plusieurs états. Ces états influenceront chacun deux autres états dans une chaéne
 * liée invariante. On modifiera les états dans un ordre aléatoire, chaque état subiera le méme nombre d'essais de mutations.
 * 
 * @see solver.simule.RecuitSimule
 * @see solver.commun.Particule
 * @see Etat
 */
public class RecuitQuantique implements IRecuit { 				

	/**
	 * Fonction Gamma modulable. Représente l'effet tunnel.
	 * @see Fonction
	 */
	public Fonction Gamma;
	/**
	 * Constante k modulable.
	 * @see ConstanteK
	 */
	public ConstanteK K;
	/**
	 * Meilleure énergie(potentielle + cinétique) atteinte par le recuit simulé.
	 */
	public double meilleureEnergie = Double.MAX_VALUE;
	/**
	 * Température du recuit intervenant dans les calculs de probabilité, constante dans le recuit quantique.
	 */
	public double temperature;

	/**
	 * Nombre maximal d'itérations si la solution n'est pas trouvée, en redondance avec T.nbIteration
	 */
	public int nbMaxIteration;
	/**
	 * Nombre d'itérations consécutives sur un seul état.
	 */
	public int palier;

	public int mutationTentess;


	/**
	 * Réinitialise Gamma et K au début de lancer().
	 */
	protected void init(){
		this.Gamma.init();
		this.K.init();
		meilleureEnergie = Double.MAX_VALUE;
	}

	/**
	 * On envoie les paramétres modulables.
	 * @param Gamma
	 * Fonction Gamma effet tunnel créée au préalable.
	 * @param K
	 * Constante k créée au préalable.
	 * @param palier
	 * Nombre d'itérations consécutives sur un seul état.
	 * @param temperature
	 * Température constante du recuit quantique.
	 */
	public RecuitQuantique(Fonction Gamma, ConstanteK K, int palier, double temperature) {
		this.Gamma=Gamma;
		this.K=K;
		this.nbMaxIteration=this.Gamma.nbIteration;
		this.palier = palier;		
		this.temperature = temperature;
	}


	/**
	 * Effectue le recuit quantique sur le probléme.
	 * Le recuit quantique va préparer les variables, puis circuler aléatoirement sur une chaéne liée invariante connectant les 
	 * divers états du problémes.
	 * Ensuite il va penser é une mutations possible é l'état. Si elle est positive(diminue l'énergie potentielle) 
	 * alors on va l'effectuer, sinon on va l'effectuer avec une probabilité dépendante de la température, de la
	 * différence d'énergie potentielle de la mutation et de k.
	 * On réitére le processus un certain nombre de fois puis on change d'état.
	 * On réitére le tout jusqu'é avoir trouvé une réponse voulue ou un nombre d'itération maximale.
	 * <p>
	 * Pour ce qui est de l'utilisation de ce recuit, il faut créer une Fonction Gamma, une Constante k et un Probléme 
	 * au préalable. On initialise le recuit avec les deux premiers ainsi qu'une température et un palier constant,
	 * et on lance ensuite le recuit en lui envoyant le probléme.
	 * A la fin de lancer, on peut obtenir les résultats sur la variable probléme modifiée.
	 * @param probleme
	 * Le probléme sur lequel on veut effectuer le recuit quantique.
	 */
	public void lancer(Probleme probleme) {
		System.out.println("utilise la méthode 'lancer(Probleme probleme')");

		this.init();

		int mutationsTentees = 0;
		int mutationsAcceptees = 0;

		int nombreRepliques = probleme.etats.length;

		Etat etat = probleme.etats[0];
		Etat previous = probleme.etats[nombreRepliques-1];
		Etat next = probleme.etats[1];
		System.out.println("Energie de depart de l'état 0 = "+ probleme.etats[0].Ep.calculer(probleme.etats[0]));
		for (int i = 0; i < nombreRepliques; i++){	// initialisation de meilleureEnergie
			double energie = probleme.etats[i].Ep.calculer(probleme.etats[i]) ;
			if (energie < this.meilleureEnergie){
				this.meilleureEnergie = energie ;
			}

		}	

		double proba = 1;
		double deltaEp = 0;
		double deltaEc = 0;
		double deltaE = 0;
		double EpActuelle = 0;
		double Jr = 0;

		// tableau des indices des etats a parcourir dans un certain ordre
		ArrayList<Integer> indiceEtats = new ArrayList<Integer>(); 
		for( int i = 0; i < nombreRepliques ; i++){
			indiceEtats.add(i);
		}
		System.out.println("INITIAL WEIGHTS");
		Tools.printArray(((Portfolio)probleme.etats[0]).getWeights());
		System.out.println("END");
		
		boolean bool = false;
		System.out.println("se trouve avant la boucle Gamma.modifierT() =");

		while(Gamma.modifierT() /*&& this.meilleureEnergie!=0*/){
			System.out.println("entre dans la boucle Gamma.modifierT() =");

			Collections.shuffle(indiceEtats, probleme.gen);	// melanger l'ordre de parcours des indices
			Jr = -this.temperature/2*Math.log(Math.tanh(this.Gamma.t/nombreRepliques/this.temperature));	// calcul de Jr pour ce palier
			System.out.println("indiceEtats.size() ="+indiceEtats.size());

			for (Integer p : indiceEtats){	
				System.out.println("entre dans la boucle des p : indiceEtats");

				etat = probleme.etats[p];	

				if(p == 0){
					previous = probleme.etats[nombreRepliques-1];
				}
				else{
					previous = probleme.etats[p-1];
				}

				if (p == nombreRepliques - 1){
					next = probleme.etats[0];
				}
				else{
					next = probleme.etats[p+1];
				}

				for (int j = 0; j < this.palier; j++){
					System.out.println("entre dans la boucle jusqua this.palier");

					MutationElementaire mutation = probleme.getMutationElementaire(etat);	// trouver une mutation possible
					mutationsTentees++; //permet d'avoir une référence indépendante pour les améliorations de l'algorithme, mais aussi sur son temps

					deltaEp = probleme.calculerDeltaEp(etat, mutation);	// calculer deltaEp si la mutation etait acceptee
					deltaEc = probleme.calculerDeltaEc(etat, previous, next, mutation);  // calculer deltaIEc si la mutation etait acceptee

					//différences du hamiltonien total
					//multiplier deltaIEc par JGamma
					deltaE = deltaEp/nombreRepliques - deltaEc*Jr;

					//K.calculerK(deltaE);

					System.out.println("deltaE = "+deltaE+" : doit etre négative ou nulle pour accepter");
					System.out.println("deltaEp = "+deltaEp+" : doit etre négative pour accepter");

					if( deltaE <= 0 || deltaEp < 0) {
						proba = 1;
					}
					else	proba = Math.exp(-deltaE / (this.K.k * this.temperature));
					if (proba == 1 || proba >= probleme.gen.nextDouble()) {
						mutationsAcceptees++;
						probleme.modifElem(etat, mutation);	// faire la mutation
						System.out.println("Entrée dans proba == 1 || proba >= probleme.gen.nextDouble()");
						
						if (deltaEp < 0){
							EpActuelle = etat.Ep.calculer(etat);		// energie potentielle temporelle
							System.out.println("Ep Actuelle : " + EpActuelle);

							
							if( EpActuelle < this.meilleureEnergie ){		// mettre a jour la meilleur energie
								System.out.println("ACTUALISATION DE L ENERGIE : " + EpActuelle);

								this.meilleureEnergie = EpActuelle;
								System.out.println("meilleureEnergie = "+ this.meilleureEnergie);
								System.out.println("mutationsTentees = "+ mutationsTentees);
							}
						}
						
					}
				}

			}
		}


		System.out.println("Mutations tentées : " + mutationsTentees);
		System.out.println("Mutations acceptées : " + mutationsAcceptees);
		System.out.println("Meilleur Ep :"+ this.meilleureEnergie);
		System.out.println("Meilleur Poids :");
		Tools.printArray(((Portfolio)probleme.etats[0]).getWeights());
		double sum =0;
		for(int i = 0; i<((Portfolio)probleme.etats[0]).getWeights().length;i++){
			sum += ((Portfolio)probleme.etats[0]).getWeights()[i];
		}
		System.out.println("sum = "+sum);
		System.out.println("Expected Return :"+ ((Portfolio)probleme.etats[0]).computeExpectedReturn());
		this.mutationTentess=mutationsTentees;
		return;
	}

	public double getMeilleureEnergie() {
		return meilleureEnergie;
	}

	public int getMutationTentess() {
		return mutationTentess;
	}





}
