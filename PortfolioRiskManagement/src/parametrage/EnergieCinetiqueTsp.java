package parametrage;


import java.util.ArrayList;

import modele.Etat;
import modele.ParticuleTSP;
import modele.Routage;



public class EnergieCinetiqueTsp extends EnergieCinetique {
	
/**
 * Calcule les sommes de produits spiniques dans la particule à partir des matrices d'Ising de chaque réplique. Cela correspond à l'énergie cinétique, sans prendre en compte J
 * @param p
 * Particule sur laquelle on calcule la somme des produits spiniques
 * @return
 * La somme des produits spiniques entre répliques de la particule (Ecin sans J)
 */
public static double calculerCompteurSpinique(ParticuleTSP p){
		
		ArrayList<Etat> r = p.getEtat();
		int n = r.size();
		double compteurspinique=0;
		for(int i =0; i<n;i++){
			//// IL reste a coder le calcul particulier
			Etat e = r.get(i);
			Routage r1=(Routage) e;
			Routage r2=(Routage) e.getNext();
			
			//System.out.println(r1);
			int[][] Mi = r1.getIsing();
			int[][] Mj = r2.getIsing();
			for(int k =0;k<Mi.length-1;k++){
				for(int l =k+1;l<Mi.length;l++){
					compteurspinique+=Mi[k][l]*Mj[k][l];
				}
			}	
		
		}
		return compteurspinique;
	}
	
	/**
	 * Calcule l'énergie cinétique de la particule pour TSP. 
	 * @param p
	 * ParticuleTSP sur laquelle on calcule l'énergie cinétique
	 * @param J
	 * Ponderation qui entre dans le calcul de l'énergie cinétique
	 * @return
	 * Energie Cinétique de la particule
	 */
	public static double calculer(ParticuleTSP p, Ponderation J){
		int n = p.getEtat().size();
		return J.calcul(p.getT(),n)*calculerCompteurSpinique(p);
	}
	


}
