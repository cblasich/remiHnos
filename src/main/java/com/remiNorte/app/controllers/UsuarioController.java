package com.remiNorte.app.controllers;

import java.util.Map;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import com.remiNorte.app.validations.UsuarioExisteException;
import com.remiNorte.app.models.entity.UsuarioDTO;
import com.remiNorte.app.models.dao.IPasajeroDao;
import com.remiNorte.app.models.dao.IUsuarioDao;
import com.remiNorte.app.models.entity.Pasajero;
import com.remiNorte.app.models.entity.Usuario;
import com.remiNorte.app.models.service.IUsuarioService;

@Controller
@SessionAttributes("usuario")
public class UsuarioController {

	//protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired 
	private IUsuarioService usuarioService;
	
	@Autowired
	private IUsuarioDao usuarioDao;
	
	@Autowired
	private IPasajeroDao pasajeroDao;
	
	private Logger logger = LoggerFactory.getLogger(UsuarioController.class);
	
	@RequestMapping(value="/listarUsuarios", method=RequestMethod.GET)
	public String listarUsuarios(Model model) {
		
		model.addAttribute("backPage","/iniOperador");
		model.addAttribute("titulo", "Listado de Usuarios");
		model.addAttribute("usuarios" , usuarioService.findAll());
		return "listarUsuarios";		
	}
	
