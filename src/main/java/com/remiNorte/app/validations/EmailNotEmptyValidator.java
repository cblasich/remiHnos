package com.remiNorte.app.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.remiNorte.app.controllers.InicioController;
import com.remiNorte.app.models.entity.Usuario;

public class EmailNotEmptyValidator implements ConstraintValidator<EmailNotEmpty, Usuario> {
	
	@Override
	public boolean isValid(Usuario usuario, ConstraintValidatorContext context) {
		
		if (usuario.getUsername() == null || usuario.getUsername() == "") {
			return false;
		} else {
			return true;
		}
		
	}

}
