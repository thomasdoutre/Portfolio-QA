import java.util.Calendar;
import java.util.GregorianCalendar;


public class Main {

	public static void main(String[] args) {

		System.out.println("PO-VersionSimple");
		String[] tickers = {/*"GSPC",*/"AAPL","FCHI","LG.PA","GSZ.PA","KER.PA","RNO.PA","AIR.PA"/*,"OMXC20.CO"*/,"DG.PA","UG.PA"};
		Calendar startCalendar = new GregorianCalendar(2010,0,27);
		Calendar endCalendar = Calendar.getInstance();

		TickersSet tickersSet = new TickersSet(tickers);
		Data data = new Data(tickersSet,startCalendar,endCalendar);
		tickersSet.setData(data);

		/*System.out.println("SA -----");
		for (int i = 0; i < 100; i++) {

			System.out.print((0.01+0.001*i) + " ; ");
		RecuitSimule recuitSimule = new RecuitSimule(1000,0.1,100000,tickersSet);
		recuitSimule.run(0.01,(0.01+0.001*i));

		}*/
		
		System.out.println("QA -----");
		for (int i = 0; i < 300; i++) {

			System.out.print((0.01+0.001*i) + " ; ");
			RecuitQuantique recuitQuantique = new RecuitQuantique(12500, 0.00001, 10000, tickersSet, 500);
			recuitQuantique.run(0.1, (0.01+0.001*i),10);

		}
		/*for (int j = 6; j < 7; j++) {


		System.out.println("QA ----- nbIt = "+ Math.pow(10, j));*/
		/*for (int i = 0; i < 100; i++) {

			RecuitQuantique recuitQuantique = new RecuitQuantique(12500, 0.00001, 10000, tickersSet, 500);
			recuitQuantique.run(0.1, 0.09,10);

		}*/

		//}


	}

}
