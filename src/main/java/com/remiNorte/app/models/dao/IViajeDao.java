package com.remiNorte.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.remiNorte.app.models.entity.Viaje;

@Repository
public interface IViajeDao extends CrudRepository<Viaje, Long> {  //Long es el tipo de dato de la clave primaria de Pasajero
	
	@Query(value = "select count(tarid) from viajes where tarid = :tarifaId", nativeQuery = true)
	public Long devCanTarViajes(Long tarifaId);
}
