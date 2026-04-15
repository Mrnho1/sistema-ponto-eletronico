package dio.portifolio.controller;

import dio.portifolio.dto.FuncionarioInputDTO;
import dio.portifolio.dto.FuncionarioOutputDTO;
import dio.portifolio.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/funcionarios")
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioService service;

    @PostMapping
    public FuncionarioOutputDTO criar(@RequestBody FuncionarioInputDTO dto) {
        return service.criar(dto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<FuncionarioOutputDTO> listar() {
        return service.listar();
    }
}