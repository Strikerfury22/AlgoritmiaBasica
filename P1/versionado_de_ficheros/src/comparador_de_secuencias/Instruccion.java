package comparador_de_secuencias;

import java.io.FileInputStream;
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
	
	public byte[] serializar() {
		ArrayList<Byte> serializado = new ArrayList<Byte>();
		switch(this.tipoInst()) {
			case "INS":	//000
				serializado.add(this.inst);
				serializado.add(this.indice);
				serializado.add(this.valor);
				break;
			case "REMP": //001
				serializado.add(this.inst);
				serializado.add(this.indice);
				serializado.add(this.valor);
				break;
			case "ELIM": //010
				serializado.add(this.inst);
				serializado.add(this.indice);
				break;
			case "FBLO": //011
				serializado.add(this.inst);
				break;
			case "FVER": //100
				serializado.add(this.inst);
				break;
			default:
				System.err.println("Instrucción desconocida: IGNORADA");
		}
		Byte []temp = serializado.toArray(new Byte[0]);
		byte[] resultado = new byte[temp.length];
		IntStream.range(0, temp.length).forEach(i -> resultado[i] = temp[i]);
		return resultado;
	}

	public static ArrayList<Instruccion> deSerializar(String fichVersiones){
		ArrayList<Instruccion> resultado = new ArrayList<Instruccion>();
		try {
			FileInputStream inputVersiones = new FileInputStream(fichVersiones);
			byte []buffer = new byte[1024];
			int blv = inputVersiones.read(buffer);	
			int i = 0; //Indice que recorre el array de bytes.
			while(blv!=-1) {
				byte indice = 0;
				byte valor = 0;
				switch(tipoInst(buffer[i++])) {
					case "INS":	//000
						if(i==blv) {blv = inputVersiones.read(buffer); i = 0;} //Si se ha recorrido todo, rellena el buffer
						indice = buffer[i++];
						if(i==blv) {blv = inputVersiones.read(buffer); i = 0;}
						valor = buffer[i++];
						i++; //Para que apunte al siguiente byte
						resultado.add(new Instruccion("INS",indice,valor));
						break;
					case "REMP": //001
						if(i==blv) {blv = inputVersiones.read(buffer); i = 0;}
						indice = buffer[++i];
						if(i==blv) {blv = inputVersiones.read(buffer); i = 0;}
						valor = buffer[++i];
						i++; //Para que apunte al siguiente byte
						resultado.add(new Instruccion("REMP",indice,valor));
						break;
					case "ELIM": //010
						if(i==blv) {blv = inputVersiones.read(buffer); i = 0;}
						indice = buffer[++i];
						i++; //Para que apunte al siguiente byte
						resultado.add(new Instruccion("ELIM",indice,(byte)0));
						break;
					case "FBLO": //011
						if(i==blv) {blv = inputVersiones.read(buffer); i = 0;}
						resultado.add(new Instruccion("FBLO",(byte)0,(byte)0));
						break;
					case "FVER": //100
						if(i==blv) {blv = inputVersiones.read(buffer); i = 0;}
						resultado.add(new Instruccion("FVER",(byte)0,(byte)0));
						break;
					default:
						i++;
						System.err.println("Instrucción desconocida: IGNORADA");
				}
			}
			inputVersiones.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return resultado;
	}

	public static byte[] serializar(ArrayList<Instruccion> insts){
		ArrayList<Byte> serializado = new ArrayList<Byte>();
		for(Instruccion i : insts) {
			switch(i.tipoInst()) {
				case "INS":	//000
					serializado.add(i.inst);
					serializado.add(i.indice);
					serializado.add(i.valor);
					break;
				case "REMP": //001
					serializado.add(i.inst);
					serializado.add(i.indice);
					serializado.add(i.valor);
					break;
				case "ELIM": //010
					serializado.add(i.inst);
					serializado.add(i.indice);
					break;
				case "FBLO": //011
					serializado.add(i.inst);
					break;
				case "FVER": //100
					serializado.add(i.inst);
					break;
				default:
					System.err.println("Instrucción desconocida: IGNORADA");
			}
		}
		Byte []temp = serializado.toArray(new Byte[0]);
		byte[] resultado = new byte[temp.length];
		IntStream.range(0, temp.length).forEach(i -> resultado[i] = temp[i]);
		return resultado;
	}
	
	//Devuelve el tipo de instrucción en formato String
	public String tipoInst() {
		switch(inst) {
			case 0: 			//000
				return "INS";
			case (byte)(1<<5):	//001
				return "REMP";	
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

	public static String tipoInst(byte b) {
		switch(b) {
			case 0: 			//000
				return "INS";
			case (byte)(1<<5):	//001
				return "REMP";	
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
	
	public byte[] ejecutarInstruccion( byte[] A, int tamano) {
		int longitud = tamano;
		final Byte[] temp = new Byte[longitud];
		IntStream.range(0, tamano).forEach(i -> temp[i] = A[i]);
		List<Byte> tempList = new ArrayList<Byte>(Arrays.asList(temp));
		
		if(this.tipoInst()=="ELIM") {
			int ind = Byte.toUnsignedInt(this.indice);
			tempList.remove(Byte.toUnsignedInt(this.indice));
			longitud--;
		} else if (this.tipoInst()=="REMP") {
			tempList.set(this.indice, this.valor);
		} else { //"INS"
			tempList.add(this.indice+1, this.valor);
			longitud++;
		}
		
		Byte[] temp2 = tempList.toArray(new Byte[0]);
		byte[] resultado = new byte[longitud];
		IntStream.range(0, temp2.length).forEach(i -> resultado[i] = temp2[i]);
		return resultado;
	}

}
