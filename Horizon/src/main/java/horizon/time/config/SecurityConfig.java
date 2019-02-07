package horizon.time.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import horizon.time.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = false, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	/**
	 * General security override
	 */
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@PreAuthorize("hasRole('USER')")
	public @interface IsUser {
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/error**", "/resources/**", "/static/**", "/css/**", "/js/**", "/img/**");
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeRequests().antMatchers("/", "/business/view*", "/login*", "/register").permitAll()
				.antMatchers("/business/add*").hasRole("USER").antMatchers("/business/edit/**").hasRole("USER")
				.antMatchers("/business/view/**").hasRole("USER").antMatchers("/business/***").hasRole("USER")
				.antMatchers("/business/setimagecover/*").hasRole("USER").anyRequest().authenticated().and().formLogin()
				.loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/console", true).permitAll().and()
				.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // CSRF logout:
																						// https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#csrf-logout
				.invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll().and().sessionManagement()
				// .invalidSessionUrl("/invalidSession.html")
				.maximumSessions(1).sessionRegistry(sessionRegistry()).and().sessionFixation().none().and()
				.rememberMe();

		httpSecurity.headers().frameOptions().disable(); // can't add to iframes

		// Enable HSTS, risky - make sure you have an eternal SSL
		/*
		 * httpSecurity.headers() .httpStrictTransportSecurity()
		 * .includeSubDomains(true) .maxAgeInSeconds(31536000);
		 */

	}

	public SimpleUrlAuthenticationFailureHandler failureHandler() {
		return new SimpleUrlAuthenticationFailureHandler("/login?errored=true");
	}

	public SimpleUrlAuthenticationSuccessHandler successHandler() {
		SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler("/successLogin");
		handler.setUseReferer(true);
		handler.setTargetUrlParameter("url");
		return handler;
	}

	@Bean
	static public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private CustomUserDetailsService userDetailsService;

	/**
	 * Main authentication configuration Set the authentication provider
	 */
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	private DaoAuthenticationProvider authenticationProvider() {
		final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
		return authProvider;
	}
}