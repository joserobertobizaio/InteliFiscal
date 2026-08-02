package br.com.intelifiscal.service;

import br.com.intelifiscal.entity.MinhaEmpresa;
import br.com.intelifiscal.repository.MinhaEmpresaRepository;

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

    public boolean ehMinhaEmpresa(String cnpj) {

        return buscarEmpresa()
                .map(emp -> emp.getCnpj().equals(cnpj))
                .orElse(false);

    }

    private final MinhaEmpresaRepository repository;

    /**
     * Construtor padrão.
     */
    public MinhaEmpresaService() {

        this.repository = new MinhaEmpresaRepository();

    }

    /**
     * Verifica se existe empresa cadastrada.
     *
     * @return true caso exista.
     */
    public boolean existeEmpresa() {

        throw new UnsupportedOperationException(
                "Método ainda não implementado."
        );

    }

    /**
     * Retorna a empresa cadastrada.
     *
     * @return empresa cadastrada.
     */

    public Optional<MinhaEmpresa> buscarEmpresa() {

        return repository.buscar();

    }

    /**
     * Salva ou atualiza a empresa.
     *
     * @param empresa empresa a ser persistida.
     */
    public void salvar(MinhaEmpresa empresa) {

        if (repository.existeEmpresa()) {

            repository.atualizar(empresa);

        } else {

            repository.salvar(empresa);

        }

    }

    /**
     * Remove a empresa cadastrada.
     */
    public void excluir() {

        throw new UnsupportedOperationException(
                "Método ainda não implementado."
        );

    }

}