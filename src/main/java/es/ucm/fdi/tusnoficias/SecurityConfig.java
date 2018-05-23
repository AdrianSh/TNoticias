package es.ucm.fdi.tusnoficias;

import java.io.File;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	/*
	@Value("${spring.queries.users-query}")
	private String usersQuery;

	@Value("${spring.queries.roles-query}")
	private String rolesQuery;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// auth.jdbcAuthentication().
		
		/*
		 * auth.
			jdbcAuthentication()
				.usersByUsernameQuery(usersQuery)
				.authoritiesByUsernameQuery(rolesQuery)
				.dataSource(dataSource)
				.passwordEncoder(bCryptPasswordEncoder);
		 
		auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder);
	}
	*/
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
        		.antMatchers("/static/**", "/logout", "/403", "/**").permitAll()
				.mvcMatchers("/admin").hasRole("ADMIN")
        		.antMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.permitAll()
	            .loginPage("/login")
	            .and()
			.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login")
	            .permitAll();
	}
	
	@Bean
	public IwUserDetailsService springDataUserDetailsService() {
		return new IwUserDetailsService();
	}
	
/* 
	Si eliminas el "Bean" anterior, esto funciona sin BD ni nada:

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) 
			throws Exception {
		auth.inMemoryAuthentication()
				.withUser("user").password("password").roles("USER")
				.and()
				.withUser("paco").password("password").roles("USER", "ADMIN")
				.and()
				.withUser("juan").password("password").roles("USER", "ADMIN");
	}
*/
	
	@Bean(name="passwordEncoder")
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Value("${es.ucm.fdi.base-path}")
	private String basePath;
	
    @Bean(name="localData")
    public LocalData getLocalData() {
    	return new LocalData(new File(basePath));
    }    
}