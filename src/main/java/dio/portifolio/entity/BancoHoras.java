package dio.portifolio.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "banco_horas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BancoHoras {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long saldoMinutos; // saldo total em minutos

    @OneToOne
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;
}