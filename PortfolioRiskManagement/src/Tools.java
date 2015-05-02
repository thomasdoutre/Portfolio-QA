
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
	 * This method is used to display a matrix of double is the console.
	 * @param twoDm This is the matrix to be displayed
	 */

	public static void printArray(double[] v) {
		System.out.println("=================================================================");
		for (double i : v) {
			System.out.print(i);
			System.out.print("\t");
		}
		System.out.println("=================================================================");

	}
	
}
