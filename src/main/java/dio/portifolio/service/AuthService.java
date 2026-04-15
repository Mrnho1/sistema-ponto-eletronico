package dio.portifolio.service;

import dio.portifolio.dto.AuthRequestDTO;
import dio.portifolio.dto.AuthResponseDTO;
import dio.portifolio.entity.Funcionario;
import dio.portifolio.repository.FuncionarioRepository;
import dio.portifolio.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final FuncionarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Responsável por autenticar o usuário
    public AuthResponseDTO login(AuthRequestDTO dto) {
        // Busca usuário pelo email
        Funcionario funcionario = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        // Verifica se a senha digitada bate com a criptografada
        if (!passwordEncoder.matches(dto.getSenha(), funcionario.getSenha())) {
            throw new RuntimeException("Senha inválida");
        }
        // Gera token JWT com base no email
        String token = jwtService.generateToken(
                funcionario.getEmail(),
                funcionario.getRole().name()
        );
        // Retorna token para o frontend
        return new AuthResponseDTO(token);
    }
}