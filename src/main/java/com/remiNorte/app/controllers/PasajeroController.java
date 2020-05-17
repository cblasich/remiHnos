package com.remiNorte.app.controllers;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.remiNorte.app.models.dao.IPasajeroDao;
import com.remiNorte.app.models.entity.Pasajero;
import com.remiNorte.app.models.entity.Rol;
import com.remiNorte.app.models.entity.Usuario;
import com.remiNorte.app.models.entity.UsuarioDTO;
import com.remiNorte.app.models.service.IUsuarioService;
import com.remiNorte.app.validations.UsuarioExisteException;


@Controller
@SessionAttributes("pasajero") 
public class PasajeroController {
	
	@Autowired 
	private IUsuarioService usuarioService;
	
	@Autowired 
	private IPasajeroDao pasajeroDao;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	private Logger logger = LoggerFactory.getLogger(OperadorController.class);
	
	@RequestMapping(value="/listarPasajeros", method=RequestMethod.GET)
	public String listarPasajeros(Model model) {
		
		model.addAttribute("titulo", "Listado de Pasajeros");
		model.addAttribute("backPage", "/iniOperador");
		model.addAttribute("pasajeros" , pasajeroDao.findAll());
		return "listarPasajeros";		
	}
	
	// FUNCIONA 26/04   -- SE COMENTA PARA PRUEBAS
	/*@RequestMapping(value="/formPasajero")
	public String crear(Model model) {   
		logger.info("GET pasajero");
		Pasajero pasajero = new Pasajero(); 
		model.addAttribute("titulo", "Nuevo Registro");
		model.addAttribute("pasajero", pasajero);
		model.addAttribute("backPage","/inicio");
		
		return "formPasajero";
	}
	
	@RequestMapping(value="/formPasajero", method=RequestMethod.POST)
	public String guardar(@Valid Pasajero pasajero, BindingResult result, Model model, SessionStatus status) { //metodo que procesa el formulario
			Usuario usuario = new Usuario();
			Rol rol = new Rol();
			rol.setRolOperad();
			
			usuario = pasajero.getUsuario();
			String bcryptPassword = passwordEncoder.encode(usuario.getPassword());
			usuario.setPassword(bcryptPassword);		
			usuario.setRol(rol);
			usuario.setUsuEnabled(true);
			pasajero.setUsuario(usuario);
			
			if(result.hasErrors()) {
				model.addAttribute("titulo", "Nuevo Registro");
				return "formPasajero";
			}
			
			pasajeroDao.save(pasajero);
			status.setComplete(); //elimina el objeto cliente de la sesión.
			
			model.addAttribute("titulo", "Datos personales");
			model.addAttribute("pasajero", pasajero);
			return "redirect:listarPasajeros";
		
	}*/
	
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
