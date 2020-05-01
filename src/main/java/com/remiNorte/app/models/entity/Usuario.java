package com.remiNorte.app.models.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "usuid")
	private Long UsuId;
	
	//LOGIN
	@Column(name = "username", length = 100, unique = true)
	private String username;

	@Column(name = "password", length = 60)
	private String password;

	@Column(name = "usuenabled")
	private Boolean UsuEnabled; // debe setearse 1 por defecto

	//OTROS ATRIBUTOS
	@OneToOne(mappedBy = "usuario",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Operador operador;

	@OneToOne(mappedBy = "usuario",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "pasid")
	private Pasajero pasajero;
	
	@ManyToOne
	private Rol rol;
	
	public Operador getOperador() {
		return operador;
	}

	public void setOperador(Operador operador) {
		this.operador = operador;
	}

	public Pasajero getPasajero() {
		return pasajero;
	}

	public void setPasajero(Pasajero pasajero) {
		this.pasajero = pasajero;
	}
	
	//GETTERS AND SETTERS
	public Boolean getUsuEnabled() {
		return UsuEnabled;
	}

	public void setUsuEnabled(Boolean usuEnabled) {
		UsuEnabled = usuEnabled;
	}

	public Long getUsuId() {
		return UsuId;
	}

	public void setUsuId(Long usuId) {
		UsuId = usuId;
	}

	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}




	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
