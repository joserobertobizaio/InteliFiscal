package br.com.intelifiscal.service;

import br.com.intelifiscal.entity.NFe;
import br.com.intelifiscal.repository.NFeRepository;

import java.util.List;

public class NFeService {

    private final NFeRepository repository =
            new NFeRepository();


    //==================================================
    // SALVAR
    //==================================================

    public Integer salvar(NFe nfe) {

        return repository.salvar(nfe);
    }


    //==================================================
    // EXISTE
    //==================================================

    public boolean existe(String chave) {

        return repository.existePorChave(chave);
    }


    //==================================================
    // BUSCAR ID POR CHAVE
    //==================================================

    public Long buscarIdPorChave(String chave) {

        return repository.buscarIdPorChave(chave);
    }


    //==================================================
    // PESQUISAR
    //==================================================

    public List<NFe> pesquisar(String texto) {

        return repository.pesquisar(texto);
    }


    //==================================================
    // REGISTRAR CANCELAMENTO
    //==================================================

    public void registrarCancelamento(
            String chave,
            String dataCancelamento,
            String protocoloCancelamento,
            String motivoCancelamento) {

        repository.registrarCancelamento(
                chave,
                dataCancelamento,
                protocoloCancelamento,
                motivoCancelamento
        );
    }


    //==================================================
    // ATUALIZAR TIPO
    //==================================================

    public void atualizarTipo(
            Long id,
            String tipo) {

        repository.atualizarTipo(
                id,
                tipo
        );
    }


    //==================================================
    // EXCLUIR NF-e
    //==================================================

    public void excluir(Long id) {

        repository.excluir(id);
    }

}