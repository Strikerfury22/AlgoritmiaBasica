package compresorHuffman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Scanner;

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
	
	
	public static void main(String args[]) {
		if (args.length<2) {
			System.out.println("Estructura de llamada: huf <operacion> <fichero>");
		} else if(args.length>2){
			///Extraer_Frecuencias e = new Extraer_Frecuencias(new FileReader(args[1]));
			//PriorityQueue<Nodo> lista = e.sacarFrecuencias();
			
			//Nodo raiz = Main.Huffman(lista);
			//Pruebas.prueba2(raiz);
			Pruebas.prueba3();
			
		} else {
			if (args[0].equals("-c")) {
				try {
					//Abrimos el fichero de entrada
					FileReader readerContar = new FileReader(args[1]);
					
					//Extraemos las frecuencias de los caracteres. //Ahora codificamos también el \n, ¿en lugar de codificarlo, ponerlo directamente?
					Extraer_Frecuencias e = new Extraer_Frecuencias(readerContar);
					MyInt numCaracteres = new MyInt(0);
					PriorityQueue<Nodo> lista = e.sacarFrecuencias(numCaracteres);
					
					//Creamos el árbol Huffman
					Nodo raiz = Huffman(lista);
					System.out.println("Hecho el código"); //DEBUG
					
					//Volvemos a abrir el fichero de entrada
					FileReader readerCodificar = new FileReader(args[1]);
					BufferedReader br = new BufferedReader(readerCodificar);
					
					//Creamos fichero de salida
					String nomSalida = args[1] + "_comprimido.txt";
					File ficheroSalida = new File(nomSalida);
					
					//Convertimos el árbol en un String
					String arbol = "";
					arbol = ToFile.anyadirNodos(arbol,raiz) + "\n";
					
					//Abrimos el canal de escritura
		            FileOutputStream fout = new FileOutputStream(ficheroSalida);
		            
		            //Introducimos el árbol y el número de caracteres
		            byte b[] = arbol.getBytes();
		            fout.write(b);
		            String nc = String.valueOf(numCaracteres.value)+"\n";
		            fout.write(nc.getBytes());
		            
		            //Empezamos a codificar el fichero (recorrer árbol y generar código binario en base al valor encontrado).
		            int valor = br.read();
		            
		            //Buffer en el que se acumulan 1s y 0s hasta tener 8.
					String bits = "";
		            while(valor!=-1) {
						char c = (char)valor;
					    String codigo = TratarCaracter.codificarCaracter(c, raiz);
					    for(int i = 0; i < codigo.length(); i++) { //10
					    	if(bits.length()==8) {
					    		//Escribimos el Byte del buffer en el fichero
					    		Byte cb = Byte.valueOf(String.valueOf(TratarCaracter.convertirBitsAEntero(bits)));
					    		fout.write(cb);
					    		//Vacíar el "buffer"
					    		bits="";
					    	}
					    	bits = bits + codigo.charAt(i);
					    }
					    valor = br.read();
					}
		            //Ya se ha leído todo el fichero
					br.close();
					if(bits.length()>0) {
						while(bits.length()<8) {
							bits += "1";
						}
						//Escribimos el Byte del buffer en el fichero
			    		Byte cb = Byte.valueOf(String.valueOf(TratarCaracter.convertirBitsAEntero(bits)));
			    		fout.write(cb);
			    		//Vacíar el "buffer"
			    		bits="";
					}
					
					//Cerramos el fichero de salida
					fout.close();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			} else if (args[0].equals("-d")) {
				try {
					//Abrimos el fichero de entrada
					File fichero = new File(args[1]);
					Scanner scanner = new Scanner(fichero);
					
					//Abrimos fichero de salida
					File ficheroSalida = new File(args[1]+"_decodificado.txt");
					FileOutputStream fout = new FileOutputStream(ficheroSalida);
					
					//Extraemos el árbol del fichero
					String arbol = scanner.nextLine();
					Nodo raiz = ToFile.extraerNodos(arbol, new MyInt(0));
					
					//Decodificamos el resto del fichero
					
					
				} catch (Exception e){
		            e.printStackTrace();
		        }
			}
		}
	}
}
