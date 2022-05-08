package compresorHuffman;

public class TratarCaracter {
	
	//Pasando un String con 8 caracteres de 1s y 0s genera su valor Byte
	public static char convertirBitsAChar(String bits) {
		char valor = 0;
		for(int i = 0; i < 16; i++) {
			char val = bits.charAt(i);
			if (val=='1') {
				valor += Math.pow(2,(15-i));
			}
		}
		return valor;
	}
	
	//Pasando el valor entero positivo de un carácter, devuelve el array de Strings con los bits que lo codifican
	public static String convertirCharABits(char caracter) { //Debe pasar un valor no negativo (usar toUnsignedInt)
		String valor = "";
		String auxiliar = "";
		int check = (int)caracter;
		for(int i = 15; i >= 0; i--) { //Los 8 bits
			auxiliar += String.valueOf(check%2);
			check/=2;
		}
		for(int i = 15; i >= 0; i--) {
			valor += auxiliar.charAt(i);
		}
		return valor;
	}
	
	public static String codificarCaracter(char c, Nodo arbol) {
		String s = sacaCodigo(c,arbol);
		return s;
	}
	
	//////////////////////////////////////////////////
	//PRIVADAS
	//////////////////////////////////////////////////
	
	private static String sacaCodigo(char c, Nodo nodo) {
		String s = "";
		if (nodo!=null) {
			if(nodo.esHoja()) {
				if (nodo.caracter==c) {
					s = "t";
				}
			} else {
				s = sacaCodigo(c,nodo.izquierda);
				if(!s.equals("")) { //Si se ha encontrado
					if (s.equals("t")) {
						s = "0";
					} else {
						s = "0" + s;
					}
				} else {
					s = sacaCodigo(c,nodo.derecha);
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
