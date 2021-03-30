package com.remiNorte.app.models.service;

import com.remiNorte.app.models.entity.Empresa;

public interface IEmpresaService {

	Empresa findOne(int i);

	public void save(Empresa empresa);
	
}
