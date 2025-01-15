package eus.birt.dam.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.LoggerFactory;
import org.hibernate.query.Query;

import eus.birt.dam.domain.Reserva;
import eus.birt.dam.domain.Conductor;
import eus.birt.dam.domain.Pasajero;
import eus.birt.dam.domain.Viaje;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class ViajeService {
    
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
    
    public static void crearViaje(Scanner keyboard) {
        
        abrirSesion();
        LocalDateTime dateTime = null;
        
        try {
            // Solicitar la ciudad de origen al usuario
            System.out.println("Indica la ciudad de origen:");
            String origen = keyboard.nextLine();
            
            // Solicitar la ciudad de destino al usuario
            System.out.println("Indica la ciudad de destino:");
            String destino = keyboard.nextLine();
            
            // Solicitar la fecha y hora del viaje al usuario y formatearla
            System.out.println("Indica la fecha y hora del viaje en formato dd-MM-yy:HH:mm (11-01-2025:09:00):");
            String fechaHora = keyboard.next();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy:HH:mm"); 
            dateTime = LocalDateTime.parse(fechaHora, formatter);
            
            // Solicitar el número de plazas disponibles al usuario
            System.out.println("Indica las plazas disponibles:");
            int plazas = keyboard.nextInt();
            
            keyboard.nextLine(); //Limpiar el buffer
            // Solicitar el nombre del conductor al usuario
            System.out.println("Indica el nombre del conductor:");
            String conductor = keyboard.nextLine();
            
            // Buscar el conductor en la base de datos
            Query<Conductor> query1 = session.createQuery("FROM Conductor as c WHERE c.nombre = :nombre", Conductor.class);
            query1.setParameter("nombre", conductor);
            Conductor unConductor = query1.uniqueResult(); 
            
            // Crear un nuevo objeto Viaje
            System.out.println("Creando un nuevo objeto viaje...");
            Viaje nuevoViaje = new Viaje(origen, destino, dateTime, plazas, unConductor);
            
            // Iniciar una transacción
            session.beginTransaction();
            
            // Guardar el objeto Viaje en la base de datos
            System.out.println("Guardando el viaje...");
            session.persist(nuevoViaje);
            
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
    
    public static void buscarViajesDisponibles(int numPlazasReservadas) {
        
        abrirSesion();
        
        // Buscar viajes disponibles con al menos el número de plazas reservadas
        Query<Viaje> query1 = session.createQuery("FROM Viaje as v where v.plazasDisponibles >= :numPlazasReservadas", Viaje.class);
        query1.setParameter("numPlazasReservadas", numPlazasReservadas);
        List<Viaje> viajes = query1.list();
        
        // Imprimir los viajes disponibles
        System.out.println("Viajes disponibles...");
        for (Viaje unViaje : viajes) {
            System.out.println(unViaje);
        }
        
        cerrarSesion();
    }
    
    public static void listarViaje() {
        
        abrirSesion();
        
        // Obtener y listar todos los viajes
        Query<Viaje> query1 = session.createQuery("FROM Viaje", Viaje.class);
        List<Viaje> viajes = query1.list();
        
        for (Viaje unViaje : viajes) {
            System.out.println(unViaje);
        }
        
        cerrarSesion();
    }            
}
