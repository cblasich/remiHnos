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
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.remiNorte.app.validations.EmailDisponible;
import com.remiNorte.app.validations.EmailNotEmpty;



@Entity
@Table(name = "operadores")
public class Operador implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="opeid")
	private Long OpeId;
	
	@NotEmpty(message = "Debe ingresar un nombre.")
	@Column(name="openombre", length=40)
	private String OpeNombre;

	@NotEmpty(message = "Debe ingresar un apellido.")
	@Column(name="opeapellido", length=40)
	private String OpeApellido;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "opeusuid", referencedColumnName = "usuid")
	@EmailNotEmpty
	@EmailDisponible	
	private Usuario usuario;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL,mappedBy="Operador")
	private List<Viaje> Viajes;
	
	public Operador() {
		Viajes = new ArrayList<Viaje>();
	}
	
	public Long getOpeId() {
		return OpeId;
	}

	public String getOpeNombre() {
		return OpeNombre;
	}

	public void setOpeNombre(String opeNombre) {
		OpeNombre = opeNombre;
	}

	public String getOpeApellido() {
		return OpeApellido;
	}

	public void setOpeApellido(String opeApellido) {
		OpeApellido = opeApellido;
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
