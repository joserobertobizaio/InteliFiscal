package br.com.intelifiscal.service.relatorio;

import br.com.intelifiscal.dto.relatorio.FornecedorCompraDTO;
import br.com.intelifiscal.dto.relatorio.ResumoComprasDTO;
import br.com.intelifiscal.repository.relatorio.ResumoComprasRepository;

import java.time.LocalDate;
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
    // RESUMO GERAL POR PERÍODO
    //==================================================

    public ResumoComprasDTO consultarResumo(
            LocalDate dataInicio,
            LocalDate dataFim
    ) {

        return repository.consultarResumo(
                dataInicio,
                dataFim
        );
    }


    //==================================================
    // POR FORNECEDOR
    //==================================================

    public List<FornecedorCompraDTO> consultarPorFornecedor() {

        return repository.consultarPorFornecedor();
    }


    //==================================================
    // POR FORNECEDOR E PERÍODO
    //==================================================

    public List<FornecedorCompraDTO> consultarPorFornecedor(
            LocalDate dataInicio,
            LocalDate dataFim
    ) {

        return repository.consultarPorFornecedor(
                dataInicio,
                dataFim
        );
    }
}