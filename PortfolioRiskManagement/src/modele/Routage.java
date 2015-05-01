package modele;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import mutation.IMutation;
import mutation.TwoOptMove;

import parametrage.EnergiePotentielleTsp;
public class Routage extends Etat {





	EnergiePotentielleTsp epot= new EnergiePotentielleTsp();

	ArrayList<Integer> route ;
	private int taille;
	Graphe g;
	int[][] ising;
	public ArrayList<Integer> getRoute(){
		return this.route;
	}
	public int tailleRoute() {
		return this.route.size();
	}
	public Routage (Graphe g){
		this.g = g;
		this.route=routeInitiale();
		this.updateIsing();
		this.taille=route.size();
	}
	public void setIsing(int[][] ising){
		this.ising =ising ;
	}

	public int[][] getIsing(){
		return this.ising;
	}


	public Routage (Graphe g, ArrayList<Integer> liste){
		this.g=g;
		this.route=liste;
		this.updateIsing();
	}
	public ArrayList<Integer> routeInitiale() {
		int n = this.g.nombreDeNoeuds();
		ArrayList<Integer> liste = new ArrayList<Integer>();
		for (int index = 0; index < n; index++) {
			liste.add(new Integer(index));
		}
		// Réorganise aléatoirement l'ordre de visite
		Collections.shuffle(liste);
		return liste;
	}
	public Routage clone()
	{
		Routage clone= new Routage(this.g);
		int n = this.g.nombreDeNoeuds();
		ArrayList<Integer> l =clone.getRoute();
		for (int index = 0; index < n; index++){
			l.set(index, this.route.get(index));
		}

		// On clone la matrice d'Ising
		int[][] m = new int[n][n];
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				m[i][j] = this.ising[i][j];
			}
		}
		clone.setIsing(m);
		return clone;
	}

	public EnergiePotentielleTsp getE(){
		return this.epot;
	}

	public Graphe getGraphe(){
		return this.g;
	}
	public int getNextIndex(int index){
		if (index==(this.tailleRoute()-1)) {
			return 0;
		} else {
			return (index+1);
		}
	}
	public int getPreviousIndex(int index){
		if (index==0) {
			return (this.tailleRoute() - 1);
		} else {
			return (index-1);
		}
	}
	public String toString() {
		int n = this.tailleRoute();
		String s = "";
		for (int index = 0; index < n; index++){
			s += this.route.get(index).intValue() + "->";
		}
		return s;
	}

	public double getEnergie(){
		double cpt=0.0;
		int j=0;
		int L = this.tailleRoute();
		for(int i=0;i<L;i++){
			j=this.getNextIndex(i);
			cpt+=this.getGraphe().getdists()[this.getRoute().get(i)][this.getRoute().get(j)];
		}
		return cpt;
	}


	//Rend la representation d'Ising du routage

	//Renvoie la valeur d'Ising pour deux noeuds fournis
	public int valueIsing(int i, int j){
		if (i<j) return this.ising[i][j];
		if (j<i) return this.ising[j][i];
		return 0;
	}

	//Cette fonction met à jour la matrice d'Ising en connectant les noeuds i et j. Condition : i != j
	public void connect(int i, int j){
		if (i<j) this.ising[i][j] = 1;
		if (j<i) this.ising[j][i] = 1;
	}

	//Fonction disconnect
	public void disconnect(int i, int j){ 
		if (i<j) this.ising[i][j] = -1;
		if (j<i) this.ising[j][i] = -1;
	}

	//Mise à jour de la matrice d'Ising et renvoi de cette matrice
	public int[][] updateIsing(){
		int n = this.tailleRoute();
		ArrayList<Integer> r= this.getRoute();
		int now;
		int next;
		int[][] m = new int[n][n];

		//On passe à 1 les noeuds lies entre eux
		for(int i =0; i<n;i++){
			now= r.get(i);
			next = r.get(getNextIndex(i));
			if (now<next){
				m[now][next]=1;
			} else {
				m[next][now]=1;
			}
		}
		//S'ils ne sont pas lies dans la route, on met -1
		for (int k=0; k<n-1; k++){
			for (int l=k+1; l<n; l++){
				if (m[k][l] != 1) m[k][l] = -1;
			}
		}
		this.ising = m;
		return m;

	}

	//Calcule le produit spinique entre deux matrices d'Ising
	public int distanceIsing(Routage autre){
		int compteurspinique = 0;
		int[][] Mi = this.getIsing();
		int[][] Mj = autre.getIsing();
		for(int k =0;k<Mi.length-1;k++){
			for(int l =k+1;l<Mi.length;l++){
				compteurspinique+=Mi[k][l]*Mj[k][l];
			}
		}
		return compteurspinique;
	}

	//Affiche le pourcentage de similarite entre 2 routes. Si on renvoie 100, elles sont complètement similaires. Si on renvoie 0, elles sont complètement differentes.
	public int pourcentageSimilarite(Routage autre){
		int n = this.tailleRoute();
		int rapprochement = this.distanceIsing(autre);
		int limitesup = n*(n-1)/2;
		int limiteinf = n*(n-9)/2;
		int differenceTotale = limitesup - limiteinf;
		int difference = rapprochement - limiteinf;
		return ((difference*100)/differenceTotale);
	}

	//Affiche la matrice d'Ising de la route. Utile pour vérifications
	public void afficheIsing(){
		int[][] M = this.getIsing();
		for(int k =0;k<M.length;k++){
			for(int l =0;l<M.length;l++){
				System.out.print(M[k][l] + " , ");
			}
			System.out.println("");
		}
	}

	public int getTaille(){
		return this.taille;
	}



}
