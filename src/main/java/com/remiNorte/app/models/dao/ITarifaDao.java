package com.remiNorte.app.models.dao;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;

import com.remiNorte.app.models.entity.Tarifa;

public interface ITarifaDao extends CrudRepository<Tarifa, Long>{

	Tarifa findByTarFecVigHas(Date fechaNula);
	
}
