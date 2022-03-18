package compresorHuffman;

import java.util.Iterator;
import java.util.PriorityQueue;

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
	
	public static void main(String args[]) {
		if (args.length!=2) {
			System.out.println("Estructura de llamada: huf <operacion> <fichero>");
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
				System.out.println("Hecho el código");
			}
		}
	}
}
