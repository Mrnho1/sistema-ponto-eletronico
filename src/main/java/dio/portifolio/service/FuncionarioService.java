package dio.portifolio.service;

import dio.portifolio.adapter.FuncionarioAdapter;
import dio.portifolio.dto.FuncionarioInputDTO;
import dio.portifolio.dto.FuncionarioOutputDTO;
import dio.portifolio.entity.Funcionario;
import dio.portifolio.exception.BusinessException;
import dio.portifolio.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public FuncionarioOutputDTO criar(FuncionarioInputDTO dto) {

        repository.findByEmail(dto.getEmail())
                .ifPresent(f -> {
                    throw new BusinessException("Email já cadastrado!");
                });


        Funcionario funcionario = FuncionarioAdapter.toEntity(dto);


        funcionario.setSenha(passwordEncoder.encode(dto.getSenha()));


        Funcionario salvo = repository.save(funcionario);

        return FuncionarioAdapter.toDTO(salvo);
    }

    public List<FuncionarioOutputDTO> listar() {
        return repository.findAll()
                .stream()
                .map(FuncionarioAdapter::toDTO)
                .collect(Collectors.toList());
    }
}