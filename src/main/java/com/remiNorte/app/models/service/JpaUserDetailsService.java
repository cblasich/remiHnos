package com.remiNorte.app.models.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remiNorte.app.models.dao.IUsuarioDao;
import com.remiNorte.app.models.entity.Rol;
import com.remiNorte.app.models.entity.Usuario;

@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService{

	@Autowired
	private IUsuarioDao usuarioDao;
	
	private Logger logger = LoggerFactory.getLogger(JpaUserDetailsService.class);
	
	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//UserDetails es una interfaz de spring security que representa un usuario autenticado
		
		Usuario usuario = usuarioDao.findByUsername(username);
		
		if (usuario==null) {
			logger.error("Error login: no existe el usuario '" + username + "'");
			throw new UsernameNotFoundException("Mail " + username + "no existe en el sistema!");
		}
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		//grantedauthority es un tipo abstracto
		
		Rol rol = new Rol();
		rol = usuario.getRol();
		authorities.add(new SimpleGrantedAuthority(rol.getRolNombre()));
		logger.info("Rol: ".concat(rol.getRolNombre()));
		
		/* comentado porque solo usamos un rol por usuario
		 * for(Rol role : usuario.getRoles()) {
		 * logger.info("Role: ".concat(role.getRolNombre())); 
		 * authorities.add(new SimpleGrantedAuthority(role.getRolNombre())); }
		 */
		
		if (authorities.isEmpty()) {
			logger.error("Error login: usuario '"+username+"'no tiene roles asignados!");
			throw new UsernameNotFoundException("Error login: usuario '"+username+"'no tiene roles asignados!");
		}
		
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getUsuEnabled(), true, true, true, authorities);
	}

}
