package compresorHuffman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

public class Extraer_Frecuencias {
	private String fichero;
	public int numChars;
	
	public Extraer_Frecuencias(String _fichero) {
		fichero = _fichero;
	}
	
	public PriorityQueue<Nodo> sacarFrecuencias() {
		numChars = 0;
		Map<Byte,Integer> d = new HashMap<Byte,Integer>();
		try {
			FileInputStream br = new FileInputStream(fichero);
			byte []buffer = new byte[1024];
			int bytesLeidos = 0;
			while((bytesLeidos=br.read(buffer))>0) {
				numChars += bytesLeidos;
				for(int i = 0; i < bytesLeidos; i++) {
					d.put(buffer[i], d.getOrDefault(buffer[i], 0) + 1);
				}
			}
			br.close();
		} catch (Exception e) {
			System.out.println("Error al acceder al fichero.");
		}
		Set<Byte> set = d.keySet();
		PriorityQueue<Nodo> lista = new PriorityQueue<Nodo>(d.size(),new ImplementComparator());
		for (Byte b : set) {
			Nodo n = new Nodo(d.get(b),b,null,null);
			lista.add(n);
		}
		return lista;
	}

}
