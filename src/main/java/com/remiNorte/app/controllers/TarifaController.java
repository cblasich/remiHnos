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

import com.remiNorte.app.models.entity.Tarifa;
import com.remiNorte.app.models.service.ITarifaService;

@Controller
@SessionAttributes("tarifa")
public class TarifaController {
	
	@Autowired
	private ITarifaService tarifaService;
	
	@RequestMapping(value="/listarTarifas", method=RequestMethod.GET)
	public String listarTarifas(Model model) {
		
		model.addAttribute("backPage","/iniOperador");
		model.addAttribute("titulo", "Listado de Tarifas");
		model.addAttribute("tarifas" , tarifaService.findAll());
		return "listarTarifas";
	}
	
	@RequestMapping(value="/formTarifa") //fase inicial formulario
	public String crear(Model model) {
		Tarifa tarifa = new Tarifa();
		model.addAttribute("titulo", "Nueva Tarifa");
		model.addAttribute("tarifa", tarifa);
		return "formTarifa";
	}
	
	@RequestMapping(value="/formTarifa/{id}")
	public String editar(@PathVariable(value="id") Long id,Map<String, Object> model) {
		Tarifa tarifa = null;
		if (id > 0) {
			tarifa = tarifaService.findOne(id);
		} else {
			return "redirect:/listarTarifas";
		}
		model.put("tarifa", tarifa);
		model.put("titulo", "Modificar Tarifa");
		return "formTarifa";
	}
	
	@RequestMapping(value="/formTarifa", method=RequestMethod.POST) //fase submit del formulario
	public String guardar(@Valid Tarifa tarifa, BindingResult result, Model model, SessionStatus status) {
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Nueva Tarifa");
			return "formTarifa";
		}
		
		tarifaService.save(tarifa);
		status.setComplete(); //elimina el objeto cliente de la sesión.
		
		return "redirect:listarTarifas";
	}
	
	@RequestMapping(value="/eliminarTarifa/{id}")
	public String eliminar(@PathVariable(value="id") Long id) {
		if (id > 0) {
			tarifaService.delete(id);
		}
		return "redirect:/listarTarifas";
	}
}
