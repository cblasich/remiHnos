package com.remiNorte.app.models.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.remiNorte.app.models.entity.Pasajero;
import com.remiNorte.app.models.entity.Usuario;

//tambien puede ser: extends JpaRepository , que implementa mas cosas, como paginación de resultados.
@Repository
public interface IPasajeroDao extends CrudRepository<Pasajero, Long> {  //Long es el tipo de dato de la clave primaria de Pasajero

	public Pasajero findByUsuario(Usuario usuario);
}
