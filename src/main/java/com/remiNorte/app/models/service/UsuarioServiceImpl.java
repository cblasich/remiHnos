package com.remiNorte.app.models.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remiNorte.app.validations.UsuarioExisteException;
import com.remiNorte.app.models.dao.IUsuarioDao;
import com.remiNorte.app.models.entity.Pasajero;
import com.remiNorte.app.models.entity.Rol;
import com.remiNorte.app.models.entity.Usuario;
import com.remiNorte.app.models.entity.UsuarioDTO;

@Service
public class UsuarioServiceImpl implements IUsuarioService {
	
	@Autowired
	private IUsuarioDao usuarioDao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	@Transactional(readOnly = true)
	public List<Usuario> findAll() {
		return (List<Usuario>) usuarioDao.findAll();
	}

	@Override
	@Transactional
	public void save(Usuario usuario) {
		usuarioDao.save(usuario);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Usuario findOne(Long id) {
		return usuarioDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		usuarioDao.deleteById(id);
	}
	
	
	//// para registro usuario
	@Transactional
	@Override
	public Usuario registrarNuevoUsuario(UsuarioDTO cuentaDTO) 
			throws UsuarioExisteException {
		
		if(usuarioExiste(cuentaDTO.getUsername())) {
			throw new UsuarioExisteException("Ya existe un Usuario registrado con ese Email. Por favor, intente con otro nuevamente.");
		};
		
		Usuario usuario = new Usuario();
		Pasajero pasajero = new Pasajero();
		Rol rol = new Rol();
		rol.setRolUser(); //para que por defecto sea ROLE_USER
		
		
		usuario.setUsername(cuentaDTO.getUsername());
		usuario.setPassword(passwordEncoder.encode(cuentaDTO.getPassword()));
		usuario.setUsuEnabled(true); //deberia habilitarse cuando se manda confirmacion de cuenta!
		//usuario.setRoles(Arrays.asList(rol));
		usuario.setRol(rol);
		
		pasajero.setPasNombre(cuentaDTO.getPasajero().getPasNombre()); //.setNom(cuentaDTO.getCliente().getNom());
		pasajero.setPasApellido(cuentaDTO.getPasajero().getPasApellido());
		pasajero.setPasTelefono(cuentaDTO.getPasajero().getPasTelefono());
		pasajero.setPasEstado("C");
		pasajero.setUsuario(usuario);
		
		pasajero.getUsuario().setUsername(usuario.getUsername());
		usuario.setPasajero(pasajero);
		
		return usuarioDao.save(usuario);
		
	}
	
	private boolean usuarioExiste(String username) {
		Usuario usuario = usuarioDao.findByUsername(username);
		if (usuario!=null) {
			return true;
		}
		return false;
	}
	
	@Override
	@Transactional
	public Usuario findByUsername(String username) {
		return usuarioDao.findByUsername(username);
	}
	

	@Transactional
	@Override
	public Usuario editarUsuario(UsuarioDTO cuentaDTO) {
		
		Usuario usuario = this.findOne(cuentaDTO.getUsuId());
		Pasajero pasajero = usuario.getPasajero();
		
		usuario.setUsername(cuentaDTO.getUsername());
		if(!(cuentaDTO.getPassword().isEmpty())) {
			usuario.setPassword(passwordEncoder.encode(cuentaDTO.getPassword()));
		}
		pasajero.setPasNombre(cuentaDTO.getPasajero().getPasNombre()); 
		pasajero.setPasApellido(cuentaDTO.getPasajero().getPasApellido());
		pasajero.setPasTelefono(cuentaDTO.getPasajero().getPasTelefono());

		//pasajero.setUsuario(usuario);
		
		//pasajero.getUsuario().setUsername(usuario.getUsername());
		//usuario.setPasajero(pasajero);

		return usuarioDao.save(usuario);	
	}

	public boolean validarMail(String username) {   // DEVUELVE TRUE SI MAIL ES VALIDO PARA GRABAR.   FALSE SI MAIL YA ESTA EN USO
		if(usuarioExiste(username)) {
			return false;  //si usuario existe => mail no es valido
		} else {
			return true;   // si usuario NO existe => mail valido
		}
	}

	@Override
	@Transactional
	public Usuario findByUsuResTok(String usurestok) {
		return usuarioDao.findByUsuResTok(usurestok);
	}
}
