package com.remiNorte.app.controllers;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.remiNorte.app.models.entity.Usuario;
import com.remiNorte.app.models.service.IEmailService;
import com.remiNorte.app.models.service.IUsuarioService;

@Controller
public class PasswordController {
	
	@Autowired
	private IUsuarioService userService;

	@Autowired
	private IEmailService emailService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private Logger logger = LoggerFactory.getLogger(PasswordController.class);
	
	// Display forgotPassword page
	@RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
	public ModelAndView displayForgotPasswordPage() {
		return new ModelAndView("forgotPassword");
    }
    
    // Process form submission from forgotPassword page
	@RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
	public String processForgotPasswordForm(Model model,
											@RequestParam("email") String userEmail, 
											HttpServletRequest request, 
											RedirectAttributes flash) {

		// Lookup user in database by e-mail
		Usuario optional = null;
		optional = userService.findByUsername(userEmail);
		
		if (optional == null) {
			//modelAndView.addObject("errorMessage", "We didn't find an account for that e-mail address.");
		} else {
			
			// Generate random 36-character string token for reset password 
			Usuario user = optional;
			user.setUsuResTok(UUID.randomUUID().toString());

			// Save token to database
			userService.save(user);

			String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getLocalPort();
			logger.info(appUrl);
			
			// Email message
			SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
			passwordResetEmail.setFrom("support@demo.com");
			passwordResetEmail.setTo(user.getUsername());
			passwordResetEmail.setSubject("Password Reset Request");
			passwordResetEmail.setText("To reset your password, click the link below:\n" + appUrl
					+ "/resetPassword?token=" + user.getUsuResTok());
			
			emailService.sendEmail(passwordResetEmail);

			// Add success message to view
			//modelAndView.addObject("successMessage", "A password reset link has been sent to " + userEmail);
		}
		
		flash.addFlashAttribute("success","Si el E-Mail es válido, recibirá un correo de reinicio de contraseña.");
		//modelAndView.setViewName("inicio");
		//return modelAndView;
		return "redirect:inicio";

	}

	// Display form to reset password
	@RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
	public String displayResetPasswordPage(Model model, 
												@RequestParam("token") String token) {
		
		Usuario user = null;
		logger.info("token GET:".concat(token));
		user = userService.findByUsuResTok(token);

		if (user != null) { // Token found in DB
			logger.info("user encontrado");
			//modelAndView.addObject("token", token);
			model.addAttribute("token", token);
		} else { // Token not found in DB
			//modelAndView.addObject("errorMessage", "Oops!  This is an invalid password reset link.");
			model.addAttribute("error", "Oops!  This is an invalid password reset link.");
		}

		//modelAndView.setViewName("resetPassword");
		//return modelAndView;
		return "resetPassword";
	}

	// Process reset password form
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public String setNewPassword(Model model, 
								@RequestParam Map<String, String> requestParams, 
								RedirectAttributes redir,
								@RequestParam("password") String password,
								@RequestParam("confirmPassword") String confirmPassword,
								@RequestParam("token") String token) {
		
		// Find the user associated with the reset token
		Usuario user = null;
		logger.info("token POST:".concat(token));
		
		user = userService.findByUsuResTok(token);

		// This should always be non-null but we check just in case
		if (user != null) {
			
			Usuario resetUser = user;
            
			// Set new password    
            resetUser.setPassword(bCryptPasswordEncoder.encode(password));
            
			// Set the reset token to null so it cannot be used again
			resetUser.setUsuResTok(null);

			// Save user
			userService.save(resetUser);

			// In order to set a model attribute on a redirect, we must use
			// RedirectAttributes
			redir.addFlashAttribute("success", "La contraseña fue cambiada exitosamente.");

			//modelAndView.setViewName("redirect:login");
			//return modelAndView;
			return "redirect:login";
			
		} else {
			//modelAndView.addObject("error", "Oops!  This is an invalid password reset link.");
			//modelAndView.setViewName("resetPassword");
			
			redir.addFlashAttribute("error", "Oops!  Este es un link de reinicio de contraseña inválido.");
			return "redirect:inicio";
		}
		
		//return "redirecct:login";
   }
   
    // Going to reset page without a token redirects to login page
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ModelAndView handleMissingParams(MissingServletRequestParameterException ex) {
		return new ModelAndView("redirect:login");
	}
}
