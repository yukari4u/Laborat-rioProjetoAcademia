package com.academia.service;

import com.academia.dto.request.PlanoRequest;
import com.academia.dto.response.PlanoResponse;
import com.academia.entity.Plano;
import com.academia.exception.RecursoNaoEncontradoException;
import com.academia.exception.RegraNegocioException;
import com.academia.repository.PlanoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanoService {

    private final PlanoRepository planoRepository;

    // -------------------------------------------------------
    //  Cadastrar Plano
    // -------------------------------------------------------
    @Transactional
    public PlanoResponse cadastrar(PlanoRequest request) {
        validarNomeDuplicado(request.getNome());

        Plano plano = Plano.builder()
                .nome(request.getNome())
                .mensalidade(request.getMensalidade())
                .duracaoMeses(request.getDuracaoMeses())
                .descricao(request.getDescricao())
                .build();

        Plano salvo = planoRepository.save(plano);
        return toResponse(salvo);
    }

    // -------------------------------------------------------
    //  Buscar por ID
    // -------------------------------------------------------
    @Transactional(readOnly = true)
    public PlanoResponse buscarPorId(Long id) {
        Plano plano = buscarEntidadePorId(id);
        return toResponse(plano);
    }

    // -------------------------------------------------------
    //  Listar Todos
    // -------------------------------------------------------
    @Transactional(readOnly = true)
    public List<PlanoResponse> listarTodos() {
        return planoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------
    //  Atualizar Plano
    // -------------------------------------------------------
    @Transactional
    public PlanoResponse atualizar(Long id, PlanoRequest request) {
        Plano plano = buscarEntidadePorId(id);

        // Só valida nome duplicado se o nome foi alterado
        if (!plano.getNome().equalsIgnoreCase(request.getNome())) {
            validarNomeDuplicado(request.getNome());
        }

        plano.setNome(request.getNome());
        plano.setMensalidade(request.getMensalidade());
        plano.setDuracaoMeses(request.getDuracaoMeses());
        plano.setDescricao(request.getDescricao());

        return toResponse(planoRepository.save(plano));
    }

    // -------------------------------------------------------
    //  Excluir Plano
    // -------------------------------------------------------
    @Transactional
    public void excluir(Long id) {
        Plano plano = buscarEntidadePorId(id);

        if (!plano.getAlunos().isEmpty()) {
            throw new RegraNegocioException(
                    "Não é possível excluir o plano pois existem alunos vinculados a ele.");
        }

        planoRepository.delete(plano);
    }

    // -------------------------------------------------------
    //  Métodos internos / auxiliares
    // -------------------------------------------------------

    /** Retorna a entidade ou lança exceção se não encontrada. */
    public Plano buscarEntidadePorId(Long id) {
        return planoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Plano", id));
    }

    /** Valida se já existe um plano com o mesmo nome. */
    private void validarNomeDuplicado(String nome) {
        if (planoRepository.existsByNome(nome)) {
            throw new RegraNegocioException(
                    "Já existe um plano cadastrado com o nome: " + nome);
        }
    }

    /** Converte a entidade para DTO de resposta. */
    private PlanoResponse toResponse(Plano plano) {
        return PlanoResponse.builder()
                .id(plano.getId())
                .nome(plano.getNome())
                .mensalidade(plano.getMensalidade())
                .duracaoMeses(plano.getDuracaoMeses())
                .descricao(plano.getDescricao())
                .totalAlunos(plano.getAlunos() != null ? plano.getAlunos().size() : 0)
                .build();
    }
}
