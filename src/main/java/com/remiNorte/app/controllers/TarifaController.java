package com.remiNorte.app.controllers;

import java.util.Date;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.remiNorte.app.models.entity.Tarifa;
import com.remiNorte.app.models.service.ITarifaService;
import com.remiNorte.app.models.service.IViajeService;

@Controller
@SessionAttributes("tarifa")
public class TarifaController {
	
	@Autowired
	private ITarifaService tarifaService;
	
	@Autowired
	private IViajeService viajeService;
	
	@RequestMapping(value="/listarTarifas", method=RequestMethod.GET)
	public String listarTarifas(Model model) {
		
		model.addAttribute("backPage","/iniOperador");
		model.addAttribute("titulo", "Listado de Tarifas");
		model.addAttribute("tarifas" , tarifaService.findAll());
		return "listarTarifas";
	}
	
	@GetMapping(value="/formTarifa") //fase inicial formulario
	public String crear(Model model) {
		Tarifa tarifa = new Tarifa();
		
		Date fechaHoy = new Date();
		tarifa.setTarFecVigDes(fechaHoy);
		
		model.addAttribute("backPage","/listarTarifas");
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
	
	@PostMapping(value="/formTarifa") //fase submit del formulario
	public String guardar(@Valid Tarifa tarifa, BindingResult result, Model model, SessionStatus status) {
		
		if(result.hasErrors()) {
			model.addAttribute("tarifa", tarifa);			
			model.addAttribute("titulo", "Nueva Tarifa");
			model.addAttribute("backPage","/listarTarifas");
			return "formTarifa";
		}
		
		//busca la ultima tarifa vigente y graba vigencia hasta con fecha de hoy
		Tarifa tarifaVig = null;
		tarifaVig = tarifaService.findByTarVigHas(null);
		if (tarifaVig != null) {
			Date fechaHoy = new Date();
			tarifaVig.setTarFecVigHas(fechaHoy);
			tarifaService.save(tarifaVig);
		}		
		
		tarifaService.save(tarifa);
		status.setComplete(); //elimina el objeto cliente de la sesión.
		
		return "redirect:listarTarifas";
	}
	
	@RequestMapping(value="/eliminarTarifa/{id}")
	public String eliminar(@PathVariable(value="id") Long id) {
		if (id > 0) {
			
			Long cantTarViajes = (long) 0;
			cantTarViajes = viajeService.devCanTarViajes(id);
			
			if (cantTarViajes == 0) {
				//busco tarifa anterior para dejar como ultima vigente, luego elimino
				tarifaService.delete(id);
			} else {
				//muestro mensaje "no se puede eliminar"
			}
				
		}
		return "redirect:/listarTarifas";
	}
}
