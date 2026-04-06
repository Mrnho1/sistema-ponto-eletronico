package dio.portifolio.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AtualizarPontoDTO {

    private Long registroId;
    private LocalDateTime novaDataHora;
}