package dio.portifolio.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BancoHorasDTO {

    private Long saldoMinutos;
    private String saldoFormatado;
}