package com.remiNorte.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.remiNorte.app.models.entity.Remis;

@Transactional
public interface IRemisDao extends CrudRepository<Remis, Long> {

	@Query(value = "select * from remises where remestado = 'S'", nativeQuery = true) //S=disponible N=no disponible
	public List<Remis> devRemisesDisponibles();
	
	@Query(value = "select * from remises  where remestado = 'S' and (remnomconductor like concat('%',:nombre,'%') or remapeconductor like  concat('%',:nombre,'%'))", 
			nativeQuery = true)
	public List<Remis> devRemisesPorNombre(String nombre);
	
	public Remis findByRemPatente(String patente);
}
