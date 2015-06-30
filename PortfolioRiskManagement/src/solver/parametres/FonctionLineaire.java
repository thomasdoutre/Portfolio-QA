package solver.parametres;

/**
 * A chaque appel de modification de la température, la température descend d'un pas constant.
 * Le nombre réel d'itérations est une approximation puisque le pas linéaire est un double é précision limitée.
 */
public class FonctionLineaire extends Fonction {

	/**
	 * Valeur approximée du pas(en négatif) pour descendre de la 
	 * température de début é celle de fin en le nombre d'itérations voulu.
	 */
	double pasLineaire;
	
	/**
	 * Création d'une variable pasLineaire.
	 */
	public FonctionLineaire(double tdebut, double tfinal, int nbIteration) {
		super(tdebut,tfinal,nbIteration); 
		this.pasLineaire = (this.Tfinal-this.Tdebut)/this.nbIteration ; 
}

	/**
	 * <p>
	 * Ici, on ajoute le pas é la température et on vérifie si la température résultante est inférieure é la température de fin.
	 * Pour le recuit quantique, modifierT() modifie Gamma, pas la température, on a donc des valeurs croissantes.
	 */
	public boolean modifierT() {
		if (this.t+this.pasLineaire > this.Tfinal) {
			return false;
		} else {
			this.t += this.pasLineaire; //pas lineaire positif
			return true;
		}
	}
}
