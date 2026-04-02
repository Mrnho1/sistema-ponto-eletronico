package dio.portifolio.repository;

import dio.portifolio.entity.RegistroPonto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RegistroPontoRepository extends JpaRepository<RegistroPonto, Long> {

    List<RegistroPonto> findByFuncionarioId(Long funcionarioId);
    List<RegistroPonto> findByFuncionarioIdAndDataHoraBetween(
            Long funcionarioId,
            LocalDateTime inicio,
            LocalDateTime fim
    );
}