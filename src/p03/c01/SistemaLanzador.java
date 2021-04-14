package p03.c01;

public class SistemaLanzador {
	public static void main(String[] args) {
		
		// Inicializo el parque con un aforo de 50 personas
		IParque parque = new Parque(50); 
		char letra_puerta = 'A';
		
		System.out.println("¡Parque abierto!");
		
		for (int i = 0; i < Integer.parseInt(args[0]); i++) {
			
			String puerta = ""+((char) (letra_puerta++));
			
			// Creación de hilos de entrada
			ActividadEntradaPuerta entradas = new ActividadEntradaPuerta(puerta, parque);
			new Thread (entradas).start();

			
			// Creacion de hilos de salida
			ActividadSalidaPuerta salidas = new ActividadSalidaPuerta(puerta, parque);
			new Thread (salidas).start();	
			
		}
	}	
}