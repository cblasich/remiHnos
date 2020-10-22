package com.remiNorte.app.models.dao;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.remiNorte.app.models.entity.Tarifa;

public interface ITarifaDao extends PagingAndSortingRepository<Tarifa, Long>{

	public Tarifa findByTarFecVigHas(Date fecha);
	
	@Query( value = "select * from tarifas where tarid < :tarId order by tarid desc limit 1",
			nativeQuery = true)
	public Tarifa devTarAnt(Long tarId);
	
	public Page<Tarifa> findAll(Pageable pageable);
}
