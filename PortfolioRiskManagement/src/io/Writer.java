package io;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class Writer {

	public static PrintWriter energie;
	public static PrintWriter proba;
	
	public static void ecriture (int compteur, double nombre, PrintWriter printWriter) throws IOException{
		
		printWriter.print(nombre);
		printWriter.println();
		

	}
	
	public static void creerFichierProba() throws IOException{
		PrintWriter printProba = new PrintWriter(new FileWriter("Proba.txt"));
		proba = printProba;	
	}
	
	public static void creerFichierEnergie(int i) throws IOException{
		PrintWriter sortie = new PrintWriter(new FileWriter("EnergieIteration"+ i +".txt"));
		energie = sortie;	
	}
	
}
