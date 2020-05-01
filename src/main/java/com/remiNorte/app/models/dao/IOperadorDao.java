package com.remiNorte.app.models.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.remiNorte.app.models.entity.Operador;

@Repository
public interface IOperadorDao extends CrudRepository<Operador, Long>{

	/*@Query("SELECT o FROM Operador o WHERE o.OpeId = ?1 ")
	Operador findOpeById(Long id);*/

}
