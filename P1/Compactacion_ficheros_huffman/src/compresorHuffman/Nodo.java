package compresorHuffman;

public class Nodo {
	public int valor;
	public char caracter;
	public Nodo izquierda;
	public Nodo derecha;
	//public boolean esCaracter; //Si es car�cter, es hoja, comprobar ambos hijos
	
	public Nodo(int v, char c, Nodo l, Nodo r) {
		valor = v;
		caracter = c;
		izquierda = l;
		derecha = r;
	}
	
	public boolean esHoja() {	
		return izquierda == null && derecha == null;
	}
	
}
