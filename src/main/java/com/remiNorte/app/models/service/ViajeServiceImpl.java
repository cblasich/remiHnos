package com.remiNorte.app.models.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.remiNorte.app.models.dao.IViajeDao;

public class ViajeServiceImpl implements IViajeService {
	
	@Autowired
	IViajeDao viajeDao;
	
	@Override
	@Transactional
	public Long devCanTarViajes(Long tarifaId) {
		
		return viajeDao.devCanTarViajes(tarifaId);
	}
}
