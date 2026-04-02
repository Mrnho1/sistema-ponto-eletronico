package dio.portifolio.controller;


import dio.portifolio.dto.BancoHorasDTO;
import dio.portifolio.dto.RelatorioDTO;
import dio.portifolio.entity.BancoHoras;
import dio.portifolio.service.BancoHorasService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/banco-horas")
@RequiredArgsConstructor
public class BancoHorasController {

    private final BancoHorasService service;

    @GetMapping("/me")
    public BancoHorasDTO getSaldo() {
        return service.getSaldoUsuarioLogado();
    }
    @GetMapping("/relatorio")
    public List<RelatorioDTO> relatorio(
            @RequestParam String inicio,
            @RequestParam String fim
    ) {
        return service.relatorio(LocalDate.parse(inicio), LocalDate.parse(fim));
    }
}