package compresorHuffman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;

public class ToFile {


	public static Nodo extraerNodos(BufferedReader br, MyInt seComeSeparador) {
		try {
			int valor = br.read();
			Nodo n = null;
			if(valor!=1) {
				char c = (char)valor;
				if(c=='N') { 											//Puede ser un nodo no hoja
					valor = br.read();
					if(valor!=1) {
						char d = (char)valor;
						if(d=='(') { 													//Es inicio de nodo no hoja
							MyInt seHaComidoSeparador = new MyInt(0);
							Nodo izq = extraerNodos(br,seHaComidoSeparador);
							if(seHaComidoSeparador.value==0) {
								br.read(); //Se come la coma
							} else {
								seHaComidoSeparador.value=0; //Reinicia el indicador
							}
							Nodo dcha = extraerNodos(br,seHaComidoSeparador);
							if(seHaComidoSeparador.value==0) {
								br.read(); //Se come el cierre del paréntesis
							} //No se reinicia el indicador porque ya no se usa
							n = new Nodo(1,'_',izq,dcha);
						} else { 														//Era una hoja. d es ',' o ')'
							n = new Nodo(0,c,null,null);
							seComeSeparador.value=1; //Indica al padre que se ha comido el separador
						}
					}
				} else { 												//Es nodo hoja.
					n = new Nodo(0,c,null,null);
				}
				return n;
			} else { // No hay nada que leer, error.
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String anyadirNodos(String s, Nodo nodo) {
		String arbol = s;
		if(nodo.esHoja()) {
			arbol += nodo.caracter;
		} else {
			arbol += "N(";
			if(nodo.izquierda!=null) {
				arbol = anyadirNodos(arbol,nodo.izquierda);
			} 
			arbol += ",";
			if(nodo.derecha!=null) {
				arbol = anyadirNodos(arbol,nodo.derecha);
			}
			arbol += ")";
		}
		return arbol;
	}
	
	public static String postOrderTraversal(Nodo raiz) {
		if (raiz != null) {
			if (raiz.esHoja()) {
			return postOrderTraversal(raiz.izquierda) +
			postOrderTraversal(raiz.derecha) +
			"1"+raiz.caracter;
				//System.out.print("1" + "-" + raiz.caracter);
			} else {
				return postOrderTraversal(raiz.izquierda) +
				postOrderTraversal(raiz.derecha) +
				"0";
				//System.out.print("0");
			}
			//System.out.println(raiz.caracter + "-" + raiz.valor);
		} else {
			return "";
		}
	}
	public static void escribirArbol (File fichero, Nodo raiz) {
		String s = "";
		s = anyadirNodos(s,raiz) + "\n";
		try {
            FileOutputStream fout
                    = new FileOutputStream(fichero);
            byte b[] = s.getBytes();
            fout.write(b);
        }catch (Exception e){
            e.printStackTrace();
        }
	}
}
