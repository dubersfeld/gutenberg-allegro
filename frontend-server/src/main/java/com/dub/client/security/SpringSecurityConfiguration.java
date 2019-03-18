package com.dub.client.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.dub.client.services.UserService;

@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true, order = 0, mode = AdviceMode.PROXY,
        proxyTargetClass = true
)
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {
		  
	@Autowired
	private UserService userService;
	
	@Lazy
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception
	{    
		return super.authenticationManagerBean();
	}
	
	@Bean
	public SimpleUrlAuthenticationSuccessHandler myAuthenticationSuccessHandler() {
		
		SimpleUrlAuthenticationSuccessHandler handler 
				= new SimpleUrlAuthenticationSuccessHandler();
			
		handler.setDefaultTargetUrl("/index");
		
		return handler;
	}
	

    @Bean
    protected SessionRegistry sessionRegistryImpl() {
        return new SessionRegistryImpl();
    }

    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
	  
    	 builder
         	.userDetailsService(this.userService)     
         	.passwordEncoder(passwordEncoder())
         	.and()
         	.eraseCredentials(true);
    }

    
    @Override
    protected void configure(HttpSecurity security) 
    		throws Exception {
        security
                .authorizeRequests()  
                	.antMatchers("/register").permitAll()
                    .antMatchers("/login/**").permitAll()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/logout").permitAll()    
                    .antMatchers("/**").hasAuthority("ROLE_USER")                                                
                    .and().formLogin()
                    .loginPage("/login").failureUrl("/login?loginFailed")
                    .successHandler(myAuthenticationSuccessHandler()) 
                    //.defaultSuccessUrl("/index")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .permitAll()
                .and().logout()
                    .logoutUrl("/logout")
                    .invalidateHttpSession(true).deleteCookies("JSESSIONID")
                    .permitAll()
                .and().sessionManagement()
                    .sessionFixation().changeSessionId()
                    .maximumSessions(1).maxSessionsPreventsLogin(false)
                    .sessionRegistry(this.sessionRegistryImpl())
                .and().and().csrf().disable();
        
    }            

    @Bean
    public PasswordEncoder passwordEncoder() {
          return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}