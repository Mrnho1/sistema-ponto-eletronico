package dio.portifolio.adapter;


import dio.portifolio.dto.FuncionarioInputDTO;
import dio.portifolio.dto.FuncionarioOutputDTO;
import dio.portifolio.entity.Funcionario;
import dio.portifolio.entity.Role;

public class FuncionarioAdapter {
    // Converte dados Front para Objeto do Banco de Dados
    public static Funcionario toEntity(FuncionarioInputDTO dto) {
        // Cria objeto Funcionário
        return Funcionario.builder()
                //copia dados do dto
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                // o backend define a role
                .role(Role.USER)
                .build();
    }
    // Converte entidade como resposta ao front
    public static FuncionarioOutputDTO toDTO(Funcionario entity) {
        return FuncionarioOutputDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .email(entity.getEmail())
                .role(entity.getRole())
                .build();
    }
}
// Esse adapter faz
// 1. Cria objeto Funcionário
// 2. Copia dados do DTO
// 3. Define a role para usuário
// 5. Converte Entidade como resposta ao front
// 6. Não responde com a senha por segurança