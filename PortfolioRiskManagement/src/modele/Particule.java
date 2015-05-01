package modele;
import java.util.ArrayList;

import parametrage.Temperature;
/**
 * 
 * @author BaptCav
 *
 */
public class Particule {
	ArrayList<Etat> etat;
	Temperature T;
	

	
	public Particule(ArrayList<Etat> etat,Temperature T){
	
		this.setEtat(etat);
		this.T=T;
	}
	public Particule(){
		
	}
	/**
	 * 
	 * @return
	 * La température (fixe) de la particule
	 */
	public Temperature getT(){
		return this.T;
	}
	/**
	 * 
	 * @param t
	 * 	va fixer la température de la particule a partir de la classe Temperature
	 */
	public void setT(Temperature t){
		this.T=t;
	}
	/**
	 * 
	 * @param value
	 * va fixer la température de la particule a partir d'un double
	 */
	public void setT(double value){
		Temperature temp = new Temperature(value);
		this.T = temp;
	}
	/**
	 * 
	 * @return
	 * la liste des états composant la particule
	 */
	public ArrayList<Etat> getEtat(){
		return this.etat;
	}
	/**
	 * 
	 * @return
	 * le nombre d'etats total
	 */
	public int nombreEtat(){
		return etat.size();
	}
	/**
	 * Clone la particule 
	 */
	public Particule clone(){
		int n = this.etat.size();
		ArrayList<Etat> r = new ArrayList<Etat>(n);
		for(int i=0; i<n; i++){
			r.add(( (this.etat.get(i)).clone()));
		}
		 Particule p = new Particule(r,this.T) ;
		 
		return p;
		
	}
	/**
	 * Set la liste d'etats en effectuant le bouclage au passage
	 * @param e
	 * la liste d'etat
	 */
	public void setEtat( ArrayList<Etat> e){
		this.etat= e;
		for( int i =1;i< e.size()-1; i++){
			e.get(i).setprevious(e.get(i-1));
			e.get(i).setnext(e.get(i+1));
		}
		e.get(e.size()-1).setprevious( e.get(e.size()-2));
		e.get(e.size()-1).setnext( e.get(0));
		e.get(0).setprevious( e.get(e.size()-1));
		e.get(0).setnext(e.get(1));
	}
	/**
	 * 
	 * @param index
	 * l'int de position
	 * @return
	 * i+1 dans la boucle cad (i+1)%Nombreetat
	 */
	public int getNextIndex(int index){
		if (index==(this.getEtat().size()-1)) {
			return 0;
		} else {
			return (index+1);
		}
	}
	/**
	 * 
	 * @param index
	 * l'int de position
	 * @return
	 * i-1 dans la boucle cad (i-1)%Nombreetat
	 */
	public int getPreviousIndex(int index){
		if (index==0) {
			return (this.getEtat().size() - 1);
		} else {
			return (index-1);
		}
	}
}
