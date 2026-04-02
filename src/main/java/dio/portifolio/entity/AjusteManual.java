package dio.portifolio.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    private LocalDate data;

    private String motivo;

    private Long minutos;

    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;
}