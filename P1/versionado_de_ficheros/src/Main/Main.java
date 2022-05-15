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
				} //else if (insts.get(0).tipoInst().equals("FBLO")) {
					//insts.remove(0); //Eliminamos la instruccion de Fin anterior
				//}
				while (!insts.isEmpty()) {
					if(insts.get(0).tipoInst().equals("FBLO") || insts.get(0).tipoInst().equals("FVER")) { //Ya no quedan instrucciones en esta lectura
						//Hemos acabado con el bloque, así que lo volcamos
						fout.write(buffer);
						break;
					}
					buffer = insts.get(0).ejecutarInstruccion(buffer,longitud);
					longitud = buffer.length; //actualizamos la longitud
					insts.remove(0);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return buffer;
	}
	
	/**
	 * 
	 * @param fichVersiones	fichero que contiene los distintos cambios a realizar desde el fichero base para alcanzar cualquier versión registrada.
	 * @param fichBase		fichero que contiene la primera versión del archivo.
	 * @param versionObj	numero de la versión a alcanzar con valor entre 0 y el numero de actualizaciones que ha tenido (-1 para buscar la última).
	 * 						en caso de introducir una versión superior a la última registrada, saca la última registrada.
	 * @return				un objeto con el que leer la versión solicitada del fichero.
	 */
	public static FileInputStream generarVersion(String fichVersiones, String fichBase, int versionObj) {
		try {
			FileInputStream inputBase = new FileInputStream(fichBase);
			byte []bufferBase = new byte[256];
			int A = 0;
			
			
			ArrayList<Instruccion> insts = new ArrayList<Instruccion>();
			
			if (versionObj==0) {
				return inputBase;
			} else {
				int versionAct = 0;
				
				if(new File(fichVersiones).length()!=0) { //Si hay algo que tratar (no es la primera actualización)
					
					//Primera tanda de instrucciones
					insts = Instruccion.deSerializar(fichVersiones); //De-Serializamos las instrucciones
					
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
					
					A = inputBase.read(bufferBase);
					while(!insts.get(0).tipoInst().equals("FVER")) {
						if(!insts.isEmpty()) {
							if(insts.get(0).tipoInst().equals("FBLO")) { insts.remove(0); A = inputBase.read(bufferBase);} //Si hay que empezar a tratar el siguiente bloque
							buffer = bufferBase;
							if(A<=0) {
								longitud = 0; //Evitar problemas con indices negativos.
							} else {
								longitud = A;
							}
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
					
					versionAct++;
					if(versionAct==versionObj) {
						return inputBase;
					}
					
					
					int iteracion = 0;
					if(insts.isEmpty()) { //Rellenamos si es necesario antes de empezar con la siguiente versión
						blv = inputVersiones.read(bufferVersiones);
						insts = Instruccion.deSerializar(bufferVersiones, blv); //De-Serializamos las instrucciones
					}
					while(blv!=-1) { //Mientras no se haya acabado el fichero
						if (iteracion%2==1) {
							//abrimos fichero destino de la siguiente versión
							fout = new FileOutputStream("auxiliar_versiones1");
							
							A = inputBase.read(bufferBase);
							while(!insts.get(0).tipoInst().equals("FVER")) {
								if(!insts.isEmpty()) {
									if(insts.get(0).tipoInst().equals("FBLO")) { insts.remove(0); A = inputBase.read(bufferBase);} //Si hay que empezar a tratar el siguiente bloque
									buffer = bufferBase;
									if(A<=0) {
										longitud = 0; //Evitar problemas con indices negativos.
									} else {
										longitud = A;
									}
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
							
							versionAct++;
							if(versionAct==versionObj) {
								return inputBase;
							}
							
						} else { 
							fout = new FileOutputStream("auxiliar_versiones2");	
							
							A = inputBase.read(bufferBase);
							while(insts.isEmpty() || !insts.get(0).tipoInst().equals("FVER")) {
								if(!insts.isEmpty()) {
									if(insts.get(0).tipoInst().equals("FBLO")) { insts.remove(0); A = inputBase.read(bufferBase);} //Si hay que empezar a tratar el siguiente bloque
									buffer = bufferBase;
									if(A<=0) {
										longitud = 0; //Evitar problemas con indices negativos.
									} else {
										longitud = A;
									}
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
							inputBase = new FileInputStream("auxiliar_versiones2");
							
							versionAct++;
							if(versionAct==versionObj) {
								return inputBase;
							}
							
						}
						if(insts.isEmpty()) { //Rellenamos si es necesario antes de empezar con la siguiente versión
							blv = inputVersiones.read(bufferVersiones);
							insts = Instruccion.deSerializar(bufferVersiones, blv); //De-Serializamos las instrucciones
						}
						iteracion++;
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
					FileInputStream inputBase = generarVersion(fichVersiones,fichBase,-1);
					byte []bufferBase = new byte[256];
					int A = 0;
					
					//Abrimos el fichero que es la nueva versión (el que nos han pasado)
					FileInputStream inputNuevaVersion = new FileInputStream(args[1]);
					byte []bufferNuevaVersion = new byte[256];
					int B = 0; //Para que entre en el bucle
					
					//Abrimos el fichero de versiones para escribir al final.
					FileOutputStream outputVersionado = new FileOutputStream(fichVersiones,true);
					
					B = inputNuevaVersion.read(bufferNuevaVersion);
					A = inputBase.read(bufferBase);
					while(B != -1 || A != -1) {
						if(A<=0) {A = 0;}
						if(B<=0) {B = 0;}
						CalcularMatrizSecuencias cms = new CalcularMatrizSecuencias();
						cms.compSec(bufferBase, bufferNuevaVersion, A, B);
						ArrayList<Instruccion> insts = cms.resolver(A, B, bufferNuevaVersion);
						insts.add(new Instruccion("FBLO",(byte)0,(byte)0)); //Añadimos la marca de fin de bloque de 256 Bytes
						byte []bufferAVersionado = Instruccion.serializar(insts);
						outputVersionado.write(bufferAVersionado); //Añadimos las instrucciones
						B = inputNuevaVersion.read(bufferNuevaVersion);
						A = inputBase.read(bufferBase);
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
			
		} else if (args.length==3 && args[0].equals("-gv")) { //Operación de recuperación de una versión
			String fichBase = args[1] + "_base";
			String fichVersiones = args[1] + "_versiones";
			if (new File(fichBase).isFile() && new File(fichVersiones).isFile()) {
				FileInputStream inputVersion = generarVersion(fichVersiones,fichBase,Integer.valueOf(args[2]));
				String fichSalida = args[1]+"_version_"+args[2];
				try {
					FileOutputStream outputVersion = new FileOutputStream(fichSalida);
					byte []buffer = new byte[1024];
					int bytesLeidos=0;
					while((bytesLeidos=inputVersion.read(buffer))>0) {
						outputVersion.write(buffer,0,bytesLeidos);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("No existe un control de versionado completo de este fichero. Generelo con la siguiente llamada:");
				System.out.println("prog -rv "+args[1]);
			}
		} else { //Operación inválida
			System.out.println("Llamada con formato erróneo. Debe llamarse con una de las siguientes estructuras:");
			System.out.println("prog -rv <fichero>");
			System.out.println("prog -gv <fichero> <versión>");
		}
		//Eliminamos los ficheros auxiliares que se hayan creado
		eliminarFichero("auxiliar_versiones1");
		eliminarFichero("auxiliar_versiones2");
	}

	private static void eliminarFichero(String path) {
		File aux = new File(path);
		if(aux.isFile()) {
			aux.delete();
		}
	}
	
}

