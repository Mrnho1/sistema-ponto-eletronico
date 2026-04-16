package dio.portifolio.config;

import dio.portifolio.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    //Pipeline de segurança
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                //desativa csrf : proteção contra ataque de formulário
                .csrf(csrf -> csrf.disable())
                //permite abrir o h2 no navegador
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                //define quem pode acessar o que
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/h2-console/**","/swagger-ui/**","/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/register").permitAll()
                        //rota autenticada
                        .requestMatchers("/pontos/**").authenticated()
                        .requestMatchers("/banco-horas/**").authenticated()
                        //rota com acesso limitado ao admin
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        //o restante precisa estar logado
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
// Este config
// 1. Verifica quem pode acessar cada rota
// 2. Quando precisa de login
// 3. Quando precisa ser admin