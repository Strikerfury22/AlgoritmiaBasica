package compresorHuffman;

import java.io.Serializable;

public class Nodo implements Serializable{
	private static final long serialVersionUID = 1L;
	public int peso;
	public byte contenido;
	public Nodo izquierda;
	public Nodo derecha;
	//public boolean esCaracter; //Si es car�cter, es hoja, comprobar ambos hijos
	
	public Nodo(int v, byte b, Nodo l, Nodo r) {
		peso = v;
		contenido = b;
		izquierda = l;
		derecha = r;
	}
	
	public boolean esHoja() {	
		return izquierda == null && derecha == null;
	}
	
}
