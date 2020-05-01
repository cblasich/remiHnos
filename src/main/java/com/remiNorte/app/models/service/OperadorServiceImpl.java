package com.remiNorte.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remiNorte.app.models.dao.IOperadorDao;
import com.remiNorte.app.models.entity.Operador;

@Service
public class OperadorServiceImpl implements IOperadorService {

	@Autowired
	private IOperadorDao operadorDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Operador> findAll() {
		return (List<Operador>) operadorDao.findAll();
	}

	@Override
	@Transactional //sin readonly ya que es un metodo de escritura
	public void save(Operador operador) {
		operadorDao.save(operador);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Operador findOne(Long id) {
		return operadorDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		operadorDao.deleteById(id);
	}

}
