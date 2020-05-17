package com.remiNorte.app.controllers;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller 
public class InicioController {
	
	private Logger logger = LoggerFactory.getLogger(InicioController.class);
	
	@RequestMapping(value= {"/inicio","/"}, method=RequestMethod.GET)
    public String welcome(Model model) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		@SuppressWarnings("unchecked")
		Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) auth.getAuthorities();
		
		for (SimpleGrantedAuthority rol : authorities) {
			logger.info("Role actual: ".concat(rol.toString()));
			if (rol.toString().equalsIgnoreCase("ROLE_OPERAD")) {
				return "redirect:iniOperador";
			}
		}
		
        return "inicio";
    }
	
}
