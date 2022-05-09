package compresorHuffman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Scanner;

import pruebas.Pruebas;

public class Main {
	private static Nodo Huffman(PriorityQueue<Nodo> lista) {
		while(lista.size()>1) {
			Nodo x = lista.poll();
			Nodo y = lista.poll();
			Nodo nuevo = new Nodo(x.peso+y.peso,(byte)0,x,y);
			lista.add(nuevo);
		}
		return lista.poll();
	}
	
	
	public static void main(String args[]) {
		if (args.length<2) {
			System.out.println("Estructura de llamada: huf <operacion> <fichero>");
		} else if(args.length>2){
			try {
				//Extraer_Frecuencias e = new Extraer_Frecuencias(args[1]);
				//PriorityQueue<Nodo> lista = e.sacarFrecuencias();
				//int numCaracteres = e.numChars;
				//Nodo raiz = Main.Huffman(lista);
				//Pruebas.prueba2(raiz,numCaracteres);
				//Pruebas.prueba3();
				Pruebas.prueba4();
			}catch(Exception e) {
				e.printStackTrace();
			}
		} else {
			if (args[0].equals("-c")) {
				try {
					//Abrimos el fichero de entrada
					//Extraemos las frecuencias de los caracteres. //Ahora codificamos también el \n, ¿en lugar de codificarlo, ponerlo directamente?
					Extraer_Frecuencias e = new Extraer_Frecuencias(args[1]);
					PriorityQueue<Nodo> lista = e.sacarFrecuencias();
					int numCaracteres = e.numChars;

					
					//Creamos el árbol Huffman
					Nodo raiz = Huffman(lista);
					System.out.println("Hecho el código"); //DEBUG
					
					//Volvemos a abrir el fichero de entrada
					FileInputStream input = new FileInputStream(args[1]);
					byte []buffer = new byte[1024];
					int bytesLeidos = 0;
					
						
					
					//Creamos fichero de salida
					String nomSalida = args[1] + "_comprimido.txt";
					File ficheroSalida = new File(nomSalida);
					
					//Convertimos el árbol en un String
					String arbol = "";
					//arbol = ToFile.anyadirNodos(arbol,raiz);
					
					
					//String test = ToFile.postOrderTraversal(raiz);
					//Para identificar el final de la representación del árbol de Huffman, se escribe un último 0.
					//test = test + "0";
					//System.out.print(test);
					
					
					
					//Abrimos el canal de escritura
		            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ficheroSalida));
		            //Introducimos el árbol y el número de caracteres
		            oos.writeObject(raiz);
		            oos.writeInt(numCaracteres);
		            
		            //Empezamos a codificar el fichero (recorrer árbol y generar código binario en base al valor encontrado).
		            String bits="";
		            while((bytesLeidos=input.read(buffer))>0) {
						for (int i = 0; i<bytesLeidos; i++) {
							String codigo = TratarCaracter.codificarCaracter(buffer[i], raiz);
							for(int j = 0; j < codigo.length(); j++) { //10
						    	if(bits.length()==8) {
						    		//Escribimos el Byte del buffer en el fichero
						    		oos.write(TratarCaracter.convertirBitsAByte(bits));
						    		bits="";
						    	}
						    	bits = bits + codigo.charAt(j);
						    }
						}
					}
		            //Buffer en el que se acumulan 1s y 0s hasta tener 8.
					int valor=0;
		            if(bits.length()>0) {
						while(bits.length()<8) {
							bits += "1";
						}
						//Escribimos el Byte del buffer en el fichero
						oos.write(TratarCaracter.convertirBitsAByte(bits));
			    		//Vaciar el "buffer"
			    		bits="";
					}
					//Cerramos el fichero de salida
					oos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			} else if (args[0].equals("-d")) {
				try {
					
					//Abrimos el fichero de entrada
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(args[1]));
					byte []buffer = new byte[1024];
					int bytesLeidos = 0;
					
					//Abrimos fichero de salida
					FileOutputStream fout = new FileOutputStream(args[1]+"_decodificado.txt");
					
					//Extraemos el árbol del fichero
					Nodo raiz = (Nodo)ois.readObject();
					int totalCaracteres = ois.readInt();
					int numDecodificados = 0;
					Nodo iterador = raiz;
					
					//Decodificamos el resto del fichero
					//Buffer en el que se acumulan 1s y 0s hasta tener 8.
		            while((bytesLeidos=ois.read(buffer))>0) {
		            	for (int j = 0; j < bytesLeidos; j++) {
			            	String bits = TratarCaracter.convertirByteABits(buffer[j]);
			            	for(int i = 0; i < bits.length(); i++) { //De 0 a 7 
						    	if(numDecodificados == totalCaracteres) {
						    		break;
						    	}
						    	if(raiz.esHoja()) { //Esto debería saltar solo si la raíz es hoja
						    		numDecodificados++;
						            fout.write(String.valueOf(raiz.contenido).getBytes());
						            
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
							    		byte t = iterador.contenido;
							    		fout.write(t);
							            
							    		//Reiniciamos el árbol para buscar
							    		iterador = raiz;
							    	}
						    	}
						    }
		            	}	    
					}
		            fout.close();
		            ois.close();
				} catch (Exception e){
		            e.printStackTrace();
		        }
			}
		}
	}
}
