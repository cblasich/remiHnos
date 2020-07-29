package com.remiNorte.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.remiNorte.app.models.service.JpaUserDetailsService;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JpaUserDetailsService userDetailsService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//"/" -> ruta raiz, /css/** -> todo lo que este dentro de css
		http.authorizeRequests().antMatchers("/css/**","/js/**","/open-iconic/**","/webfonts/**","/formViaje","/error_403",
				"/inicio","/","/formUsuario","/forgotPassword").permitAll()
		.antMatchers("/formOperador","/formTarifa","/iniOperador", 
				"/listarTarifas","/listarOperadores","/listarPasajeros","/listarRemises",
				"/listarViajes","/formOperador","/formRemis","/formTarifa").hasAnyRole("ADMIN","OPERAD")
		.antMatchers("/iniOperador").hasAnyRole("OPERAD")
		.anyRequest().authenticated()
		.and()
			.formLogin().loginPage("/login")
			.permitAll()
			.defaultSuccessUrl("/inicio")
		.and()
		.logout().permitAll().logoutSuccessUrl("/inicio")
		.and()
		.exceptionHandling().accessDeniedPage("/error_403");
	}

	
	
	@Autowired
	public void ConfigurerGlobal(AuthenticationManagerBuilder build) throws Exception {
		
		build.userDetailsService(userDetailsService)
		.passwordEncoder(passwordEncoder);
	}
}

