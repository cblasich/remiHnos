package com.remiNorte.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller 
public class IniOpeController {

	@RequestMapping(value= {"/iniOperador"}, method=RequestMethod.GET)
    public String welcome(Model model) {
        return "iniOperador";
    }
	
}
