package dio.portifolio.service;

import dio.portifolio.dto.AjusteManualDTO;
import dio.portifolio.entity.*;
import dio.portifolio.repository.*;
import dio.portifolio.util.RegistroPontoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AjusteManualService {

    private final AjusteManualRepository ajusteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final BancoHorasRepository bancoHorasRepository;
    private final RegistroPontoRepository registroPontoRepository;

    public void ajustar(AjusteManualDTO dto) {

        // 🔍 Busca o registro que será corrigido
        RegistroPonto registro = registroPontoRepository.findById(dto.getRegistroId())
                .orElseThrow(() -> new RuntimeException("Registro não encontrado"));

        Funcionario funcionario = registro.getFuncionario();

        // 💾 Salva histórico do ajuste
        AjusteManual ajuste = AjusteManual.builder()
                .motivo(dto.getMotivo())
                .dataHoraOriginal(registro.getDataHora())
                .novaDataHora(dto.getNovaDataHora())
                .tipo(registro.getTipo())
                .funcionario(funcionario)
                .build();

        ajusteRepository.save(ajuste);

        // 🔧 Atualiza o registro real
        registro.setDataHora(dto.getNovaDataHora());
        registroPontoRepository.save(registro);

        // 🔥 Recalcula banco de horas completo
        recalcularBancoHoras(funcionario);
    }

    // 🔥 MÉTODO PRINCIPAL DE RECÁLCULO
    private void recalcularBancoHoras(Funcionario funcionario) {

        List<RegistroPonto> registros = registroPontoRepository
                .findByFuncionarioId(funcionario.getId());

        // garante ordem cronológica
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
}