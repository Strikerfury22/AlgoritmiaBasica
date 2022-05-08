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
			try {
				Extraer_Frecuencias e = new Extraer_Frecuencias(new FileReader(args[1]));
				PriorityQueue<Nodo> lista = e.sacarFrecuencias();
				int numCaracteres = e.numChars;
				Nodo raiz = Main.Huffman(lista);
				Pruebas.prueba2(raiz,numCaracteres);
				Pruebas.prueba3();
			}catch(Exception e) {
				e.printStackTrace();
			}
		} else {
			if (args[0].equals("-c")) {
				try {
					//Abrimos el fichero de entrada
					FileReader readerContar = new FileReader(args[1]);
					
					//Extraemos las frecuencias de los caracteres. //Ahora codificamos también el \n, ¿en lugar de codificarlo, ponerlo directamente?
					Extraer_Frecuencias e = new Extraer_Frecuencias(readerContar);
					PriorityQueue<Nodo> lista = e.sacarFrecuencias();
					int numCaracteres = e.numChars;

					
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
					arbol = ToFile.anyadirNodos(arbol,raiz);
					
					
					String test = ToFile.postOrderTraversal(raiz);
					//Para identificar el final de la representación del árbol de Huffman, se escribe un último 0.
					test = test + "0";
					System.out.print(test);
					
					
					
					//Abrimos el canal de escritura
		            FileOutputStream fout = new FileOutputStream(ficheroSalida);
		            
		            //Introducimos el árbol y el número de caracteres
		            byte b[] = arbol.getBytes();
		            fout.write(b);
		            String nc = String.valueOf(numCaracteres)+"\n";
		            fout.write(nc.getBytes());
		            
		            //Empezamos a codificar el fichero (recorrer árbol y generar código binario en base al valor encontrado).
		            int valor = br.read();
		            
		            //Buffer en el que se acumulan 1s y 0s hasta tener 8.
					String bits = "";
		            while(valor!=-1) {
						char c = (char)valor;
					    String codigo = TratarCaracter.codificarCaracter(c, raiz);
					    for(int i = 0; i < codigo.length(); i++) { //10
					    	if(bits.length()==16) {
					    		//Escribimos el Byte del buffer en el fichero
					    		fout.write(String.valueOf(TratarCaracter.convertirBitsAChar(bits)).getBytes());
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
						while(bits.length()<16) {
							bits += "1";
						}
						//Escribimos el Byte del buffer en el fichero
						char input = TratarCaracter.convertirBitsAChar(bits);
			    		fout.write(String.valueOf(input).getBytes());
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
					FileReader readerDecodificar = new FileReader(args[1]);
					BufferedReader br = new BufferedReader(readerDecodificar);
					
					//Abrimos fichero de salida
					File ficheroSalida = new File(args[1]+"_decodificado.txt");
					FileOutputStream fout = new FileOutputStream(ficheroSalida);
					
					//Extraemos el árbol del fichero
					Nodo raiz = ToFile.extraerNodos(br, new MyInt(0));
					String number = br.readLine();
					int totalCaracteres = Integer.valueOf(number);
					
					//Decodificamos el resto del fichero
					int valor = br.read();
		            int numDecodificados = 0;
		            Nodo iterador = raiz;
		            
		            //Buffer en el que se acumulan 1s y 0s hasta tener 8.
		            while(valor!=-1) {
		            	char c = (char) valor;
		            	String bits = TratarCaracter.convertirCharABits(c);
		            	for(int i = 0; i < bits.length(); i++) { //De 0 a 15 
					    	if(numDecodificados == totalCaracteres) {
					    		break;
					    	}
					    	if(raiz.esHoja()) { //Esto debería saltar solo si la raíz es hoja
					    		numDecodificados++;
					            fout.write(String.valueOf(raiz.caracter).getBytes());
					            
					    		//Reiniciamos el árbol para buscar
					    		iterador = raiz;
					    	} else {
						    	if(bits.charAt(i)=='1') {
						    		iterador = iterador.derecha;
						    	} else { //0
						    		iterador = iterador.izquierda;
						    	}
						    	if(iterador.esHoja()) {
						    		numDecodificados++;
						    		char t = iterador.caracter;
						    		fout.write(String.valueOf(iterador.caracter).getBytes());
						            
						    		//Reiniciamos el árbol para buscar
						    		iterador = raiz;
						    	}
					    	}
					    }
					    valor = br.read();
					}
				} catch (Exception e){
		            e.printStackTrace();
		        }
			}
		}
	}
}
