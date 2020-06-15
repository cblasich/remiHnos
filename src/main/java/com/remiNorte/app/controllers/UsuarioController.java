package com.remiNorte.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.remiNorte.app.validations.UsuarioExisteException;
import com.remiNorte.app.models.entity.UsuarioDTO;
import com.remiNorte.app.models.entity.Pasajero;
import com.remiNorte.app.models.entity.Usuario;
import com.remiNorte.app.models.service.IUsuarioService;

@Controller
@SessionAttributes("usuario")
public class UsuarioController {

	@Autowired 
	private IUsuarioService usuarioService;
	
	@RequestMapping(value="/listarUsuarios", method=RequestMethod.GET)
	public String listarUsuarios(Model model) {
		
		model.addAttribute("backPage","/iniOperador");
		model.addAttribute("titulo", "Listado de Usuarios");
		model.addAttribute("usuarios" , usuarioService.findAll());
		return "listarUsuarios";		
	}
	
	//comento para pasar metodos dulce carolina
	@RequestMapping(value="/formUsuario")
	public String crear(Map<String, Object> model) {   
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		model.put("titulo", "Nuevo Usuario");
		model.put("usuario", usuarioDTO);
		
		return "formUsuario";
	} 
	
	/*@RequestMapping(value="/formUsuario", method=RequestMethod.GET)
	public String showRegistrationForm(WebRequest request, Model model) {
		
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		model.addAttribute("usuario", usuarioDTO);
		
		return "formUsuario";
	}*/
	
	@RequestMapping(value="/formUsuario", method=RequestMethod.POST)
	public ModelAndView registrarUsuario(
			@ModelAttribute("usuario") @Valid UsuarioDTO cuentaDTO,
			BindingResult result,
			Model model,
			@RequestParam(defaultValue="") String passwordConf ) {
		
		Usuario registrado = new Usuario();
		model.addAttribute("titulo", "Nuevo Usuario");
		//validacion de email
		
		if (cuentaDTO.getUsername().equals(null) || cuentaDTO.getUsername().equals("")) {
			result.rejectValue("username", "username", "Debe ingresar un E-Mail.");
		}
		
		String password = cuentaDTO.getPassword();
		if (password.equals(null) || password.equals("")) {
			result.rejectValue("password", "password", "Debe ingresar una contraseña.");
		} else {
			if (!password.equals(passwordConf)) {
				result.reject("usuario.password","Las contraseñas no coinciden.");
			}
		}		
		
		int error = 0;
		Pasajero pasajero = new Pasajero();
		pasajero = cuentaDTO.getPasajero();
		
		if (pasajero.getPasNombre().equals(null) || pasajero.getPasNombre().contentEquals("")) {
			error = 1;
			result.rejectValue("pasajero.PasNombre", "pasajero.PasNombre", "Debe ingresar un nombre.");
		}
		
		if (pasajero.getPasApellido().equals(null) || pasajero.getPasApellido().equals("")) {
			error = 1;
			result.rejectValue("pasajero.PasApellido", "pasajero.PasApellido", "Debe ingresar un apellido.");
		}
		
		if (pasajero.getPasTelefono().equals(null) || pasajero.getPasTelefono().equals("") || pasajero.getPasTelefono() == "0") {
			error = 1;
			result.rejectValue("pasajero.PasTelefono", "pasajero.PasTelefono", "Debe ingresar un teléfono.");
		}
		
		if (!cuentaDTO.getUsername().equals(null) && !cuentaDTO.getUsername().equals("") 
				&& !cuentaDTO.getPassword().equals(null) && !cuentaDTO.getPassword().equals("") && error == 0) {
				
				registrado = crearCuentaUsuario(cuentaDTO, result);
				
				if(registrado == null) {
					//model.addAttribute("danger", "Email ya registrado. Por favor, intente con otro nuevamente.");
					result.rejectValue("username", "message.regError","Ya existe un usuario registrado con ese E-Mail. Por favor, intente con otro nuevamente.");
					//return new ModelAndView("formUsuario");	
				} else {
					model.addAttribute("success","Usuario registrado con éxito.");
					//return new ModelAndView("inicio");	
				}
			}
		
		if (result.hasErrors()) {
			return new ModelAndView("formUsuario");
		}
		return new ModelAndView("inicio");
	}
	
	private Usuario crearCuentaUsuario(UsuarioDTO cuentaDTO, BindingResult result) {
		
		Usuario registrado = null;
		try {
		registrado = usuarioService.registrarNuevoUsuario(cuentaDTO);
		} catch (UsuarioExisteException e) {
			return null;
		}
		return registrado;
	}
	
	@RequestMapping(value="/formUsuario/{id}")
	public String editar(@PathVariable(value="id") Long id,Map<String, Object> model) {
		Usuario usuario = null;
		if (id > 0) {
			usuario = usuarioService.findOne(id);
		} else {
			return "redirect:/listarUsuarios";
		}
		model.put("usuario", usuario);
		model.put("titulo", "Modificar Usuario");
		return "formUsuario";
	}
	
	/*@RequestMapping(value="/formUsuario", method=RequestMethod.POST)
	public String guardar(@Valid Usuario usuario, BindingResult result, Model model, SessionStatus status) { //metodo que procesa el formulario
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Nuevo Registro");
			return "formUsuario";
		}
		
		usuarioService.save(usuario);
		status.setComplete(); //elimina el objeto cliente de la sesión.
		
		//model.addAttribute("titulo", "Informacion del Usuario");
		//model.addAttribute("usuario", usuario);
		
		return "redirect:listarUsuarios";
	}*/
	
	@RequestMapping(value="/eliminarUsuario/{id}")
	public String eliminar(@PathVariable(value="id") Long id) {
		if (id > 0) {
			usuarioService.delete(id);
		}		
		return "redirect:/listarUsuarios";
	}
	
}
