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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.remiNorte.app.models.entity.Remis;
import com.remiNorte.app.models.service.IRemisService;
import com.remiNorte.app.models.service.IViajeService;

@Controller
@SessionAttributes("remis")
public class RemisController {

	@Autowired 
	private IRemisService remisService;
	
	@Autowired
	private IViajeService viajeService;
	
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
		
		remis.setRemEstado("S"); //estado disponible
		
		model.put("backPage", "/listarRemises");
		model.put("titulo", "Nuevo Remis");
		model.put("remis", remis);
		return "formRemis";
	}
		
	@RequestMapping(value="/formRemis/{id}", method=RequestMethod.GET)
	public String editar(@PathVariable(value="id") Long id,Map<String, Object> model) {
		Remis remis = null;
		if (id > 0) {
			remis = remisService.findOne(id);
		} else {
			return "redirect:/listarRemises";
		}
		model.put("remis", remis);
		model.put("backPage", "/listarRemises");
		model.put("titulo", "Modificar Remís");
		return "formRemis";
	}
	
	@RequestMapping(value="/formRemis", method=RequestMethod.POST)
	public String guardar(@Valid Remis remis, 
							BindingResult result, 
							Model model, 
							SessionStatus status) { //metodo que procesa el formulario
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Nuevo Registro");
			return "formRemis";
		}
		
		Remis remisAux = null;
		remisAux = remisService.findByRemPatente(remis.getRemPatente().trim());
		
		if (remisAux == null || remisAux.getRemId() == remis.getRemId()) {
			remisService.save(remis);
			status.setComplete(); //elimina el objeto cliente de la sesión.			
		} else {
			result.rejectValue("remPatente","remPatente","Patente ya registrada en otro remís.");
		}
		
		if (result.hasErrors()) {
			model.addAttribute("remis", remis);
			model.addAttribute("titulo", "Modificar Remís");
			model.addAttribute("backPage", "/listarRemises");
			return "formRemis";
		}
		
		return "redirect:listarRemises";
	}
	

	@RequestMapping(value="/eliminarRemis/{id}")
	public String eliminar(@PathVariable(value="id") Long id, 
							RedirectAttributes flash) {
		
		if (id > 0) {
			Long canRemViajes = (long) 0;
			canRemViajes = viajeService.canRemVia(id);
			
			if (canRemViajes == 0) {
				remisService.delete(id);
			} else {
				flash.addFlashAttribute("error", "El remís no se puede eliminar porque tiene viajes asociados.");
			}
			
		}
		return "redirect:/listarRemises";
	}
	
	@RequestMapping(value = "/ajax/remiseros/{nombre}")
	public String devRemiserosPorNombre(Model model, @PathVariable("nombre") String nombre) {
		
		model.addAttribute("remises", remisService.devRemisesPorNombre(nombre));
		
		return "resultados/resultadosRemis :: resRemiserosDisponibles";
	}
	
	@RequestMapping(value = "/ajax/remiseros")
	public String devRemiserosDisponibles(Model model) {
		
		model.addAttribute("remises", remisService.devRemiserosDisponibles());
		
		return "resultados/resultadosRemis :: resRemiserosDisponibles";
	}
}
