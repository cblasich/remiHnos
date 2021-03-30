package com.remiNorte.app.controllers;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.remiNorte.app.models.entity.Empresa;
import com.remiNorte.app.models.service.IEmpresaService;

@Controller 
public class InicioController {
	
	private Logger logger = LoggerFactory.getLogger(InicioController.class);
	
	@Autowired
	private IEmpresaService empresaService;
	
	@RequestMapping(value= {"/inicio","/"}, method=RequestMethod.GET)
    public String welcome(Model model) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		@SuppressWarnings("unchecked")
		Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) auth.getAuthorities();
		
		for (SimpleGrantedAuthority rol : authorities) {
			logger.info("Role actual: ".concat(rol.toString()));
			if (rol.toString().equalsIgnoreCase("ROLE_ADMIN")) {
				return "redirect:iniOperador";
			}
			
			if (rol.toString().equalsIgnoreCase("ROLE_OPERAD")) {
				return "redirect:listarViajes";
			}
		}
		
		Empresa	empresa = empresaService.findOne(1); 
		String hrsDesde = empresa.getEmpHrsDesde().toString();
		String hrsHasta = empresa.getEmpHrsHasta().toString();
		String minDesde = empresa.getEmpMinDesde().toString();
		String minHasta = empresa.getEmpMinHasta().toString();
		
		if(hrsDesde.length() == 1) {
			hrsDesde = "0" + hrsDesde;
		}
		if(hrsHasta.length() == 1) {
			hrsHasta = "0" + hrsHasta;
		}
		if(minDesde.length() == 1) {
			minDesde = "0" + minDesde;
		}
		if(minHasta.length() == 1) {
			minHasta = "0" + minHasta;
		}
		
		
		String horaDesde = hrsDesde + ":" + minDesde;
		String horaHasta = hrsHasta + ":" + minHasta;
		
		model.addAttribute("horaDesde", horaDesde);
		model.addAttribute("horaHasta", horaHasta);
		
        return "inicio";
    }
	
}
