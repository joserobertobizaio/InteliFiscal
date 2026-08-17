package br.com.intelifiscal.service.relatorio;

import br.com.intelifiscal.dto.relatorio.ResumoVendasDTO;
import br.com.intelifiscal.repository.relatorio.ResumoVendasRepository;

public class ResumoVendasService {

    private final ResumoVendasRepository repository =
            new ResumoVendasRepository();


    //==================================================
    // RESUMO GERAL
    //==================================================

    public ResumoVendasDTO consultarResumo() {

        return repository.consultarResumo();
    }
}