package com.remiNorte.app.controllers;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.remiNorte.app.validations.UsuarioExisteException;
import com.remiNorte.app.models.entity.UsuarioDTO;
import com.remiNorte.app.security.ISecurityUserService;
import com.remiNorte.app.dto.PasswordDto;
import com.remiNorte.app.models.entity.GenericResponse;
import com.remiNorte.app.models.entity.Pasajero;
import com.remiNorte.app.models.entity.Usuario;
import com.remiNorte.app.models.service.IUsuarioService;

@Controller
@SessionAttributes("usuario")
public class UsuarioController {

	@Autowired 
	private IUsuarioService usuarioService;
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Autowired
    private MessageSource messages;
	
	@Autowired
    private Environment env;
	
	@Autowired
    private ISecurityUserService securityUserService;
	
	private Logger logger = LoggerFactory.getLogger(UsuarioController.class);
	
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
		model.put("backPage", "/inicio");
		
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
			error = 1;
			result.rejectValue("password", "password", "Debe ingresar una contraseña.");
		} else {
			if (!password.equals(passwordConf)) {
				error = 1;
				result.reject("usuario.password","Las contraseñas no coinciden.");
			}
		}		
		
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
			model.addAttribute("backPage", "/inicio");
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
	
	@PostMapping("/user/resetPassword")
	public GenericResponse resetPassword(HttpServletRequest request, 
	  @RequestParam("email") String userEmail) {
	    Usuario user = usuarioService.findByUsername(userEmail);
	    if (user == null) {
	        //throw new UserNotFoundException();
	    }
	    
	    logger.info("resertpassword");
	    String token = UUID.randomUUID().toString();
	    usuarioService.createPasswordResetTokenForUser(user, token);
	    mailSender.send(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, user));
	    return new GenericResponse(messages.getMessage("message.resetPasswordEmail", null, request.getLocale()));
	}
	
	@GetMapping("/user/changePassword")
    public String showChangePasswordPage(final Locale locale, final Model model, @RequestParam("id") final long id, @RequestParam("token") final String token) {
        final String result = securityUserService.validatePasswordResetToken(id, token);
        if (result != null) {
            model.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
            return "redirect:/login?lang=" + locale.getLanguage();
        }
        return "redirect:/updatePassword.html?lang=" + locale.getLanguage();
    }
	
	@PostMapping("/user/savePassword")
    public GenericResponse savePassword(final Locale locale, @Valid PasswordDto passwordDto) {
        final Usuario user = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        usuarioService.changeUserPassword(user, passwordDto.getNewPassword());
        return new GenericResponse(messages.getMessage("message.resetPasswordSuc", null, locale));
    }
	
	@GetMapping("/forgotPassword")
	public String olvidaPassword(Model model) {
		
		model.addAttribute("backPage", "/inicio");
		return "forgotPassword";
	}
	
	// ============== NON-API ============
	
	private SimpleMailMessage constructResetTokenEmail(
			  String contextPath, Locale locale, String token, Usuario user) {
			    String url = contextPath + "/user/changePassword?token=" + token;
			    String message = messages.getMessage("message.resetPassword", 
			      null, locale);
			    return constructEmail("Reset Password", message + " \r\n" + url, user);
			}
			 
	private SimpleMailMessage constructEmail(String subject, String body, 
	  Usuario user) {
	    SimpleMailMessage email = new SimpleMailMessage();
	    email.setSubject(subject);
	    email.setText(body);
	    email.setTo(user.getUsername());
	    email.setFrom(env.getProperty("support.email"));
	    return email;
	}
	
	private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
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
