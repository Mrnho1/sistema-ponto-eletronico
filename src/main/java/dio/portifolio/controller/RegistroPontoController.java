package dio.portifolio.controller;


import dio.portifolio.dto.RegistroPontoDTO;
import dio.portifolio.entity.RegistroPonto;
import dio.portifolio.service.RegistroPontoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/pontos")
@RequiredArgsConstructor
public class RegistroPontoController {

    private final RegistroPontoService service;

    @PostMapping("/{funcionarioId}")
    public RegistroPonto baterPonto(@PathVariable Long funcionarioId,
                                    @RequestBody RegistroPontoDTO dto) {
        return service.baterPonto(funcionarioId, dto);
    }

    @GetMapping("/{funcionarioId}")
    public List<RegistroPonto> listar(@PathVariable Long funcionarioId) {
        return service.listarPorFuncionario(funcionarioId);
    }
}