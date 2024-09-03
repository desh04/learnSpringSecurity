package com.desh.learnSpringSecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.desh.learnSpringSecurity.filter.JwtFilter;

@Configuration // to tell if it is configuration class
@EnableWebSecurity // to tell spring that need to override the filter channing with this class
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    // defining bean to inject the config object
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSec) throws Exception {
        // if no filter is here then it will have no default filter chaning

        // httpSec.csrf(customizer -> customizer.disable()); // Disable CSRF // will
        // handle this by stateless session
        // httpSec.authorizeHttpRequests(request ->
        // request.anyRequest().authenticated()); // to enable authentication
        // httpSec.formLogin(Customizer.withDefaults()); // for enabling login from the
        // UI this will return the html , so
        // // if only this is used it will not work with the postman
        // httpSec.httpBasic(Customizer.withDefaults()); // For enabling login from the
        // api clients also( postman)

        // // making http state less to prevent CSRF
        // httpSec.sessionManagement(session ->
        // session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // for
        // // CSRF
        // if on UI you see that after login for it is asking for login agaign and again
        // this is case session ID is changing every time and you will need to login
        // To prevent this you could remove the httpSec.formLogin

        // in impretive way of coding
        // Customizer<CsrfConfigurer<HttpSecurity>> custCsrf = new
        // Customizer<CsrfConfigurer<HttpSecurity>>() {
        // @Override
        // public void customize(CsrfConfigurer<HttpSecurity>
        // httpSecurityScrfConfigurer) {
        // httpSecurityScrfConfigurer.disable();
        // }
        // };

        // Customizer<AuthorizationManagerRequestMatcherRegistry> authorizeHttpReq = new
        // Customizer<AuthorizationManagerRequestMatcherRegistry>() {

        // @Override
        // public void customize(AuthorizationManagerRequestMatcherRegistry request) {
        // ((AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl)
        // request.anyRequest()).authenticated();

        // }
        // };
        // httpSec.csrf(custCsrf);

        // httpSec.authorizeHttpRequests(authorizeHttpReq);

        // even shorter way of applying the above filter chaining (BUILDER PATTEREN)
        // one object is passing through different method
        httpSec
                .csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("register", "login").permitAll() // permiting resquest with auth which matches
                                                                          // with "login" and "register" pattern
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        // .formLogin(Customizer.withDefaults()).httpBasic(Customizer.withDefaults())

        return httpSec.build();
    }

    // @Bean
    // public UserDetailsService userDetailsService() {

    // UserDetails user1 = User
    // .withDefaultPasswordEncoder()
    // .username("dd2")
    // .password("d@222")
    // .roles("USER").build(); // don't use it in production(deprecated)

    // UserDetails user2 = User
    // .withDefaultPasswordEncoder()
    // .username("dd3")
    // .password("d@333")
    // .roles("ADMIN")
    // .build();

    // // InMemoryUserDetailsManager implement the UserDetailsService
    // return new InMemoryUserDetailsManager(user1, user2);

    // }

    // Working with data base
    // Un-Authenticated Object ===> Authentication Provider ==> Authenticated Object
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // we have multiple authetication provider, using Dao
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // provider.setPasswordEncoder((NoOpPasswordEncoder.getInstance())); // default
        // using BCryptPassword Encoder
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
