package compresorHuffman;

public class TratarCaracter {
	
	//Pasando un String con 8 caracteres de 1s y 0s genera su valor Byte
	public static byte convertirBitsAByte(String bits) {
		byte valor = 0;
		for(int i = 0; i < 8; i++) {
			char val = bits.charAt(i);
			if (val=='1') {
				valor += 1<<(7-i);
			}
		}
		return valor;
	}
	//byte beta = 0B1000000;
	//Pasando el valor entero positivo de un carácter, devuelve el array de Strings con los bits que lo codifican
	public static String convertirByteABits(byte miByte) { //Debe pasar un valor no negativo (usar toUnsignedInt)
		String valor = "";
		for(int i = 7; i >= 0; i--) { //Los 8 bits
			int check = (miByte & (1<<i)) >>(i);
			valor += String.valueOf(check);
		}
		return valor;
	}
	
	public static String codificarCaracter(byte b, Nodo arbol) {
		String s = sacaCodigo(b,arbol);
		return s;
	}
	
	//////////////////////////////////////////////////
	//PRIVADAS
	//////////////////////////////////////////////////
	
	private static String sacaCodigo(byte b, Nodo nodo) {
		String s = "";
		if (nodo!=null) {
			if(nodo.esHoja()) {
				if (nodo.contenido==b) {
					s = "t";
				}
			} else {
				s = sacaCodigo(b,nodo.izquierda);
				if(!s.equals("")) { //Si se ha encontrado
					if (s.equals("t")) {
						s = "0";
					} else {
						s = "0" + s;
					}
				} else {
					s = sacaCodigo(b,nodo.derecha);
					if(!s.equals("")) { ///Si se ha encontrado
						if (s.equals("t")) {
							s = "1";
						} else {
							s = "1" + s;
						}
					}
				}
			}
		}
		return s;
	}
}
