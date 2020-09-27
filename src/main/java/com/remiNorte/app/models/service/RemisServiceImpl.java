package com.remiNorte.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remiNorte.app.models.dao.IRemisDao;
import com.remiNorte.app.models.entity.Remis;

@Service
public class RemisServiceImpl implements IRemisService {
//dentro de esta podriamos tener como atributos varias clases Dao
	@Autowired
	private IRemisDao remisDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Remis> findAll() {
		return (List<Remis>) remisDao.findAll();
	}
	
	@Override
	@Transactional //sin readonly ya que es un metodo de escritura
	public void save(Remis remis) {
		remisDao.save(remis);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Remis findOne(Long id) {
		return remisDao.findById(id).orElse(null);
	}
	
	@Override
	@Transactional
	public void delete(Long id) {
		remisDao.deleteById(id);
	}
	
	@Override
	@Transactional
	public List<Remis> devRemiserosDisponibles(){
		return (List<Remis>) remisDao.devRemisesDisponibles();
	}
	
	@Override
	@Transactional
	public List<Remis> devRemisesPorNombre(String nombre){
		return (List<Remis>) remisDao.devRemisesPorNombre(nombre);
	}

	@Override
	public Remis findByRemPatente(String patente) {
		// TODO Auto-generated method stub
		return remisDao.findByRemPatente(patente);
	}
}
