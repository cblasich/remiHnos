package com.remiNorte.app.models.entity;

import javax.validation.constraints.NotNull;

public class UsuarioDTO {  //clase que transferira todos los datos del usuario
//REMI NORTE

	@NotNull
	private String username;
	
	@NotNull
	private String password;
	
	private Long UsuId;

	public Long getUsuId() {
		return UsuId;
	}
	public void setUsuId(Long usuId) {
		UsuId = usuId;
	}
	
	private Pasajero pasajero;
	private Operador operador;
	private Rol rol;
	
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
	public Pasajero getPasajero() {
		return pasajero;
	}
	public void setPasajero(Pasajero pasajero) {
		this.pasajero = pasajero;
	}
	public Operador getOperador() {
		return operador;
	}
	public void setOperador(Operador operador) {
		this.operador = operador;
	}
	public Rol getRol() {
		return rol;
	}
	public void setRol(Rol rol) {
		this.rol = rol;
	}
}
