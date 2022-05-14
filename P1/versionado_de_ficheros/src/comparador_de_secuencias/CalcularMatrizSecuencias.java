package comparador_de_secuencias;

import java.util.ArrayList;

public class CalcularMatrizSecuencias {
	public int [][]matrizCoste;
	public Instruccion [][]matrizOperaciones;
	
	public CalcularMatrizSecuencias() {
		
	}
	
	private int min(int x, int y, int z) {
		if(x<=y && x<=z) {
			return x;
		} else if (y<=x && y<=z) {
			return y;
		} else { //(z<=x && z<=y)
			return z;
		}
	}
	
	/**
	 * Dadas dos secuencias de Bytes, almacena su mejor coste de transformación y operación para cada problema
	 * generado con sus subcadenas en <matrizCoste> y <matrizOperaciones>.
	 * 
	 * Después de esta llamada habría que resolver el problema.
	 * 
	 * @param A Secuencia de bytes original
	 * @param B Secuencia de bytes destino
	 */
	public void compSec(byte []A, byte []B) {
		matrizCoste = new int[A.length+1][B.length+1];;
		matrizOperaciones = new Instruccion[A.length+1][B.length+1];
		for(int i = 0; i <= A.length; i++) { matrizCoste[i][0]=i; } //Caso partiendo de la cadena vacía
		for(int j = 0; j <= A.length; j++) { matrizCoste[0][j]=j; } //Caso objetivo la cadena vacía
		for(int i = 1; i <= A.length; i++){
			for(int j = 1; j <= B.length; j++) {
				int x = matrizCoste[i-1][j] + 1; //Borrar carácter.
				int y = matrizCoste[i][j-1] + 1; //Insertar carácter.
				int z;
				//System.out.println(A[i-1]+" "+B[j-1]); //DEBUG
				if(A[i-1]==B[j-1]) { z = matrizCoste[i-1][j-1]; } //Sin necesidad de operar.
				else { z = matrizCoste[i-1][j-1] + 1; } //Reemplazar carácter.
				matrizCoste[i][j] = min(x,y,z);
				if (x==matrizCoste[i][j]) {
					matrizOperaciones[i][j] = new Instruccion("ELIM",(byte)(i-1),(byte)0);
				} else if (y==matrizCoste[i][j]) {
					matrizOperaciones[i][j] = new Instruccion("INS",(byte)(i-1),B[j-1]);
				} else if (z==matrizCoste[i][j]) {					
					if(A[i-1]==B[j-1]) {
						matrizOperaciones[i][j] = null;
					} else {
						matrizOperaciones[i][j] = new Instruccion("REMP",(byte)(i-1),B[j-1]);
					}
				}
			}
		}
		return;
	}
	
	/**
	 * 
	 * @param _a 	Longitud de la cadena A
	 * @param _b	Longitud de la cadena B
	 * @return		Lista de instrucciones para transformar la cadena A en la cadena B.
	 */
	public ArrayList<Instruccion> resolver(int _a, int _b, byte[] B){
		int i = _a;
		int j = _b;
		ArrayList<Instruccion> insts = new ArrayList<Instruccion>();
		while(i != 0 || j != 0) { //Mientras las dos no sean cadena vacía
			if(j==0) {
				insts.add(new Instruccion("ELIM",(byte)(--i),(byte)0));
			} else if(i==0) {
				insts.add(new Instruccion("INS",(byte)(0),B[--j]));			
			} else {
				if(matrizOperaciones[i][j]==null) { //Nada  
															/*SALTA ERROR: Index -1 out of bounds for length 257
															at comparador_de_secuencias.CalcularMatrizSecuencias.resolver(CalcularMatrizSecuencias.java:78)
															at Main.Main.main(Main.java:201)
															*/
					i--;
					j--;
				} else {
					insts.add(matrizOperaciones[i][j]);
					if(matrizOperaciones[i][j].tipoInst()=="INS") {
						j--;
					} else if(matrizOperaciones[i][j].tipoInst()=="ELIM") {
						i--;
					} else { //"REMP"
						i--;
						j--;
					}
				}
			}
		}
		return insts;
	}
}
