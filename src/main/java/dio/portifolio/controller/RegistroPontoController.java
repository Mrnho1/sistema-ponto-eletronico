package dio.portifolio.controller;


import dio.portifolio.dto.RegistroPontoDTO;
import dio.portifolio.entity.RegistroPonto;
import dio.portifolio.service.RegistroPontoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/pontos")
@RequiredArgsConstructor
public class RegistroPontoController {

    private final RegistroPontoService service;

    @PostMapping
    public RegistroPonto baterPonto(@RequestBody RegistroPontoDTO dto) {
        return service.baterPonto(dto);
    }

    @GetMapping("/me")
    public List<RegistroPonto> listar() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return service.listarPorEmail(email);
    }
}