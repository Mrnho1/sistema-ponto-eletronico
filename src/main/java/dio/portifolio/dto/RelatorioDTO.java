package dio.portifolio.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RelatorioDTO {

    private String data;
    private String horasTrabalhadas;
    private String horasEsperadas;
    private String saldo;
}