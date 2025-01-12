package eus.birt.dam.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="reserva")
public class Reserva {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) //La opción más usada con MySQL
	@Column(name="id")
	private int id;
	
	@Column(name="fechaReserva")
	private LocalDate fechaReserva;
	
	@Column(name="numPlazasReservadas")
	private int numPlazasReservadas;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn (name="viaje_id")
	private Viaje viaje;
	
	@OneToMany(mappedBy = "reserva", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<Pasajero> pasajeros = new ArrayList<>();


	public Reserva() {
		
	}
	

	public Reserva(LocalDate fechaReserva, int numPlazasReservadas, Viaje viaje, List<Pasajero> pasajeros) {
		super();
		this.fechaReserva = fechaReserva;
		this.numPlazasReservadas = numPlazasReservadas;
		this.viaje = viaje;
		this.pasajeros = pasajeros;
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public LocalDate getFechaReserva() {
		return fechaReserva;
	}


	public void setFechaReserva(LocalDate fechaReserva) {
		this.fechaReserva = fechaReserva;
	}


	public int getNumPlazasReservadas() {
		return numPlazasReservadas;
	}


	public void setNumPlazasReservadas(int numPlazasReservadas) {
		this.numPlazasReservadas = numPlazasReservadas;
	}


	public Viaje getViaje() {
		return viaje;
	}


	public void setViaje(Viaje viaje) {
		this.viaje = viaje;
	}


	public List<Pasajero> getPasajeros() {
		return pasajeros;
	}


	public void setPasajeros(List<Pasajero> pasajeros) {
		this.pasajeros = pasajeros;
	}


	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("Detalles de la Reserva:\n");
	    sb.append("*************************\n");
	    sb.append("ID Reserva: ").append(id).append("\n");
	    sb.append("Fecha de Reserva: ").append(fechaReserva).append("\n");
	    sb.append("Número de Plazas Reservadas: ").append(numPlazasReservadas).append("\n");
	    sb.append(viaje).append("\n");
	    sb.append("Pasajeros: \n");
	    
	    for (Pasajero pasajero : pasajeros) {
	        sb.append(pasajero).append("\n");
	    }
	    
	    sb.append("*************************\n");
	    return sb.toString();
	}


}