package pruebas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import comparador_de_secuencias.CalcularMatrizSecuencias;
import comparador_de_secuencias.Instruccion;

public class Prueba {
	
	//Codificación correcta de instrucciones
	public static void prueba1() {
		int i = 1;
		System.out.println(--i);
		System.out.println(i);
		System.out.println("Prueba de codificcación de tipo de instrucción:");
		Instruccion ins = new Instruccion("FVER",(byte)0,(byte)0);
		System.out.println(ins.tipoInst());
		ins = new Instruccion("FBLO",(byte)0,(byte)0);
		System.out.println(ins.tipoInst());
		ins = new Instruccion("INS",(byte)0,(byte)0);
		System.out.println(ins.tipoInst());
		ins = new Instruccion("ELIM",(byte)0,(byte)0);
		System.out.println(ins.tipoInst());
		ins = new Instruccion("REMP",(byte)0,(byte)0);
		System.out.println(ins.tipoInst());
		System.out.println("FIN prueba de codificcación de tipo de instrucción:");
	}
	
	public static void prueba2() {
		System.out.println("Prueba de resolución de array de Bytes:");
		byte []A = {1,2,2,3};
		byte []B = {2,1,2,2};
		CalcularMatrizSecuencias c = new CalcularMatrizSecuencias();
		
		c.compSec(A, B);
		ArrayList<Instruccion> insts = c.resolver(A.length, B.length, B);
		for (Instruccion i : insts) {
			System.out.println("Instruccion: "+i.tipoInst());
			System.out.println("Indice: "+i.indice);
			System.out.println("Valor: "+i.valor);
			A = i.ejecutarInstruccion(A,A.length);
		}
		System.out.println("FIN prueba de resolución de array de Bytes:");
	}
	
	/*//VERSION BETA//
	public static byte[] ejecutarInstruccion(Instruccion ins, byte[] A) {
		int longitud = A.length;
		final Byte[] temp = new Byte[longitud];
		IntStream.range(0, A.length).forEach(i -> temp[i] = A[i]);
		List<Byte> tempList = new ArrayList<Byte>(Arrays.asList(temp));
		
		if(ins.tipoInst()=="ELIM") {
			tempList.remove(ins.indice);
			longitud--;
		} else if (ins.tipoInst()=="REMP") {
			tempList.set(ins.indice, ins.valor);
		} else { //"INS"
			tempList.add(ins.indice, ins.valor);
			longitud++;
		}
		
		Byte[] temp2 = tempList.toArray(new Byte[0]);
		byte[] resultado = new byte[longitud];
		IntStream.range(0, temp2.length).forEach(i -> resultado[i] = temp2[i]);
		return resultado;
	}*/
}
