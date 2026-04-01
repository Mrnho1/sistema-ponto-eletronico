package dio.portifolio.dto;


import dio.portifolio.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FuncionarioOutputDTO {
    private Long id;
    private String nome;
    private String email;
    private Role role;
}