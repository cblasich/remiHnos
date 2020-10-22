package com.remiNorte.app.controllers;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.remiNorte.app.models.dao.IPasajeroDao;
import com.remiNorte.app.models.entity.Pasajero;
import com.remiNorte.app.models.entity.Usuario;
import com.remiNorte.app.models.entity.UsuarioDTO;
import com.remiNorte.app.models.service.IUsuarioService;
import com.remiNorte.app.util.paginator.PageRender;
import com.remiNorte.app.validations.UsuarioExisteException;

@Controller
@SessionAttributes("pasajero") 
public class PasajeroController {
	
	@Autowired 
	private IUsuarioService usuarioService;
	
	@Autowired 
	private IPasajeroDao pasajeroDao;
	
	private Logger logger = LoggerFactory.getLogger(OperadorController.class);
	
	@RequestMapping(value="/listarPasajeros", method=RequestMethod.GET)
	public String listarPasajeros(@RequestParam(name="page", defaultValue="0") int page, Model model) {
		Pageable pageRequest = PageRequest.of(page, 20, Sort.by("pasId").descending());
		Page<Pasajero> pasajeros = pasajeroDao.findAll(pageRequest);
		PageRender<Pasajero> pageRender = new PageRender<Pasajero>("/listarPasajeros", pasajeros);
		
		model.addAttribute("backPage", "/iniOperador");
		model.addAttribute("titulo", "Listado de Pasajeros");
		model.addAttribute("pasajeros" , pasajeros);
		model.addAttribute("page", pageRender);
		return "listarPasajeros";		
	}
	
	@RequestMapping(value="/formPasajero")
	public String crear(Model model) {   
		logger.info("GET pasajero");
		//Pasajero pasajero = new Pasajero(); 
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		
		model.addAttribute("titulo", "Datos personales");
		model.addAttribute("pasajero", usuarioDTO);
		model.addAttribute("backPage","/inicio");
		
		return "formPasajero";
	}
	
	@RequestMapping(value="/formPasajero", method=RequestMethod.POST)
	public ModelAndView registrarPasajero(
			@ModelAttribute("pasajero") @Valid UsuarioDTO cuentaDTO,
			WebRequest request,
			BindingResult result,
			Model model,
			RedirectAttributes flash,
			Errors errors) {
		
		Usuario registrado = new Usuario();
		
		//validacion de email
		
		registrado = crearCuentaPasajero(cuentaDTO, result);
		
		if(registrado == null) {
			model.addAttribute("danger", "Email ya registrado. Por favor, intente con otro nuevamente.");
			result.rejectValue("username", "message.regError","Ya existe un usuario registrado con ese Email. Por favor, intente con otro nuevamente.");
			return new ModelAndView("formUsuario");	
		} else {
			model.addAttribute("success","Usuario registrado con éxito.");
			return new ModelAndView("inicio");	
		}
		
	}
	
	private Usuario crearCuentaPasajero(UsuarioDTO cuentaDTO, BindingResult result) {
		
		Usuario registrado = null;
		try {
		registrado = usuarioService.registrarNuevoUsuario(cuentaDTO);
		} catch (UsuarioExisteException e) {
			return null;
		}
		return registrado;
	}
	
	@RequestMapping(value="/eliminarPasajero/{id}")
	public String eliminar(@PathVariable(value="id") Long id) {
		if (id > 0) {
			pasajeroDao.deleteById(id);
		}		
		return "redirect:/listarPasajeros";
	}
	
}
