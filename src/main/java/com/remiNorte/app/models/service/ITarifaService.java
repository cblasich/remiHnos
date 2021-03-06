package com.remiNorte.app.models.service;

import java.util.Date;
import java.util.List;

import com.remiNorte.app.models.entity.Tarifa;

public interface ITarifaService {

	List<Tarifa> findAll();

	Tarifa findOne(Long id);

	void delete(Long id);

	public void save(Tarifa tarifa);

	public Tarifa findByTarVigHas(Date fecha);

	public Tarifa devTarAnt(Long tarId);  
	
	public Long devCanTarifas();
	
}
