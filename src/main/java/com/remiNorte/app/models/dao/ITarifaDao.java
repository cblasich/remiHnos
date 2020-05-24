package com.remiNorte.app.models.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.remiNorte.app.models.entity.Tarifa;

public interface ITarifaDao extends CrudRepository<Tarifa, Long>{

	public Tarifa findByTarFecVigHas(Date fecha);
	
	@Query( value = "select * from tarifas where tarid < :tarId order by tarid desc limit 1",
			nativeQuery = true)
	public Tarifa devTarAnt(Long tarId);
}
