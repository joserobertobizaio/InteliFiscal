package br.com.intelifiscal.service;

import br.com.intelifiscal.entity.NFe;
import br.com.intelifiscal.repository.NFeRepository;

public class NFeService {

    private final NFeRepository repository =
            new NFeRepository();

    public Integer salvar(NFe nfe) {

        return repository.salvar(nfe);

    }

    public boolean existe(String chave) {

        return repository.existePorChave(chave);

    }

    public Long buscarIdPorChave(String chave) {

        return repository.buscarIdPorChave(chave);
    }

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

}