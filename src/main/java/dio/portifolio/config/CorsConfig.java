package dio.portifolio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.*;
import java.util.List;

// Indica que é uma configuração de sistema
@Configuration
public class CorsConfig {
    // Bean de CORS indica ser uma configuração do sistema
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();
        //Somente permite requisições desse endereço
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        //Permite todos os métodos http
        config.setAllowedMethods(List.of("*"));
        //Permite qualquer headers
        config.setAllowedHeaders(List.of("*"));
        //Permite cookies e autenticações
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //Aplica para todas as rotas
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
// ESte CORS Config faz
//1. Browser manda "preflight" (OPTIONS)
//2. Backend responde com regras CORS
//3. Browser valida
//4. Se permitido → faz requisição real