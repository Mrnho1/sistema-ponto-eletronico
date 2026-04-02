package dio.portifolio.service;

import dio.portifolio.dto.RegistroPontoDTO;
import dio.portifolio.entity.BancoHoras;
import dio.portifolio.entity.Funcionario;
import dio.portifolio.entity.RegistroPonto;
import dio.portifolio.entity.TipoRegistro;
import dio.portifolio.repository.BancoHorasRepository;
import dio.portifolio.repository.FuncionarioRepository;
import dio.portifolio.repository.RegistroPontoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static dio.portifolio.util.RegistroPontoUtils.calcularMinutosTrabalhados;

@Service
@RequiredArgsConstructor
public class RegistroPontoService {

    private static final long JORNADA_DIARIA_MINUTOS = 480;

    private final RegistroPontoRepository repository;
    private final FuncionarioRepository funcionarioRepository;
    private final BancoHorasRepository bancoHorasRepository;

    public RegistroPonto baterPonto(RegistroPontoDTO dto) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Funcionario funcionario = funcionarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        List<RegistroPonto> registros = repository.findByFuncionarioId(funcionario.getId());


        if (!registros.isEmpty()) {
            RegistroPonto ultimo = registros.get(registros.size() - 1);

            if (ultimo.getTipo() == dto.getTipo()) {
                throw new RuntimeException("Não pode registrar duas vezes seguidas o mesmo tipo");
            }
        }

        RegistroPonto registro = RegistroPonto.builder()
                .dataHora(java.time.LocalDateTime.now())
                .tipo(dto.getTipo())
                .funcionario(funcionario)
                .build();

        if (dto.getTipo() == TipoRegistro.SAIDA) {

            LocalDateTime inicioDia = LocalDateTime.now().toLocalDate().atStartOfDay();
            LocalDateTime fimDia = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);

            List<RegistroPonto> registrosDia = repository
                    .findByFuncionarioIdAndDataHoraBetween(
                            funcionario.getId(),
                            inicioDia,
                            fimDia
                    );

            long totalMinutos = calcularMinutosTrabalhados(registrosDia);

            long saldo = totalMinutos - funcionario.getJornadaMinutos();

            atualizarBancoHoras(funcionario, saldo);
        }

        return repository.save(registro);
    }
    private void atualizarBancoHoras(Funcionario funcionario, long saldoMinutos) {

        BancoHoras banco = bancoHorasRepository
                .findByFuncionarioId(funcionario.getId())
                .orElse(BancoHoras.builder()
                        .funcionario(funcionario)
                        .saldoMinutos(0L)
                        .build());

        banco.setSaldoMinutos(banco.getSaldoMinutos() + saldoMinutos);

        bancoHorasRepository.save(banco);
    }


    public List<RegistroPonto> listarPorEmail(String email) {

        Funcionario funcionario = funcionarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        return repository.findByFuncionarioId(funcionario.getId());
    }
}