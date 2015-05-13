import modele.Routage;
import parametrage.EnergieCinetique;
import modele.Etat;
import mutation.IMutation;
import mutation.TwoOptMove;


public class PortfolioDistance extends EnergieCinetique {
	
	
	
	
	public static double calculer(Particule p){	
		return 0;
	}
	

	public double calculerDeltaE(Etat etat, Etat prev, Etat next,IMutation mutation) {
		Portfolio portfolio=(Portfolio) etat;
		Portfolio portfolionext=(Portfolio) next;
		Portfolio portfolioprev=(Portfolio) prev;
		
		SwapAssets mutationAssets = (SwapAssets) mutation;
		
		int longueur = portfolio.getWeights().length;
	
		double EcPrecedent =0;
		
		double[] poidsnext=portfolionext.getWeights();
		double[] poids=portfolio.getWeights();
		double[] poidsprev=portfolioprev.getWeights();
		
		for (int i=0;i<longueur;i++) {
			EcPrecedent=+Math.pow(poidsnext[i]-poids[i], 2)+Math.pow(poidsprev[i]-poids[i], 2);
		}
		
		
		
		
		
		
		
		double EcSuivant= Math.pow(position2Dnext.getX()- position2D.getX()-mutation2D.deltaX,2)+Math.pow(position2Dnext.getY()- position2D.getY()-mutation2D.deltaY,2)+
				(Math.pow(position2Dprev.getX()- position2D.getX()-mutation2D.deltaX,2)+Math.pow(position2Dprev.getY()- position2D.getY()-mutation2D.deltaY,2));
		
		double deltaE=EcSuivant-EcPrecedent;
		double signe = Math.signum(deltaE);
	
		double DeltaEc=signe*Math.sqrt(signe*deltaE);
		
		return -DeltaEc;
		
	}
	
	
}
