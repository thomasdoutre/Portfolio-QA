import java.text.ParseException;

import umontreal.iro.lecuyer.probdist.Distribution;
import umontreal.iro.lecuyer.probdist.EmpiricalDist;
import umontreal.iro.lecuyer.randvar.KernelDensityGen;
import umontreal.iro.lecuyer.randvar.NormalGen;
import umontreal.iro.lecuyer.randvar.RandomVariateGen;
import umontreal.iro.lecuyer.rng.MRG32k3a;
import recuit.*;


public class Main {

	public static void main(String[] args) throws ParseException {

		String[] tickers = {"GSPC","AAPL","FCHI","LG.PA","GSZ.PA","KER.PA","RNO.PA","AIR.PA"};
		double[] weights = {0.125,0.125,0.125,0.125,0.125,0.125,0.125,0.125};


		Portfolio portfolio = new Portfolio(tickers,weights);

/*		double[] obs = new double[100];
		double bandWidth = 0.004;

		MRG32k3a s = new MRG32k3a();
		EmpiricalDist dist = new EmpiricalDist(obs);
		//RandomVariateGen randomVariateGen = new RandomVariateGen(s,dist);
		NormalGen kGen = new NormalGen(s);
		KernelDensityGen kernelDensityGen = new KernelDensityGen(s,dist,kGen);
		//System.out.println(""+ KernelDensityGen.getBaseBandwidth(dist));
		kernelDensityGen.setBandwidth(KernelDensityGen.getBaseBandwidth(dist));
		Distribution distribution = kernelDensityGen.getDistribution();

		for(int i=0;i<300;i++){
			System.out.println(""+distribution.cdf(i*0.1));
		}*/
	}

}
