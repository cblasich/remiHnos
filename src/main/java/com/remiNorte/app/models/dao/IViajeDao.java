package com.remiNorte.app.models.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.remiNorte.app.models.entity.Viaje;

@Repository
public interface IViajeDao extends CrudRepository<Viaje, Long> {  //Long es el tipo de dato de la clave primaria de Pasajero

}
