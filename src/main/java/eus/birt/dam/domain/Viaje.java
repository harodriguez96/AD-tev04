package eus.birt.dam.domain;

import jakarta.persistence.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="viaje")
public class Viaje {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) //La opción más usada con MySQL
	@Column(name="id")
	private int id;
	
	@Column(name="ciudadDestino")
	private String ciudadDestino;
	
	@Column(name="ciudadOrigen")
	private String ciudadOrigen;
	
	@Column(name="fechaHora")
	private LocalDateTime fechaHora;
	
	@Column(name="plazasDisponibles")
	private int plazasDisponibles;
	
	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn (name="conductor_id")
	private Conductor conductor;
	
	@OneToMany (mappedBy="viaje", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	List <Reserva> reservas = new ArrayList<>();
	
	
	public Viaje() {
		
	}


	public Viaje(String ciudadOrigen, String ciudadDestino,  LocalDateTime fechaHora, int plazasDisponibles, Conductor conductor) {
		super();
		this.id = id;
		this.ciudadDestino = ciudadDestino;
		this.ciudadOrigen = ciudadOrigen;
		this.fechaHora = fechaHora;
		this.plazasDisponibles = plazasDisponibles;
		this.conductor = conductor;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getCiudadDestino() {
		return ciudadDestino;
	}


	public void setCiudadDestino(String ciudadDestino) {
		this.ciudadDestino = ciudadDestino;
	}


	public String getCiudadOrigen() {
		return ciudadOrigen;
	}


	public void setCiudadOrigen(String ciudadOrigen) {
		this.ciudadOrigen = ciudadOrigen;
	}


	public LocalDateTime getFechaHora() {
		return fechaHora;
	}


	public void setFechaHora(LocalDateTime fechaHora) {
		this.fechaHora = fechaHora;
	}


	public int getPlazasDisponibles() {
		return plazasDisponibles;
	}


	public void setPlazasDisponibles(int plazasDisponibles) {
		this.plazasDisponibles = plazasDisponibles;
	}


	public Conductor getConductor() {
		return conductor;
	}


	public void setConductor(Conductor conductor) {
		this.conductor = conductor;
	}


	@Override
	public String toString() {
	    return "Detalles del Viaje:\n" +
	           "------------------------\n" +
	           "ID Viaje: " + id + "\n" +
	           "Ciudad de Destino: " + ciudadDestino + "\n" +
	           "Ciudad de Origen: " + ciudadOrigen + "\n" +
	           "Fecha y Hora: " + fechaHora + "\n" +
	           "Plazas Disponibles: " + plazasDisponibles + "\n" +
	           conductor + "\n" +
	           "------------------------\n";
	}

	
}