package dio.portifolio.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AjusteManualDTO {
    private Long funcionarioId;
    private Long minutos;
    private String motivo;
    private Long registroId;
    private LocalDateTime novaDataHora;
}