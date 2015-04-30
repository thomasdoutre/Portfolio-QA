import umontreal.iro.lecuyer.probdist.Distribution;
import umontreal.iro.lecuyer.probdist.EmpiricalDist;
import umontreal.iro.lecuyer.randvar.KernelDensityGen;
import umontreal.iro.lecuyer.randvar.RandomVariateGen;
import umontreal.iro.lecuyer.rng.MRG32k3a;
import recuit.*;


public class Main {

	public static void main(String[] args) {
		
		String[] symbols = {"GSPC","AAPL"};
		YahooData yahooData = new YahooData(symbols);
		
			
			double[] obs = new double[100];
			double bandWidth = 0.004;
			
			MRG32k3a s = new MRG32k3a();
			EmpiricalDist dist = new EmpiricalDist(obs);
			RandomVariateGen randomVariateGen = new RandomVariateGen(s,dist);
			KernelDensityGen kernelDensityGen = new KernelDensityGen(s,dist,randomVariateGen,bandWidth);
			//System.out.println(""+ KernelDensityGen.getBaseBandwidth(dist));
			kernelDensityGen.setBandwidth(KernelDensityGen.getBaseBandwidth(dist));
			Distribution distribution = kernelDensityGen.getDistribution();
			
			for(int i=0;i<300;i++){
			System.out.println(""+distribution.cdf(i*0.1));
			}
	}

}
