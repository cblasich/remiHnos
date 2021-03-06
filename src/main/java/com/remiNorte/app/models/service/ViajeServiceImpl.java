package com.remiNorte.app.models.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remiNorte.app.models.dao.IViajeDao;

@Service
public class ViajeServiceImpl implements IViajeService {
	
	@Autowired
	IViajeDao viajeDao;
	
	@Override
	@Transactional
	public Long devCanTarViajes(Long tarifaId) {
		
		return viajeDao.devCanTarViajes(tarifaId);
	}

	@Override
	public Long canRemVia(Long remisId) {
		// TODO Auto-generated method stub
		return viajeDao.canRemVia(remisId);
	}
	
	@Override
	public Long canOpeVia(Long opeId) {
		// TODO Auto-generated method stub
		return viajeDao.canOpeVia(opeId);
	}
}
