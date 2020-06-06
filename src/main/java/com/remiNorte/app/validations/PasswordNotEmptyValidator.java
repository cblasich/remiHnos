package com.remiNorte.app.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.remiNorte.app.models.entity.Usuario;

public class PasswordNotEmptyValidator implements ConstraintValidator<PasswordNotEmpty, Usuario>{
	
	@Override
	public boolean isValid(Usuario usuario, ConstraintValidatorContext context) {
		
		String password = usuario.getPassword();
		
		if (password != null && password != "") {
			return true;
		} else {
			return false;
		}
	}
}
