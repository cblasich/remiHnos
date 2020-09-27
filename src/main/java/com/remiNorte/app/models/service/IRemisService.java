package com.remiNorte.app.models.service;

import java.util.List;

import com.remiNorte.app.models.entity.Remis;

public interface IRemisService {

	public List<Remis> findAll();

	public Remis findOne(Long id);

	public void delete(Long id);

	public void save(Remis remis);

	public List<Remis> devRemiserosDisponibles();

	public List<Remis> devRemisesPorNombre(String nombre);
	
	public Remis findByRemPatente(String patente);

}
