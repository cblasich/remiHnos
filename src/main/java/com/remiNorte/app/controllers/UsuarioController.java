package com.remiNorte.app.controllers;

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
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired 
	private IUsuarioService usuarioService;
	
	@Autowired
	private IUsuarioDao usuarioDao;
	
	@Autowired
	private IPasajeroDao pasajeroDao;
	
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
			model.put("titulo", "Editar datos personales");
			model.put("lblContra", "Nueva contraseña");
			logger.info("Hola usuario autenticado: "+auth.getName());			
			Usuario usuario = usuarioDao.findByUsername(auth.getName());
			Pasajero pasajero = pasajeroDao.findByUsuario(usuario);
			logger.info("Pasajero:"+pasajero.getPasApellido()+" "+pasajero.getPasNombre());
			logger.info("Id usuario:"+usuario.getUsuId().toString());
			usuarioDTO.setUsuId(usuario.getUsuId());   // si está logueado llevo id usuario
			usuarioDTO.setUsername(usuario.getUsername());
			usuarioDTO.setPasajero(pasajero);   
		} else {
			usuarioDTO.setUsuId(null);   // si no está logueado id usuario es nulo
			model.put("titulo", "Nuevo Usuario");
			model.put("lblContra", "Contraseña");
			logger.info("ID NULO");
		}
		model.put("usuario", usuarioDTO);
		
		return "formUsuario";
	} 
	
	@RequestMapping(value="/formUsuario", method=RequestMethod.POST)
	public ModelAndView registrarUsuario(
			@ModelAttribute("usuario") @Valid UsuarioDTO cuentaDTO,
			WebRequest request,
			BindingResult result,
			Model model,
			RedirectAttributes flash,
			Errors errors) {
		
		Usuario registrado = new Usuario();
		Usuario logueado = new Usuario();
		model.addAttribute("titulo", "Nuevo Usuario");
		
		if(cuentaDTO.getUsuId() == null) {     // CREANDO USUARIO NUEVO
			//validacion de email 
			registrado = crearCuentaUsuario(cuentaDTO, result);	
			if(registrado == null) {
				result.rejectValue("username", "message.regError","Ya existe un usuario registrado con ese Email. Por favor, intente con otro nuevamente.");
				model.addAttribute("lblContra", "Contraseña");
				return new ModelAndView("formUsuario");	
			} else {
				model.addAttribute("success","Usuario registrado con éxito.");
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
					return new ModelAndView("inicio");
				} else {
					result.rejectValue("username", "message.regError","Ya existe un usuario registrado con ese Email. Por favor, intente con otro nuevamente.");
					return new ModelAndView("formUsuario");	
				}
			} else {  //si el mail no se modificó, guardar cambios. contraseña solo guardar si NO es vacío
				logger.info("MAIL ES EL MISMO");
				editarCuentaUsuario(cuentaDTO);	//cuentaDTO tiene datos nuevos. e ID del usuario a modificar
				model.addAttribute("success","Datos modificados con éxito.");
				return new ModelAndView("inicio");
			}
		}
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
	
	private void editarCuentaUsuario(UsuarioDTO cuentaDTO) {
		usuarioService.editarUsuario(cuentaDTO);
	}
	
	private boolean validarMail(UsuarioDTO cuentaDTO, BindingResult result) {
		 return usuarioService.validarMail(cuentaDTO.getUsername());
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
