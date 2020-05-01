package com.remiNorte.app.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.remiNorte.app.models.dao.IRemisDao;
import com.remiNorte.app.models.entity.Remis;

public class RemisCodigoValidator implements ConstraintValidator<RemisCodigo, Remis> {
	
	@Autowired 
	private IRemisDao remisDao;	
	
	@Override
	public boolean isValid(Remis remis, ConstraintValidatorContext context) {
		
		Remis remisVal = new Remis();
		remisVal = null;
		Long codigo = remis.getRemId();
		if (codigo != null) {
			remisVal = remisDao.findById(codigo).orElse(null);
		}	
		
		if (remisVal == null) {
			return false;
		} else {
			return true;
		}
	}
}
