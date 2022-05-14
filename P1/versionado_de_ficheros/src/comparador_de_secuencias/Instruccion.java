package comparador_de_secuencias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/** Clase orientada a codificar las posibles instrucciones extraídas por <compSec>
 *  y las instrucciones de separación del algoritmo principal.
 */
public class Instruccion {
	public byte inst;
	public byte indice;
	public byte valor; //Valor a usar en instrucciones de INS y REMP.

	public Instruccion(String _inst, byte _indice, byte _valor) {
		inst = 0;
		switch(_inst) {
			case "INS":	//000
				indice = _indice;
				valor = _valor;
				break;
			case "REMP": //001
				inst = (byte)(1<<5); 
				indice = _indice;
				valor = _valor;
				break;
			case "ELIM": //010
				inst = (byte)(1<<6); 
				indice = _indice;
				break;
			case "FBLO": //011
				inst = (byte)(3<<5); 
				break;
			case "FVER": //100
				inst = (byte)(1<<7); 
				break;
			default: //111 <- ERROR
				inst = (byte)(7<<5);
		}
	}
	
	//Devuelve el tipo de instrucción en formato String
	public String tipoInst() {
		switch(inst) {
			case 0: 			//000
				return "INS";
			case (byte)(1<<5):
				return "REMP";	//001
			case (byte)(1<<6):	//010
				return "ELIM";
			case (byte)(3<<5):	//011
				return "FBLO";
			case (byte)(1<<7):	//100
				return "FVER";
			default:			//ERROR
				return "ERROR";
		}
		
	}
	
	public byte[] ejecutarInstruccion( byte[] A) {
		int longitud = A.length;
		final Byte[] temp = new Byte[longitud];
		IntStream.range(0, A.length).forEach(i -> temp[i] = A[i]);
		List<Byte> tempList = new ArrayList<Byte>(Arrays.asList(temp));
		
		if(this.tipoInst()=="ELIM") {
			tempList.remove(this.indice);
			longitud--;
		} else if (this.tipoInst()=="REMP") {
			tempList.set(this.indice, this.valor);
		} else { //"INS"
			tempList.add(this.indice, this.valor);
			longitud++;
		}
		
		Byte[] temp2 = tempList.toArray(new Byte[0]);
		byte[] resultado = new byte[longitud];
		IntStream.range(0, temp2.length).forEach(i -> resultado[i] = temp2[i]);
		return resultado;
	}
}
