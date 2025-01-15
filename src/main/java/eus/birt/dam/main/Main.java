package eus.birt.dam.main;

import java.util.Scanner;
import eus.birt.dam.service.*;

public class Main {
	
	public static void main(String[] args) {
		
		Scanner keyboard = new Scanner(System.in);
		
		menu();	
		System.out.println("Selecciona la opción (1-8): ");
		int num = keyboard.nextInt();
		
		while (num < 8) {
		    switch (num) {
		        case 1:
		        	keyboard.nextLine(); //Limpiar el buffer
		            ConductorService.crearConductor(keyboard);
		            break;
		        case 2:
		        	keyboard.nextLine(); //Limpiar el buffer
		            ViajeService.crearViaje(keyboard);
		            break;
		        case 3:
		        	System.out.println("Indica para cuantas personas se busca viaje:");
		            int numPersonas = keyboard.nextInt();
		            keyboard.nextLine(); //Limpiar el buffer
		            ViajeService.buscarViajesDisponibles(numPersonas);
		            break;
		        case 4:
		        	keyboard.nextLine(); //Limpiar el buffer
		            PasajeroService.crearPasajero(keyboard);
		            break;
		        case 5:
		        	keyboard.nextLine(); //Limpiar el buffer
		            ReservaService.crearReserva(keyboard);
		            break;
		        case 6:
		        	keyboard.nextLine(); //Limpiar el buffer
		            ReservaService.cancelarReserva(keyboard);
		            break;
		        case 7:
		        	keyboard.nextLine(); //Limpiar el buffer
		            ViajeService.listarViaje();
		            break;
		        default:
		            System.out.println("Opción no válida.");
		    }
		    
		    menu();
		    System.out.println("Selecciona la opción (1-8): ");
		    num = keyboard.nextInt();
		}

		System.out.println("Saliendo del programa...");

		
		keyboard.close();
	}
	
	public static void menu () {
		System.out.print( "=== Menu de Gestion de Viajes Compartidos === \n"
				+ "1. Crear Conductor \n"
				+ "2. Crear viaje \n"
				+ "3. Buscar viajes disponibles \n"
				+ "4. Crear pasajero \n"
				+ "5. Crear reserva \n"
				+ "6. Cancelar reserva \n"
				+ "7. Listar viajes \n"
				+ "8. Salir \n");
	}
}
