package pruebas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
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
	public static void prueba2(Nodo raiz, int numCaracteres) {
		
		
		File file = new File("output.txt");
		String s = "";
		s = ToFile.anyadirNodos(s,raiz);
		try {
            FileOutputStream fout
                    = new FileOutputStream(file);
            byte b[] = s.getBytes();
            fout.write(b);
            fout.write((String.valueOf(numCaracteres)+"\n").getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }
	}
	
	public static void prueba3() {
		try {
			FileReader file = new FileReader("output.txt");
			BufferedReader br = new BufferedReader(file);
			Nodo raiz = ToFile.extraerNodos(br, new MyInt(0));
			String s = "";
			s = ToFile.anyadirNodos(s,raiz);
			String number = br.readLine();
			System.out.println(s);
			System.out.println(number);
			br.close();
		} catch (Exception e) {
			System.out.println("Archivo no encontrado");
		}
	}
	
}
