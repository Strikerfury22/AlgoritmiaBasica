package pruebas;

import java.io.File;
import java.io.FileOutputStream;
import java.util.PriorityQueue;
import java.util.Scanner;

import compresorHuffman.Extraer_Frecuencias;
import compresorHuffman.MyInt;
import compresorHuffman.Nodo;
import compresorHuffman.ToFile;

public class Pruebas {
	
	public static void prueba1() {
		File file = new File("output.txt");
        try {
            FileOutputStream fout
                    = new FileOutputStream(file);
            String s = "Example of Java program to write Bytes using ByteStream.";
            byte b[] = s.getBytes();
            fout.write(b);
        }catch (Exception e){
            e.printStackTrace();
        }
	}
	public static void prueba2(Nodo raiz) {
		
		
		File file = new File("output.txt");
		String s = "";
		s = ToFile.anyadirNodos(s,raiz) + "\n";
		try {
            FileOutputStream fout
                    = new FileOutputStream(file);
            byte b[] = s.getBytes();
            fout.write(b);
        }catch (Exception e){
            e.printStackTrace();
        }
	}
	
	public static void prueba3() {
		File file = new File("output.txt");
		try {
			Scanner scanner = new Scanner(file);
			String arbol = scanner.nextLine();
			Nodo raiz = ToFile.extraerNodos(arbol, new MyInt(0));
			String s = "";
			s = ToFile.anyadirNodos(s,raiz) + "\n";
			System.out.println(s);
			scanner.close();
		} catch (Exception e) {
			System.out.println("Archivo no encontrado");
		}
	}
	
}
