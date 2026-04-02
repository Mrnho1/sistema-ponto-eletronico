package dio.portifolio.service;

import dio.portifolio.entity.BancoHoras;
import dio.portifolio.entity.Funcionario;
import dio.portifolio.repository.BancoHorasRepository;
import dio.portifolio.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BancoHorasService {

    private final BancoHorasRepository repository;
    private final FuncionarioRepository funcionarioRepository;

    public BancoHoras getSaldoUsuarioLogado() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Funcionario funcionario = funcionarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        return repository.findByFuncionarioId(funcionario.getId())
                .orElse(BancoHoras.builder()
                        .funcionario(funcionario)
                        .saldoMinutos(0L)
                        .build());
    }
}