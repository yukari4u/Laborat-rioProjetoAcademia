package com.academia.service;

import com.academia.dto.request.AlunoRequest;
import com.academia.dto.response.AlunoResponse;
import com.academia.entity.Aluno;
import com.academia.entity.Plano;
import com.academia.exception.RecursoNaoEncontradoException;
import com.academia.exception.RegraNegocioException;
import com.academia.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final PlanoService planoService;   // Injeção de Dependência via construtor (Lombok)

    // -------------------------------------------------------
    //  Cadastrar Aluno
    // -------------------------------------------------------
    @Transactional
    public AlunoResponse cadastrar(AlunoRequest request) {
        validarEmailDuplicado(request.getEmail());
        validarCpfDuplicado(request.getCpf());

        // Valida existência do plano (lança RecursoNaoEncontradoException se não existir)
        Plano plano = planoService.buscarEntidadePorId(request.getPlanoId());

        Aluno aluno = Aluno.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .cpf(request.getCpf())
                .dataNascimento(request.getDataNascimento())
                .telefone(request.getTelefone())
                .plano(plano)
                .build();

        Aluno salvo = alunoRepository.save(aluno);
        return toResponse(salvo);
    }

    // -------------------------------------------------------
    //  Buscar por ID
    // -------------------------------------------------------
    @Transactional(readOnly = true)
    public AlunoResponse buscarPorId(Long id) {
        Aluno aluno = buscarEntidadePorId(id);
        return toResponse(aluno);
    }

    // -------------------------------------------------------
    //  Buscar por E-mail
    // -------------------------------------------------------
    @Transactional(readOnly = true)
    public AlunoResponse buscarPorEmail(String email) {
        Aluno aluno = alunoRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Aluno com e-mail " + email + " não encontrado."));
        return toResponse(aluno);
    }

    // -------------------------------------------------------
    //  Listar Todos
    // -------------------------------------------------------
    @Transactional(readOnly = true)
    public List<AlunoResponse> listarTodos() {
        return alunoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------
    //  Listar por Plano
    // -------------------------------------------------------
    @Transactional(readOnly = true)
    public List<AlunoResponse> listarPorPlano(Long planoId) {
        // Valida existência do plano antes de listar
        planoService.buscarEntidadePorId(planoId);

        return alunoRepository.findByPlanoId(planoId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------
    //  Atualizar Aluno
    // -------------------------------------------------------
    @Transactional
    public AlunoResponse atualizar(Long id, AlunoRequest request) {
        Aluno aluno = buscarEntidadePorId(id);

        // Valida e-mail somente se foi alterado
        if (!aluno.getEmail().equalsIgnoreCase(request.getEmail())) {
            validarEmailDuplicado(request.getEmail());
        }

        // Valida CPF somente se foi alterado
        if (!aluno.getCpf().equals(request.getCpf())) {
            validarCpfDuplicado(request.getCpf());
        }

        Plano plano = planoService.buscarEntidadePorId(request.getPlanoId());

        aluno.setNome(request.getNome());
        aluno.setEmail(request.getEmail());
        aluno.setCpf(request.getCpf());
        aluno.setDataNascimento(request.getDataNascimento());
        aluno.setTelefone(request.getTelefone());
        aluno.setPlano(plano);

        return toResponse(alunoRepository.save(aluno));
    }

    // -------------------------------------------------------
    //  Excluir Aluno
    // -------------------------------------------------------
    @Transactional
    public void excluir(Long id) {
        Aluno aluno = buscarEntidadePorId(id);
        alunoRepository.delete(aluno);
    }

    // -------------------------------------------------------
    //  Métodos internos / auxiliares
    // -------------------------------------------------------

    /** Retorna a entidade ou lança exceção se não encontrada. */
    private Aluno buscarEntidadePorId(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno", id));
    }

    /** Valida se já existe um aluno com o mesmo e-mail. */
    private void validarEmailDuplicado(String email) {
        if (alunoRepository.existsByEmail(email)) {
            throw new RegraNegocioException(
                    "Já existe um aluno cadastrado com o e-mail: " + email);
        }
    }

    /** Valida se já existe um aluno com o mesmo CPF. */
    private void validarCpfDuplicado(String cpf) {
        if (alunoRepository.existsByCpf(cpf)) {
            throw new RegraNegocioException(
                    "Já existe um aluno cadastrado com o CPF: " + cpf);
        }
    }

    /** Converte a entidade para DTO de resposta. */
    private AlunoResponse toResponse(Aluno aluno) {
        return AlunoResponse.builder()
                .id(aluno.getId())
                .nome(aluno.getNome())
                .email(aluno.getEmail())
                .cpf(aluno.getCpf())
                .dataNascimento(aluno.getDataNascimento())
                .telefone(aluno.getTelefone())
                .planoId(aluno.getPlano().getId())
                .planoNome(aluno.getPlano().getNome())
                .build();
    }
}
