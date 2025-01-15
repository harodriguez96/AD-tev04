package eus.birt.dam.service;

import org.hibernate.Session;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import java.util.Scanner;

import eus.birt.dam.domain.Conductor;
import eus.birt.dam.service.*;

public class ConductorService {
    public static void crearConductor(Scanner keyboard) {
        
        // Cambiar el nivel de logging para org.hibernate
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.hibernate")).setLevel(ch.qos.logback.classic.Level.WARN);
        
        // Crear el StandardServiceRegistry usando la configuración en hibernate.cfg.xml
        StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();

        // Crear el Metadata utilizando el StandardServiceRegistry y añadir la clase anotada Conductor
        Metadata metadata = new MetadataSources(standardRegistry)
                .addAnnotatedClass(Conductor.class)
                .getMetadataBuilder()
                .build();

        // Construir el SessionFactory utilizando el Metadata
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder()
                .build();    
        
        // Abrir una nueva sesión
        Session session = sessionFactory.openSession();
        
        try {
            // Solicitar el nombre del conductor al usuario
            System.out.println("Indica el nombre del conductor:");
            String nombre = keyboard.nextLine(); 

            // Solicitar el vehículo que conduce al usuario
            System.out.println("Indica el vehiculo que se conduce:");
            String vehiculo = keyboard.nextLine(); 
            
            // Crear un nuevo objeto Conductor con los datos proporcionados
            System.out.println("Creando un nuevo objeto conductor...");
            Conductor nuevoConductor = new Conductor(nombre, vehiculo);
            
            // Iniciar una transacción
            session.beginTransaction();
            
            // Guardar el objeto Conductor en la base de datos
            System.out.println("Guardando el conductor...");
            session.persist(nuevoConductor);
            
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
