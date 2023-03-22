package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebSecurityAuthorization {

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/rest/**").hasAuthority("ADMIN")
                .antMatchers("/h2-console/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/clients").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/accounts").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/loans").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.GET, "/api/transactions").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET,"/web/views/adminPanel.html").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET,"/web/views/accounts.html").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.GET,"/web/views/account.html").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.GET,"/web/views/cards.html").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.GET,"/web/views/create-cards.html").hasAnyAuthority("CLIENT")
                .antMatchers(HttpMethod.GET,"/web/views/transfer.html").hasAnyAuthority("CLIENT")
                .antMatchers(HttpMethod.GET,"/web/views/loan-application.html").hasAnyAuthority("CLIENT")
                .antMatchers(HttpMethod.GET,"/web/views/cardPayment.html").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/accounts").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/clients/current/accounts").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/clients/current/cards").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/transactions").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/loans").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/clients").permitAll()
                .antMatchers(HttpMethod.GET,"/web/views/index.html").permitAll();

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");


        http.logout().logoutUrl("/api/logout");

        http.csrf().disable(); // desactiva la comprobación csrf tokens
        http.headers().frameOptions().disable(); // desactiva frameOption h2 se puede acceder
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED)); // si el usuario no está autenticado, solo responde un fallo de autenticación
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));   // si el inicio de sesión es exitoso, limpia las banderas que piden autenticación
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED)); // si el login falla, envía una respuesta de fallo de autenticación
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()); // si el logout es exitoso, envía una respuesta de éxito

        return http.build();
    }

//    Este método elimina el atributo WebAttributes.AUTHENTICATION_EXCEPTION de la sesión si existe. Este atributo se utiliza para almacenar información de error de autenticación en la sesión,
//    y eliminarlo ayuda a garantizar que no se pueda acceder a la información de error de autenticación después de que el usuario haya iniciado sesión correctamente.
    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}

// here WebSecurityConfigurerAdapter is used which is deprecated
//
//@EnableWebSecurity
//@Configuration
//class WebSecurityAuthorization extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http.authorizeRequests()
//                .antMatchers("/rest/**").hasAuthority("ADMIN")
//                .antMatchers("/h2-console/**").hasAuthority("ADMIN")
//                .antMatchers(HttpMethod.GET, "/api/clients").hasAuthority("ADMIN")
//                .antMatchers(HttpMethod.GET, "/api/accounts").hasAuthority("ADMIN")
//                .antMatchers(HttpMethod.GET,"/web/accounts.html").hasAnyAuthority("CLIENT", "ADMIN")
//                .antMatchers(HttpMethod.GET,"/web/account.html").hasAnyAuthority("CLIENT", "ADMIN")
//                .antMatchers(HttpMethod.GET,"/web/cards.html").hasAnyAuthority("CLIENT", "ADMIN")
//                .antMatchers(HttpMethod.GET,"/web/create-cards.html").hasAnyAuthority("CLIENT")
//                .antMatchers(HttpMethod.POST, "/api/accounts").hasAnyAuthority("CLIENT")
//                .antMatchers(HttpMethod.POST, "/api/clients/current/accounts").hasAuthority("CLIENT")
//                .antMatchers(HttpMethod.POST, "/api/clients/current/cards").hasAuthority("CLIENT")
//                .antMatchers(HttpMethod.POST, "/api/clients").permitAll()
//                .antMatchers(HttpMethod.GET,"/web/index.html").permitAll();
//
//        http.formLogin()
//                .usernameParameter("name")
//                .passwordParameter("pwd")
//                .loginPage("/api/login");
//
//
//        http.logout().logoutUrl("/api/logout");
//
//        http.csrf().disable(); // desactiva la comprobación csrf tokens
//        http.headers().frameOptions().disable(); // desactiva frameOption h2 se puede acceder
//        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED)); // si el usuario no está autenticado, solo responde un fallo de autenticación
//        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));   // si el inicio de sesión es exitoso, limpia las banderas que piden autenticación
//        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED)); // si el login falla, envía una respuesta de fallo de autenticación
//        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()); // si el logout es exitoso, envía una respuesta de éxito
//
//    }
//
//
//    //    Este método elimina el atributo WebAttributes.AUTHENTICATION_EXCEPTION de la sesión si existe. Este atributo se utiliza para almacenar información de error de autenticación en la sesión,
////    y eliminarlo ayuda a garantizar que no se pueda acceder a la información de error de autenticación después de que el usuario haya iniciado sesión correctamente.
//    private void clearAuthenticationAttributes(HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//        if (session != null) {
//            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
//        }
//    }
//}
