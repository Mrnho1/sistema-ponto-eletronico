package dio.portifolio.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ajustes_manuais")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AjusteManual {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String motivo;

    private LocalDateTime dataHoraOriginal;
    private LocalDateTime novaDataHora;

    @Enumerated(EnumType.STRING)
    private TipoRegistro tipo;

    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;
}