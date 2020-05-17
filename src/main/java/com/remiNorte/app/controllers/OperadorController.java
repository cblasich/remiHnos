package com.remiNorte.app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.remiNorte.app.models.dao.IOperadorDao;
import com.remiNorte.app.models.entity.Operador;
import com.remiNorte.app.models.entity.Rol;
import com.remiNorte.app.models.entity.Usuario;

@Controller
@SessionAttributes({"operador","usuario"}) 
public class OperadorController {
	
	@Autowired
	private IOperadorDao operadorDao;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	private Logger logger = LoggerFactory.getLogger(OperadorController.class);
	
	@RequestMapping(value="/listarOperadores", method=RequestMethod.GET)
	public String listarOperadores(Model model) {
		
		model.addAttribute("titulo", "Listado de Operadores");
		model.addAttribute("backPage", "/iniOperador");
		model.addAttribute("operadores" , operadorDao.findAll());
		return "listarOperadores";		
	}
	
	@RequestMapping(value="/formOperador", method=RequestMethod.GET) //fase inicial formulario
	public String crear(Model model) {
		logger.info("GET operador");
		Operador operador = new Operador();
		//Usuario usuario = new Usuario();
		//logger.info("Passowrd:".concat(usuario.getPassword()));		
		model.addAttribute("titulo", "Formulario de Operador");
		model.addAttribute("operador", operador);
		//model.addAttribute("usuario", usuario);
		
		return "formOperador";
	}
	
	@RequestMapping(value="/formOperador", method=RequestMethod.POST) //fase submit del formulario
	public String guardar(Operador operador) {
		
		Usuario usuario = new Usuario();
		Rol rol = new Rol();
		rol.setRolOperad();
		
		usuario = operador.getUsuario();
		String bcryptPassword = passwordEncoder.encode(usuario.getPassword());
		usuario.setPassword(bcryptPassword);		
		usuario.setRol(rol);
		usuario.setUsuEnabled(true);
		operador.setUsuario(usuario);
			
		operadorDao.save(operador);
		return "redirect:listarOperadores";
	}
	
	@RequestMapping(value="/eliminarOperador/{id}")
	public String eliminar(@PathVariable(value="id") Long id) {
		if (id > 0) {
			operadorDao.deleteById(id);
		}		
		return "redirect:/listarOperadores";
	}
}
