package modele;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import mutation.IMutation;
import mutation.TwoOptMove;
import parametrage.EnergiePotentielle;
/**
 * Un etat a par défaut ses voisins definis a NULL, il doit etre modifié par l'utilisateur.
 * L'utilisateur se devra d'instancier Etat en une classe correspondant à son probleme.
 * 
 */
public class Etat {
	
	Etat previous;
	Etat next;
	
	
	
	EnergiePotentielle epot;
	
	public Etat(){
		
	}
	public Etat(EnergiePotentielle epot){
		this.epot=epot;
	}
	
	/**
	 * 
	 * @return
	 * l'energie potentielle en tant que CLASS instanciée pour faciliter la représentation
	 */
	public EnergiePotentielle getE(){
		return this.epot;
	}
	

	public void setprevious(Etat etat) {
		this.previous=etat;
		
	}

	public void setnext(Etat etat) {
		this.next=etat;		
	}
	/**
	 * Clone l'etat
	 */
	public Etat clone(){
	Etat e = new Etat(this.epot);
	return e;
	}
	/**
	 * @return
	 * L'etat suivant dans la chaine liant les états
	 */
	
	public Etat getNext(){
		return this.next;
	}
	/**
	 * @return
	 * L'etat précédent dans la chaine liant les états
	 */
	public Etat getPrevious(){
		return this.previous;
	}
	/**
	 * 
	 * @return
	 * L'energie potentielle de l'etat
	 */
	public double getEnergie(){
		return this.epot.calculer(this);
	}
	
	/**
	 * L'utilisateur devra implementer la methode appropriée a son "type" d'etat
	 * retourne 0 par défaut
	 * @param e
	 * Etat a calculer
	 * @return
	 * l'entier de la somme de spin
	 */
	public int distanceIsing(Etat e){

		
		return 0;
	}
	

	/**
	 * Fonction dont le but est de construire une liste des deltaepot engendrés par une mutation élémentaire
	 * @param p
	 * instance du probleme étudié
	 * @param m
	 * mutation qui va induire les deltaEpot
	 * @param longueurListeVoisins
	 * Longueur desirée de la liste
	 * @return
	 * La liste des deltaEpot
	 */
	public List<Double> buildListeVoisins(Probleme p,IMutation m,int longueurListeVoisins){

		double deltaE = -1;
		List<Double> l = new LinkedList<Double>();
		for (int i =0; i < longueurListeVoisins; i++){
			deltaE = -1;
			while(deltaE<=0){
				m.maj();
				deltaE=m.calculer(p,this);

			}
			l.add(deltaE);
		}
		Collections.sort(l);
		return l;
	}
}
