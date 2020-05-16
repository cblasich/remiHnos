package com.remiNorte.app.controllers;

import java.util.Date;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.remiNorte.app.models.dao.IPasajeroDao;
import com.remiNorte.app.models.dao.IRemisDao;
import com.remiNorte.app.models.dao.ITarifaDao;
import com.remiNorte.app.models.dao.IUsuarioDao;
import com.remiNorte.app.models.dao.IViajeDao;
import com.remiNorte.app.models.entity.Pasajero;
import com.remiNorte.app.models.entity.Tarifa;
import com.remiNorte.app.models.entity.Usuario;
import com.remiNorte.app.models.entity.Viaje;

@Controller 
@SessionAttributes("viaje")
public class ViajeController {

	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private IUsuarioDao usuarioDao;
	
	@Autowired 
	private IViajeDao viajeDao;
	
	@Autowired
	private IPasajeroDao pasajeroDao;
	
	@Autowired
	private ITarifaDao tarifaDao;
	
	@Autowired 
	private IRemisDao remisDao;
	
	@GetMapping(value =  "/formViaje")
	public String crear(Model model) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Viaje viaje = new Viaje();
		
		if (auth.getName() != "anonymousUser") {
			logger.info("Hola usuario autenticado: "+auth.getName());			
			Usuario usuario = usuarioDao.findByUsername(auth.getName());
			Pasajero pasajero = pasajeroDao.findByUsuario(usuario);
			logger.info("Pasajero:"+pasajero.getPasApellido()+" "+pasajero.getPasNombre());
			viaje.setViaTelefono(pasajero.getPasTelefono());
			viaje.setPasajero(pasajero);
		} else {
			logger.info("Hola usuario anonimo");
		}
			
	/////tarifa
		Date fechaNula = null;
		Tarifa tarifa = tarifaDao.findByTarFecVigHas(fechaNula);
		viaje.setTarifa(tarifa);	
		tarifa.addViaje(viaje);   
		
	////forma de pago	
		viaje.setViaForPag("E");
		
		model.addAttribute("titulo", "Pedido Remis");
		model.addAttribute("viaje", viaje);
		return "formViaje";
	}
	
	@PostMapping(value = "/formViaje")
	public String guardar(@ModelAttribute Viaje viaje, Model model, RedirectAttributes flash) {
		
		flash.addFlashAttribute("success","Solicitud enviada. En breve, recibirá confirmación vía mensaje de texto al móvil. Gracias!");
		viajeDao.save(viaje);
		return "redirect:inicio";
	}
	
	@GetMapping(value = "/formViajeRem/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model) {
		
		Viaje viaje = new Viaje();
		if (id > 0) {
			viaje = viajeDao.findById(id).get();
		} else {
			return "redirect:/listarViajes";
		}
		logger.info("Viaje: ".concat(viaje.getViaId().toString()));
		
		model.put("viaje", viaje);
		model.put("titulo", "Editar Viaje");
		model.put("backPage", "/listarViajes");
		model.put("remises" , remisDao.devRemisesDisponibles());
		return "formViajeRem";
	}
	
	@PostMapping(value = "/formViajeRem")
	public String guardarEditar(@ModelAttribute("viaje") @Valid Viaje viaje, BindingResult result, Model model, RedirectAttributes flash) {
		
		if (result.hasErrors()) {
			model.addAttribute("viaje", viaje);
			model.addAttribute("titulo", "Editar Viaje");
			model.addAttribute("backPage", "/listarViajes");
			model.addAttribute("remises" , remisDao.findAll());
			return "formViajeRem";
		}
		
		viajeDao.save(viaje);
		return "redirect:/listarViajes";
	}
	
	@RequestMapping(value="/eliminarViaje/{id}")
	public String eliminar(@PathVariable(value="id") Long id) {
		if (id > 0) {
			viajeDao.deleteById(id);
		}		
		return "redirect:/listarViajes";
	}
	
	@RequestMapping(value="/listarViajes", method=RequestMethod.GET)
	public String listarViajes(Model model) {
		model.addAttribute("titulo", "Listado de Viajes");
		model.addAttribute("viajes" , viajeDao.findAll());
		return "listarViajes";		
	}  
	
	@RequestMapping(value="/infoViaSol")
	public String infoViaSol(Model model) {
		model.addAttribute("titulo", "Solicitud exitosa");
		return "infoViaSol";		
	}  
	
}
