package dio.portifolio.service;

import dio.portifolio.dto.RegistroPontoDTO;
import dio.portifolio.entity.Funcionario;
import dio.portifolio.entity.RegistroPonto;
import dio.portifolio.repository.FuncionarioRepository;
import dio.portifolio.repository.RegistroPontoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistroPontoService {

    private final RegistroPontoRepository repository;
    private final FuncionarioRepository funcionarioRepository;

    public RegistroPonto baterPonto(Long funcionarioId, RegistroPontoDTO dto) {

        Funcionario funcionario = funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        List<RegistroPonto> registros = repository.findByFuncionarioId(funcionarioId);


        if (!registros.isEmpty()) {
            RegistroPonto ultimo = registros.get(registros.size() - 1);

            if (ultimo.getTipo() == dto.getTipo()) {
                throw new RuntimeException("Não pode registrar duas vezes seguidas o mesmo tipo");
            }
        }

        RegistroPonto registro = RegistroPonto.builder()
                .dataHora(LocalDateTime.now())
                .tipo(dto.getTipo())
                .funcionario(funcionario)
                .build();

        return repository.save(registro);
    }

    public List<RegistroPonto> listarPorFuncionario(Long funcionarioId) {
        return repository.findByFuncionarioId(funcionarioId);
    }
}