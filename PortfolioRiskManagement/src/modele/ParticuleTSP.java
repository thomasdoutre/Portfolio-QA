package modele;
import java.util.ArrayList;








import mutation.IMutation;
import mutation.TwoOptMove;
import parametrage.EnergieCinetique;
import parametrage.EnergieCinetiqueTsp;
import parametrage.EnergiePotentielle;
import parametrage.EnergiePotentielleTsp;
import parametrage.ParametreGamma;
import parametrage.Ponderation;
import parametrage.Temperature;


public class ParticuleTSP extends Probleme {
	Graphe g;
	
	public ParticuleTSP(Graphe g, ArrayList<Etat> etat){
		/**
		 * le constructeur initial les autres valeurs sont "setables"
		 **/
	this.g=g;
	this.etat=etat;
	}
	
	public ParticuleTSP(Graphe g, ArrayList<Etat> etat,Temperature T,int seed,EnergieCinetiqueTsp energiecin,EnergiePotentielleTsp energiepot,ParametreGamma gamma){
		super(etat,T,seed,energiecin,energiepot,gamma);
		this.g=g;
	}
	
	public Graphe getGraphe(){
		return this.g;
	}
	
	
	public double calculerEnergiePotentielle(){
		double compteur=0;
		for (Etat i:this.etat){
			compteur +=EnergiePotentielleTsp.calculer((Routage)i);	
		}
		return compteur/this.etat.size();
	}
	//Amelioration calculs
	public double[] tableauDistances(){
		int p = this.etat.size();
		double[] tab = new double[p];
		for (int i = 0; i < p; i++){
			tab[i] = EnergiePotentielleTsp.calculer((Routage)this.etat.get(i));	
		}
		return tab;
	}
	 //Universel mais ne marche pas, à corriger

	
	//Moins de calcul que la methode precedente
	@Override
	public int differenceSpins(Etat e,IMutation mutation){
		int cptspin = 0;
		TwoOptMove m = (TwoOptMove) mutation;
		int i = m.getI();
		int j = m.getJ();
		
		Routage now = (Routage) e;
		
		
		//Routes concernees, voisines de index dans la particule
		Routage avant = (Routage) e.getPrevious();
		Routage apres = (Routage) e.getNext();
		
		
		int NodeI  = now.getRoute().get(i);
		int NodeBeforeI = now.getRoute().get(now.getPreviousIndex(i));
		int NodeJ = now.getRoute().get(j);
		int NodeAfterJ = now.getRoute().get(now.getNextIndex(j));
		
		if(now.getNextIndex(j)!=i){
			cptspin -= avant.valueIsing(NodeBeforeI,NodeI) + apres.valueIsing(NodeBeforeI,NodeI);
			cptspin -= avant.valueIsing(NodeAfterJ,NodeJ) + apres.valueIsing(NodeAfterJ,NodeJ);
			cptspin += avant.valueIsing(NodeBeforeI,NodeJ) + apres.valueIsing(NodeBeforeI,NodeJ);
			cptspin += avant.valueIsing(NodeAfterJ,NodeI) + apres.valueIsing(NodeAfterJ,NodeI);
		}
		// Avec un facteur 2 car un passage de 1 à -1 decremente le compteur spinique de 2
		return (2*cptspin);
	}
	
	
	
	//Calcul Ecin sans J
	public double calculerCompteurSpinique(){
		return EnergieCinetiqueTsp.calculerCompteurSpinique(this);
	}
	
	//Calcul Ecin
	public double calculerEnergieCinetique(){
		return this.calculerCompteurSpinique();
	}
	

	
	
	public ArrayList<Etat> getEtat(){
		return this.etat;
	}
	
	
	
	
	public Temperature getTemperature() {
		// TODO Auto-generated method stub
		return this.getT();
	}
	
	
	public ParticuleTSP clone(){
		int n = this.etat.size();
		ArrayList<Etat> r = new ArrayList<Etat>(n);
		for(int i=0; i<n; i++){
			r.add(((Routage) this.etat.get(i)).clone());
		}
		 ParticuleTSP p = new ParticuleTSP(this.g,r,this.getT(), this.getSeed(),(EnergieCinetiqueTsp) this.getEcin(),(EnergiePotentielleTsp) this.getEpot(),this.getGamma()) ;
		 p.setT(this.getT());
		return p;
		
	}
	public void setRoutage(ArrayList<Etat> e) {
		// TODO Auto-generated method stub
		this.setEtat(e);
		
	}
	public Etat creeEtatAleatoire(){
		return new Routage(this.g);
	}
}
