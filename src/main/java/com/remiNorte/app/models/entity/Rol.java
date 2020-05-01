package com.remiNorte.app.models.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class Rol implements Serializable {

	@Id
	@Column(name = "rolid")
	private Long RolId;
	
	@Column(name = "rolnombre")
	private String RolNombre;
	
	@OneToMany(mappedBy = "rol")
	private List<Usuario> usuarios;

	public Long getRolId() {
		return RolId;
	}

	public void setRolId(Long rolId) {
		RolId = rolId;
	}

	public String getRolNombre() {
		return RolNombre;
	}

	public void setRolNombre(String rolNombre) {
		this.RolNombre = rolNombre;
	}
	
	public void setRolUser() {
		this.RolId = (long) 1;
		this.RolNombre = "ROLE_ADMIN";
	}
	
	public void setRolAdmin() {
		this.RolId = (long) 2;
		this.RolNombre = "ROLE_OPERAD";
	}
	
	public void setRolOperad() {
		this.RolId = (long) 3;
		this.RolNombre = "ROLE_USER";
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
