package compresorHuffman;

import java.util.Iterator;
import java.util.PriorityQueue;

import pruebas.Pruebas;

public class Main {
	private static Nodo Huffman(PriorityQueue<Nodo> lista) {
		while(lista.size()>1) {
			Nodo x = lista.poll();
			Nodo y = lista.poll();
			Nodo nuevo = new Nodo(x.valor+y.valor,' ',x,y);
			lista.add(nuevo);
		}
		return lista.poll();
	}
	
	private static int convertirBitsAEntero(String bits) {
		int valor = 0;
		boolean negativo = false;
		for(int i = 0; i < 8; i++) {
			char val = bits.charAt(i);
			if(!negativo) {
				if (val=='1') {
					if (i == 0) {
						negativo = true;
						
					} else {
						valor += Math.pow(2,(7-i));
					}
				}
			} else {
				if (val=='0') {
					valor += Math.pow(2,(7-i));
				}
			}
		}
		if(negativo) {
			valor = -(valor+1);
		}
		return valor;
	}
	
	private static String convertirEnteroABits(int entero) { //Debe pasar un valor no negativo (usar toUnsignedInt)
		String valor = "";
		String auxiliar = "";
		int check = entero;
		for(int i = 7; i >= 0; i--) { //Los 8 bits
			auxiliar += String.valueOf(check%2);
			check/=2;
		}
		for(int i = 7; i >= 0; i--) {
			valor += auxiliar.charAt(i);
		}
		return valor;
	}
	
	public static void main(String args[]) {
		if (args.length<2) {
			System.out.println("Estructura de llamada: huf <operacion> <fichero>");
		} else if(args.length>2){
			//Extraer_Frecuencias e = new Extraer_Frecuencias(args[1]);
			//PriorityQueue<Nodo> lista = e.sacarFrecuencias();
			
			//Nodo raiz = Main.Huffman(lista);
			//Pruebas.prueba2(raiz);
			Pruebas.prueba3();
			
		} else {
			if (args[0].equals("-c")) {
				Extraer_Frecuencias e = new Extraer_Frecuencias(args[1]);
				PriorityQueue<Nodo> lista = e.sacarFrecuencias();
				/*
				Iterator<Nodo> i = lista.iterator();
				while(i.hasNext()) {
					Nodo n = i.next();
					System.out.printf("Nodo con caracter %s y numero de repeticiones %d\n",n.caracter,n.valor);
				}
				*/
				Nodo raiz = Huffman(lista);
				System.out.println("Hecho el c�digo");
			}
		}
	}
}
