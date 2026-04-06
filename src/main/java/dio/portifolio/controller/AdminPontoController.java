package dio.portifolio.controller;

import dio.portifolio.dto.AtualizarPontoDTO;
import dio.portifolio.entity.RegistroPonto;
import dio.portifolio.service.RegistroPontoService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/pontos")
@RequiredArgsConstructor
public class AdminPontoController {

    private final RegistroPontoService service;

    @GetMapping("/{funcionarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<RegistroPonto> listarPorFuncionario(@PathVariable Long funcionarioId) {
        return service.listarPorFuncionario(funcionarioId);
    }
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void atualizar(@RequestBody AtualizarPontoDTO dto) {
        service.atualizarPonto(dto);
    }
    @GetMapping("/email")
    @PreAuthorize("hasRole('ADMIN')")
    public List<RegistroPonto> listarPorEmail(@RequestParam String email) {
        return service.listarPorEmail(email);
    }
}