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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;


@Entity
@Table(name="remises")
public class Remis implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="remid")
	private Long RemId;
	
	@Column(name="rempatente", length=10, unique=true)
	@NotEmpty(message = "Debe ingresar una patente.")
	private String RemPatente;
	
	@Column(name="remmodelo", length=30)
	@NotEmpty(message = "Debe ingresar un modelo.")
	private String RemModelo;
	
	@Column(name="remmarca", length=30)
	@NotEmpty(message = "Debe ingresar una marca.")
	private String RemMarca;
	
	@Column(name="remnomconductor", length=40)
	@NotEmpty(message = "Debe ingresar un nombre.")
	private String RemNomConductor;
	
	@Column(name="remapeconductor", length=40)
	@NotEmpty(message = "Debe ingresar un apellido.")
	private String RemApeConductor;

	@Column(name="remestado", length=1)
	private String RemEstado;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL,mappedBy="Remis")
	private List<Viaje> Viajes;
	
	public Remis() {
		Viajes = new ArrayList<Viaje>();
	} 
	
	public Long getRemId() {
		return RemId;
	}

	public String getRemPatente() {
		return RemPatente;
	}

	public void setRemPatente(String remPatente) {
		RemPatente = remPatente;
	}

	public String getRemModelo() {
		return RemModelo;
	}

	public void setRemModelo(String remModelo) {
		RemModelo = remModelo;
	}

	public String getRemMarca() {
		return RemMarca;
	}

	public void setRemMarca(String remMarca) {
		RemMarca = remMarca;
	}

	public String getRemNomConductor() {
		return RemNomConductor;
	}

	public void setRemNomConductor(String remNomConductor) {
		RemNomConductor = remNomConductor;
	}

	public String getRemApeConductor() {
		return RemApeConductor;
	}

	public void setRemApeConductor(String remApeConductor) {
		RemApeConductor = remApeConductor;
	}

	public String getRemEstado() {
		return RemEstado;
	}

	public void setRemEstado(String remEstado) {
		RemEstado = remEstado;
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

	public void setRemId(Long remId) {
		RemId = remId;
	}
	
	
}
