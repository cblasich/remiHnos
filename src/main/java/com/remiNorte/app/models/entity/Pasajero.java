package com.remiNorte.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;



@Entity
@Table(name="pasajeros")
public class Pasajero implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="pasid")
	private Long PasId;
	
	@Column(name="pasnombre", length=40)
	private String PasNombre;
	
	@Column(name="pasapellido", length=40)
	@NotEmpty
	private String PasApellido;
	
	@Column(name="pastelefono", length=15)
	@NotEmpty
	private String PasTelefono;
	
	@Column(name="pasestado", length=1)
	private String PasEstado;

	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "pasusuid", referencedColumnName = "usuid")
	private Usuario usuario;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL,mappedBy="Pasajero")
	private List<Viaje> Viajes;
	
	public Pasajero() {
		Viajes = new ArrayList<Viaje>();
	}

	@PrePersist
	public void PrePersist() {  //este metodo se ejecutará antes de insertar registro en la BDD
		PasEstado = "P";
	}
	
	public String getPasNombre() {
		return PasNombre;
	}

	public void setPasNombre(String pasNombre) {
		PasNombre = pasNombre;
	}

	public String getPasApellido() {
		return PasApellido;
	}

	public void setPasApellido(String pasApellido) {
		PasApellido = pasApellido;
	}

	public String getPasTelefono() {
		return PasTelefono;
	}

	public void setPasTelefono(String pasTelefono) {
		PasTelefono = pasTelefono;
	}

	public String getPasEstado() {
		return PasEstado;
	}

	public void setPasEstado(String pasEstado) {
		PasEstado = pasEstado;
	}

	public Long getPasId() {
		return PasId;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Viaje> getViajes() {
		return Viajes;
	}

	public void setViajes(List<Viaje> viajes) {
		Viajes = viajes;
	}	
	
	public void addViaje(Viaje viaje) { 
		Viajes.add(viaje);
	}
	
}
