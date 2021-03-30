package com.remiNorte.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import com.remiNorte.app.models.entity.Empresa;
import com.remiNorte.app.models.service.IEmpresaService;


@Controller
// @SessionAttributes("tarifa")
public class EmpresaController {
	
	@Autowired
	private IEmpresaService empresaService;
	
	@RequestMapping(value="/formEmpresa/{id}")
	public String editar(@PathVariable(value="id") Long id,Map<String, Object> model) {
		//Empresa empresa = null;
		//if (id > 0) {
		Empresa	empresa = empresaService.findOne(1);   //findOne(id);
		//} else {
		//	return "redirect:/listarTarifas";
		//}
		model.put("empresa", empresa);
		model.put("titulo", "Horarios de atención");
		return "formEmpresa";
	}
	
	@SuppressWarnings("unused")
	@PostMapping(value="/formEmpresa") //fase submit del formulario
	public String guardar(@Valid Empresa empresa, BindingResult result, Model model, SessionStatus status) {
		
		//validaciones
		//int error = 0;		
		String pattern = null;
		
		Short hrsDesde = empresa.getEmpHrsDesde();
		Short hrsHasta = empresa.getEmpHrsHasta();
		Short minDesde = empresa.getEmpMinDesde();
		Short minHasta = empresa.getEmpMinHasta();
		
		if (hrsDesde == null) {
			result.rejectValue("empHrsDesde", "empHrsDesde", "Falta completar.");
		} else {
			if (minDesde == null) {
				result.rejectValue("empMinDesde", "empMinDesde", "Falta completar.");
			} else {
				if (hrsHasta == null) {
					result.rejectValue("empHrsHasta", "empHrsHasta", "Falta completar.");
				} else {
					if (minHasta == null) {
						result.rejectValue("empMinHasta", "empMinHasta", "Falta completar.");
					} else {
						if (hrsDesde >= hrsHasta && hrsDesde>=0 && hrsDesde<=23 && hrsHasta>=0 && hrsHasta<=23) {
							result.rejectValue("empHrsDesde", "empHrsDesde", "La hora desde debe ser inferior a la hora hasta.");
						} 
					}
				}
			}
		}
		
		if(result.hasErrors()) {
			model.addAttribute("empresa", empresa);			
			model.addAttribute("titulo", "Horario de atención");
			model.addAttribute("backPage","/iniOperador");
			//return "formEmpresa";
		}
		
		empresaService.save(empresa);
		status.setComplete(); 
		
		return "redirect:iniOperador";
	}
	
}
