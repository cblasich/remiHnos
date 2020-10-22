package com.remiNorte.app.models.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.remiNorte.app.models.entity.Pasajero;
import com.remiNorte.app.models.entity.Usuario;

//tambien puede ser: extends JpaRepository , que implementa mas cosas, como paginación de resultados.
@Repository
public interface IPasajeroDao extends PagingAndSortingRepository<Pasajero, Long> {  //Long es el tipo de dato de la clave primaria de Pasajero

	public Pasajero findByUsuario(Usuario usuario);
	
	public Page<Pasajero> findAll(Pageable pageable);
}
