package com.remiNorte.app.models.service;

import java.util.List;

import com.remiNorte.app.models.entity.Pasajero;

public interface IPasajeroService {

	public List<Pasajero> findAll();

	public void save(Pasajero pasajero); // "contrato" de implementacion para guardar un nuevo cliente en la base de datos

	public Pasajero findOne(Long id);

	public void delete(Long id);
	
}
