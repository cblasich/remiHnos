package com.remiNorte.app.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.remiNorte.app.models.entity.Empresa;

public interface IEmpresaDao extends PagingAndSortingRepository<Empresa, Long>{

}
