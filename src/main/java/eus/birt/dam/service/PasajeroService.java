package eus.birt.dam.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

import eus.birt.dam.domain.*;

public class PasajeroService {
    public static void crearPasajero(Scanner keyboard) {
        
        // Cambiar el nivel de logging para org.hibernate
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.hibernate")).setLevel(ch.qos.logback.classic.Level.WARN);
        
        // Crear el StandardServiceRegistry usando la configuración en hibernate.cfg.xml
        StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();

        // Crear el Metadata utilizando el StandardServiceRegistry y añadir la clase anotada Pasajero
        Metadata metadata = new MetadataSources(standardRegistry)
                .addAnnotatedClass(Pasajero.class)
                .getMetadataBuilder()
                .build();

        // Construir el SessionFactory utilizando el Metadata
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder()
                .build();    
        
        // Abrir una nueva sesión
        Session session = sessionFactory.openSession();
        
        try {            
            // Solicitar el nombre del pasajero al usuario
            System.out.println("Indica el nombre del pasajero:");
            String nombre = keyboard.nextLine();
            
            // Solicitar el email del pasajero al usuario
            System.out.println("Indica el email del pasajero:");
            String email = keyboard.nextLine();

            // Crear un nuevo objeto Pasajero con los datos proporcionados
            System.out.println("Creando un nuevo objeto pasajero...");
            Pasajero nuevoPasajero = new Pasajero(nombre, email);
            
            // Iniciar una transacción
            session.beginTransaction();
            
            // Guardar el objeto Pasajero en la base de datos
            System.out.println("Guardando el pasajero...");
            session.persist(nuevoPasajero);
            
            // Hacer commit de la transacción
            session.getTransaction().commit();
            
            System.out.println("Hecho!");
                        
        } catch (Exception e) {
            // Hacer rollback en caso de una excepción
            System.out.println("Realizando Rollback");
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            // Cerrar la sesión
            session.close();
        }
    }   
}
