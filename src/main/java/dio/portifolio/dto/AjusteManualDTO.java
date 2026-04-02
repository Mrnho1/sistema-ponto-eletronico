package dio.portifolio.dto;

import lombok.Data;

@Data
public class AjusteManualDTO {
    private Long funcionarioId;
    private Long minutos;
    private String motivo;
}