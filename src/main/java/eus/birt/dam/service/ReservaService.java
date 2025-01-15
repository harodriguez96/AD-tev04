package eus.birt.dam.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.slf4j.LoggerFactory;

import eus.birt.dam.domain.Reserva;
import eus.birt.dam.domain.Conductor;
import eus.birt.dam.domain.Pasajero;
import eus.birt.dam.domain.Viaje;
import java.util.*;

import java.time.LocalDate; 
import java.time.format.DateTimeFormatter; 
import java.time.format.DateTimeParseException;

public class ReservaService {
    private static SessionFactory sessionFactory;
    private static Session session;

    static {
        // Cambiar el nivel de logging para org.hibernate
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.hibernate")).setLevel(ch.qos.logback.classic.Level.WARN);
        
        // Crear el StandardServiceRegistry usando la configuración en hibernate.cfg.xml
        StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();

        // Crear el Metadata utilizando el StandardServiceRegistry y añadir la clase anotada Reserva
        Metadata metadata = new MetadataSources(standardRegistry)
                .addAnnotatedClass(Reserva.class)
                .getMetadataBuilder()
                .build();

        // Construir el SessionFactory utilizando el Metadata
        sessionFactory = metadata.getSessionFactoryBuilder()
                .build();
    }

    private static void abrirSesion() {
        // Abrir una nueva sesión
        session = sessionFactory.openSession();
        System.out.println("Sesion abierta!");
    }

    private static void cerrarSesion() {
        // Cerrar la sesión si está abierta
        if (session != null) {
            session.close();
            System.out.println("Sesion cerrada!");
        }
    }

    public static void crearReserva(Scanner keyboard) {
        abrirSesion();
        LocalDate date = null;
        String name;
        List<Pasajero> pasajeros = new ArrayList<>();
        
        try {
            // Iniciar una transacción
            session.beginTransaction();
            
            // Obtener la fecha actual
            date = LocalDate.now();

            // Solicitar el número de plazas reservadas al usuario
            System.out.println("Indica cuantas personas se van a inscribir:");
            int numPlazasReservadas = keyboard.nextInt();
            
            // Buscar y listar los viajes disponibles para la cantidad de personas seleccionadas
            System.out.println("Listado de viajes disponibles para la cantidad de personas selecionadas:");
            ViajeService.buscarViajesDisponibles(numPlazasReservadas);
            
            // Solicitar el ID del viaje al usuario
            System.out.println("Indica en que viaje quiere hacer la reserva:");
            int viajeId = keyboard.nextInt();
            
            // Obtener el viaje seleccionado
            Query<Viaje> query1 = session.createQuery("FROM Viaje as v WHERE v.id = :viaje", Viaje.class);
            query1.setParameter("viaje", viajeId);
            Viaje unViaje = query1.uniqueResult();
            
            // Actualizar el número de plazas disponibles en el viaje
            unViaje.setPlazasDisponibles(unViaje.getPlazasDisponibles() - numPlazasReservadas); 

            // Solicitar los nombres de los pasajeros y añadirlos a la lista
            for (int i = 1; i <= numPlazasReservadas; i++) {
            	keyboard.nextLine(); //Limpiar el buffer
                System.out.println("Indica el nombre del pasajero:");
                name = keyboard.nextLine();
                
                // Buscar el pasajero en la base de datos
                Query<Pasajero> query2 = session.createQuery("FROM Pasajero as p WHERE p.nombre = :nombre", Pasajero.class);
                query2.setParameter("nombre", name);
                Pasajero unPasajero = query2.uniqueResult();
                pasajeros.add(unPasajero);
                
                System.out.println(pasajeros.toString());
            }

            // Crear un nuevo objeto Reserva
            System.out.println("Creando una reserva...");
            
            Query<Reserva> queryCheck = session.createQuery("FROM Reserva as r WHERE r.viaje.id = :viaje", Reserva.class);
            queryCheck.setParameter("viaje", viajeId);
            Reserva nuevaReserva = queryCheck.uniqueResult();
            
            System.out.println("Nueva reserva...");
            nuevaReserva = new Reserva(date, numPlazasReservadas, unViaje, pasajeros);
            
            // Asignar la reserva a cada pasajero y guardar los cambios en la base de datos
            for (Pasajero pasajero : pasajeros) { 
                pasajero.setReserva(nuevaReserva); 
                session.persist(pasajero); 
            }

            // Guardar la reserva en la base de datos
            System.out.println("Guardando la reserva...");
            session.persist(nuevaReserva);

            // Hacer commit de la transacción
            session.getTransaction().commit();

            System.out.println("Hecho!");

        } catch (Exception e) {
            // Hacer rollback en caso de una excepción
            System.out.println("Realizando Rollback");
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            cerrarSesion();
        }
    }

    public static void cancelarReserva(Scanner keyboard) {
        abrirSesion();

        try {
            // Iniciar una transacción
            session.beginTransaction();

            // Listar todas las reservas
            listarReservas();
            
            // Solicitar el ID de la reserva a cancelar
            System.out.println("Indica la reserva que quiere cancelar:");
            int id = keyboard.nextInt();

            // Obtener la reserva seleccionada
            Query<Reserva> query3 = session.createQuery("FROM Reserva as r WHERE r.id = :id", Reserva.class);
            query3.setParameter("id", id);
            Reserva unaReserva = query3.uniqueResult(); 
         
            // Actualizar la reserva de los pasajeros a null
            Query updateQuery = session.createQuery("UPDATE Pasajero SET reserva = null WHERE reserva.id = :id"); 
            updateQuery.setParameter("id", id); 
            updateQuery.executeUpdate();
            
            
            // Actualizar el número de plazas disponibles en el viaje
            int plazasReservadas = unaReserva.getNumPlazasReservadas();
            int viajeId = unaReserva.getViaje().getId();
            
            Query updateQuery2 = session.createQuery("UPDATE Viaje SET plazasDisponibles = plazasDisponibles + :plazasReservadas WHERE id = :viajeId");
            updateQuery2.setParameter("plazasReservadas", plazasReservadas);
            updateQuery2.setParameter("viajeId", viajeId);
            updateQuery2.executeUpdate();
            
            // Eliminar la reserva de la base de datos
            session.remove(unaReserva);
            
            // Hacer commit de la transacción
            session.getTransaction().commit();
                    
            System.out.println("Hecho!");

        } catch (Exception e) {
            // Hacer rollback en caso de una excepción
            System.out.println("Realizando Rollback");
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            cerrarSesion();
        }
    }
    
    public static void listarReservas() {
        // Obtener y listar todas las reservas
        Query<Reserva> query1 = session.createQuery("FROM Reserva", Reserva.class);
        List<Reserva> reservas = query1.list();

        for (Reserva unaReserva : reservas) {
            System.out.println(unaReserva);
        }
    }
}
