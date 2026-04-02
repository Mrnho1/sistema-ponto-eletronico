package dio.portifolio.controller;


import dio.portifolio.entity.BancoHoras;
import dio.portifolio.service.BancoHorasService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/banco-horas")
@RequiredArgsConstructor
public class BancoHorasController {

    private final BancoHorasService service;

    @GetMapping("/me")
    public BancoHoras getSaldo() {
        return service.getSaldoUsuarioLogado();
    }
}