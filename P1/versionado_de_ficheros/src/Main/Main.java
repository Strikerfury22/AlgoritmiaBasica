package Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;

import comparador_de_secuencias.CalcularMatrizSecuencias;
import comparador_de_secuencias.Instruccion;
import pruebas.Prueba;

//import pruebas.Pruebas;


//./prog -rv fich.txt
//./prog -gv fich.txt 3
public class Main {

	public static byte[] aplicarVersion(FileOutputStream fout, ArrayList<Instruccion> insts, byte[] buffer, int longitud) {
		try {
			if(!insts.get(0).tipoInst().equals("FVER")) {
				if(insts.isEmpty()) { //Si ya no podemos operar nada más
					return buffer; //Salimos sin escribir nada en fout porque puede que queden modificaciones por hacer en buffer
				} else {
					insts.remove(0); //Eliminamos la instruccion de Fin anterior
				}
				while (!insts.isEmpty()) {
					if(insts.get(0).tipoInst().equals("FBLO") || insts.get(0).tipoInst().equals("FVER")) { //Ya no quedan instrucciones en esta lectura
						break;
					}
					buffer = insts.get(0).ejecutarInstruccion(buffer,longitud);
					longitud = buffer.length; //actualizamos la longitud
					insts.remove(0);
				}
			}
			fout.write(buffer);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static FileInputStream generarUltimaVersionRegistrada(String fichVersiones, String fichBase) {
		try {
			FileInputStream inputBase = new FileInputStream(fichBase);
			byte []bufferBase = new byte[256];
			int A = 0;
			
			FileInputStream inputVersiones = new FileInputStream(fichVersiones);
			byte []bufferVersiones = new byte[1024];
			
			int blv = 0;
			ArrayList<Instruccion> insts = new ArrayList<Instruccion>();
			
			if(new File(fichVersiones).length()!=0) { //Si hay algo que tratar (no es la primera actualización)
				
				//Primera tanda de instrucciones
				blv = inputVersiones.read(bufferVersiones);
				insts = Instruccion.deSerializar(bufferVersiones, blv); //De-Serializamos las instrucciones
				
				//Creamos los ficheros que se utilizarán en este bucle
				File aux1 = new File("auxiliar_versiones1");
				aux1.createNewFile();
				File aux2 = new File("auxiliar_versiones2");
				aux2.createNewFile();
				
				
				FileOutputStream fout = new FileOutputStream("auxiliar_versiones1");
				
				//Llamada
						//Si faltan instrucciones (y la que toca no es "FBLO"), puede que quede modificación en bufferBase, si no se lee.
						//Si no queda por leer, tampoco queda por escribir, se comprueba si la instrucción que toca es "FVER"
						//Si quedan instrucciones, y la que toca es "FVER", entonces se acabó este versionado.
				byte []buffer = {};
				int longitud = 0;
				while((insts.isEmpty() && !insts.get(0).tipoInst().equals("FBLO")) || (A = inputBase.read(bufferBase))>0 || insts.get(0).tipoInst().equals("FVER")) {
					if(!insts.isEmpty()) {
						if(insts.get(0).tipoInst().equals("FBLO")) { insts.remove(0); }
						buffer = bufferBase;
						longitud = A;
					} else { //Caso vacía
						//buffer debe mantener el valor devuelto en la iteración anterior en este caso (no sobreescrito por inputBase.read()).
						blv = inputVersiones.read(bufferVersiones);
						insts = Instruccion.deSerializar(bufferVersiones, blv); //De-Serializamos las instrucciones
						longitud = buffer.length;
					}
					buffer = aplicarVersion(fout, insts, buffer, longitud);
				}
				
				//En insts[0] debe de haber una instrucción "FVER", la extraemos
				insts.remove(0);
				
				//cerramos el origen y el destino
				inputBase.close();
				fout.close();
				
				//reabrimos inputBase con el fichero que acabamos de escribir
				inputBase = new FileInputStream("auxiliar_versiones1");
				
				int iteracion = 0;
				if(insts.isEmpty()) { //Rellenamos si es necesario antes de empezar con la siguiente versión
					blv = inputVersiones.read(bufferVersiones);
					insts = Instruccion.deSerializar(bufferVersiones, blv); //De-Serializamos las instrucciones
				}
				while(blv!=0) {
					if (iteracion%2==1) {
						//abrimos fichero destino de la siguiente versión
						fout = new FileOutputStream("auxiliar_versiones1");
						
						while((insts.isEmpty() && !insts.get(0).tipoInst().equals("FBLO")) || (A = inputBase.read(bufferBase))>0 || insts.get(0).tipoInst().equals("FVER")) {
							if(!insts.isEmpty()) {
								if(insts.get(0).tipoInst().equals("FBLO")) { insts.remove(0); }
								buffer = bufferBase;
								longitud = A;
							} else { //Caso vacía
								//buffer debe mantener el valor devuelto en la iteración anterior en este caso (no sobreescrito por inputBase.read()).
								blv = inputVersiones.read(bufferVersiones);
								insts = Instruccion.deSerializar(bufferVersiones, blv); //De-Serializamos las instrucciones
								longitud = buffer.length;
							}
							buffer = aplicarVersion(fout, insts, buffer, longitud);
						}
						
						//En insts[0] debe de haber una instrucción "FVER", la extraemos
						insts.remove(0);
						
						//cerramos el origen y el destino
						inputBase.close();
						fout.close();
						
						//reabrimos inputBase con el fichero que acabamos de escribir
						inputBase = new FileInputStream("auxiliar_versiones1");
						
						
					} else { 
						fout = new FileOutputStream("auxiliar_versiones2");	
						
						while((insts.isEmpty() && !insts.get(0).tipoInst().equals("FBLO")) || (A = inputBase.read(bufferBase))>0 || insts.get(0).tipoInst().equals("FVER")) {
							if(!insts.isEmpty()) {
								if(insts.get(0).tipoInst().equals("FBLO")) { insts.remove(0); }
								buffer = bufferBase;
								longitud = A;
							} else { //Caso vacía
								//buffer debe mantener el valor devuelto en la iteración anterior en este caso (no sobreescrito por inputBase.read()).
								blv = inputVersiones.read(bufferVersiones);
								insts = Instruccion.deSerializar(bufferVersiones, blv); //De-Serializamos las instrucciones
								longitud = buffer.length;
							}
							buffer = aplicarVersion(fout, insts, buffer, longitud);
						}
						
						//En insts[0] debe de haber una instrucción "FVER", la extraemos
						insts.remove(0);
						
						//cerramos el origen y el destino
						inputBase.close();
						fout.close();
						
						//reabrimos inputBase con el fichero que acabamos de escribir
						inputBase = new FileInputStream("auxiliar_versiones1");
						
					}
				}
			}
			inputVersiones.close(); //Ya lo hemos leído entero			
			return inputBase;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null; //Caso de que salte la excepción
	}
	
	public static void main(String args[]) {
		//Prueba.prueba1();
		//Prueba.prueba2();
		
		if(args.length==2 && args[0].equals("-rv")) { //Operación registrar nueva versión
			String fichBase = args[1] + "_base";
			String fichVersiones = args[1] + "_versiones";
			if (new File(fichBase).isFile() && new File(fichVersiones).isFile()) { //Caso ya hay control de versiones de este fichero
				try {	
					//Generación de la última versión registrada
					FileInputStream inputBase = generarUltimaVersionRegistrada(fichVersiones,fichBase);
					byte []bufferBase = new byte[256];
					int A = 0;
					
					//Abrimos el fichero que es la nueva versión (el que nos han pasado)
					FileInputStream inputNuevaVersion = new FileInputStream(args[1]);
					byte []bufferNuevaVersion = new byte[256];
					int B = 1; //Para que entre en el bucle
					
					//Abrimos el fichero de versiones para escribir al final.
					FileOutputStream outputVersionado = new FileOutputStream(fichVersiones,true);
					
					while(B != 0 || A != 0) {
						B = inputNuevaVersion.read(bufferNuevaVersion);
						A = inputBase.read(bufferBase);
						CalcularMatrizSecuencias cms = new CalcularMatrizSecuencias();
						cms.compSec(bufferBase, bufferNuevaVersion);
						ArrayList<Instruccion> insts = cms.resolver(A, B, bufferNuevaVersion);
						insts.add(new Instruccion("FBLO",(byte)0,(byte)0)); //Añadimos la marca de fin de bloque de 256 Bytes
						byte []bufferAVersionado = Instruccion.serializar(insts);
						outputVersionado.write(bufferAVersionado); //Añadimos las instrucciones
					}
					Instruccion finVersion = new Instruccion("FVER",(byte)0,(byte)0);
					byte []bufferAVersionado = finVersion.serializar();
					outputVersionado.write(bufferAVersionado);
					
					outputVersionado.close();
					inputBase.close();
					inputNuevaVersion.close();
					
					//FileOutputStream output = new FileOutputStream(fichBase);
				} catch(Exception e) {
					e.printStackTrace();
				}
				
			} 
			else { //Caso no hay control de versiones de este fichero
				try {
					// Inicializamos el fichero base:
					FileInputStream input = new FileInputStream(args[1]);
					FileOutputStream output = new FileOutputStream(fichBase);
					byte []buffer = new byte[1024];
					int bytesLeidos = 0;
					while((bytesLeidos = input.read(buffer))>0) {
						output.write(buffer, 0, bytesLeidos);
					}
					input.close();
					output.close();
					
					//Creamos el fichero vacío de versiones:
					File versionado = new File(fichVersiones);
					versionado.createNewFile();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		} else if (args.length==2 && args[0].equals("-gv")) { //Operación de recuperación de una versión
			
		} else { //Operación inválida
			System.out.println("Llamada con formato erróneo. Debe llamarse con una de las siguientes estructuras:");
			System.out.println("prog -rv <fichero>");
			System.out.println("prog -gv <fichero> <versión>");
		}
	}

}
