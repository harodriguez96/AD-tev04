package eus.birt.dam.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.*;
import eus.birt.dam.domain.*;



@Entity
@Table(name="pasajero")
public class Pasajero {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) //La opción más usada con MySQL
	@Column(name="id")
	private int id;
	
	@Column(name="nombre")
	private String nombre;
	
	@Column(name="email")
	private String email;
	
	@ManyToOne
	@JoinColumn (name = "reserva_id")
	private Reserva reserva;
	
	public Pasajero() {
		
	}


	public Pasajero(String nombre, String email) {
		super();
		this.nombre = nombre;
		this.email = email;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}
	
	public Reserva getReserva() {
		return reserva;
	}


	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}
	
	@Override
    public String toString() {
        return "Pasajero{" +
                "nombre='" + nombre + '\'' +
                ", email=" + email +
                '}';
    }

	
}