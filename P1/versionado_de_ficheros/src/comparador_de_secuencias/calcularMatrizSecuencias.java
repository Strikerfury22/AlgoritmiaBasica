package comparador_de_secuencias;

public class calcularMatrizSecuencias {
	public int [][]matrizCoste;
	public Instruccion [][]matrizOperaciones;
	
	public calcularMatrizSecuencias() {
		
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
		matrizCoste = new int[A.length][B.length];;
		matrizOperaciones = new Instruccion[A.length][B.length];
		for(int i = 0; i < A.length; i++) { matrizCoste[i][0]=i; } //Caso partiendo de la cadena vacía
		for(int i = 0; i < A.length; i++) { matrizCoste[0][i]=i; } //Caso objetivo la cadena vacía
		for(int i = 1; i < A.length; i++){
			for(int j = 1; j < B.length; j++) {
				int x = matrizCoste[i-1][j] + 1; //Borrar carácter.
				int y = matrizCoste[i][j-1] + 1; //Insertar carácter.
				int z;
				if(A[i]==B[j]) { z = matrizCoste[i-1][j-1]; } //Sin necesidad de operar.
				else { z = matrizCoste[i-1][j-1] + 1; } //Reemplazar carácter.
				matrizCoste[i][j] = min(x,y,z);
				if (x==matrizCoste[i][j]) {
					matrizOperaciones[i][j] = new Instruccion("ELIM",(byte)i,(byte)0);
				} else if (y==matrizCoste[i][j]) {
					matrizOperaciones[i][j] = new Instruccion("INS",(byte)i,B[j]);
				} else if (z==matrizCoste[i][j]) {					
					if(A[i]==B[i]) {
						matrizOperaciones[i][j] = null;
					} else {
						matrizOperaciones[i][j] = new Instruccion("REMP",(byte)i,B[j]);
					}
				}
			}
		}
	}
	
	
}
