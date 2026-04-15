package dio.portifolio.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends org.springframework.web.filter.OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    // Não filtra quando o caminho for esses específicados
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return path.startsWith("/auth")
                || path.startsWith("/h2-console")
                || path.equals("/cadastro");
    }
    // Filtra quando ter essas especificações
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        // Aqui ele vai ler o header
        final String authHeader = request.getHeader("Authorization");
        // Se não tiver o token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        // remove o bearer com os 7 caracteres
        String token = authHeader.substring(7);
        // extrai o email do token
        // abre o jwt e lé o json
        String email = jwtService.extractEmail(token);
        // verifica se está logado
        // se tem email no token e ainda não tem usuário logado
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            try {
                // vai no banco e pega email, senha e role
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                // objeto de identificação
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                // detalhes da requisição
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // coloca no spring security
                SecurityContextHolder.getContext().setAuthentication(authToken);

            } catch (UsernameNotFoundException e) {
                System.out.println("Usuário do token não encontrado: " + email);
            }
        }

        filterChain.doFilter(request, response);
    }
}
// Este filtro faz
//1. pega request
//2. vê se tem token
//3. extrai email do token
//4. busca usuário no banco
//5. cria usuário autenticado
//6. coloca no Spring Security
//7. libera requisição