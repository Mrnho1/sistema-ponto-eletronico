package dio.portifolio.repository;

import dio.portifolio.entity.BancoHoras;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BancoHorasRepository extends JpaRepository<BancoHoras, Long> {

    Optional<BancoHoras> findByFuncionarioId(Long funcionarioId);
}