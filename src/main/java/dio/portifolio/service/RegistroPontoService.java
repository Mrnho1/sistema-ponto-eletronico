package dio.portifolio.service;

import dio.portifolio.dto.AtualizarPontoDTO;
import dio.portifolio.dto.RegistroPontoDTO;
import dio.portifolio.entity.BancoHoras;
import dio.portifolio.entity.Funcionario;
import dio.portifolio.entity.RegistroPonto;
import dio.portifolio.entity.TipoRegistro;
import dio.portifolio.repository.BancoHorasRepository;
import dio.portifolio.repository.FuncionarioRepository;
import dio.portifolio.repository.RegistroPontoRepository;
import dio.portifolio.util.RegistroPontoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
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
        if (dto.getTipo() == null) {
            throw new RuntimeException("Tipo é obrigatório");
        }

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Funcionario funcionario = funcionarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        List<RegistroPonto> registros = repository.findByFuncionarioId(funcionario.getId())
                .stream()
                .sorted(Comparator.comparing(RegistroPonto::getDataHora))
                .toList();


        if (!registros.isEmpty()) {
            RegistroPonto ultimo = registros.get(registros.size() - 1);

            if (ultimo.getTipo() == dto.getTipo()) {
                throw new RuntimeException("Você já registrou esse tipo de ponto");
            }
        }

        RegistroPonto registro = RegistroPonto.builder()
                .dataHora(java.time.LocalDateTime.now())
                .tipo(dto.getTipo())
                .funcionario(funcionario)
                .build();

        if (dto.getTipo() == TipoRegistro.SAIDA) {

            if (registros.isEmpty()) {
                throw new RuntimeException("Não existe entrada para registrar saída");
            }

            RegistroPonto entrada = registros.get(registros.size() - 1);

            if (!entrada.isProcessado()) {

                long minutos = Duration
                        .between(entrada.getDataHora(), registro.getDataHora())
                        .toMinutes();

                long saldo = minutos - funcionario.getJornadaMinutos();

                atualizarBancoHoras(funcionario, saldo);

                entrada.setProcessado(true);
                registro.setProcessado(true);

                repository.save(entrada);
            }
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

        return repository.findByFuncionarioId(funcionario.getId())
                .stream()
                .sorted(Comparator.comparing(RegistroPonto::getDataHora))
                .toList();
    }

    public List<RegistroPonto> listarPorFuncionario(Long funcionarioId) {
        return repository.findByFuncionarioId(funcionarioId);
    }

    public void atualizarPonto(AtualizarPontoDTO dto) {

        RegistroPonto registro = repository.findById(dto.getRegistroId())
                .orElseThrow(() -> new RuntimeException("Registro não encontrado"));

        Funcionario funcionario = registro.getFuncionario();

        // 🔧 atualiza o ponto
        registro.setDataHora(dto.getNovaDataHora());
        repository.save(registro);

        // 🔥 recalcula banco de horas
        recalcularBancoHoras(funcionario);
    }

    private void recalcularBancoHoras(Funcionario funcionario) {

        List<RegistroPonto> registros = repository.findByFuncionarioId(funcionario.getId());

        registros.sort(Comparator.comparing(RegistroPonto::getDataHora));

        long totalMinutos = RegistroPontoUtils.calcularMinutosTrabalhados(registros);

        long saldo = totalMinutos - funcionario.getJornadaMinutos();

        BancoHoras banco = bancoHorasRepository
                .findByFuncionarioId(funcionario.getId())
                .orElse(BancoHoras.builder()
                        .funcionario(funcionario)
                        .saldoMinutos(0L)
                        .build());

        banco.setSaldoMinutos(saldo);

        bancoHorasRepository.save(banco);
    }

    public List<RegistroPonto> buscarHoje() {

        var auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        // 🔒 valida autenticação
        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            return List.of(); // retorna vazio ao invés de quebrar
        }

        String email = auth.getName();

        Funcionario funcionario = funcionarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        LocalDate hoje = LocalDate.now();

        return repository.findByFuncionarioIdAndDataHoraBetween(
                        funcionario.getId(),
                        hoje.atStartOfDay(),
                        hoje.atTime(23, 59, 59)
                ).stream()
                .sorted(Comparator.comparing(RegistroPonto::getDataHora))
                .toList();
    }
    public RegistroPonto buscarUltimoRegistro() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Funcionario funcionario = funcionarioRepository.findByEmail(email)
                .orElseThrow();

        return repository
                .findTopByFuncionarioIdOrderByDataHoraDesc(funcionario.getId())
                .orElse(null);
    }
}