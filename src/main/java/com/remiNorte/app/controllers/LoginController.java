package com.remiNorte.app.controllers;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
	
	@GetMapping(value = "/login")
	public String login(@RequestParam(value="error", required=false) String error,
			@RequestParam(value="logout", required=false) String logout,
			Model model, Principal principal, RedirectAttributes flash) {
		
			model.addAttribute("titulo", "Iniciar sesión");
			model.addAttribute("backPage","/inicio");
			
		if (principal != null) { //si ya inició sesion anteriormente
			flash.addFlashAttribute("info", "Ya ha iniciado sesión anteriormente");
			return "redirect:inicio";
		}
		
		if (error != null) {
			model.addAttribute("error", "Error: e-mail o contraseña incorrectos, por favor vuelva a intentarlo.");
		}
		
		if (logout != null) {
			model.addAttribute("success", "Ha cerrado sesión con éxito.");
		}
		
		return "login";
	}
}

