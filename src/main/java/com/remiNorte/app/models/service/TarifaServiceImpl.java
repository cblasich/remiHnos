package com.remiNorte.app.models.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remiNorte.app.models.dao.ITarifaDao;
import com.remiNorte.app.models.entity.Tarifa;

@Service
public class TarifaServiceImpl implements ITarifaService {
	
	@Autowired
	private ITarifaDao tarifaDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Tarifa> findAll() {
		return (List<Tarifa>) tarifaDao.findAll();
	}

	@Override
	@Transactional //sin readonly ya que es un metodo de escritura
	public void save(Tarifa tarifa) {
		tarifaDao.save(tarifa);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Tarifa findOne(Long id) {
		return tarifaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		tarifaDao.deleteById(id);
	}
	
	@Override
	@Transactional
	public Tarifa findByTarVigHas(Date fecha) {
		return tarifaDao.findByTarFecVigHas(fecha);
	}
	
	@Override
	@Transactional
	public Tarifa devTarAnt(Long tarId) {
		return tarifaDao.devTarAnt(tarId);
	}
	
	@Override
	@Transactional
	public Long devCanTarifas() {
		return tarifaDao.count();
	}
}
