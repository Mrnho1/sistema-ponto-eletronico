package dio.portifolio.service;

import dio.portifolio.dto.BancoHorasDTO;
import dio.portifolio.dto.RelatorioDTO;
import dio.portifolio.entity.BancoHoras;
import dio.portifolio.entity.Funcionario;
import dio.portifolio.entity.RegistroPonto;
import dio.portifolio.repository.BancoHorasRepository;
import dio.portifolio.repository.FuncionarioRepository;
import dio.portifolio.repository.RegistroPontoRepository;
import dio.portifolio.util.RegistroPontoUtils;
import dio.portifolio.util.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static dio.portifolio.util.RegistroPontoUtils.calcularMinutosTrabalhados;

@Service
@RequiredArgsConstructor
public class BancoHorasService {

    private final BancoHorasRepository repository;
    private final RegistroPontoRepository registroPontoRepository;
    private final FuncionarioRepository funcionarioRepository;


    public BancoHorasDTO getSaldoUsuarioLogado() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Funcionario funcionario = funcionarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        BancoHoras banco = repository.findByFuncionarioId(funcionario.getId())
                .orElse(BancoHoras.builder()
                        .funcionario(funcionario)
                        .saldoMinutos(0L)
                        .build());

        return BancoHorasDTO.builder()
                .saldoMinutos(banco.getSaldoMinutos())
                .saldoFormatado(TimeUtils.formatarMinutos(banco.getSaldoMinutos()))
                .build();
    }
    public List<RelatorioDTO> relatorio(LocalDate inicio, LocalDate fim) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Funcionario funcionario = funcionarioRepository.findByEmail(email)
                .orElseThrow();

        List<RegistroPonto> registros = registroPontoRepository.findByFuncionarioId(funcionario.getId());

        Map<LocalDate, List<RegistroPonto>> agrupado = registros.stream()
                .collect(Collectors.groupingBy(r -> r.getDataHora().toLocalDate()));

        return agrupado.entrySet().stream()
                .filter(e -> !e.getKey().isBefore(inicio) && !e.getKey().isAfter(fim))
                .map(e -> {

                    long minutosTrabalhados = calcularMinutosTrabalhados(e.getValue());

                    long minutosEsperados = funcionario.getJornadaMinutos();

                    long saldoMinutos = minutosTrabalhados - minutosEsperados;

                    return RelatorioDTO.builder()
                            .data(e.getKey().toString())
                            .horasTrabalhadas(TimeUtils.formatarMinutos(minutosTrabalhados))
                            .horasEsperadas(TimeUtils.formatarMinutos(minutosEsperados))
                            .saldo(TimeUtils.formatarMinutos(saldoMinutos))
                            .build();
                })
                .toList();
    }

}