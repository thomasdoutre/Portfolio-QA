package io;
import java.util.Locale;
import java.util.Scanner;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class TSPParser {

	/* La classe LectureBenchmarks donne la matrice utilisée 
		pour créer le graphe servant au recuit.
		Elle permet la lecture des Benchmarks présentés sour forme de matrice
		et sous forme de coordonnées associées à des points de l'espace
	 */

	public static double[][] donneMatrice (String stringFichier) {


		/* Principe : Un bufferReader lira le fichier .tsp
			et formera une chaine de nombres
			Un scanner scannera cette chaine et on formera,
			selon le type de Benchmark utilisé, la matrice correspondante.
			Note : Les benchmarks se divisent en plusieurs groupes :
			@ les matrices d'entiers :
					1. définies par tous les coefficients (FULL_MATRIX)
					2. définies par la sous matrice triangulaire haut (UPPER_ROW) [Matrices symétriques]
					3. définies par la sous matrice triangulaire basse (LOWER_DIAG_ROW) [Matrices symétriques]
			@ les coordonnées entières ou doubles
			Note 2 : la lecture ne fonctionne pas pour les fichiers :
				brg180.tsp
				si175.tsp
				si535.tsp
				si1032.tsp
		 */


		BufferedReader br;
		Scanner scanner;

		// Le String thisLine stocke la ligne lue par le BufferReader
		String thisLine;

		//Les infos ci dessous seront déterminées par le texte précédent les données dans le fichier .tsp
		int dimension = 0;
		boolean FULL_MATRIX = false;	// true si la matrice est de type FULL_MATRIX
		boolean UPPER_ROW = false;		// true si la matrice est de type UPPER_ROW
		boolean UPPER_DIAG_ROW = false;		// true si la matrice est de type UPPER_DIAG_ROW
		boolean LOWER_DIAG_ROW = false;		// true si la matrice est de type LOWER_ROW
		boolean EUC_2D = false;		// true si la matrice est de type EUC_2D
		boolean CEIL_2D = false;		// true si la matrice est de type CEIL_2D
		boolean GEO = false;		// true si la matrice est de type GEO

		/*
			Note : Si les 3 derniers booléens restent faux, cela indique que les données sont sous forme
			de points et coordonnees.
		 */


		try {


			br = new BufferedReader(new InputStreamReader(new FileInputStream(stringFichier)));

			// On stocke les données utiles (coefficients de matrice ou coordonnées dans "chaine")
			String chaine ="";

			// Définit une condition d'arrêt de la lecture du Benchmark
			boolean doitSArreter = false;



			while (((thisLine = br.readLine()) != null)&&!(doitSArreter)) {

				//On lit les lignes une a une et on réalise une disjonction de cas



				/* les benchmarks se terminent par "EOF" ou, dans le cas où la matrice ET les
					coordonnees sont données, elles sont séparées par "DISPLAY_DATA_SECTION"
				 */
				if(thisLine.startsWith("DISPLAY_DATA_SECTION")||thisLine.startsWith("EOF")){
					doitSArreter = true;
				}


				/*On définit un booleen "ligneDeLaMatrice" qui indique si les données sont des
					coefficients de matrice ou des coordonnées */
				boolean ligneDeLaMatrice = (thisLine.startsWith(" "));
				for(int j=0; j < 10 ; j++){
					ligneDeLaMatrice = ligneDeLaMatrice || thisLine.startsWith(""+j);
				}

				// On ajoute la ligne de données à "chaine"
				if (ligneDeLaMatrice){

					chaine = chaine + " " +thisLine;
				} 

				// La disjonction de cas détermine : la dimension et le type de données.
				if(thisLine.startsWith("DIMENSION :")){
					dimension = Integer.parseInt(thisLine.replace("DIMENSION : ", ""));
					System.out.println("dimension = " +dimension);
				}

				if(thisLine.startsWith("DIMENSION:")){
					dimension = Integer.parseInt(thisLine.replace("DIMENSION: ", ""));
					System.out.println("dimension = " +dimension);
				}

				if(creeBooleen(thisLine,"LOWER_DIAG_ROW")){
					LOWER_DIAG_ROW = true;
					System.out.println("Type : LOWER_DIAG_ROW");
				}

				if(creeBooleen(thisLine,"FULL_MATRIX")){
					FULL_MATRIX = true;
					System.out.println("Type : FULL_MATRIX");
				}

				if(creeBooleen(thisLine,"UPPER_ROW")){
					UPPER_ROW = true;
					System.out.println("Type : UPPER_ROW");
				}

				if(creeBooleen(thisLine,"UPPER_DIAG_ROW")){
					UPPER_DIAG_ROW = true;
					System.out.println("Type : UPPER_DIAG_ROW");
				}

				if(creeBooleen(thisLine,"EUC_2D")){
					EUC_2D = true;
					System.out.println("Type : EUC_2D");
				}

				if(creeBooleen(thisLine,"CEIL_2D")){
					CEIL_2D = true;
					System.out.println("Type : CEIL_2D");
				}

				if(creeBooleen(thisLine,"GEO")){
					GEO = true;
					System.out.println("Type : GEO");
				}

			}


			// Fermeture du bufferReader
			br.close();


			// On va scanner "chaine" et répartir les éléments comme souhaités

			scanner = new Scanner(chaine);

			/* A ce stade, on crée une matrice "matrice" qui va :
					@ contenir tous les éléments si la matrice est de type FULL_MATRIX
					@ contenir seulement les éléments scannés si la matrice est du type
						UPPER_ROW ou LOWER_DIAG_ROW (la matrice à rendre (appellée plus tard "mat" 
						se déduira d'opérations de transposition et d'ajout)
					@ contenir les distances euclidiennes déduites des coordonnées si les données
				 		sont sous forme de points.
			 */

			double[][] matrice = new double[dimension][dimension];

			//Disjonction de cas selon le type

			if(LOWER_DIAG_ROW){

				for(int j=0; j < dimension ; j++){
					for (int i=0; i <= j ; i++){
						if (scanner.hasNextInt()) {
							matrice[j][i] = (double)scanner.nextInt();
						}
					}

				}

				double[][] mat = MatrixAddDouble(matrice,transposeMatrixDouble(matrice));

				// On affiche cette matrice (cf méthode printApproxMatrix plus bas)
				// printApproxMatrix(mat);

				// Une fois qu'on obtient la matrice, on ferme le scanner et on retourne cette matrice
				scanner.close();
				return mat;


			}

			else if(FULL_MATRIX)  {

				for(int j=0; j < dimension ; j++){
					for (int i=0; i < dimension ; i++){
						if (scanner.hasNextInt()) {
							matrice[j][i] = (double)scanner.nextInt();
						}
					}
				}


				// printApproxMatrix(matrice);

				scanner.close();
				return matrice;

			}

			else if(UPPER_ROW){


				for(int j=0; j < dimension ; j++){
					for (int i=j+1; i <dimension ; i++){
						if (scanner.hasNextInt()) {
							matrice[j][i] = (double)scanner.nextInt();
						}
					}

				}



				double[][] mat = MatrixAddDouble(matrice,transposeMatrixDouble(matrice));

				// printApproxMatrix(mat);

				scanner.close();
				return mat;	

			}

			else if(UPPER_DIAG_ROW){


				for(int j=0; j < dimension ; j++){
					for (int i=j; i <dimension ; i++){
						if (scanner.hasNextInt()) {
							matrice[j][i] = (double)scanner.nextInt();
						}
					}

				}

				double[][] mat = MatrixAddDouble(matrice,transposeMatrixDouble(matrice));

				printApproxMatrix(mat);

				scanner.close();
				return mat;	

			}




			else if (EUC_2D){

				System.out.println("Type : EUC_2D");

				double[][] matriceDesCoordonnees = new double[dimension][3];

				System.out.println("dimension = " +dimension);

				// On définit ce qu'est un double pour le scanner
				scanner.useLocale(Locale.US);

				for(int j=0; j < dimension ; j++){
					for (int i=0; i <3 ; i++){

						if (scanner.hasNextDouble()) {
							matriceDesCoordonnees[j][i] = (double)scanner.nextDouble();
						}
					}
				}

				for (int i=0; i<dimension;i++){
					for (int j=0; j<dimension;j++){
						double x = matriceDesCoordonnees[i][1]-matriceDesCoordonnees[j][1];
						double y = matriceDesCoordonnees[i][2]-matriceDesCoordonnees[j][2];
						matrice[i][j] = Math.sqrt(x*x+y*y);
					}
				}

				scanner.close();
				return matrice;	
				// Affichage de la matrice
				//printApproxMatrix(matrice);

			}

			else if (CEIL_2D){

				System.out.println("Type : CEIL_2D");

				double[][] matriceDesCoordonnees = new double[dimension][3];

				System.out.println("dimension = " +dimension);

				// On définit ce qu'est un double pour le scanner
				scanner.useLocale(Locale.US);

				for(int j=0; j < dimension ; j++){
					for (int i=0; i <3 ; i++){

						if (scanner.hasNextDouble()) {
							matriceDesCoordonnees[j][i] = (double)scanner.nextDouble();
						}
					}
				}

				for (int i=0; i<dimension;i++){
					for (int j=0; j<dimension;j++){
						double x = matriceDesCoordonnees[i][1]-matriceDesCoordonnees[j][1];
						double y = matriceDesCoordonnees[i][2]-matriceDesCoordonnees[j][2];
						matrice[i][j] = Math.ceil(Math.sqrt(x*x+y*y));
					}
				}

				scanner.close();
				return matrice;	

			}

			else if (GEO){

				System.out.println("Type : GEO");

				double[][] matriceDesCoordonnees = new double[dimension][3];

				System.out.println("dimension = " +dimension);

				// On définit ce qu'est un double pour le scanner
				scanner.useLocale(Locale.US);

				for(int j=0; j < dimension ; j++){
					for (int i=0; i <3 ; i++){

						if (scanner.hasNextDouble()) {
							matriceDesCoordonnees[j][i] = (double)scanner.nextDouble();
						}
					}
				}

				for (int i=0; i<dimension;i++){
					for (int j=0; j<dimension;j++){
						double xi = matriceDesCoordonnees[i][1];
						double xj = matriceDesCoordonnees[j][1];
						double yi = matriceDesCoordonnees[i][2];
						double yj = matriceDesCoordonnees[j][2];
						double deg = Math.floor(xi);
						double min = xi - deg;
						double latitudeI = Math.PI * ( deg + 5.0 * min / 3.0 ) / 180.0;

						deg = Math.floor(yi);
						min = yi - deg;
						double longitudeI = Math.PI * ( deg + 5.0 * min / 3.0 ) / 180.0;

						deg = Math.floor(xj);
						min = xj - deg;
						double latitudeJ = Math.PI * ( deg + 5.0 * min / 3.0 ) / 180.0;

						deg = Math.floor(yj);
						min = yj - deg;
						double longitudeJ = Math.PI * ( deg + 5.0 * min / 3.0 ) / 180.0;

						double RRR = 6378.388;
						double q1 = Math.cos( longitudeI - longitudeJ );
						double q2 = Math.cos( latitudeI - latitudeJ );
						double q3 = Math.cos( latitudeI + latitudeJ );
						matrice[i][j] = (int) Math.round( (RRR*Math.acos(0.5*((1.0+q1)*q2-(1.0-q1)*q3))+1.0) );


					}
				}

				// Affichage de la matrice
				//printApproxMatrix(matrice);

			}

			else {
				System.out.println("type non reconnu");
			}


			scanner.close();
			return matrice;


			// Gestion des exceptions
		} catch (IOException e) {
			System.err.println("Error: " + e);
		} catch (NumberFormatException e) {
			System.err.println("Invalid number");
		}

		return null;

	}



	// Transpose une matrice d'entiers
	public static int[][] transposeMatrixInt(int [][] m){
		int[][] temp = new int[m[0].length][m.length];
		for (int i = 0; i < m.length; i++)
			for (int j = 0; j < m[0].length; j++)
				temp[j][i] = m[i][j];
		return temp;
	}

	// Transpose une matrice de double
	public static double[][] transposeMatrixDouble(double [][] m){
		double[][] temp = new double[m[0].length][m.length];
		for (int i = 0; i < m.length; i++)
			for (int j = 0; j < m[0].length; j++)
				temp[j][i] = m[i][j];
		return temp;
	}

	// Effectue la somme de 2 matrices d'entiers
	public static int[][] MatrixAddInt(int[][] A, int[][] B)
	{
		int[][]C = new int[A.length][A[0].length];

		for(int i =0; i < A.length; i++)
		{
			for(int j=0; j < A[i].length;j++)
			{
				C[i][j] = A[i][j] + B[i][j]; 
			}
		}
		return C;
	}

	// Effectue la somme de 2 matrices de doubles
	public static double[][] MatrixAddDouble(double[][] A, double[][] B)
	{
		double[][]C = new double[A.length][A[0].length];

		for(int i =0; i < A.length; i++)
		{
			for(int j=0; j < A[i].length;j++)
			{
				C[i][j] = A[i][j] + B[i][j]; 
			}
		}
		return C;
	}

	// Affiche correctement une matrice (pour plus de lisibilité, on approxime aux entiers avec le transtypage)
	public static void printApproxMatrix(double[][] twoDm) {
		System.out.println("=================================================================");
		for(double[] row : twoDm) {
			for (double i : row) {
				System.out.print((int)i);
				System.out.print("\t");
			}
			System.out.println();
		}
		System.out.println("=================================================================");

	}

	public static boolean creeBooleen(String thisLine, String type){
		return ((thisLine.startsWith("EDGE_WEIGHT_FORMAT")) && (thisLine.replace("EDGE_WEIGHT_FORMAT: ", "").startsWith(type)||thisLine.replace("EDGE_WEIGHT_FORMAT : ", "").startsWith(type))||(thisLine.startsWith("EDGE_WEIGHT_TYPE") && (thisLine.replace("EDGE_WEIGHT_TYPE: ", "").startsWith(type)||thisLine.replace("EDGE_WEIGHT_TYPE : ", "").startsWith(type))));
	}
}
