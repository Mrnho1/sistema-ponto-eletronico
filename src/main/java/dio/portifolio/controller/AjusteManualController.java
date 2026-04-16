package dio.portifolio.controller;

import dio.portifolio.dto.AjusteManualDTO;
import dio.portifolio.service.AjusteManualService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/ajustes")
@RequiredArgsConstructor
public class AjusteManualController {

    private final AjusteManualService service;
    //Atualizar usando post para cirar histórico de alteração
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void ajustar(@RequestBody AjusteManualDTO dto) {
        service.ajustar(dto);
    }
}