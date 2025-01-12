package eus.birt.dam.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="conductor")
public class Conductor {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) //La opción más usada con MySQL
	@Column(name="id")
	private int id;
	
	@Column(name="nombre")
	private String nombre;
	
	@Column(name="vehiculo")
	private String vehiculo;
	
	
	public Conductor() {
		
	}


	public Conductor(String nombre, String vehiculo) {
		super();
		this.nombre = nombre;
		this.vehiculo = vehiculo;
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


	public String getVehiculo() {
		return vehiculo;
	}


	public void setVehiculo(String vehiculo) {
		this.vehiculo = vehiculo;
	}
	
	@Override
	public String toString() { 
		return "Conductor:\n" + " - ID: " + id + "\n" + " - Nombre: " + nombre + "\n" + " - Vehículo: " + vehiculo;
	}

	
	
}
