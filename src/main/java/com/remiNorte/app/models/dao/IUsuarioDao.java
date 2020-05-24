package com.remiNorte.app.models.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.remiNorte.app.models.entity.Usuario;

@Transactional
public interface IUsuarioDao extends CrudRepository<Usuario, Long>{
	
	public Usuario findByUsername(String username); //findby + nombre del campo
	
}
