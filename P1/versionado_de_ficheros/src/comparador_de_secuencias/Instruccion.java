package comparador_de_secuencias;

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
		switch(_inst) {
		case 0: 			//000
			return "INS";
			break;
		case (byte)(1<<5):
			return "REMP";	//001
			break;
		case (byte)(1<<6):	//010
			return "ELIM";
			break;
		case (byte)(3<<5):	//011
			return "FBLO";
			break;
		case (byte)(1<<7):	//100
			return "FVER";
			break;
		default:			//ERROR
			return "ERROR";
		}
		
	}
}
