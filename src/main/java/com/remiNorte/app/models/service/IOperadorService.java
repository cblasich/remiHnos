package com.remiNorte.app.models.service;

import java.util.List;

import com.remiNorte.app.models.entity.Operador;

public interface IOperadorService {
	
	public List<Operador> findAll();

	public Operador findOne(Long id);

	public void delete(Long id);

	public void save(Operador remis);

}
