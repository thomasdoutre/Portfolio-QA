package Optionnel;


/**
 * This class includes some useful methods
 * @author  Thomas Doutre
 * @version 1.0
 * @since   2015-05-01
 */

public class Tools {


	/**
	 * This method is used to display a matrix of double is the console.
	 * @param twoDm This is the matrix to be displayed
	 */

	public static void printMatrix(double[][] twoDm) {
		System.out.println("=================================================================");
		for(double[] row : twoDm) {
			for (double i : row) {
				System.out.print(i);
				System.out.print("\t");
			}
			System.out.println();
		}
		System.out.println("=================================================================");

	}

	/**
	 * This method is used to display an array of double is the console.
	 * @param v This is the array to be displayed
	 */

	public static void printArray(double[] v) {
		System.out.println("=================================================================");
		for (double i : v) {
			System.out.print(i);
			System.out.print("\t");
		}
		System.out.println("=================================================================");

	}
	
	/**
	 * This method is used to clone an array of double.
	 * @param array the array to be cloned
	 * @return the cloned array.
	 */
	public static double[] cloneArray(double[] array){
		double[] cloned = new double[array.length];
		for(int i=0; i<array.length;i++){
			cloned[i]=array[i];
		}
		return cloned;
	}
	
}
