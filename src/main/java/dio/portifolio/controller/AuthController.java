package dio.portifolio.controller;

import dio.portifolio.dto.AuthRequestDTO;
import dio.portifolio.dto.AuthResponseDTO;
import dio.portifolio.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody AuthRequestDTO dto) {
        return service.login(dto);
    }
}