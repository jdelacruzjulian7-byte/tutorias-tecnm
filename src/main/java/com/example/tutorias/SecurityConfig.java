package com.example.tutorias;

import com.example.tutorias.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth

            // Recursos públicos
            .requestMatchers("/css/**", "/js/**", "/img/**", "/uploads/**",
                "/login", "/login?error", "/login?logout", "/acceso-denegado", "/", "/bienvenida")
            .permitAll()

            // Redirect post-login
            .requestMatchers("/dashboard/redirect").authenticated()

            // Dashboards por rol
            .requestMatchers("/dashboard/admin/**").hasRole("ADMIN")
            .requestMatchers("/dashboard/tutor/**").hasRole("TUTOR")
            .requestMatchers("/dashboard/tutorado/**").hasRole("TUTORADO")
            .requestMatchers("/dashboard/coordinador/**").hasRole("COORDINADOR")
            .requestMatchers("/dashboard/jefe/**").hasRole("JEFE")
            .requestMatchers("/dashboard/subdirector/**").hasRole("SUBDIRECTOR")

            // Usuarios: solo Admin
            .requestMatchers("/usuarios/**").hasRole("ADMIN")

            // Carreras y semestres: solo Admin
            .requestMatchers("/semestres/**", "/carreras/**").hasRole("ADMIN")

            // Tutores y asignaciones: Admin y Jefe
            .requestMatchers("/tutores/**").hasAnyRole("ADMIN", "JEFE")
            .requestMatchers("/asignaciones/**").hasAnyRole("ADMIN", "JEFE")

            // Tutorados: Admin, Tutor, Jefe, Coordinador
            .requestMatchers("/tutorados/**").hasAnyRole("ADMIN", "TUTOR", "JEFE", "COORDINADOR")

            // PAT: Admin crea/aprueba, Coordinador edita su carrera, Tutor y Jefe leen
            .requestMatchers("/pats/nuevo", "/pats/guardar", "/pats/eliminar/**",
                "/pats/asignar/**", "/pats/aprobar/**", "/pats/rechazar/**", "/pats/reabrir/**")
                .hasAnyRole("ADMIN")
            .requestMatchers("/pats/editar/**").hasAnyRole("ADMIN", "COORDINADOR")
            .requestMatchers("/pats/**").hasAnyRole("ADMIN", "COORDINADOR", "TUTOR", "JEFE", "SUBDIRECTOR")

            // Actividades:
            // - Admin y Coordinador: CRUD completo
            // - Tutor: puede crear/agregar (nuevo+guardar) pero NO editar ni eliminar las existentes
            // - Jefe: solo lectura
            .requestMatchers("/actividades/eliminar/**").hasAnyRole("ADMIN", "COORDINADOR")
            .requestMatchers("/actividades/editar/**").hasAnyRole("ADMIN", "COORDINADOR")
            .requestMatchers("/actividades/nuevo", "/actividades/guardar").hasAnyRole("ADMIN", "COORDINADOR", "TUTOR")
            .requestMatchers("/actividades/**").hasAnyRole("ADMIN", "COORDINADOR", "TUTOR", "JEFE")

            // Sesiones: Admin y Tutor
            .requestMatchers("/sesiones/**").hasAnyRole("ADMIN", "TUTOR", "COORDINADOR")

            // Asistencias: Tutor toma, Admin y Coordinador consultan
            .requestMatchers("/asistencias/**").hasAnyRole("ADMIN", "TUTOR", "COORDINADOR")

            // Documentos: todos los roles tienen acceso a ver documentos
            // El subdirector puede ver TODO
            .requestMatchers("/documentos/**").hasAnyRole("ADMIN", "SUBDIRECTOR", "COORDINADOR", "TUTOR", "TUTORADO", "JEFE")

            // Búsquedas
            .requestMatchers("/buscar/**").hasAnyRole("ADMIN", "COORDINADOR", "JEFE")

            .anyRequest().authenticated())

            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/dashboard/redirect", true)
                .failureUrl("/login?error=true")
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll())
            .exceptionHandling(ex -> ex.accessDeniedPage("/acceso-denegado"));

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder()).and().build();
    }
}