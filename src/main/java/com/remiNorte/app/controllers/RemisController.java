package com.remiNorte.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.remiNorte.app.models.entity.Remis;
import com.remiNorte.app.models.service.IRemisService;

@Controller
@SessionAttributes("remis")
public class RemisController {

	@Autowired 
	private IRemisService remisService;
	
	@RequestMapping(value="/listarRemises", method=RequestMethod.GET)
	public String listarRemises(Model model) {
		
		model.addAttribute("backPage","/iniOperador");
		model.addAttribute("titulo", "Listado de Remises");
		model.addAttribute("remises" , remisService.findAll());
		return "listarRemises";		
	}
	
	@RequestMapping(value="/formRemis")
	public String crear(Map<String, Object> model) {   
		Remis remis = new Remis(); 
		model.put("titulo", "Nuevo Remis");
		model.put("remis", remis);
		return "formRemis";
	}
		
	@RequestMapping(value="/formRemis/{id}")
	public String editar(@PathVariable(value="id") Long id,Map<String, Object> model) {
		Remis remis = null;
		if (id > 0) {
			remis = remisService.findOne(id);
		} else {
			return "redirect:/listarRemises";
		}
		model.put("remis", remis);
		model.put("titulo", "Modificar Remis");
		return "formRemis";
	}
	
	@RequestMapping(value="/formRemis", method=RequestMethod.POST)
	public String guardar(@Valid Remis remis, BindingResult result, Model model, SessionStatus status) { //metodo que procesa el formulario
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Nuevo Registro");
			return "formRemis";
		}
		
		remisService.save(remis);
		status.setComplete(); //elimina el objeto cliente de la sesión.
		
		//model.addAttribute("titulo", "Informacion del Remis");
		//model.addAttribute("remis", remis);
		
		return "redirect:listarRemises";
	}
	

	@RequestMapping(value="/eliminarRemis/{id}")
	public String eliminar(@PathVariable(value="id") Long id) {
		if (id > 0) {
			remisService.delete(id);
		}		
		return "redirect:/listarRemises";
	}
	
}
