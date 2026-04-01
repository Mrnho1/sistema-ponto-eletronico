package dio.portifolio.dto;

import lombok.Data;

@Data
public class AuthRequestDTO {
    private String email;
    private String senha;
}