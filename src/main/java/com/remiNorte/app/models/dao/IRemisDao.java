package com.remiNorte.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.remiNorte.app.models.entity.Remis;

public interface IRemisDao extends CrudRepository<Remis, Long> {
	
}
