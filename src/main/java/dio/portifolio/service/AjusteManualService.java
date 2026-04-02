package dio.portifolio.service;

import dio.portifolio.dto.AjusteManualDTO;
import dio.portifolio.entity.AjusteManual;
import dio.portifolio.entity.BancoHoras;
import dio.portifolio.entity.Funcionario;
import dio.portifolio.repository.AjusteManualRepository;
import dio.portifolio.repository.BancoHorasRepository;
import dio.portifolio.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AjusteManualService {

    private final AjusteManualRepository ajusteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final BancoHorasRepository bancoHorasRepository;

    public void ajustar(AjusteManualDTO dto) {

        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        AjusteManual ajuste = AjusteManual.builder()
                .data(LocalDate.now())
                .motivo(dto.getMotivo())
                .minutos(dto.getMinutos())
                .funcionario(funcionario)
                .build();

        ajusteRepository.save(ajuste);

        BancoHoras banco = bancoHorasRepository
                .findByFuncionarioId(funcionario.getId())
                .orElse(BancoHoras.builder()
                        .funcionario(funcionario)
                        .saldoMinutos(0L)
                        .build());

        banco.setSaldoMinutos(banco.getSaldoMinutos() + dto.getMinutos());

        bancoHorasRepository.save(banco);
    }
}