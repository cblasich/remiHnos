package com.remiNorte.app.models.service;

import java.util.List;

import com.remiNorte.app.models.entity.Usuario;
import com.remiNorte.app.models.entity.UsuarioDTO;
import com.remiNorte.app.validations.UsuarioExisteException;

public interface IUsuarioService {

	public List<Usuario> findAll();

	public Usuario findOne(Long id);

	public void delete(Long id);

	public void save(Usuario usuario);

	Usuario registrarNuevoUsuario(UsuarioDTO cuentaDTO) throws UsuarioExisteException;
}
