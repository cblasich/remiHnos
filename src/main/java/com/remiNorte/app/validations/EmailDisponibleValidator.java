package com.remiNorte.app.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.remiNorte.app.controllers.InicioController;
import com.remiNorte.app.models.entity.Usuario;
import com.remiNorte.app.models.service.IUsuarioService;

public class EmailDisponibleValidator implements ConstraintValidator<EmailDisponible, Usuario >{
	
	@Autowired
	IUsuarioService usuarioService;
	
	private Logger logger = LoggerFactory.getLogger(InicioController.class);
	
	@Override
	public boolean isValid(Usuario usuario, ConstraintValidatorContext context) {
		
		String mail = usuario.getUsername();
		Usuario usuExiste = null;
		
		if (mail != null && mail != "") {
			logger.info("mail: ".concat(mail));			
			usuExiste = usuarioService.findByUsername(mail);
		}		
		
		if (usuExiste == null) {
			return true;
		} else {
			return false;
		}
	}

}
