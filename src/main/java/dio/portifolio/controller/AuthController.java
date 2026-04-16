package dio.portifolio.controller;

import dio.portifolio.dto.AuthRequestDTO;
import dio.portifolio.dto.AuthResponseDTO;
import dio.portifolio.dto.FuncionarioInputDTO;
import dio.portifolio.dto.FuncionarioOutputDTO;
import dio.portifolio.service.AuthService;
import dio.portifolio.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;
    private final FuncionarioService funcionarioService;

    // recebe email + senha, manda para o service e devolve um token
    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody AuthRequestDTO dto) {
        return service.login(dto);
    }

    @PostMapping("/register")
    public FuncionarioOutputDTO cadastro(@RequestBody FuncionarioInputDTO dto) {
        return funcionarioService.criar(dto);
    }
}