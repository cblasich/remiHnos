package com.remiNorte.app.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.remiNorte.app.validations.RemisCodigo;

@Entity
@Table(name = "viajes")
public class Viaje implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="viaid")
	private Long ViaId; 
	
	@NotEmpty
	@Column(name="viacalleori", length=100)
	private String ViaCalleOri;
	
	@NotNull
	@Column(name="vianumori")
	private Integer ViaNumOri;
	
	@Column(name="viacalledes", length=100)
	private String ViaCalleDes;
	
	@Column(name="vianumdes")
	private Integer ViaNumDes;
	
	@Temporal(TemporalType.DATE)
	@Column(name="viafecsol")
	private Date ViaFecSol;

	@Temporal(TemporalType.TIME)
	@Column(name="viahorsol")
	private Date ViaHorSol;
	
	@Column(name="vismonest")
	private Double ViaMonEst;
	
	@Column(name="viamonfin")
	private Double ViaMonFin;
	
	@Column(name="viaforpag", length=2)
	@NotEmpty
	private String ViaForPag;
	
	@NotEmpty(message = "Debe ingresar un teléfono.")
	@Column(name="viatelefono", length=15)
	private String ViaTelefono;
	
	@Column(name="viaestado", length=1)
	@NotEmpty
	private String ViaEstado;
	
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY) 
	@JoinColumn(name="pasid")
	private Pasajero Pasajero;
	
	@RemisCodigo
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY) 
	@JoinColumn(name="remid")
	private Remis Remis;
	
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)  
	@JoinColumn(name="tarid")
	private Tarifa Tarifa;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) 
	@JoinColumn(name="opeid")
	private Operador Operador;
	
	@PrePersist
	public void PrePersist() {  //este metodo se ejecutará antes de insertar registro en la BDD
		ViaFecSol = new Date();
		ViaHorSol = new Date();
		ViaEstado = "P";
	}
	
	public Long getViaId() {
		return ViaId;
	}

	public void setViaId(Long viaId) {
		ViaId = viaId;
	}

	public String getViaCalleOri() {
		return ViaCalleOri;
	}

	public void setViaCalleOri(String viaCalleOri) {
		ViaCalleOri = viaCalleOri;
	}

	public Integer getViaNumOri() {
		return ViaNumOri;
	}

	public void setViaNumOri(Integer viaNumOri) {
		ViaNumOri = viaNumOri;
	}

	public String getViaCalleDes() {
		return ViaCalleDes;
	}

	public void setViaCalleDes(String viaCalleDes) {
		ViaCalleDes = viaCalleDes;
	}

	public Integer getViaNumDes() {
		return ViaNumDes;
	}

	public void setViaNumDes(Integer viaNumDes) {
		ViaNumDes = viaNumDes;
	}

	public Date getViaFecSol() {
		return ViaFecSol;
	}

	public void setViaFecSol(Date viaFecSol) {
		ViaFecSol = viaFecSol;
	}

	public Date getViaHorSol() {
		return ViaHorSol;
	}

	public void setViaHorSol(Date viaHorSol) {
		ViaHorSol = viaHorSol;
	}

	public Double getViaMonEst() {
		return ViaMonEst;
	}

	public void setViaMonEst(Double viaMonEst) {
		ViaMonEst = viaMonEst;
	}

	public Double getViaMonFin() {
		return ViaMonFin;
	}

	public void setViaMonFin(Double viaMonFin) {
		ViaMonFin = viaMonFin;
	}

	public String getViaForPag() {
		return ViaForPag;
	}

	public void setViaForPag(String viaForPag) {
		ViaForPag = viaForPag;
	}

	public String getViaTelefono() {
		return ViaTelefono;
	}

	public void setViaTelefono(String viaTelefono) {
		ViaTelefono = viaTelefono;
	}

	public String getViaEstado() {
		return ViaEstado;
	}

	public void setViaEstado(String viaEstado) {
		ViaEstado = viaEstado;
	}

	public Pasajero getPasajero() {
		return Pasajero;
	}

	public void setPasajero(Pasajero pasajero) {
		this.Pasajero = pasajero;
	}

	public Remis getRemis() {
		return Remis;
	}

	public void setRemis(Remis remis) {
		this.Remis = remis;
	}

	public Tarifa getTarifa() {
		return Tarifa;
	}

	public void setTarifa(Tarifa tarifa) {
		this.Tarifa = tarifa;
	}

	public Operador getOperador() {
		return Operador;
	}

	public void setOperador(Operador operador) {
		this.Operador = operador;
	}
	
}

	
	
