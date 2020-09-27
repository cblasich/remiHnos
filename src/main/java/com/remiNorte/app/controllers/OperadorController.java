package com.remiNorte.app.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.remiNorte.app.models.entity.Operador;
import com.remiNorte.app.models.entity.Rol;
import com.remiNorte.app.models.entity.Usuario;
import com.remiNorte.app.models.service.IOperadorService;
import com.remiNorte.app.models.service.IViajeService;

@Controller
@SessionAttributes({"operador","usuario"}) 
public class OperadorController {
	
	@Autowired
	private IOperadorService operadorService;
	
	@Autowired
	private IViajeService viajeService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	private Logger logger = LoggerFactory.getLogger(OperadorController.class);
	
	@RequestMapping(value="/listarOperadores", method=RequestMethod.GET)
	public String listarOperadores(Model model) {
		
		model.addAttribute("titulo", "Listado de Operadores");
		model.addAttribute("backPage", "/iniOperador");
		model.addAttribute("operadores" , operadorService.findAll());
		return "listarOperadores";		
	}
	
	@RequestMapping(value="/formOperador", method=RequestMethod.GET) //fase inicial formulario
	public String crear(Model model) {
		
		logger.info("GET operador");
		
		Operador operador = new Operador();
		
		model.addAttribute("backPage","/listarOperadores");
		model.addAttribute("titulo", "Nuevo Operador");
		model.addAttribute("operador", operador);
		
		return "formOperador";
	}
	
	@RequestMapping(value="/formOperador", method=RequestMethod.POST) //fase submit del formulario
	public String guardar(@ModelAttribute @Valid Operador operador, 
							BindingResult result, 
							Model model,
							@RequestParam(defaultValue="") String passwordConf ) {
				
		//Valida password
		Usuario usuario = new Usuario();
		usuario  = operador.getUsuario();
		String password = usuario.getPassword().trim();
		
		if (password == null || password == "") {
			result.rejectValue("usuario.password", "usuario.password", "Debe ingresar una contraseña.");
		} else {
			if (!password.equals(passwordConf)) {
				logger.info("Password 1: "+password+" password 2: "+passwordConf);
				result.reject("usuario.password","Las contraseñas no coinciden.");
			}
		}
		
		if (result.hasErrors()) {
			model.addAttribute("operador", operador);
			model.addAttribute("titulo", "Nuevo Operador");
			model.addAttribute("backPage", "/listarOperadores");
			return "formOperador";
		}
		
		Rol rol = new Rol();
		rol.setRolOperad();
		
		String bcryptPassword = passwordEncoder.encode(usuario.getPassword());
		usuario.setPassword(bcryptPassword);		
		usuario.setRol(rol);
		usuario.setUsuEnabled(true);
		operador.setUsuario(usuario);
			
		operadorService.save(operador);
		return "redirect:listarOperadores";
	}
	
	@RequestMapping(value="/eliminarOperador/{id}")
	public String eliminar(@PathVariable(value="id") Long id,
							RedirectAttributes flash) {
		if (id > 0) {
			Long canOpeViajes = (long) 0;
			canOpeViajes = viajeService.canOpeVia(id);
			
			if (canOpeViajes == 0) {
				operadorService.delete(id);
			} else {
				flash.addFlashAttribute("error", "El operador no se puede eliminar porque tiene viajes asociados.");
			}			
		}		
		return "redirect:/listarOperadores";
	}
}
