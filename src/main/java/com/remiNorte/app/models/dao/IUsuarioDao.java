package com.remiNorte.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.remiNorte.app.models.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long>{
	
	public Usuario findByUsername(String username); //findby + nombre del campo
}
