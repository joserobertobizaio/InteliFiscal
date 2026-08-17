package br.com.intelifiscal.service.relatorio;

import br.com.intelifiscal.dto.relatorio.FornecedorCompraDTO;
import br.com.intelifiscal.dto.relatorio.ResumoComprasDTO;
import br.com.intelifiscal.repository.relatorio.ResumoComprasRepository;

import java.util.List;

public class ResumoComprasService {

    private final ResumoComprasRepository repository =
            new ResumoComprasRepository();


    //==================================================
    // RESUMO GERAL
    //==================================================

    public ResumoComprasDTO consultarResumo() {

        return repository.consultarResumo();
    }


    //==================================================
    // POR FORNECEDOR
    //==================================================

    public List<FornecedorCompraDTO> consultarPorFornecedor() {

        return repository.consultarPorFornecedor();
    }
}