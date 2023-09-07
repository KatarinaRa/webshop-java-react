package webshop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import webshop.config.jwt.AuthJwt;
import webshop.config.jwt.TokenVerifier;
import webshop.config.services.CustomUserDetailsService;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
public class WebShopConfiguration { 
  @Autowired
  CustomUserDetailsService userDetailsService;

  @Autowired
  private AuthJwt unauthorizedHandler;

  @Bean
  public TokenVerifier authenticationTokenVerifier() {
    return new TokenVerifier();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
       
      authProvider.setUserDetailsService(userDetailsService);
      authProvider.setPasswordEncoder(passwordEncoder());
   
      return authProvider;
  }
  
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	  http.cors(withDefaults())
	          .csrf(csrf -> csrf.disable())
	          .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
	          .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	          .authorizeHttpRequests(auth ->
	                  auth.requestMatchers("/auth/**",
	                		  "/favicon.ico",
	                		  "/products/images/**",
	                		  "/webshop v2/upload/**",
	                		  "/api/test/**",
	                		  "/products/**").permitAll()
	                  .requestMatchers(HttpMethod.PUT).hasRole("ADMIN")  
	                  .requestMatchers(
	                		    "/orders/**",
	                		    "/orderDetails/all/**",
	                		    "/orderDetails/**",
	                		    "/productscategory/**",
	                		    "/sizes/**",
	                		    "/colors/**",
	                		    "/genders/**",
	                		    "/products/{id}/sizes/**",
	                		    "/name/{sizeId}",
	                		    "/complite/**").authenticated()
	                  .requestMatchers("/**").hasRole("ADMIN")
	                          .anyRequest().authenticated()
	          );

	http.authenticationProvider(authenticationProvider());

	http.addFilterBefore(authenticationTokenVerifier(), UsernamePasswordAuthenticationFilter.class);

	return http.build();
	}
  
}