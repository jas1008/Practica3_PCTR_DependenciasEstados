package p03.c01;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Clase que representa un parque con varias puertas
 * y regula su comportamiento con concurrencia y
 * exclusión mutua.
 * @author Daniel Alonso Báscones
 * @author Juan Abelairas Soto-Largo
 *
 */
public class Parque implements IParque{

	/**
	 * Aforo del parque.
	 */
	private int aforo;
	
	/**
	 * Contador de personas en total.
	 */
	private int contadorPersonasTotales;
	
	/**
	 * Contadores de las puertas.
	 */
	private Hashtable<String, Integer> contadoresPersonasPuerta;
	
	/**
	 * Constructor de parque.
	 * @param aforo Aforo del parque.
	 */
	public Parque( int aforo ) { 
		contadorPersonasTotales = 0;
		contadoresPersonasPuerta = new Hashtable<String, Integer>();
		this.aforo=aforo;
	}


	@Override
	synchronized public void entrarAlParque(String puerta) {
		
		// Si no hay entradas por esa puerta, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null) {
			contadoresPersonasPuerta.put(puerta, 0);
		}
		
		// Llamada al metodo de comprobacion
		comprobarAntesDeEntrar();
		
		
		// Aumentamos el contador total y el individual
		contadorPersonasTotales++;		
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta) + 1);
		
		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Entrada");
		
		// Comprobamos el invariante
		checkInvariante();
		this.notifyAll();
	}
	
	@Override
	public synchronized void salirDelParque(String puerta) {
		// Si no hay salidas por esa puerta, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null) {
			contadoresPersonasPuerta.put(puerta, 0);
		}
		
		// Llamada al metodo de comprobacion
		comprobarAntesDeSalir();
		
		// Aumentamos el contador total y el individual
		contadorPersonasTotales--;
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta) - 1);
		
		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Salida");
		
		// Comprobamos el invariante
		checkInvariante();
		this.notifyAll();
	}
	
	/**
	 * Imprime la información.
	 * @param puerta Puerta por la que sucede el evento.
	 * @param movimiento Movimiento realizado.
	 */
	private void imprimirInfo (String puerta, String movimiento){
		System.out.println(movimiento + " por puerta " + puerta);
		System.out.println("--> Personas en el parque " + contadorPersonasTotales); //+ " tiempo medio de estancia: "  + tmedio);
		
		// Iteramos por todas las puertas e imprimimos sus entradas
		for(String p: contadoresPersonasPuerta.keySet()){
			System.out.println("----> Por puerta " + p + " " + contadoresPersonasPuerta.get(p));
		}
		System.out.println(" ");
	}
	
	/**
	 * Suma los contadores de las puertas.
	 * @return Suma total
	 */
	private int sumarContadoresPuerta() {
		int sumaContadoresPuerta = 0;
			Enumeration<Integer> iterPuertas = contadoresPersonasPuerta.elements();
			while (iterPuertas.hasMoreElements()) {
				sumaContadoresPuerta += iterPuertas.nextElement();
			}
		return sumaContadoresPuerta;
	}
	
	/**
	 * Comprueba los invariantes.
	 */
	protected void checkInvariante() {
		
		assert sumarContadoresPuerta() == contadorPersonasTotales: "INV: La suma de contadores de las puertas debe ser igual al valor del contador del parte";
		assert sumarContadoresPuerta() >= 0: "El parque está vacío";
		assert sumarContadoresPuerta() <= aforo: "El parque está lleno";
	}

	/**
	 * Este metodo se encarga de dormir el hilo si el parque esta lleno
	 */
	protected void comprobarAntesDeEntrar(){	// TODO
		
		while ( contadorPersonasTotales >= aforo ) {
			
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}	
		
	}

	/**
	 * Este metodo se encarga de dormir el hilo si el parque esta vacío
	 */
	protected void comprobarAntesDeSalir(){		// TODO
		while ( contadorPersonasTotales <= 0 ) {
			
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}	
	}
}
