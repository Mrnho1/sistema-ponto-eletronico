package dio.portifolio.adapter;


import dio.portifolio.dto.FuncionarioInputDTO;
import dio.portifolio.dto.FuncionarioOutputDTO;
import dio.portifolio.entity.Funcionario;
import dio.portifolio.entity.Role;

public class FuncionarioAdapter {

    public static Funcionario toEntity(FuncionarioInputDTO dto) {
        return Funcionario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .role(Role.USER)
                .build();
    }

    public static FuncionarioOutputDTO toDTO(Funcionario entity) {
        return FuncionarioOutputDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .email(entity.getEmail())
                .role(entity.getRole())
                .build();
    }
}