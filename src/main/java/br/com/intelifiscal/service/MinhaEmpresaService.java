package br.com.intelifiscal.service;

import br.com.intelifiscal.entity.MinhaEmpresa;
import br.com.intelifiscal.repository.MinhaEmpresaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Responsável pelas regras de negócio relacionadas
 * à empresa proprietária do sistema.
 *
 * Esta classe faz a ponte entre a camada de interface
 * e o repositório de persistência.
 *
 * @author José Roberto Bizaio
 */
public class MinhaEmpresaService {

    private final MinhaEmpresaRepository repository;

    /**
     * Construtor padrão.
     */
    public MinhaEmpresaService() {

        this.repository = new MinhaEmpresaRepository();

    }

    /**
     * Verifica se o CNPJ informado pertence a uma das empresas
     * cadastradas no sistema.
     */
    public boolean ehMinhaEmpresa(String cnpj) {

        if (cnpj == null || cnpj.isBlank()) {
            return false;
        }

        return buscarTodas()
                .stream()
                .anyMatch(
                        empresa ->
                                empresa.getCnpj() != null
                                        && empresa.getCnpj().equals(cnpj)
                );
    }

    /**
     * Verifica se existe empresa cadastrada.
     */
    public boolean existeEmpresa() {

        return repository.existeEmpresa();

    }

    /**
     * Retorna a primeira empresa cadastrada.
     * (Mantido por compatibilidade com o restante do sistema.)
     */
    public Optional<MinhaEmpresa> buscarEmpresa() {

        return repository.buscar();

    }

    public Optional<MinhaEmpresa> buscarPorId(Long id) {

        return repository.buscarPorId(id);

    }

    /**
     * Retorna todas as empresas cadastradas.
     */
    public List<MinhaEmpresa> buscarTodas() {

        return repository.buscarTodas();

    }

    /**
     * Insere uma nova empresa.
     */
    public void salvar(MinhaEmpresa empresa) {

        repository.salvar(empresa);

    }

    /**
     * Atualiza uma empresa existente.
     */
    public void atualizar(MinhaEmpresa empresa) {

        repository.atualizar(empresa);

    }

    /**
     * Exclui a empresa.
     */
    public void excluir(Long id) {

        repository.excluir(id);

    }

}