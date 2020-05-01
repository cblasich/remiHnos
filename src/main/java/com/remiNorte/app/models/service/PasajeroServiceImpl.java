package com.remiNorte.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remiNorte.app.models.dao.IPasajeroDao;
import com.remiNorte.app.models.entity.Pasajero;

@Service
public class PasajeroServiceImpl implements IPasajeroService {

	@Autowired
	private IPasajeroDao pasajeroDao; 

	@Override
	@Transactional(readOnly = true)
	public List<Pasajero> findAll() {
		return (List<Pasajero>) pasajeroDao.findAll();
	}
	
	@Override
	@Transactional 
	public void save(Pasajero pasajero) {
		pasajeroDao.save(pasajero);
	}

	@Override
	@Transactional(readOnly = true)
	public Pasajero findOne(Long id) {
		return pasajeroDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		pasajeroDao.deleteById(id);	
	}


	
}
