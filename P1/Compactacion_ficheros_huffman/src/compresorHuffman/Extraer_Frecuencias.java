package compresorHuffman;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Extraer_Frecuencias {
	private String fichero;
	
	public Extraer_Frecuencias(String _fichero) {
		fichero = _fichero;
	}
	
	public PriorityQueue<Nodo> sacarFrecuencias() {
		PriorityQueue<Nodo> lista = new PriorityQueue<Nodo>(fichero.length(),new ImplementComparator());
		Map<Character,Integer> d = new HashMap<Character,Integer>();
		for (int i=0; i < fichero.length(); i++) {
		    char c = fichero.charAt(i);   
		    if (d.containsKey(c)) {
		    	d.put(c,d.get(c)+1);
		    } else {  	
		    	d.put(c,1);
		    }
		}
		Set<Character> set = d.keySet();
		for (Character c : set) {
			Nodo n = new Nodo(d.get(c),c,null,null);
			lista.add(n);
		}
		return lista;
	}
}
