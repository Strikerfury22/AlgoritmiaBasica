package compresorHuffman;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;

public class ToFile {


	public static Nodo extraerNodos(String arbol, MyInt carABorrar) {
		Nodo n = null;
		if(arbol.charAt(0)=='N') {
			String auxiliar = arbol.substring(2); //Actualizamos el String en local
			MyInt aBorrar = new MyInt(0);
			Nodo izq = extraerNodos(auxiliar, aBorrar);
			auxiliar = auxiliar.substring(aBorrar.value); //Actualizamos el String en local
			Nodo dcha = extraerNodos(auxiliar, aBorrar);
			n = new Nodo(1,'_',izq,dcha);
			carABorrar.value = aBorrar.value+3; //Le decimos al padre que borre lo que han borrado nuestros hijos, más los caracteres de apertura "N(" y de cierre ")".
		} else if (arbol.charAt(0)=='H') {
			n = new Nodo(0,arbol.charAt(1),null,null);
			if (arbol.charAt(2)==',') {
				carABorrar.value=3; //Borrar H, carácter y separador coma.
			} else {
				carABorrar.value=2; //Borrar H y carácter.
			}
		}//Si no, es E, que es nodo nulo.
		return n;
	}
	
	public static String anyadirNodos(String s, Nodo nodo) {
		String arbol = s;
		if(nodo.esHoja()) {
			arbol += "H"+nodo.caracter;
		} else {
			arbol += "N(";
			if(nodo.izquierda!=null) {
				arbol = anyadirNodos(arbol,nodo.izquierda)+",";
			} else {
				arbol += "E,";
			}
			if(nodo.derecha!=null) {
				arbol = anyadirNodos(arbol,nodo.derecha)+")";
			} else {
				arbol += "E)";
			}
		}
		return arbol;
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
