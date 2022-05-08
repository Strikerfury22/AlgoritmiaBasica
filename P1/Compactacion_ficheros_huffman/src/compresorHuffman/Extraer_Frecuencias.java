package compresorHuffman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

public class Extraer_Frecuencias {
	private FileReader fichero;
	public int numChars;
	
	public Extraer_Frecuencias(FileReader _fichero) {
		fichero = _fichero;
	}
	
	public PriorityQueue<Nodo> sacarFrecuencias() {
		numChars = 0;
		Map<Character,Integer> d = new HashMap<Character,Integer>();
		try {
			BufferedReader br = new BufferedReader(fichero);
			int valor = br.read();
			while(valor!=-1) {
				numChars++;
				char c = (char)valor;
			    if (d.containsKey(c)) {
			    	d.put(c,d.get(c)+1);
			    } else {  	
			    	d.put(c,1);
			    }
			    valor = br.read();
			}
			br.close();
		} catch (Exception e) {
			System.out.println("Error al acceder al fichero.");
		}
		Set<Character> set = d.keySet();
		PriorityQueue<Nodo> lista = new PriorityQueue<Nodo>(d.size(),new ImplementComparator());
		for (Character c : set) {
			Nodo n = new Nodo(d.get(c),c,null,null);
			lista.add(n);
		}
		return lista;
	}

}