	@RequestMapping(value="/formUsuario")
	public String crear(Map<String, Object> model) {   
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		
		if (auth.getName() != "anonymousUser") {   // si usuario está logueado
			model.put("titulo", "Editar datos");
			model.put("lblContra", "Nueva contraseña");
			model.put("backPage", "/inicio");
			//logger.info("Hola usuario autenticado: "+auth.getName());			
			Usuario usuario = usuarioDao.findByUsername(auth.getName());
			Pasajero pasajero = pasajeroDao.findByUsuario(usuario);
			//logger.info("Pasajero:"+pasajero.getPasApellido()+" "+pasajero.getPasNombre());
			//logger.info("Id usuario:"+usuario.getUsuId().toString());
			usuarioDTO.setUsuId(usuario.getUsuId());   // si está logueado llevo id usuario
			usuarioDTO.setUsername(usuario.getUsername());
			usuarioDTO.setPasajero(pasajero);   
		} else {
			usuarioDTO.setUsuId(null);   // si no está logueado id usuario es nulo
			model.put("titulo", "Nuevo Usuario");
			model.put("lblContra", "Contraseña");
			model.put("backPage", "/inicio");
			//logger.info("ID NULO");
		}
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
			WebRequest request,
			BindingResult result,
			Model model,
			RedirectAttributes flash,
			Errors errors,
			@RequestParam(defaultValue="") String passwordConf ) {
		
		Usuario registrado = new Usuario();
		Usuario logueado = new Usuario();
		//Usuario logueado = usuarioService.findOne(cuentaDTO.getUsuId());  //en logueado tendria el "usuario original" (el que se quiere modificar)
		model.addAttribute("titulo", "Nuevo Usuario");
		
		//validaciones
		int error = 0;		
		String pattern = null;
		
		String mail = cuentaDTO.getUsername();
		if (mail.equals(null) || mail.equals("")) {
			error = 1;
			result.rejectValue("username", "username", "Debe ingresar un E-Mail.");
		} else {
			pattern = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";
			if (!mail.matches(pattern) ) {
				error = 1;
				result.rejectValue("username", "username", "El E-Mail ingresado no es válido.");
			}
		}
		
		 
		String password = cuentaDTO.getPassword();
		if (password.equals(null) || password.equals("")) {
			if(cuentaDTO.getUsuId() == null) {  //si es usu nuevo
				error = 1;
				model.addAttribute("lblContra", "Contraseña");
				result.rejectValue("password", "password", "Debe ingresar una contraseña.");
			} 
			if (!password.equals(passwordConf)) {
				error = 1;
				model.addAttribute("lblContra", "Nueva contraseña");
				result.reject("usuario.password","Las contraseñas no coinciden.");
			}
		} else {
			if (!password.equals(passwordConf)) {
				error = 1;
				if(cuentaDTO.getUsuId() == null) {  //si es usu nuevo
					model.addAttribute("lblContra", "Contraseña");
				} else {
					model.addAttribute("lblContra", "Nueva contraseña");
				}
				result.reject("usuario.password","Las contraseñas no coinciden.");
			}
		}	
		
		
		
		Pasajero pasajero = new Pasajero();
		pasajero = cuentaDTO.getPasajero();
		
		if (pasajero.getPasNombre().equals(null) || pasajero.getPasNombre().equals("")) {
			error = 1;
			result.rejectValue("pasajero.PasNombre", "pasajero.PasNombre", "Debe ingresar un nombre.");
		} else {
			String nombre = pasajero.getPasNombre().trim();
			pattern = "^[a-zA-Z]+";
			if (!nombre.matches(pattern)) {
				error = 1;
				result.rejectValue("pasajero.PasNombre", "pasajero.PasNombre", "Debe ingresar solo letras.");
			}
		}
		
		if (pasajero.getPasApellido().equals(null) || pasajero.getPasApellido().equals("")) {
			error = 1;
			result.rejectValue("pasajero.PasApellido", "pasajero.PasApellido", "Debe ingresar un apellido.");
		} else {
			String apellido = pasajero.getPasApellido().trim();
			pattern = "^[a-zA-Z]+";
			if (!apellido.matches(pattern)) {
				error = 1;
				result.rejectValue("pasajero.PasApellido", "pasajero.PasApellido", "Debe ingresar solo letras.");
			}
		}
		
		String telefono = pasajero.getPasTelefono();
		if (telefono.equals(null) || telefono.equals("") || telefono == "0") {
			error = 1;
			result.rejectValue("pasajero.PasTelefono", "pasajero.PasTelefono", "Debe ingresar un teléfono.");
		} else {
			pattern = "^[0-9]+";
			if (!telefono.matches(pattern)) {
				error = 1;
				result.rejectValue("pasajero.PasTelefono", "pasajero.PasTelefono", "Debe ingresar solo números.");
			}
		}
		
		if (error == 0) {
			if(cuentaDTO.getUsuId() == null) {     // CREANDO USUARIO NUEVO
				//validacion de email 
				registrado = crearCuentaUsuario(cuentaDTO, result);	
				if(registrado == null) {
					result.rejectValue("username", "message.regError","Ya existe un usuario registrado con ese Email. Por favor, intente con otro nuevamente.");
					model.addAttribute("lblContra", "Contraseña");
					return new ModelAndView("formUsuario");	
				} else {
					model.addAttribute("success","Usuario registrado con éxito.");
					model.addAttribute("titulo", "Inicio");
					return new ModelAndView("inicio");
				}
			
			} else {   // EDITANDO DATOS DE USUARIO LOGUEADO
				
				boolean mailValido;
				logueado = usuarioService.findOne(cuentaDTO.getUsuId());  //en logueado tendria el "usuario original" (el que se quiere modificar)
				
				if(logueado.getUsername().trim().compareTo(cuentaDTO.getUsername().trim()) != 0) {  // si ingreso un mail diferente, validar que nuevo mail no exista
					logger.info("SE MODIFICO MAIL");
					logger.info("Logueado:"+logueado.getUsername());
					logger.info("Nuevo:"+cuentaDTO.getUsername());
					mailValido = validarMail(cuentaDTO, result);
					if(mailValido) {
						editarCuentaUsuario(cuentaDTO);	//cuentaDTO tiene datos nuevos. e ID del usuario a modificar	
						model.addAttribute("success","Datos modificados con éxito.");
						model.addAttribute("titulo", "Inicio");
						return new ModelAndView("inicio");
					} else {
						result.rejectValue("username", "message.regError","Ya existe un usuario registrado con ese Email. Por favor, intente con otro nuevamente.");
						return new ModelAndView("formUsuario");	
					}
				} else {  //si el mail no se modificó, guardar cambios. contraseña solo guardar si NO es vacío
					logger.info("MAIL ES EL MISMO");
					editarCuentaUsuario(cuentaDTO);	//cuentaDTO tiene datos nuevos. e ID del usuario a modificar
					model.addAttribute("success","Datos modificados con éxito.");
					model.addAttribute("titulo", "Inicio");
					return new ModelAndView("inicio");
				}
			}
		}
		
		if (result.hasErrors()) {
			model.addAttribute("backPage", "/inicio");
			return new ModelAndView("formUsuario");
		}
		model.addAttribute("backPage", "/inicio");
		model.addAttribute("titulo", "Inicio");
		return new ModelAndView("inicio");
	}
	 
	private void editarCuentaUsuario(UsuarioDTO cuentaDTO) {
		usuarioService.editarUsuario(cuentaDTO);
	}
	
	private boolean validarMail(UsuarioDTO cuentaDTO, BindingResult result) {
		 return usuarioService.validarMail(cuentaDTO.getUsername());
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
	
	@RequestMapping(value="/eliminarUsuario/{id}")
	public String eliminar(@PathVariable(value="id") Long id) {
		if (id > 0) {
			usuarioService.delete(id);
		}		
		return "redirect:/listarUsuarios";
	}

	
	/*
	 * public String validatePasswordResetToken(String token) { final
	 * PasswordResetToken passToken = passwordTokenRepository.findByToken(token);
	 * 
	 * return !isTokenFound(passToken) ? "invalidToken" : isTokenExpired(passToken)
	 * ? "expired" : null; }
	 * 
	 * private boolean isTokenFound(PasswordResetToken passToken) { return passToken
	 * != null; }
	 * 
	 * private boolean isTokenExpired(PasswordResetToken passToken) { final Calendar
	 * cal = Calendar.getInstance(); return
	 * passToken.getExpiryDate().before(cal.getTime()); }
	 */
}
