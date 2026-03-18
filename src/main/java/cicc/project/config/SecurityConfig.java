package cicc.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // 1. Recursos estáticos
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                
                // 2. Rutas públicas de autenticación
                .requestMatchers("/login", "/register", "/error").permitAll()
                
                // 3. El simulador de Mocks debe ser público para consumo externo
                .requestMatchers("/api/v1/mock/**", "/api/v1/status").permitAll()
                
                // 4. Gestión solo para ADMIN
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // 5. Todo lo demás requiere autenticación
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/manage/mocks", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // Sintaxis moderna que elimina el warning
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );
            // Eliminamos la configuración de frameOptions porque ya no tenemos consola H2

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}