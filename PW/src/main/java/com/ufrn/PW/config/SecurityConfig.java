package com.ufrn.PW.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.ufrn.PW.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/login", "/cadusuario").permitAll();
                    auth.requestMatchers("/index").permitAll();
                    auth.requestMatchers("/admin/**", "/cadastro", "/salvar", "/editar", "/deletar").hasRole("ADMIN");
                    auth.requestMatchers("/verCarrinho", "/adicionarCarrinho", "/finalizarcompra").hasRole("USER");
                    auth.anyRequest().authenticated();
                })
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/default", true)
                        .permitAll())
                .logout(l -> {
                    l.logoutUrl("/logout");
                    l.logoutSuccessUrl("/logout-success");
                    l.clearAuthentication(true);
                    l.deleteCookies("JSESSIONID");
                    l.invalidateHttpSession(true);
                })
                .exceptionHandling(e -> e.accessDeniedHandler(new CustomAccessDeniedHandler())); 

        return http.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }
}
