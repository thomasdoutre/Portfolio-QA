package modele;
import java.util.ArrayList;

import mutation.IMutation;
import parametrage.EnergieCinetique;
import parametrage.EnergiePotentielle;
import parametrage.ParametreGamma;
import parametrage.Temperature;
/**
 * 
 * 
 * 
 * Cette classe est clef dans la conception du recuit
 * C'est une particule a laquelle est liée la totalité du problème, energie cinétique et Etats spécifiés sur le problème.
 * 
 * Elle dépend aussi d'une seed qui va par la suite permettre de générer des nombres aléatoires 
 * 
 * 
 * 
 * 
 * 
 * 
 * @author BaptCav
 * 
 * 
 * 
 * 
 *
 */
public class Probleme extends Particule{

	// On rajoute de plus une seed de génération de nombres aléatoires
	private int seed;
	private EnergieCinetique energiecin;
	private EnergiePotentielle energiepot;
	// Fonctions Ã©lÃ©mentaires de calcul de l'energie de la particule
	private ParametreGamma gamma;

	
	public Probleme(){
		
	}
	
	public Probleme(ArrayList<Etat> etat,Temperature T,int seed,EnergieCinetique energiecin,EnergiePotentielle energiepot,ParametreGamma gamma){
		super(etat, T);
		this.seed=seed;
		this.energiecin=energiecin;
		this.energiepot=energiepot;
		this.gamma=gamma;
		
	}


	public Probleme clone(){
		ArrayList<Etat> e= this.getEtat();
		int n = this.etat.size();
		ArrayList<Etat> r = new ArrayList<Etat>(n);
		for(int i=0; i<n; i++){
			r.add(( (this.etat.get(i)).clone()));
		}
		Probleme p = new Probleme(r,this.getT(),this.seed,this.energiecin,this.energiepot,this.gamma);
		return p;
	}
	/**
	 * 
	 * @return
	 * L'energie totale de la particule
	 */
	public double calculerEnergie(){
		return this.calculerEnergieCinetique()+this.calculerEnergiePotentielle();
	}
	
	/**
	 * 
	 * @return
	 *L'energie cinétique associée au problème
	 */
	public double calculerEnergieCinetique(){
		return 	this.energiecin.calculer(this);
	}
	/**
	 * 
	 * @return
	 *L'energie potentielle totale de la particule
	 */
	public double calculerEnergiePotentielle(){
		double compteur=0;
		for (Etat i:this.etat){
			compteur +=this.energiepot.calculer(i);	
		}
		return compteur/this.etat.size();
	}


	public void modifElem(){
	}
	public void annulerModif(){
	}
	public void modifEtat(Etat e){
	}
	public void annulerModifEtat(Etat e){
	}

	public int getSeed(){
		return this.seed;
	}
	public void setSeed(int seed){
		this.seed=seed;
	}
/**
 * 
 * @return
 * la meilleure instance Etatique
 */
	public Etat getBest() {
		Etat best = this.etat.get(0);
		double min = energiepot.calculer(best);
		for(Etat i : this.etat){
			double j = energiepot.calculer((Routage)i);
			if(j<min){
				best= i;
				min =j;
			}
		}
		return best;

	}

	/**
	 * Best avoid ----- A recoder
	 * @param e
	 * etat a calculer
	 * @param m
	 * mutation pour calculer
	 * @return
	 * la difference locale de spin engendré par la mutation
	 */
	public int differenceSpins(Etat e,IMutation m){
		
			Etat now = e;
			Etat now2 = e.clone();
			
			Etat avant = now.getPrevious();
			Etat apres = now.getNext();
			
			m.faire(this,now2);
			
			int cptspin = now2.distanceIsing(avant) + now2.distanceIsing(apres) - now.distanceIsing(avant) - now.distanceIsing(apres);
			return cptspin;

	}

	public ParametreGamma getGamma(){
		return this.gamma;
	}
	public void majgamma(){
		this.gamma.refroidissementLin();
	}
	public void setGamma(ParametreGamma gamma){
		this.gamma=gamma;
	}
	
	public EnergieCinetique getEcin(){
		return energiecin;
	}
	public EnergiePotentielle getEpot(){
		return energiepot;
	}
	public void setEcin(EnergieCinetique ecin){
		this.energiecin=ecin;
	}
	
	public void setEpot(EnergiePotentielle epot){
		this.energiepot=epot;
	}
	
	
	/**
	 * Par defaut cree un Etat a NULL
	 * L'utilisateur DOIT IMPLEMENTER cett methode pour creer un Etat adapte a son probleme
	 *@return un etat créé aléatoirement
	 */
	

	public Etat creeEtatAleatoire(){
			return new Etat();
	}
	
}

