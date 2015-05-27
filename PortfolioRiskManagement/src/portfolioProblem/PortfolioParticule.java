package portfolioProblem;

import solver.commun.EnergieCinetique;
import solver.commun.EnergiePotentielle;
import solver.commun.Etat;
import solver.commun.IMutation;
import solver.commun.Probleme;

public class PortfolioParticule extends Probleme {

	int Nombrerepliques;
	
	
	
	
	
	public PortfolioParticule(EnergiePotentielle Ep, IMutation mutation, EnergieCinetique Ec, int nombrerepliques,TickersSet tickersSet, double[] weights,double[] returns) {
		this.Ec = Ec;
		this.mutation = mutation;
		this.Nombrerepliques = nombrerepliques;
		
		this.etats = new Etat[Nombrerepliques];
		for (int i = 0; i < this.Nombrerepliques; i++){
			this.etats[i] = new Portfolio(Ep , tickersSet, weights, returns );
		}
	}

	@Override
	public void initialiser() {
		for (Etat portfolio : etats){
			( (Portfolio) portfolio).initialiser();
		}
		
	}

	@Override
	public void sauvegarderSolution() {
		// TODO Auto-generated method stub
		for (Etat portfolio : etats){
			( (Portfolio) portfolio).sauvegarderSolution();
		}
	}

	public int getNombrerepliques() {
		return Nombrerepliques;
	}

	public void setNombrerepliques(int nombrerepliques) {
		Nombrerepliques = nombrerepliques;
	}

	
	


}
