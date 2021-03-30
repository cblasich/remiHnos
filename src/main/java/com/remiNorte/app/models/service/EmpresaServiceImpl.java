package com.remiNorte.app.models.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remiNorte.app.models.dao.IEmpresaDao;
import com.remiNorte.app.models.entity.Empresa;

@Service
public class EmpresaServiceImpl implements IEmpresaService {
	
	@Autowired
	private IEmpresaDao empresaDao;

	@Override
	@Transactional //sin readonly ya que es un metodo de escritura
	public void save(Empresa empresa) {
		empresaDao.save(empresa);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Empresa findOne(int id) {
		return empresaDao.findById((long) id).orElse(null);
	}

}
